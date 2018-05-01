package com.lzy.iml.core;

import android.content.res.Resources;
import android.graphics.Movie;

import com.lzy.iml.cache.ImageCache;
import com.lzy.iml.gif.GifUtil;
import com.lzy.iml.loader.AssetLoader;
import com.lzy.iml.loader.FileLoader;
import com.lzy.iml.loader.HttpLoader;
import com.lzy.iml.loader.Load;
import com.lzy.iml.loader.ResLoader;
import com.lzy.iml.request.BitmapRequest;


/**
 * Created by lizhiyun on 16/5/23.
 */
public class LoadTask implements Runnable {
    public BitmapRequest mRequest;

    public LoadTask(BitmapRequest request) {
        super();
        this.mRequest = request;
    }

    @Override
    public void run() {
        if (!mRequest.checkIfNeedLoad()) return;
        Load load = null;
        switch (mRequest.sourceType) {
            case RES:
                load = new ResLoader();
                break;
            case FILE:
                load = new FileLoader();
                break;
            case ASSERTS:
                load = new AssetLoader();
                break;
            case HTTP:
                load = new HttpLoader();
                break;
        }
        if (load != null) {
            if (mRequest.bitmap == null) {
                load.loadBitmap(mRequest);
                ImageCache.getInstance().putBitmap2Memory(mRequest.getMemoryKey(), mRequest.bitmap);
                mRequest.refreashBitmap();
            }
            Movie movie = load.loadMovie(mRequest);
            if (movie != null) {
                GifUtil.getInstance().getGifDraw(movie, mRequest);
                ImageCache.getInstance().putMovie2Memory(mRequest.getPathKey(), movie);
            }
        }
    }
}
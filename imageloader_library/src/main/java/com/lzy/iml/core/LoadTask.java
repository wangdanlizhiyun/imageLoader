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
import com.lzy.iml.request.BitmapRequestBuilder;


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
        if (mRequest.customLoader != null){
            load = mRequest.customLoader;
        }else {
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
        }
        if (load != null) {
            if (mRequest.bitmap == null) {
                load.loadBitmap(mRequest);
                if (mRequest.diskCacheStrategy != BitmapRequestBuilder.DiskCacheStrategy.NONE){
                    ImageCache.getInstance().putBitmap2Memory(mRequest.getMemoryKey(), mRequest.bitmap);
                }
                if (isCanceled) {
                    return;
                }
                mRequest.refreashBitmap();
            }
            Movie movie = load.loadMovie(mRequest);
            if (movie != null) {
                GifUtil.getInstance().getGifDraw(movie, mRequest);
                if (mRequest.diskCacheStrategy != BitmapRequestBuilder.DiskCacheStrategy.NONE){
                    ImageCache.getInstance().putMovie2Memory(mRequest.getPathKey(), movie);
                }
            }
        }
    }

    public void cancel(){

    }
    private boolean isCanceled;
}
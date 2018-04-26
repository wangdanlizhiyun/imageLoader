package com.lzy.iml.core;

import com.lzy.iml.cache.ImageCache;
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
        mRequest.loadBitmap();
        ImageCache.getInstance().putBitmap2Memory(mRequest.getMemoryKey(), mRequest.bitmap);
        mRequest.refreashBitmap();
    }



}
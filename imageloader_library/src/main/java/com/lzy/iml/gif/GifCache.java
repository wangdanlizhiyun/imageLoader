package com.lzy.iml.gif;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by lizhiyun on 2018/4/27.
 */

public class GifCache {
    private static class InstanceHolder{
        private static final GifCache instance = new GifCache();
    }
    public static GifCache getInstance(){
        return InstanceHolder.instance;
    }

    private LruCache<Integer, GifDraw> mGifCache;
    private GifCache() {
        mGifCache = new LruCache<>(100);
    }

    public void putGifDraw(int hashCode,GifDraw gifDraw){
        mGifCache.put(hashCode,gifDraw);
    }



    public void stopGif(int code){

    }
}

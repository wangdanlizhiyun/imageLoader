package com.lzy.iml.gif;

import android.graphics.Movie;
import android.util.Log;
import android.view.View;

import com.lzy.iml.request.BitmapRequest;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lizhiyun on 2018/4/27.
 */

public class GifUtil {
    private static class InstanceHolder {
        private static final GifUtil instance = new GifUtil();
    }

    public static GifUtil getInstance() {
        return InstanceHolder.instance;
    }

    private GifUtil() {
        gifDrawConcurrentHashMap = new ConcurrentHashMap<>();
    }

    ConcurrentHashMap<Integer, GifDraw> gifDrawConcurrentHashMap;

    public void getGifDraw(Movie movie, BitmapRequest bitmapRequest) {
        if (movie == null || bitmapRequest.view == null || bitmapRequest.view.get() == null || !bitmapRequest.checkIfCanDisplay()) return;
        synchronized (gifDrawConcurrentHashMap) {
            GifDraw gifDraw = gifDrawConcurrentHashMap.get(bitmapRequest.view.get().hashCode());
            if (gifDraw == null) {
                gifDraw = new GifDraw(movie, bitmapRequest);
                gifDrawConcurrentHashMap.put(bitmapRequest.view.get().hashCode(), gifDraw);
                gifDraw.into(bitmapRequest);
            }else {
                stopGif(bitmapRequest.view.get());
            }
        }
    }

    public void stopGif(int code) {
        synchronized (gifDrawConcurrentHashMap) {
            for (GifDraw gifDraw : gifDrawConcurrentHashMap.values()
                    ) {
                if (gifDraw.bitmapRequest.parentCode == code) {
                    gifDraw.onStop();
                }
            }
        }
    }
    public void stopGif(View view) {
        if (view != null){
            synchronized (gifDrawConcurrentHashMap) {
                if (view != null){
                    GifDraw gifDraw = gifDrawConcurrentHashMap.get(view.hashCode());
                    if (gifDraw != null){
                        gifDraw.onStop();
                        gifDrawConcurrentHashMap.remove(view.hashCode());
                    }
                }
            }
        }
    }



    public void startGif(int code) {
        synchronized (gifDrawConcurrentHashMap) {
            for (GifDraw gifDraw : gifDrawConcurrentHashMap.values()
                    ) {
                if (gifDraw.bitmapRequest.parentCode == code) {
                    gifDraw.onStart();
                }
            }
        }
    }
    public void removeGif(int code) {
        synchronized (gifDrawConcurrentHashMap) {
            Iterator iterator = gifDrawConcurrentHashMap.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry entry = (Map.Entry) iterator.next();
                int key = (int) entry.getKey();
                GifDraw gifDraw = (GifDraw) entry.getValue();
                if (gifDraw.bitmapRequest.parentCode == code) {
                    gifDraw.onStop();
                    gifDrawConcurrentHashMap.remove(key);
                }
            }
        }
    }

}

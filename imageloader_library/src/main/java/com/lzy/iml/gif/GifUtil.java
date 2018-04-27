package com.lzy.iml.gif;

import android.graphics.Movie;
import android.util.LruCache;
import android.widget.ImageView;

import com.lzy.iml.request.BitmapRequest;

import java.lang.ref.WeakReference;
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
        if (movie == null || bitmapRequest.view == null || bitmapRequest.view.get() == null) return;
        synchronized (gifDrawConcurrentHashMap) {
            GifDraw gifDraw = gifDrawConcurrentHashMap.get(bitmapRequest.view.get().hashCode());
            if (gifDraw == null) {
                gifDraw = new GifDraw(movie, bitmapRequest);
                gifDrawConcurrentHashMap.put(bitmapRequest.view.get().hashCode(), gifDraw);
            }
            gifDraw.into(bitmapRequest);
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
}

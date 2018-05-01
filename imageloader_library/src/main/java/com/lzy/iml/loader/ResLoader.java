package com.lzy.iml.loader;

import android.graphics.BitmapFactory;
import android.graphics.Movie;

import com.lzy.iml.cache.ImageCache;
import com.lzy.iml.gif.GifUtil;
import com.lzy.iml.request.BitmapRequest;
import com.lzy.iml.util.Util;

import java.io.InputStream;

/**
 * Created by lizhiyun on 2018/5/1.
 */

public class ResLoader implements Load {

    @Override
    public void loadBitmap(final BitmapRequest request) {
        request.loadBitmapFromResource(new Runnable() {
            @Override
            public void run() {
                request.bitmap = BitmapFactory.decodeResource(ImageCache.getInstance().context.getResources(), request.resId, request.options);
            }
        });
    }

    @Override
    public Movie loadMovie(BitmapRequest request) {
        InputStream is = null;
        try {
            is = ImageCache.getInstance().context.getResources().openRawResource(request.resId);
            if (is != null) {
                if (request.view != null && request.view.get() != null) {
                    return Movie.decodeStream(is);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Util.close(is);
        }
        return null;
    }
}

package com.lzy.iml.loader;

import android.graphics.BitmapFactory;
import android.graphics.Movie;

import com.lzy.iml.cache.ImageCache;
import com.lzy.iml.gif.GifUtil;
import com.lzy.iml.request.BitmapRequest;
import com.lzy.iml.util.Util;

import java.io.File;
import java.io.InputStream;

/**
 * Created by lizhiyun on 2018/5/1.
 */

public class FileLoader implements Load {

    @Override
    public void loadBitmap(final BitmapRequest request) {
        if (!new File(request.path).exists()) return;
        request.loadBitmapFromResource(new Runnable() {
            @Override
            public void run() {
                try{
                    request.bitmap = BitmapFactory.decodeFile(request.path, request.options);
                }catch (Exception e){

                }
            }
        });
    }

    @Override
    public Movie loadMovie(BitmapRequest request) {
        if (!new File(request.path).exists()) return null;
        if (request.view != null && request.view.get() != null) {
            return Movie.decodeFile(request.path);
        }
        return null;
    }
}

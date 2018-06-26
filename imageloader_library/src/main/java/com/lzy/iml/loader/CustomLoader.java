package com.lzy.iml.loader;

import android.graphics.Movie;

import com.lzy.iml.cache.ImageCache;
import com.lzy.iml.request.BitmapRequest;
import com.lzy.iml.util.Util;

import java.io.InputStream;

/**
 * Created by lizhiyun on 2018/5/1.
 */

public abstract class CustomLoader implements Load {

    @Override
    public void loadBitmap(final BitmapRequest request) {

    }

    @Override
    public Movie loadMovie(BitmapRequest request) {

        return null;
    }
}

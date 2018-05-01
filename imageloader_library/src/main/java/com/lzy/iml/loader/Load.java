package com.lzy.iml.loader;

import android.graphics.Movie;

import com.lzy.iml.request.BitmapRequest;

/**
 * Created by lizhiyun on 2018/5/1.
 */

public interface Load {
    public void loadBitmap(final BitmapRequest request);
    public Movie loadMovie(final BitmapRequest request);
}

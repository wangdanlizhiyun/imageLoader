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

public class AssetLoader implements Load {

    @Override
    public void loadBitmap(final BitmapRequest request) {
        InputStream is = null;
        try {
            is = ImageCache.getInstance().context.getAssets().open(request.path);
            if (is != null) {
                request.loadBitmapFromIs(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Util.close(is);
        }
    }

    @Override
    public Movie loadMovie(BitmapRequest request) {
        InputStream is = null;
        try {
            is = ImageCache.getInstance().context.getAssets().open(request.path);
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

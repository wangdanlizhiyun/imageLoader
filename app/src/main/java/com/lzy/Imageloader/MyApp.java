package com.lzy.Imageloader;

import android.app.Application;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.lzy.Imageloader.view.FailedDrawable;
import com.lzy.Imageloader.view.LoadingDrawable;
import com.lzy.iml.cache.ImageCache;
import com.lzy.iml.core.Image;


/**
 * Created by lizhiyun on 2018/4/22.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ImageCache.getInstance().init(this,getCacheDir().getAbsolutePath()+"/image");
        Image.placeHolder = new LoadingDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.loading_gray));
        Image.errorDrawable = new FailedDrawable(Color.RED);
    }
}

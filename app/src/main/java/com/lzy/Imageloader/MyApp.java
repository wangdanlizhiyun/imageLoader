package com.lzy.Imageloader;

import android.app.Application;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.lzy.Imageloader.view.FailedDrawable;
import com.lzy.Imageloader.view.LoadingDrawable;
import com.lzy.iml.cache.ImageCache;
import com.lzy.iml.core.ImageLoader;


/**
 * Created by lizhiyun on 2018/4/22.
 */

public class MyApp extends Application {
    public static Application application;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        ImageCache.getInstance().init(this,getCacheDir().getAbsolutePath()+"/image");
        ImageLoader.placeHolder = new LoadingDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.loading_gray));
        ImageLoader.errorDrawable = new FailedDrawable(Color.RED);
    }
}

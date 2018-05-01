package com.lzy.iml.core;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.lzy.iml.lifecycle.EmptyUtil;
import com.lzy.iml.request.BitmapRequestBuilder;

/**
 * Created by lizhiyun on 16/6/7.
 */
public class Image {
    public static BitmapRequestBuilder with(final Context context) {
        return new BitmapRequestBuilder(context.hashCode());
    }

    public static BitmapRequestBuilder with(final Activity activity) {
        EmptyUtil.setLifeCycle(activity);
        return new BitmapRequestBuilder(activity.hashCode());
    }

    public static BitmapRequestBuilder with(final Fragment fragment) {
        EmptyUtil.setLifeCycle(fragment);
        return new BitmapRequestBuilder(fragment.hashCode());
    }
    public static BitmapRequestBuilder with(final android.support.v4.app.Fragment fragment) {
        EmptyUtil.setLifeCycle(fragment);
        return new BitmapRequestBuilder(fragment.hashCode());
    }


    public static Drawable placeHolder;
    public static Drawable errorDrawable;
    public static int ConnectTimeout = 5_000;

}

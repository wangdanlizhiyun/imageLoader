package com.lzy.iml.request;


import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.lzy.iml.R;
import com.lzy.iml.cache.ImageCache;
import com.lzy.iml.gif.GifDraw;
import com.lzy.iml.gif.GifUtil;
import com.lzy.iml.loader.HttpLoader;
import com.lzy.iml.util.FaceUtil;
import com.lzy.iml.util.GaussianBlur;
import com.lzy.iml.util.ImageRotateUtil;
import com.lzy.iml.util.ImageSizeUtil;
import com.lzy.iml.util.Util;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;


public class BitmapRequest {
    public WeakReference<ImageView> view;
    public Bitmap bitmap;
    public int width;
    public int height;
    public Boolean isFirstDown;
    public String path;
    public int resId;
    public long totalSize;
    public SourceType sourceType;
    public Drawable placeHolder;
    public Drawable errorDrawable;
    public Bitmap.Config inPreferredConfig;
    public boolean isFace;
    public int blurSize;
    public int parentCode;
    public BitmapRequestBuilder.DiskCacheStrategy diskCacheStrategy;
    public BitmapFactory.Options options = new BitmapFactory.Options();

    public static enum SourceType {
        FILE, ASSERTS, HTTP, RES
    }

    public BitmapRequest() {
        isFirstDown = false;
    }


    public String getDiskKey() {
        return Util.md5(this.path);
    }

    public String getMemoryKey() {
        if (TextUtils.isEmpty(path)){
            return Util.md5(this.resId + "" + this.width + this.height + isFace+blurSize);
        }else {
            return Util.md5(this.path + this.width + this.height + isFace+blurSize);
        }
    }
    public String getPathKey() {
        if (TextUtils.isEmpty(path)){
            return Util.md5(this.resId+"");
        }else {
            return Util.md5(this.path);
        }
    }


    public Boolean checkIfCanDisplay() {

        if (view == null) return false;
        if (view.get() == null) return false;
        return (view.get().getTag(R.id.tag_url)).equals(getMemoryKey());
    }
    public Boolean checkIfNeedLoad() {
        if (this.view == null) return true;
        if (this.view.get() != null && ((String) this.view.get().getTag(R.id.tag_url)).equals(getMemoryKey())) {
            return true;
        }
        return false;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void displayLoading(Drawable b) {
        if (view == null || view.get() == null) {
            return;
        }
        setBitmap(view.get(), b);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void display() {
        if (view == null || view.get() == null) {
            return;
        }
        if (bitmap != null) {
            setBitmap(view.get(), bitmap);
        } else {
            setBitmap(view.get(), errorDrawable);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setBitmap(ImageView view, Drawable bitmap) {
        if (view == null) {
            return;
        }
        view.setImageDrawable(bitmap);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setBitmap(ImageView view, Bitmap bitmap) {
        if (view == null) {
            return;
        }
        view.setImageBitmap(bitmap);
    }

    public void loadBitmapFromIs(final InputStream is) {
        loadBitmapFromResource(new Runnable() {
            @Override
            public void run() {
                bitmap = BitmapFactory.decodeStream(is, null, options);
            }
        });

    }
//
    public void loadBitmapFromDescriptor(final FileDescriptor fileDescriptor) {
        loadBitmapFromResource(new Runnable() {
            @Override
            public void run() {
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
            }
        });
    }
//
    public void loadBitmapFromResource(Runnable runnable) {
        options.inJustDecodeBounds = true;
        runnable.run();
        options.inSampleSize = ImageSizeUtil.caculateInSampleSize(options, path,
                width, height);
        options.inPreferredConfig = inPreferredConfig;
        options.inJustDecodeBounds = false;
        options.inMutable = true;
        options.inBitmap = ImageCache.getInstance().getReusable(options);
        try {
            runnable.run();
        }catch (Exception e){
            e.printStackTrace();
        }
        if (bitmap == null) {
            options.inBitmap = null;
            runnable.run();
        }
        modify();
    }
//
//
    private void modify() {
        if (bitmap == null) return;
        bitmap = ImageRotateUtil.modifyBitmap(path, bitmap);
        if (isFace) {
            bitmap = FaceUtil.face(bitmap);
        }
        if (blurSize > 0){
            bitmap = new GaussianBlur().blur(bitmap, blurSize);
        }
    }


    public void refreashBitmap() {
        if (!checkIfCanDisplay()) return;
        Message message = Message.obtain();
        message.what = REFRESH;
        message.obj = this;
        sUIHandler.sendMessage(message);
    }

    private static Handler sUIHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            final BitmapRequest request = (BitmapRequest) msg.obj;
            switch (msg.what) {
                case REFRESH:
                    if (request.checkIfCanDisplay()) {
                        request.display();
                    }
                    break;

                default:
                    break;
            }

        }

    };
    public final static int REFRESH = 1;
}

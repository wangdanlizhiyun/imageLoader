package com.lzy.iml.request;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.lzy.iml.R;
import com.lzy.iml.cache.ImageCache;
import com.lzy.iml.core.Image;
import com.lzy.iml.core.LoadTask;
import com.lzy.iml.util.ImageLoaderExecutor;
import com.lzy.iml.util.ImageSizeUtil;

import java.lang.ref.WeakReference;


/**
 * Created by lizhiyun on 16/6/3.
 */
public class BitmapRequestBuilder {
    public WeakReference<View> view;
    public int width;
    public int height;
    public String path;
    public BitmapRequest.SourceType sourceType;
    public int resId;
    public Drawable placeHolder;
    public Drawable errorDrawable;
    public Bitmap.Config inPreferredConfig;
    public boolean isFace;

    int parentCode;

    public BitmapRequestBuilder(int parentCode) {
        this.parentCode = parentCode;
        inPreferredConfig = Bitmap.Config.RGB_565;
        placeHolder = Image.placeHolder;
        errorDrawable = Image.errorDrawable;
    }


    public void into(ImageView view) {
        if (view == null) return;
        BitmapRequest bitmapRequest = new BitmapRequest();
        bitmapRequest.parentCode = parentCode;
        bitmapRequest.path = path;
        bitmapRequest.width = width;
        bitmapRequest.height = height;
        bitmapRequest.sourceType = sourceType;
        bitmapRequest.isFace = isFace;
        bitmapRequest.resId = resId;
        bitmapRequest.placeHolder = placeHolder;
        bitmapRequest.errorDrawable = errorDrawable;
        bitmapRequest.inPreferredConfig = inPreferredConfig;
        bitmapRequest.view = new WeakReference<ImageView>(view);
        loadImage(bitmapRequest);
    }

    /**
     * 识别头像并剪切
     *
     * @return
     */
    public BitmapRequestBuilder face() {
        this.isFace = true;
        return this;
    }

    public BitmapRequestBuilder bitmapConfig(Bitmap.Config inPreferredConfig) {
        this.inPreferredConfig = inPreferredConfig;
        return this;
    }

    public BitmapRequestBuilder size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public BitmapRequestBuilder placeHolder(Drawable placeHolder) {
        this.placeHolder = placeHolder;
        return this;
    }

    public BitmapRequestBuilder errorDrawable(Drawable errorDrawable) {
        this.errorDrawable = errorDrawable;
        return this;
    }

    public BitmapRequestBuilder load(String path) {
        if (TextUtils.isEmpty(path)) return this;
        this.path = path;
        if (path.startsWith("http")) {
            this.sourceType = BitmapRequest.SourceType.HTTP;
        } else if (path.startsWith("assert")) {
            this.sourceType = BitmapRequest.SourceType.ASSERTS;
        } else {
            this.sourceType = BitmapRequest.SourceType.FILE;
        }
        return this;
    }
    public void preLoad(String path) {
        if (TextUtils.isEmpty(path)) return;
        this.path = path;
        if (path.startsWith("http")) {
            this.sourceType = BitmapRequest.SourceType.HTTP;
        }
        into(null);
    }

    public BitmapRequestBuilder loadAsserts(String path) {
        if (TextUtils.isEmpty(path)) return this;
        this.path = path;
        this.sourceType = BitmapRequest.SourceType.ASSERTS;
        return this;
    }

    public BitmapRequestBuilder load(int resId) {
        this.resId = resId;
        this.sourceType = BitmapRequest.SourceType.RES;
        return this;
    }


    public void loadImage(final BitmapRequest request) {
        if (TextUtils.isEmpty(request.getMemoryKey())) return;
        if (Looper.myLooper() == Looper.getMainLooper()) {
            if (request.view == null || request.view.get() == null) {//preload
                final LoadTask task = new LoadTask(request);
                ImageLoaderExecutor.getInstance().execute(task);
            } else {
                request.view.get().post(new Runnable() {
                    @Override
                    public void run() {
                        ImageSizeUtil.getImageViewSize(request);
                        request.view.get().setTag(R.id.tag_url, request.getMemoryKey());
                        Bitmap bitmap = ImageCache.getInstance().getBitmapFromMemory(request);
                        if (bitmap != null) {
                            request.bitmap = bitmap;
                            request.display();
                        } else {
                            request.displayLoading(request.placeHolder);
                        }
                        final LoadTask task = new LoadTask(request);
                        ImageLoaderExecutor.getInstance().execute(task);
                    }
                });

            }
        } else {
            throw new RuntimeException("only run on ui thread");
        }
    }

}

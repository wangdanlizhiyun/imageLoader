package com.lzy.iml.request;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;

import com.lzy.iml.R;
import com.lzy.iml.cache.ImageCache;
import com.lzy.iml.core.ImageLoader;
import com.lzy.iml.core.LoadTask;
import com.lzy.iml.gif.GifUtil;
import com.lzy.iml.loader.CustomLoader;
import com.lzy.iml.util.ImageLoaderExecutor;
import com.lzy.iml.util.ImageSizeUtil;

import java.lang.ref.WeakReference;


/**
 * Created by lizhiyun on 16/6/3.
 */
public class BitmapRequestBuilder {
    private WeakReference<View> view;
    private int width;
    private int height;
    private String path;
    private BitmapRequest.SourceType sourceType;
    private int resId;
    private Drawable placeHolder;
    private Drawable errorDrawable;
    private Bitmap.Config inPreferredConfig;
    private boolean isFace;
    private int blurSize;
    private DiskCacheStrategy diskCacheStrategy;
    private RequestListener requestListener;
    private CustomLoader customLoader;
    private CustomDisplayMethod customDisplayMethod;

    int parentCode;

    public static enum DiskCacheStrategy {
        ALL, MEMORY, NONE;
    }

    public BitmapRequestBuilder(int parentCode) {
        this.parentCode = parentCode;
        inPreferredConfig = Bitmap.Config.RGB_565;
        placeHolder = ImageLoader.placeHolder;
        errorDrawable = ImageLoader.errorDrawable;
        diskCacheStrategy = DiskCacheStrategy.ALL;
    }


    public void into(View view) {
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
        bitmapRequest.diskCacheStrategy = diskCacheStrategy;
        bitmapRequest.view = new WeakReference<View>(view);
        bitmapRequest.requestListener = requestListener;
        bitmapRequest.customLoader = customLoader;
        bitmapRequest.customDisplayMethod = customDisplayMethod;
        loadImage(bitmapRequest);
    }

    public BitmapRequestBuilder customLoader(CustomLoader customLoader) {
        this.customLoader = customLoader;
        return this;
    }

    public BitmapRequestBuilder customDisplayMethod(CustomDisplayMethod customDisplayMethod) {
        this.customDisplayMethod = customDisplayMethod;
        return this;
    }

    public BitmapRequestBuilder requestListener(RequestListener requestListener) {
        this.requestListener = requestListener;
        return this;
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

    public BitmapRequestBuilder blur(int blurSize) {
        this.blurSize = blurSize;
        return this;
    }

    public BitmapRequestBuilder bitmapConfig(Bitmap.Config inPreferredConfig) {
        this.inPreferredConfig = inPreferredConfig;
        return this;
    }

    public BitmapRequestBuilder diskCacheStrategy(DiskCacheStrategy diskCacheStrategy) {
        this.diskCacheStrategy = diskCacheStrategy;
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
                ImageSizeUtil.getImageViewSize(request);
                if (request.view.get() != null) {
                    request.view.get().setTag(R.id.tag_lzy_il_url, request.getMemoryKey());
                } else {
                    return;
                }
                Bitmap bitmap = null;
                if (request.diskCacheStrategy != DiskCacheStrategy.NONE) {
                    bitmap = ImageCache.getInstance().getBitmapFromMemory(request);
                }
                if (bitmap != null) {
                    request.bitmap = bitmap;
                    request.display();
                } else {
                    request.displayLoading(request.placeHolder);
                }

                GifUtil.getInstance().stopGif(request.view.get());
                final LoadTask task = new LoadTask(request);
                ImageLoaderExecutor.getInstance().execute(task);
            }
        } else {
            throw new RuntimeException("only run on ui thread");
        }
    }

}

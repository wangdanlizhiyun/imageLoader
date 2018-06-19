package com.lzy.iml.request;

public interface RequestListener {
    boolean onLoadFailed(Exception e, BitmapRequest request);

    boolean onResourceReady(BitmapRequest request, boolean isFirstResource);
}

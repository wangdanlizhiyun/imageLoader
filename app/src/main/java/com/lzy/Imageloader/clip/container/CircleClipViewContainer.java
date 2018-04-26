package com.lzy.Imageloader.clip.container;

import android.content.Context;
import android.graphics.Path;
import android.support.annotation.NonNull;

/**
 * Created by lizhiyun on 2017/11/8.
 */

public class CircleClipViewContainer extends ClipViewContainer {

    public CircleClipViewContainer(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void setClipPath() {
        this.mFloatRadio = Math.min(mRect.width(), mRect.height()) / 2;
        mPathOuter.addRoundRect(mRect, mFloatRadio, mFloatRadio, Path.Direction.CCW);
    }
}

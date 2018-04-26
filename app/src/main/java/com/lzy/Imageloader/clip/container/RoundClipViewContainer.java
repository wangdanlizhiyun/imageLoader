package com.lzy.Imageloader.clip.container;

import android.content.Context;
import android.graphics.Path;
import android.support.annotation.NonNull;

/**
 * Created by lizhiyun on 2017/11/8.
 */

public class RoundClipViewContainer extends ClipViewContainer {

    public RoundClipViewContainer(@NonNull Context context, float radio) {
        super(context);
        this.mFloatRadio = radio;
    }


    @Override
    protected void setClipPath() {
        mPathOuter.addRoundRect(mRect, mFloatRadio, mFloatRadio, Path.Direction.CCW);
    }
}

package com.lzy.Imageloader.clip.customer;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by lizhiyun on 2017/11/25.
 */

public class ClipHelp {
    Path mPathOuter;
    RectF mRect;

    public ClipHelp(@NonNull final View view, final ClipListener clipListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }else {
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mPathOuter = new Path();
        mRect = new RectF(0, 0, 0, 0);
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                mRect.left = 0;
                mRect.right = right-left;
                mRect.top = 0;
                mRect.bottom = bottom-top;
                mPathOuter.reset();
                if (clipListener != null){
                    clipListener.setClipPath(mPathOuter,mRect);
                }
                view.invalidate();
            }
        });

    }
    void clipCanvas(Canvas canvas){
        canvas.clipPath(mPathOuter);
        canvas.clipRect(new RectF(0, 0, mRect.right-mRect.left, mRect.bottom-mRect.top), Region.Op.INTERSECT);
    }

}

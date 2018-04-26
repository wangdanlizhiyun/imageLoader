package com.lzy.Imageloader.clip.container;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by lizhiyun on 2017/11/8.
 */

public abstract class ClipViewContainer extends FrameLayout {
    float mFloatRadio = 0;
    protected Path mPathOuter;
    protected RectF mRect;
    protected Paint mPaintBorder;
    protected float mBorderWidth = 0;
    protected int mBorderCorlor = Color.TRANSPARENT;

    public ClipViewContainer(@NonNull Context context) {
        super(context);
        init();
    }

    public ClipViewContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClipViewContainer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        setBackgroundColor(Color.TRANSPARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            setLayerType(LAYER_TYPE_HARDWARE, null);
        } else {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        mPaintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBorder.setColor(mBorderCorlor);
        mPaintBorder.setStrokeWidth(mBorderWidth);
        mPaintBorder.setStyle(Paint.Style.STROKE);
        mPathOuter = new Path();
        mRect = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    public ClipViewContainer setBorder(float width, int corlor) {
        mBorderCorlor = corlor;
        mBorderWidth = width * 2;
        mPaintBorder.setColor(mBorderCorlor);
        mPaintBorder.setStrokeWidth(mBorderWidth);
        invalidate();
        return this;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (getChildCount() == 1) {
            View child = getChildAt(0);
            mRect.left = child.getLeft()+child.getPaddingLeft();
            mRect.right = child.getRight()-child.getPaddingRight();
            mRect.top = child.getPaddingTop();
            mRect.bottom = (float) (child.getBottom() - child.getTop()-child.getPaddingBottom());
            mPathOuter.reset();
            setClipPath();
            invalidate();
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(mPathOuter, mPaintBorder);
        canvas.save();
        canvas.clipPath(mPathOuter);
        canvas.clipRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), Region.Op.INTERSECT);
        super.draw(canvas);
        canvas.restore();
    }

    protected abstract void setClipPath();
}

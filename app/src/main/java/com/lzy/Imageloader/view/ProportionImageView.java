package com.lzy.Imageloader.view;

import android.content.Context;
import android.util.AttributeSet;



public class ProportionImageView extends android.support.v7.widget.AppCompatImageView {
    ProportionHelper mProportionHelper = new ProportionHelper();
    public ProportionImageView(Context context) {
        super(context);
        init();
    }

    public ProportionImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProportionImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    public void setProportion(float p) {
        mProportionHelper.mProportion = p;
        requestLayout();
    }

    public void init() {
        mProportionHelper.init(getContentDescription());
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, mProportionHelper.getHeigt(widthMeasureSpec));
    }
}

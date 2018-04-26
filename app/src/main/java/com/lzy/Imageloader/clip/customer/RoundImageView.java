package com.lzy.Imageloader.clip.customer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.lzy.Imageloader.clip.DipValueUtil;
import com.lzy.Imageloader.clip.RoundHelper;


/**
 * Created by lizhiyun on 2017/11/25.
 * 圆角和圆形imageview
 */

public class RoundImageView extends android.support.v7.widget.AppCompatImageView {
    ClipHelp mClipHelp = new ClipHelp(this, new ClipListener() {
        @Override
        public void setClipPath(Path path, RectF rectF) {
            int radio = 0;
            if (RoundHelper.getRonundRatio(getContentDescription()) == Integer.MAX_VALUE){
                radio = getMeasuredHeight()/2;
            }else {
                radio = DipValueUtil.getDipValue(getContext(),RoundHelper.getRonundRatio(getContentDescription()));
            }
            path.addRoundRect(rectF, radio, radio, Path.Direction.CCW);
        }
    });

    public RoundImageView(Context context) {
        super(context);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        mClipHelp.clipCanvas(canvas);
        super.draw(canvas);
        canvas.restore();
    }

}

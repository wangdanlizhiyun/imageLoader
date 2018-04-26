package com.lzy.Imageloader.clip.customer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by lizhiyun on 2017/11/25.
 */

public class RoundRecycleView extends RecyclerView {
    ClipHelp mClipHelp = new ClipHelp(this, new ClipListener() {
        @Override
        public void setClipPath(Path path, RectF rectF) {
            path.addRoundRect(rectF, 5, 50, Path.Direction.CCW);
        }
    });

    public RoundRecycleView(Context context) {
        super(context);
    }

    public RoundRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
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

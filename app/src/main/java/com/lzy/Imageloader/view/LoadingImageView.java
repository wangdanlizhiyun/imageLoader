package com.lzy.Imageloader.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;


public class LoadingImageView extends android.support.v7.widget.AppCompatImageView {
    public LoadingImageView(Context ctx) {
        super(ctx);
    }

    public LoadingImageView(Context ctx, AttributeSet attri) {
        super(ctx, attri);
    }

    public LoadingImageView(Context ctx, AttributeSet attri, int defaultSet) {
        super(ctx, attri, defaultSet);
    }
    private int width, height;

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            if (getDrawable() != null && getDrawable() instanceof LoadingDrawable) {
                if (width == 0)
                    width = getWidth() >> 1;
                if (height == 0)
                    height = getHeight() >> 1;
                canvas.save();
                canvas.rotate((360 - (System.currentTimeMillis() / 3) % 360), width, height);
                getDrawable().draw(canvas);
                canvas.restore();
                postInvalidateDelayed(30);
//                handler.sendMessageDelayed(handler.obtainMessage(),30);
            } else {
                super.onDraw(canvas);
            }
        }catch (Exception e){

        }
    }
    Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacksAndMessages(null);
    }
}
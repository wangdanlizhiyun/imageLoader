package com.lzy.Imageloader.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class LoadingDrawable extends Drawable {
	private Matrix matrix;
	private Rect clipRect;
	private boolean hasBack = false;;

	public LoadingDrawable(Bitmap bitmap) {
		matrix = new Matrix();
		clipRect = new Rect();
		bm = bitmap;
	}

	Bitmap bm = null;

	@Override
	public void draw(Canvas canvas) {
		if (bm == null){
			return;
		}
		Rect rect = getBounds();
		rect.set(0, 0, canvas.getWidth(), canvas.getHeight());
		int startx = (rect.right - rect.left - bm.getWidth()) >> 1;
		int starty = (rect.bottom - rect.top - bm.getHeight()) >> 1;

		if (startx < 0)
			startx = 0;
		if (starty < 0)
			starty = 0;

		float scaleX = 1;
		if (bm.getWidth() > rect.right - rect.left)
			scaleX = (rect.right - rect.left) * 1.0f / bm.getWidth();

		clipRect.set(0, 0, bm.getWidth(), bm.getHeight());
		matrix.reset();
		matrix.postTranslate(startx, starty);
		if (scaleX != 1.0f)
			matrix.postScale(scaleX, scaleX);
		matrix.postRotate(degree, (rect.right - rect.left) >> 1,
				(rect.bottom - rect.top) >> 1);
		canvas.clipRect(rect);
		canvas.drawBitmap(bm, matrix, null);
	}

	@Override
	public void setAlpha(int alpha) {
	}

	private static int degree = 0;

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {

	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}
}

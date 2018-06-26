package com.lzy.iml.util;

import android.content.Context;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.widget.ImageView;

import com.lzy.iml.request.BitmapRequest;

import java.io.InputStream;
import java.lang.reflect.Field;

public class ImageSizeUtil {
    public static void modifyOptions(Options options, String pathName){
        int width = options.outWidth;
        int height = options.outHeight;
        if (height == -1 || width == -1) {
            try {
                ExifInterface exifInterface = new ExifInterface(pathName);
                height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的高度
                width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的宽度
                options.outWidth = width;
                options.outHeight = height;
            } catch (Exception e) {
                Log.v("test","caculateInSampleSize e="+e.getMessage());
                e.printStackTrace();
            }
        }
    }
	/**
	 * 根据需求的宽和高以及图片实际的宽和高计算SampleSize
	 *
	 */
	public static int caculateInSampleSize(Options options, String pathName, int reqWidth,
                                           int reqHeight) {
		int width = options.outWidth;
		int height = options.outHeight;
        if (height == -1 || width == -1) {
            try {
                ExifInterface exifInterface = new ExifInterface(pathName);
                height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的高度
                width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的宽度
                options.outWidth = width;
                options.outHeight = height;
                Log.v("test","caculateInSampleSize options.outWidth="+options.outWidth);
            } catch (Exception e) {
                Log.v("test","caculateInSampleSize e="+e.getMessage());
                e.printStackTrace();
            }
        }

		int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            long totalPixels = width * height / inSampleSize;

            // Anything more than 2x the requested pixels we'll sample down further
            final long totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 2;
            }
        }
		return inSampleSize;
	}
	public static int caculateInSampleSize(Options options, InputStream is, int reqWidth,
                                           int reqHeight) {
		int width = options.outWidth;
		int height = options.outHeight;
        if (height == -1 || width == -1) {
            try {
                ExifInterface exifInterface = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    exifInterface = new ExifInterface(is);
                    height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的高度
                    width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的宽度
                    options.outWidth = width;
                    options.outHeight = height;
                }else {
                    return 2;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

		int inSampleSize = 1;

        if (width > reqWidth && height > reqWidth){
            inSampleSize = 2;
            //循环 使宽、高小于 最大的宽、高
            while (width /inSampleSize > reqWidth && height / inSampleSize > reqWidth){
                inSampleSize *= 2;
            }
        }
		return inSampleSize;
	}


	public static void getImageViewSize(BitmapRequest request) {
		if (request.width > 0 && request.height > 0) {
			return;
		}
		if (request.view == null || request.view.get() == null) {
			return;
		}
        request.width = request.view.get().getMeasuredWidth();
        request.height = request.view.get().getMeasuredHeight();

		if (request.width <= 10){
		    request.width = getScreenWidth(request.view.get().getContext());
        }
		if (request.height <= 10){
		    request.height = getScreenHeight(request.view.get().getContext());
        }



	}
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}

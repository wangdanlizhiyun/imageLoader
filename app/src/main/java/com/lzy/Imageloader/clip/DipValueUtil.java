package com.lzy.Imageloader.clip;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by lizhiyun on 2017/5/12.
 */
public class DipValueUtil {
    public static int getDipValue(Context context, int number) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, number, context.getResources().getDisplayMetrics()) + 0.5);
    }

    public static int getDipValue(Context context, float number) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, number, context.getResources().getDisplayMetrics()) + 0.5);
    }
    public static float getDipValueToFloat(Context context, float number) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, number, context.getResources().getDisplayMetrics());
    }
}

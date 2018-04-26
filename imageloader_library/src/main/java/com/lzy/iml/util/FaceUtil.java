package com.lzy.iml.util;

import android.graphics.Bitmap;

/**
 * Created by lizhiyun on 2018/4/24.
 */

public class FaceUtil {
    static FaceCropper fc = null;
    public static synchronized Bitmap face(Bitmap bitmap){
        try{
            if (fc == null){
                fc = new FaceCropper();
                fc.setDebug(false);
            }
            Bitmap faceBitmap = fc.cropFace(bitmap);
            return faceBitmap;
        }catch (Exception e){
            return bitmap;
        }
    }
}

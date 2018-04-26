package com.lzy.Imageloader.view;

import android.text.TextUtils;
import android.view.View;

/**
 * Created by lizhiyun on 2017/3/22.
 */

public class ProportionHelper {
    public float mProportion = 1.0f;

    //获取不同属性用|分割，前缀p表示宽高比
    public void init(CharSequence charSequence) {
        if (charSequence != null){
            String content = charSequence.toString();
            if (!TextUtils.isEmpty(content) && content.toString().trim().length() > 0) {
                String sContent = content.toString().trim();
                if (sContent.contains("|")) {
                    String[] sContents = sContent.split("|");
                    if (null != sContents && sContents.length > 0) {
                        for (String s : sContents
                                ) {
                            if (!TextUtils.isEmpty(s) && !TextUtils.isEmpty(s.trim())) {
                                if (s.trim().startsWith("p")) {
                                    useContent(sContent);
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    if (sContent.startsWith("p")) {
                        useContent(sContent);
                    }
                }

            }
        }
    }
    private void useContent(String sContent) {
        float proprotion = 0;
        try {
            proprotion = Float.parseFloat(sContent.substring(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (proprotion > 0) {
            mProportion = proprotion;
        }
    }
    public int getHeigt(int widthMeasureSpec){
        return View.MeasureSpec.makeMeasureSpec(
                (int) (View.MeasureSpec.getSize(widthMeasureSpec) * mProportion),
                View.MeasureSpec.getMode(widthMeasureSpec));
    }
}

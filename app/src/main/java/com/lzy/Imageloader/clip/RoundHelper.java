package com.lzy.Imageloader.clip;

import android.text.TextUtils;

import java.util.regex.Pattern;


/**
 * Created by lizhiyun on 2017/3/22.
 */

public class RoundHelper {

    //获取不同属性用|分割，前缀p表示宽高比
    public static int getRonundRatio(CharSequence charSequence) {
        if (charSequence != null){
            String content = charSequence.toString();
            if (!TextUtils.isEmpty(content) && content.toString().trim().length() > 0) {
                String sContent = content.toString().trim();
                if (sContent.contains("|")) {
                    String[] sContents = sContent.split("|");
                    if (null != sContents && sContents.length > 0) {
                        for (String s : sContents
                                ) {
                            int radius = getRoundRatioFromRatioPart(s);
                            if (radius > 0){
                                break;
                            }
                        }
                    }
                } else {
                    return getRoundRatioFromRatioPart(sContent);
                }

            }
        }
        return 0;
    }
    private static int getRoundRatioFromRatioPart(String content){
        int radius = 0;
        if (content.equals("circle")) {
            return Integer.MAX_VALUE;
        }else {
            Pattern patten = Pattern.compile("[0-9]{1,3}");
            if (patten.matcher(content).matches()) {
                try {
                    radius = Integer.parseInt(content);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return radius;
    }

}

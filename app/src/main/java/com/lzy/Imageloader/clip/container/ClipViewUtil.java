package com.lzy.Imageloader.clip.container;

import android.view.View;
import android.view.ViewGroup;

/**
 * 通过包裹一层clipview实现。缺点：在列表中使用绘制性能会明显下降。优点：不需要给不同view自定义，使用方便快捷。
 */

public class ClipViewUtil {
    public static ClipViewContainer clipView(View view,ClipViewContainer clipViewContainer){
        ViewGroup contentParent = (ViewGroup) (view.getParent());
        if (!(contentParent instanceof ClipViewContainer)){
            int childCount = contentParent.getChildCount();
            int index = 0;
            View oldContent = view;
            for (int i = 0; i < childCount; i++) {
                if (contentParent.getChildAt(i) == oldContent) {
                    index = i;
                    break;
                }
            }
            contentParent.removeView(oldContent);
            ViewGroup.LayoutParams lp = oldContent.getLayoutParams();
            contentParent.addView(clipViewContainer, index, lp);
            if (oldContent.getLayoutParams() instanceof ViewGroup.MarginLayoutParams){
                ViewGroup.MarginLayoutParams oldLp = (ViewGroup.MarginLayoutParams) oldContent.getLayoutParams();
                ViewGroup.MarginLayoutParams newLp = new ViewGroup.MarginLayoutParams(oldLp.width,oldLp.height);
                clipViewContainer.addView(oldContent,newLp);
            } else {
                clipViewContainer.addView(oldContent);
            }
            return clipViewContainer;
        }else {
            return (ClipViewContainer) contentParent;
        }

    }
    public static ClipViewContainer clipViewRound(View view ,float radio){
        return clipView(view,new RoundClipViewContainer(view.getContext(),radio));
    }
    public static ClipViewContainer clipViewCircle(View view){
        return clipView(view,new CircleClipViewContainer(view.getContext()));
    }

}
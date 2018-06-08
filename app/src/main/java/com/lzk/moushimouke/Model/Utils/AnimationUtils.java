package com.lzk.moushimouke.Model.Utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.widget.ImageView;

/**
 * Created by huqun on 2018/5/22.
 */

public class AnimationUtils {

    public void alphaAnimation(ImageView o){
        //沿x轴放大
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(o, "scaleX", 1f,1.3f, 1f);
        //沿y轴放大
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(o, "scaleY", 1f, 1.3f, 1f);

        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.play(scaleXAnimator).with(scaleYAnimator);
        animatorSet.setDuration(1000);
        animatorSet.start();
    }
}

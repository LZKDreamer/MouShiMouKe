package com.lzk.moushimouke.View.CustomView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lzk.moushimouke.R;

import cn.bmob.v3.b.V;

/**
 * Created by huqun on 2018/6/28.
 */

public class SmartMenu extends LinearLayout implements View.OnClickListener{
    private RelativeLayout menuAdd;
    private RelativeLayout menuText;
    private RelativeLayout menuGallery;
    private RelativeLayout menuVideo;
    private boolean menuStateFlag=false;

    public SmartMenu(Context context) {
        super(context);
        initView(context);
    }

    public SmartMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.smart_menu_layout,this,true);
        menuAdd=findViewById(R.id.smart_menu_add);
        menuText=findViewById(R.id.smart_menu_text);
        menuGallery=findViewById(R.id.smart_menu_gallery);
        menuVideo=findViewById(R.id.smart_menu_video);
        menuAdd.setOnClickListener(this);
        menuText.setOnClickListener(this);
        menuGallery.setOnClickListener(this);
        menuVideo.setOnClickListener(this);
    }

    public interface OnSmartMenuItemClickListener{
        void onTextClicked();
        void onGalleryClicked();
        void onVideoClicked();
    }

    private OnSmartMenuItemClickListener mListener;

    public void setOnSmartMenuClickListener(OnSmartMenuItemClickListener listener){
        mListener=listener;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.smart_menu_add:
                if (!menuStateFlag){
                    menuStartAnimator();
                }else {
                    menuEndAnimator();
                }
                break;
            case R.id.smart_menu_text:
                if (mListener!=null){
                    menuEndAnimator();
                    mListener.onTextClicked();

                }
                break;
            case R.id.smart_menu_gallery:
                if (mListener!=null){
                    menuEndAnimator();
                    mListener.onGalleryClicked();

                }
                break;
            case R.id.smart_menu_video:
                if (mListener!=null){
                    menuEndAnimator();
                    mListener.onVideoClicked();

                }
                break;
        }
    }

    private void menuStartAnimator(){
        ObjectAnimator animator0=ObjectAnimator.ofFloat(menuAdd,"rotation",135f);
        ObjectAnimator animator1=ObjectAnimator.ofFloat(menuText,"translationY",-20f);
        ObjectAnimator animator2=ObjectAnimator.ofFloat(menuGallery,"translationY",-20f);
        ObjectAnimator animator3=ObjectAnimator.ofFloat(menuVideo,"translationY",-20f);

        ObjectAnimator animator4=ObjectAnimator.ofFloat(menuText,"alpha",0f,1f);
        ObjectAnimator animator5=ObjectAnimator.ofFloat(menuGallery,"alpha",0f,1f);
        ObjectAnimator animator6=ObjectAnimator.ofFloat(menuVideo,"alpha",0f,1f);

        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.setDuration(800);
        animatorSet.setInterpolator(new OvershootInterpolator());
        animatorSet.playTogether(animator0,animator1,animator2,animator3,animator4,animator5,animator6);
        animatorSet.start();

        menuText.setVisibility(VISIBLE);
        menuGallery.setVisibility(VISIBLE);
        menuVideo.setVisibility(VISIBLE);

        menuStateFlag=!menuStateFlag;

    }

    private void menuEndAnimator(){
        ObjectAnimator animator0=ObjectAnimator.ofFloat(menuAdd,"rotation",0f);
        ObjectAnimator animator1=ObjectAnimator.ofFloat(menuText,"translationY",60f);
        ObjectAnimator animator2=ObjectAnimator.ofFloat(menuGallery,"translationY",60f);
        ObjectAnimator animator3=ObjectAnimator.ofFloat(menuVideo,"translationY",60f);

        ObjectAnimator animator4=ObjectAnimator.ofFloat(menuText,"alpha",0f);
        ObjectAnimator animator5=ObjectAnimator.ofFloat(menuGallery,"alpha",0f);
        ObjectAnimator animator6=ObjectAnimator.ofFloat(menuVideo,"alpha",0f);

        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new OvershootInterpolator());
        animatorSet.playTogether(animator0,animator1,animator2,animator3,animator4,animator5,animator6);
        animatorSet.start();

        menuStateFlag=!menuStateFlag;
    }

}

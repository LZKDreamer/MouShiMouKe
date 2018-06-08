package com.lzk.moushimouke.View.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzk.moushimouke.MyApplication;
import com.lzk.moushimouke.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by huqun on 2018/6/1.
 */

public class ViewPortraitDialogFragment extends DialogFragment {
    public static final String VIEW_PORTRAIT = "view_portrait";
    @BindView(R.id.view_portrait_dialog_img)
    ImageView mViewPortraitDialogImg;
    Unbinder unbinder;

    public static ViewPortraitDialogFragment newInstance(String portraitUrl) {
        Bundle bundle = new Bundle();
        bundle.putString(VIEW_PORTRAIT, portraitUrl);
        ViewPortraitDialogFragment fragment = new ViewPortraitDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Black_NoTitleBar);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_portrait_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String portraitUrl=getArguments().getString(VIEW_PORTRAIT);
        if (portraitUrl!=null){
            Glide.with(MyApplication.getContext()).load(portraitUrl).into(mViewPortraitDialogImg);
        }else {
            Glide.with(MyApplication.getContext()).load(R.drawable.ic_user_portrait_default).into(mViewPortraitDialogImg);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

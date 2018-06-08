package com.lzk.moushimouke.View.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzk.moushimouke.Model.Bean.EditPhotoItemBean;
import com.lzk.moushimouke.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by huqun on 2018/4/27.
 */

public class ViewPictureDialogFragment extends DialogFragment {
    public static final String PICTURE_DIALOG_BUNDLE_PHOTO="pictureData";
    public static final String PICTURE_DIALOG_BUNDLE_POSITION="picturePosition";
    @BindView(R.id.view_picture_view_pager)
    ViewPager mViewPictureViewPager;
    @BindView(R.id.view_picture_dialog_count)
    TextView mViewPictureDialogCount;
    Unbinder unbinder;
    private List<EditPhotoItemBean> mPhotoList;
    private List<View> mViews;
    private ViewPicturePagerAdapter mPagerAdapter;
    public static ViewPictureDialogFragment sDialogFragment;


    public static ViewPictureDialogFragment newInstance(List<EditPhotoItemBean> itemDataList, int position){
        Bundle args=new Bundle();
        args.putSerializable(PICTURE_DIALOG_BUNDLE_PHOTO, (Serializable) itemDataList);
        args.putInt(PICTURE_DIALOG_BUNDLE_POSITION,position);
        ViewPictureDialogFragment pictureDialogFragment=new ViewPictureDialogFragment();
        pictureDialogFragment.setArguments(args);
        return pictureDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Black_NoTitleBar);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgament_view_picture_dialog, null);
        unbinder = ButterKnife.bind(this, view);
        sDialogFragment=this;
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPhotoList= (List<EditPhotoItemBean>) getArguments().getSerializable(PICTURE_DIALOG_BUNDLE_PHOTO);
        mViews=new ArrayList<>();
        ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i=0;i<mPhotoList.size();i++){
            EditPhotoItemBean itemData=mPhotoList.get(i);
            ImageView imageView=new ImageView(getActivity());
            imageView.setLayoutParams(layoutParams);
            Glide.with(getActivity()).load(itemData.getImagePath()).into(imageView);
            mViews.add(imageView);
        }
        mPagerAdapter=new ViewPicturePagerAdapter(mViews);
        mViewPictureViewPager.setAdapter(mPagerAdapter);
        int curPosition=getArguments().getInt(PICTURE_DIALOG_BUNDLE_POSITION);
        mViewPictureViewPager.setCurrentItem(curPosition);
        int photoPosition=curPosition+1;
        if (mPhotoList.size()>1){
            mViewPictureDialogCount.setText(photoPosition+"/"+mPhotoList.size());
        }else {
            mViewPictureDialogCount.setVisibility(View.GONE);
        }

        mViewPictureViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int scrolledPosition=position+1;
                mViewPictureDialogCount.setText(scrolledPosition+"/"+mPhotoList.size());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class ViewPicturePagerAdapter extends PagerAdapter{
        private List<View> mViewList;

        public ViewPicturePagerAdapter(List<View> views){
            mViewList=views;
        }

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }
    }

}

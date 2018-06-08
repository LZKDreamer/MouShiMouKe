package com.lzk.moushimouke.Model.Adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.lzk.moushimouke.Model.Bean.EditPhotoItemBean;
import com.lzk.moushimouke.MyApplication;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Activity.MainActivity;
import com.lzk.moushimouke.View.Fragment.ExploreFragment;
import com.lzk.moushimouke.View.Fragment.ViewPictureDialogFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by huqun on 2018/5/8.
 */

public class ExploreNinePhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int SINGLE_PHOTO=1;
    private final int MULTI_PHOTO=2;
    private List<BmobFile> mFileList;
    private FragmentManager mFragmentManager;

    class MultiViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.explore_recycler_item_photo)
        ImageView mExploreRecyclerItemPhoto;
        public MultiViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class SingleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.explore_recycler_item_photo)
        ImageView mExploreRecyclerItemPhoto;
        public SingleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public ExploreNinePhotoAdapter(List<BmobFile> fileList,FragmentManager fragmentManager) {
        mFileList = fileList;
        mFragmentManager=fragmentManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==SINGLE_PHOTO){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_explore_recycler_item_single_photo, parent, false);
            SingleViewHolder singleViewHolder = new SingleViewHolder(view);
            return singleViewHolder;
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_explore_recycler_item_photo, parent, false);
            MultiViewHolder holder = new MultiViewHolder(view);
            return holder;
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MultiViewHolder) {
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.image_placeholder)
                    .priority(Priority.HIGH);
            Glide.with(MyApplication.getContext())
                    .load(mFileList.get(position).getUrl())
                    .thumbnail(0.5f)
                    .apply(options)
                    .into(((MultiViewHolder) holder).mExploreRecyclerItemPhoto);
            ((MultiViewHolder) holder).mExploreRecyclerItemPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<EditPhotoItemBean> list = new ArrayList<>();
                    for (BmobFile file : mFileList) {
                        EditPhotoItemBean itemBean = new EditPhotoItemBean(file.getUrl());
                        list.add(itemBean);
                    }
                    FragmentManager manager =mFragmentManager;
                    ViewPictureDialogFragment pictureDialogFragment = ViewPictureDialogFragment.newInstance(list, position);
                    pictureDialogFragment.show(manager, "");
                }
            });
        }else if (holder instanceof SingleViewHolder){
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.image_placeholder)
                    .priority(Priority.HIGH);
            Glide.with(MyApplication.getContext())
                    .load(mFileList.get(position).getUrl())
                    .thumbnail(0.5f)
                    .apply(options)
                    .into(((SingleViewHolder) holder).mExploreRecyclerItemPhoto);
            ((SingleViewHolder) holder).mExploreRecyclerItemPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<EditPhotoItemBean> list = new ArrayList<>();
                    for (BmobFile file : mFileList) {
                        EditPhotoItemBean itemBean = new EditPhotoItemBean(file.getUrl());
                        list.add(itemBean);
                    }
                    FragmentManager manager =mFragmentManager;
                    ViewPictureDialogFragment pictureDialogFragment = ViewPictureDialogFragment.newInstance(list, position);
                    pictureDialogFragment.show(manager, "");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mFileList.size()==1){
            return SINGLE_PHOTO;
        }else {
            return MULTI_PHOTO;
        }

    }
}

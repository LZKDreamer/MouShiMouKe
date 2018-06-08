package com.lzk.moushimouke.Model.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.lzk.moushimouke.Model.Bean.EditPhotoItemBean;
import com.lzk.moushimouke.MyApplication;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Activity.EditPhotoActivity;
import com.lzk.moushimouke.View.Fragment.ViewPictureDialogFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huqun on 2018/4/27.
 */

public class EditItemAdapter extends RecyclerView.Adapter<EditItemAdapter.ViewHolder> {
    private List<EditPhotoItemBean> mDataList;
    private android.support.v4.app.FragmentManager mFragmentManager;
    public static final String DIALOG_PHOTO="DialogPhoto";

    public EditItemAdapter(List<EditPhotoItemBean> itemDataList) {
        mDataList = itemDataList;
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.edit_item_img)
        ImageView mEditItemImg;
        @BindView(R.id.edit_item_delete)
        ImageView mEditItemDelete;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_edit_recycler_view_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (mDataList.size()<9&&position==mDataList.size()){
            Glide.with(MyApplication.getContext())
                    .load(R.drawable.ic_add_photo)
                    .into(holder.mEditItemImg);
            holder.mEditItemDelete.setVisibility(View.INVISIBLE);
        }else {
            holder.mEditItemDelete.setVisibility(View.VISIBLE);
            EditPhotoItemBean itemData=mDataList.get(position);
            RequestOptions options=new RequestOptions();
            options.placeholder(R.drawable.image_placeholder)
                    .priority(Priority.HIGH);
            Glide.with(MyApplication.getContext())
                    .load(itemData.getImagePath())
                    .thumbnail(0.5f)
                    .apply(options)
                    .into(holder.mEditItemImg);
        }
        holder.mEditItemImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDataList.size()<9&&position==mDataList.size()){
                    PictureSelector.create(EditPhotoActivity.sEditPhotoActivity)
                            .openGallery(PictureMimeType.ofImage())
                            .maxSelectNum(9-mDataList.size())
                            .imageSpanCount(3)
                            .selectionMode(PictureConfig.MULTIPLE)
                            .previewImage(true)
                            .isCamera(false)
                            .sizeMultiplier(0.5f)
                            .isGif(false)
                            .previewEggs(true)
                            .forResult(PictureConfig.CHOOSE_REQUEST);
                }else {
                    mFragmentManager = EditPhotoActivity.sEditPhotoActivity.getSupportFragmentManager();
                    ViewPictureDialogFragment pictureDialogFragment = ViewPictureDialogFragment.newInstance(mDataList, position);
                    pictureDialogFragment.show(mFragmentManager, DIALOG_PHOTO);
                }


            }
        });
        holder.mEditItemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataList.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDataList.size()<9){
            return mDataList.size()+1;
        }else {
            return mDataList.size();
        }
    }

}

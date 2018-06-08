package com.lzk.moushimouke.Model.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzk.moushimouke.Model.Bean.Follow;
import com.lzk.moushimouke.Model.Bean.FollowStateBean;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.MyApplication;
import com.lzk.moushimouke.Presenter.FollowActivityPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Activity.HomePageActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by huqun on 2018/6/3.
 */

public class FollowActivityAdapter extends RecyclerView.Adapter<FollowActivityAdapter.ViewHolder> {

    private List<Follow> mFollowList;
    private List<Post> mPostList;
    private List<FollowStateBean> mStateBeanList;
    private FollowActivityPresenter mPresenter;
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.follow_item_portrait)
        CircleImageView mFollowItemPortrait;
        @BindView(R.id.follow_item_username)
        TextView mFollowItemUsername;
        @BindView(R.id.follow_item_profile)
        TextView mFollowItemProfile;
        @BindView(R.id.follow_item_post_text)
        TextView mFollowItemPostText;
        @BindView(R.id.follow_item_follow_btn)
        Button mFollowItemFollowBtn;
        @BindView(R.id.follow_item_ll)
        LinearLayout mFollowItemLl;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            mPresenter=new FollowActivityPresenter();
        }
    }

    public FollowActivityAdapter(List<Follow> followList,List<Post> postList) {
        mFollowList = followList;
        mPostList=postList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(MyApplication.getContext())
                .inflate(R.layout.activity_follow_recycler_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.mFollowItemFollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStateBeanList.get(holder.getAdapterPosition()).isState()){
                    mStateBeanList.get(holder.getAdapterPosition()).setState(false);
                    holder.mFollowItemFollowBtn.setText(MyApplication.getContext().getString(R.string.follow));
                    mPresenter.requestUpdateFollowState(false,mFollowList.get(holder.getAdapterPosition()).getFollowUser());
                }else {
                    mStateBeanList.get(holder.getAdapterPosition()).setState(true);
                    holder.mFollowItemFollowBtn.setText(MyApplication.getContext().getString(R.string.following));
                    mPresenter.requestUpdateFollowState(true,mFollowList.get(holder.getAdapterPosition()).getFollowUser());
                }
            }
        });

        holder.mFollowItemLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= HomePageActivity.newIntent(MyApplication.getContext(),mFollowList.get(holder.getAdapterPosition()).getFollowUser());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Follow follow=mFollowList.get(position);
        String portraitUrl=follow.getFollowUser().getPortrait();
        if (portraitUrl!=null){
            Glide.with(MyApplication.getContext()).load(portraitUrl).thumbnail(0.5f).into(holder.mFollowItemPortrait);
        }else {
            Glide.with(MyApplication.getContext()).load(R.drawable.ic_user_portrait_default).thumbnail(0.5f).into(holder.mFollowItemPortrait);
        }

        holder.mFollowItemUsername.setText(follow.getFollowUser().getUsername());

        String profile=follow.getFollowUser().getProfile();
        if (profile!=null){
            holder.mFollowItemProfile.setText(profile);
        }

        if (mPostList!=null){
            String followUserId=follow.getFollowUser().getObjectId();
            for (Post post:mPostList){

                if (post.getUser().getObjectId().equals(followUserId)){
                    String description=post.getDescription();
                    holder.mFollowItemPostText.setText(description);
                    break;
                }else {
                    continue;
                }
            }
        }

        holder.mFollowItemFollowBtn.setText(MyApplication.getContext().getString(R.string.following));
    }

    @Override
    public int getItemCount() {
        for (int i=0;i<mFollowList.size();i++){
            FollowStateBean followStateBean=new FollowStateBean();
            followStateBean.setState(true);
            mStateBeanList=new ArrayList<>();
            mStateBeanList.add(followStateBean);
        }
        return mFollowList.size();
    }
}

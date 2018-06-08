package com.lzk.moushimouke.Model.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzk.moushimouke.Model.Bean.Follow;
import com.lzk.moushimouke.Model.Bean.FollowStateBean;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.MyApplication;
import com.lzk.moushimouke.Presenter.ExploreItemPresenter;
import com.lzk.moushimouke.Presenter.FollowerActivityPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Activity.FollowerActivity;
import com.lzk.moushimouke.View.Activity.HomePageActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by huqun on 2018/6/4.
 */

public class FollowerActivityAdapter extends RecyclerView.Adapter<FollowerActivityAdapter.ViewHolder> {

    private List<Follow> mFollowerList;
    private List<Post> mPostList;
    private List<Follow> mFollowList;
    private List<String> mIdList;
    private List<FollowStateBean> mStateBeanList;
    private FollowerActivityPresenter mPresenter;



    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.follower_item_portrait)
        CircleImageView mFollowerItemPortrait;
        @BindView(R.id.follower_item_username)
        TextView mFollowerItemUsername;
        @BindView(R.id.follower_item_profile)
        TextView mFollowerItemProfile;
        @BindView(R.id.follower_item_post_text)
        TextView mFollowerItemPostText;
        @BindView(R.id.follower_item_follow_btn)
        Button mFollowerItemFollowBtn;
        @BindView(R.id.follower_item_ll)
        LinearLayout mFollowerItemLl;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public FollowerActivityAdapter(List<Follow> followerList, List<Post> postList,List<Follow> followList) {
        mFollowerList = followerList;
        mPostList = postList;
        mFollowList=followList;
        mIdList=new ArrayList<>();
        mStateBeanList=new ArrayList<>();
        mPresenter=new FollowerActivityPresenter();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_follower_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.mFollowerItemFollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStateBeanList.get(holder.getAdapterPosition()).isState()){
                    mStateBeanList.get(holder.getAdapterPosition()).setState(false);
                    holder.mFollowerItemFollowBtn.setText(MyApplication.getContext().getString(R.string.follow));
                    mPresenter.requestUpdateFollow(false,mFollowerList.get(holder.getAdapterPosition()).getUser());
                }else {
                    mStateBeanList.get(holder.getAdapterPosition()).setState(true);
                    holder.mFollowerItemFollowBtn.setText(MyApplication.getContext().getString(R.string.following));
                    mPresenter.requestUpdateFollow(true,mFollowerList.get(holder.getAdapterPosition()).getUser());
                }
            }
        });

        holder.mFollowerItemLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=HomePageActivity.newIntent(MyApplication.getContext(),mFollowerList.get(holder.getAdapterPosition()).getUser());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String portraitUrl=mFollowerList.get(position).getUser().getPortrait();
        if (portraitUrl!=null){
            Glide.with(MyApplication.getContext()).load(portraitUrl).thumbnail(0.5f).into(holder.mFollowerItemPortrait);
        }else {
            Glide.with(MyApplication.getContext()).load(R.drawable.ic_user_portrait_default).thumbnail(0.5f).into(holder.mFollowerItemPortrait);
        }

        holder.mFollowerItemUsername.setText(mFollowerList.get(position).getUser().getUsername());

        String profile=mFollowerList.get(position).getUser().getProfile();
        if (profile!=null){
            holder.mFollowerItemProfile.setText(profile);
        }

        if (mPostList!=null){
            String id=mFollowerList.get(position).getUser().getObjectId();
            for (Post post:mPostList){
                if (post.getUser().getObjectId().equals(id)){
                    holder.mFollowerItemPostText.setText(post.getDescription());
                    break;
                }else {
                    continue;
                }
            }
        }

        holder.mFollowerItemFollowBtn.setVisibility(View.VISIBLE);
        if (mIdList!=null&&mIdList.size()!=0){
            if (mIdList.contains(mFollowerList.get(position).getUser().getObjectId())){
                FollowStateBean stateBean=new FollowStateBean();
                stateBean.setState(true);
                mStateBeanList.add(stateBean);
                holder.mFollowerItemFollowBtn.setText(MyApplication.getContext().getString(R.string.following));
            }else {
                FollowStateBean stateBean=new FollowStateBean();
                stateBean.setState(false);
                mStateBeanList.add(stateBean);
                holder.mFollowerItemFollowBtn.setText(MyApplication.getContext().getString(R.string.follow));
            }
        }else {
            FollowStateBean stateBean=new FollowStateBean();
            stateBean.setState(false);
            mStateBeanList.add(stateBean);
            holder.mFollowerItemFollowBtn.setText(MyApplication.getContext().getString(R.string.follow));
        }


    }

    @Override
    public int getItemCount() {

        if (mFollowList!=null&&mFollowList.size()!=0){
            for (Follow follow:mFollowList){
                mIdList.add(follow.getFollowUser().getObjectId());
            }
        }

        return mFollowerList.size();
    }
}

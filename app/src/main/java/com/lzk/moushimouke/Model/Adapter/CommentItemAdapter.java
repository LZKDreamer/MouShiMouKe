package com.lzk.moushimouke.Model.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzk.moushimouke.Model.Bean.Comment;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Utils.GetTimeUtils;
import com.lzk.moushimouke.Model.Utils.IsNetWorkConnectedUtils;
import com.lzk.moushimouke.MyApplication;
import com.lzk.moushimouke.Presenter.CommentPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Activity.CommentReplyActivity;
import com.lzk.moushimouke.View.Activity.HomePageActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by huqun on 2018/5/22.
 */

public class CommentItemAdapter extends RecyclerView.Adapter<CommentItemAdapter.ViewHolder> {

    private List<Comment> mCommentList;
    private boolean[] likeState;
    private CommentPresenter mCommentPresenter;


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.comment_recycler_comment_portrait)
        CircleImageView mCommentRecyclerCommentPortrait;
        @BindView(R.id.comment_recycler_comment_user)
        TextView mCommentRecyclerCommentUser;
        @BindView(R.id.comment_recycler_comment_text)
        TextView mCommentRecyclerCommentText;
        @BindView(R.id.comment_recycler_time)
        TextView mCommentRecyclerTime;
        @BindView(R.id.comment_recycler_comment)
        ImageView mCommentRecyclerComment;
        @BindView(R.id.comment_recycler_like)
        ImageView mCommentRecyclerLike;
        @BindView(R.id.comment_recycler_like_num)
        TextView mCommentRecyclerLikeNum;
        @BindView(R.id.comment_recycler_comment_num)
        TextView mCommentRecyclerCommentNum;
        @BindView(R.id.comment_recycler_comment_ll)
        LinearLayout mCommentRecyclerCommentLl;
        @BindView(R.id.comment_recycler_like_ll)
        LinearLayout mCommentRecyclerLikeLl;
        @BindView(R.id.comment_recycler_bottom_ll)
        LinearLayout mCommentRecyclerBottomLl;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public CommentItemAdapter(List<Comment> commentList) {
        mCommentList = commentList;
        likeState = new boolean[mCommentList.size()];
        for (int i = 0; i < mCommentList.size(); i++) {
            likeState[i] = false;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_comment_recycler_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.mCommentRecyclerCommentPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUser user = mCommentList.get(holder.getAdapterPosition()).getUser();
                startHomePageActivity(MyApplication.getContext(), user);
            }
        });

        holder.mCommentRecyclerCommentUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUser user = mCommentList.get(holder.getAdapterPosition()).getUser();
                startHomePageActivity(MyApplication.getContext(), user);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Comment comment = mCommentList.get(position);
        final MyUser user = comment.getUser();
        if (position == 0) {
            String portrait = comment.getUser().getPortrait();
            if (portrait != null) {
                Glide.with(MyApplication.getContext()).load(portrait).thumbnail(0.5f).into(holder.mCommentRecyclerCommentPortrait);
            } else {
                Glide.with(MyApplication.getContext()).load(R.drawable.ic_user_portrait_default).thumbnail(0.5f).into(holder.mCommentRecyclerCommentPortrait);
            }

            holder.mCommentRecyclerCommentUser.setText(comment.getUser().getUsername());
            String description = comment.getComment();
            if (description != null) {
                holder.mCommentRecyclerCommentText.setText(description);
            }
            holder.mCommentRecyclerBottomLl.setVisibility(View.GONE);
        }else {
            holder.mCommentRecyclerBottomLl.setVisibility(View.VISIBLE);
            String portraitUrl = user.getPortrait();
            String userName = user.getUsername();
            String content = comment.getComment();
            String time = new GetTimeUtils().formatTime(comment.getCreatedAt());
            if (portraitUrl != null) {
                Glide.with(MyApplication.getContext()).load(portraitUrl).thumbnail(0.5f).into(holder.mCommentRecyclerCommentPortrait);
            } else {
                Glide.with(MyApplication.getContext()).load(R.drawable.ic_user_portrait_default).thumbnail(0.5f).into(holder.mCommentRecyclerCommentPortrait);
            }
            holder.mCommentRecyclerCommentUser.setText(userName);
            holder.mCommentRecyclerCommentText.setText(content);
            holder.mCommentRecyclerTime.setText(time);
            if (comment.getLike() != 0) {
                int likeNum = comment.getLike();
                holder.mCommentRecyclerLikeNum.setText(String.valueOf(likeNum));
            } else {
                holder.mCommentRecyclerLikeNum.setText(MyApplication.getContext().getString(R.string.like));
            }

            if (comment.getCommentNum() != 0) {
                holder.mCommentRecyclerCommentNum.setText(String.valueOf(comment.getCommentNum()));
            } else {
                holder.mCommentRecyclerCommentNum.setText(MyApplication.getContext().getString(R.string.comment));
            }

        }


        holder.mCommentRecyclerCommentLl.setOnClickListener(new View.OnClickListener() {
            IsNetWorkConnectedUtils connectedUtils = new IsNetWorkConnectedUtils();

            @Override
            public void onClick(View view) {
                boolean state = connectedUtils.IsNetWorkConnected(MyApplication.getContext());
                Intent intent;
                if (state) {
                    if (comment.getCommentNum() != null) {
                        intent = CommentReplyActivity.newIntent(MyApplication.getContext(), comment.getCommentNum(), mCommentList.get(position).getObjectId(), user,mCommentList.get(position).getComment());
                    } else {
                        intent = CommentReplyActivity.newIntent(MyApplication.getContext(), 0, mCommentList.get(position).getObjectId(), user,mCommentList.get(position).getComment());
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getContext().startActivity(intent);
                } else {
                    createSnackBar(holder.mCommentRecyclerComment, MyApplication.getContext().getString(R.string.network_error));
                }

            }
        });


        holder.mCommentRecyclerLikeLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsNetWorkConnectedUtils connectedUtils = new IsNetWorkConnectedUtils();
                boolean state = connectedUtils.IsNetWorkConnected(MyApplication.getContext());
                if (state) {
                    if (likeState[position]) {//已点赞
                        likeState[position] = false;
                        holder.mCommentRecyclerLike.setImageResource(R.drawable.ic_like_nor);
                        int num = mCommentList.get(position).getLike() - 1;
                        holder.mCommentRecyclerLikeNum.setText(num + "");
                        mCommentList.get(position).setLike(num);
                        mCommentPresenter = new CommentPresenter();
                        mCommentPresenter.requestUpdateCommentLikeNum(mCommentList.get(position).getObjectId(), -1);
                    } else {
                        likeState[position] = true;
                        holder.mCommentRecyclerLike.setImageResource(R.drawable.ic_like_select);
                        int num = mCommentList.get(position).getLike() + 1;
                        holder.mCommentRecyclerLikeNum.setText(num + "");
                        mCommentList.get(position).setLike(num);
                        mCommentPresenter = new CommentPresenter();
                        mCommentPresenter.requestUpdateCommentLikeNum(mCommentList.get(position).getObjectId(), 1);
                    }
                } else {
                    createSnackBar(holder.mCommentRecyclerComment, MyApplication.getContext().getString(R.string.network_error));
                }

            }
        });


    }

    private void startHomePageActivity(Context context, MyUser user) {
        Intent intent = HomePageActivity.newIntent(context, user);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void createSnackBar(View view, String prompt) {
        Snackbar.make(view, prompt, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }


}

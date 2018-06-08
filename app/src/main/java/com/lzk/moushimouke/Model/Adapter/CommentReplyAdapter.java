package com.lzk.moushimouke.Model.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzk.moushimouke.Model.Bean.InnerComment;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Utils.GetTimeUtils;
import com.lzk.moushimouke.MyApplication;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Activity.HomePageActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by huqun on 2018/5/24.
 */

public class CommentReplyAdapter extends RecyclerView.Adapter<CommentReplyAdapter.ViewHolder> {

    private List<InnerComment> mInnerComments;

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.reply_item_portrait)
        CircleImageView mReplyItemPortrait;
        @BindView(R.id.reply_item_username)
        TextView mReplyItemUsername;
        @BindView(R.id.reply_item_time)
        TextView mReplyItemTime;
        @BindView(R.id.reply_item_content)
        TextView mReplyItemContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public CommentReplyAdapter(List<InnerComment> innerComments) {
        mInnerComments = innerComments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_comment_reply_recycler_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.mReplyItemPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUser user=mInnerComments.get(holder.getAdapterPosition()).getUser();
                startHomePageActivity(MyApplication.getContext(),user);
            }
        });

        holder.mReplyItemUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUser user=mInnerComments.get(holder.getAdapterPosition()).getUser();
                startHomePageActivity(MyApplication.getContext(),user);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        InnerComment innerComment=mInnerComments.get(position);
        String portraitUrl=innerComment.getUser().getPortrait();
        if (portraitUrl!=null){
            Glide.with(MyApplication.getContext()).load(portraitUrl).into(holder.mReplyItemPortrait);
        }
        String userName=innerComment.getUser().getUsername();
        holder.mReplyItemUsername.setText(userName);
        String content=innerComment.getContent();
        holder.mReplyItemContent.setText(content);

        if (position==0){
            holder.mReplyItemTime.setVisibility(View.INVISIBLE);
        }else {
            holder.mReplyItemTime.setVisibility(View.VISIBLE);
            String createdAt=innerComment.getCreatedAt();
            GetTimeUtils timeUtils=new GetTimeUtils();
            String time=timeUtils.formatTime(createdAt);
            holder.mReplyItemTime.setText(time);
        }
    }

    @Override
    public int getItemCount() {
        return mInnerComments.size();
    }


    private void startHomePageActivity(Context context, MyUser user){
        Intent intent= HomePageActivity.newIntent(context,user);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}

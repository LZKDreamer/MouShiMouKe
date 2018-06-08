package com.lzk.moushimouke.Model.Adapter;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Notification;
import com.lzk.moushimouke.Model.Utils.GetTimeUtils;
import com.lzk.moushimouke.Model.Utils.MyClickableSpan;
import com.lzk.moushimouke.MyApplication;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Activity.CommentActivity;
import com.lzk.moushimouke.View.Activity.CommentReplyActivity;
import com.lzk.moushimouke.View.Activity.HomePageActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by huqun on 2018/6/5.
 */

public class NotificationFragmentAdapter extends RecyclerView.Adapter<NotificationFragmentAdapter.ViewHolder> {

    private List<Notification> mNotifications;
    private GetTimeUtils mTimeUtils;


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.notification_portrait)
        CircleImageView mNotificationPortrait;
        @BindView(R.id.notification_content)
        TextView mNotificationContent;
        @BindView(R.id.notification_time)
        TextView mNotificationTime;
        @BindView(R.id.notification_ll)
        LinearLayout mNotificationLl;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public NotificationFragmentAdapter(List<Notification> notificationList) {
        mNotifications = notificationList;
        mTimeUtils=new GetTimeUtils();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_notification_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.mNotificationLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notification notification=mNotifications.get(holder.getAdapterPosition());
                if(mNotifications.get(holder.getAdapterPosition()).getType()==1){
                    int commentNum=notification.getPost().getComment();
                    String postId=notification.getPost().getObjectId();
                    MyUser user=notification.getOtherUser();
                    String content=notification.getPost().getDescription();
                    Intent intent= CommentActivity.newIntent(MyApplication.getContext(),commentNum,postId,user,content);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getContext().startActivity(intent);
                }else {
                    int replyNum=notification.getComment().getCommentNum();
                    String commentId=notification.getComment().getObjectId();
                    MyUser user=notification.getOtherUser();
                    String content=notification.getComment().getComment();
                    Intent intent= CommentReplyActivity.newIntent(MyApplication.getContext(),replyNum,commentId,user,content);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getContext().startActivity(intent);
                }
            }
        });


        holder.mNotificationPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= HomePageActivity.newIntent(MyApplication.getContext(),mNotifications.get(holder.getAdapterPosition()).getUser());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notification notification=mNotifications.get(position);
        int type=notification.getType();
        String portraitUrl=notification.getUser().getPortrait();
        String createdTime=notification.getCreatedAt();
        String time=mTimeUtils.formatTime(createdTime);

        if (!portraitUrl.isEmpty()){
            Glide.with(MyApplication.getContext()).load(portraitUrl).thumbnail(0.5f).into(holder.mNotificationPortrait);
        }else {
            Glide.with(MyApplication.getContext()).load(R.drawable.ic_user_portrait_default).thumbnail(0.5f).into(holder.mNotificationPortrait);
        }

        holder.mNotificationTime.setText(time);

        String commentUserName=notification.getUser().getUsername();
        String replyUserName=notification.getUser().getUsername();
        String content=notification.getContent();
        String finalCommentUserName=String.format(MyApplication.getContext().getResources().getString(R.string.user_comment_your_post)
                +":"+content,
                commentUserName);
        String finalReplyUserName=String.format(MyApplication.getContext().getResources().getString(R.string.user_reply_your_comment)
                        +":"+content,
                replyUserName);

        MyClickableSpan clickableSpan=new MyClickableSpan(notification.getUser());
        if (type==1){
            SpannableString commentSpannable=new SpannableString(finalCommentUserName);
            ForegroundColorSpan colorSpan=new ForegroundColorSpan(Color.parseColor("#00BFFF"));
            ForegroundColorSpan colorSpan2=new ForegroundColorSpan(Color.parseColor("#000000"));

            commentSpannable.setSpan(clickableSpan,0,commentUserName.length(),Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            commentSpannable.setSpan(colorSpan,0,commentUserName.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            commentSpannable.setSpan(colorSpan2,commentUserName.length(),finalCommentUserName.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.mNotificationContent.setMovementMethod(LinkMovementMethod.getInstance());
            holder.mNotificationContent.setHighlightColor(Color.parseColor("#e6e6e6"));
            holder.mNotificationContent.setText(commentSpannable);
        }else {
            SpannableString replySpannable=new SpannableString(finalReplyUserName);
            ForegroundColorSpan colorSpan2=new ForegroundColorSpan(Color.parseColor("#000000"));
            ForegroundColorSpan colorSpan=new ForegroundColorSpan(Color.parseColor("#00BFFF"));
            replySpannable.setSpan(clickableSpan,0,replyUserName.length(),Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            replySpannable.setSpan(colorSpan,0,replyUserName.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            replySpannable.setSpan(colorSpan2,replyUserName.length(),finalReplyUserName.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.mNotificationContent.setMovementMethod(LinkMovementMethod.getInstance());
            holder.mNotificationContent.setHighlightColor(Color.parseColor("#e6e6e6"));
            holder.mNotificationContent.setText(replySpannable);
        }
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

}

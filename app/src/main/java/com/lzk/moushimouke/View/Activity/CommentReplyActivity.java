package com.lzk.moushimouke.View.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import com.lzk.moushimouke.Model.Adapter.CommentReplyAdapter;
import com.lzk.moushimouke.Model.Bean.InnerComment;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Presenter.CommentReplyPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Fragment.CommentReplyDialogFragment;
import com.lzk.moushimouke.View.Interface.ICommentReplyDataCallback;
import com.zrq.divider.Divider;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by huqun on 2018/5/24.
 */

public class CommentReplyActivity extends AppCompatActivity implements ICommentReplyDataCallback {

    public static final String REPLY_NUM = "com.lzk.moushimouke.View.Activity.CommentReplyActivity.reply_num";
    public static final String COMMENT_ID = "com.lzk.moushimouke.View.Activity.CommentReplyActivity.comment_id";
    public static final String USER = "com.lzk.moushimouke.View.Activity.CommentReplyActivity.user";
    private CommentReplyPresenter mReplyPresenter;
    public static CommentReplyActivity sCommentReplyActivity;

    @BindView(R.id.comment_reply_back)
    ImageView mCommentReplyBack;
    @BindView(R.id.comment_reply_num)
    TextView mCommentReplyNum;
    @BindView(R.id.comment_reply_recycler)
    RecyclerView mCommentReplyRecycler;
    @BindView(R.id.comment_reply_user_portrait)
    CircleImageView mCommentReplyUserPortrait;
    @BindView(R.id.comment_reply_content)
    TextView mCommentReplyContent;
    @BindView(R.id.state_layout_loading)
    SpinKitView mStateLayoutLoading;
    @BindView(R.id.state_layout_empty)
    LinearLayout mStateLayoutEmpty;
    @BindView(R.id.state_layout_error)
    LinearLayout mStateLayoutError;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_recycler_item__comment_reply);
        ButterKnife.bind(this);
        sCommentReplyActivity=this;
        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        if (user.getPortrait() != null) {
            Glide.with(this).load(user.getPortrait()).into(mCommentReplyUserPortrait);
        }
        int replyNum = getIntent().getIntExtra(REPLY_NUM, 0);
        String numString = getString(R.string.reply_num);
        String replyNumString = String.format(numString, replyNum);
        if (replyNum == 0) {
            mCommentReplyNum.setText(replyNumString);
            mStateLayoutLoading.setVisibility(View.GONE);
            mStateLayoutEmpty.setVisibility(View.VISIBLE);
            mStateLayoutError.setVisibility(View.GONE);
        } else {
            mCommentReplyNum.setText(replyNumString);
            String commentId=getIntent().getStringExtra(COMMENT_ID);
            mReplyPresenter=new CommentReplyPresenter(CommentReplyDialogFragment.sCommentReplyDialogFragment);
            mReplyPresenter.requestCommentReplyData(commentId);
        }


    }

    public static Intent newIntent(Context context, int replyNum,String commentId,MyUser user,String commentText) {
        Intent intent = new Intent(context, CommentReplyActivity.class);
        intent.putExtra(REPLY_NUM, replyNum);
        intent.putExtra(COMMENT_ID, commentId);
        intent.putExtra(USER,user);
        intent.putExtra("commentText",commentText);
        return intent;
    }

    @Override
    public String getCommentReplyActivityText() {
        return mCommentReplyContent.getText().toString();
    }

    @Override
    public void getCommentReplyDialogText(String replyText) {
        mCommentReplyContent.setText(replyText);
    }

    @Override
    public void getCommentReplySendResult(boolean result, List<InnerComment> innerComments) {
        if (result){
            String commentText=getIntent().getStringExtra("commentText");
            MyUser commentUser= (MyUser) getIntent().getSerializableExtra(USER);
            InnerComment innerComment=new InnerComment();
            innerComment.setUser(commentUser);
            innerComment.setContent(commentText);
            innerComments.add(0,innerComment);

            mStateLayoutLoading.setVisibility(View.GONE);
            mCommentReplyRecycler.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager=new LinearLayoutManager(this);
            mCommentReplyRecycler.setLayoutManager(layoutManager);
            CommentReplyAdapter adapter=new CommentReplyAdapter(innerComments);
            mCommentReplyRecycler.addItemDecoration(Divider.builder()
                    .color(Color.parseColor("#e6e6e6"))
                    .width(2)
                    .height(2)
                    .build());
            mCommentReplyRecycler.setAdapter(adapter);
        }else {
            mStateLayoutLoading.setVisibility(View.GONE);
            mStateLayoutError.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R.id.comment_reply_back, R.id.comment_reply_content})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.comment_reply_back:
                finish();
                break;
            case R.id.comment_reply_content:
                FragmentManager manager = getSupportFragmentManager();
                String commentId = getIntent().getStringExtra(COMMENT_ID);
                CommentReplyDialogFragment replyDialogFragment = CommentReplyDialogFragment.newInstance(commentId,
                        (MyUser) getIntent().getSerializableExtra(USER));
                replyDialogFragment.show(manager, "");
                break;
        }
    }
}

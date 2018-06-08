package com.lzk.moushimouke.View.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import com.lzk.moushimouke.Model.Adapter.CommentItemAdapter;
import com.lzk.moushimouke.Model.Bean.Comment;
import com.lzk.moushimouke.Model.Bean.CommentItemBean;
import com.lzk.moushimouke.Model.Bean.InnerComment;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Presenter.CommentPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Fragment.CommentEditDialogFragment;
import com.lzk.moushimouke.View.Interface.ICommentActivityDataCallBack;
import com.zrq.divider.Divider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity implements ICommentActivityDataCallBack {

    @BindView(R.id.comment_back)
    ImageView mCommentBack;
    @BindView(R.id.comment_num)
    TextView mCommentNum;
    @BindView(R.id.comment_recycler)
    RecyclerView mCommentRecycler;
    @BindView(R.id.state_layout_loading)
    SpinKitView mCommentLoading;
    @BindView(R.id.comment_user_portrait)
    CircleImageView mCommentUserPortrait;
    @BindView(R.id.comment_hint)
    TextView mCommentHint;
    @BindView(R.id.state_layout_empty)
    LinearLayout mStateLayoutEmpty;
    @BindView(R.id.state_layout_error)
    LinearLayout mStateLayoutError;
    private String postId;
    private CommentPresenter mCommentPresenter;
    public static CommentActivity sCommentActivity;
    private MyUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        int commentNum = intent.getIntExtra("commentNum", 0);
        String commentString = getResources().getString(R.string.comment_num);
        String value = String.format(commentString, commentNum);
        mCommentNum.setText(value);
        postId = intent.getStringExtra("postId");
        sCommentActivity=this;
        String portraitUrl=BmobUser.getCurrentUser(MyUser.class).getPortrait();
        if (portraitUrl!=null){
            Glide.with(this).load(portraitUrl).into(mCommentUserPortrait);
        }else {
            Glide.with(this).load(R.drawable.ic_user_portrait_default).into(mCommentUserPortrait);
        }

        mCommentPresenter=new CommentPresenter();
        mCommentPresenter.getCommentData(postId);
    }

    public static Intent newIntent(Context context,int commentNum, String postId, MyUser user, String content){
        Intent intent=new Intent(context,CommentActivity.class);
        intent.putExtra("commentNum",commentNum);
        intent.putExtra("postId",postId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        /*用于在评论列表的第一个item展示帖子内容*/
        intent.putExtra("user",user);//帖子的User。
        intent.putExtra("content",content);
        return intent;
    }



    @OnClick({R.id.comment_back, R.id.comment_hint})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.comment_back:
                finish();
                break;
            case R.id.comment_hint:
                FragmentManager manager = getSupportFragmentManager();
                CommentEditDialogFragment dialogFragment = CommentEditDialogFragment.newInstance(postId,user);
                dialogFragment.show(manager, "");
                break;
        }
    }

    @Override
    public String getActivityComment() {
        return mCommentHint.getText().toString();
    }

    @Override
    public void getDialogFragmentComment(String comment) {
        mCommentHint.setText(comment);
    }

    @Override
    public void getRequestCommentResult(boolean result, List<Comment> commentList) {
        if (result) {
            if (commentList.size()!=0) {
                /*获取帖子的信息，用于在评论列表的第一个item展示帖子内容*/
                String postDescription=getIntent().getStringExtra("content");
                user= (MyUser) getIntent().getSerializableExtra("user");
                Comment firstComment=new Comment();
                firstComment.setUser(user);
                firstComment.setComment(postDescription);
                commentList.add(0,firstComment);

                LinearLayoutManager layoutManager=new LinearLayoutManager(this);
                mCommentRecycler.setLayoutManager(layoutManager);
                CommentItemAdapter adapter=new CommentItemAdapter(commentList);
                mCommentRecycler.addItemDecoration(Divider.builder()
                        .color(Color.parseColor("#e6e6e6"))
                        .width(2)
                        .height(2)
                        .build());
                mCommentRecycler.setAdapter(adapter);
                mCommentLoading.setVisibility(View.GONE);
                mCommentRecycler.setVisibility(View.VISIBLE);
            } else {
                mCommentLoading.setVisibility(View.GONE);
                mStateLayoutEmpty.setVisibility(View.VISIBLE);
            }

        } else {
            mCommentLoading.setVisibility(View.GONE);
            mStateLayoutError.setVisibility(View.VISIBLE);
        }
    }
}

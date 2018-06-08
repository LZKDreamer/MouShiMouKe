package com.lzk.moushimouke.View.Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.ybq.android.spinkit.SpinKitView;
import com.lzk.moushimouke.Model.Adapter.FollowActivityAdapter;
import com.lzk.moushimouke.Model.Bean.Follow;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Presenter.FollowActivityPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Interface.IFollowActivityCallBack;
import com.zrq.divider.Divider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FollowActivity extends AppCompatActivity implements IFollowActivityCallBack{

    @BindView(R.id.follow_back)
    ImageView mFollowBack;
    @BindView(R.id.follow_recycler_view)
    RecyclerView mFollowRecyclerView;
    @BindView(R.id.state_layout_loading)
    SpinKitView mStateLayoutLoading;
    @BindView(R.id.state_layout_empty)
    LinearLayout mStateLayoutEmpty;
    @BindView(R.id.state_layout_error)
    LinearLayout mStateLayoutError;

    private FollowActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        ButterKnife.bind(this);
        mPresenter=new FollowActivityPresenter();
        mPresenter.requestFollowData(this);
    }


    @OnClick(R.id.follow_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void getFollowDataResult(List<Follow> followList, List<Post> postList, boolean result) {
        if (result){
            if (followList.size()!=0){
                mStateLayoutLoading.setVisibility(View.GONE);
                mFollowRecyclerView.setVisibility(View.VISIBLE);
                LinearLayoutManager manager=new LinearLayoutManager(this);
                mFollowRecyclerView.setLayoutManager(manager);
                FollowActivityAdapter activityAdapter=new FollowActivityAdapter(followList,postList);
                mFollowRecyclerView.addItemDecoration(Divider.builder()
                        .color(Color.parseColor("#e6e6e6"))
                        .width(2)
                        .height(2)
                        .build());
                mFollowRecyclerView.setAdapter(activityAdapter);
            }else {
                mStateLayoutLoading.setVisibility(View.GONE);
                mStateLayoutEmpty.setVisibility(View.VISIBLE);
            }

        }else {
            mStateLayoutLoading.setVisibility(View.GONE);
            mStateLayoutError.setVisibility(View.VISIBLE);
        }
    }
}

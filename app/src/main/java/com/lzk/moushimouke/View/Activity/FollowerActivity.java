package com.lzk.moushimouke.View.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.lzk.moushimouke.Model.Adapter.FollowActivityAdapter;
import com.lzk.moushimouke.Model.Adapter.FollowerActivityAdapter;
import com.lzk.moushimouke.Model.Bean.Follow;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Presenter.FollowerActivityPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Interface.IFollowerActivityCallBack;
import com.zrq.divider.Divider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FollowerActivity extends AppCompatActivity implements IFollowerActivityCallBack{

    @BindView(R.id.follower_back)
    ImageView mFollowerBack;
    @BindView(R.id.follower_recycler_view)
    RecyclerView mFollowerRecyclerView;
    @BindView(R.id.state_layout_loading)
    SpinKitView mStateLayoutLoading;
    @BindView(R.id.state_layout_empty)
    LinearLayout mStateLayoutEmpty;
    @BindView(R.id.state_layout_error)
    LinearLayout mStateLayoutError;

    private FollowerActivityPresenter mActivitypresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);
        ButterKnife.bind(this);
        mActivitypresenter=new FollowerActivityPresenter();
        mActivitypresenter.requestFollowerData(this);
    }

    @OnClick(R.id.follower_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void getFollowerDataResult(List<Follow> followerList, List<Post> postList,List<Follow> followList, boolean result) {
        if (result){
            if (followerList.size()!=0){
                mStateLayoutLoading.setVisibility(View.GONE);
                mFollowerRecyclerView.setVisibility(View.VISIBLE);
                LinearLayoutManager layoutManager=new LinearLayoutManager(this);
                mFollowerRecyclerView.setLayoutManager(layoutManager);
                FollowerActivityAdapter activityAdapter=new FollowerActivityAdapter(followerList,postList,followList);
                mFollowerRecyclerView.addItemDecoration(Divider.builder()
                        .color(Color.parseColor("#e6e6e6"))
                        .width(2)
                        .height(2)
                        .build());
                mFollowerRecyclerView.setAdapter(activityAdapter);
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

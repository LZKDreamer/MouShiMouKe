package com.lzk.moushimouke.View.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.nuptboyzhb.lib.SuperSwipeRefreshLayout;
import com.github.ybq.android.spinkit.SpinKitView;
import com.lzk.moushimouke.Model.Adapter.ExploreItemAdapter;
import com.lzk.moushimouke.Model.Bean.ExploreRecyclerItemBean;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Model.Listener.AutoPlayScrollListener;
import com.lzk.moushimouke.Model.Utils.GetTimeUtils;
import com.lzk.moushimouke.Model.Utils.IsNetWorkConnectedUtils;
import com.lzk.moushimouke.MyApplication;
import com.lzk.moushimouke.Presenter.HomeFragmentPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Interface.IHomeFragmentCallBack;
import com.zrq.divider.Divider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.jzvd.JZVideoPlayerStandard;

public class HomeFragment extends Fragment implements IHomeFragmentCallBack{
    public static final int REQUEST_TYPE_REFRESH=1;
    public static final int REQUEST_TYPE_LOAD_MORE=2;


    @BindView(R.id.state_layout_loading)
    SpinKitView mStateLayoutLoading;
    @BindView(R.id.state_layout_empty)
    LinearLayout mStateLayoutEmpty;
    @BindView(R.id.state_layout_error)
    LinearLayout mStateLayoutError;
    @BindView(R.id.home_recycler_view)
    RecyclerView mHomeRecyclerView;
    @BindView(R.id.home_swipe_refresh_layout)
    SuperSwipeRefreshLayout mHomeSwipeRefreshLayout;
    Unbinder unbinder;

    private int curPage,limit;
    private IsNetWorkConnectedUtils mConnectedUtils;
    private HomeFragmentPresenter mPresenter;
    private List<ExploreRecyclerItemBean> dataList;
    private ExploreItemAdapter mAdapter;
    private int insertPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void requestData(final int curPage, final int limit, int type){
        mConnectedUtils=new IsNetWorkConnectedUtils();
        boolean netWork=mConnectedUtils.IsNetWorkConnected(getActivity());
        if (netWork){

                mPresenter=new HomeFragmentPresenter();
                mPresenter.requestInitOrRefreshData(curPage,limit,type,this);


        }else {
            Snackbar.make(mHomeRecyclerView, getResources().getString(R.string.network_error), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getResources().getString(R.string.try_again), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requestData(curPage,limit,REQUEST_TYPE_REFRESH);
                        }
                    }).show();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        curPage=1;
        limit=10;
        requestData(curPage,limit,REQUEST_TYPE_REFRESH);
        mHomeSwipeRefreshLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                curPage=1;
                requestData(curPage,limit,REQUEST_TYPE_REFRESH);
            }

            @Override
            public void onPullDistance(int i) {

            }

            @Override
            public void onPullEnable(boolean b) {

            }
        });

        mHomeSwipeRefreshLayout.setOnPushLoadMoreListener(new SuperSwipeRefreshLayout.OnPushLoadMoreListener() {
            @Override
            public void onLoadMore() {
                requestData(curPage,limit,REQUEST_TYPE_LOAD_MORE);
            }

            @Override
            public void onPushDistance(int i) {

            }

            @Override
            public void onPushEnable(boolean b) {

            }
        });
        mHomeSwipeRefreshLayout.setHeaderView(createFooterView());
        mHomeSwipeRefreshLayout.setFooterView(createFooterView());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void getInitOrRefreshDataResult(List<Post> postList, boolean result) {
        mHomeSwipeRefreshLayout.setRefreshing(false);
        dataList=new ArrayList<>();
        dataList.clear();
        if (result){
            if (postList.size()!=0){
                for (Post post : postList) {
                    MyUser user = post.getUser();
                    String portrait = user.getPortrait();
                    String userName = user.getUsername();

                    String description = post.getDescription();

                    GetTimeUtils timeUtils = new GetTimeUtils();
                    String postTime = timeUtils.formatTime(post.getCreatedAt());

                    String thumbnail = post.getThumbnail();

                    String postId = post.getObjectId();

                    int forwardNum = post.getForward();
                    int likeNum = post.getLike();
                    int commentNum = post.getComment();

                    List<BmobFile> files = post.getMedia();
                    ExploreRecyclerItemBean itemBean = new ExploreRecyclerItemBean(portrait,
                            userName, postTime, files, forwardNum, likeNum,
                            description, thumbnail, null, false, user, post, commentNum,postId);
                    dataList.add(itemBean);


                }
                insertPosition=dataList.size();
                LinearLayoutManager manager=new LinearLayoutManager(getActivity());
                mHomeRecyclerView.setLayoutManager(manager);
                mStateLayoutLoading.setVisibility(View.GONE);
                mStateLayoutEmpty.setVisibility(View.GONE);
                mStateLayoutError.setVisibility(View.GONE);
                mHomeSwipeRefreshLayout.setVisibility(View.VISIBLE);
                mHomeRecyclerView.addItemDecoration(Divider.builder()
                        .color(Color.parseColor("#e6e6e6"))
                        .width(20)
                        .height(20)
                        .build());
                mAdapter=new ExploreItemAdapter(dataList,getFragmentManager());
                mHomeRecyclerView.setAdapter(mAdapter);
                mHomeRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
                    @Override
                    public void onChildViewAttachedToWindow(View view) {

                    }

                    @Override
                    public void onChildViewDetachedFromWindow(View view) {
                        JZVideoPlayerStandard videoPlayerStandard=view.findViewById(R.id.explore_video);
                        if (videoPlayerStandard.getVisibility()==View.VISIBLE&&videoPlayerStandard.currentState!=JZVideoPlayerStandard.CURRENT_STATE_PAUSE){
                            videoPlayerStandard.release();
                        }
                    }
                });
                mHomeRecyclerView.addOnScrollListener(new AutoPlayScrollListener());

            }else {
                mStateLayoutLoading.setVisibility(View.GONE);
                mStateLayoutEmpty.setVisibility(View.VISIBLE);
                mStateLayoutError.setVisibility(View.GONE);
                mHomeSwipeRefreshLayout.setVisibility(View.GONE);
            }
        }else {
            mStateLayoutLoading.setVisibility(View.GONE);
            mStateLayoutError.setVisibility(View.VISIBLE);
            mStateLayoutEmpty.setVisibility(View.GONE);
            mHomeSwipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void getLoadMoreDataResult(List<Post> postList, boolean result) {
        mHomeSwipeRefreshLayout.setLoadMore(false);
        if (result) {
            if (postList.size() != 0) {
                for (Post post : postList) {
                    MyUser user = post.getUser();
                    String portrait = user.getPortrait();
                    String userName = user.getUsername();

                    String description = post.getDescription();

                    GetTimeUtils timeUtils = new GetTimeUtils();
                    String postTime = timeUtils.formatTime(post.getCreatedAt());

                    String thumbnail = post.getThumbnail();

                    String postId = post.getObjectId();

                    int forwardNum = post.getForward();
                    int likeNum = post.getLike();
                    int commentNum = post.getComment();

                    List<BmobFile> files = post.getMedia();
                    ExploreRecyclerItemBean itemBean = new ExploreRecyclerItemBean(portrait,
                            userName, postTime, files, forwardNum, likeNum,
                            description, thumbnail, null, false, user, post, commentNum,postId);
                    dataList.add(itemBean);

                }
                curPage++;
                mAdapter.notifyItemInserted(insertPosition);
                mHomeRecyclerView.smoothScrollToPosition(insertPosition);
                insertPosition = dataList.size();
            }else {
                createSnackBar(mHomeRecyclerView,getResources().getString(R.string.no_more));
            }
        }else {
            return;
        }

    }

    @Override
    public void getFollowNumEmptyResult(boolean result) {
        if (!result){
            mStateLayoutLoading.setVisibility(View.GONE);
            mStateLayoutEmpty.setVisibility(View.VISIBLE);
            mStateLayoutError.setVisibility(View.GONE);
            mHomeSwipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void getDataErrorResult(boolean result) {
        if (!result){
            mStateLayoutLoading.setVisibility(View.GONE);
            mStateLayoutError.setVisibility(View.VISIBLE);
            mStateLayoutEmpty.setVisibility(View.GONE);
            mHomeSwipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    private View createFooterView() {
        View view = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.recyclerview_footer, null);
        return view;
    }

    private void createSnackBar(View view, String prompt) {
        Snackbar.make(view, prompt, Snackbar.LENGTH_LONG).show();
    }
}

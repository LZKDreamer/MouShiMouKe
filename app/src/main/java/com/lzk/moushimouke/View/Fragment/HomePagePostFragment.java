package com.lzk.moushimouke.View.Fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;

import com.github.nuptboyzhb.lib.SuperSwipeRefreshLayout;
import com.github.ybq.android.spinkit.SpinKitView;
import com.lzk.moushimouke.Model.Adapter.ExploreItemAdapter;
import com.lzk.moushimouke.Model.Adapter.MeFragmentItemAdapter;
import com.lzk.moushimouke.Model.Bean.ExploreRecyclerItemBean;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Model.EventBus.ToUpdateExploreItemEB;
import com.lzk.moushimouke.Model.Listener.AutoPlayScrollListener;
import com.lzk.moushimouke.Model.Utils.GetTimeUtils;
import com.lzk.moushimouke.Presenter.HomePageActivityPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Interface.IHomePagePostCallBack;
import com.zrq.divider.Divider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePagePostFragment extends Fragment implements IHomePagePostCallBack{


    @BindView(R.id.state_layout_loading)
    SpinKitView mStateLayoutLoading;
    @BindView(R.id.state_layout_empty)
    LinearLayout mStateLayoutEmpty;
    @BindView(R.id.state_layout_error)
    LinearLayout mStateLayoutError;
    @BindView(R.id.home_page_post_recycler)
    RecyclerView mHomePagePostRecycler;
    @BindView(R.id.home_page_post_refresh)
    SuperSwipeRefreshLayout mHomePagePostRefresh;
    Unbinder unbinder;

    public static final String CURRENT_USER="curUser";

    private MyUser mUser;
    private HomePageActivityPresenter mPresenter;
    private List<ExploreRecyclerItemBean> mItemBeanList;
    private MeFragmentItemAdapter mAdapter;
    private boolean isCurUser=false;

    public static HomePagePostFragment newInstance(MyUser user){
        Bundle bundle=new Bundle();
        bundle.putSerializable(CURRENT_USER,user);
        HomePagePostFragment fragment=new HomePagePostFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page_post, container, false);
        unbinder = ButterKnife.bind(this, view);
        mUser= (MyUser) getArguments().getSerializable(CURRENT_USER);
        mPresenter=new HomePageActivityPresenter();
        mPresenter.requestCurUserPostData(mUser,this);
        mItemBeanList=new ArrayList<>();
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getCurUserPostList(List<Post> postList) {
        MyUser curUser=BmobUser.getCurrentUser(MyUser.class);
        String curUserId=curUser.getObjectId();

        if (postList!=null&&postList.size()!=0){
            for (Post post:postList){
                MyUser user = post.getUser();
                String portrait = user.getPortrait();
                String userName = user.getUsername();
                String description = post.getDescription();

                GetTimeUtils timeUtils=new GetTimeUtils();
                String postTime=timeUtils.formatTime(post.getCreatedAt());

                String thumbnail=post.getThumbnail();

                String postId=post.getObjectId();

                if (user.getObjectId().equals(curUserId)){
                    isCurUser=true;
                }else {
                    isCurUser=false;
                }

                int forwardNum=post.getForward();
                int likeNum=post.getLike();
                int commentNum=post.getComment();


                List<BmobFile> files = post.getMedia();
                ExploreRecyclerItemBean itemBean = new ExploreRecyclerItemBean(portrait,
                        userName, postTime, files, forwardNum, likeNum,
                        description,thumbnail,null,isCurUser,user,post,commentNum,postId);
                mItemBeanList.add(itemBean);
            }
            mStateLayoutLoading.setVisibility(View.GONE);
            mStateLayoutEmpty.setVisibility(View.GONE);
            mHomePagePostRefresh.setVisibility(View.VISIBLE);
            mHomePagePostRefresh.setEnabled(false);
            LinearLayoutManager manager=new LinearLayoutManager(getActivity());
            mHomePagePostRecycler.setLayoutManager(manager);
            mAdapter=new MeFragmentItemAdapter(mItemBeanList,getFragmentManager());
            mHomePagePostRecycler.addItemDecoration(Divider.builder()
                    .color(Color.parseColor("#e6e6e6"))
                    .width(20)
                    .height(20)
                    .build());
            mHomePagePostRecycler.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
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
            mHomePagePostRecycler.addOnScrollListener(new AutoPlayScrollListener());
            mHomePagePostRecycler.setAdapter(mAdapter);


        }else {
            mStateLayoutLoading.setVisibility(View.GONE);
            mStateLayoutEmpty.setVisibility(View.VISIBLE);
            mHomePagePostRefresh.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleDeletePostMsg(ToUpdateExploreItemEB updateExploreItemEB){
        int position=updateExploreItemEB.getPosition();
        mItemBeanList.remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position,mItemBeanList.size()-position);
    }
}

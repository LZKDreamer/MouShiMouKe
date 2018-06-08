package com.lzk.moushimouke.View.Fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.nuptboyzhb.lib.SuperSwipeRefreshLayout;
import com.github.ybq.android.spinkit.SpinKitView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzk.moushimouke.Model.Adapter.ExploreItemAdapter;
import com.lzk.moushimouke.Model.Bean.ExploreRecyclerItemBean;
import com.lzk.moushimouke.Model.Bean.Follow;
import com.lzk.moushimouke.Model.Bean.FollowStateBean;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Model.EventBus.GetFollowDataEB;
import com.lzk.moushimouke.Model.EventBus.ToUpdateExploreItemEB;
import com.lzk.moushimouke.Model.Listener.AutoPlayScrollListener;
import com.lzk.moushimouke.Model.Utils.GetTimeUtils;
import com.lzk.moushimouke.Model.Utils.IsNetWorkConnectedUtils;
import com.lzk.moushimouke.MyApplication;
import com.lzk.moushimouke.Presenter.EditTextPresenter;
import com.lzk.moushimouke.Presenter.ExploreItemPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Activity.EditPhotoActivity;
import com.lzk.moushimouke.View.Activity.EditTextActivity;
import com.lzk.moushimouke.View.Activity.EditVideoActivity;
import com.lzk.moushimouke.View.Interface.IExploreItem;
import com.zrq.divider.Divider;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends Fragment implements IExploreItem {
    public static final int TAKE_PHOTO = 1;
    public static final int TAKE_VIDEO = 3;
    public static final int CAMERA_REQUEST_CODE = 4;
    public static final int GALLERY_REQUEST_CODE = 5;
    public static final int VIDEO_REQUEST_CODE = 6;
    public static final int HANDLER_INIT_DATA = 7;
    public static final int HANDLER_LOAD_MORE = 12;
    public static final int HANDLER_ERROR_LOAD_MORE = 13;
    public static final int TYPE_LOAD_MORE = 8;
    public static final int TYPE_PULL_REFRESH = 10;//下拉刷新
    public static final int TYPE_FIRST_REFRESH = 11;//第一次进入时


    @BindView(R.id.explore_floating_menu)
    FloatingActionsMenu mExploreFloatingMenu;
    @BindView(R.id.state_layout_loading)
    SpinKitView mStateLayoutLoading;
    @BindView(R.id.explore_fab_camera)
    FloatingActionButton mExploreFabCamera;
    @BindView(R.id.explore_fab_gallery)
    FloatingActionButton mExploreFabGallery;
    @BindView(R.id.explore_fab_video)
    FloatingActionButton mExploreFabVideo;
    Unbinder unbinder;
    @BindView(R.id.explore_recycler_view)
    RecyclerView mExploreRecyclerView;
    @BindView(R.id.explore_swipe_refresh_layout)
    SuperSwipeRefreshLayout mExploreSwipeRefreshLayout;

    private ExploreItemAdapter adapter;
    private GetTimeUtils mTimeUtils;
    private Uri photoUri;
    private File photoFile;
    private List<String> mChoosePhotoUri = new ArrayList<>();
    public static final int CHOOSE_PHOTO_NUM = 9;
    private String videoPath;
    private ExploreItemPresenter mPresenter;
    private MyUser mMyUser;
    public static ExploreFragment sExploreFragment;
    private List<ExploreRecyclerItemBean> dataList;
    private Handler mHandler, mLoadMoreHandler;
    private FragmentManager mFragmentManager;
    private int curPage, insertPosition;
    private int limit = 10;
    private IsNetWorkConnectedUtils connectedUtils;
    /*区别是否关注某用户*/
    private boolean isFollowed=false;
    private List<String> userIdList;
    /*判断Post表中查询到的User是否是当前User*/
    private boolean isCurUser;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTimeUtils = new GetTimeUtils();
        mPresenter = new ExploreItemPresenter(this);
        mMyUser = BmobUser.getCurrentUser(MyUser.class);
        dataList = new ArrayList<>();
        sExploreFragment = this;
        connectedUtils = new IsNetWorkConnectedUtils();
        userIdList=new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentManager = getFragmentManager();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
        curPage = 1;
        initItemData(curPage, limit, TYPE_FIRST_REFRESH);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case HANDLER_INIT_DATA:
                        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                        mExploreRecyclerView.setLayoutManager(manager);
                        mStateLayoutLoading.setVisibility(View.GONE);
                        mExploreRecyclerView.setVisibility(View.VISIBLE);
                        adapter = new ExploreItemAdapter(dataList,getChildFragmentManager());
                        mExploreRecyclerView.addItemDecoration(Divider.builder()
                                .color(Color.parseColor("#e6e6e6"))
                                .width(20)
                                .height(20)
                                .build());
                        mExploreRecyclerView.setAdapter(adapter);
                        mExploreRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
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
                        mExploreRecyclerView.addOnScrollListener(new AutoPlayScrollListener());
                        break;
                }
            }
        };

        mExploreSwipeRefreshLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                initItemData(curPage, limit, TYPE_PULL_REFRESH);
                userIdList.clear();
                mExploreSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onPullDistance(int i) {

            }

            @Override
            public void onPullEnable(boolean b) {

            }
        });

        mExploreSwipeRefreshLayout.setOnPushLoadMoreListener(new SuperSwipeRefreshLayout.OnPushLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPresenter.requestPostData(curPage, limit, TYPE_LOAD_MORE);
                mLoadMoreHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case HANDLER_LOAD_MORE:
                                curPage++;
                                adapter.notifyItemInserted(dataList.size());
                                mExploreRecyclerView.smoothScrollToPosition(insertPosition);
                                mExploreSwipeRefreshLayout.setLoadMore(false);
                                insertPosition = dataList.size();
                                break;
                            case HANDLER_ERROR_LOAD_MORE:
                                mExploreSwipeRefreshLayout.setLoadMore(false);
                                createSnackBar(mExploreFloatingMenu, getResources().getString(R.string.no_more));
                                break;
                        }
                    }
                };

            }

            @Override
            public void onPushDistance(int i) {

            }

            @Override
            public void onPushEnable(boolean b) {
            }
        });
        mExploreSwipeRefreshLayout.setHeaderView(createFooterView());
        mExploreSwipeRefreshLayout.setFooterView(createFooterView());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.explore_fab_camera, R.id.explore_fab_gallery, R.id.explore_fab_video})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.explore_fab_camera:
                    startEditText();
                break;
            case R.id.explore_fab_gallery:
                if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, GALLERY_REQUEST_CODE);
                } else {
                    openGallery();
                }
                break;
            case R.id.explore_fab_video:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, VIDEO_REQUEST_CODE);
                } else {
                    startVideo();
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PictureConfig.CHOOSE_REQUEST:
                if (resultCode == getActivity().RESULT_OK && data != null) {
                    mChoosePhotoUri.clear();
                    List<LocalMedia> photoList = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : photoList) {
                        mChoosePhotoUri.add(media.getPath());
                    }
                    Intent intent = new Intent(getActivity(), EditPhotoActivity.class);
                    intent.putStringArrayListExtra("photoPath", (ArrayList<String>) mChoosePhotoUri);
                    startActivity(intent);
                }
                break;
            case TAKE_VIDEO:
                if (resultCode == getActivity().RESULT_OK) {
                    List<LocalMedia> localMedia = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : localMedia) {
                        videoPath = media.getPath();
                    }
                    Intent videoIntent = new Intent(getActivity(), EditVideoActivity.class);
                    videoIntent.putExtra("videoPath", videoPath);
                    startActivity(videoIntent);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case GALLERY_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    createSnackBar(mExploreFabCamera, getResources().getString(R.string.permission_denied_prompt));
                }
                break;
            case VIDEO_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startVideo();
                } else {
                    createSnackBar(mExploreFabCamera, getResources().getString(R.string.permission_denied_prompt));
                }
                break;
        }
    }

    private void startEditText() {
        Intent intent=new Intent(getActivity(), EditTextActivity.class);
        startActivity(intent);
    }

    private void openGallery() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(CHOOSE_PHOTO_NUM)
                .imageSpanCount(3)
                .selectionMode(PictureConfig.MULTIPLE)
                .previewImage(true)
                .isCamera(true)
                .sizeMultiplier(0.5f)
                .isGif(false)
                .previewEggs(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    private void createSnackBar(View view, String prompt) {
        Snackbar.make(view, prompt, Snackbar.LENGTH_LONG).show();
    }


    private void startVideo() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofVideo())
                .imageSpanCount(2)
                .selectionMode(PictureConfig.SINGLE)
                .previewVideo(true)
                .isCamera(false)
                .sizeMultiplier(0.5f)
                .previewEggs(true)
                .forResult(TAKE_VIDEO);
    }

    private void initItemData(int page, final int limit, int type) {
        boolean isConnect = connectedUtils.IsNetWorkConnected(MyApplication.getContext());
        if (isConnect) {
            dataList.clear();
            mPresenter.requestPostData(page, limit, type);
        } else {
            Snackbar.make(mExploreFloatingMenu, getResources().getString(R.string.network_error), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getResources().getString(R.string.try_again), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            initItemData(curPage,limit,TYPE_FIRST_REFRESH);
                        }
                    })
                    .show();
        }
    }


    @Override
    public void setPostData(List<Post> postList, int type, boolean result) {

        if (result) {
            for (Post post : postList) {
                MyUser user = post.getUser();
                String portrait = user.getPortrait();
                String userName = user.getUsername();
                FollowStateBean followStateBean=new FollowStateBean();
                if (userIdList!=null){
                    if (userIdList.contains(user.getObjectId())){
                        isFollowed=true;
                        followStateBean.setState(isFollowed);
                    }else {
                        isFollowed=false;
                        followStateBean.setState(isFollowed);
                    }
                }
                MyUser curUser=BmobUser.getCurrentUser(MyUser.class);
                if (user.getObjectId().equals(curUser.getObjectId())){
                    isCurUser=true;
                }else {
                    isCurUser=false;
                }
                String description = post.getDescription();

                GetTimeUtils timeUtils=new GetTimeUtils();
                String postTime=timeUtils.formatTime(post.getCreatedAt());

                String thumbnail=post.getThumbnail();

                int forwardNum=post.getForward();
                int likeNum=post.getLike();
                int commentNum=post.getComment();

                String postId=post.getObjectId();

                List<BmobFile> files = post.getMedia();
                ExploreRecyclerItemBean itemBean = new ExploreRecyclerItemBean(portrait,
                        userName, postTime, files, forwardNum, likeNum,
                        description,thumbnail,followStateBean,isCurUser,user,post,
                        commentNum,postId);
                dataList.add(itemBean);
            }
            if (type == TYPE_PULL_REFRESH) {
                Message message = new Message();
                message.what = HANDLER_INIT_DATA;
                mHandler.sendMessage(message);
                insertPosition = dataList.size();
            } else {
                Message message = new Message();
                message.what = HANDLER_LOAD_MORE;
                mLoadMoreHandler.sendMessage(message);
            }
        } else {
            if (type == TYPE_LOAD_MORE) {
                Message message = new Message();
                message.what = HANDLER_ERROR_LOAD_MORE;
                mLoadMoreHandler.sendMessage(message);
            }

            Snackbar.make(mExploreSwipeRefreshLayout,getResources().getString(R.string.loading_error),Snackbar.LENGTH_INDEFINITE)
                    .setAction(getResources().getString(R.string.try_again), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            initItemData(1,limit,TYPE_FIRST_REFRESH);
                        }
                    }).show();
        }

    }

    private View createFooterView() {
        View view = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.recyclerview_footer, null);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /*处理查询到的本用户的Follow表的数据*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleFollowMsg(GetFollowDataEB getFollowDataEB){
        List<Follow> follows=getFollowDataEB.getFollowList();
        if (follows!=null){
            for (Follow follow:follows){
                userIdList.add(follow.getFollowUser().getObjectId());
            }
        }
    }
}

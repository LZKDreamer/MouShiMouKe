package com.lzk.moushimouke.View.Fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.nuptboyzhb.lib.SuperSwipeRefreshLayout;
import com.github.ybq.android.spinkit.SpinKitView;
import com.lzk.moushimouke.Model.Adapter.ExploreNinePhotoAdapter;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.MyApplication;
import com.lzk.moushimouke.Presenter.HomePageActivityPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Interface.IHomePageImageCallBack;
import com.zrq.divider.Divider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.datatype.BmobFile;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageGalleryFragment extends Fragment implements IHomePageImageCallBack{
    public static final String POST_USER="home_page_gallery_user";


    @BindView(R.id.home_page_gallery_recycler)
    RecyclerView mHomePageGalleryRecycler;
    @BindView(R.id.home_page_gallery_refresh)
    SuperSwipeRefreshLayout mHomePageGalleryRefresh;
    @BindView(R.id.state_layout_loading)
    SpinKitView mStateLayoutLoading;
    @BindView(R.id.state_layout_empty)
    LinearLayout mStateLayoutEmpty;
    @BindView(R.id.state_layout_error)
    LinearLayout mStateLayoutError;
    Unbinder unbinder;

    private List<BmobFile> imageList;
    private HomePageActivityPresenter mPresenter;

    public static HomePageGalleryFragment newInstance(MyUser user){
        Bundle args=new Bundle();
        args.putSerializable(POST_USER, user);
        HomePageGalleryFragment fragment=new HomePageGalleryFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page_gallery, container, false);
        unbinder = ButterKnife.bind(this, view);
        imageList=new ArrayList<>();
        MyUser user= (MyUser) getArguments().getSerializable(POST_USER);
        mPresenter=new HomePageActivityPresenter();
        mPresenter.requestCurUserPostImage(user,this);
        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void getHomePageImageResult(List<Post> posts) {
        if (posts!=null&&posts.size()!=0){
            for (Post post:posts){
                List<BmobFile> fileList=post.getMedia();
                if (fileList!=null){
                    BmobFile file=fileList.get(0);
                    String fileName = file.getFilename();
                    String[] token = fileName.split("\\.");
                    String type = token[1].toLowerCase();
                    String[] videoType = new String[]{"avi", "wmv", "mp4", "mpeg4"};
                    List<String> videoTypeList = Arrays.asList(videoType);
                    if (videoTypeList.contains(type)){
                        continue;
                    }else {
                        for (BmobFile imageFile:fileList){
                            imageList.add(imageFile);
                        }
                    }
                }else {//如果某帖子没有图片就执行下一次循环。
                    continue;
                }
            }
            mStateLayoutLoading.setVisibility(View.GONE);
            mStateLayoutEmpty.setVisibility(View.GONE);
            mHomePageGalleryRefresh.setVisibility(View.VISIBLE);
            mHomePageGalleryRefresh.setEnabled(false);
            GridLayoutManager manager=new GridLayoutManager(MyApplication.getContext(),3);
            mHomePageGalleryRecycler.setLayoutManager(manager);
            ExploreNinePhotoAdapter ninePhotoAdapter=new ExploreNinePhotoAdapter(imageList,getFragmentManager());
            mHomePageGalleryRecycler.addItemDecoration(Divider.builder()
                    .color(Color.parseColor("#FFFFFF"))
                    .width(5)
                    .height(5)
                    .build());
            mHomePageGalleryRecycler.setAdapter(ninePhotoAdapter);
        }else {
            mStateLayoutLoading.setVisibility(View.GONE);
            mStateLayoutEmpty.setVisibility(View.VISIBLE);
            mHomePageGalleryRefresh.setVisibility(View.GONE);
        }

    }
}

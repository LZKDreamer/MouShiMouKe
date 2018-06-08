package com.lzk.moushimouke.View.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzk.moushimouke.Model.Adapter.HomePageFragmentAdapter;
import com.lzk.moushimouke.Model.Bean.Follow;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Model.EventBus.ToUpdateExploreItemEB;
import com.lzk.moushimouke.Model.Utils.IsNetWorkConnectedUtils;
import com.lzk.moushimouke.MyApplication;
import com.lzk.moushimouke.Presenter.MeFragmentPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Activity.FollowActivity;
import com.lzk.moushimouke.View.Activity.FollowerActivity;
import com.lzk.moushimouke.View.Activity.UserInfoActivity;
import com.lzk.moushimouke.View.Interface.IMeFragmentCallBack;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;
import cn.jzvd.JZVideoPlayerStandard;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment implements IMeFragmentCallBack {
    @BindView(R.id.me_background_image)
    ImageView mMeBackgroundImage;
    @BindView(R.id.me_portrait)
    CircleImageView mMePortrait;
    @BindView(R.id.me_user_name)
    TextView mMeUserName;
    @BindView(R.id.me_profile)
    TextView mMeProfile;
    @BindView(R.id.me_edit_info_btn)
    Button mMeEditInfoBtn;
    @BindView(R.id.me_post_num)
    TextView mMePostNum;
    @BindView(R.id.me_post_ll)
    LinearLayout mMePostLl;
    @BindView(R.id.me_follow_num)
    TextView mMeFollowNum;
    @BindView(R.id.me_follow_ll)
    LinearLayout mMeFollowLl;
    @BindView(R.id.me_follower_num)
    TextView mMeFollowerNum;
    @BindView(R.id.me_follower_ll)
    LinearLayout mMeFollowerLl;
    @BindView(R.id.me_collapsing_toolbar)
    CollapsingToolbarLayout mMeCollapsingToolbar;
    @BindView(R.id.me_appbar_layout)
    AppBarLayout mMeAppbarLayout;
    @BindView(R.id.me_tab_layout)
    TabLayout mMeTabLayout;
    @BindView(R.id.me_view_pager)
    ViewPager mMeViewPager;
    Unbinder unbinder;
    private List<Fragment> mFragmentList;
    private String[] mTabTitles;
    private MyUser mMyUser;
    private MeFragmentPresenter mPresenter;
    private MyUser mCurUser;
    private IsNetWorkConnectedUtils connectedUtils;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Glide.with(getActivity()).load(R.drawable.ic_user_portrait_default).into(mMePortrait);
        mMyUser = BmobUser.getCurrentUser(MyUser.class);
        connectedUtils=new IsNetWorkConnectedUtils();
        mPresenter = new MeFragmentPresenter();
        mPresenter.requestMeUserInfo(mMyUser, this);
    }

    private void initFragmentList() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(HomePagePostFragment.newInstance(mMyUser));
        mFragmentList.add(HomePageGalleryFragment.newInstance(mMyUser));
    }

    private void initTabAndViewPager() {
        mTabTitles = new String[]{getString(R.string.post1), getString(R.string.gallery)};
        HomePageFragmentAdapter adapter = new HomePageFragmentAdapter(getFragmentManager(), mFragmentList, mTabTitles);
        mMeViewPager.setAdapter(adapter);
        mMeTabLayout.setupWithViewPager(mMeViewPager);
        mMeTabLayout.getTabAt(0).select();
        mMeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                JZVideoPlayerStandard.releaseAllVideos();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.me_background_image, R.id.me_portrait, R.id.me_user_name, R.id.me_profile, R.id.me_edit_info_btn, R.id.me_post_ll, R.id.me_follow_ll, R.id.me_follower_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.me_background_image:
                break;
            case R.id.me_portrait:
                FragmentManager fragmentManager = getFragmentManager();
                ViewPortraitDialogFragment portraitDialogFragment = ViewPortraitDialogFragment.newInstance(mCurUser.getPortrait());
                portraitDialogFragment.show(fragmentManager, "");
                break;
            case R.id.me_user_name:
                startUserInfoActivity();
                break;
            case R.id.me_profile:
                startUserInfoActivity();
                break;
            case R.id.me_edit_info_btn:
                startUserInfoActivity();
                break;
            case R.id.me_follow_ll:
                connectedUtils=new IsNetWorkConnectedUtils();
                boolean state=connectedUtils.IsNetWorkConnected(getActivity());
                if (state){
                    Intent intent=new Intent(getActivity(), FollowActivity.class);
                    startActivity(intent);
                }else {
                    showSnackBar(mMeCollapsingToolbar,getString(R.string.network_error));
                }
                break;
            case R.id.me_follower_ll:
                connectedUtils=new IsNetWorkConnectedUtils();
                boolean netState=connectedUtils.IsNetWorkConnected(getActivity());
                if (netState){
                    Intent intent=new Intent(getActivity(), FollowerActivity.class);
                    startActivity(intent);
                }else {
                    showSnackBar(mMeCollapsingToolbar,getString(R.string.network_error));
                }
                break;
        }

    }

    private void showSnackBar(View view,String prompt){
        Snackbar.make(view,prompt,Snackbar.LENGTH_SHORT).show();
    }


    private void startUserInfoActivity() {
        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
        startActivity(intent);
    }

    @Override
    public void getRequestUserInfoResult(List<Post> postList, List<Follow> followList, List<Follow> followerList, boolean result, List<MyUser> userList) {
        for (MyUser user : userList) {
            mCurUser = user;
            if (user.getPortrait() != null) {
                Glide.with(getActivity()).load(user.getPortrait()).thumbnail(0.5f).into(mMePortrait);
            }
            mMeUserName.setVisibility(View.VISIBLE);
            mMeUserName.setText(user.getUsername());
            String profile = user.getProfile();
            mMeProfile.setVisibility(View.VISIBLE);
            if (profile != null) {
                mMeProfile.setText(user.getProfile());
            } else {
                mMeProfile.setText(getString(R.string.empty_profile));
            }

            mMeEditInfoBtn.setVisibility(View.VISIBLE);
            mMePostLl.setVisibility(View.VISIBLE);
            mMeFollowLl.setVisibility(View.VISIBLE);
            mMeFollowerLl.setVisibility(View.VISIBLE);

            if (result) {
                String postNum = postList.size() + "";
                mMePostNum.setText(postNum);

                String followNum = followList.size() + "";
                mMeFollowNum.setText(followNum);

                String followerNum = followerList.size() + "";
                mMeFollowerNum.setText(followerNum);
            } else {
                mMePostNum.setText(0 + "");
                mMeFollowNum.setText(0 + "");
                mMeFollowerNum.setText(0 + "");
                Toast.makeText(MyApplication.getContext(), getString(R.string.loading_error), Toast.LENGTH_SHORT).show();
            }

        }
        initFragmentList();
        initTabAndViewPager();
    }

}

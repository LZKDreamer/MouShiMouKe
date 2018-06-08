package com.lzk.moushimouke.View.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzk.moushimouke.Model.Adapter.HomePageFragmentAdapter;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Presenter.HomePageActivityPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Fragment.HomePageGalleryFragment;
import com.lzk.moushimouke.View.Fragment.HomePagePostFragment;
import com.lzk.moushimouke.View.Fragment.ViewPortraitDialogFragment;
import com.lzk.moushimouke.View.Interface.IHomePageActivityDataCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayerStandard;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomePageActivity extends AppCompatActivity implements IHomePageActivityDataCallBack {

    @BindView(R.id.home_page_background)
    ImageView mHomePageBackground;
    @BindView(R.id.home_page_portrait)
    CircleImageView mHomePagePortrait;
    @BindView(R.id.home_page_user_name)
    TextView mHomePageUserName;
    @BindView(R.id.home_page_post_num)
    TextView mHomePagePostNum;
    @BindView(R.id.home_page_follower_num)
    TextView mHomePageFollowerNum;
    @BindView(R.id.home_page_profile)
    TextView mHomePageProfile;
    @BindView(R.id.home_page_toolbar)
    Toolbar mHomePageToolbar;
    @BindView(R.id.home_page_tab_layout)
    TabLayout mHomePageTabLayout;
    @BindView(R.id.home_page_view_pager)
    ViewPager mHomePageViewPager;
    @BindView(R.id.home_page_appbar)
    AppBarLayout mHomePageAppbar;
    @BindView(R.id.home_page_toolbar_title)
    TextView mHomePageToolbarTitle;
    @BindView(R.id.home_page_post)
    TextView mHomePagePost;
    @BindView(R.id.home_page_follower)
    TextView mHomePageFollower;
    @BindView(R.id.home_page_toolbar_back)
    ImageView mHomePageToolbarBack;
    @BindView(R.id.home_page_ll_divider)
    View mHomePageLlDivider;


    private List<Fragment> mFragmentList;
    private String[] mTabTitles;
    private HomePageFragmentAdapter mFragmentAdapter;
    public static final String tag = "HOME_PAGE_DATA";
    private MyUser mUser;
    private String portraitUrl, userName, profile;
    private int postNum, followerNum;
    private List<Post> mPosts;
    private HomePageActivityPresenter mActivityPresenter;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);
        initToolbar();
        Glide.with(this).load(R.drawable.ic_user_portrait_default).into(mHomePagePortrait);
        initUserInfo();

        mHomePageAppbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                /*折叠状态*/
                if (getSupportActionBar().getHeight() - mHomePageAppbar.getHeight() == verticalOffset) {
                    mHomePageToolbarTitle.setText(userName);
                    mHomePageToolbarBack.setImageResource(R.drawable.ic_home_page_back_dark);
                    mHomePageToolbar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                } else {
                    mHomePageToolbarTitle.setText("");
                    mHomePageToolbarBack.setImageResource(R.drawable.ic_home_page_back_white);
                    mHomePageToolbar.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                }
            }
        });
    }

    public static Intent newIntent(Context context, MyUser user) {
        Intent intent = new Intent(context, HomePageActivity.class);
        intent.putExtra(tag, user);
        return intent;
    }

    private void initUserInfo() {
        Intent intent = getIntent();
        mUser = (MyUser) intent.getSerializableExtra(tag);
        mActivityPresenter = new HomePageActivityPresenter();
        mActivityPresenter.requestPostData(mUser, this);

    }

    private void initFragmentList() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(HomePagePostFragment.newInstance(mUser));
        mFragmentList.add(HomePageGalleryFragment.newInstance(mUser));

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.home_page_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
    }

    private void initTabAndViewPager() {
        mTabTitles = new String[]{getString(R.string.post1), getString(R.string.gallery)};
        mFragmentAdapter = new HomePageFragmentAdapter(getSupportFragmentManager(), mFragmentList, mTabTitles);
        mHomePageViewPager.setAdapter(mFragmentAdapter);
        mHomePageTabLayout.setupWithViewPager(mHomePageViewPager);
        mHomePageTabLayout.getTabAt(0).select();
        mHomePageTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
    public void getPostAndFollowerResult(List<Post> postList, int followerNum) {
        mHomePagePost.setVisibility(View.VISIBLE);
        mHomePageFollower.setVisibility(View.VISIBLE);
        mHomePageLlDivider.setVisibility(View.VISIBLE);
        if (postList != null) {
            postNum = postList.size();
            this.followerNum = followerNum;
            mPosts = postList;

            mHomePagePostNum.setText(postNum + "");
            mHomePageFollowerNum.setText(this.followerNum + "");
        } else {
            mHomePagePostNum.setText(0 + "");
            mHomePageFollowerNum.setText(0 + "");
        }

        portraitUrl = mUser.getPortrait();
        if (portraitUrl != null) {
            Glide.with(this).load(portraitUrl).into(mHomePagePortrait);
        }

        userName = mUser.getUsername();
        mHomePageUserName.setText(userName);

        profile = mUser.getProfile();
        if (profile != null) {
            mHomePageProfile.setText(profile);
        } else {
            mHomePageProfile.setText(getString(R.string.empty_profile));
        }
        initFragmentList();
        initTabAndViewPager();

    }

    @OnClick(R.id.home_page_toolbar_back)
    public void onViewClicked() {
        finish();
    }

    @OnClick(R.id.home_page_portrait)
    public void onPortraitClicked() {
        if (mUser.getPortrait()!=null){
            FragmentManager manager=getSupportFragmentManager();
            ViewPortraitDialogFragment portraitDialogFragment=ViewPortraitDialogFragment.newInstance(mUser.getPortrait());
            portraitDialogFragment.show(manager,"");
        }
    }

}

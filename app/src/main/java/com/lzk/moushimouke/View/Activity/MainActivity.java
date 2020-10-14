package com.lzk.moushimouke.View.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Presenter.MainActivityPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Fragment.ExploreFragment;
import com.lzk.moushimouke.View.Fragment.HomeFragment;
import com.lzk.moushimouke.View.Fragment.MeFragment;
import com.lzk.moushimouke.View.Fragment.NotificationFragment;
import com.lzk.moushimouke.View.Interface.IMainActivityCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobUser;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener,IMainActivityCallBack{
    @BindView(R.id.main_tab_fragment_container)
    FrameLayout mMainTabFragmentContainer;
    @BindView(R.id.main_bnb)
    BottomNavigationBar mMainBnb;
    private ExploreFragment mExploreFragment;
    private HomeFragment mHomeFragment;
    private MeFragment mMeFragment;
    private NotificationFragment mNotificationFragment;
    private int mFragmentContainer=R.id.main_tab_fragment_container;
    public static MainActivity sMainActivity;
    private MainActivityPresenter mActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
        sMainActivity=this;
        if (myUser == null) {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }else {
            mActivityPresenter=new MainActivityPresenter();
            SharedPreferences preferences=getSharedPreferences("account",MODE_PRIVATE);

            String phoneNumber=preferences.getString("phoneNumber","");
            String passWord= preferences.getString("password","");
            mActivityPresenter.login(phoneNumber,passWord,this);
        }
    }


    private void setDefaultFragment(){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction transaction=fm.beginTransaction();
        mExploreFragment=new ExploreFragment();
        transaction.replace(mFragmentContainer,mExploreFragment);
        transaction.commit();
    }

    @Override
    public void onTabSelected(int position) {
        FragmentManager fm=this.getSupportFragmentManager();
        FragmentTransaction transaction=fm.beginTransaction();
        hideFragment(transaction);
        switch (position){
            case 0:
                if (mExploreFragment==null){
                    mExploreFragment=new ExploreFragment();
                    transaction.add(R.id.main_tab_fragment_container,mExploreFragment);
                }else {
                    transaction.show(mExploreFragment);
                }

                break;
            case 1:
                if (mHomeFragment==null){
                    mHomeFragment=new HomeFragment();
                    transaction.add(mFragmentContainer,mHomeFragment);
                }else {
                    transaction.show(mHomeFragment);
                }
                break;
            case 2:
                if (mNotificationFragment==null){
                    mNotificationFragment=new NotificationFragment();
                    transaction.add(mFragmentContainer,mNotificationFragment);
                }else {
                    transaction.show(mNotificationFragment);
                }

                break;
            case 3:
                if (mMeFragment==null){
                    mMeFragment=new MeFragment();
                    transaction.add(mFragmentContainer,mMeFragment);
                }else {
                    transaction.show(mMeFragment);
                }

                break;
            default:
                break;
        }
        transaction.commit();

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    private void hideFragment(FragmentTransaction transaction){
        if (mExploreFragment!=null){
            transaction.hide(mExploreFragment);
        }
        if (mHomeFragment!=null){
            transaction.hide(mHomeFragment);
        }
        if (mNotificationFragment!=null){
            transaction.hide(mNotificationFragment);
        }
        if (mMeFragment!=null) {
            transaction.hide(mMeFragment);
        }
        JZVideoPlayerStandard.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            JZVideoPlayer.releaseAllVideos();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JZVideoPlayerStandard.releaseAllVideos();
    }

    @Override
    public void getLoginResult(boolean result) {
        if (result){
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);
            mMainBnb
                    .setActiveColor(R.color.colorWhite)
                    .setInActiveColor(R.color.colorGrey)
                    .setMode(BottomNavigationBar.MODE_SHIFTING)
                    .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                    .setBarBackgroundColor(R.color.color_blue)
                    .addItem(new BottomNavigationItem(R.drawable.ic_tab_explore_select,getResources().getString(R.string.tab_explore)))
                    .addItem(new BottomNavigationItem(R.drawable.ic_tab_home_select,getResources().getString(R.string.tab_home)))
                    .addItem(new BottomNavigationItem(R.drawable.ic_tab_notification_select,getResources().getString(R.string.tab_notification)))
                    .addItem(new BottomNavigationItem(R.drawable.ic_tab_me_select,getResources().getString(R.string.tab_me)))
                    .setFirstSelectedPosition(0)
                    .initialise();
            mMainBnb.setTabSelectedListener(this);
            setDefaultFragment();
        }else {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

    }
}
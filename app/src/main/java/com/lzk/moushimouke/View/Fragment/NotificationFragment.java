package com.lzk.moushimouke.View.Fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.ybq.android.spinkit.SpinKitView;
import com.lzk.moushimouke.Model.Adapter.NotificationFragmentAdapter;
import com.lzk.moushimouke.Model.Bean.Notification;
import com.lzk.moushimouke.Presenter.NotificationPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Interface.INotificationFragmentCallBack;
import com.zrq.divider.Divider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment implements INotificationFragmentCallBack {

    @BindView(R.id.notification_recycler_view)
    RecyclerView mNotificationRecyclerView;
    @BindView(R.id.state_layout_loading)
    SpinKitView mStateLayoutLoading;
    @BindView(R.id.state_layout_empty)
    LinearLayout mStateLayoutEmpty;
    @BindView(R.id.state_layout_error)
    LinearLayout mStateLayoutError;
    Unbinder unbinder;
    private NotificationPresenter mPresenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new NotificationPresenter();
        mPresenter.requestNotificationData(this);
    }

    @Override
    public void getNotificationDataResult(List<Notification> notificationList, boolean result) {
        if (result){
            if (notificationList.size()!=0){
                mStateLayoutLoading.setVisibility(View.GONE);
                LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
                mNotificationRecyclerView.setLayoutManager(layoutManager);
                NotificationFragmentAdapter adapter=new NotificationFragmentAdapter(notificationList);
                mNotificationRecyclerView.addItemDecoration(Divider.builder()
                        .color(Color.parseColor("#e6e6e6"))
                        .width(2)
                        .height(2)
                        .build());
                mNotificationRecyclerView.setAdapter(adapter);

            }else {
                mStateLayoutLoading.setVisibility(View.GONE);
                mStateLayoutEmpty.setVisibility(View.VISIBLE);
            }

        }else {
            mStateLayoutLoading.setVisibility(View.GONE);
            mStateLayoutError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

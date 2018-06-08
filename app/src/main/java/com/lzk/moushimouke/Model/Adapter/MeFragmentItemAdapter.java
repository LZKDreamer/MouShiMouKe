package com.lzk.moushimouke.Model.Adapter;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzk.moushimouke.Model.Bean.ExploreRecyclerItemBean;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Model.EventBus.ToUpdateExploreItemEB;
import com.lzk.moushimouke.Model.Utils.AnimationUtils;
import com.lzk.moushimouke.Model.Utils.IsNetWorkConnectedUtils;
import com.lzk.moushimouke.Model.Utils.MyClickableSpan;
import com.lzk.moushimouke.MyApplication;
import com.lzk.moushimouke.Presenter.ExploreItemPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Activity.CommentActivity;
import com.lzk.moushimouke.View.Activity.HomePageActivity;
import com.lzk.moushimouke.View.Activity.MainActivity;
import com.lzk.moushimouke.View.Activity.RepostActivity;
import com.lzk.moushimouke.View.Fragment.ExploreFragment;
import com.zrq.divider.Divider;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import cn.jzvd.JZVideoPlayerStandard;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by huqun on 2018/5/5.
 */

public class MeFragmentItemAdapter extends RecyclerView.Adapter<ViewHolder> {
    public static final int VIDEO_ITEM_TYPE = 1;
    public static final int PHOTO_ITEM_TYPE = 2;
    public static final int TEXT_ITEM_TYPE = 3;

    private ExploreNinePhotoAdapter mPhotoAdapter;
    private ExploreItemPresenter mExploreItemPresenter;
    private FragmentManager mFragmentManager;


    private List<ExploreRecyclerItemBean> dataList;
    private List<String> likeStateList = new ArrayList<>();
    private boolean followState;

    public MeFragmentItemAdapter(List<ExploreRecyclerItemBean> dataList, FragmentManager fragmentManager) {
        this.dataList = dataList;
        mFragmentManager = fragmentManager;
        mExploreItemPresenter = new ExploreItemPresenter(ExploreFragment.sExploreFragment);
    }


    class PhotoViewHolder extends ViewHolder {
        @BindView(R.id.explore_recycler_view_portrait)
        CircleImageView mExploreRecyclerViewPortrait;
        @BindView(R.id.explore_recycler_view_username)
        TextView mExploreRecyclerViewUsername;
        @BindView(R.id.explore_recycler_view_time)
        TextView mExploreRecyclerViewTime;
        @BindView(R.id.explore_recycler_view_follow)
        Button mExploreRecyclerViewFollow;
        @BindView(R.id.explore_item_nine_photo)
        RecyclerView mExploreItemNinePhoto;
        @BindView(R.id.explore_video)
        JZVideoPlayerStandard mExploreVideo;
        @BindView(R.id.explore_rePost)
        ImageView mExploreRePost;
        @BindView(R.id.explore_rePost_num)
        TextView mExploreRePostNum;
        @BindView(R.id.explore_comment)
        ImageView mExploreComment;
        @BindView(R.id.explore_comment_num)
        TextView mExploreCommentNum;
        @BindView(R.id.explore_like)
        ImageView mExploreLike;
        @BindView(R.id.explore_like_num)
        TextView mExploreLikeNum;
        @BindView(R.id.explore_item_text_view)
        TextView mExploreExpandableTextView;
        @BindView(R.id.explore_comment_ll)
        LinearLayout mExploreCommentLl;
        @BindView(R.id.explore_like_ll)
        LinearLayout mExploreLikeLl;
        @BindView(R.id.explore_rePost_ll)
        LinearLayout mExploreRePostLl;
        @BindView(R.id.explore_recycler_view_delete)
        ImageView mExploreRecyclerViewDelete;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class VideoViewHolder extends ViewHolder {
        @BindView(R.id.explore_recycler_view_portrait)
        CircleImageView mExploreRecyclerViewPortrait;
        @BindView(R.id.explore_recycler_view_username)
        TextView mExploreRecyclerViewUsername;
        @BindView(R.id.explore_recycler_view_time)
        TextView mExploreRecyclerViewTime;
        @BindView(R.id.explore_recycler_view_follow)
        Button mExploreRecyclerViewFollow;
        @BindView(R.id.explore_item_nine_photo)
        RecyclerView mExploreItemNinePhoto;
        @BindView(R.id.explore_video)
        JZVideoPlayerStandard mExploreVideo;
        @BindView(R.id.explore_rePost)
        ImageView mExploreRePost;
        @BindView(R.id.explore_rePost_num)
        TextView mExploreRePostNum;
        @BindView(R.id.explore_comment)
        ImageView mExploreComment;
        @BindView(R.id.explore_comment_num)
        TextView mExploreCommentNum;
        @BindView(R.id.explore_like)
        ImageView mExploreLike;
        @BindView(R.id.explore_like_num)
        TextView mExploreLikeNum;
        @BindView(R.id.explore_item_text_view)
        TextView mExploreExpandableTextView;
        @BindView(R.id.explore_comment_ll)
        LinearLayout mExploreCommentLl;
        @BindView(R.id.explore_like_ll)
        LinearLayout mExploreLikeLl;
        @BindView(R.id.explore_rePost_ll)
        LinearLayout mExploreRePostLl;
        @BindView(R.id.explore_recycler_view_delete)
        ImageView mExploreRecyclerViewDelete;

        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class TextViewHolder extends ViewHolder {
        @BindView(R.id.explore_recycler_view_portrait)
        CircleImageView mExploreRecyclerViewPortrait;
        @BindView(R.id.explore_recycler_view_username)
        TextView mExploreRecyclerViewUsername;
        @BindView(R.id.explore_recycler_view_time)
        TextView mExploreRecyclerViewTime;
        @BindView(R.id.explore_recycler_view_follow)
        Button mExploreRecyclerViewFollow;
        @BindView(R.id.explore_item_nine_photo)
        RecyclerView mExploreItemNinePhoto;
        @BindView(R.id.explore_video)
        JZVideoPlayerStandard mExploreVideo;
        @BindView(R.id.explore_rePost)
        ImageView mExploreRePost;
        @BindView(R.id.explore_rePost_num)
        TextView mExploreRePostNum;
        @BindView(R.id.explore_comment)
        ImageView mExploreComment;
        @BindView(R.id.explore_comment_num)
        TextView mExploreCommentNum;
        @BindView(R.id.explore_like)
        ImageView mExploreLike;
        @BindView(R.id.explore_like_num)
        TextView mExploreLikeNum;
        @BindView(R.id.explore_item_text_view)
        TextView mExploreExpandableTextView;
        @BindView(R.id.explore_comment_ll)
        LinearLayout mExploreCommentLl;
        @BindView(R.id.explore_like_ll)
        LinearLayout mExploreLikeLl;
        @BindView(R.id.explore_rePost_ll)
        LinearLayout mExploreRePostLl;
        @BindView(R.id.explore_recycler_view_delete)
        ImageView mExploreRecyclerViewDelete;

        public TextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewHolder holder;
        if (viewType == PHOTO_ITEM_TYPE) {
            View view = inflater.inflate(R.layout.frgament_explore_recycler_view_photo, parent, false);
            holder = new PhotoViewHolder(view);
        } else if (viewType == VIDEO_ITEM_TYPE) {
            View view = inflater.inflate(R.layout.frgament_explore_recycler_view_photo, parent, false);
            holder = new VideoViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.frgament_explore_recycler_view_photo, parent, false);
            holder = new TextViewHolder(view);
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final IsNetWorkConnectedUtils connectedUtils = new IsNetWorkConnectedUtils();

        if (holder instanceof PhotoViewHolder) {
            ((PhotoViewHolder) holder).mExploreItemNinePhoto.setVisibility(View.VISIBLE);
            ((PhotoViewHolder) holder).mExploreVideo.setVisibility(View.GONE);

            final ExploreRecyclerItemBean data = dataList.get(position);
            String description = data.getDescription();
            /*1*/
            if (description.isEmpty()) {
                ((PhotoViewHolder) holder).mExploreExpandableTextView.setVisibility(View.GONE);
            } else {
                ((PhotoViewHolder) holder).mExploreExpandableTextView.setVisibility(View.VISIBLE);
                if (data.getPost().getType() == 1) {
                    ((PhotoViewHolder) holder).mExploreExpandableTextView.setText(description);
                } else {
                    SpannableString spannableString = formatTextToSpannable(data, description);
                    ((PhotoViewHolder) holder).mExploreExpandableTextView.setMovementMethod(LinkMovementMethod.getInstance());
                    ((PhotoViewHolder) holder).mExploreExpandableTextView.setText(spannableString);
                }

            }
            /*2*/
            final String portrait = data.getPortrait();
            if (portrait != null) {
                Glide.with(MyApplication.getContext()).load(portrait).into(((PhotoViewHolder) holder).mExploreRecyclerViewPortrait);
            }
            /*3*/
            String userName = data.getUserName();
            ((PhotoViewHolder) holder).mExploreRecyclerViewUsername.setText(userName);
            /*4*/
            String postTime = data.getPostTime();
            ((PhotoViewHolder) holder).mExploreRecyclerViewTime.setText(postTime);
            /*5*/
            final List<BmobFile> mediaList = data.getMediaList();
            mPhotoAdapter = new ExploreNinePhotoAdapter(mediaList, mFragmentManager);
            GridLayoutManager manager;
            if (mediaList.size() == 1) {
                manager = new GridLayoutManager(MyApplication.getContext(), 1);//单张图片
            } else {
                manager = new GridLayoutManager(MyApplication.getContext(), 3);
            }
            ((PhotoViewHolder) holder).mExploreItemNinePhoto.setLayoutManager(manager);
            if (((PhotoViewHolder) holder).mExploreItemNinePhoto.getAdapter() == null) {
                ((PhotoViewHolder) holder).mExploreItemNinePhoto.addItemDecoration(Divider.builder()
                        .color(Color.parseColor("#FFFFFF"))
                        .width(5)
                        .height(5)
                        .build());
            }
            ((PhotoViewHolder) holder).mExploreItemNinePhoto.setAdapter(mPhotoAdapter);
            /*6*/
            String forwardNum = String.valueOf(data.getForwardNum());
            if (forwardNum.equals("0")) {
                ((PhotoViewHolder) holder).mExploreRePostNum.setText(MyApplication.getContext()
                        .getResources().getString(R.string.forward));
            } else {
                ((PhotoViewHolder) holder).mExploreRePostNum.setText(forwardNum);
            }
            /*7*/
            final String[] likeNum = {String.valueOf(data.getLikeNum())};
            if (likeNum[0].equals("0")) {
                ((PhotoViewHolder) holder).mExploreLikeNum.setText(MyApplication.getContext()
                        .getResources().getString(R.string.like));
            } else {
                ((PhotoViewHolder) holder).mExploreLikeNum.setText(likeNum[0]);
            }
            if (likeStateList.contains(data.getPostId())) {
                ((PhotoViewHolder) holder).mExploreLike.setImageResource(R.drawable.ic_like_select);
            } else {
                ((PhotoViewHolder) holder).mExploreLike.setImageResource(R.drawable.ic_like_nor);
            }
            /*8*/
            final String[] commentNum = {String.valueOf(data.getCommentNum())};
            if (commentNum[0].equals("0")) {
                ((PhotoViewHolder) holder).mExploreCommentNum.setText(MyApplication.getContext().getString(R.string.comment));
            } else {
                ((PhotoViewHolder) holder).mExploreCommentNum.setText(commentNum[0]);
            }
             /*9*/
            if (data.isCurUser()) {
                ((PhotoViewHolder) holder).mExploreRecyclerViewFollow.setVisibility(View.GONE);
                ((PhotoViewHolder) holder).mExploreRecyclerViewDelete.setVisibility(View.VISIBLE);
            } else {
                ((PhotoViewHolder) holder).mExploreRecyclerViewDelete.setVisibility(View.GONE);
                if (data.isFollowed() == null) {
                    ((PhotoViewHolder) holder).mExploreRecyclerViewFollow.setVisibility(View.GONE);
                } else {
                    ((PhotoViewHolder) holder).mExploreRecyclerViewFollow.setVisibility(View.VISIBLE);
                    if (data.isFollowed().isState()) {
                        ((PhotoViewHolder) holder).mExploreRecyclerViewFollow.setText(MyApplication.getContext().getResources().getString(R.string.following));
                    } else if (!data.isFollowed().isState()){
                        ((PhotoViewHolder) holder).mExploreRecyclerViewFollow.setText(MyApplication.getContext().getResources().getString(R.string.follow));
                    }
                }


            }


            /*删除按钮的点击事件*/
            ((PhotoViewHolder) holder).mExploreRecyclerViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(MainActivity.sMainActivity)
                            .setTitle(MyApplication.getContext().getString(R.string.prompt))
                            .setMessage(MyApplication.getContext().getString(R.string.are_you_sure_delete_post))
                            .setNegativeButton(MyApplication.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton(MyApplication.getContext().getString(R.string.sure), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    EventBus.getDefault().post(new ToUpdateExploreItemEB(position));
                                    mExploreItemPresenter.requestDeletePost(data.getPost().getObjectId());
                                }
                            }).create().show();
                }
            });

                /*关注按钮的点击事件*/
            ((PhotoViewHolder) holder).mExploreRecyclerViewFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean netState = connectedUtils.IsNetWorkConnected(MyApplication.getContext());
                    if (netState) {
                        if (data.isFollowed().isState()) {//点击取消关注
                            ((PhotoViewHolder) holder).mExploreRecyclerViewFollow
                                    .setText(MyApplication.getContext().getResources()
                                            .getString(R.string.follow));
                            mExploreItemPresenter.requestUpdateFollow(false, data.getPostUser());//false表示取消关注
                            data.isFollowed().setState(false);
                        } else {//点击关注
                            ((PhotoViewHolder) holder).mExploreRecyclerViewFollow
                                    .setText(MyApplication.getContext().getResources()
                                            .getString(R.string.following));
                            mExploreItemPresenter.requestUpdateFollow(true, data.getPostUser());//true表示关注
                            data.isFollowed().setState(true);
                        }
                    } else {
                        return;
                    }
                }
            });


                /*点赞按钮的点击事件*/
            ((PhotoViewHolder) holder).mExploreLikeLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String postId = dataList.get(position).getPostId();
                    AnimationUtils animationUtils = new AnimationUtils();
                    animationUtils.alphaAnimation(((PhotoViewHolder) holder).mExploreLike);
                    int number;
                    if (likeStateList.contains(postId)) {//表示已经点过赞
                        likeStateList.remove(postId);
                        ((PhotoViewHolder) holder).mExploreLike.setImageResource(R.drawable.ic_like_nor);
                        number = Integer.parseInt(likeNum[0]);
                        number--;
                        dataList.get(holder.getAdapterPosition()).setLikeNum(number);
                        mExploreItemPresenter.requestUpdateLikeNum(number, postId, -1);//1为递增，-1为递减
                        likeNum[0] = String.valueOf(number);
                        if (likeNum[0].equals(0)) {
                            ((PhotoViewHolder) holder).mExploreLikeNum.setText(MyApplication.getContext().getResources().getString(R.string.like));
                        } else {
                            ((PhotoViewHolder) holder).mExploreLikeNum.setText(likeNum[0]);
                        }

                    } else {
                        likeStateList.add(postId);
                        ((PhotoViewHolder) holder).mExploreLike.setImageResource(R.drawable.ic_like_select);
                        number = Integer.parseInt(likeNum[0]);
                        number++;
                        dataList.get(holder.getAdapterPosition()).setLikeNum(number);
                        mExploreItemPresenter.requestUpdateLikeNum(number, postId, 1);//1为递增，0为递减

                        likeNum[0] = String.valueOf(number);
                        ((PhotoViewHolder) holder).mExploreLikeNum.setText(likeNum[0]);
                    }
                }
            });

            /*转发按钮的点击事件*/
            ((PhotoViewHolder) holder).mExploreRePostLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Post post = dataList.get(position).getPost();
                    List<BmobFile> list = dataList.get(position).getMediaList();
                    String imageUrlString = list.get(0).getUrl();
                    String description = dataList.get(position).getDescription();
                    Intent intent = new Intent(MyApplication.getContext(), RepostActivity.class);
                    intent.putExtra("TAG", 1);
                    intent.putExtra("imageUrlString", imageUrlString);
                    intent.putExtra("description", description);
                    intent.putExtra("post", post);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getContext().startActivity(intent);

                }
            });

            /*评论按钮的点击事件*/
            ((PhotoViewHolder) holder).mExploreCommentLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean state = connectedUtils.IsNetWorkConnected(MyApplication.getContext());
                    if (state) {
                        String postId = dataList.get(position).getPostId();
                        Intent intent = CommentActivity.newIntent(MyApplication.getContext(),
                                dataList.get(position).getCommentNum(),
                                postId,
                                dataList.get(position).getPostUser(),
                                dataList.get(position).getDescription());
                        MyApplication.getContext().startActivity(intent);
                    } else {
                        return;
                    }
                }
            });

            /*头像的点击事件*/
            ((PhotoViewHolder) holder).mExploreRecyclerViewPortrait.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean state = connectedUtils.IsNetWorkConnected(MyApplication.getContext());
                    if (state) {
                        MyUser user = data.getPostUser();
                        Intent intent = HomePageActivity.newIntent(MyApplication.getContext(), user);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getContext().startActivity(intent);
                    } else {
                        createSnackBar(((PhotoViewHolder) holder).mExploreRecyclerViewUsername, MyApplication.getContext().getString(R.string.network_error));
                    }

                }
            });

            ((PhotoViewHolder) holder).mExploreRecyclerViewUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean state = connectedUtils.IsNetWorkConnected(MyApplication.getContext());
                    if (state) {
                        MyUser user = data.getPostUser();
                        Intent intent = HomePageActivity.newIntent(MyApplication.getContext(), user);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getContext().startActivity(intent);
                    } else {
                        createSnackBar(((PhotoViewHolder) holder).mExploreRecyclerViewUsername, MyApplication.getContext().getString(R.string.network_error));
                    }
                }
            });

        } else if (holder instanceof VideoViewHolder) {
            ((VideoViewHolder) holder).mExploreItemNinePhoto.setVisibility(View.GONE);
            ((VideoViewHolder) holder).mExploreVideo.setVisibility(View.VISIBLE);

            final ExploreRecyclerItemBean data = dataList.get(position);
            String description = data.getDescription();
            /*1*/
            if (description.isEmpty()) {
                ((VideoViewHolder) holder).mExploreExpandableTextView.setVisibility(View.GONE);
            } else {
                ((VideoViewHolder) holder).mExploreExpandableTextView.setVisibility(View.VISIBLE);
                if (data.getPost().getType() == 1) {
                    ((VideoViewHolder) holder).mExploreExpandableTextView.setText(description);
                } else {
                    SpannableString spannableString = formatTextToSpannable(data, description);
                    ((VideoViewHolder) holder).mExploreExpandableTextView.setMovementMethod(LinkMovementMethod.getInstance());
                    ((VideoViewHolder) holder).mExploreExpandableTextView.setText(spannableString);
                }


            }
            /*2*/
            final String portrait = data.getPortrait();
            if (portrait != null) {
                Glide.with(MyApplication.getContext()).load(portrait).into(((VideoViewHolder) holder).mExploreRecyclerViewPortrait);
            }
            /*3*/
            String userName = data.getUserName();
            ((VideoViewHolder) holder).mExploreRecyclerViewUsername.setText(userName);
            /*4*/
            String postTime = data.getPostTime();
            ((VideoViewHolder) holder).mExploreRecyclerViewTime.setText(postTime);
            /*5*/
            List<BmobFile> mediaList = data.getMediaList();
            String thumbnailUri = data.getThumbnailUri();
            for (BmobFile file : mediaList) {
                ((VideoViewHolder) holder).mExploreVideo.setUp(file.getUrl(), JZVideoPlayerStandard.SCREEN_WINDOW_LIST, "");
                Glide.with(MyApplication.getContext()).load(thumbnailUri).into(((VideoViewHolder) holder).mExploreVideo.thumbImageView);
            }
            /*6*/
            String forwardNum = String.valueOf(data.getForwardNum());
            if (forwardNum.equals("0")) {
                ((VideoViewHolder) holder).mExploreRePostNum.setText(MyApplication
                        .getContext().getString(R.string.forward));
            } else {
                ((VideoViewHolder) holder).mExploreRePostNum.setText(forwardNum);
            }
            /*7*/
            final String[] likeNum = {String.valueOf(data.getLikeNum())};
            if (likeNum[0].equals("0")) {
                ((VideoViewHolder) holder).mExploreLikeNum.setText(MyApplication.getContext()
                        .getString(R.string.like));
            } else {
                ((VideoViewHolder) holder).mExploreLikeNum.setText(likeNum[0]);
            }
            if (likeStateList.contains(data.getPostId())) {
                ((VideoViewHolder) holder).mExploreLike.setImageResource(R.drawable.ic_like_select);
            } else {
                ((VideoViewHolder) holder).mExploreLike.setImageResource(R.drawable.ic_like_nor);
            }
            /*8*/
            final String[] commentNum = {String.valueOf(data.getCommentNum())};
            if (commentNum[0].equals("0")) {
                ((VideoViewHolder) holder).mExploreCommentNum.setText(MyApplication.getContext().getString(R.string.comment));
            } else {
                ((VideoViewHolder) holder).mExploreCommentNum.setText(commentNum[0]);
            }
             /*9*/
            if (data.isCurUser()) {
                ((VideoViewHolder) holder).mExploreRecyclerViewFollow.setVisibility(View.GONE);
                ((VideoViewHolder) holder).mExploreRecyclerViewDelete.setVisibility(View.VISIBLE);
            } else {
                ((VideoViewHolder) holder).mExploreRecyclerViewDelete.setVisibility(View.GONE);
                if (data.isFollowed() == null) {
                    ((VideoViewHolder) holder).mExploreRecyclerViewFollow.setVisibility(View.GONE);
                } else {
                    ((VideoViewHolder) holder).mExploreRecyclerViewFollow.setVisibility(View.VISIBLE);
                    if (data.isFollowed().isState()) {
                        ((VideoViewHolder) holder).mExploreRecyclerViewFollow.setText(MyApplication.getContext().getResources().getString(R.string.following));
                    } else if (!data.isFollowed().isState()){
                        ((VideoViewHolder) holder).mExploreRecyclerViewFollow.setText(MyApplication.getContext().getResources().getString(R.string.follow));
                    }
                }
            }
            /*删除按钮的点击事件*/
            ((VideoViewHolder) holder).mExploreRecyclerViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(MainActivity.sMainActivity)
                            .setTitle(MyApplication.getContext().getString(R.string.prompt))
                            .setMessage(MyApplication.getContext().getString(R.string.are_you_sure_delete_post))
                            .setNegativeButton(MyApplication.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton(MyApplication.getContext().getString(R.string.sure), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    EventBus.getDefault().post(new ToUpdateExploreItemEB(position));
                                    mExploreItemPresenter.requestDeletePost(data.getPost().getObjectId());
                                }
                            }).create().show();
                }
            });


                /*关注按钮的点击事件*/
            ((VideoViewHolder) holder).mExploreRecyclerViewFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean netState = connectedUtils.IsNetWorkConnected(MyApplication.getContext());
                    if (netState) {
                        if (data.isFollowed().isState()) {//点击取消关注
                            ((VideoViewHolder) holder).mExploreRecyclerViewFollow
                                    .setText(MyApplication.getContext().getResources()
                                            .getString(R.string.follow));
                            mExploreItemPresenter.requestUpdateFollow(false, data.getPostUser());//false表示取消关注
                            data.isFollowed().setState(false);
                        } else {//点击关注
                            ((VideoViewHolder) holder).mExploreRecyclerViewFollow
                                    .setText(MyApplication.getContext().getResources()
                                            .getString(R.string.following));
                            mExploreItemPresenter.requestUpdateFollow(true, data.getPostUser());//true表示关注
                            data.isFollowed().setState(true);
                        }
                    } else {
                        return;
                    }
                }
            });

                /*点赞按钮的点击事件*/
            ((VideoViewHolder) holder).mExploreLikeLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String postId = data.getPostId();
                    int number;
                    AnimationUtils animationUtils = new AnimationUtils();
                    animationUtils.alphaAnimation(((VideoViewHolder) holder).mExploreLike);
                    if (likeStateList.contains(postId)) {//表示已经点过赞
                        likeStateList.remove(postId);
                        ((VideoViewHolder) holder).mExploreLike.setImageResource(R.drawable.ic_like_nor);
                        number = Integer.parseInt(likeNum[0]);
                        number--;
                        dataList.get(holder.getAdapterPosition()).setLikeNum(number);
                        mExploreItemPresenter.requestUpdateLikeNum(number, postId, -1);//1为递增，0为递减
                        likeNum[0] = String.valueOf(number);
                        if (likeNum[0].equals(0)) {
                            ((VideoViewHolder) holder).mExploreLikeNum.setText(MyApplication.getContext().getResources().getString(R.string.like));
                        } else {
                            ((VideoViewHolder) holder).mExploreLikeNum.setText(likeNum[0]);
                        }

                    } else {
                        likeStateList.add(postId);
                        ((VideoViewHolder) holder).mExploreLike.setImageResource(R.drawable.ic_like_select);
                        number = Integer.parseInt(likeNum[0]);
                        number++;
                        dataList.get(holder.getAdapterPosition()).setLikeNum(number);
                        mExploreItemPresenter.requestUpdateLikeNum(number, postId, 1);//1为递增，0为递减
                        likeNum[0] = String.valueOf(number);
                        ((VideoViewHolder) holder).mExploreLikeNum.setText(likeNum[0]);
                    }
                }
            });

            /*转发按钮的点击事件*/
            ((VideoViewHolder) holder).mExploreRePostLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Post post = dataList.get(position).getPost();
                    String thumbnailString = dataList.get(position).getThumbnailUri();
                    String description = dataList.get(position).getDescription();
                    Intent intent = new Intent(MyApplication.getContext(), RepostActivity.class);
                    intent.putExtra("TAG", 2);
                    intent.putExtra("imageUrlString", thumbnailString);
                    intent.putExtra("description", description);
                    intent.putExtra("post", post);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getContext().startActivity(intent);
                }
            });

            /*评论按钮的点击事件*/
            ((VideoViewHolder) holder).mExploreCommentLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean state = connectedUtils.IsNetWorkConnected(MyApplication.getContext());
                    if (state) {
                        String postId = dataList.get(position).getPostId();
                        Intent intent = CommentActivity.newIntent(MyApplication.getContext(),
                                dataList.get(position).getCommentNum(),
                                postId,
                                dataList.get(position).getPostUser(),
                                dataList.get(position).getDescription());
                        MyApplication.getContext().startActivity(intent);
                    } else {
                        return;
                    }
                }
            });

             /*头像的点击事件*/
            ((VideoViewHolder) holder).mExploreRecyclerViewPortrait.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean state = connectedUtils.IsNetWorkConnected(MyApplication.getContext());
                    if (state) {
                        MyUser user = data.getPostUser();
                        Intent intent = HomePageActivity.newIntent(MyApplication.getContext(), user);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getContext().startActivity(intent);
                    } else {
                        createSnackBar(((PhotoViewHolder) holder).mExploreRecyclerViewUsername, MyApplication.getContext().getString(R.string.network_error));
                    }

                }
            });

            ((VideoViewHolder) holder).mExploreRecyclerViewUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean state = connectedUtils.IsNetWorkConnected(MyApplication.getContext());
                    if (state) {
                        MyUser user = data.getPostUser();
                        Intent intent = HomePageActivity.newIntent(MyApplication.getContext(), user);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getContext().startActivity(intent);
                    } else {
                        createSnackBar(((PhotoViewHolder) holder).mExploreRecyclerViewUsername, MyApplication.getContext().getString(R.string.network_error));
                    }
                }
            });

        } else if (holder instanceof TextViewHolder) {
            ((TextViewHolder) holder).mExploreItemNinePhoto.setVisibility(View.GONE);
            ((TextViewHolder) holder).mExploreVideo.setVisibility(View.GONE);
            final ExploreRecyclerItemBean data = dataList.get(position);
            String description = data.getDescription();
            /*1*/
            if (!description.isEmpty()) {
                ((TextViewHolder) holder).mExploreExpandableTextView.setVisibility(View.VISIBLE);
                ((TextViewHolder) holder).mExploreExpandableTextView.setText(description);
            } else {
                ((TextViewHolder) holder).mExploreExpandableTextView.setVisibility(View.VISIBLE);
                if (data.getPost().getType() == 1) {
                    ((TextViewHolder) holder).mExploreExpandableTextView.setText(description);
                } else {
                    SpannableString spannableString = formatTextToSpannable(data, description);
                    ((TextViewHolder) holder).mExploreExpandableTextView.setMovementMethod(LinkMovementMethod.getInstance());
                    ((TextViewHolder) holder).mExploreExpandableTextView.setText(spannableString);
                }


            }
            /*2*/
            final String portrait = data.getPortrait();
            if (portrait != null) {
                Glide.with(MyApplication.getContext()).load(portrait).into(((TextViewHolder) holder).mExploreRecyclerViewPortrait);
            }
            /*3*/
            String userName = data.getUserName();
            ((TextViewHolder) holder).mExploreRecyclerViewUsername.setText(userName);
            /*4*/
            String postTime = data.getPostTime();
            ((TextViewHolder) holder).mExploreRecyclerViewTime.setText(postTime);
            /*6*/
            String forwardNum = String.valueOf(data.getForwardNum());
            if (forwardNum.equals("0")) {
                ((TextViewHolder) holder).mExploreRePostNum.setText(MyApplication
                        .getContext().getString(R.string.forward));
            } else {
                ((TextViewHolder) holder).mExploreRePostNum.setText(forwardNum);
            }
            /*7*/
            final String[] likeNum = {String.valueOf(data.getLikeNum())};
            if (likeNum[0].equals("0")) {
                ((TextViewHolder) holder).mExploreLikeNum.setText(MyApplication.getContext()
                        .getString(R.string.like));
            } else {
                ((TextViewHolder) holder).mExploreLikeNum.setText(likeNum[0]);
            }
            if (likeStateList.contains(data.getPostId())) {
                ((TextViewHolder) holder).mExploreLike.setImageResource(R.drawable.ic_like_select);
            } else {
                ((TextViewHolder) holder).mExploreLike.setImageResource(R.drawable.ic_like_nor);
            }
             /*8*/
            final String[] commentNum = {String.valueOf(data.getCommentNum())};
            if (commentNum[0].equals("0")) {
                ((TextViewHolder) holder).mExploreCommentNum.setText(MyApplication.getContext().getString(R.string.comment));
            } else {
                ((TextViewHolder) holder).mExploreCommentNum.setText(commentNum[0]);
            }
            /*9*/
            if (data.isCurUser()) {
                ((TextViewHolder) holder).mExploreRecyclerViewFollow.setVisibility(View.GONE);
                ((TextViewHolder) holder).mExploreRecyclerViewDelete.setVisibility(View.VISIBLE);
            } else {
                ((TextViewHolder) holder).mExploreRecyclerViewDelete.setVisibility(View.GONE);
                if (data.isFollowed()==null){
                    ((TextViewHolder) holder).mExploreRecyclerViewFollow.setVisibility(View.GONE);
                }else {
                    ((TextViewHolder) holder).mExploreRecyclerViewFollow.setVisibility(View.VISIBLE);
                    if (data.isFollowed().isState()) {
                        ((TextViewHolder) holder).mExploreRecyclerViewFollow.setText(MyApplication.getContext().getResources().getString(R.string.following));
                    } else if (!data.isFollowed().isState()){
                        ((TextViewHolder) holder).mExploreRecyclerViewFollow.setText(MyApplication.getContext().getResources().getString(R.string.follow));
                    }
                }
            }

            /*删除按钮的点击事件*/
            ((TextViewHolder) holder).mExploreRecyclerViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(MainActivity.sMainActivity)
                            .setTitle(MyApplication.getContext().getString(R.string.prompt))
                            .setMessage(MyApplication.getContext().getString(R.string.are_you_sure_delete_post))
                            .setNegativeButton(MyApplication.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton(MyApplication.getContext().getString(R.string.sure), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    EventBus.getDefault().post(new ToUpdateExploreItemEB(position));
                                    mExploreItemPresenter.requestDeletePost(data.getPost().getObjectId());
                                }
                            }).create().show();
                }
            });

                /*关注按钮的点击事件*/
            ((TextViewHolder) holder).mExploreRecyclerViewFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean netState = connectedUtils.IsNetWorkConnected(MyApplication.getContext());
                    if (netState) {
                        if (data.isFollowed().isState()) {//点击取消关注
                            ((TextViewHolder) holder).mExploreRecyclerViewFollow
                                    .setText(MyApplication.getContext().getResources()
                                            .getString(R.string.follow));
                            mExploreItemPresenter.requestUpdateFollow(false, data.getPostUser());//false表示取消关注
                            data.isFollowed().setState(false);
                        } else {//点击关注
                            ((TextViewHolder) holder).mExploreRecyclerViewFollow
                                    .setText(MyApplication.getContext().getResources()
                                            .getString(R.string.following));
                            mExploreItemPresenter.requestUpdateFollow(true, data.getPostUser());//true表示关注
                            data.isFollowed().setState(true);
                        }
                    } else {
                        return;
                    }
                }
            });

                /*点赞按钮的点击事件*/
            ((TextViewHolder) holder).mExploreLikeLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String postId = data.getPostId();
                    int number;
                    AnimationUtils animationUtils = new AnimationUtils();
                    animationUtils.alphaAnimation(((TextViewHolder) holder).mExploreLike);
                    if (likeStateList.contains(postId)) {//表示已经点过赞
                        likeStateList.remove(postId);
                        ((TextViewHolder) holder).mExploreLike.setImageResource(R.drawable.ic_like_nor);
                        number = Integer.parseInt(likeNum[0]);
                        number--;
                        dataList.get(holder.getAdapterPosition()).setLikeNum(number);
                        mExploreItemPresenter.requestUpdateLikeNum(number, postId, -1);//1为递增，0为递减
                        likeNum[0] = String.valueOf(number);
                        if (likeNum[0].equals(0)) {
                            ((TextViewHolder) holder).mExploreLikeNum.setText(MyApplication.getContext().getResources().getString(R.string.like));
                        } else {
                            ((TextViewHolder) holder).mExploreLikeNum.setText(likeNum[0]);
                        }

                    } else {
                        likeStateList.add(postId);
                        ((TextViewHolder) holder).mExploreLike.setImageResource(R.drawable.ic_like_select);
                        number = Integer.parseInt(likeNum[0]);
                        number++;
                        dataList.get(holder.getAdapterPosition()).setLikeNum(number);
                        mExploreItemPresenter.requestUpdateLikeNum(number, postId, 1);//1为递增，0为递减
                        likeNum[0] = String.valueOf(number);
                        ((TextViewHolder) holder).mExploreLikeNum.setText(likeNum[0]);
                    }
                }
            });

            /*转发按钮的点击事件*/
            ((TextViewHolder) holder).mExploreRePostLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Post post = dataList.get(position).getPost();
                    String description = dataList.get(position).getDescription();
                    Intent intent = new Intent(MyApplication.getContext(), RepostActivity.class);
                    intent.putExtra("TAG", 3);
                    intent.putExtra("description", description);
                    intent.putExtra("post", post);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getContext().startActivity(intent);
                }
            });

            /*评论按钮的点击事件*/
            ((TextViewHolder) holder).mExploreCommentLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean state = connectedUtils.IsNetWorkConnected(MyApplication.getContext());
                    if (state) {
                        String postId = dataList.get(position).getPostId();
                        Intent intent = CommentActivity.newIntent(MyApplication.getContext(),
                                dataList.get(position).getCommentNum(),
                                postId,
                                dataList.get(position).getPostUser(),
                                dataList.get(position).getDescription());
                        MyApplication.getContext().startActivity(intent);
                    } else {
                        createSnackBar(((TextViewHolder) holder).mExploreRecyclerViewUsername, MyApplication.getContext().getString(R.string.network_error));
                    }
                }
            });

            /*头像的点击事件*/
            ((TextViewHolder) holder).mExploreRecyclerViewPortrait.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean state = connectedUtils.IsNetWorkConnected(MyApplication.getContext());
                    if (state) {
                        MyUser user = data.getPostUser();
                        Intent intent = HomePageActivity.newIntent(MyApplication.getContext(), user);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getContext().startActivity(intent);
                    } else {
                        createSnackBar(((PhotoViewHolder) holder).mExploreRecyclerViewUsername, MyApplication.getContext().getString(R.string.network_error));
                    }

                }
            });


            ((TextViewHolder) holder).mExploreRecyclerViewUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean state = connectedUtils.IsNetWorkConnected(MyApplication.getContext());
                    if (state) {
                        MyUser user = data.getPostUser();
                        Intent intent = HomePageActivity.newIntent(MyApplication.getContext(), user);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getContext().startActivity(intent);
                    } else {
                        createSnackBar(((PhotoViewHolder) holder).mExploreRecyclerViewUsername, MyApplication.getContext().getString(R.string.network_error));
                    }
                }
            });
        }

    }


    private void createSnackBar(View view, String prompt) {
        Snackbar.make(view, prompt, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ExploreRecyclerItemBean itemBean = dataList.get(position);
        List<BmobFile> files = itemBean.getMediaList();
        if (itemBean.getMediaList() != null) {
            BmobFile file = files.get(0);
            String fileName = file.getFilename();
            String[] token = fileName.split("\\.");
            String type = token[1].toLowerCase();
            String[] videoType = new String[]{"avi", "wmv", "mp4", "mpeg4"};
            List<String> videoTypeList = Arrays.asList(videoType);
            if (videoTypeList.contains(type)) {
                return VIDEO_ITEM_TYPE;
            } else {
                return PHOTO_ITEM_TYPE;
            }
        } else {
            return TEXT_ITEM_TYPE;
        }
    }

    private SpannableString formatTextToSpannable(ExploreRecyclerItemBean data, String description) {
        String repostText = data.getPost().getRepostText() + "//";
        String oldUsername = "@" + data.getPost().getOldUser().getUsername() + ":";
        SpannableString spannableString = new SpannableString(repostText + oldUsername + description);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#000000"));
        spannableString.setSpan(colorSpan1, 0, repostText.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.parseColor("#00BFFF"));
        spannableString.setSpan(colorSpan2, repostText.length(), repostText.length() + oldUsername.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#000000"));
        spannableString.setSpan(colorSpan3, repostText.length() + oldUsername.length(),
                repostText.length() + oldUsername.length() + description.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        MyClickableSpan clickableSpan = new MyClickableSpan(data.getPost().getOldUser());
        spannableString.setSpan(clickableSpan, repostText.length(), repostText.length() + oldUsername.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableString;
    }

}

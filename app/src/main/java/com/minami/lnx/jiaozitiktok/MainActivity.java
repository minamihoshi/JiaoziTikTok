package com.minami.lnx.jiaozitiktok;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    private static final int PLAYER_COUNT_MIN = 3;
    private static final int PLAYER_COUNT_MAX = 10;
    private static final String TAG = "jiaozi";
    private RecyclerView mRv ;
    private PagerLayoutManager pagerLayoutManager;
    private LittleVideoListAdapter mAdapter;
    private View mPlayerViewContainer;
    private List<VideoSourceModel> list;

    /**
     * 判断是否处于加载更多的状态中
     */
    private boolean isLoadingMore;
    /**
     * 预加载位置, 默认离底部还有5条数据时请求下一页视频列表
     */
    private static final int DEFAULT_PRELOAD_NUMBER = 5;
    /**
     * 是否点击暂停
     */
    private boolean isPauseClick = false;
    /**
     * 当前选中位置
     */
    //当前播放位置
    private int mCurrentPosition;

    //指定的缓存播放器数量
    private int mPlayerCount = 3;

    //每页数量
    private int MY_PAGE_SIZE = 20;
    /**
     * 数据是否到达最后一也
     */
    private boolean isEnd;
    /**
     * 是否页面正在后台
     */
    private boolean isBackground;
    private boolean hasInvokPlayOnBackground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();

        mRv = findViewById(R.id.rv);
        mAdapter = new LittleVideoListAdapter(this,list);
        mRv.setAdapter(mAdapter);
        pagerLayoutManager = new PagerLayoutManager(this);
        setPlayerCount(5);
        mRv.setLayoutManager(pagerLayoutManager);
        pagerLayoutManager.setOnViewPagerListener(new PagerLayoutManager.OnViewPagerListener() {
            @Override
            public void onInitComplete(int position) {

                if (position == 0) {
                    startPlay(0);
                    prepareData(position);
                }

            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                if (mCurrentPosition == position) {

                    //mQuickPlayer.stopPlay();
                    Log.e(TAG, "停止当前播放器" + position );
                    if (isNext) {
                        for (int i = 1; i < mPlayerCount; i++) {
                            prepareVideo(position + i);
                        }
                    } else {
                        for (int i = 1; i < mPlayerCount; i++) {
                            prepareVideo(position - i);
                        }
                    }
                    //每次stop之后就重置mCurrentPosition，防止stop后，下次再次选中该位置的时候选中位置等于mCurrentPosition
                    //导致不走播放的方法
                    mCurrentPosition = -1;
                }

            }

            @Override
            public void onPageSelected(int position, boolean b) {
                onSelected(position);
            }
        });

        getVideoData(true);

    }


    //
    private void initJiaozi() {
        // TODO: 2019/6/13  获取饺子实例 设置饺子播放器监听
        // mjiaoziView = mPlayerViewContainer.findViewById(R.id.video_textureview);

    }

    /**
     * Activity生命周期onPause发生时调用,防止切换到后台视频继续播放的问题
     */
    public void onPause() {
        super.onPause();
        isBackground = true;
//        mPlayIcon.setVisibility(View.VISIBLE);
//        mQuickPlayer.pausePlay();
        Log.e(TAG, "onPause方法 暂停当前播放器" );
    }

    /**
     * Activity生命周期onResume发生时调用,防止切换到后台视频继续播放的问题
     */
    public void onResume() {
        super.onResume();
        isBackground = false;
        //mPlayIcon.setVisibility(View.GONE);
        if (hasInvokPlayOnBackground) {
            hasInvokPlayOnBackground = false;
            VideoSourceModel video = list.get(mCurrentPosition);
            // mQuickPlayer.startPlay(video);
            Log.e(TAG, "onResume:  当前播放器开始播放 position" + mCurrentPosition );
        } else {
            //mQuickPlayer.resumePlay();
            Log.e(TAG, "onResume: 当前播放器继续播放 position" + mCurrentPosition);
        }

    }

    //获取列表数据
    private void getVideoData(boolean isRefresh) {
        if (isRefresh) {
            reFreshData(generateData());
        }else {
            loadMoreData(generateData());
        }
    }

    private List<VideoSourceModel> generateData(){
        ArrayList<VideoSourceModel> tempList = new ArrayList<VideoSourceModel>();
        for (int i = 0; i < 10; i++) {
            tempList.add(new VideoSourceModel());
        }
        return tempList ;
    }


    //播放
    private void startPlay(int i) {
        //非wifi下不播放
//        if (isWifi) {
//            showWifiDialog();
//            return;
//        }
        VideoSourceModel video = list.get(i);
        // mPlayIcon.setVisibility(View.GONE);
        isPauseClick = false;
        BaseVideoListAdapter.BaseHolder holder = (BaseVideoListAdapter.BaseHolder) mRv.findViewHolderForLayoutPosition(i);
        //防止退出后台之后，再次调用start方法，导致视频播放
        if (!isBackground) {
            Log.e(TAG, "播放当前视频");
            //mQuickPlayer.startPlay(video);
        } else {
            hasInvokPlayOnBackground = true;
        }
    }

    private void prepareData(int position){
        int itemCount = mAdapter.getItemCount();
        if (itemCount - position < DEFAULT_PRELOAD_NUMBER && !isLoadingMore) {
            // 正在加载中, 防止网络太慢或其他情况造成重复请求列表
            isLoadingMore = true;
            loadMoreData(generateData());
        }

        if (itemCount == position + 1 && isEnd) {
            //视频看光了
        }
    }

    private void reFreshData(List<VideoSourceModel> list){
        mAdapter.refreshData(list);
//        if (refreshView != null && refreshView.isRefreshing()) {
//            refreshView.setRefreshing(false);
//            // 加载完毕, 重置加载状态
//        }
        isEnd = false;
        isLoadingMore = false;
    }

    private void loadMoreData(List<VideoSourceModel> list) {
        isLoadingMore = false;
        if (list == null || list.size() < MY_PAGE_SIZE) {
            isEnd = true;
        } else {
            isEnd = false;
        }
        if (mAdapter != null) {
            mAdapter.addMoreData(list);
        }


    }

    /**
     * 预加载视频
     *
     * @param position 预加载视频在列表中的数据
     */
    private void prepareVideo(int position) {
        if (position > 0 && position < list.size()) {
            VideoSourceModel video = list.get(position);
            //mQuickPlayer.prepare(video);
            Log.e(TAG, "预加载播放器 对应position" + position );
        }
    }


    public void onSelected(int position) {
        if (mCurrentPosition == position) {
            return;
        }
        mCurrentPosition = position;

        prepareData(position);
        startPlayIfExist(position);
    }

    //通常有视频是否被封禁等判断
    private void startPlayIfExist(int position) {

        if (checkVideoExist(list.get(position))) {
            startPlay(position);
        }
    }

    boolean checkVideoExist(VideoSourceModel model){
        //根据业务返回
        return true;
    }
    
    /**
     * 视频暂停/恢复的时候使用，
     */
    public void onPauseClick() {
        if (isPauseClick) {
            isPauseClick = false;
//            mPlayIcon.setVisibility(View.GONE);
//            mQuickPlayer.resumePlay();
        } else {
            isPauseClick = true;
//            mPlayIcon.setVisibility(View.VISIBLE);
//            mQuickPlayer.pausePlay();
        }
    }

    /**
     * 设置播放器数量
     *
     * @param playerCount
     */
    public void setPlayerCount(int playerCount) {
        if (playerCount < PLAYER_COUNT_MIN) {
            this.mPlayerCount = PLAYER_COUNT_MIN;
        } else if (playerCount > PLAYER_COUNT_MAX) {
            this.mPlayerCount = PLAYER_COUNT_MAX;
        } else {
            this.mPlayerCount = playerCount;
        }
       // mQuickPlayer.setMaxVideoCount(this.mPlayerCount);
    }
}

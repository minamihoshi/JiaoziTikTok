package com.minami.lnx.jiaozitiktok;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;


/**
 * 视频列表的adapter
 *
 * @author xlx
 */
public class LittleVideoListAdapter extends BaseVideoListAdapter<LittleVideoListAdapter.MyHolder, VideoSourceModel> {
    public static final String TAG = LittleVideoListAdapter.class.getSimpleName();
    public static final int PAYLOAD_LIKE = 1;
    public static final int PAYLOAD_FOCUS = 2;
    private OnItemBtnClick mItemBtnClick;

    public LittleVideoListAdapter(Context context,
                                  List<VideoSourceModel> urlList) {
        super(context, urlList);
    }

    public LittleVideoListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public LittleVideoListAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video, viewGroup, false);
        return new MyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
            super.onBindViewHolder(holder, position, payloads);
        } else {
            int type = (int) payloads.get(0);
            switch (type) {
                //点赞状态改变
                case PAYLOAD_LIKE:
                    break;
                //已关注状态
                case PAYLOAD_FOCUS:
                    break;
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int position) {
        super.onBindViewHolder(myHolder, position);

//        ImageLoader.loadImage(myHolder.ivAvatar, videoModel.avatar);
//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.fitCenter();
//        requestOptions.placeholder(R.drawable.image_video_bg);
       // Glide.with(context).load(videoModel.coverUrl).apply(requestOptions).into(myHolder.ivCover);
        myHolder.tv.setText(String.valueOf(position));

    }

    public final class MyHolder extends BaseVideoListAdapter.BaseHolder {



        private TextView tv;

        MyHolder(@NonNull View itemView) {
            super(itemView);

            tv = itemView.findViewById(R.id.tv);
        }

        @Override
        public ImageView getCoverView() {
            return null;
        }

        @Override
        public ViewGroup getContainerView() {
            return null;
        }

    }

    //接口回调
    public interface OnItemBtnClick {
        void onLikeClick(int position);

        void onShareClick(int position);

        void onFocusClick(int position);

        void onUserClick(int position);

        void onInviteClick(int position);

        void onFaceTimeClick(int position);

    }

    public void setItemBtnClick(
            OnItemBtnClick mItemBtnClick) {
        this.mItemBtnClick = mItemBtnClick;
    }


}

package com.minami.lnx.jiaozitiktok;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频列表的adapter
 * @author xlx
 */
public abstract class BaseVideoListAdapter<VH extends BaseVideoListAdapter.BaseHolder,T extends VideoSourceModel> extends RecyclerView.Adapter<VH> {
    public static final String TAG = LittleVideoListAdapter.class.getSimpleName();
    protected List<T> list;
    protected Context context;

    private Point mScreenPoint = new Point();

    public BaseVideoListAdapter(Context context, List<T> urlList) {
        this.context = context;
        this.list = urlList;
        Log.i(TAG, "BaseVideoListAdapter: "+list);
        //获取屏幕宽高
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mScreenPoint.x = displayMetrics.widthPixels;
        mScreenPoint.y = displayMetrics.heightPixels;

    }
    public BaseVideoListAdapter(Context context){
        this(context,new ArrayList<T>());
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {

//        new ImageLoaderImpl().loadImage(context,coverPath,new ImageLoaderOptions.Builder()
//            .asBitmap()
//            .placeholder(android.R.color.black)
//            .thumbnail(0.1f)
//            .build()
//        ).listener(new ImageLoaderRequestListener<Bitmap>() {
//            @Override
//            public boolean onLoadFailed(String exception, boolean isFirstResource) {
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Bitmap resource, boolean isFirstResource) {
//                //趣视频画面需求,宽填满屏幕,高度自适应
//                float screenWith = mScreenPoint.x;
//                ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
//                float height = screenWith * resource.getHeight()/resource.getWidth();
//
//                layoutParams.width = (int)screenWith;
//                layoutParams.height = (int)height;
//                iv.setLayoutParams(layoutParams);
//                Log.e("eeeee","bitmap width : " + screenWith + " height : " + height);
//                return false;
//            }
//        }).into(iv);

    }
    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    /**
     * 刷新数据
     * @param list
     */
    public void refreshData(List<T> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     * @param list
     */
    public void addMoreData(List<T> list){
        this.list.addAll(list);
        notifyItemRangeInserted(this.list.size()-list.size(),list.size());
    }

    public List<T> getDataList() {
        return list;
    }

    public abstract class BaseHolder extends RecyclerView.ViewHolder  {
        public BaseHolder(View itemView) {
            super(itemView);
        }
        public abstract ImageView getCoverView();
        public abstract ViewGroup getContainerView();
    }


}

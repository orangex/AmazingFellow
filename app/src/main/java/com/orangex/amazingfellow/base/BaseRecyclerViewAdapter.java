package com.orangex.amazingfellow.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by orangex on 2017/11/3.
 */

public abstract class BaseRecyclerViewAdapter<M, VH extends BaseViewHolder<M>> extends RecyclerView.Adapter<VH> {
    private static final String TAG = BaseRecyclerViewAdapter.class.getSimpleName();
    protected List<M> mDataList;
    protected Context mContext;
    protected onItemClickListener mOnItemClickListener;
    protected int mCurrentPos;
    
    public interface onItemClickListener {
        void onItemClick(int position, View view);
    }
    
    public BaseRecyclerViewAdapter(Context context, @Nullable List<M> list) {
        mContext = context;
        mDataList = list;
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        
    }
    
    
    @Override
    public void onBindViewHolder(final VH holder, final int position) {// TODO: 2017/11/3 best practice of setting clicklistener to item in rcv
        holder.setData(mDataList.get(position));
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(position, holder.itemView);
                }
            });
            
        }
    }
    
    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }
    
    @Override
    public long getItemId(int position) {
        mCurrentPos = position;
        return position;
    }
    
    
    public void setOnItemClickListener(onItemClickListener listener) {
        mOnItemClickListener = listener;
    }
    
    public List<M> getDataList() {
        return mDataList;
    }
    
    public void setDatas(List<M> mList) {
        mDataList.clear();
        mDataList.addAll(mList);
        notifyDataSetChanged();
        Log.e(TAG, "setDatas: at" + System.currentTimeMillis());
    }
    
    public void addDatas(int index, List<M> mList) {
        mDataList.addAll(index, mList);
        notifyItemRangeInserted(index, mList.size());
        Log.e(TAG, "setDatas: at" + System.currentTimeMillis());
    }
    
    public void addDatas(List<M> mList) {
        addDatas(0, mList);
    }
    
}
    
    

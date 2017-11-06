package com.orangex.amazingfellow.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by orangex on 2017/11/3.
 */

public abstract class BaseViewHolder<M> extends RecyclerView.ViewHolder {
    protected Context mContext;
    
    public BaseViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(LayoutInflater.from(context).inflate(layoutRes, root, false));
        ButterKnife.bind(this, itemView);
        mContext = context;
    }
    public abstract void setData(M m);
    
}

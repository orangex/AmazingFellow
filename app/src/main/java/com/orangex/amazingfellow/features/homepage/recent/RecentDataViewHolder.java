package com.orangex.amazingfellow.features.homepage.recent;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.orangex.amazingfellow.base.BaseViewHolder;
import com.orangex.amazingfellow.features.homepage.recent.pulling.data.MatchModel;
import com.orangex.amazingfellow.features.homepage.recent.pulling.data.dota.RecentDotaDataViewHolder;

/**
 * Created by orangex on 2017/11/3.
 */

public class RecentDataViewHolder extends BaseViewHolder<MatchModel>{
    private static final String TAG = RecentDotaDataViewHolder.class.getSimpleName();
    
    public RecentDataViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, root, layoutRes);
    }
    
    @Override
    public void setData(MatchModel matchModel) {
        Log.i(TAG, "setData: " + matchModel.toString());
    }
}

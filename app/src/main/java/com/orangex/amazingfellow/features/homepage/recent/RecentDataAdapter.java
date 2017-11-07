package com.orangex.amazingfellow.features.homepage.recent;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.orangex.amazingfellow.base.BaseRecyclerViewAdapter;
import com.orangex.amazingfellow.features.homepage.recent.dota.RecentDotaDataViewHolder;

import java.util.List;

/**
 * Created by orangex on 2017/11/3.
 */

public class RecentDataAdapter extends BaseRecyclerViewAdapter<MatchModel,RecentDataViewHolder> {
    
    public RecentDataAdapter(Context context, @Nullable List<MatchModel> list) {
        super(context, list);
    }
    
    @Override
    public RecentDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MatchModel.TYPE_DOTA:
                return new RecentDotaDataViewHolder(parent.getContext(), parent);
            default:
                return new UnknownDataViewHolder(parent.getContext(), parent);
        }
    }
    
    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).getType();
    }
    
    
    @Override
    public void setDatas(List<MatchModel> matchModels) {
        super.setDatas(matchModels);
        RecentDataHelper.onRecentDataChanged(mDataList);
    }
    
    @Override
    public void addDatas(List<MatchModel> matchModels) {
        super.addDatas(matchModels);
        RecentDataHelper.onRecentDataChanged(mDataList);
    }
    
    @Override
    public void addDatas(int index, List<MatchModel> matchModels) {
        super.addDatas(index, matchModels);
        RecentDataHelper.onRecentDataChanged(mDataList);
    }
    
}

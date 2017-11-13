package com.orangex.amazingfellow.features.homepage.recent;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ViewGroup;

import com.orangex.amazingfellow.base.BaseRecyclerViewAdapter;
import com.orangex.amazingfellow.features.homepage.recent.pulling.data.MatchModel;
import com.orangex.amazingfellow.features.homepage.recent.pulling.data.dota.RecentDotaDataViewHolder;

import java.util.List;

/**
 * Created by orangex on 2017/11/3.
 */

public class RecentDataAdapter extends BaseRecyclerViewAdapter<MatchModel,RecentDataViewHolder> {
    
    private static final String TAG ="datui "+ RecentDataAdapter.class.getSimpleName();
    
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
        return 1;
        //if (mDataList.get(position).getClass()
    }
    
    
    @Override
    public void setDatas(List<MatchModel> matchModels) {
        Log.i(TAG, "set timeline size " + matchModels.size());
        super.setDatas(matchModels);
        
    }
    
    @Override
    public void addDatas(List<MatchModel> matchModels) {
        super.addDatas(matchModels);
    }
    
    @Override
    public void addDatas(int index, List<MatchModel> matchModels) {
        Log.i(TAG, "addDatas: "+matchModels.toString());
        super.addDatas(index, matchModels);
    }
    
    public void fillMissingDatas(List<MatchModel> matchModels) {
        
        Log.i(TAG, "fillMissingDatas: origin size " + mDataList.size() + " now come size " + matchModels.size());
    
        super.setDatas(matchModels);
//        for (int i =0; i <matchModels.size();i++) {
//            MatchModel model = matchModels.get(i);
//            if (!mDataList.contains(model)) {
//                boolean hasInsert = false;
//                for (int j = 0; i < mDataList.size(); j++) {
//                    if (Long.parseLong(model.getId()) > Long.parseLong(mDataList.get(j).getId())) {
//                        mDataList.add(j, model);
//                        notifyItemInserted(j);
//                        hasInsert = true;
//                        break;
//                    }
//                }
//                if (!hasInsert) {
//                    mDataList.add(model);
//                    notifyItemInserted(mDataList.size());
//                }
//            }
//        }
        
//        List<MatchModel> temp = mDataList;
//        mDataList.clear();
//        mDataList.addAll(matchModels);
//        int lastPos = -1;
//        for (int i = 0; i < temp.size(); i++) {
//            int pos = mDataList.indexOf(temp.get(i));
//            notifyItemRangeInserted(i, pos - lastPos - 1);
//            lastPos = pos;
//        }
//        notifyItemRangeInserted(temp.size(), mDataList.size() - lastPos);


//        for (int i=0; i<mDataList.size();i++) {
//            if (!temp.contains(mDataList.get(i))) {
//                notifyItemInserted(i);
//            }
//        }
    }
}

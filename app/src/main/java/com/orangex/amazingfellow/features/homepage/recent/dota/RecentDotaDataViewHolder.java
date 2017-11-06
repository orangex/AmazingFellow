package com.orangex.amazingfellow.features.homepage.recent.dota;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orangex.amazingfellow.R;
import com.orangex.amazingfellow.features.homepage.recent.MatchModel;
import com.orangex.amazingfellow.features.homepage.recent.RecentDataViewHolder;

import butterknife.BindView;

/**
 * Created by orangex on 2017/11/3.
 */

public class RecentDotaDataViewHolder extends RecentDataViewHolder {
    @BindView(R.id.textView)
    TextView mTextView;
    private static final String TAG = RecentDotaDataViewHolder.class.getSimpleName();
    
    public RecentDotaDataViewHolder(Context context, ViewGroup parent) {
        super(context,parent, R.layout.item_recent_dota);
    }
    
    @Override
    public void setData(MatchModel matchModel) {
        super.setData(matchModel);
        DotaMatchModel dotaDataModel = (DotaMatchModel) matchModel;
        mTextView.setText(dotaDataModel.toString());
        Log.i(TAG, "setData: " + dotaDataModel.toString());
    }
}

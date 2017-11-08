package com.orangex.amazingfellow.features.homepage.recent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.orangex.amazingfellow.R;
import com.orangex.amazingfellow.base.BaseFragment;
import com.orangex.amazingfellow.utils.DotaUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecentFragment extends BaseFragment {// TODO: 2017/11/3 Lazy load of Fragment
    public static final String TAG = RecentFragment.class.getSimpleName();
    @BindView(R.id.rcv_recent)
    RecyclerView mRcvRecent;
    
    @BindView(R.id.refreshLayout_recent)
    SmartRefreshLayout mSmartRefreshLayoutRecent;
    
    private RecentDataAdapter mRecentDataAdapter;
    
    
    private Observer<List<MatchModel>> mGetRecentDataObserver = new Observer<List<MatchModel>>() {
        @Override
        public void onSubscribe(Disposable d) {
        
        }
    
        @Override
        public void onNext(List<MatchModel> matchModels) {
            mRecentDataAdapter.addDatas(matchModels);
        }
        
    
        @Override
        public void onError(Throwable e) {
        
        }
    
        @Override
        public void onComplete() {
        
        }
    };
    
//    public static RecentFragment newInstance(){
//        return newInstance(null);
//    }
//    public static RecentFragment newInstance(Bundle bundle){
//        RecentFragment recentFragment = new RecentFragment();
//        if (bundle != null) {
//            recentFragment.setArguments(bundle);
//        }
//        return recentFragment;
//    }

    public RecentFragment() {
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
    
    @Override
    protected void parseArgs(Bundle bundle) {
    
    }
    
    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        mRecentDataAdapter = new RecentDataAdapter(mContext, null);
        // TODO: 2017/11/4  mRecentDataAdapter.registerAdapterDataObserver();
        mRcvRecent.setAdapter(mRecentDataAdapter);
        mRcvRecent.setLayoutManager(new LinearLayoutManager(mContext));
    }
    
    @Override
    protected void initDatas(Bundle savedInstanceState) {
        Log.e(TAG, "initDatas: at" + System.currentTimeMillis());
        DotaUtil.initNameMap();
        RecentDataHelper.getRecentMVPMoments(getGetRecentDataObserver());
    }
    
    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_home;
    }
    
    public Observer<List<MatchModel>> getGetRecentDataObserver() {
        return mGetRecentDataObserver;
    }
}

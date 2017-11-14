package com.orangex.amazingfellow.features.homepage.recent;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.orangex.amazingfellow.R;
import com.orangex.amazingfellow.base.BaseFragment;
import com.orangex.amazingfellow.features.homepage.recent.pulling.data.MatchModel;
import com.orangex.amazingfellow.features.homepage.recent.pulling.data.RecentDataHelper;
import com.orangex.amazingfellow.utils.AccountUtil;
import com.orangex.amazingfellow.utils.DotaUtil;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import jp.wasabeef.recyclerview.animators.FlipInLeftYAnimator;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecentFragment extends BaseFragment {// TODO: 2017/11/3 Lazy load of Fragment
    public static final String TAG ="datui "+ RecentFragment.class.getSimpleName();
    @BindView(R.id.rcv_recent)
    RecyclerView mRcvRecent;
    
    @BindView(R.id.refreshLayout_recent)
    SmartRefreshLayout mSmartRefreshLayoutRecent;
    
    @BindView(R.id.refreshHeader_recent)
    MaterialHeader mHeader;
    private RecentDataAdapter mRecentDataAdapter;
    
    @BindView(R.id.fab_upward)
    FloatingActionButton mUpwardFab;
    
    
    private Observer<List<MatchModel>> mRefreshRecentDataObserver = new Observer<List<MatchModel>>() {
        @Override
        public void onSubscribe(Disposable d) {
        
        }
    
        @Override
        public void onNext(List<MatchModel> matchModels) {
            mRecentDataAdapter.addDatas(0, matchModels);
            mSmartRefreshLayoutRecent.finishRefresh(500);
        }
        
    
        @Override
        public void onError(Throwable e) {
        
        }
    
        @Override
        public void onComplete() {
        }
    };
    
    private Observer<List<MatchModel>> mLoadMoreRecentDataObserver = new Observer<List<MatchModel>>() {
        @Override
        public void onSubscribe(Disposable d) {
        
        }
        
        @Override
        public void onNext(List<MatchModel> matchModels) {
            Log.i(TAG, "timeline loadmore with " + matchModels.size());
            mSmartRefreshLayoutRecent.finishLoadmore(200);
            mRecentDataAdapter.addDatas(matchModels);
        }
        
        
        @Override
        public void onError(Throwable e) {
        
        }
        
        @Override
        public void onComplete() {
        }
    };
    private Observer<List<MatchModel>> mFillMissingDataObserver=new Observer<List<MatchModel>>() {
        @Override
        public void onSubscribe(Disposable d) {
        
        }
    
        @Override
        public void onNext(List<MatchModel> matchModels) {
            
            mRecentDataAdapter.fillMissingDatas(matchModels);
        }
    
        @Override
        public void onError(Throwable e) {
        
        }
    
        @Override
        public void onComplete() {
        
        }
    };

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
        mSmartRefreshLayoutRecent.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Log.i(TAG, "onRefresh: ");
                mSmartRefreshLayoutRecent.autoRefresh();
                RecentDataHelper.getRecentMVPMoments(mFillMissingDataObserver,mRefreshRecentDataObserver, RecentDataHelper.TYPE_REFRESH);
            }
        });
        mSmartRefreshLayoutRecent.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                Log.i(TAG, "onLoadmore: ");
                mSmartRefreshLayoutRecent.autoLoadmore();
                RecentDataHelper.getRecentMVPMoments(mFillMissingDataObserver,mLoadMoreRecentDataObserver, RecentDataHelper.TYPE_LOADMORE);
            }
        });
//        mSmartRefreshLayoutRecent.setEnableAutoLoadmore(false);
//        mSmartRefreshLayoutRecent.setEnableScrollContentWhenLoaded(false);
        mRecentDataAdapter = new RecentDataAdapter(mContext, null);
        // TODO: 2017/11/4  mRecentDataAdapter.registerAdapterDataObserver();
        mRcvRecent.setItemAnimator(new FlipInLeftYAnimator());
    
        mRcvRecent.addItemDecoration(new SpaceItemDecorator(10));
        
        mRcvRecent.setAdapter(mRecentDataAdapter);
        mRcvRecent.setLayoutManager(new LinearLayoutManager(mContext));
        mRcvRecent.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (manager.findFirstVisibleItemPosition() == 0) {
                    mUpwardFab.setVisibility(View.INVISIBLE);
                } else {
                    mUpwardFab.setVisibility(View.VISIBLE);
                }
            }
        });
        mUpwardFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRcvRecent.smoothScrollToPosition(0);
            }
        });
        
    }
    
    @Override
    protected void initDatas(Bundle savedInstanceState) {
        Log.e(TAG, "initDatas: at" + System.currentTimeMillis());
        DotaUtil.initNameMap();
        if (AccountUtil.hasBindSteam()) {
            mSmartRefreshLayoutRecent.autoRefresh();
        }

    }
    
    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_home;
    }
    
    public Observer<List<MatchModel>> getRefreshRecentDataObserver() {
        return mRefreshRecentDataObserver;
    }
    
    public void autoSmartRefresh() {
        mSmartRefreshLayoutRecent.autoRefresh();
    }
    
    private class SpaceItemDecorator extends RecyclerView.ItemDecoration {
        int space;
        public SpaceItemDecorator(int space) {
            this.space = space;
        }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            if (position == 0) {
                outRect.top = 2*space;
            }
            outRect.bottom = 2 * space;
            outRect.left = space;
            outRect.right = space;
        }
    }
}

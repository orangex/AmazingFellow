package com.orangex.amazingfellow.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by orangex on 2017/11/3.
 */

public abstract class BaseFragment extends Fragment {
    protected Context mContext;
    
    public static <T extends BaseFragment> T newInstance(Class<T> cls, @Nullable Bundle bundle) {
        T fragment = null;
        try {
            fragment = cls.newInstance();
        } catch (java.lang.InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (fragment != null && bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
    

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArgs(getArguments());
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    
        View contentView = inflater.inflate(getLayoutResID(), container, false);
        ButterKnife.bind(this, contentView);
        initViews(contentView, savedInstanceState);
        return contentView;
    }
    
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDatas(savedInstanceState);
    }
    
    protected abstract void parseArgs(Bundle bundle);
    
    protected abstract void initDatas(Bundle savedInstanceState);
    
    protected abstract void initViews(View view, Bundle savedInstanceState);
    
    protected abstract int getLayoutResID();
    

    
}



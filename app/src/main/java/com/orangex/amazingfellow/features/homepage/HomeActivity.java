package com.orangex.amazingfellow.features.homepage;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orangex.amazingfellow.R;
import com.orangex.amazingfellow.features.bind.BindManager;
import com.orangex.amazingfellow.features.homepage.recent.RecentDataHelper;
import com.orangex.amazingfellow.features.homepage.recent.RecentFragment;
import com.orangex.amazingfellow.view.ProgressDialogObserver;

import java.util.List;

public class HomeActivity extends AppCompatActivity {// TODO: 2017/11/3 import dagger2
    private static final String TAG = HomeActivity.class.getSimpleName();
    private RecentFragment mRecentFragment;
    final ProgressDialogObserver<List<String>> bindSteamIdObserver = new ProgressDialogObserver<List<String>>(HomeActivity.this) {
        @Override
        public void onNext(List<String> strings) {
            RecentDataHelper.getRecentMVPMoments(strings,mRecentFragment.getGetRecentDataObserver());
        }
    };

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
    }
    
    private void initViews() {
        initFragments();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(HomeActivity.this)
                        .title(R.string.title_dialog_bind_steam)
                        .content(R.string.tip_dialog_bind_steam)
                        .input(R.string.input_hint_dialog_bind_steam, R.string.input_prefill_dialog_bind_steam, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                BindManager.bindSteamId(input.toString(), bindSteamIdObserver);
                            }
                        }).show();
            
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            
                            }
                        }).show();
            }
        });
        
    }
    
    private void initFragments() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        RecentFragment recentFragment = RecentFragment.newInstance(RecentFragment.class, null);
        mRecentFragment = recentFragment;
        ft.replace(R.id.container_content, recentFragment);
        ft.commit();
    }
    
}

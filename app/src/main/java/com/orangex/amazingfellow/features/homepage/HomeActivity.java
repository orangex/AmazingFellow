package com.orangex.amazingfellow.features.homepage;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.orangex.amazingfellow.R;
import com.orangex.amazingfellow.features.bind.BindManager;
import com.orangex.amazingfellow.features.homepage.recent.RecentFragment;
import com.orangex.amazingfellow.features.homepage.recent.pulling.data.RecentDataHelper;
import com.orangex.amazingfellow.utils.AccountUtil;
import com.orangex.amazingfellow.view.ProgressDialogObserver;

import java.util.List;

public class HomeActivity extends AppCompatActivity {// TODO: 2017/11/3 import dagger2
    private static final String TAG = HomeActivity.class.getSimpleName();
    private RecentFragment mRecentFragment;
    final ProgressDialogObserver<List<String>> bindSteamIdObserver = new ProgressDialogObserver<List<String>>(HomeActivity.this) {
        @Override
        public void onNext(List<String> strings) {
            RecentDataHelper.getRecentMVPMoments(mRecentFragment.getRefreshRecentDataObserver(),RecentDataHelper.TYPE_REFRESH);
        }
    };
    
    private MaterialDialog.Builder mBindSteamDialog;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.i(TAG, "onCreate: timeline test " + Thread.currentThread());
        initViews();
        RecentDataHelper.startPulling();
      
    }
    
    private void initViews() {
        initFragments();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        mBindSteamDialog = new MaterialDialog.Builder(HomeActivity.this)
                .title(R.string.title_dialog_bind_steam)
                .customView(R.layout.dlg_content_bind_steam_guide, false)
                .positiveText("绑定")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String input = ((EditText) dialog.findViewById(R.id.edt_input_steam_url)).getText().toString().trim();
                        BindManager.bindSteamId(input, bindSteamIdObserver);
                    }
                });
        if (!AccountUtil.hasBindSteam()) {
            mBindSteamDialog.show();
        }
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

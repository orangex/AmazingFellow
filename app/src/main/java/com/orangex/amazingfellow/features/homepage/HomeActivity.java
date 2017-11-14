package com.orangex.amazingfellow.features.homepage;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private static final String TAG ="datui "+ HomeActivity.class.getSimpleName();
    private RecentFragment mRecentFragment;
    final ProgressDialogObserver<List<String>> bindSteamIdObserver = new ProgressDialogObserver<List<String>>(HomeActivity.this) {
        @Override
        public void onNext(List<String> strings) {
            Log.i(TAG, "bind complete at "+ DateUtils.formatDateTime(HomeActivity.this,System.currentTimeMillis(),DateUtils.FORMAT_ABBREV_ALL));
            mRecentFragment.autoSmartRefresh();
        }
    };
    
    private MaterialDialog.Builder mBindSteamDialog;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        RecentDataHelper.loadfromDB();
        RecentDataHelper.startPulling();
    }
    
    private void initViews() {
        initFragments();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.title_app_name);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
//            case R.id.action_more:
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
    
}

package com.orangex.amazingfellow.features.homepage;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orangex.amazingfellow.R;
import com.orangex.amazingfellow.view.ProgressDialogObserver;
import com.orangex.amazingfellow.features.bind.BindManager;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();
    final ProgressDialogObserver<String> bindSteamIdObserver=new ProgressDialogObserver<String>(HomeActivity.this) {
        @Override
        public void onNext(Object o) {

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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

}

package com.orangex.amazingfellow.features.bind;

import android.app.Activity;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orangex.amazingfellow.R;

import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by chengyuan.wang on 2017/10/26.
 */

public class BindManager {
    public static void showBindDialog(View fromClick, Activity activity) {
        new MaterialDialog.Builder(activity)
                .title(R.string.bind_steam_dialog_title)
                .content(R.string.bind_steam_dialog_tip)
                .input(R.string.bind_steam_dialog_input_hint, R.string.bind_steam_dialog_input_prefill, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {

                    }
                }).show();
    }

    public static void bindSteamId(String input) {
        Observable.just(input)
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String input) throws Exception {
                        Pattern.compile(".*steamcommunity\\.com/profiles/(\\d+)").matcher(input).matches()

                    }
                })
    }
}

package com.orangex.amazingfellow.utils;

import android.text.TextUtils;

import com.orangex.amazingfellow.base.AFApplication;
import com.orangex.amazingfellow.constant.PrefKeys;
import com.white.easysp.EasySP;

/**
 * Created by orangex on 2017/11/5.
 */

public class AccountUtil {
    private static final Long STEAM_ID_64_TO_32 = Long.valueOf("76561197960265728");
    
    public static String getSteamID32By64(String steamId64) {
        return String.valueOf(Long.valueOf(steamId64) - STEAM_ID_64_TO_32);
    }
    
    
    public static boolean hasBindSteam() {
        return !TextUtils.isEmpty(EasySP.init(AFApplication.getAppContext()).getString(PrefKeys.STEAM_ID));
    }
    
}

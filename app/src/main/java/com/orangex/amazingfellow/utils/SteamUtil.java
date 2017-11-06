package com.orangex.amazingfellow.utils;

/**
 * Created by orangex on 2017/11/5.
 */

public class SteamUtil {
    private static final Long STEAM_ID_64_TO_32 = Long.valueOf("76561197960265728");
    
    public static String getSteamID32By64(String steamId64) {
        return String.valueOf(Long.valueOf(steamId64) - STEAM_ID_64_TO_32);
    }
    
    
}

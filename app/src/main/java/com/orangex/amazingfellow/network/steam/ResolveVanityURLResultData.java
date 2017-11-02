package com.orangex.amazingfellow.network.steam;

/**
 * Created by chengyuan.wang on 2017/10/30.
 * https://wiki.teamfortress.com/wiki/WebAPI/ResolveVanityURL
 */

public class ResolveVanityURLResultData {
    public static int CODE_SUCCESSFUL = 1;
    public static int CODE_NO_MATCH = 42;
    /**
     * 1 if successful, 42 if there was no match.
     */
    private int success;
    /**
     * Optional . Not returned on resolution failures.
     */
    private String steamid;
    /**
     * Optional .The message associated with the request status. Currently only used on resolution failures.
     */
    private String message;

    public int getSuccess() {
        return success;
    }

    public String getSteamid() {
        return steamid;
    }

    public String getMessage() {
        return message;
    }
}

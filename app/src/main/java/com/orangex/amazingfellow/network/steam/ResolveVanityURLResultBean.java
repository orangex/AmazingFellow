package com.orangex.amazingfellow.network.steam;

/**
 * Created by chengyuan.wang on 2017/10/30.
 * https://wiki.teamfortress.com/wiki/WebAPI/ResolveVanityURL
 */

public class ResolveVanityURLResultBean {
    
    public static int CODE_SUCCESSFUL = 1;
    public static int CODE_NO_MATCH = 42;
    
    /**
     * 1 if successful, 42 if there was no match.
     */
    
    private ResponseBean response;
    
    public ResponseBean getResponse() {
        return response;
    }
    
    public void setResponse(ResponseBean response) {
        this.response = response;
    }
    
    public static class ResponseBean {
        /**
         * steamid : 76561198094151923
         * success : 1
         */
        
        private String steamid;
        private int success;
        
        public String getSteamid() {
            return steamid;
        }
        
        public void setSteamid(String steamid) {
            this.steamid = steamid;
        }
        
        public int getSuccess() {
            return success;
        }
        
        public void setSuccess(int success) {
            this.success = success;
        }
    }
}

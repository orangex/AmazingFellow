package com.orangex.amazingfellow.network.steam;

import com.orangex.amazingfellow.network.steam.dota.DotaMatchDetailResultBean;
import com.orangex.amazingfellow.network.steam.dota.DotaMatchHistoryResultBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by orangex on 2017/10/28.
 */

public interface ISteamApiService  {
    String baseUrl = "https://api.steampowered.com/";

    @GET("ISteamUser/ResolveVanityURL/v0001/")
    Observable<ResolveVanityURLResultBean> getSteamIdByVanityUrl(@Query("key") String key, @Query("vanityurl") String vanityurl);
    
    @GET("ISteamUser/GetFriendList/v0001/")
    Observable<FriendListResultBean> getFriendList(@Query("key") String key, @Query("steamid") String steamid64, @Query("relationship") String relationship);
    
    @GET("IDOTA2Match_570/GetMatchHistory/V1/")
    Observable<DotaMatchHistoryResultBean> getMatchHistory(@Query("key") String key, @Query("account_id") String steamId, @Query("start_at_match_id") long mostRecentMatchID);
    
    @GET("IDOTA2Match_570/GetMatchHistory/V1/")
    Observable<DotaMatchHistoryResultBean> getMatchHistory(@Query("key") String key, @Query("account_id") String steamId);
    
    @GET("IDOTA2Match_570/GetMatchDetails/v1/")
    Observable<DotaMatchDetailResultBean> getMatchDetail(@Query("key") String key, @Query("match_id") long matchId);
}

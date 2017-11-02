package com.orangex.amazingfellow.network.steam;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by orangex on 2017/10/28.
 */

public interface ISteamApiService  {
    String baseUrl = "https://api.steampowered.com/";

    @GET("ISteamUser/ResolveVanityURL/v0001/")
    Observable<ResolveVanityURLResultData> getSteamIdByVanityUrl(@Query("key") String key, @Query("vanityurl") String vanityurl);

    @GET("IDOTA2Match_570/GetMatchHistory/V001/")
    Observable<MatchHistoryResultData> getMatchHistory(@Query("account_id") String steamId, @Query("start_at_match_id") long mostRecentMatchID);

}

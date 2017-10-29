package com.orangex.amazingfellow.network.steam;

import java.util.Observable;

import retrofit2.Response;
import retrofit2.http.GET;

/**
 * Created by orangex on 2017/10/28.
 */

public interface ISteamApiService  {
    String baseUrl = "https://api.steampowered.com/";
    
    @GET("ISteamUser/ResolveVanityURL/v0001/")
    Observable<Response<>>
}

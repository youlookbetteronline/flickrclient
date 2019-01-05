package com.example.gav.flickrclient.api;

import com.example.gav.flickrclient.model.Result;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickrApi {
    String API_KEY = "ebddd78f9a35a068bf338ec461059b5f";
    String SECRET = "36760937264341e9";

    @GET("services/rest/")
    Observable<Result> listRepos(
            @Query("method") String method,
            @Query("api_key") String apiKey,
            @Query("format") String format,
            @Query("nojsoncallback") int noJsonCallback
    );

}

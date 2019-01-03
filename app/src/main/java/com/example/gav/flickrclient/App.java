package com.example.gav.flickrclient;

import android.app.Application;
import android.content.Context;

import com.example.gav.flickrclient.api.FlickrApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    private FlickrApi flickrApi;
    public FlickrApi getFlickrApi() {
        if (flickrApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.flickr.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            flickrApi = retrofit.create(FlickrApi.class);
        }
        return flickrApi;
    }

    public static App getApp(Context context) {
        return ((App) context.getApplicationContext());
    }
}

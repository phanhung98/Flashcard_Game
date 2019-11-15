package com.example.flashcard_game.Retrofit;

import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit ourInstance;

   public static Retrofit getInstance() {

       if (ourInstance == null){
           ourInstance= new Retrofit.Builder()
                   .baseUrl("https://gist.githubusercontent.com/cc-vietvo/1b049dc214dd09b01d62b9a26e9171c9/raw/")
                   .addConverterFactory(GsonConverterFactory.create())
                   .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                   .build();
       }
        return ourInstance;
    }

    private RetrofitClient() {
    }
}

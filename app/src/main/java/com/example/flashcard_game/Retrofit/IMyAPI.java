package com.example.flashcard_game.Retrofit;

import com.example.flashcard_game.Models.Games;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface IMyAPI {

    @GET("1b049dc214dd09b01d62b9a26e9171c9/raw/games_list")
    Observable<List<Games>> getGames();

}

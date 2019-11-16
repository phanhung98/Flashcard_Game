package com.example.flashcard_game.Retrofit;

import com.example.flashcard_game.Models.Games;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface IMyAPI {

    @GET("games_list")
    Observable<List<Games>> getGames();

}

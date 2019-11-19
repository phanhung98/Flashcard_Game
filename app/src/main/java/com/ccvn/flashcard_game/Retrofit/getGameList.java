package com.ccvn.flashcard_game.Retrofit;

import com.ccvn.flashcard_game.Models.Games;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface getGameList {

    @GET(ApiUtils.GAME_LIST)
    Observable<List<Games>> getGames();

}

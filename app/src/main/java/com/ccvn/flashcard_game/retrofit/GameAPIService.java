package com.ccvn.flashcard_game.retrofit;

import com.ccvn.flashcard_game.models.Game;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GameAPIService {

    @GET(ApiUtils.URL_GAME_LIST)
    Observable<List<Game>> getGames();

//    @POST()
//    Observable<Integer> getScore();

}

package com.ccvn.flashcard_game.retrofit;

import com.ccvn.flashcard_game.models.Flashcard;
import com.ccvn.flashcard_game.models.Game;
import com.ccvn.flashcard_game.models.Score;
import com.ccvn.flashcard_game.views.GamePlayActivity;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GameAPIService {

    @GET(APIUtils.URL_GAME_LIST1)
    Observable<List<Game>> getGame();

    @GET
    Observable<Flashcard> getFlashcard(@Url String url);

    @GET
    Observable<Game> getGameDetail(@Url String url);

    @GET(APIUtils.URL_HIGHSCORE)
    Observable<List<Score>> getHighScore();

    @POST("post")
    @FormUrlEncoded
    Observable<Score> insertScore(@Field("user") String user,
                                    @Field("score") double score,
                                   @Field("Name") String gameName );

}

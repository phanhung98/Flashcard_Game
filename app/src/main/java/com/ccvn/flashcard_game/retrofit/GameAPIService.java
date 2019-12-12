package com.ccvn.flashcard_game.retrofit;

import com.ccvn.flashcard_game.models.Flashcard;
import com.ccvn.flashcard_game.models.Game;
import com.ccvn.flashcard_game.models.Score;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GameAPIService {

    @GET(APIUtils.URL_GAME_LIST)
    Observable<List<Game>> getGame();

    @GET
    Observable<Flashcard> getFlashcard(@Url String url);

    @GET
    Observable<Game> getGameDetail(@Url String url);

    @GET(APIUtils.URL_HIGHSCORE)
    Observable<List<Score>> getHighScore();

    @POST("insertScore.php")
    @FormUrlEncoded
    Observable<Score> insertScore(@Field("gameId") String mGameId,
                                    @Field("score") double mScore,
                                   @Field("name") String mName ,
                                  @Field("age") String mAge ,
                                  @Field("sex") String mSex);

}

package com.ccvn.flashcard_game.retrofit;

import com.ccvn.flashcard_game.models.Flashcard;
import com.ccvn.flashcard_game.models.Game;
import com.ccvn.flashcard_game.models.Score;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GameAPIService {

    @GET(APIUtils.URL_GAME_LIST)
    Observable<List<Game>> get_game();

    @GET(APIUtils.URL_GAMEPLAY)
    Observable<List<Flashcard>> get_flash_card();

    @GET(APIUtils.URL_GAME_DETAIL)
    Observable<List<Game>> get_game_detail();


    @POST("post")
    @FormUrlEncoded
    Observable<Score> insert_score(@Field("user") String user,
                                    @Field("score") double score,
                                   @Field("Name") String gameName );

}

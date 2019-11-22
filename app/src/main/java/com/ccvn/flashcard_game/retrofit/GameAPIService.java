package com.ccvn.flashcard_game.retrofit;

import com.ccvn.flashcard_game.models.Flashcard;
import com.ccvn.flashcard_game.models.Game;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface GameAPIService {

    @GET(APIUtils.URL_GAME_LIST)
    Observable<List<Game>> get_game();

    @GET(APIUtils.URL_GAMEPLAY)
    Observable<List<Flashcard>> get_flash_card();

    @GET(APIUtils.URL_GAME_DETAIL)
    Observable<List<Game>> get_game_detail();


//    @POST()
//    Observable<Integer> get_score();

}

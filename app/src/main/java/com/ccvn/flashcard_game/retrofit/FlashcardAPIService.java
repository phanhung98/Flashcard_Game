package com.ccvn.flashcard_game.retrofit;

import com.ccvn.flashcard_game.models.Flashcard;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface FlashcardAPIService {

    @GET(ApiUtils.FLASHCARD)
    Observable<List<Flashcard>> get_flash_card();

}

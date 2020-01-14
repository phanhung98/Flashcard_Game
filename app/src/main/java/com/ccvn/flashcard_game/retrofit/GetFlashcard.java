package com.ccvn.flashcard_game.retrofit;

import com.ccvn.flashcard_game.models.Flashcard;

public interface GetFlashcard {

    interface View {
        void getCard(Flashcard flashcard);
    }
    interface Presenter{
        void handleFlashcard(String url);
    }
}

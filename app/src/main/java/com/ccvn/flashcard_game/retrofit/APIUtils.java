package com.ccvn.flashcard_game.retrofit;

public class APIUtils {


        public static final String BASE_URL = "https://flashcard.dev.codecomplete.asia/api/";
        public static final String URL_GAME_LIST = "games/";
        public static final String URL_FLASHCARD = "flashcards/";
        public static final String URL_SCORE = "game/";
        public static final String URL_HIGHSCORE = "highscore";
        public static final String URL_GAMEHIGHSCORE = "gamescore/";

        private APIUtils(){

        }

        public static GameAPIService getAPIService(){
                return APIClient.getInstance(BASE_URL).create(GameAPIService.class);
        }

}

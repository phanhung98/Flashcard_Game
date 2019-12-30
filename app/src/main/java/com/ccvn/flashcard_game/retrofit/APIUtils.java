package com.ccvn.flashcard_game.retrofit;

public class APIUtils {

<<<<<<< HEAD
        public static final String BASE_URL = "https://raw.githubusercontent.com/phanhung98/FakeApi/master/";
        public static final String URL_GAME_LIST = "games";
=======

        public static final String BASE_URL = "http://192.168.1.30:8000/api/";
        public static final String URL_GAME_LIST = "games/";
>>>>>>> 6357388020f41094f882b4277633d0aa46cd7523
        public static final String URL_FLASHCARD = "flashcards/";
        public static final String URL_SCORE = "game/";
        public static final String URL_HIGHSCORE = "highscore";

        private APIUtils(){

        }

        public static GameAPIService getAPIService(){
                return APIClient.getInstance(BASE_URL).create(GameAPIService.class);
        }

}

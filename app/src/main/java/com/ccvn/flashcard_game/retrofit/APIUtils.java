package com.ccvn.flashcard_game.retrofit;

public class APIUtils {

        public static final String BASE_URL = "https://raw.githubusercontent.com/phanhung98/FakeApi/master/";
        public static final String URL_GAME_LIST = "listgame";
        public static final String URL_GAME_DETAIL = "gamedetail";
        public static final String URL_HIGHSCORE = "highscore";


        private APIUtils(){

        }

        public static GameAPIService getAPIService(){
                return APIClient.getInstance(BASE_URL).create(GameAPIService.class);
        }


}

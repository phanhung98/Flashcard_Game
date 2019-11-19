package com.example.flashcard_game.Retrofit;

public class ApiUtils {

        public static final String GAME_LIST="1b049dc214dd09b01d62b9a26e9171c9/raw/";

        private ApiUtils(){

        }
        public static final String BASE_URL="https://gist.githubusercontent.com/cc-vietvo/";
        public static getGameList getAPIService(){
                return RetrofitClient.getInstance(BASE_URL).create(getGameList.class);
        }
}

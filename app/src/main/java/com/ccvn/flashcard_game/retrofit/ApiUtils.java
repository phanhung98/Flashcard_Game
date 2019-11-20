package com.ccvn.flashcard_game.retrofit;

public class ApiUtils {

        public static final String URL_GAME_LIST="1b049dc214dd09b01d62b9a26e9171c9/raw/";
        public static final String URL_GAME_DETAIL="";
        public static final String FLASHCARD="";


        private ApiUtils(){

        }
        public static final String BASE_URL="https://gist.githubusercontent.com/cc-vietvo/";
        public static GameAPIService getAPIService(){
                return APIClient.getInstance(BASE_URL).create(GameAPIService.class);
        }
}

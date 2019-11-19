package com.example.flashcard_game.Retrofit;

public class ApiUtils {

        public static final String RAW="1b049dc214dd09b01d62b9a26e9171c9/raw/";

        private ApiUtils(){

        }
        public static final String BASE_URL="https://gist.githubusercontent.com/cc-vietvo/";
        public static IMyAPI getAPIService(){
                return RetrofitClient.getInstance(BASE_URL).create(IMyAPI.class);
        }
}

package com.example.flashcard_game.Retrofit;

public class ApiUtils {

        private ApiUtils(){

        }
        public static final String BASE_URL="https://gist.githubusercontent.com/cc-vietvo/1b049dc214dd09b01d62b9a26e9171c9/raw/";
        public static IMyAPI getAPIService(){
                return RetrofitClient.getInstance(BASE_URL).create(IMyAPI.class);
        }
}

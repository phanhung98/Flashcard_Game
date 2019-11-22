package com.ccvn.flashcard_game.retrofit;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetAnswerOption extends AsyncTask<String,Void,String>{


    Context mContext;
    String url= APIUtils.BASE_URL + APIUtils.URL_GAMEPLAY;


    public GetAnswerOption(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(strings[0]);

            InputStreamReader inputStreamReader= new InputStreamReader(url.openConnection().getInputStream());

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = "";

            while ((line = bufferedReader.readLine()) != null){
                    content.append(line);
            }
            bufferedReader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        List<String> list_answer_option = new ArrayList<>();

        try {
            JSONObject  jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray(APIUtils.URL_GAMEPLAY);
            for (int i = 0; i < jsonArray.length(); i++){

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}

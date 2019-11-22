package com.ccvn.flashcard_game.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ccvn.flashcard_game.R;
import com.ccvn.flashcard_game.models.Flashcard;
import com.ccvn.flashcard_game.retrofit.APIUtils;
import com.ccvn.flashcard_game.retrofit.GameAPIService;
import com.ccvn.flashcard_game.retrofit.GetAnswerOption;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GamePlayActivity extends AppCompatActivity{

    GameAPIService gameAPIService;
    int gameid;
    CompositeDisposable compositeDisposable= new CompositeDisposable();

    List<Flashcard> flashcardList;
    TextView tv_score, tv_question_count;
    ImageView image_detail;
    Button btn_submit;
    RadioGroup radio_group;
    RadioButton radio_button_one, radio_button_two, radio_button_three;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        gameAPIService = APIUtils.getAPIService();
        flashcardList = new ArrayList<>();

        initview();

        fetch_flashcard_data();
        GetAnswerOption getAnswerOption= new GetAnswerOption(this);
            getAnswerOption.execute(APIUtils.BASE_URL + APIUtils.URL_GAMEPLAY);



        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radio_button_one.isChecked()){
                    Toast.makeText(GamePlayActivity.this, radio_button_one.getText(), Toast.LENGTH_SHORT).show();
                }
                if (radio_button_two.isChecked()){
                    Toast.makeText(GamePlayActivity.this, radio_button_two.getText(), Toast.LENGTH_SHORT).show();
                }
                if (radio_button_three.isChecked()){
                    Toast.makeText(GamePlayActivity.this, radio_button_three.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initview() {
        tv_question_count = findViewById(R.id.tv_count_question);
        tv_score = findViewById(R.id.tv_score);
        image_detail = findViewById(R.id.image_detail);
        btn_submit = findViewById(R.id.btn_submit);
        radio_group = findViewById(R.id.radio_group);
        radio_button_one =findViewById(R.id.radio_button_one);
        radio_button_two =findViewById(R.id.radio_button_two);
        radio_button_three =findViewById(R.id.radio_button_three);
    }

    public void fetch_flashcard_data(){
            compositeDisposable.add(gameAPIService.get_flash_card()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<Flashcard>>() {
                @Override
                public void accept(List<Flashcard> flashcards) throws Exception {
                    flashcardList = flashcards;
                    show_flashcard(flashcards);
                }
            }));
    }

    public void show_flashcard(List<Flashcard> list){

                Glide.with(GamePlayActivity.this).load(list.get(0).getUpload_path()).into(image_detail);

    }

    public int get_gameid(){

        Intent intent = getIntent();
        gameid = intent.getIntExtra("GameID",0);

        return gameid;
    }

}

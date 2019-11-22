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
    private int mGameId;
    CompositeDisposable mCompositeDisposable= new CompositeDisposable();

    List<Flashcard> mFlashcardList;
    TextView mScore, mQuestionCount;
    ImageView mImageGameplay;
    Button mNextCard;
    RadioGroup mRadioGroup;
    RadioButton mAnswerOptionOne, mAnswerOptionTwo, mAnswerOptionThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        gameAPIService = APIUtils.getAPIService();
        mFlashcardList = new ArrayList<>();

        initview();

        fetch_flashcard_data();
        GetAnswerOption getAnswerOption= new GetAnswerOption(GamePlayActivity.this);
            getAnswerOption.execute(APIUtils.BASE_URL + APIUtils.URL_GAMEPLAY);



        mNextCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerOptionOne.isChecked()){
                    Toast.makeText(GamePlayActivity.this, mAnswerOptionOne.getText(), Toast.LENGTH_SHORT).show();
                }
                if (mAnswerOptionTwo.isChecked()){
                    Toast.makeText(GamePlayActivity.this, mAnswerOptionTwo.getText(), Toast.LENGTH_SHORT).show();
                }
                if (mAnswerOptionThree.isChecked()){
                    Toast.makeText(GamePlayActivity.this, mAnswerOptionThree.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initview() {
        mQuestionCount = findViewById(R.id.tv_count_question);
        mScore = findViewById(R.id.tv_score);
        mImageGameplay = findViewById(R.id.image_detail);
        mNextCard = findViewById(R.id.btn_submit);
        mRadioGroup = findViewById(R.id.radio_group);
        mAnswerOptionOne =findViewById(R.id.radio_button_one);
        mAnswerOptionTwo =findViewById(R.id.radio_button_two);
        mAnswerOptionThree =findViewById(R.id.radio_button_three);
    }

    public void fetch_flashcard_data(){
            mCompositeDisposable.add(gameAPIService.get_flash_card()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<Flashcard>>() {
                @Override
                public void accept(List<Flashcard> flashcards) throws Exception {
                    mFlashcardList = flashcards;
                    show_flashcard(flashcards);
                }
            }));
    }

    public void show_flashcard(List<Flashcard> list){

                Glide.with(GamePlayActivity.this).load(list.get(0).getUploadPath()).into(mImageGameplay);

    }

    public int get_gameid(){

        Intent intent = getIntent();
        mGameId = intent.getIntExtra("GameID",0);

        return mGameId;
    }

}

package com.ccvn.flashcard_game.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
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
import com.ccvn.flashcard_game.models.Score;
import com.ccvn.flashcard_game.retrofit.APIUtils;
import com.ccvn.flashcard_game.retrofit.GameAPIService;
import com.ccvn.flashcard_game.viewmodels.GamePlayViewModel;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GamePlayActivity extends AppCompatActivity{

    GameAPIService gameAPIService;
    private int mGameId;
    CompositeDisposable mCompositeDisposable= new CompositeDisposable();
    GamePlayViewModel mGamePlayViewModel;

    List<Flashcard> mFlashcardList;
    TextView mScore, mQuestionCount;
    ImageView mImageGameplay;
    Button mNextCard;
    RadioGroup mRadioGroup;
    RadioButton mAnswerOptionOne, mAnswerOptionTwo, mAnswerOptionThree;
    private int position = 0;
    private int count = 1;
    private double score = 0.0;
    private boolean isRight = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        mFlashcardList = new ArrayList<>();
        gameAPIService = APIUtils.getAPIService();

        mGamePlayViewModel = ViewModelProviders.of(this).get(GamePlayViewModel.class);
        mGamePlayViewModel.getFlashcard();

        initview();

        mNextCard.setVisibility(View.INVISIBLE);
        showFlashcard();



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



    public void showFlashcard(){



                mGamePlayViewModel.getAllFlashcard().observe(GamePlayActivity.this, new Observer<List<Flashcard>>() {
                    @Override
                    public void onChanged(final List<Flashcard> flashcardList) {

                        mScore.setText(getString(R.string.score) + score);
                        mQuestionCount.setText(count + "/" + flashcardList.size());

                        mAnswerOptionOne.setText(flashcardList.get(position).getAnswerOption().get(0));
                        mAnswerOptionTwo.setText(flashcardList.get(position).getAnswerOption().get(1));
                        mAnswerOptionThree.setText(flashcardList.get(position).getAnswerOption().get(2));

                        Glide.with(GamePlayActivity.this).load(flashcardList.get(position).getUploadPath()).into(mImageGameplay);

                        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                switch (checkedId){
                                    case R.id.radio_button_one:
                                        String value = (String) mAnswerOptionOne.getText();
                                        getRightAnswer(position);
                                        if (value.equals(flashcardList.get(position).getRightAnswer())){
                                            score = score + 1;
                                            mScore.setText(getString(R.string.score) + score);
                                        }

                                        mNextCard.setVisibility(View.VISIBLE);
                                        break;
                                    case R.id.radio_button_two:
                                        String option = (String) mAnswerOptionTwo.getText();
                                        getRightAnswer(position);
                                        if (option.equals(flashcardList.get(position).getRightAnswer())){
                                            score = score + 1;
                                            mScore.setText(getString(R.string.score) + score);
                                        }
                                        mNextCard.setVisibility(View.VISIBLE);
                                        break;
                                    case R.id.radio_button_three:
                                        String val = (String) mAnswerOptionThree.getText();
                                        getRightAnswer(position);
                                        if (val.equals(flashcardList.get(position).getRightAnswer())){
                                            score = score + 1;
                                            mScore.setText(getString(R.string.score) + score);
                                            mNextCard.setVisibility(View.VISIBLE);
                                        }
                                        break;
                                }
                            }
                        });


                        mFlashcardList = flashcardList;
                    }
                });

    }


    public void nextCard(View view) {
        position++;
        mNextCard.setVisibility(View.INVISIBLE);

        if (position <= mFlashcardList.size()-1){
            count++;

            setAnswerOptionDefault();
            showFlashcard();

            if (position == mFlashcardList.size()-1){
                mNextCard.setText("Finish");
            }
        }else {
            insertScore();

        }
    }

    private void insertScore() {

    }

    public void getRightAnswer(int i){

        if (mAnswerOptionOne.getText().equals(mFlashcardList.get(i).getRightAnswer())){
            mAnswerOptionOne.setTextColor(Color.GREEN);
            mAnswerOptionOne.setClickable(false);
            mAnswerOptionTwo.setTextColor(Color.RED);
            mAnswerOptionTwo.setClickable(false);
            mAnswerOptionThree.setTextColor(Color.RED);
            mAnswerOptionThree.setClickable(false);


        }else if (mAnswerOptionTwo.getText().equals(mFlashcardList.get(i).getRightAnswer())){
            mAnswerOptionOne.setTextColor(Color.RED);
            mAnswerOptionOne.setClickable(false);
            mAnswerOptionTwo.setTextColor(Color.GREEN);
            mAnswerOptionTwo.setClickable(false);
            mAnswerOptionThree.setTextColor(Color.RED);
            mAnswerOptionThree.setClickable(false);

        }else if (mAnswerOptionThree.getText().equals(mFlashcardList.get(i).getRightAnswer())){
            mAnswerOptionOne.setTextColor(Color.RED);
            mAnswerOptionOne.setClickable(false);
            mAnswerOptionTwo.setTextColor(Color.RED);
            mAnswerOptionTwo.setClickable(false);
            mAnswerOptionThree.setTextColor(Color.GREEN);
            mAnswerOptionThree.setClickable(false);

        }

    }
    private void setAnswerOptionDefault(){
        mAnswerOptionOne.setTextColor(Color.BLACK);
        mAnswerOptionOne.setClickable(true);

        mAnswerOptionTwo.setTextColor(Color.BLACK);
        mAnswerOptionTwo.setClickable(true);

        mAnswerOptionThree.setTextColor(Color.BLACK);
        mAnswerOptionThree.setClickable(true);

        mRadioGroup.clearCheck();
    }

}

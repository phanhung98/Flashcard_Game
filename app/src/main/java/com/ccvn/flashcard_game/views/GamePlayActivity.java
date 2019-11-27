package com.ccvn.flashcard_game.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
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

    private static final int WORD_TYPE = 1;
    private static final int IMAGE_TYPE = 2;
    private static final int IMAGE_AND_WORD_TYPE = 3;

    GameAPIService gameAPIService;
    private int mGameId;
    GamePlayViewModel mGamePlayViewModel;

    List<Flashcard> mFlashcardList;
    TextView mScore, mQuestionCount;
    TextView mImageViewText;
    TextView mQuestion;
    ImageView mImageGameplay;
    Button mNextCard;
    RadioGroup mRadioGroup;
    RadioButton mAnswerOptionOne, mAnswerOptionTwo, mAnswerOptionThree;
    private int position = 0;
    private int count = 1;
    private double score = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        mFlashcardList = new ArrayList<>();
        gameAPIService = APIUtils.getAPIService();

        mGamePlayViewModel = ViewModelProviders.of(this).get(GamePlayViewModel.class);
        mGamePlayViewModel.getFlashcard();

        initview();
        mImageViewText.setVisibility(View.INVISIBLE);
        mImageGameplay.setVisibility(View.INVISIBLE);
        mQuestion.setVisibility(View.INVISIBLE);

        mNextCard.setVisibility(View.INVISIBLE);
        showFlashcard();

    }

    private void initview() {
        mQuestionCount = findViewById(R.id.tv_count_question);
        mScore = findViewById(R.id.tv_score);
        mImageGameplay = findViewById(R.id.image_detail);
        mNextCard = findViewById(R.id.btn_submit);
        mRadioGroup = findViewById(R.id.radio_group);
        mAnswerOptionOne = findViewById(R.id.radio_button_one);
        mAnswerOptionTwo = findViewById(R.id.radio_button_two);
        mAnswerOptionThree = findViewById(R.id.radio_button_three);
        mImageViewText = findViewById(R.id.ImageViewText);
        mQuestion = findViewById(R.id.tv_question);
    }



    public void showFlashcard(){



                mGamePlayViewModel.getAllFlashcard().observe(GamePlayActivity.this, new Observer<List<Flashcard>>() {
                    @Override
                    public void onChanged(final List<Flashcard> flashcardList) {

                        mScore.setText(getString(R.string.score) +score);
                        mQuestionCount.setText(count + "/" + flashcardList.size());

                        mAnswerOptionOne.setText(flashcardList.get(position).getAnswerOption().get(0));
                        mAnswerOptionTwo.setText(flashcardList.get(position).getAnswerOption().get(1));
                        mAnswerOptionThree.setText(flashcardList.get(position).getAnswerOption().get(2));

                        if (flashcardList.get(position).getTypeId() == IMAGE_TYPE) {

                            mImageViewText.setVisibility(View.INVISIBLE);
                            mQuestion.setVisibility(View.INVISIBLE);
                            mImageGameplay.setVisibility(View.VISIBLE);

                            Glide.with(GamePlayActivity.this).load(flashcardList.get(position).getUploadPath()).into(mImageGameplay);

                        }

                        if (flashcardList.get(position).getTypeId() == WORD_TYPE){

                            mImageGameplay.setVisibility(View.INVISIBLE);
                            mQuestion.setVisibility(View.INVISIBLE);
                            mImageViewText.setVisibility(View.VISIBLE);

                            mImageViewText.setText(flashcardList.get(position).getWord());

                        }

                        if (flashcardList.get(position).getTypeId() == 3){

                            mImageGameplay.setVisibility(View.VISIBLE);
                            mQuestion.setVisibility(View.VISIBLE);
                            mImageViewText.setVisibility(View.INVISIBLE);

                            mQuestion.setText(flashcardList.get(position).getWord()+"?");
                            Glide.with(GamePlayActivity.this).load(flashcardList.get(position).getUploadPath()).into(mImageGameplay);

                        }


                        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                switch (checkedId){
                                    case R.id.radio_button_one:

                                      getRightAnswer(position);

                                        break;
                                    case R.id.radio_button_two:

                                       getRightAnswer(position);

                                        break;

                                    case R.id.radio_button_three:

                                       getRightAnswer(position);

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
            scoreDialog();

        }
    }

    private void insertScore() {

    }

   private void scoreDialog(){

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.score_dialog, null);
        final TextView mScore = view.findViewById(R.id.dialog_score);
        Button mConfirm = view.findViewById(R.id.btn_dialog);

        Context context;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Congratulation!");
        alertDialog.setView(view);
        alertDialog.setCancelable(false);

       mScore.setText("Your score: " + score);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(GamePlayActivity.this, GameActivity.class);
                startActivity(home);
            }
        });

        AlertDialog dialog = alertDialog.create();
        dialog.show();
   }

    public void getRightAnswer(int i){


        if (mAnswerOptionOne.isChecked()){

            String value = (String) mAnswerOptionOne.getText();


            if (mAnswerOptionOne.getText().equals(mFlashcardList.get(position).getRightAnswer())){
                mAnswerOptionOne.setTextColor(Color.GREEN);
                mAnswerOptionOne.setClickable(false);
                mAnswerOptionTwo.setTextColor(Color.GRAY);
                mAnswerOptionTwo.setClickable(false);
                mAnswerOptionThree.setTextColor(Color.GRAY);
                mAnswerOptionThree.setClickable(false);


            }else {
                mAnswerOptionOne.setTextColor(Color.RED);
                mAnswerOptionOne.setClickable(false);
                mAnswerOptionTwo.setTextColor(Color.GRAY);
                mAnswerOptionTwo.setClickable(false);
                mAnswerOptionThree.setTextColor(Color.GRAY);
                mAnswerOptionThree.setClickable(false);
            }

            if (value.equals(mFlashcardList.get(position).getRightAnswer())){
                score = score + 1;
                mScore.setText(getString(R.string.score) + score);
            }

            mNextCard.setVisibility(View.VISIBLE);

        }
        if (mAnswerOptionTwo.isChecked()){

            String option = (String) mAnswerOptionTwo.getText();



            if (mAnswerOptionTwo.getText().equals(mFlashcardList.get(position).getRightAnswer())){
                mAnswerOptionOne.setTextColor(Color.GRAY);
                mAnswerOptionOne.setClickable(false);
                mAnswerOptionTwo.setTextColor(Color.GREEN);
                mAnswerOptionTwo.setClickable(false);
                mAnswerOptionThree.setTextColor(Color.GRAY);
                mAnswerOptionThree.setClickable(false);

            }else {
                mAnswerOptionOne.setTextColor(Color.GRAY);
                mAnswerOptionOne.setClickable(false);
                mAnswerOptionTwo.setTextColor(Color.RED);
                mAnswerOptionTwo.setClickable(false);
                mAnswerOptionThree.setTextColor(Color.GRAY);
                mAnswerOptionThree.setClickable(false);
            }

            if (option.equals(mFlashcardList.get(position).getRightAnswer())){
                score = score + 1;
                mScore.setText(getString(R.string.score) + score);
            }
            mNextCard.setVisibility(View.VISIBLE);

        }
        if (mAnswerOptionThree.isChecked()){

            String val = (String) mAnswerOptionThree.getText();



            if (mAnswerOptionThree.getText().equals(mFlashcardList.get(position).getRightAnswer())){
                mAnswerOptionOne.setTextColor(Color.GRAY);
                mAnswerOptionOne.setClickable(false);
                mAnswerOptionTwo.setTextColor(Color.GRAY);
                mAnswerOptionTwo.setClickable(false);
                mAnswerOptionThree.setTextColor(Color.GREEN);
                mAnswerOptionThree.setClickable(false);

            }else {

                mAnswerOptionOne.setTextColor(Color.GRAY);
                mAnswerOptionOne.setClickable(false);
                mAnswerOptionTwo.setTextColor(Color.GRAY);
                mAnswerOptionTwo.setClickable(false);
                mAnswerOptionThree.setTextColor(Color.RED);
                mAnswerOptionThree.setClickable(false);

            }

            if (val.equals(mFlashcardList.get(position).getRightAnswer())){
                score = score + 1;
                mScore.setText(getString(R.string.score) + score);
            }
            mNextCard.setVisibility(View.VISIBLE);

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

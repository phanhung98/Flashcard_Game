package com.ccvn.flashcard_game.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import android.widget.EditText;
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

    private static final int RADIO_BOX = 1;
    private static final int INPUT_TEXT = 2;


    public List<Integer> mFlashcardId;
    GameAPIService gameAPIService;
    private int mGameId;
    GamePlayViewModel mGamePlayViewModel;

    TextView mScore, mQuestionCount;
    TextView mImageViewText;
    TextView mQuestion;
    ImageView mImageGameplay;
    RadioGroup mRadioGroup;
    RadioButton mAnswerOptionOne, mAnswerOptionTwo, mAnswerOptionThree;
    EditText mInputAnswer;
    Button mNextCard, mSubmit;

    private Flashcard mFlashcard;

    private int position =0;
    private int count = 1;
    private double score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        gameAPIService = APIUtils.getAPIService();

        mGamePlayViewModel = ViewModelProviders.of(this).get(GamePlayViewModel.class);

        getId();

        getUrlForNextFlashcard(position);

        initview();

        mImageViewText.setVisibility(View.INVISIBLE);
        mImageGameplay.setVisibility(View.INVISIBLE);
        mQuestion.setVisibility(View.INVISIBLE);
        mRadioGroup.setVisibility(View.INVISIBLE);
        mInputAnswer.setVisibility(View.INVISIBLE);
        mSubmit.setVisibility(View.INVISIBLE);

        mNextCard.setVisibility(View.INVISIBLE);
        showFlashcard();

    }

    private void initview() {
        mQuestionCount = findViewById(R.id.tv_count_question);
        mScore = findViewById(R.id.tv_score);
        mImageGameplay = findViewById(R.id.image_detail);
        mRadioGroup = findViewById(R.id.radio_group);
        mAnswerOptionOne = findViewById(R.id.radio_button_one);
        mAnswerOptionTwo = findViewById(R.id.radio_button_two);
        mAnswerOptionThree = findViewById(R.id.radio_button_three);
        mImageViewText = findViewById(R.id.ImageViewText);
        mQuestion = findViewById(R.id.tv_question);
        mNextCard = findViewById(R.id.btn_next_card);
        mInputAnswer = findViewById(R.id.inputAnswer);
        mSubmit = findViewById(R.id.btn_Submit);

    }

    //Get game Id and flashcard Id
    private void getId(){

        Intent intent = getIntent();
        mFlashcardId =  intent.getIntegerArrayListExtra(GameDetailActivity.FLASHCARD_ID);
        mGameId = intent.getIntExtra(GameDetailActivity.GAMEID, 0);

    }

    //Set url for next flashcard
    private void getUrlForNextFlashcard(int pos){

        String flashcradId = String.valueOf(mFlashcardId.get(pos));
        String url = APIUtils.URL_FLASHCARD + flashcradId;
        mGamePlayViewModel.getNextFlashcard(url);

    }

    //Show flashcard
    public void showFlashcard(){

                mGamePlayViewModel.getAllFlashcard().observe(GamePlayActivity.this, new Observer<Flashcard>() {
                    @Override
                    public void onChanged(final Flashcard flashcard) {

                        mScore.setText(getString(R.string.score) +score);
                        mQuestionCount.setText(count + "/" + mFlashcardId.size());


                        if (flashcard.getWord() == null) {

                            mImageViewText.setVisibility(View.INVISIBLE);
                            mQuestion.setVisibility(View.INVISIBLE);
                            mImageGameplay.setVisibility(View.VISIBLE);

                            Glide.with(GamePlayActivity.this).load(flashcard.getUpload_path()).into(mImageGameplay);

                        }

                        if (flashcard.getUpload_path() == null){

                            mImageGameplay.setVisibility(View.INVISIBLE);
                            mQuestion.setVisibility(View.INVISIBLE);
                            mImageViewText.setVisibility(View.VISIBLE);

                            mImageViewText.setText(flashcard.getWord());

                        }

                        if (flashcard.getWord() != null && flashcard.getWord() != null){

                            mImageGameplay.setVisibility(View.VISIBLE);
                            mQuestion.setVisibility(View.VISIBLE);
                            mImageViewText.setVisibility(View.INVISIBLE);

                            mQuestion.setText(flashcard.getWord()+"?");
                            Glide.with(GamePlayActivity.this).load(flashcard.getUpload_path()).into(mImageGameplay);

                        }

                        if (flashcard.getType_id() == RADIO_BOX){

                            mAnswerOptionOne.setText(flashcard.getValue().get(0));
                            mAnswerOptionTwo.setText(flashcard.getValue().get(1));
                            mAnswerOptionThree.setText(flashcard.getValue().get(2));

                            mRadioGroup.setVisibility(View.VISIBLE);
                            mInputAnswer.setVisibility(View.INVISIBLE);
                            mSubmit.setVisibility(View.INVISIBLE);

                            mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    switch (checkedId){
                                        case R.id.radio_button_one:
                                        case R.id.radio_button_two:
                                        case R.id.radio_button_three:

                                            getRightAnswer();
                                            break;
                                    }
                                }
                            });

                        }
                        if (flashcard.getType_id() == INPUT_TEXT){

                            mRadioGroup.setVisibility(View.INVISIBLE);
                            mSubmit.setVisibility(View.VISIBLE);
                            mInputAnswer.setVisibility(View.VISIBLE);

                            mSubmit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mInputAnswer.getText().length() == 0){

                                        mInputAnswer.setError("Please input your answer");
                                    }else {
                                        getRightAnswerInputText(flashcard);
                                    }
                                }
                            });
                        }
                            mFlashcard = flashcard;
                    }
                });
    }

    //click next flashcard button
    public void nextCard(View view) {

        position++;
        mNextCard.setVisibility(View.INVISIBLE);

        if (position <= mFlashcardId.size()-1){
            count++;

            getUrlForNextFlashcard(position);

            setAnswerOptionDefault();
            setInputAnserDefault();
            showFlashcard();

            if (position == mFlashcardId.size()-1){
                mNextCard.setText("Finish");
            }
        }else {
            insertScore();
            scoreDialog();

        }
    }

    private void insertScore() {

    }

    //Show score
   private void scoreDialog(){

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.score_dialog, null);
        final TextView mScore = view.findViewById(R.id.dialog_score);
        Button mConfirm = view.findViewById(R.id.btn_dialog);

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

   // get right answer
    public void getRightAnswer(){

        if (mAnswerOptionOne.isChecked()){

            String value = (String) mAnswerOptionOne.getText();

            if (mAnswerOptionOne.getText().equals(mFlashcard.getRight_answer())){
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

            if (value.equals(mFlashcard.getRight_answer())){
                score = score + 1;
                mScore.setText(getString(R.string.score) + score);
            }

            mNextCard.setVisibility(View.VISIBLE);

        }
        if (mAnswerOptionTwo.isChecked()){

            String option = (String) mAnswerOptionTwo.getText();

            if (mAnswerOptionTwo.getText().equals(mFlashcard.getRight_answer())){
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

            if (option.equals(mFlashcard.getRight_answer())){
                score = score + 1;
                mScore.setText(getString(R.string.score) + score);
            }
            mNextCard.setVisibility(View.VISIBLE);

        }
        if (mAnswerOptionThree.isChecked()){

            String val = (String) mAnswerOptionThree.getText();

            if (mAnswerOptionThree.getText().equals(mFlashcard.getRight_answer())){
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

            if (val.equals(mFlashcard.getRight_answer())){
                score = score + 1;
                mScore.setText(getString(R.string.score) + score);
            }
            mNextCard.setVisibility(View.VISIBLE);

        }

    }

    // Set radio button to default
    private void setAnswerOptionDefault(){

        mAnswerOptionOne.setTextColor(Color.BLACK);
        mAnswerOptionOne.setClickable(true);

        mAnswerOptionTwo.setTextColor(Color.BLACK);
        mAnswerOptionTwo.setClickable(true);

        mAnswerOptionThree.setTextColor(Color.BLACK);
        mAnswerOptionThree.setClickable(true);

        mRadioGroup.clearCheck();

    }
    private void setInputAnserDefault(){

        mInputAnswer.setEnabled(true);
        mInputAnswer.setText("");
        mInputAnswer.setTextColor(ContextCompat.getColor(this, R.color.defaultColorText));
        mSubmit.setEnabled(true);

    }

    public void getRightAnswerInputText(Flashcard flashcard){

        String answer = mInputAnswer.getText().toString();

        if (flashcard.getRight_answer().equals(answer)){

            mInputAnswer.setTextColor(ContextCompat.getColor(this, R.color.rightColorText));
            mInputAnswer.setEnabled(false);
            mSubmit.setEnabled(false);
            mNextCard.setVisibility(View.VISIBLE);

            score = score + 1;
            mScore.setText(getString(R.string.score) + score);

        }else {

            mInputAnswer.setTextColor(ContextCompat.getColor(this, R.color.wrongColorText));
            mInputAnswer.setEnabled(false);
            mSubmit.setEnabled(false);
            mNextCard.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onBackPressed() {

    }
}

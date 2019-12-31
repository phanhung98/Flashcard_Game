package com.ccvn.flashcard_game.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ccvn.flashcard_game.Common.Common;
import com.ccvn.flashcard_game.Common.NetworkChangeReceiver;
import com.ccvn.flashcard_game.R;
import com.ccvn.flashcard_game.databinding.ActivityGamePlayBinding;
import com.ccvn.flashcard_game.models.Flashcard;

import com.ccvn.flashcard_game.retrofit.APIUtils;
import com.ccvn.flashcard_game.retrofit.GameAPIService;
import com.ccvn.flashcard_game.viewmodels.GamePlayViewModel;
import java.text.DecimalFormat;

import java.util.List;

import static com.ccvn.flashcard_game.views.ListGameFragment.AGE;
import static com.ccvn.flashcard_game.views.ListGameFragment.NAME;
import static com.ccvn.flashcard_game.views.ListGameFragment.SEX;
import static com.ccvn.flashcard_game.views.ListGameFragment.USERINFO;


public class GamePlayActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private static final int RADIO_BOX = 1;
    private static final int INPUT_TEXT = 2;
    public static final int SWIPE_THRESHOLD = 100;
    public static final int SWIPE_VELOCITY_THRESHOLD = 100;

    public List<Integer> mFlashcardId;
    GameAPIService gameAPIService;
    private int mGameId;
    GamePlayViewModel mGamePlayViewModel;
    DecimalFormat f;
    boolean isRight;
    GestureDetectorCompat detector;

    private Flashcard mFlashcard;
    private int position =0;
    private int count = 1;
    private double score = 0;
    private long time;
    private int totalTime = 0;

    private ActivityGamePlayBinding mActivityGamePlayBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       mActivityGamePlayBinding = DataBindingUtil.setContentView(this, R.layout.activity_game_play);

        f = new DecimalFormat("##.##");
        gameAPIService = APIUtils.getAPIService();

        getId();

        getUrlForNextFlashcard(position);

        setViewVisible();

        if (NetworkChangeReceiver.isOnline(getBaseContext())) {
            showFlashcard();
        }else {
            Toast.makeText(this, R.string.check_connection, Toast.LENGTH_SHORT).show();
        }
        detector = new GestureDetectorCompat(this, this);
    }

    private void setViewVisible() {

        mActivityGamePlayBinding.ImageViewText.setVisibility(View.INVISIBLE);
        mActivityGamePlayBinding.imageGameplay.setVisibility(View.INVISIBLE);
        mActivityGamePlayBinding.tvQuestion.setVisibility(View.INVISIBLE);
        mActivityGamePlayBinding.radioGroup.setVisibility(View.INVISIBLE);
        mActivityGamePlayBinding.group.setVisibility(View.INVISIBLE);
        mActivityGamePlayBinding.btnNextCard.setVisibility(View.INVISIBLE);
        mActivityGamePlayBinding.btnFinish.setVisibility(View.INVISIBLE);

    }

    //Get game Id and flashcard Id
    private void getId(){

        Intent intent = getIntent();
        mFlashcardId =  intent.getIntegerArrayListExtra(GameDetailActivity.FLASHCARD_ID);
        mGameId = intent.getIntExtra(GameDetailActivity.GAMEID, 0);

    }

    //Set url for next flashcard
    private void getUrlForNextFlashcard(int pos){

        mGamePlayViewModel = ViewModelProviders.of(this).get(GamePlayViewModel.class);
        String flashcradId = String.valueOf(mFlashcardId.get(pos));
        String url = APIUtils.URL_FLASHCARD + flashcradId;
        mGamePlayViewModel.getNextFlashcard(url);

    }

    //Show flashcard
    public void showFlashcard(){

            mGamePlayViewModel.show(this);

                mGamePlayViewModel.getAllFlashcard().observe(GamePlayActivity.this, new Observer<Flashcard>() {
                    @Override
                    public void onChanged(final Flashcard flashcard) {

                        mActivityGamePlayBinding.tvScore.setText(getString(R.string.score) + f.format(score));
                        mActivityGamePlayBinding.tvCountQuestion.setText(count + "/" + mFlashcardId.size());

                        if (flashcard.getWord() == null) {

                            mActivityGamePlayBinding.ImageViewText.setVisibility(View.INVISIBLE);
                            mActivityGamePlayBinding.tvQuestion.setVisibility(View.INVISIBLE);
                            mActivityGamePlayBinding.imageGameplay.setVisibility(View.VISIBLE);

                            Glide.with(GamePlayActivity.this).load(flashcard.getUpload_path()).into(mActivityGamePlayBinding.imageGameplay);
                        }

                        if (flashcard.getUpload_path() == null) {

                            mActivityGamePlayBinding.imageGameplay.setVisibility(View.INVISIBLE);
                            mActivityGamePlayBinding.tvQuestion.setVisibility(View.INVISIBLE);
                            mActivityGamePlayBinding.ImageViewText.setVisibility(View.VISIBLE);

                            mActivityGamePlayBinding.ImageViewText.setText(flashcard.getWord());
                        }

                        if (flashcard.getWord() != null && flashcard.getWord() != null) {

                            mActivityGamePlayBinding.imageGameplay.setVisibility(View.VISIBLE);
                            mActivityGamePlayBinding.tvQuestion.setVisibility(View.INVISIBLE);
                            mActivityGamePlayBinding.ImageViewText.setVisibility(View.INVISIBLE);

                            mActivityGamePlayBinding.tvQuestion.setText(flashcard.getWord() + "?");
                            Glide.with(GamePlayActivity.this).load(flashcard.getUpload_path()).into(mActivityGamePlayBinding.imageGameplay);
                        }

                        if (flashcard.getType_id() == RADIO_BOX) {

                            for (int i = 0; i < 3; i++){
                                ((RadioButton)mActivityGamePlayBinding.radioGroup.getChildAt(i)).setText(flashcard.getValue().get(i));
                            }

                            mActivityGamePlayBinding.radioGroup.setVisibility(View.VISIBLE);
                            mActivityGamePlayBinding.group.setVisibility(View.INVISIBLE);

                            mActivityGamePlayBinding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    switch (checkedId) {
                                        case R.id.radio_button_one:
                                        case R.id.radio_button_two:
                                        case R.id.radio_button_three:

                                            getRightAnswer();
                                            break;
                                    }
                                }
                            });
                        }
                        if (flashcard.getType_id() == INPUT_TEXT) {

                            mActivityGamePlayBinding.radioGroup.setVisibility(View.INVISIBLE);
                            mActivityGamePlayBinding.group.setVisibility(View.VISIBLE);

                            mActivityGamePlayBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mActivityGamePlayBinding.inputAnswer.getText().length() == 0) {

                                        mActivityGamePlayBinding.inputAnswer.setError("Please input your answer");
                                    } else {
                                        getRightAnswerInputText(flashcard);
                                    }
                                }
                            });
                        }
                        mFlashcard = flashcard;
                        if (position == 0) {
                            mActivityGamePlayBinding.time.setBase(SystemClock.elapsedRealtime());
                            mActivityGamePlayBinding.time.start();
                        }
                    }
                });
                mGamePlayViewModel.getDialog().dismiss();
    }

    public void getRightAnswer(){

        if (mActivityGamePlayBinding.radioButtonOne.isChecked()){
            setAnswer();
        }
        if (mActivityGamePlayBinding.radioButtonTwo.isChecked()){
            setAnswer();
        }
        if (mActivityGamePlayBinding.radioButtonThree.isChecked()){
            setAnswer();
        }
    }
    // Set right and wrong answer
    @SuppressLint("NewApi")
    public void setAnswer(){

        for (int i = 0; i < 3; i++){

            isRight =  ((RadioButton)mActivityGamePlayBinding.radioGroup.getChildAt(i)).getText().toString().equals(mFlashcard.getRight_answer());

            if (isRight && mActivityGamePlayBinding.radioGroup.getChildAt(i).getId() == mActivityGamePlayBinding.radioGroup.getCheckedRadioButtonId()){

                mActivityGamePlayBinding.radioGroup.getChildAt(i).setBackground(getDrawable(R.drawable.right_answer));
                mActivityGamePlayBinding.radioGroup.getChildAt(i).setClickable(false);
                ((RadioButton)mActivityGamePlayBinding.radioGroup.getChildAt(i)).setTextColor(Color.WHITE);

                setScore();
                getTotalTime();
            }
            else if (!isRight && mActivityGamePlayBinding.radioGroup.getChildAt(i).getId() == mActivityGamePlayBinding.radioGroup.getCheckedRadioButtonId()){
                mActivityGamePlayBinding.radioGroup.getChildAt(i).setBackground(getDrawable(R.drawable.wrong_answer));
                mActivityGamePlayBinding.radioGroup.getChildAt(i).setClickable(false);
                ((RadioButton)mActivityGamePlayBinding.radioGroup.getChildAt(i)).setTextColor(Color.WHITE);

                getTotalTime();
            }
            else {
                ((RadioButton)mActivityGamePlayBinding.radioGroup.getChildAt(i)).setTextColor(Color.GRAY);
                mActivityGamePlayBinding.radioGroup.getChildAt(i).setClickable(false);
            }
        }
        mActivityGamePlayBinding.btnNextCard.setVisibility(View.VISIBLE);
    }

    //click next flashcard button
    public void nextCard(View view) {
        if (NetworkChangeReceiver.isOnline(getBaseContext())) {

            position++;
            mActivityGamePlayBinding.btnNextCard.setVisibility(View.INVISIBLE);

            if (position <= mFlashcardId.size() - 1) {
                count++;

                getUrlForNextFlashcard(position);

                setAnswerOptionDefault();
                setInputAnserDefault();
                showFlashcard();
                mActivityGamePlayBinding.time.setBase(SystemClock.elapsedRealtime());
                mActivityGamePlayBinding.time.start();

                if (position == mFlashcardId.size() - 1) {
                    mActivityGamePlayBinding.btnFinish.setVisibility(View.VISIBLE);
                    mActivityGamePlayBinding.btnFinish.setText("Finish");
                }
            } else {
                insertScore();
            }
        }else {
            Toast.makeText(this, R.string.check_connection, Toast.LENGTH_SHORT).show();
        }
    }

    // Set radio button to default
    @SuppressLint("NewApi")
    private void setAnswerOptionDefault(){
        for (int i = 0; i < 3; i++){

            mActivityGamePlayBinding.radioGroup.getChildAt(i).setBackground(getDrawable(R.drawable.radio_flat_selector));
            mActivityGamePlayBinding.radioGroup.getChildAt(i).setClickable(true);
            ((RadioButton)mActivityGamePlayBinding.radioGroup.getChildAt(i)).setTextColor(getResources().getColor(R.color.color_on_background));

        }
        mActivityGamePlayBinding.radioGroup.clearCheck();

    }

    // Set input text default
    private void setInputAnserDefault(){

        mActivityGamePlayBinding.inputAnswer.setEnabled(true);
        mActivityGamePlayBinding.inputAnswer.setText("");
        mActivityGamePlayBinding.inputAnswer.setTextColor(ContextCompat.getColor(this, R.color.color_on_surface));
        mActivityGamePlayBinding.btnSubmit.setEnabled(true);

    }

    //Insert score to database
    private void insertScore() {

        if (Common.currentUser == null){
            SharedPreferences preferences = getSharedPreferences(USERINFO, Context.MODE_PRIVATE);
            String name = preferences.getString(NAME, "");
            int age = preferences.getInt(AGE, 0);
            String sex = preferences.getString(SEX, "");
            int gameId = Common.currentGame.getId();

            mGamePlayViewModel.storeScore(gameId, score, totalTime, name, age, sex);
            storeScore();
        }
    }

    public void storeScore(){

        mGamePlayViewModel.getmSuccess().observe(GamePlayActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                    if (s.equals("success")){
                        scoreDialog();
                    }else {
                        Toast.makeText(GamePlayActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
            }
        });
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

       mScore.setText("Your score: " + f.format(score));
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

    // set score for each flashcard
    private void setScore() {

        mActivityGamePlayBinding.time.stop();
        time = (SystemClock.elapsedRealtime() - mActivityGamePlayBinding.time.getBase())/1000;
        score = score + Math.round((0.01 + Math.pow(0.99, time)) * 100.0)/100.0;
        mActivityGamePlayBinding.tvScore.setText(getString(R.string.score) + f.format(score));
    }

    // get total time play game
    private void getTotalTime(){
        mActivityGamePlayBinding.time.stop();
        time = ((SystemClock.elapsedRealtime() - mActivityGamePlayBinding.time.getBase())/1000);
        totalTime = (int) (totalTime + time);
    }

    public void getRightAnswerInputText(Flashcard flashcard){

        String answer = mActivityGamePlayBinding.inputAnswer.getText().toString();

        if (flashcard.getRight_answer().equals(answer)){

            mActivityGamePlayBinding.inputAnswer.setTextColor(ContextCompat.getColor(this, R.color.rightColorText));
            mActivityGamePlayBinding.inputAnswer.setEnabled(false);
            mActivityGamePlayBinding.btnSubmit.setEnabled(false);
            mActivityGamePlayBinding.btnNextCard.setVisibility(View.VISIBLE);

           setScore();
           getTotalTime();

        }else {

            mActivityGamePlayBinding.inputAnswer.setTextColor(ContextCompat.getColor(this, R.color.color_error));
            mActivityGamePlayBinding.inputAnswer.setEnabled(false);
            mActivityGamePlayBinding.btnSubmit.setEnabled(false);
            mActivityGamePlayBinding.btnNextCard.setVisibility(View.VISIBLE);

            getTotalTime();
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    // Move to next flashcard when user swipe
    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {

        float diffY = moveEvent.getY() - downEvent.getY();
        float diffX =  moveEvent.getX() - downEvent.getX();

        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX < 0) {
                    onSwipeLeft();
                }
            }
        }

        return true;
    }

    private void onSwipeLeft() {

        if (mActivityGamePlayBinding.radioButtonOne.isChecked() || mActivityGamePlayBinding.radioButtonTwo.isChecked()
                || mActivityGamePlayBinding.radioButtonThree.isChecked()
                || !mActivityGamePlayBinding.btnSubmit.isEnabled()){

            position++;
            mActivityGamePlayBinding.btnNextCard.setVisibility(View.INVISIBLE);

            if (position <= mFlashcardId.size() - 1) {
                count++;

                getUrlForNextFlashcard(position);

                setAnswerOptionDefault();
                setInputAnserDefault();
                showFlashcard();
                mActivityGamePlayBinding.time.setBase(SystemClock.elapsedRealtime());
                mActivityGamePlayBinding.time.start();

                if (position == mFlashcardId.size() - 1) {
                    mActivityGamePlayBinding.btnFinish.setVisibility(View.VISIBLE);
                    mActivityGamePlayBinding.btnFinish.setText("Finish");
                }
            } else {
                insertScore();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {

    }
}

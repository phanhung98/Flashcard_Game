package com.ccvn.flashcard_game.views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
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
import com.ccvn.flashcard_game.Common.CustomDialog;
import com.ccvn.flashcard_game.Common.NetworkChangeReceiver;
import com.ccvn.flashcard_game.R;
import com.ccvn.flashcard_game.models.Flashcard;

import com.ccvn.flashcard_game.retrofit.APIUtils;
import com.ccvn.flashcard_game.retrofit.GameAPIService;
import com.ccvn.flashcard_game.viewmodels.GamePlayViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.circularreveal.CircularRevealWidget;

import java.text.DecimalFormat;

import java.util.List;
import java.util.logging.Logger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.ccvn.flashcard_game.views.ListGameFragment.AGE;
import static com.ccvn.flashcard_game.views.ListGameFragment.NAME;
import static com.ccvn.flashcard_game.views.ListGameFragment.SEX;
import static com.ccvn.flashcard_game.views.ListGameFragment.USERINFO;


public class GamePlayActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private static final int RADIO_BOX = 1;
    private static final int INPUT_TEXT = 2;
    public static final int SWIPE_THRESHOLD = 100;
    public static final int SWIPE_VELOCITY_THRESHOLD = 100;
    public static final int RADIO_BUUTON_SIZE = 3;

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

    Button mFinish;
    ImageButton mNextCard;
    Chronometer mChronometer;
    DecimalFormat f;

    EditText mInputAnswer;
    Button mSubmit;
    Group group;
    boolean isRight;

    GestureDetectorCompat detector;
    private CustomDialog mCustomDialog;

    private Flashcard mFlashcard;
    CompositeDisposable compositeDisposable;
    GameAPIService mGameAPIService;

    private int position =0;
    private int count = 1;
    private double score = 0;
    private long time;
    private int totalTime = 0;
    String flashcradId;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        compositeDisposable = new CompositeDisposable();
        mGameAPIService = APIUtils.getAPIService();
        mGamePlayViewModel = ViewModelProviders.of(this).get(GamePlayViewModel.class);

        f = new DecimalFormat("##.##");
        gameAPIService = APIUtils.getAPIService();
        mCustomDialog = new CustomDialog();

        getId();

        getUrlForNextFlashcard(position);

        initview();

        mImageViewText.setVisibility(View.INVISIBLE);
        mImageGameplay.setVisibility(View.INVISIBLE);
        mQuestion.setVisibility(View.INVISIBLE);
        mRadioGroup.setVisibility(View.INVISIBLE);
       group.setVisibility(View.INVISIBLE);
       mNextCard.setVisibility(View.INVISIBLE);
        mFinish.setVisibility(View.INVISIBLE);

        if (NetworkChangeReceiver.isOnline(getBaseContext())) {
            showGamePlay();
        }else {
            Toast.makeText(this, R.string.check_connection, Toast.LENGTH_SHORT).show();
        }
        detector = new GestureDetectorCompat(this, this);
    }

    public void nextCard(View view) {
        if (NetworkChangeReceiver.isOnline(getBaseContext())) {
            showNextFlashcard();
        }else {
            disconnectDialog();
        }
    }

    public void showNextFlashcard(){
        position++;
        mNextCard.setVisibility(View.INVISIBLE);
        if (countToLastFlashcard(mFlashcardId)) {

            count++;
            getUrlForNextFlashcard(position);
            showGamePlay();

            if (isLastFlashcard(mFlashcardId)) {
                mFinish.setVisibility(View.VISIBLE);
                mFinish.setText("Finish");
            }
        }
    }

    public void finishGame(View view){
        insertScore();
    }

    public boolean countToLastFlashcard(List<Integer> mFlashcardId){
        return position <= mFlashcardId.size() - 1;
    }

    public boolean isLastFlashcard(List<Integer> mFlashcardId){
        return position == mFlashcardId.size() - 1;
    }

    private void disconnectDialog() {
        Toast.makeText(this, R.string.check_connection, Toast.LENGTH_SHORT).show();
    }

    private void initview() {
        group = findViewById(R.id.group);
        mQuestionCount = findViewById(R.id.tv_count_question);
        mScore = findViewById(R.id.tv_score);
        mImageGameplay = findViewById(R.id.imageView);
        mRadioGroup = findViewById(R.id.radio_group);
        mAnswerOptionOne = findViewById(R.id.radio_button_one);
        mAnswerOptionTwo = findViewById(R.id.radio_button_two);
        mAnswerOptionThree = findViewById(R.id.radio_button_three);
        mImageViewText = findViewById(R.id.ImageViewText);
        mQuestion = findViewById(R.id.tv_question);
        mChronometer = findViewById(R.id.time);
        mNextCard = findViewById(R.id.btn_next_card);
        mFinish = findViewById(R.id.btn_finish);
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
        Log.d("BBBB", "getUrlForNextFlashcard");
        Log.d("BBBB", ""+pos);
        flashcradId = String.valueOf(mFlashcardId.get(pos));
        url = APIUtils.URL_FLASHCARD + flashcradId;
    }

    public void showGamePlay(){

        mScore.setText(getString(R.string.score) + f.format(score));
        mQuestionCount.setText(count + "/" + mFlashcardId.size());
        compositeDisposable.add(mGameAPIService.getFlashcard(flashcradId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Flashcard>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void accept(Flashcard flashcard) throws Exception {
                        showFlashcard(flashcard);
                        showAnswer(flashcard);
                        mFlashcard = flashcard;
                    }
                }));

        if (position == 0) {
            setStartTime();
        }
    }

    private void setStartTime(){
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void showFlashcard(Flashcard flashcard){
        showWord(flashcard.getWord());
        showImage(flashcard.getUpload_path());
        showWordAndImage(flashcard.getWord(), flashcard.getUpload_path());
        Log.d("BBBB", "ShowFlashcard");
    }

    private void showAnswer(Flashcard flashcard) {
        showRadioBox(flashcard.getType_id(), flashcard);
        showInputText(flashcard.getType_id(), flashcard);
//        mCustomDialog.getProgressBarDialog().dismiss();
        Log.d("BBBB", "set default answer");
        setStartTime();
    }

    private void showRadioBox(int mRadioBox, final Flashcard flashcard) {
        if (isRadioBox(mRadioBox)){

            for (int i = 0; i < 3; i++){
                ((RadioButton)mRadioGroup.getChildAt(i)).setText(flashcard.getValue().get(i));
            }
            Log.d("BBBB", "ShowRadioBox");
            mRadioGroup.setVisibility(View.VISIBLE);
            group.setVisibility(View.INVISIBLE);

            setAnswerOptionDefault();

            mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
    }

    private boolean isRadioBox(int mRadioBox) {
        return mRadioBox == RADIO_BOX;
    }

    private void showInputText(int mInputText, final Flashcard flashcard) {
        if (isInputText(mInputText)){

            mRadioGroup.setVisibility(View.INVISIBLE);
            group.setVisibility(View.VISIBLE);

            setInputAnserDefault();
            Log.d("BBBB", "ShowInputText");

            mSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mInputAnswer.getText().length() == 0) {

                        mInputAnswer.setError("Please input your answer");
                    } else {
                        getRightAnswerInputText(flashcard);
                    }
                }
            });

        }
    }

    public void getRightAnswerInputText(Flashcard flashcard){

        String answer = mInputAnswer.getText().toString();

        if (isRightAnswerInputText(flashcard, answer)){

            showRightAnswerInputText();

        }else {
            showWrongAnswerInputText();
        }
        visibleNextcardButton();
    }

    private void visibleNextcardButton(){
           if(isLastFlashcard(mFlashcardId)){
               mNextCard.setVisibility(View.INVISIBLE);
           }else {
               mNextCard.setVisibility(View.VISIBLE);
           }
    }

    private boolean isInputText(int mInputText) {
        return mInputText == INPUT_TEXT;
    }

    private void showWordAndImage(String word, String image) {

        if (isWordAndImage(word, image)){

            mImageGameplay.setVisibility(View.VISIBLE);
            mQuestion.setVisibility(View.VISIBLE);
            mImageViewText.setVisibility(View.INVISIBLE);

            mQuestion.setText(word + "?");
            Glide.with(GamePlayActivity.this).load(image).into(mImageGameplay);

        }
    }

    private boolean isWordAndImage(String word, String image) {
        return word != null && image != null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showImage(String image) {

        if (isImage(image)){

            mImageViewText.setVisibility(View.INVISIBLE);
            mQuestion.setVisibility(View.INVISIBLE);
//            mImageGameplay.setVisibility(View.VISIBLE);
            Glide.with(GamePlayActivity.this).load(image).into(mImageGameplay);
            showImageAnimation();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showImageAnimation(){
        int cx = mImageGameplay.getWidth() / 2;
        int cy = mImageGameplay.getHeight() / 2;
        float finalRadius = (float) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(mImageGameplay, cx, cy,
                0, finalRadius);
        anim.setDuration(800);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mImageGameplay.setVisibility(View.VISIBLE);
            }
        });
        anim.start();
    }

    private boolean isImage(String image) {
        return image != null;
    }

    private void showWord(String word) {

        if (isWord(word)){
            mImageGameplay.setVisibility(View.INVISIBLE);
            mQuestion.setVisibility(View.INVISIBLE);
            mImageViewText.setVisibility(View.VISIBLE);

            mImageViewText.setText(word);
        }
    }

    private boolean isWord(String word) {
        return word != null;
    }

    public void getRightAnswer(){

        for (int i = 0; i < RADIO_BUUTON_SIZE; i++){
            if (isChecked(i))
                setAnswer();
        }
       visibleNextcardButton();
    }

    private boolean isChecked(int i){
        return ((RadioButton)mRadioGroup.getChildAt(i)).isChecked();
    }

    // Set right and wrong answer
    @SuppressLint("NewApi")
    public void setAnswer(){

        for (int i = 0; i < RADIO_BUUTON_SIZE; i++){

            if (isRightAnswer(i)){
               showRightAnswer(i);
            }
            else if (isWrongAnswer(i)){
                showWrongAnswer(i);
            }
            else {
                setColorUncheckRadioButton(i);
            }
        }
    }
    @SuppressLint("NewApi")
    public void showRightAnswer(int i){

            mRadioGroup.getChildAt(i).setBackground(getDrawable(R.drawable.right_answer));
            mRadioGroup.getChildAt(i).setClickable(false);
            ((RadioButton)mRadioGroup.getChildAt(i)).setTextColor(Color.WHITE);

            setScore();
            getTotalTime();

    }
    @SuppressLint("NewApi")
    public void showWrongAnswer(int i){
        mRadioGroup.getChildAt(i).setBackground(getDrawable(R.drawable.wrong_answer));
        mRadioGroup.getChildAt(i).setClickable(false);
        ((RadioButton)mRadioGroup.getChildAt(i)).setTextColor(Color.WHITE);

        getTotalTime();
    }

    public void setColorUncheckRadioButton(int i){
        ((RadioButton)mRadioGroup.getChildAt(i)).setTextColor(Color.GRAY);
        mRadioGroup.getChildAt(i).setClickable(false);
    }

    public boolean isRight(int i){
        return isRight =  ((RadioButton)mRadioGroup.getChildAt(i)).getText().toString().equals(mFlashcard.getRight_answer());
    }

    public boolean isRightAnswer(int i){
        return isRight(i) && mRadioGroup.getChildAt(i).getId() == mRadioGroup.getCheckedRadioButtonId();
    }

    public boolean isWrongAnswer(int i){
        return !isRight(i) && mRadioGroup.getChildAt(i).getId() == mRadioGroup.getCheckedRadioButtonId();
    }

    // Set radio button to default
    @SuppressLint("NewApi")
    private void setAnswerOptionDefault(){
        Log.d("BBBB", "setAnswerOptionDefault");
        for (int i = 0; i < 3; i++){

            mRadioGroup.getChildAt(i).setBackground(getDrawable(R.drawable.radio_flat_selector));
            mRadioGroup.getChildAt(i).setClickable(true);
            ((RadioButton)mRadioGroup.getChildAt(i)).setTextColor(getResources().getColor(R.color.color_on_background));

        }
        mRadioGroup.clearCheck();

    }

    // Set input text default
    private void setInputAnserDefault(){
        Log.d("BBBB", "setInputAnserDefault");
        mInputAnswer.setEnabled(true);
        mInputAnswer.setText("");
        mInputAnswer.setTextColor(ContextCompat.getColor(this, R.color.color_on_surface));
        mSubmit.setEnabled(true);

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
                        showScoreDialog();
                    }else {
                        Toast.makeText(GamePlayActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }

    //Show score
   private void showScoreDialog(){

        mCustomDialog.showScoreDialog(this, score);

   }

    // set score for each flashcard
    private void setScore() {

        mChronometer.stop();
        time = (SystemClock.elapsedRealtime() - mChronometer.getBase())/1000;
        score = score + Math.round((0.01 + Math.pow(0.99, time)) * 100.0)/100.0;
        mScore.setText(getString(R.string.score) + f.format(score));
    }

    // get total time play game
    private void getTotalTime(){
        mChronometer.stop();
        time = ((SystemClock.elapsedRealtime() - mChronometer.getBase())/1000);
        totalTime = (int) (totalTime + time);
    }

    private void showRightAnswerInputText(){
        mInputAnswer.setTextColor(ContextCompat.getColor(this, R.color.rightColorText));
        mInputAnswer.setEnabled(false);
        mSubmit.setEnabled(false);
        setScore();
        getTotalTime();
    }

    private void showWrongAnswerInputText(){

        mInputAnswer.setTextColor(ContextCompat.getColor(this, R.color.color_error));
        mInputAnswer.setEnabled(false);
        mSubmit.setEnabled(false);
        getTotalTime();
    }

    private boolean isRightAnswerInputText(Flashcard flashcard, String answer){
        return flashcard.getRight_answer().equalsIgnoreCase(answer);
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

                greaterMovement(diffX, diffY);

        return true;
    }

    private void greaterMovement(float diffX, float diffY) {
        if (isgreaterMovement(diffX, diffY))
            leftSwipe(diffX, diffY);
    }

    private void leftSwipe(float diffX, float diffY) {
        if (isLeftSwipe(diffX, diffY))
            onLeftSwipe(diffX);
    }

    private void onLeftSwipe(float diffX){
        if (isLeft(diffX))
            onSwipeLeft();
    }

    private boolean isgreaterMovement(float diffX, float diffY){
        return Math.abs(diffX) > Math.abs(diffY);
    }
    private boolean isLeftSwipe(float diffX, float velocityX){
        return Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD;
    }
    private boolean isLeft(float diffX){
        return diffX < 0;
    }

    private void onSwipeLeft() {

        if (isCheckedRadioButton()){

           showNextFlashcard();
        }
    }

    private boolean isCheckedRadioButton(){
        return mAnswerOptionOne.isChecked() || mAnswerOptionTwo.isChecked() || mAnswerOptionThree.isChecked()
                || !mSubmit.isEnabled();
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

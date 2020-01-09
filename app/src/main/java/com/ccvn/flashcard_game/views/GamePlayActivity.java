package com.ccvn.flashcard_game.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.ccvn.flashcard_game.Common.Common;
import com.ccvn.flashcard_game.Common.CustomDialog;
import com.ccvn.flashcard_game.Common.NetworkChangeReceiver;
import com.ccvn.flashcard_game.R;
import com.ccvn.flashcard_game.databinding.ActivityGamePlayBinding;
import com.ccvn.flashcard_game.models.Flashcard;
import com.ccvn.flashcard_game.retrofit.APIUtils;
import com.ccvn.flashcard_game.retrofit.GameAPIService;
import com.ccvn.flashcard_game.viewmodels.GamePlayViewModel;
import java.text.DecimalFormat;
import java.util.List;
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
    DecimalFormat f;
    boolean isRight;
    GestureDetectorCompat detector;
    Animation animSlideDown;
    private Flashcard mFlashcard;

    private int position = 0;
    private int count = 1;
    private double score = 0;
    private long time;
    private int totalTime = 0;
    private String flashcradId;
    private String url;

    private ActivityGamePlayBinding mActivityGamePlayBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       mActivityGamePlayBinding = DataBindingUtil.setContentView(this, R.layout.activity_game_play);

        mGamePlayViewModel = ViewModelProviders.of(this).get(GamePlayViewModel.class);
        f = new DecimalFormat("##.##");
        gameAPIService = APIUtils.getAPIService();
        detector = new GestureDetectorCompat(this, this);
        getId();
        setViewVisible();
        if (NetworkChangeReceiver.isOnline(getBaseContext())) {
            showGamePlay();
        }else {
            Toast.makeText(this, R.string.check_connection, Toast.LENGTH_SHORT).show();
        }
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
        mActivityGamePlayBinding.btnNextCard.setVisibility(View.INVISIBLE);
        if (countToLastFlashcard(mFlashcardId)){

            count++;
            getUrlForNextFlashcard(position);
            showGamePlay();

            if (isLastFlashcard(mFlashcardId)){
                mActivityGamePlayBinding.btnFinish.setText("Finish");
            }
        }
    }

    public void finishGame(View view){
//        insertScore();
        showScoreDialog();
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
        getUrlForNextFlashcard(position);
    }

    //Set url for next flashcard
    private void getUrlForNextFlashcard(int pos){

        flashcradId = String.valueOf(mFlashcardId.get(pos));
        url = APIUtils.URL_FLASHCARD + flashcradId;
    }

    public void showGamePlay(){
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        mActivityGamePlayBinding.tvScore.setText(getString(R.string.score) + f.format(score));
        mActivityGamePlayBinding.tvCountQuestion.setText(count + "/" + mFlashcardId.size());

        compositeDisposable.add(gameAPIService.getFlashcard(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Flashcard>() {
                    @Override
                    public void accept(Flashcard flashcard) throws Exception {
                        showFlashcard(flashcard);
                        showAnswer(flashcard);
                        mFlashcard = flashcard;
                    }
                }));
    }

    private void setStartTime(){
        mActivityGamePlayBinding.time.setBase(SystemClock.elapsedRealtime());
        mActivityGamePlayBinding.time.start();
    }

    public void showFlashcard(Flashcard flashcard){
        showWord(flashcard.getWord());
        showImage(flashcard.getUpload_path());
        showWordAndImage(flashcard.getWord(), flashcard.getUpload_path());
    }

    private void showAnswer(Flashcard flashcard) {
        showRadioBox(flashcard.getType_id(), flashcard);
        showInputText(flashcard.getType_id(), flashcard);
        setStartTime();
    }

    private void showRadioBox(int mRadioBox, final Flashcard flashcard){
        if (isRadioBox(mRadioBox)){
            mActivityGamePlayBinding.btnSubmit.setEnabled(true);
            for (int i = 0; i < 3; i++){
                ((RadioButton)mActivityGamePlayBinding.radioGroup.getChildAt(i)).setText(flashcard.getValue().get(i));
            }
            mActivityGamePlayBinding.radioGroup.setVisibility(View.VISIBLE);
            mActivityGamePlayBinding.group.setVisibility(View.INVISIBLE);
            setAnswerOptionDefault();

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
    }

    private boolean isRadioBox(int mRadioBox) {
        return mRadioBox == RADIO_BOX;
    }

    private void showInputText(int mInputText, final Flashcard flashcard) {
        if (isInputText(mInputText)){
            mActivityGamePlayBinding.radioGroup.setVisibility(View.INVISIBLE);
            mActivityGamePlayBinding.group.setVisibility(View.VISIBLE);
            setInputAnserDefault();
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
    }

    public void getRightAnswerInputText(Flashcard flashcard){

        String answer = mActivityGamePlayBinding.inputAnswer.getText().toString();

        if (isRightAnswerInputText(flashcard, answer)){

            showRightAnswerInputText();

        }else {
            showWrongAnswerInputText();
        }
        visibleNextcardButton();
    }

    private void visibleNextcardButton(){
           if(isLastFlashcard(mFlashcardId)){
               mActivityGamePlayBinding.btnNextCard.setVisibility(View.INVISIBLE);
               mActivityGamePlayBinding.btnFinish.setVisibility(View.VISIBLE);
           }else {
               mActivityGamePlayBinding.btnNextCard.setVisibility(View.VISIBLE);
           }
    }

    private boolean isInputText(int mInputText) {
        return mInputText == INPUT_TEXT;
    }

    private void showWordAndImage(String word, String image) {

        if (isWordAndImage(word, image)){

            mActivityGamePlayBinding.imageGameplay.setVisibility(View.VISIBLE);
            mActivityGamePlayBinding.tvQuestion.setVisibility(View.VISIBLE);
            mActivityGamePlayBinding.ImageViewText.setVisibility(View.INVISIBLE);

            mActivityGamePlayBinding.tvQuestion.setText(word + "?");
            Glide.with(GamePlayActivity.this).load(image).into(mActivityGamePlayBinding.imageGameplay);

        }
    }

    private boolean isWordAndImage(String word, String image) {
        return word != null && image != null;
    }


    private void showImage(String image) {
        if (isImage(image)){
            mActivityGamePlayBinding.ImageViewText.setVisibility(View.INVISIBLE);
            mActivityGamePlayBinding.tvQuestion.setVisibility(View.INVISIBLE);
            mActivityGamePlayBinding.imageGameplay.setVisibility(View.VISIBLE);
            Glide.with(GamePlayActivity.this).load(image).into(mActivityGamePlayBinding.imageGameplay);
            setImageAnimation();
        }
    }

    private void setImageAnimation(){
        animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        mActivityGamePlayBinding.imageGameplay.startAnimation(animSlideDown);
    }

    private boolean isImage(String image) {
        return image != null;
    }

    private void showWord(String word) {

        if (isWord(word)){
            mActivityGamePlayBinding.imageGameplay.setVisibility(View.INVISIBLE);
            mActivityGamePlayBinding.tvQuestion.setVisibility(View.INVISIBLE);
            mActivityGamePlayBinding.ImageViewText.setVisibility(View.VISIBLE);

            mActivityGamePlayBinding.ImageViewText.setText(word);
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
    }

    private boolean isChecked(int i){
        return ((RadioButton)mActivityGamePlayBinding.radioGroup.getChildAt(i)).isChecked();
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
        visibleNextcardButton();
    }
    @SuppressLint("NewApi")
    public void showRightAnswer(int i){

            mActivityGamePlayBinding.radioGroup.getChildAt(i).setBackground(getDrawable(R.drawable.right_answer));
            mActivityGamePlayBinding.radioGroup.getChildAt(i).setClickable(false);
            ((RadioButton)mActivityGamePlayBinding.radioGroup.getChildAt(i)).setTextColor(Color.WHITE);

            setScore();
            getTotalTime();

    }
    @SuppressLint("NewApi")
    public void showWrongAnswer(int i){
        mActivityGamePlayBinding.radioGroup.getChildAt(i).setBackground(getDrawable(R.drawable.wrong_answer));
        mActivityGamePlayBinding.radioGroup.getChildAt(i).setClickable(false);
        ((RadioButton)mActivityGamePlayBinding.radioGroup.getChildAt(i)).setTextColor(Color.WHITE);

        getTotalTime();
    }

    public void setColorUncheckRadioButton(int i){
        ((RadioButton)mActivityGamePlayBinding.radioGroup.getChildAt(i)).setTextColor(Color.GRAY);
        mActivityGamePlayBinding.radioGroup.getChildAt(i).setClickable(false);
    }

    public boolean isRight(int i){
        return isRight = ((RadioButton)mActivityGamePlayBinding.radioGroup.getChildAt(i)).
                getText().toString().equals(mFlashcard.getRight_answer());
    }

    public boolean isRightAnswer(int i){
        return isRight(i) && mActivityGamePlayBinding.radioGroup.getChildAt(i).getId()
                == mActivityGamePlayBinding.radioGroup.getCheckedRadioButtonId();
    }

    public boolean isWrongAnswer(int i){
        return !isRight(i) && mActivityGamePlayBinding.radioGroup.getChildAt(i).getId() ==
                mActivityGamePlayBinding.radioGroup.getCheckedRadioButtonId();
    }

    // Set radio button to default
    @SuppressLint("NewApi")
    private void setAnswerOptionDefault(){
        Log.d("BBBB", "setAnswerOptionDefault");
        for (int i = 0; i < 3; i++){

            mActivityGamePlayBinding.radioGroup.getChildAt(i).setBackground(getDrawable(R.drawable.radio_flat_selector));
            mActivityGamePlayBinding.radioGroup.getChildAt(i).setClickable(true);
            ((RadioButton)mActivityGamePlayBinding.radioGroup.getChildAt(i)).setTextColor(getResources().getColor(R.color.color_on_background));

        }
        mActivityGamePlayBinding.radioGroup.clearCheck();

    }

    // Set input text default
    private void setInputAnserDefault(){
        Log.d("BBBB", "setInputAnserDefault");
        mActivityGamePlayBinding.inputAnswer.setEnabled(true);
        mActivityGamePlayBinding.inputAnswer.setText("");
        mActivityGamePlayBinding.inputAnswer.setTextColor(ContextCompat.getColor(this, R.color.color_on_surface));
        mActivityGamePlayBinding.btnSubmit.setEnabled(true);
    }

    //Insert score to databaseas
    private void insertScore() {

        if (Common.currentUser == null){
            SharedPreferences preferences = getSharedPreferences(USERINFO, Context.MODE_PRIVATE);
            String name = preferences.getString(NAME, "");
            int age = preferences.getInt(AGE, 0);
            String sex = preferences.getString(SEX, "");
            int gameId = Common.currentGame.getId();

            mGamePlayViewModel.getmSuccess(gameId, score, totalTime, name, age, sex).observe(GamePlayActivity.this, new Observer<String>() {
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
    }

    //Show score
   private void showScoreDialog(){
        CustomDialog mCustomDialog = new CustomDialog();
        mCustomDialog.showScoreDialog(this, score);
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

    private void showRightAnswerInputText(){
        mActivityGamePlayBinding.inputAnswer.setTextColor(ContextCompat.getColor(this, R.color.rightColorText));
        mActivityGamePlayBinding.inputAnswer.setEnabled(false);
        mActivityGamePlayBinding.btnSubmit.setEnabled(false);
        setScore();
        getTotalTime();
    }

    private void showWrongAnswerInputText(){

        mActivityGamePlayBinding.inputAnswer.setTextColor(ContextCompat.getColor(this, R.color.color_error));
        mActivityGamePlayBinding.inputAnswer.setEnabled(false);
        mActivityGamePlayBinding.btnSubmit.setEnabled(false);
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
        return mActivityGamePlayBinding.radioButtonOne.isChecked() ||
                mActivityGamePlayBinding.radioButtonTwo.isChecked() || mActivityGamePlayBinding.radioButtonThree.isChecked()
                || !mActivityGamePlayBinding.btnSubmit.isEnabled();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

}

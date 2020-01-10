package com.ccvn.flashcard_game.views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import com.ccvn.flashcard_game.Common.Common;
import com.ccvn.flashcard_game.Common.CustomDialog;
import com.ccvn.flashcard_game.R;
import com.ccvn.flashcard_game.databinding.ActivityGameDetailBinding;
import com.ccvn.flashcard_game.models.Game;
import com.ccvn.flashcard_game.retrofit.APIUtils;
import com.ccvn.flashcard_game.viewmodels.GameDetailViewModel;
import java.util.ArrayList;
import java.util.List;

public class GameDetailActivity extends AppCompatActivity {

    public static final String FLASHCARD_ID = "FlashCardId";
    public static final String GAMEID = "GameId";
    public List<Integer> mListFlashcardId;
    private int id;

    GameDetailViewModel mGameDetailViewModel;
    private CustomDialog customDialog;
    private ActivityGameDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game_detail);
        mListFlashcardId = new ArrayList<>();
        customDialog = new CustomDialog();
        mGameDetailViewModel = ViewModelProviders.of(this).get(GameDetailViewModel.class);
        getGameId();
        customDialog.showProgressBarDialog(this);
        showGameDetail();
    }

    public void getGameId(){

        Intent intent = getIntent();
        id = intent.getIntExtra(ListGameFragment.GAME_ID, 0);
        mGameDetailViewModel.getGameDetail(APIUtils.URL_GAME_LIST + id);
    }
    private void showGameDetail() {

        mGameDetailViewModel.getAllGamedetail().observe(GameDetailActivity.this, new Observer<Game>() {
            @Override
            public void onChanged(Game game) {
                binding.setGame(game);
                binding.progressBar.setVisibility(View.GONE);
                mListFlashcardId = game.getFlashcard_id();
                Common.currentGame = game;
                customDialog.getProgressBarDialog().dismiss();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void show_game_play(View view) {

        Intent intent = new Intent(GameDetailActivity.this, GamePlayActivity.class);
        intent.putIntegerArrayListExtra(FLASHCARD_ID, (ArrayList<Integer>) mListFlashcardId);
        intent.putExtra(GAMEID, id);
        startActivity(intent);
    }
}

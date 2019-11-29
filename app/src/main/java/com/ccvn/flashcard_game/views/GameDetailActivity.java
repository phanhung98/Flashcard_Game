package com.ccvn.flashcard_game.views;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ccvn.flashcard_game.R;
import com.ccvn.flashcard_game.models.Game;

import com.ccvn.flashcard_game.viewmodels.GameDetailViewModel;
import java.util.ArrayList;
import java.util.List;



public class GameDetailActivity extends AppCompatActivity {


    public List<Integer> mListFlashcardId;
    private TextView mGameNameDetail, mHighestScore, mFlashcardTotal;
    private ImageView mImageDetail;

    GameDetailViewModel mGameDetailViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);




        mListFlashcardId = new ArrayList<>();

            mGameDetailViewModel = ViewModelProviders.of(this).get(GameDetailViewModel.class);
            mGameDetailViewModel.getGameDetail();

        initview();

        showGameDetail();


    }


    private void showGameDetail() {

        mGameDetailViewModel.getAllGamedetail().observe(GameDetailActivity.this, new Observer<List<Game>>() {
            @Override
            public void onChanged(List<Game> games) {
                Intent intent = getIntent();
                int position = intent.getIntExtra("position", 0);
                mGameNameDetail.setText(games.get(position).getName());
                mHighestScore.setText("Highest Score: " + games.get(position).getScore());
                mFlashcardTotal.setText("Consist: " + games.get(position).getFlashcard_total());
                Glide.with(GameDetailActivity.this).load(games.get(position).getUpload_path()).into(mImageDetail);

                mListFlashcardId = games.get(position).getFlashcard_id();
            }
        });

    }

    private void initview() {

        mGameNameDetail = findViewById(R.id.tv_game_name_detail);
        mHighestScore = findViewById(R.id.tv_game_highest_score);
        mFlashcardTotal=findViewById(R.id.tv_flashcard_total);
        mImageDetail = findViewById(R.id.image_detail);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void show_game_play(View view) {
        Intent intent = new Intent(GameDetailActivity.this, GamePlayActivity.class);

        intent.putIntegerArrayListExtra("Id", (ArrayList<Integer>) mListFlashcardId);

        View share_image = findViewById(R.id.image_detail);
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(this,
                share_image, "image");
        startActivity(intent, activityOptions.toBundle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.highscore, menu);
        return true;
    }

}

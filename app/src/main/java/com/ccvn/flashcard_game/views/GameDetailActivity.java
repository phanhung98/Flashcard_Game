package com.ccvn.flashcard_game.views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ccvn.flashcard_game.R;
import com.ccvn.flashcard_game.models.Game;
import com.ccvn.flashcard_game.retrofit.APIUtils;
import com.ccvn.flashcard_game.retrofit.GameAPIService;
import com.ccvn.flashcard_game.viewmodels.GameDetailViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GameDetailActivity extends AppCompatActivity {

    private TextView mGameNameDetail, mHighestScore, mFlashcardTotal;
    private ImageView mImageDetail;


    GameDetailViewModel mGameDetailViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

            mGameDetailViewModel = ViewModelProviders.of(this).get(GameDetailViewModel.class);
            mGameDetailViewModel.get_game_detail();

        initview();

        show_game_detail();


    }


    private void show_game_detail() {

        mGameDetailViewModel.getAllGamedetail().observe(GameDetailActivity.this, new Observer<List<Game>>() {
            @Override
            public void onChanged(List<Game> games) {
                Intent intent = getIntent();
                int position = intent.getIntExtra("position", 0);
                mGameNameDetail.setText(games.get(position).getName());
                mHighestScore.setText("Highest Score: " + games.get(position).getHighestScore());
                mFlashcardTotal.setText("Consist: " + games.get(position).getFlashcardTotal());
                Glide.with(GameDetailActivity.this).load(games.get(position).getThumbnail()).into(mImageDetail);
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
        View share_image = findViewById(R.id.image_detail);
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(this,
                share_image, "image");
        startActivity(intent, activityOptions.toBundle());
    }
}

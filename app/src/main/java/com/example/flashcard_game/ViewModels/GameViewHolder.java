package com.example.flashcard_game.ViewModels;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flashcard_game.Models.Games;
import com.example.flashcard_game.R;
import com.example.flashcard_game.Retrofit.IMyAPI;
import com.example.flashcard_game.Retrofit.RetrofitClient;
import com.example.flashcard_game.ViewModels.Adapter.GameAdapter;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class GameViewHolder extends RecyclerView.ViewHolder {



    public TextView txt_game_name;
    public TextView txt_flashcard_count;
    public ImageView gameImage;


    public GameViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_game_name=(TextView)itemView.findViewById(R.id.game_name);
        txt_flashcard_count=(TextView)itemView.findViewById(R.id.flashcard_count);
        gameImage=(ImageView) itemView.findViewById(R.id.gameImage);

    }

    public void bindto(Games currentGames){

        txt_game_name.setText(currentGames.getName());
       txt_flashcard_count.setText(currentGames.getFlashcard_count()+"");
        Glide.with(GameAdapter.context).load(currentGames.getThumbnail()).into(gameImage);
    }




}

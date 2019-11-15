package com.example.flashcard_game.ViewModels.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flashcard_game.Models.Games;
import com.example.flashcard_game.R;
import com.example.flashcard_game.ViewModels.GameViewHolder;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameViewHolder> {

   public static Context context;
    List<Games> gamelist;

    public GameAdapter(Context context, List<Games> gamelist) {
        this.context = context;
        this.gamelist = gamelist;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_items, parent,false);

        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {


        Games currentGame= gamelist.get(position);
        holder.bindto(currentGame);

    }

    @Override
    public int getItemCount() {
        return gamelist.size();
    }
}

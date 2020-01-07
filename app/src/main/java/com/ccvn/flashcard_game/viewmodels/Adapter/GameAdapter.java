package com.ccvn.flashcard_game.viewmodels.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ccvn.flashcard_game.databinding.GameItemsBinding;
import com.ccvn.flashcard_game.models.Game;
import com.ccvn.flashcard_game.R;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

   public static Context mContext;
   public List<Game> gamelist;
   private OnGameListener mOnGameListener;

    public GameAdapter(Context context, List<Game> gamelist, OnGameListener onGameListener) {
       mContext = context;
        this.gamelist = gamelist;
        this.mOnGameListener= onGameListener;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GameItemsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.game_items, parent, false);

        return new GameViewHolder(binding, mOnGameListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {

        Game currentGame= gamelist.get(position);
        holder.gameItemsBinding.setGame(currentGame);

    }

    @Override
    public int getItemCount() {
        return gamelist.size();
    }

    public class GameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnGameListener onGameListener;
        private GameItemsBinding gameItemsBinding;

        public GameViewHolder(@NonNull GameItemsBinding gameItemsBinding, GameAdapter.OnGameListener onGameListener) {
            super(gameItemsBinding.getRoot());
            this.gameItemsBinding = gameItemsBinding;

            this.onGameListener= onGameListener;
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            onGameListener.onGameClick(getAdapterPosition());
        }
    }
    public interface OnGameListener{
        void onGameClick(int position);
    }

}



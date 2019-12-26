package com.ccvn.flashcard_game.viewmodels.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ccvn.flashcard_game.R;
import com.ccvn.flashcard_game.databinding.HightsocreItemsBinding;
import com.ccvn.flashcard_game.models.Score;

import java.util.List;

public class HighScoreAdapter extends RecyclerView.Adapter<HighScoreAdapter.HighScoreViewHolder>  {

    Context context;
    private List<Score> mHighScoreList;

    public HighScoreAdapter(Context context, List<Score> mHighScoreList) {
        this.context = context;
        this.mHighScoreList = mHighScoreList;
    }

    @NonNull
    @Override
    public HighScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HightsocreItemsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.hightsocre_items, parent, false);

        return new HighScoreViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HighScoreViewHolder holder, int position) {
            Score currentScore = mHighScoreList.get(position);
            holder.hightsocreItemsBinding.setScore(currentScore);
            holder.hightsocreItemsBinding.tvRank.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return mHighScoreList.size();
    }

    public class HighScoreViewHolder extends RecyclerView.ViewHolder{
        HightsocreItemsBinding hightsocreItemsBinding;
        public HighScoreViewHolder(@NonNull HightsocreItemsBinding hightsocreItemsBinding) {
            super(hightsocreItemsBinding.getRoot());
            this.hightsocreItemsBinding = hightsocreItemsBinding;
        }
    }


}


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

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>{

    private Context mContext;
    private List<Score> mScoreList;

    public ScoreAdapter(Context context, List<Score> mScoreList) {
        this.mScoreList = mScoreList;
        mContext = context;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        HightsocreItemsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.hightsocre_items, parent, false);

        return new ScoreViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {

        Score currentScore = mScoreList.get(position);
        holder.hightsocreItemsBinding.setScore(currentScore);
        holder.hightsocreItemsBinding.tvRank.setText(String.valueOf(position+1));

    }

    @Override
    public int getItemCount() {
        return mScoreList.size();
    }

    public class ScoreViewHolder extends RecyclerView.ViewHolder{

        HightsocreItemsBinding hightsocreItemsBinding;

    public ScoreViewHolder(@NonNull HightsocreItemsBinding hightsocreItemsBinding) {
        super(hightsocreItemsBinding.getRoot());
        this.hightsocreItemsBinding = hightsocreItemsBinding;

    }
}

}

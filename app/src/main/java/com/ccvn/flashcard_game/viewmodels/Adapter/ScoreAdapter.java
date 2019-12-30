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

<<<<<<< HEAD
        HightsocreItemsBinding hightsocreItemsBinding;
=======
        TextView mUserName, mHightScore, mRank, mGamename;

    public ScoreViewHolder(@NonNull View itemView) {
        super(itemView);

        mUserName = itemView.findViewById(R.id.tv_userName);
        mHightScore = itemView.findViewById(R.id.tv_score);
        mRank = itemView.findViewById(R.id.tv_rank);
        mGamename = itemView.findViewById(R.id.tv_gamename);

    }
    // this is used for binding data to ViewHolder
    public void bindDataToView(Score currentScore){

        mUserName.setText(currentScore.getUserName());
        mGamename.setText(currentScore.getGameName());
        mHightScore.setText(String.valueOf(currentScore.getScore()));
>>>>>>> 6357388020f41094f882b4277633d0aa46cd7523

    public ScoreViewHolder(@NonNull HightsocreItemsBinding hightsocreItemsBinding) {
        super(hightsocreItemsBinding.getRoot());
        this.hightsocreItemsBinding = hightsocreItemsBinding;

    }
}

}

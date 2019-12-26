package com.ccvn.flashcard_game.viewmodels.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ccvn.flashcard_game.R;
import com.ccvn.flashcard_game.models.Score;

import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    public static Context mContext;
    public List<Score> mScoreList;

    public ScoreAdapter(Context context, List<Score> mScoreList) {
        this.mScoreList = mScoreList;
        mContext = context;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hightsocre_items, parent, false);

        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {

        Score currentScore = mScoreList.get(position);
        holder.bindDataToView(currentScore);
        holder.mRank.setText(String.valueOf(position+1));

    }

    @Override
    public int getItemCount() {
        return mScoreList.size();
    }

    public class ScoreViewHolder extends RecyclerView.ViewHolder{

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


    }

}

}

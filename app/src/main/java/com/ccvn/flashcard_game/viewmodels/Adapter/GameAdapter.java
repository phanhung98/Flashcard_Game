package com.ccvn.flashcard_game.viewmodels.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ccvn.flashcard_game.models.Game;
import com.ccvn.flashcard_game.R;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

   public static Context context;
   public List<Game> gamelist;
   private OnGameListener mOnGameListener;

    public GameAdapter(Context context, List<Game> gamelist, OnGameListener onGameListener) {
        this.context = context;
        this.gamelist = gamelist;
        this.mOnGameListener= onGameListener;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_items, parent,false);

        return new GameViewHolder(view, mOnGameListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {

//        holder.gameImage.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
//        holder.container.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));

        Game currentGame= gamelist.get(position);
        holder.bindto(currentGame);

    }

    @Override
    public int getItemCount() {
        return gamelist.size();
    }

    public class GameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



        public TextView txt_game_name;
        public TextView txt_flashcard_count;
        public ImageView gameImage;

        OnGameListener onGameListener;

//    public RelativeLayout container;

        public GameViewHolder(@NonNull View itemView, GameAdapter.OnGameListener onGameListener) {
            super(itemView);

            txt_game_name=(TextView)itemView.findViewById(R.id.game_name);
            txt_flashcard_count=(TextView)itemView.findViewById(R.id.flashcard_count);
            gameImage=(ImageView) itemView.findViewById(R.id.gameImage);
            this.onGameListener= onGameListener;
//        container=(RelativeLayout) itemView.findViewById(R.id.container);
            itemView.setOnClickListener(this);

        }

        public void bindto(Game currentGame){


            txt_game_name.setText(currentGame.getName());
//       txt_flashcard_count.setText("Consist: "+currentGame.getFlashcard_count()+ " Flashcard");
            Glide.with(GameAdapter.context).load(currentGame.getThumbnail()).into(gameImage);
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



package com.ccvn.flashcard_game.views;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.ccvn.flashcard_game.R;
import com.ccvn.flashcard_game.databinding.HighscoreFragmentBinding;
import com.ccvn.flashcard_game.models.Score;
import com.ccvn.flashcard_game.viewmodels.Adapter.HighScoreAdapter;
import com.ccvn.flashcard_game.viewmodels.ScoreViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HighScoreFragment extends Fragment {
    private ScoreViewModel mViewModel;
    private HighscoreFragmentBinding binding;

    public HighScoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         binding = DataBindingUtil.inflate(inflater,R.layout.highscore_fragment, container, false);

        return binding.getRoot();
    }

    private void runLayoutAnimation(final RecyclerView recyclerView){
        final Context context= recyclerView.getContext();
        final LayoutAnimationController controller=
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_from_right);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);
        mViewModel.getHighScoreEachGame();

        mViewModel.getAllHighScore().observe(HighScoreFragment.this, new Observer<List<Score>>() {
            @Override
            public void onChanged(List<Score> scores) {
                HighScoreAdapter adapter = new HighScoreAdapter(getContext(), scores);
                binding.hightScoreRecycler.setAdapter(adapter);
                binding.hightScoreRecycler.setHasFixedSize(true);
                binding.hightScoreRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                runLayoutAnimation(binding.hightScoreRecycler);

            }
        });
    }
}

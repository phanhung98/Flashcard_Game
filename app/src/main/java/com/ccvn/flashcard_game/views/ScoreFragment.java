package com.ccvn.flashcard_game.views;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.ccvn.flashcard_game.R;
import com.ccvn.flashcard_game.databinding.ScoreFragmentBinding;
import com.ccvn.flashcard_game.models.Score;
import com.ccvn.flashcard_game.viewmodels.Adapter.ScoreAdapter;
import com.ccvn.flashcard_game.viewmodels.ScoreViewModel;

import java.util.List;

public class ScoreFragment extends Fragment {


    private ScoreViewModel mViewModel;
    private ScoreFragmentBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding  = DataBindingUtil.inflate(inflater, R.layout.score_fragment, container, false);

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
        mViewModel.getHighScore();

        mViewModel.getAllHighScore().observe(ScoreFragment.this, new Observer<List<Score>>() {
            @Override
            public void onChanged(List<Score> scores) {

                ScoreAdapter adapter = new ScoreAdapter(getContext(), scores);
                binding.Scorerecycler.setAdapter(adapter);
                binding.Scorerecycler.setHasFixedSize(true);
                binding.Scorerecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                runLayoutAnimation(binding.Scorerecycler);

            }
        });
    }

}

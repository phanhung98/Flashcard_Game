package com.example.flashcard_game.Views.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcard_game.Models.Games;
import com.example.flashcard_game.R;
import com.example.flashcard_game.Retrofit.IMyAPI;
import com.example.flashcard_game.Retrofit.RetrofitClient;
import com.example.flashcard_game.ViewModels.Adapter.GameAdapter;
import com.example.flashcard_game.ViewModels.GameViewHolder;
import com.example.flashcard_game.ViewModels.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    public static List<Games> listgame;

    private HomeViewModel homeViewModel;
    private GameViewHolder gameViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel =
//                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
        recyclerView=(RecyclerView)root.findViewById(R.id.recyclerview);
        listgame= new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        fetchData();



        return root;
    }

    public void fetchData(){
        IMyAPI myAPI;
        CompositeDisposable compositeDisposable= new CompositeDisposable();
        Retrofit retrofit= RetrofitClient.getInstance();
        myAPI= retrofit.create(IMyAPI.class);

        compositeDisposable.add(myAPI.getGames()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Games>>() {
                    @Override
                    public void accept(List<Games> games) throws Exception {
                      display(games);
                    }
                })
        );
    }

    private void display(List<Games> games) {


        GameAdapter adapter= new GameAdapter(getContext(), games);
        recyclerView.setAdapter(adapter);

    }

}
package com.ccvn.flashcard_game.Views.ui.listgame;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ccvn.flashcard_game.Models.Games;
import com.ccvn.flashcard_game.R;
import com.ccvn.flashcard_game.Retrofit.ApiUtils;
import com.ccvn.flashcard_game.Retrofit.getGameList;
import com.ccvn.flashcard_game.ViewModels.Adapter.GameAdapter;
import com.ccvn.flashcard_game.Views.GameActivitys.GameActivity;
import com.ccvn.flashcard_game.Views.GameActivitys.GameDetail;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


@SuppressWarnings("ALL")
public class ListGameFragment extends Fragment implements GameAdapter.OnGameListener {
    getGameList getGameList;

    private RecyclerView recyclerView;
    private List<Games> list;



    CompositeDisposable compositeDisposable= new CompositeDisposable();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        list= new ArrayList<>();
      // Init getGameList class
        getGameList = ApiUtils.getAPIService();

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);


       checkConextion();
        return root;


    }
    // Fetch data form API.
    private void fetchdata(){
        compositeDisposable.add(getGameList.getGames()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Games>>() {
                    @Override
                    public void accept(List<Games> games) throws Exception {
                        displayData(games);
                        list= games;
                    }
                }));
    }
     // setup recyclerview and display.
    private void displayData(List<Games> games) {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        GameAdapter adapter= new GameAdapter(getContext(), games, this);
        recyclerView.setAdapter(adapter);
        runLayoutAnimation(recyclerView);

    }
    // load animation for item.
    private void runLayoutAnimation(final RecyclerView recyclerView){
        final Context context= recyclerView.getContext();
        final LayoutAnimationController controller=
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_from_right);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();

    }
    // click item on recyclerview.
    @Override
    public void onGameClick(int position) {
        if (position ==0){
            Intent intent= new Intent(getActivity(), GameActivity.class);
            startActivity(intent);
        }
        if (position ==1){
            Intent intent= new Intent(getActivity(), GameDetail.class);
            startActivity(intent);
        }
    }
    // check online or not.
    protected boolean isOnline(){
        ConnectivityManager cm= (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo= cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting() ){
            return true;
        }else {
            return false;
        }
    }
    // check conection.
    public void checkConextion(){
        if (isOnline()){
            fetchdata();
        }else {
            Toast.makeText(getActivity(), "Please check your Internet", Toast.LENGTH_SHORT).show();
        }
    }
}
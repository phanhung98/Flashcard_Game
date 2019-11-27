package com.ccvn.flashcard_game.views;

import android.app.ActivityOptions;
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

import com.ccvn.flashcard_game.models.Game;
import com.ccvn.flashcard_game.R;
import com.ccvn.flashcard_game.retrofit.APIUtils;
import com.ccvn.flashcard_game.retrofit.GameAPIService;
import com.ccvn.flashcard_game.viewmodels.Adapter.GameAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


@SuppressWarnings("ALL")
public class ListGameFragment extends Fragment implements GameAdapter.OnGameListener {

    GameAPIService mGameAPIService;

    private RecyclerView mRecyclerView;
    private List<Game> mListGame;



    CompositeDisposable mCompositeDisposable= new CompositeDisposable();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mListGame= new ArrayList<>();
      // Init GameAPIService class
        mGameAPIService = APIUtils.getAPIService();

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);


       checkConection();
        return root;


    }
    // Fetch data form API.
    private void fetchdata(){
        mCompositeDisposable.add(mGameAPIService.getGame()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Game>>() {
                    @Override
                    public void accept(List<Game> games) throws Exception {
                        displayData(games);
                        mListGame= games;
                    }
                }));
    }
     // setup recyclerview and display.
    private void displayData(List<Game> games) {

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        GameAdapter adapter= new GameAdapter(getContext(), games, this);
        mRecyclerView.setAdapter(adapter);
        runLayoutAnimation(mRecyclerView);

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

          int posi = position;
            Intent intent = new Intent(getActivity(), GameDetailActivity.class);
            intent.putExtra("position", posi);

            startActivity(intent);


    }
    // check online or not.
    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting() ){
            return true;
        }else {
            return false;
        }
    }
    // check conection.
    public void checkConection(){
        if (isOnline()){
            fetchdata();
        }else {

            Toast.makeText(getActivity(), R.string.check_connection, Toast.LENGTH_SHORT).show();
        }
    }
}
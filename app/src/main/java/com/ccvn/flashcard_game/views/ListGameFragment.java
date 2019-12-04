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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ccvn.flashcard_game.models.Game;
import com.ccvn.flashcard_game.R;
import com.ccvn.flashcard_game.retrofit.APIUtils;
import com.ccvn.flashcard_game.retrofit.GameAPIService;
import com.ccvn.flashcard_game.viewmodels.Adapter.GameAdapter;
import com.ccvn.flashcard_game.viewmodels.ListGameViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


@SuppressWarnings("ALL")
public class ListGameFragment extends Fragment implements GameAdapter.OnGameListener {

    public static final String GAME_ID = "GameID";

    GameAPIService mGameAPIService;
    private RecyclerView mRecyclerView;
    private List<Game> mListGame;
    private ListGameViewModel mViewModel;



    CompositeDisposable mCompositeDisposable= new CompositeDisposable();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mListGame= new ArrayList<>();

        mViewModel = ViewModelProviders.of(this).get(ListGameViewModel.class);
        mViewModel.getGame();

      // Init GameAPIService class
        mGameAPIService = APIUtils.getAPIService();

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);


       checkConnection();
        return root;


    }

    private void getListGame(){

        mViewModel.getAllGame().observe(ListGameFragment.this, new Observer<List<Game>>() {
            @Override
            public void onChanged(List<Game> games) {
                displayData(games);
                mListGame = games;
            }
        });

    }

     // setup recyclerview and display.
    private void displayData(List<Game> gameList) {


        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        GameAdapter adapter= new GameAdapter(getContext(), gameList, this);
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

          int id = mListGame.get(position).getId();
            Intent intent = new Intent(getActivity(), GameDetailActivity.class);
            intent.putExtra(GAME_ID, id);

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
    public void checkConnection(){
        if (isOnline()){
           getListGame();
        }else {

            Toast.makeText(getActivity(), R.string.check_connection, Toast.LENGTH_SHORT).show();
        }
    }
}
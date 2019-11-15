package com.example.flashcard_game.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.flashcard_game.Models.Games;
import com.example.flashcard_game.Retrofit.IMyAPI;
import com.example.flashcard_game.Retrofit.RetrofitClient;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class HomeViewModel extends ViewModel {



    private MutableLiveData<List<Games>> mGamelist;

    public HomeViewModel() {



        mGamelist = new MutableLiveData<>();
//        mGamelist.setValue();
    }

    public LiveData<List<Games>> getGamelist() {
        return mGamelist;
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
                        getData(games);
                    }
                })
        );

    }

    public List<Games> getData(List<Games> games) {

        return games;

    }
}
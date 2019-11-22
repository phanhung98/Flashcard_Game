package com.ccvn.flashcard_game.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ccvn.flashcard_game.models.Game;
import com.ccvn.flashcard_game.retrofit.APIUtils;
import com.ccvn.flashcard_game.retrofit.GameAPIService;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GameDetailViewModel extends AndroidViewModel {

    private GameAPIService gameAPIService;
    private MutableLiveData<List<Game>> get_all_game_detail;

    CompositeDisposable compositeDisposable= new CompositeDisposable();

    public GameDetailViewModel(@NonNull Application application) {
        super(application);
        gameAPIService = APIUtils.getAPIService();
        get_all_game_detail = new MutableLiveData<>();
    }

    public MutableLiveData<List<Game>> getAllGamedetail(){
        return get_all_game_detail;
    }
    // get all game detail
    public void get_game_detail(){

        compositeDisposable.add(gameAPIService.get_game_detail()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Game>>() {
                    @Override
                    public void accept(List<Game> games) throws Exception {
                            get_all_game_detail.setValue(games);
                    }
                }));

    }

}

package com.ccvn.flashcard_game.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ccvn.flashcard_game.models.Game;
import com.ccvn.flashcard_game.retrofit.APIUtils;
import com.ccvn.flashcard_game.retrofit.GameAPIService;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ListGameViewModel extends ViewModel {

    private GameAPIService mGameAPIService;
    private MutableLiveData<List<Game>> mListgame;

    CompositeDisposable compositeDisposable= new CompositeDisposable();


    public MutableLiveData<List<Game>> getAllGame(){
        if(mListgame == null){
            mGameAPIService = APIUtils.getAPIService();
            mListgame = new MutableLiveData<>();
            getGame();
        }
        return mListgame;
    }

    public void getGame(){

        compositeDisposable.add(mGameAPIService.getGame()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<List<Game>>() {
                                    @Override
                                    public void accept(List<Game> games) throws Exception {
                                       mListgame.setValue(games);
                                    }
                                }));

    }
}
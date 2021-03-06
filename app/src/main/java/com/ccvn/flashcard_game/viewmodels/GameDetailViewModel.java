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

    private GameAPIService mGameAPIService;
    private MutableLiveData<Game> mGamedetail;

    CompositeDisposable compositeDisposable= new CompositeDisposable();

    public GameDetailViewModel(@NonNull Application application) {
        super(application);
        mGameAPIService = APIUtils.getAPIService();
        mGamedetail = new MutableLiveData<>();
    }

    public MutableLiveData<Game> getAllGamedetail(){
        return mGamedetail;
    }
    // get all game detail
    public void getGameDetail(String url){

        compositeDisposable.add(mGameAPIService.getGameDetail(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Game>() {
                    @Override
                    public void accept(Game game) throws Exception {
                        mGamedetail.setValue(game);
                    }
                }));

    }

}

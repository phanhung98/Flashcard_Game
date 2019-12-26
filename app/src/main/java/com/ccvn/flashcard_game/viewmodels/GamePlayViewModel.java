package com.ccvn.flashcard_game.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ccvn.flashcard_game.models.Flashcard;
import com.ccvn.flashcard_game.retrofit.APIUtils;
import com.ccvn.flashcard_game.retrofit.GameAPIService;


import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GamePlayViewModel extends AndroidViewModel {

    private GameAPIService mGameAPIService;
    private MutableLiveData<Flashcard> mFlashcard;
    private MutableLiveData<String> mSuccess;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public GamePlayViewModel(@NonNull Application application) {
        super(application);
        mGameAPIService = APIUtils.getAPIService();
        mFlashcard = new MutableLiveData<>();
        mSuccess = new MutableLiveData<>();
    }

    public MutableLiveData<Flashcard> getAllFlashcard(){
        return mFlashcard;
    }

    public MutableLiveData<String> getmSuccess(){
        return mSuccess;
    }

    public void getNextFlashcard(String url){

        compositeDisposable.add(mGameAPIService.getFlashcard(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Flashcard>() {
                    @Override
                    public void accept(Flashcard flashcard) throws Exception {
                        mFlashcard.setValue(flashcard);
                    }
                }));
    }

    public void storeScore(int gameId, double score, int totalTime, String name, int age, String sex){

        mGameAPIService.insertScore(gameId, score, totalTime, name, age, sex).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<String>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(String s) {
                                        mSuccess.setValue(s);
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
    }



}

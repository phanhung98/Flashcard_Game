package com.ccvn.flashcard_game.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ccvn.flashcard_game.models.Score;
import com.ccvn.flashcard_game.retrofit.APIUtils;
import com.ccvn.flashcard_game.retrofit.GameAPIService;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ScoreViewModel extends AndroidViewModel {

    private GameAPIService mGameApoService;
    private MutableLiveData<List<Score>> mHighScore;

    CompositeDisposable compositeDisposable= new CompositeDisposable();

    public ScoreViewModel(@NonNull Application application) {
        super(application);

        mGameApoService = APIUtils.getAPIService();
        mHighScore = new MutableLiveData<>();

    }

    public MutableLiveData<List<Score>> getAllHighScore(){
        return mHighScore;
    }

    public void getHighScore(){

        compositeDisposable.add(mGameApoService.getHighScore()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<List<Score>>() {
                                    @Override
                                    public void accept(List<Score> scores) throws Exception {
                                        mHighScore.setValue(scores);
                                    }
                                }));

    }


}

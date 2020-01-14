package com.ccvn.flashcard_game.retrofit;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.ccvn.flashcard_game.models.Flashcard;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FlashcardRepository implements GetFlashcard.Presenter {

    private GetFlashcard.View mView;

    public void setView(GetFlashcard.View view){
        mView = view;
    }

    @Override
    public void handleFlashcard(String url) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        GameAPIService mGameAPIService = APIUtils.getAPIService();

        compositeDisposable.add(mGameAPIService.getFlashcard(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Flashcard>() {
                    @Override
                    public void accept(Flashcard flashcard) throws Exception {
                        mView.getCard(flashcard);
                    }
                }));
    }



}

//
//    private Flashcard flashcard = new Flashcard();
//    private MutableLiveData<Flashcard> mFlashcard = new MutableLiveData<>();
//    CompositeDisposable compositeDisposable = new CompositeDisposable();
//
//    private Application application;
//
//    public FlashcardRepository(Application application) {
//        this.application = application;
//    }
//
//    public MutableLiveData<Flashcard> getAllFlashcard(String url){
//
//        GameAPIService mGameAPIService = APIUtils.getAPIService();
//
//        compositeDisposable.add(mGameAPIService.getFlashcard(url)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Flashcard>() {
//                    @Override
//                    public void accept(Flashcard flashcard) throws Exception {
//                        mFlashcard.setValue(flashcard);
//                    }
//                }));
//
//        return mFlashcard;
//    }

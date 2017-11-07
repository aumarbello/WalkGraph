package com.example.ahmed.walkgraph.presentation.splash;

import android.os.SystemClock;

import com.example.ahmed.walkgraph.utils.RxUtils;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by ahmed on 8/9/17.
 */

public class SplashPresenterImpl implements SplashPresenter {
    private SplashFragment fragment;
    private final int Splash_Time_Out = 3000;
    @Inject
    public SplashPresenterImpl(SplashFragmentImpl fragment){
        this.fragment = fragment;
    }
    @Override
    public void openMapFragment() {
        Observable.create(emitter -> {
            SystemClock.sleep(Splash_Time_Out);
            emitter.onNext(5);
            emitter.onComplete();
        }).compose(RxUtils.applySchedulers()).subscribe(observer ->
                fragment.openMapFragment());
    }
}

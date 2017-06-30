package com.firozmemon.fmfileexplorer.ui.base;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by firoz on 30/6/17.
 */

public abstract class BasePresenter {

    protected Scheduler mainScheduler;

    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void unsubscribe() {
        compositeDisposable.clear();
    }
}

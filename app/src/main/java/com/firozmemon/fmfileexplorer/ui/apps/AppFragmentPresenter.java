package com.firozmemon.fmfileexplorer.ui.apps;

import android.content.pm.PackageManager;

import com.firozmemon.fmfileexplorer.helper.PackageManagerHelper;
import com.firozmemon.fmfileexplorer.models.AppModel;
import com.firozmemon.fmfileexplorer.ui.base.BasePresenter;
import com.firozmemon.fmfileexplorer.ui.base.BaseView;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by firoz on 29/6/17.
 */

public class AppFragmentPresenter extends BasePresenter {

    private final BaseView<AppModel> view;
    private PackageManagerHelper helper;

    public AppFragmentPresenter(BaseView<AppModel> view, PackageManagerHelper helper, Scheduler scheduler) {
        this.view = view;
        this.helper = helper;
        mainScheduler = scheduler;
    }

    /**
     * Includes System apps + Installed Apps
     */
    public void fetchAllApps() {
        compositeDisposable.add(helper.getAllApps()
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<List<AppModel>>() {
                    @Override
                    public void onSuccess(@NonNull List<AppModel> appModelList) {
                        if (appModelList.size() > 0)
                            view.onSuccess(appModelList);
                        else
                            view.onError("No Files Found");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.onError("Error: " + e.getMessage());
                    }
                }));
    }

    /**
     * Includes only Installed Apps
     */
    public void fetchAllInstalledApps() {
        compositeDisposable.add(helper.getAllInstalledApps()
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<List<AppModel>>() {
                    @Override
                    public void onSuccess(@NonNull List<AppModel> appModelList) {
                        if (appModelList.size() > 0)
                            view.onSuccess(appModelList);
                        else
                            view.onError("No Files Found");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.onError("Error: " + e.getMessage());
                    }
                }));
    }
}

package com.firozmemon.fmfileexplorer.helper;

import android.content.Context;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;

/**
 * Created by firoz on 11/6/17.
 */

public class StorageUtilHelper {

    private Context context;

    public StorageUtilHelper(Context context) {
        this.context = context;
    }

    public Single<List<String>> getStorageDirectories() {
        return Single.fromCallable(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                List<String> storageDirectories = Collections.EMPTY_LIST;

                storageDirectories = Arrays.asList(StorageUtil.getStorageDirectories(context));

                return storageDirectories;
            }
        });
    }
}

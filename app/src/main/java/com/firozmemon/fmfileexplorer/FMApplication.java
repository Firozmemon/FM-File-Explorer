package com.firozmemon.fmfileexplorer;

import android.app.Application;

import com.firozmemon.fmfileexplorer.helper.StorageUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by firoz on 28/6/17.
 */

public class FMApplication extends Application {

    private List<String> storageDirectories;

    @Override
    public void onCreate() {
        super.onCreate();

        storageDirectories = Arrays.asList(StorageUtil.getStorageDirectories(this));
    }

    public List<String> getStorageDirectories() {
        return storageDirectories;
    }
}

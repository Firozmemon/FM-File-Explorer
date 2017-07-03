package com.firozmemon.fmfileexplorer.models;

import android.content.Intent;
import android.graphics.drawable.Drawable;

import java.io.File;

/**
 * Created by firoz on 29/6/17.
 */

public class AppModel {
    String packageName;
    Drawable appIcon;
    String appName;
    Intent launchIntent;
    File backupFile;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Intent getLaunchIntent() {
        return launchIntent;
    }

    public void setLaunchIntent(Intent launchIntent) {
        this.launchIntent = launchIntent;
    }

    public File getBackupFile() {
        return backupFile;
    }

    public void setBackupFile(File backupFile) {
        this.backupFile = backupFile;
    }
}

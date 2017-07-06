package com.firozmemon.fmfileexplorer.helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.firozmemon.fmfileexplorer.models.AppModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;

/**
 * Created by firoz on 29/6/17.
 */

public class PackageManagerHelper {

    private PackageManager packageManager;
    private Context context;
    private float APP_ICON_SIZE_FACTOR = 100;

    private PackageManagerHelper(PackageManager packageManager, Context context) {
        this.packageManager = packageManager;
        this.context = context;
    }

    public static PackageManagerHelper getInstance(PackageManager packageManager, Context context) {
        return new PackageManagerHelper(packageManager, context);
    }

    /**
     * Fetches all apps(System + Installed) from device
     *
     * @return list of app containing PackageName, Icon, AppName, LaunchingIntent & apkFileLocation
     */
    public Single<List<AppModel>> getAllApps() {
        return Single.fromCallable(new Callable<List<AppModel>>() {
            @Override
            public List<AppModel> call() throws Exception {
                List<AppModel> appModelList = new ArrayList<>();
                List<PackageInfo> packages = packageManager.getInstalledPackages(0);

                for (PackageInfo info : packages) {
                    // Get icon for app
                    Drawable icon = null;
                    try {
                        icon = packageManager.getApplicationIcon(info.packageName);
                        icon = scaleImage(icon, APP_ICON_SIZE_FACTOR);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    // Get app name
                    String appName = null;
                    try {
                        appName = packageManager.getApplicationLabel(info.applicationInfo).toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Get launch intent for package
                    Intent intent = packageManager.getLaunchIntentForPackage(info.packageName);
                    // Get file for backup
                    File file = new File(info.applicationInfo.publicSourceDir);
                    if (!file.exists())
                        file = null;

                    AppModel appModel = new AppModel();
                    appModel.setPackageName(info.packageName);
                    appModel.setAppIcon(icon);
                    appModel.setAppName(appName);
                    appModel.setLaunchIntent(intent);
                    appModel.setBackupFile(file);

                    appModelList.add(appModel);
                }

                Collections.sort(appModelList, new AppComparator());

                return appModelList;
            }
        });
    }

    /**
     * Fetches all Installed apps from device
     *
     * @return list of installed app containing PackageName, Icon, AppName, LaunchingIntent & apkFileLocation
     */
    public Single<List<AppModel>> getAllInstalledApps() {
        return Single.fromCallable(new Callable<List<AppModel>>() {
            @Override
            public List<AppModel> call() throws Exception {
                List<AppModel> appModelList = new ArrayList<>();
                List<PackageInfo> packages = packageManager.getInstalledPackages(0);

                for (PackageInfo info : packages) {
                    if (!isSystemPackage(info)) {
                        // Get icon for app
                        Drawable icon = null;
                        try {
                            icon = packageManager.getApplicationIcon(info.packageName);
                            icon = scaleImage(icon, APP_ICON_SIZE_FACTOR);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        // Get app name
                        String appName = null;
                        try {
                            appName = packageManager.getApplicationLabel(info.applicationInfo).toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // Get launch intent for package
                        Intent intent = packageManager.getLaunchIntentForPackage(info.packageName);
                        // Get file for backup
                        File file = new File(info.applicationInfo.publicSourceDir);
                        if (!file.exists())
                            file = null;

                        AppModel appModel = new AppModel();
                        appModel.setPackageName(info.packageName);
                        appModel.setAppIcon(icon);
                        appModel.setAppName(appName);
                        appModel.setLaunchIntent(intent);
                        appModel.setBackupFile(file);

                        appModelList.add(appModel);
                    }
                }

                Collections.sort(appModelList, new AppComparator());

                return appModelList;
            }
        });
    }

    /**
     * Return whether the given PackgeInfo represents a system package or not.
     * User-installed packages (Market or otherwise) should not be denoted as
     * system packages.
     *
     * @param pkgInfo
     * @return true -> if system app, false -> if non-system app
     */
    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    /**
     * Sorting apps by its name in Ascending order
     */
    private class AppComparator implements Comparator<AppModel> {

        @Override
        public int compare(AppModel a1, AppModel a2) {
            if (a1.getAppName() != null && a2.getAppName() != null) {
                return a1.getAppName().compareToIgnoreCase(a2.getAppName());
            } else if (a1.getAppName() != null) {
                return 1;
            } else if (a2.getAppName() != null) {
                return -1;
            }
            return 0;
        }
    }

    /**
     * Used to resize app icon, such that all appIcons have same size
     *
     * @param image
     * @param scaleFactor
     * @return
     */
    public Drawable scaleImage(Drawable image, float scaleFactor) {

        if ((image == null) || !(image instanceof BitmapDrawable)) {
            return image;
        }

        Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
        return new BitmapDrawable(context.getResources(), getResizedBitmap(bitmap, (int) scaleFactor));

    }

    /**
     * Resize Bitmap maintaining ratio
     * @param image
     * @param maxSize
     * @return
     */
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    /**
     * Resize Bitmap with given size(width & height)
     * @param image
     * @param bitmapWidth
     * @param bitmapHeight
     * @return
     */
    public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }
}

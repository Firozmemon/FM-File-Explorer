package com.firozmemon.fmfileexplorer.ui.main;

import com.firozmemon.fmfileexplorer.helper.StorageUtilHelper;
import com.firozmemon.fmfileexplorer.models.FileModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by firoz on 11/6/17.
 */

public class MainActivityPresenter {

    private MainActivityView view;
    private Scheduler mainScheduler;
    private StorageUtilHelper storageUtilHelper;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainActivityPresenter(MainActivityView view, StorageUtilHelper storageUtilHelper, Scheduler scheduler) {
        this.view = view;
        this.storageUtilHelper = storageUtilHelper;
        mainScheduler = scheduler;
    }

    public void unsubscribe() {
        compositeDisposable.clear();
    }

    public void fetchAllStorageDirectories() {
        compositeDisposable.add(storageUtilHelper.getStorageDirectories()
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .map(new Function<List<String>, List<FileModel>>() {
                    @Override
                    public List<FileModel> apply(@NonNull List<String> strings) throws Exception {
                        List<FileModel> fileModelList = new ArrayList<FileModel>();
                        for (String filePathString : strings) {
                            File[] listFiles = new File(filePathString).listFiles();
                            Arrays.sort(listFiles, new MyCustomFileNameComparator());

                            for (File myFile : listFiles) {
                                FileModel tempFileModel = new FileModel();
                                tempFileModel.setParentDirectoryPath(filePathString);
                                tempFileModel.setFolder(myFile.isDirectory());
                                tempFileModel.setName(myFile.getName());
                                tempFileModel.setFileSize(getFileSize(myFile));

                                fileModelList.add(tempFileModel);
                            }
                        }
                        return fileModelList;
                    }
                })
                .subscribeWith(new DisposableSingleObserver<List<FileModel>>() {
                    @Override
                    public void onSuccess(@NonNull List<FileModel> fileModels) {
                        if (fileModels.size() > 0)
                            view.onSuccess(fileModels);
                        else
                            view.onError("No Files Found");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.onError("Error: " + e.getMessage());
                    }
                }));
    }

    public void fetchCurrentStorageDirectory(final String currentDirPath) {
        compositeDisposable.add(Single
                .fromCallable(new Callable<List<FileModel>>() {
                    @Override
                    public List<FileModel> call() throws Exception {
                        List<FileModel> fileModelList = new ArrayList<FileModel>();

                        File mFile = new File(currentDirPath);
                        File[] filesList = mFile.listFiles();
                        Arrays.sort(filesList, new MyCustomFileNameComparator());

                        for (File myFile : filesList) {
                            FileModel tempFileModel = new FileModel();
                            tempFileModel.setName(myFile.getName());
                            tempFileModel.setFolder(myFile.isDirectory());
                            tempFileModel.setParentDirectoryPath(myFile.getParent());
                            tempFileModel.setFileSize(getFileSize(myFile));

                            fileModelList.add(tempFileModel);
                        }

                        return fileModelList;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<List<FileModel>>() {
                    @Override
                    public void onSuccess(@NonNull List<FileModel> fileModels) {
                        if (fileModels.size() > 0)
                            view.onSuccess(fileModels);
                        else
                            view.onError("No Files Found");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.onError("Error: " + e.getMessage());
                    }
                }));
    }

    public void loadModelData(final FileModel fileModel) {
        compositeDisposable.add(Single
                .fromCallable(new Callable<List<FileModel>>() {
                    @Override
                    public List<FileModel> call() throws Exception {
                        List<FileModel> fileModelList = new ArrayList<FileModel>();

                        File mFile = new File(fileModel.getParentDirectoryPath(), fileModel.getName());
                        File[] filesList = mFile.listFiles();
                        Arrays.sort(filesList, new MyCustomFileNameComparator());

                        for (File myFile : filesList) {
                            FileModel tempFileModel = new FileModel();
                            tempFileModel.setName(myFile.getName());
                            tempFileModel.setFolder(myFile.isDirectory());
                            tempFileModel.setParentDirectoryPath(myFile.getParent());
                            tempFileModel.setFileSize(getFileSize(myFile));

                            fileModelList.add(tempFileModel);
                        }

                        return fileModelList;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<List<FileModel>>() {
                    @Override
                    public void onSuccess(@NonNull List<FileModel> fileModels) {
                        view.onSuccess(fileModels);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.onError("Error: " + e.getMessage());
                    }
                }));
    }

    public void loadParentModelData(final String currentDirectory) {
        compositeDisposable.add(Single
                .fromCallable(new Callable<List<FileModel>>() {
                    @Override
                    public List<FileModel> call() throws Exception {
                        List<FileModel> fileModelList = new ArrayList<FileModel>();

                        File mFile = new File(currentDirectory).getParentFile();
                        File[] filesList = mFile.listFiles();
                        Arrays.sort(filesList, new MyCustomFileNameComparator());

                        for (File myFile : filesList) {
                            FileModel tempFileModel = new FileModel();
                            tempFileModel.setName(myFile.getName());
                            tempFileModel.setFolder(myFile.isDirectory());
                            tempFileModel.setParentDirectoryPath(myFile.getParent());
                            tempFileModel.setFileSize(getFileSize(myFile));

                            fileModelList.add(tempFileModel);
                        }

                        return fileModelList;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<List<FileModel>>() {
                    @Override
                    public void onSuccess(@NonNull List<FileModel> fileModels) {
                        view.onSuccess(fileModels);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.onError("Error: " + e.getMessage());
                    }
                }));
    }

    private class MyCustomFileNameComparator implements Comparator<File> {

        @Override
        public int compare(File f1, File f2) {
            if (f1.isDirectory() && f2.isDirectory()) { // Both are directories
                return f1.getName().compareToIgnoreCase(f2.getName());
            } else {
                if (f1.isDirectory())       // Only f1 is directory, f2 is file
                    return -1;
                else if (f2.isDirectory())  // Only f2 is directory, f1 is file
                    return 1;
                else                        // Both are files
                    return f1.getName().compareToIgnoreCase(f2.getName());
            }
        }
    }

    /**
     * Get the File Size in human readable format
     * @param myFile
     * @return String containing file size
     */
    private String getFileSize(File myFile) {
        String fileSizeString = "";
        long fileSizeBytes = getFolderSize(myFile);
        if (fileSizeBytes >= 1024) {
            long fileSizeKb = fileSizeBytes / 1024;
            if (fileSizeKb >= 1024) {
                long fileSizeMb = fileSizeKb / 1024;
                if (fileSizeMb >= 1024)
                    fileSizeString = fileSizeMb / 1024 + " Gb";
                else
                    fileSizeString = fileSizeMb + " Mb";
            } else
                fileSizeString = fileSizeKb + " Kb";
        } else
            fileSizeString = fileSizeBytes + " byte(s)";
        return fileSizeString;
    }

    /**
     * Gets the file size of specific directory/file
     * @param f
     * @return size of file/directory in bytes
     */
    private long getFolderSize(File f) {
        long size = 0;
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                size += getFolderSize(file);
            }
        } else {
            size = f.length();
        }
        return size;
    }
}

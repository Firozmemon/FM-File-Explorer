package com.firozmemon.fmfileexplorer.ui.main;

import com.firozmemon.fmfileexplorer.models.FileModel;

import java.util.List;

/**
 * Created by firoz on 11/6/17.
 */

public interface MainActivityView {

    void onSuccess(List<FileModel> fileModelList);

    void onError(String strText);
}

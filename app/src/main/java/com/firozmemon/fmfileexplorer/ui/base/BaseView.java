package com.firozmemon.fmfileexplorer.ui.base;

import java.util.List;

/**
 * Created by firoz on 30/6/17.
 */

public interface BaseView<T> {

    void onSuccess(List<T> list);

    void onSuccess(String currentDirectoryName);

    void onError(String strText);
}

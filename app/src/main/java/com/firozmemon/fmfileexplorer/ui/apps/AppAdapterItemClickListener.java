package com.firozmemon.fmfileexplorer.ui.apps;

import android.view.View;

import com.firozmemon.fmfileexplorer.ui.base.AdapterItemClickListener;

/**
 * Created by firoz on 6/7/17.
 */

public interface AppAdapterItemClickListener extends AdapterItemClickListener {

    void onAppBackupClicked(View view, int position);

    void onAppUnInstallClicked(View view, int position);

    void onAppPropertiesClicked(View view, int position);

}

package com.firozmemon.fmfileexplorer.ui.base;

import android.view.View;

public interface AdapterItemClickListener {
    void onAdapterItemClick(View view, int position);

    void onAdapterItemLongClick(View view, int position);
}
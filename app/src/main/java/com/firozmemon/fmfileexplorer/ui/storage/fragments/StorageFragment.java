package com.firozmemon.fmfileexplorer.ui.storage.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.firozmemon.fmfileexplorer.R;
import com.firozmemon.fmfileexplorer.helper.FileUtil;
import com.firozmemon.fmfileexplorer.helper.StorageUtilHelper;
import com.firozmemon.fmfileexplorer.models.FileModel;
import com.firozmemon.fmfileexplorer.ui.storage.StorageAdapter;
import com.firozmemon.fmfileexplorer.ui.storage.StoragePresenter;
import com.firozmemon.fmfileexplorer.ui.base.BaseFragment;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by firoz on 25/6/17.
 */

public class StorageFragment extends BaseFragment<FileModel> {

    private String CURRENT_DIR = "";
    StoragePresenter presenter;
    private final static String BUNDLE_KEY = "path";

    public static StorageFragment getInstance(String path) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY, path);

        StorageFragment fragment = new StorageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getActivity().getResources().getString(R.string.app_name));

    }

    @Override
    public void initialSetup() {
        // Setting CURRENT DIR path
        CURRENT_DIR = getArguments().getString(BUNDLE_KEY);

        CURRENT_DIR_PATH = CURRENT_DIR;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (presenter == null) {
            presenter = new StoragePresenter(this, new StorageUtilHelper(getActivity()), AndroidSchedulers.mainThread());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.fetchCurrentStorageDirectory(CURRENT_DIR);
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    @Override
    public void onAdapterItemClick(View view, int position) {
        FileModel fileModel = adapter.getItem(position);

        if (fileModel.isFolder())
            presenter.loadModelData(fileModel);
        else {
            // File Detected
            // open view intent depending on fileType
            try {
                File currentFile = new File(fileModel.getParentDirectoryPath(), fileModel.getName());
                MimeTypeMap myMime = MimeTypeMap.getSingleton();
                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                String mimeType = myMime.getMimeTypeFromExtension(FileUtil.getInstance().fileExt(currentFile.getCanonicalPath()));
                newIntent.setDataAndType(Uri.fromFile(currentFile), mimeType);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    startActivity(newIntent);
                } catch (ActivityNotFoundException e) {
                    Snackbar.make(view, "No handler for this type of file.", Snackbar.LENGTH_LONG)
                            .show();
                }
            } catch (IOException e) {
                Snackbar.make(view, "Could not fetch file", Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    }

    @Override
    public void onAdapterItemLongClick(View view, int position) {
        Snackbar.make(view, "Adapter Long Click Operation", Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onSuccess(List<FileModel> fileModelList) {
        if (!fileModelList.isEmpty()) {
            CURRENT_DIR = fileModelList.get(0).getParentDirectoryPath();

            adapter = new StorageAdapter(getActivity(), fileModelList);
            adapter.setItemClickListener(this);

            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            recyclerView.setAdapter(adapter);

            recyclerView.setVisibility(View.VISIBLE);
            noFilesFoundTextView.setVisibility(View.GONE);
        } else {
            CURRENT_DIR = CURRENT_DIR + "/0";

            recyclerView.setVisibility(View.GONE);
            noFilesFoundTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError(String strText) {
        Snackbar.make(getView(), strText, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void performBackPressedOperation() {
        if (CURRENT_DIR_PATH.equalsIgnoreCase(CURRENT_DIR)) {
            super.performBackPressedOperation();
        } else {
            presenter.loadParentModelData(CURRENT_DIR);
        }
    }
}

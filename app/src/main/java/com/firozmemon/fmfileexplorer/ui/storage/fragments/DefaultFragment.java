package com.firozmemon.fmfileexplorer.ui.storage.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.firozmemon.fmfileexplorer.R;
import com.firozmemon.fmfileexplorer.helper.FileUtil;
import com.firozmemon.fmfileexplorer.helper.StorageUtilHelper;
import com.firozmemon.fmfileexplorer.models.FileModel;
import com.firozmemon.fmfileexplorer.ui.base.AlertDialogCallback;
import com.firozmemon.fmfileexplorer.ui.base.BaseFragment;
import com.firozmemon.fmfileexplorer.ui.storage.StorageAdapter;
import com.firozmemon.fmfileexplorer.ui.storage.StoragePresenter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by firoz on 25/6/17.
 */

public class DefaultFragment extends BaseFragment<FileModel> {

    private String CURRENT_DIR = "";
    StoragePresenter presenter;

    public static DefaultFragment getInstance() {
        return new DefaultFragment();
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
        CURRENT_DIR = System.getenv("EXTERNAL_STORAGE");
        if (CURRENT_DIR == null) {
            try {
                CURRENT_DIR = Environment.getExternalStorageDirectory().getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
                CURRENT_DIR = "/"; // If nothing shows up, display root directory (This should never happen)
            }
        }
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
    public void onSuccess(String currentDirectoryName) {
        CURRENT_DIR = CURRENT_DIR + File.separator + currentDirectoryName;

        recyclerView.setVisibility(View.GONE);
        noFilesFoundTextView.setVisibility(View.VISIBLE);
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

    @Override
    public void onAdapterItemDeleteClicked(View view, int position) {
        FileModel fileModel = adapter.getItem(position);

        presenter.performFileDeletion(fileModel);
    }

    @Override
    public void onAdapterItemRenameClicked(View view, int position) {
        final FileModel fileModel = adapter.getItem(position);    // Fetching current file

        // Display Dialog to fetch new fileName
        displayAlertDialogWithEdittext(getActivity(),
                "Rename",
                "New File Name",
                fileModel.getName(),
                new AlertDialogCallback() {
                    @Override
                    public void alertDialogPositiveButtonClicked(Object obj) {
                        String newFileName = String.valueOf(obj);

                        presenter.renameFile(fileModel, newFileName);
                    }

                    @Override
                    public void alertDialogNegativeButtonClicked(String message) {
                        if (message != null)
                            onError(message);
                    }
                });
    }

    @Override
    public void fabClicked() {
        getView().setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.setHeaderTitle("Select Action");
                contextMenu.add("Create File")
                        .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                displayAlertDialogWithEdittext(getActivity(),
                                        "Create File",
                                        "New File Name",
                                        null,
                                        new AlertDialogCallback() {
                                            @Override
                                            public void alertDialogPositiveButtonClicked(Object obj) {
                                                String newFileName = String.valueOf(obj);

                                                presenter.createNewFile(CURRENT_DIR, newFileName, false);
                                            }

                                            @Override
                                            public void alertDialogNegativeButtonClicked(String message) {
                                                if (message != null)
                                                    onError(message);
                                            }
                                        });

                                return true;
                            }
                        });
                contextMenu.add("Create Folder")
                        .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                displayAlertDialogWithEdittext(getActivity(),
                                        "Create Folder",
                                        "New Folder Name",
                                        null,
                                        new AlertDialogCallback() {
                                            @Override
                                            public void alertDialogPositiveButtonClicked(Object obj) {
                                                String newFileName = String.valueOf(obj);

                                                presenter.createNewFile(CURRENT_DIR, newFileName, true);
                                            }

                                            @Override
                                            public void alertDialogNegativeButtonClicked(String message) {
                                                if (message != null)
                                                    onError(message);
                                            }
                                        });

                                return true;
                            }
                        });
            }
        });
        getView().showContextMenu();    // calling context menu on single click
    }
}

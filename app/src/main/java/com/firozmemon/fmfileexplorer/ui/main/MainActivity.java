package com.firozmemon.fmfileexplorer.ui.main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.firozmemon.fmfileexplorer.R;
import com.firozmemon.fmfileexplorer.helper.StorageUtil;
import com.firozmemon.fmfileexplorer.helper.StorageUtilHelper;
import com.firozmemon.fmfileexplorer.models.FileModel;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity implements MainActivityAdapter.AdapterItemClickListener, MainActivityView {

    @BindView(R.id.coodinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @OnClick(R.id.fab)
    void onFabClick() {
        Snackbar.make(coordinatorLayout, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    MainActivityPresenter presenter;
    MainActivityAdapter adapter;
    String CURRENT_DIR = "";
    final String ROOT_DIR = "/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        presenter = new MainActivityPresenter(this, new StorageUtilHelper(this), AndroidSchedulers.mainThread());
//        presenter.fetchAllStorageDirectories();
        CURRENT_DIR = System.getenv("EXTERNAL_STORAGE");
        presenter.fetchCurrentStorageDirectory(CURRENT_DIR);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                String mimeType = myMime.getMimeTypeFromExtension(fileExt(currentFile.getCanonicalPath()));
                newIntent.setDataAndType(Uri.fromFile(currentFile), mimeType);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    startActivity(newIntent);
                } catch (ActivityNotFoundException e) {
                    Snackbar.make(coordinatorLayout, "No handler for this type of file.", Snackbar.LENGTH_LONG)
                            .show();
                }
            } catch (IOException e) {
                Snackbar.make(coordinatorLayout, "Could not fetch file", Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    }

    @Override
    public void onAdapterItemLongClick(View view, int position) {
        Snackbar.make(coordinatorLayout, "Adapter Long Click Operation", Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onSuccess(List<FileModel> fileModelList) {
        if (!fileModelList.isEmpty())
            CURRENT_DIR = fileModelList.get(0).getParentDirectoryPath();
        else
            CURRENT_DIR = CURRENT_DIR + "/0";
        adapter = new MainActivityAdapter(MainActivity.this, fileModelList);
        adapter.setItemClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onError(String strText) {
        Snackbar.make(coordinatorLayout, strText, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onBackPressed() {
        List<String> storageDirs = Arrays.asList(StorageUtil.getStorageDirectories(MainActivity.this));
        if (CURRENT_DIR.equalsIgnoreCase(System.getenv("EXTERNAL_STORAGE")) ||
                CURRENT_DIR.equalsIgnoreCase(ROOT_DIR) ||
                storageDirs.contains(CURRENT_DIR)) {
            super.onBackPressed();
        } else {
            presenter.loadParentModelData(CURRENT_DIR);
        }
    }

    /**
     * Get file extension from string passed as param
     *
     * @param url
     * @return file extension
     */
    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }
}

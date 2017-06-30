package com.firozmemon.fmfileexplorer.ui.apps;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.firozmemon.fmfileexplorer.R;
import com.firozmemon.fmfileexplorer.helper.PackageManagerHelper;
import com.firozmemon.fmfileexplorer.models.AppModel;
import com.firozmemon.fmfileexplorer.ui.base.BaseFragment;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by firoz on 29/6/17.
 */

public class AppFragment extends BaseFragment<AppModel> {

    AppFragmentPresenter presenter;
    AppAdapter adapter;

    public static AppFragment getInstance() {
        return new AppFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getString(R.string.installedApps));

    }

    @Override
    public void initialSetup() {
        // not required for app
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (presenter == null) {
            presenter = new AppFragmentPresenter(this,
                    PackageManagerHelper.getInstance(getActivity().getPackageManager()),
                    AndroidSchedulers.mainThread());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.fetchAllInstalledApps();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    @Override
    public void onAdapterItemClick(View view, int position) {
        AppModel appModel = adapter.getItem(position);
        Intent intent = appModel.getLaunchIntent();
        if (intent == null) {
            Snackbar.make(view, "Could not launch App", Snackbar.LENGTH_LONG)
                    .show();
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onAdapterItemLongClick(View view, int position) {
        Snackbar.make(view, "Adapter Long Click Operation", Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onSuccess(List<AppModel> appModelList) {
        if (!appModelList.isEmpty()) {

            adapter = new AppAdapter(getActivity(), appModelList);
            adapter.setItemClickListener(this);

            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            recyclerView.setAdapter(adapter);

            recyclerView.setVisibility(View.VISIBLE);
            noFilesFoundTextView.setVisibility(View.GONE);
        } else {
            //CURRENT_DIR = CURRENT_DIR + "/0";

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
        super.performBackPressedOperation();
    }
}

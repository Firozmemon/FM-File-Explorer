package com.firozmemon.fmfileexplorer.ui.base;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firozmemon.fmfileexplorer.R;
import com.firozmemon.fmfileexplorer.ui.storage.StorageAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by firoz on 25/6/17.
 */

public abstract class BaseFragment<T> extends Fragment implements AdapterItemClickListener, BaseView<T> {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.noFilesFoundTextView)
    public TextView noFilesFoundTextView;

    public StorageAdapter adapter;
    public String CURRENT_DIR_PATH = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_default, container, false);
        ButterKnife.bind(this, view);

        if (CURRENT_DIR_PATH.equals(""))
            initialSetup();
        return view;
    }

    public abstract void initialSetup();

    boolean doubleBackToExitPressedOnce = false;

    public void performBackPressedOperation() {
        if (doubleBackToExitPressedOnce) {
            getActivity().finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(getActivity(), "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }
}

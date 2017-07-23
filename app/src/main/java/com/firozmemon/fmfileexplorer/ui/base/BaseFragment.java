package com.firozmemon.fmfileexplorer.ui.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

    @Override
    public void onAdapterItemDeleteClicked(View view, int position) {
        // Not doing anything here, this will be implemented in child class, if required.
    }

    @Override
    public void onAdapterItemRenameClicked(View view, int position) {
        // Not doing anything here, this will be implemented in child class, if required.
    }

    /**
     * Displaying Alert Dialog with EditText in it
     *
     * @param activity
     * @param title used to display alertDialog title
     * @param hint used to display hint inside EditText
     * @param defaultText used to display inside EditText
     * @param callback
     */
    public void displayAlertDialogWithEdittext(Activity activity,
                                               String title,
                                               String hint,
                                               String defaultText,
                                               final AlertDialogCallback callback) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        final View dialogCustomView = layoutInflater.inflate(R.layout.alertdialog_edittext, null);
        alertDialogBuilder.setView(dialogCustomView);

        final EditText dialog_editText = (EditText) dialogCustomView.findViewById(R.id.dialog_editText);
        dialog_editText.setHint(hint);
        if (defaultText != null)
            dialog_editText.setText(defaultText);

        alertDialogBuilder.setTitle(title);
//        alertDialogBuilder.setMessage("");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (callback != null) {
                    // Handling textEntered here,
                    // preventing to add code handling multiple times later
                    String textEntered = dialog_editText.getText().toString();

                    if (textEntered != null && !("".equalsIgnoreCase(textEntered)))
                        callback.alertDialogPositiveButtonClicked(textEntered);
                    else
                        onError("Invalid Text Entered");
                }
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void fabClicked() {
        onError("Could not perform any operation");
    }
}

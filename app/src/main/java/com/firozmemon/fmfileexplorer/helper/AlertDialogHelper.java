package com.firozmemon.fmfileexplorer.helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.firozmemon.fmfileexplorer.R;
import com.firozmemon.fmfileexplorer.ui.MainActivity;
import com.firozmemon.fmfileexplorer.ui.base.AlertDialogCallback;

/**
 * Created by firoz on 27/7/17.
 */

public class AlertDialogHelper {

    private Activity activity;

    public AlertDialogHelper(Activity activity) {
        this.activity = activity;
    }

    public void displayAlertDialog(String message, String title, String positiveText, String negativeText, final AlertDialogCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)
                .setTitle(title);
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (callback != null)
                    callback.alertDialogPositiveButtonClicked(null);

                // Dismissing Dialog
                dialog.dismiss();
            }
        });

        if (negativeText != null) {
            builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (callback != null)
                        callback.alertDialogNegativeButtonClicked(null);

                    // Dismissing Dialog
                    dialog.dismiss();
                }
            });
        }
        builder.show();
    }


    /**
     * Displaying Alert Dialog with EditText in it
     *
     * @param title used to display alertDialog title
     * @param hint used to display hint inside EditText
     * @param defaultText used to display inside EditText
     * @param callback
     */
    public void displayAlertDialogWithEdittext(String title,
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
                        callback.alertDialogNegativeButtonClicked("Invalid Text Entered");
                }
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (callback != null)
                    callback.alertDialogNegativeButtonClicked(null);

                // Do nothing
                dialog.dismiss();
            }
        });

        alertDialogBuilder.show();
    }
}

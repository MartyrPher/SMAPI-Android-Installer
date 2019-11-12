package com.MartyrPher.smapiandroidinstaller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * The dialog that is shown in different situations
 */
public class DialogFrag extends DialogFragment {

    //The alert dialog
    public static AlertDialog mAlertDialog;

    //Progress bar that is used... to show progress.
    private static ProgressBar mProgressBar;

    //Text view used to show the install message
    private static TextView mTextView;

    /**
     * Empty Constructor
     * Do I even need this?
     */
    public DialogFrag()
    {
    }

    /**
     * Shows the required dialog
     * @param context = The activities context
     * @param message = The message to show
     * @param tag = Which dialog to show (Install or Can't find the app)
     */
    public static void showDialog(Context context, int message, int tag)
    {
        //Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //Two different cases to show two different dialogs
        switch(tag)
        {
            case 0:
                //Dialog for showing the install progress
                //Create the layout inflater and inflate it
                LayoutInflater mLayoutInflater = LayoutInflater.from(context);
                View dialogView = mLayoutInflater.inflate(R.layout.progress_bar_dialog, null);

                //Set the view, message and is cancelable
                builder.setView(dialogView);
                builder.setMessage(message);
                builder.setCancelable(false);

                //Create the dialog and show it
                mAlertDialog = builder.create();
                mAlertDialog.show();

                //Set the progress bar and install text
                mProgressBar = dialogView.findViewById(R.id.progressBar);
                mTextView = dialogView.findViewById(R.id.install_message);
                break;
            case 1:
                //Dialog for not finding the app on the device
                builder.setMessage(message);
                builder.setPositiveButton("Okay", null);
                builder.show();
                break;
        }
    }

    /**
     * Updates the progress bar
     * @param progress = The progress measured in an int
     * @param message = The fun little message to show
     */
    public static void updateProgressBar(int progress, int message)
    {
        mProgressBar.setProgress(progress);
        mTextView.setText(message);
    }

    /**
     * Dismisses the dialog
     */
    public static void dismissDialog()
    {
        if (mAlertDialog.isShowing())
            mAlertDialog.dismiss();
    }

    /**
     * This is never used. Not sure why it's still here
     * @param context = The activities context
     * @param message = The message to show
     */
    public static void dismissDialogString(Context context, String message)
    {
        if(mAlertDialog.isShowing())
        {
            mAlertDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(message);
        builder.setPositiveButton("Oh No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

}

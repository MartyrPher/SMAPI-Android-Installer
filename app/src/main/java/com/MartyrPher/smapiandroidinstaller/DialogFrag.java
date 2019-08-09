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

public class DialogFrag extends DialogFragment {

    public static AlertDialog mAlertDialog;

    private static ProgressBar mProgressBar;
    private static TextView mTextView;

    public DialogFrag()
    {
    }

    public static void showDialog(Context context, int message, int tag)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        switch(tag)
        {
            case 0:
                LayoutInflater mLayoutInflater = LayoutInflater.from(context);
                View dialogView = mLayoutInflater.inflate(R.layout.progress_bar_dialog, null);
                builder.setView(dialogView);
                builder.setMessage(message);
                builder.setCancelable(false);
                mAlertDialog = builder.create();
                mAlertDialog.show();
                mProgressBar = dialogView.findViewById(R.id.progressBar);
                mTextView = dialogView.findViewById(R.id.install_message);
                break;
            case 1:
                builder.setMessage(message);
                builder.setPositiveButton("Okay", null);
                builder.show();
                break;
        }
    }

    public static void updateProgressBar(int progress, int message)
    {
        mProgressBar.setProgress(progress);
        mTextView.setText(message);
    }

    public static void dismissDialog()
    {
        if (mAlertDialog.isShowing())
            mAlertDialog.dismiss();
    }

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
        builder.show();
    }

}

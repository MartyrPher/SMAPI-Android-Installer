package com.MartyrPher.smapiandroidinstaller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class DialogFrag extends DialogFragment {

    public static AlertDialog mAlertDialog;

    public DialogFrag()
    {
    }

    public static void showDialog(Context context, int message, int tag)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        switch(tag)
        {
            case 0:
                builder.setMessage(message);
                builder.setCancelable(false);
                mAlertDialog = builder.create();
                mAlertDialog.show();
                break;
            case 1:
                builder.setMessage(message);
                builder.setPositiveButton("Okay", null);
                builder.show();
                break;
        }
    }

    public static void dismissDialog(Context context, int message)
    {
        if(mAlertDialog.isShowing())
        {
            mAlertDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(message);
        builder.setPositiveButton("Awesome", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}

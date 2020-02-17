package com.MartyrPher.smapiandroidinstaller;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.core.content.FileProvider;
import java.io.File;

/**
 * Allows the new app to be installed
 */
public class ApkInstall {

    //The MainActivity context
    private static Context context;

    /**
     * Constructor that sets the context
     * @param appContext = The activites context
     */
    public ApkInstall(Context appContext)
    {
        this.context = appContext;
    }

    /**
     * Installs the new app by starting a new intent
     */
    public void installNewStardew()
    {
        //The path of the lastly generated APK
        String newApkPath = Environment.getExternalStorageDirectory() + "/SMAPI Installer/base_signed.apk";

        //Create a new intent with ACTION_VIEW
        Intent intent = new Intent(Intent.ACTION_VIEW);

        //Set the data and type and add flags
        intent.setDataAndType(fromFile(new File(newApkPath)), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try
        {
            //Start the install Activity
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e)
        {
            //Show a dialog with the error
            DialogFrag.showDialog(context, R.string.install, 1);
        }
    }

    /**
     * Gets the URI from a file
     * @param file = The file to try and get the URI from
     * @return The URI for the file
     */
    private Uri fromFile(File file)
    {
        //Android versions greater than Nougat use FileProvider, others use the URI.fromFile.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
        else
            return Uri.fromFile(file);
    }

}

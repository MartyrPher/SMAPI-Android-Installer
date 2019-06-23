package com.MartyrPher.smapiandroidinstaller;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;

public class ApkInstall {

    private static Context context;

    public ApkInstall(Context appContext)
    {
        this.context = appContext;
    }

    public void installNewStardew()
    {
        String newApkPath = Environment.getExternalStorageDirectory() + "/SMAPI Installer/base_signed.apk";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fromFile(new File(newApkPath)), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try
        {
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e)
        {
            DialogFrag.showDialog(context, R.string.install, 1);
        }
    }

    private Uri fromFile(File file)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
        else
            return Uri.fromFile(file);
    }

}

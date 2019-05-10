package com.MartyrPher.smapiandroidinstaller;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

public class BackgroundTask extends AsyncTask<Void, Integer, Boolean> {

    private static final String TAG = "BackgroundTask";
    private static final String DIR_APK_FILES = "/SMAPI Installer/ApkFiles/";
    private Context contextActivity;

    public BackgroundTask(Context context)
    {
        this.contextActivity = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DialogFrag.showDialog(contextActivity, R.string.modify_apk, 0);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        WriteApk writeApk = new WriteApk();
        SignApk signApk = new SignApk();

        try
        {
            File[] moddingAPI = {new File(Environment.getExternalStorageDirectory() + DIR_APK_FILES + "StardewModdingAPI.dll")};
            publishProgress(1);
            writeApk.addFilesToApk(new File(Environment.getExternalStorageDirectory() + "/SMAPI Installer/base.apk"), moddingAPI, "assemblies/", false, 0);
            File[] resources = {new File(Environment.getExternalStorageDirectory() + DIR_APK_FILES + "AndroidManifest.xml"),
                    new File(Environment.getExternalStorageDirectory() + DIR_APK_FILES + "classes.dex")};
            writeApk.addFilesToApk(new File(Environment.getExternalStorageDirectory() + "/SMAPI Installer/base.apk_patched0.apk"), resources, "", true, 1);
            signApk.commitSignApk();
            return true;
        }
        catch(Exception e)
        {
            return false;
        }

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean)
        {
            DialogFrag.dismissDialog(contextActivity, R.string.finished);
        }
        else
        {
            Toast.makeText(contextActivity, "Something went wrong!", Toast.LENGTH_LONG).show();
        }
    }
}

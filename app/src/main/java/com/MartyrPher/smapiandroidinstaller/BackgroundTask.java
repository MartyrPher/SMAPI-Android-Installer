package com.MartyrPher.smapiandroidinstaller;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;

public class BackgroundTask extends AsyncTask<Void, Integer, Boolean> {

    private static final String TAG = "BackgroundTask";
    private static final String ASSET_APK_FILES = "SMAPI";
    private static final String ASSET_STARDEW_FILES = "Stardew";
    private static final String MOD_FILES_VK = "VirtualKeyboard";
    private static final String MOD_FILES_VK_ASSET = "VirtualKeyboard/assets";
    private static final String DIR_APK_FILES = "/SMAPI Installer/ApkFiles/";
    private static final String DIR_STARDEW_FILES = "/StardewValley/smapi-internal/";
    private static final String DIR_MODS_VK = "/StardewValley/Mods/VirtualKeyboard/";
    private static final String DIR_MODS_VK_ASSET = "/StardewValley/Mods/VirtualKeyboard/assets";

    private static final String MOD_DIR = Environment.getExternalStorageDirectory() + "/StardewValley/Mods/";
    private final Context contextActivity;

    private ApkInstall apkInstall;

    public BackgroundTask(Context context)
    {
        this.contextActivity = context;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        DialogFrag.showDialog(contextActivity, R.string.modify_apk, 0);
    }

    @Override
    protected Boolean doInBackground(Void... voids)
    {
        CopyAssets copy = new CopyAssets(contextActivity);
        WriteApk writeApk = new WriteApk();
        SignApk signApk = new SignApk();
        apkInstall = new ApkInstall(contextActivity);

        try
        {
            File modDir = new File(MOD_DIR);
            if (!modDir.exists())
            {
                modDir.mkdir();
            }

            copy.copyAssets(ASSET_APK_FILES, DIR_APK_FILES);
            copy.copyAssets(ASSET_STARDEW_FILES, DIR_STARDEW_FILES);
            copy.copyAssets(MOD_FILES_VK, DIR_MODS_VK);
            copy.copyAssets(MOD_FILES_VK_ASSET, DIR_MODS_VK_ASSET);

            File[] moddingAPI = {new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES + "StardewModdingAPI.dll"),
                                new File(Environment.getExternalStorageDirectory() + DIR_APK_FILES + "StardewModdingAPI.Toolkit.CoreInterfaces.dll"),
                                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES + "StardewModdingAPI.Toolkit.dll"),
                                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES + "Newtonsoft.json.dll"),
                                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES + "System.Data.dll"),
                                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES + "System.Numerics.dll")};
            publishProgress(1);
            writeApk.addFilesToApk(new File(Environment.getExternalStorageDirectory() + "/SMAPI Installer/base.apk"), moddingAPI, "assemblies/", false, 0);
            File[] resources = {new File(Environment.getExternalStorageDirectory() + DIR_APK_FILES + "AndroidManifest.xml"),
                    new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES + "classes.dex")};
            writeApk.addFilesToApk(new File(Environment.getExternalStorageDirectory() + "/SMAPI Installer/base.apk_patched0.apk"), resources, "", true, 1);
            signApk.commitSignApk();
            File filesToDelete = new File(Environment.getExternalStorageDirectory() + DIR_APK_FILES);
            File deleteOldApk = new File(Environment.getExternalStorageDirectory() + "/SMAPI Installer/base.apk_patched0.apk_patched1.apk");
            deleteOldApk.delete();
            if (filesToDelete.isDirectory())
            {
                String[] child = filesToDelete.list();
                for (int i = 0; i < child.length; i++)
                {
                    new File(filesToDelete, child[i]).delete();
                }
                filesToDelete.delete();
            }
            return true;
        }
        catch(Exception e)
        {
            DialogFrag.dismissDialogString(contextActivity, e.getMessage());
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
            DialogFrag.dismissDialog(contextActivity);
            apkInstall.installNewStardew();
        }
    }
}

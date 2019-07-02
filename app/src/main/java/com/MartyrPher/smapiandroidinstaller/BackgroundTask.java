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
    private static final String STARDEW_VALLEY_DIR = Environment.getExternalStorageDirectory() + "/StardewValley/";

    private final Context contextActivity;

    private ApkInstall apkInstall;
    private ApkExtractor extractor;
    private boolean updating;

    public BackgroundTask(Context context, ApkExtractor apkExtractor, boolean update)
    {
        this.contextActivity = context;
        this.extractor = apkExtractor;
        this.updating = update;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        int dialogString;
        if (updating)
            dialogString = R.string.update_apk;
        else
            dialogString = R.string.modify_apk;

        DialogFrag.showDialog(contextActivity, dialogString, 0);
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
            extractor.extractAPK();
            publishProgress(9);

            File saveFolder = new File(STARDEW_VALLEY_DIR);
            if (!saveFolder.exists())
                saveFolder.mkdir();


            File modDir = new File(MOD_DIR);
            if (!modDir.exists())
                modDir.mkdir();

            copy.copyAssets(ASSET_APK_FILES, DIR_APK_FILES);
            publishProgress(18);

            copy.copyAssets(ASSET_STARDEW_FILES, DIR_STARDEW_FILES);
            publishProgress(27);

            copy.copyAssets(MOD_FILES_VK, DIR_MODS_VK);
            publishProgress(36);

            copy.copyAssets(MOD_FILES_VK_ASSET, DIR_MODS_VK_ASSET);
            publishProgress(45);

            File[] moddingAPI = {new File(Environment.getExternalStorageDirectory() + DIR_APK_FILES + "AndroidManifest.xml"),
                                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES + "classes.dex"),
                                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES + "StardewModdingAPI.dll"),
                                new File(Environment.getExternalStorageDirectory() + DIR_APK_FILES + "StardewModdingAPI.Toolkit.CoreInterfaces.dll"),
                                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES + "StardewModdingAPI.Toolkit.dll"),
                                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES + "Newtonsoft.json.dll"),
                                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES + "System.Data.dll"),
                                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES + "System.Numerics.dll")};
            boolean[] compressed = {true, true, false, false, false, false, false, false};
            String[] paths = {"", "", "assemblies/", "assemblies/", "assemblies/", "assemblies/", "assemblies/", "assemblies/"};
            publishProgress(54);

            writeApk.addFilesToApk(new File(Environment.getExternalStorageDirectory() + "/SMAPI Installer/base.apk"), moddingAPI, paths, compressed);
            publishProgress(63);

            signApk.commitSignApk();
            publishProgress(81);

            File filesToDelete = new File(Environment.getExternalStorageDirectory() + DIR_APK_FILES);
            File deleteOldApk = new File(Environment.getExternalStorageDirectory() + "/SMAPI Installer/base.apk_patched.apk");
            deleteOldApk.delete();
            publishProgress(90);

            if (filesToDelete.isDirectory())
            {
                String[] child = filesToDelete.list();
                for (int i = 0; i < child.length; i++)
                {
                    new File(filesToDelete, child[i]).delete();
                }
                filesToDelete.delete();
            }
            publishProgress(100);
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
        int message;
        switch(values[0])
        {
            case 9:
                message = R.string.install_abigail_rocks;
                break;
            case 18:
                message = R.string.install_rock_crab;
                break;
            case 27:
                message = R.string.install_squid_ocean;
                break;
            case 34:
                message = R.string.install_dino_egg;
                break;
            case 54:
                message = R.string.installing_pufferchicks;
                break;
            case 63:
                message = R.string.install_milk_cows;
                break;
            case 81:
                message = R.string.install_feeding_juminos;
                break;
            default:
                message = R.string.blank;
                break;
        }
        DialogFrag.updateProgressBar(values[0], message);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean)
        {
            DialogFrag.dismissDialog();
            apkInstall.installNewStardew();
        }
    }
}

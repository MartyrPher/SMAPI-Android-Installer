package com.MartyrPher.smapiandroidinstaller;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class BackgroundTask extends AsyncTask<Void, Integer, Boolean> {

    private static final String TAG = "BackgroundTask";
    private static final String ASSET_APK_FILES = "SMAPI";
    private static final String ASSET_STARDEW_FILES = "Stardew";
    private static final String MOD_FILES_VK = "VirtualKeyboard";
    private static final String MOD_FILES_VK_ASSET = "VirtualKeyboard/assets";
    private static final String MOD_FILES_CC = "ConsoleCommands";
    private static final String MIPMAP_XXXHDPI_ASSET = "mipmap-xxxhdpi-v4";
    private static final String MIPMAP_XXHDPI_ASSET = "mipmap-xxhdpi-v4";
    private static final String MIPMAP_XHDPI_ASSET = "mipmap-xhdpi-v4";
    private static final String MIPMAP_MDPI_ASSET = "mipmap-mdpi";
    private static final String MIPMAP_HDPI_ASSET = "mipmap-hdpi";
    private static final String DIR_APK_FILES = "/SMAPI Installer/ApkFiles/";
    private static final String DIR_APK_FILES_XXXHDPI = "/SMAPI Installer/ApkFiles/mipmap-xxxhdpi-v4/";
    private static final String DIR_APK_FILES_XXHDPI = "/SMAPI Installer/ApkFiles/mipmap-xxhdpi-v4/";
    private static final String DIR_APK_FILES_XHDPI = "/SMAPI Installer/ApkFiles/mipmap-xhdpi-v4/";
    private static final String DIR_APK_FILES_MDPI = "/SMAPI Installer/ApkFiles/mipmap-mdpi/";
    private static final String DIR_APK_FILES_HDPI = "/SMAPI Installer/ApkFiles/mipmap-hdpi/";
    private static final String DIR_STARDEW_FILES = "/StardewValley/smapi-internal/";
    private static final String DIR_MODS_VK = "/StardewValley/Mods/VirtualKeyboard/";
    private static final String DIR_MODS_VK_ASSET = "/StardewValley/Mods/VirtualKeyboard/assets";
    private static final String DIR_MODS_CC = "/StardewValley/Mods/Console Commands/";

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
            publishProgress(9);
            extractor.extractAPK();

            //Creates a save folder if one doesn't exist already
            File saveFolder = new File(STARDEW_VALLEY_DIR);
            if (!saveFolder.exists())
                saveFolder.mkdir();

            //Creates the mod directory if one doesn't exist already
            File modDir = new File(MOD_DIR);
            if (!modDir.exists())
                modDir.mkdir();

            //Creates the .nomedia file if it doesn't exist already
            File noMedia = new File(STARDEW_VALLEY_DIR + ".nomedia");
            if (!noMedia.exists())
                noMedia.createNewFile();

            copy.copyAssets(ASSET_APK_FILES, DIR_APK_FILES);
            publishProgress(18);

            copy.copyAssets(ASSET_STARDEW_FILES, DIR_STARDEW_FILES);
            publishProgress(27);

            copy.copyAssets(MOD_FILES_VK, DIR_MODS_VK);
            publishProgress(36);

            copy.copyAssets(MOD_FILES_VK_ASSET, DIR_MODS_VK_ASSET);
            publishProgress(45);

            copy.copyAssets(MOD_FILES_CC, DIR_MODS_CC);

            copy.copyAssets(MIPMAP_XXXHDPI_ASSET, DIR_APK_FILES_XXXHDPI);
            copy.copyAssets(MIPMAP_XXHDPI_ASSET, DIR_APK_FILES_XXHDPI);
            copy.copyAssets(MIPMAP_XHDPI_ASSET, DIR_APK_FILES_XHDPI);
            copy.copyAssets(MIPMAP_MDPI_ASSET, DIR_APK_FILES_MDPI);
            copy.copyAssets(MIPMAP_HDPI_ASSET, DIR_APK_FILES_HDPI);

            File[] moddingAPI =
            {
                new File(Environment.getExternalStorageDirectory() + DIR_APK_FILES + "AndroidManifest.xml"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES + "classes.dex"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES + "StardewModdingAPI.dll"),
                new File(Environment.getExternalStorageDirectory() + DIR_APK_FILES + "StardewModdingAPI.Toolkit.CoreInterfaces.dll"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES + "StardewModdingAPI.Toolkit.dll"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES + "Newtonsoft.json.dll"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES + "System.Data.dll"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES + "System.Numerics.dll"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES_XXXHDPI + "ic_launcher.png"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES_XXXHDPI + "ic_launcher_background.png"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES_XXXHDPI + "ic_launcher_foreground.png"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES_XXXHDPI + "ic_launcher_round.png"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES_XXHDPI + "ic_launcher.png"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES_XXHDPI + "ic_launcher_background.png"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES_XXHDPI + "ic_launcher_foreground.png"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES_XXHDPI + "ic_launcher_round.png"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES_XHDPI + "ic_launcher.png"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES_XHDPI + "ic_launcher_background.png"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES_XHDPI + "ic_launcher_foreground.png"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES_XHDPI + "ic_launcher_round.png"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES_MDPI + "ic_launcher.png"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES_MDPI + "ic_launcher_background.png"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES_MDPI + "ic_launcher_foreground.png"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES_MDPI + "ic_launcher_round.png"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES_HDPI + "ic_launcher.png"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES_HDPI + "ic_launcher_background.png"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES_HDPI + "ic_launcher_foreground.png"),
                new File( Environment.getExternalStorageDirectory() + DIR_APK_FILES_HDPI + "ic_launcher_round.png")
            };

            boolean[] compressed =
            {
                true,
                true,
                false,
                false,
                false,
                false,
                false,
                false,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true
            };

            String[] paths =
            {
                "",
                "",
                "assemblies/",
                "assemblies/",
                "assemblies/",
                "assemblies/",
                "assemblies/",
                "assemblies/",
                "res/mipmap-xxxhdpi-v4/",
                "res/mipmap-xxxhdpi-v4/",
                "res/mipmap-xxxhdpi-v4/",
                "res/mipmap-xxxhdpi-v4/",
                "res/mipmap-xxhdpi-v4/",
                "res/mipmap-xxhdpi-v4/",
                "res/mipmap-xxhdpi-v4/",
                "res/mipmap-xxhdpi-v4/",
                "res/mipmap-xhdpi-v4/",
                "res/mipmap-xhdpi-v4/",
                "res/mipmap-xhdpi-v4/",
                "res/mipmap-xhdpi-v4/",
                "res/mipmap-mdpi/",
                "res/mipmap-mdpi/",
                "res/mipmap-mdpi/",
                "res/mipmap-mdpi/",
                "res/mipmap-hdpi/",
                "res/mipmap-hdpi/",
                "res/mipmap-hdpi/",
                "res/mipmap-hdpi/"
            };
            publishProgress(54);

            writeApk.addFilesToApk(new File(Environment.getExternalStorageDirectory() + "/SMAPI Installer/" + ApkExtractor.sourceApkFilename), moddingAPI, paths, compressed);
            publishProgress(63);

            signApk.commitSignApk();
            publishProgress(81);


            File deleteOldApk = new File(Environment.getExternalStorageDirectory() + "/SMAPI Installer/" + ApkExtractor.sourceApkFilename + "_patched.apk");
            deleteOldApk.delete();
            publishProgress(90);

            deleteApkFiles();
            publishProgress(100);
            return true;
        }
        catch(Exception e)
        {
            Log.e(TAG, e.getMessage(), e);
            DialogFrag.dismissDialog();
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

    //This is super wack but I'm on a time crunch and can probably do this in one nested FOR loop...
    private void deleteApkFiles()
    {
        File filesToDelete = new File(Environment.getExternalStorageDirectory() + DIR_APK_FILES);
        File mdpiToDelete = new File(Environment.getExternalStorageDirectory() + DIR_APK_FILES_MDPI);
        File hdpiToDelete = new File(Environment.getExternalStorageDirectory() + DIR_APK_FILES_HDPI);
        File xhdpiToDelete = new File(Environment.getExternalStorageDirectory() + DIR_APK_FILES_XHDPI);
        File xxhdpiToDelete = new File(Environment.getExternalStorageDirectory() + DIR_APK_FILES_XXHDPI);
        File xxxhdpiToDelete = new File(Environment.getExternalStorageDirectory() + DIR_APK_FILES_XXXHDPI);

        if (filesToDelete.isDirectory())
        {
            String[] child = filesToDelete.list();
            for (int i = 0; i < child.length; i++)
            {
                new File(filesToDelete, child[i]).delete();
            }
        }

        if (mdpiToDelete.isDirectory())
        {
            String[] child = mdpiToDelete.list();
            for (int i = 0; i < child.length; i++)
            {
                new File(mdpiToDelete, child[i]).delete();
            }
            mdpiToDelete.delete();
        }

        if (hdpiToDelete.isDirectory())
        {
            String[] child = hdpiToDelete.list();
            for (int i = 0; i < child.length; i++)
            {
                new File(hdpiToDelete, child[i]).delete();
            }
            hdpiToDelete.delete();
        }

        if (xhdpiToDelete.isDirectory())
        {
            String[] child = xhdpiToDelete.list();
            for (int i = 0; i < child.length; i++)
            {
                new File(xhdpiToDelete, child[i]).delete();
            }
            xhdpiToDelete.delete();
        }

        if (xxhdpiToDelete.isDirectory())
        {
            String[] child = xxhdpiToDelete.list();
            for (int i = 0; i < child.length; i++)
            {
                new File(xxhdpiToDelete, child[i]).delete();
            }
            xxhdpiToDelete.delete();
        }

        if (xxxhdpiToDelete.isDirectory())
        {
            String[] child = xxxhdpiToDelete.list();
            for (int i = 0; i < child.length; i++)
            {
                new File(xxxhdpiToDelete, child[i]).delete();
            }
            xxxhdpiToDelete.delete();
        }

        filesToDelete.delete();
    }
}

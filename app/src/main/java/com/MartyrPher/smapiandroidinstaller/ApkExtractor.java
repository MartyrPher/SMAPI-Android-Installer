package com.MartyrPher.smapiandroidinstaller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

/**
 * Handles extracting the APK to a directory that it can worth with
 */
public class ApkExtractor{

    //TAG used for debug purposes
    private static final String TAG = "ApkExtractor";

    //The different package names for the apps
    private static final String[] PACKAGE_NAMES = {"com.martyrpher.stardewvalley", "com.chucklefish.stardewvalley", "com.chucklefish.stardewvalleysamsung"};

    //Context of the MainActivity
    private final Context context;

    //Used to get application info including the directory where the apk is
    private ApplicationInfo mApplicationInfo;

    //Used to get the package info
    private PackageManager mPackageManager;

    //Info needed to get the application info
    private PackageInfo mPackageInfo;

    //static string for the source name since it can vary from device (EX: base.apk)
    public static String sourceApkFilename;

    //Whether it found the apk on the device
    private boolean[] foundApk = {false, false, false};

    /**
    * APK Constructor that sets the Context
    * @param appContext = The activities context
    */
    public ApkExtractor(Context appContext)
    {
        this.context = appContext;
    }

    /**
    * Checks to see if the apk is on the device
    * @return True if the apk is found on the device
    */
    public boolean[] checkForInstallOrUpgrade()
    {
        //grab the package manager
        mPackageManager = context.getPackageManager();

        //try to find the 3 different apps on the device
        for (int i = 0; i < foundApk.length; i++)
        {
            try
            {
                //get the application info from the package names found in PACKAGE_NAMES
                mPackageInfo =  mPackageManager.getPackageInfo(PACKAGE_NAMES[i], 0);
                mApplicationInfo = getApplicationInfoFrom(mPackageManager, mPackageInfo);

                //Check if the app exists in the public source directory
                File file = new File(mApplicationInfo.publicSourceDir);
                if (file.exists())
                {
                    foundApk[i] = true;
                }
            }
            catch (PackageManager.NameNotFoundException e)
            {
                Log.e(TAG, e.getMessage());
            }
        }
        return foundApk;
    }

    /**
    * Extracts the APK to a local directory where it can access the files
    */
    public boolean extractAPK() {
        try
        {
            File apkFile = new File(mApplicationInfo.publicSourceDir);
            Log.e(TAG, "APK path: " + mApplicationInfo.publicSourceDir);

            //Set the destination file to the SMAPI Installer folder
            File dest = new File(Environment.getExternalStorageDirectory() + "/SMAPI Installer/");
            if (apkFile.exists()) {
                try {
                    if(!dest.exists())
                    {
                        if(dest.mkdir());//directory is created;
                    }
                    sourceApkFilename = apkFile.getName();
                    copy(apkFile.getAbsoluteFile(), new File(dest, apkFile.getName()));
                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getLocalizedMessage());
            return false;
        }
        return true;
    }

    /**
     * Copies the apk to the destination
     * @param src = The source file
     * @param dst = The destination file
     */
    private void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    /**
     * Grabs the application info from the package manager
     * @param packageManager = The package manager
     * @param packageInfo = The package information
     * @return The application information
     */
    //Grabs the application info from the package manager
    private ApplicationInfo getApplicationInfoFrom(PackageManager packageManager, PackageInfo packageInfo) {
        return packageInfo.applicationInfo;
    }

}

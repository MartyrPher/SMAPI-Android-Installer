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

public class ApkExtractor{

    private static final String TAG = "ApkExtractor";
    private static final String[] PACKAGE_NAMES = {"com.martyrpher.stardewvalley", "com.chucklefish.stardewvalley", "com.chucklefish.stardewvalleysamsung"};

    private final Context context;

    private ApplicationInfo mApplicationInfo;
    private PackageManager mPackageManager;
    private PackageInfo mPackageInfo;

    public static String sourceApkFilename;

    private boolean[] foundApk = {false, false, false};

    public ApkExtractor(Context appContext)
    {
        this.context = appContext;
    }

    public boolean[] checkForInstallOrUpgrade()
    {
        mPackageManager = context.getPackageManager();

        for (int i = 0; i < foundApk.length; i++)
        {
            try
            {
                mPackageInfo =  mPackageManager.getPackageInfo(PACKAGE_NAMES[i], 0);
                mApplicationInfo = getApplicationInfoFrom(mPackageManager, mPackageInfo);

                File file = new File(mApplicationInfo.publicSourceDir);
                if (file.exists())
                {
                    foundApk[i] = true;
                }
            }
            catch (PackageManager.NameNotFoundException e)
            {
                //Do NOTHING
            }
        }
        return foundApk;
    }

    //Extracts the APK to a local directory where it can access the files
    public boolean extractAPK() {
        try
        {
            File apkFile = new File(mApplicationInfo.publicSourceDir);
            Log.e(TAG, "APK path: " + mApplicationInfo.publicSourceDir);
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

    private ApplicationInfo getApplicationInfoFrom(PackageManager packageManager, PackageInfo packageInfo) {
        return packageInfo.applicationInfo;
    }

}

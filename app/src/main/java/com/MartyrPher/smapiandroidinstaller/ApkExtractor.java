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
import android.widget.Toast;

public class ApkExtractor{

    private static final String PACKAGE_NAME = "com.chucklefish.stardewvalley";
    private static final String TAG = "ApkExtractor";

    //Blank Constructor
    public ApkExtractor()
    {
    }

    //Extracts the APK to a local directory where it can access the files
    public void ExtractAPK(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(PACKAGE_NAME, 0);

            ApplicationInfo applicationInfo;
            applicationInfo = getApplicationInfoFrom(packageManager, packageInfo);

            File apkFile = new File(applicationInfo.publicSourceDir);
            File dest = new File(Environment.getExternalStorageDirectory() + "/SMAPI Installer/");
            if (apkFile.exists()) {
                try {
                    if(!dest.exists())
                    {
                        if(dest.mkdir());//directory is created;
                    }
                    copy(apkFile.getAbsoluteFile(), new File(dest, apkFile.getName()));
                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }
        } catch (PackageManager.NameNotFoundException ex) {
            Log.e(TAG, ex.getLocalizedMessage());
        }
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

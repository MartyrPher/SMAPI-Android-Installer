package com.MartyrPher.smapiandroidinstaller;

import android.os.Environment;
import android.util.Log;

public class SignApk {

    private static final String TAG = "SignApk";
    private static final String APK_LOCATION = Environment.getExternalStorageDirectory() + "/SMAPI Installer/base.apk_patched.apk";
    private static final String KEYSTORE_LOCATION = Environment.getExternalStorageDirectory() + "/SMAPI Installer/debug.keystore";


    public SignApk()
    {
    }

    public void CommitSignApk()
    {

    }
}

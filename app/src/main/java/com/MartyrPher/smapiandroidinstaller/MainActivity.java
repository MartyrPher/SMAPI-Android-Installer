package com.MartyrPher.smapiandroidinstaller;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private static final String ASSET_APK_FILES = "SMAPI/";
    private static final String ASSET_STARDEW_FILES = "Stardew/";
    private static final String DIR_APK_FILES = "/SMAPI Installer/ApkFiles/";
    private static final String DIR_STARDEW_FILES = "/StardewValley/smapi-internal/";
    private static final String PATH_TO_SIGNED_APK = Environment.getExternalStorageDirectory() + "/SMAPI Installer/base_signed.apk";
    private static final String TAG = "MainActivity";

    private static final int UNINSTALL_REQUEST_CODE = 0;
    private static final int INSTALL_REQUEST_CODE = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button start_button = findViewById(R.id.start_button);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ApkExtractor apkExtractor = new ApkExtractor();
                WriteApk writeApk = new WriteApk();
                SignApk signApk = new SignApk();

                apkExtractor.ExtractAPK(getApplicationContext());
                try
                {
                    copyAssets(ASSET_APK_FILES, DIR_APK_FILES);
                    copyAssets(ASSET_STARDEW_FILES, DIR_STARDEW_FILES);
                    File[] moddingAPI = {new File(Environment.getExternalStorageDirectory() + DIR_APK_FILES + "StardewModdingAPI.dll")};

                    writeApk.AddFilesToApk(new File(Environment.getExternalStorageDirectory() + "/SMAPI Installer/base.apk"), moddingAPI, "assemblies/", false, 0);
                    File[] resources = {new File(Environment.getExternalStorageDirectory() + DIR_APK_FILES + "AndroidManifest.xml"),
                                        new File(Environment.getExternalStorageDirectory() + DIR_APK_FILES + "classes.dex")};
                    writeApk.AddFilesToApk(new File(Environment.getExternalStorageDirectory() + "/SMAPI Installer/base.apk_patched0.apk"), resources, "", true, 1);
                    signApk.CommitSignApk();

                    UninstallStardew();
                    Toast.makeText(getApplicationContext(), "Done :)", Toast.LENGTH_SHORT).show();

                }catch (Exception ex)
                {

                }
            }
        });
    }

    public void UninstallStardew()
    {
        String app_pkg_name = "com.chucklefish.stardewvalley";

        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
        intent.setData(Uri.parse("package:" + app_pkg_name));
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        startActivityForResult(intent, UNINSTALL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case UNINSTALL_REQUEST_CODE:
                if (requestCode == RESULT_OK)
                {
                    Log.d(TAG, "The User Accepted the Uninstall");
                } else if (resultCode == RESULT_CANCELED)
                {
                    Log.d(TAG, "The User Cancelled the Uninstall");
                } else if (resultCode == RESULT_FIRST_USER)
                {
                    Log.d(TAG, "Failed to Uninstall");
                }
                break;
            case INSTALL_REQUEST_CODE:
                if (requestCode == RESULT_OK)
                {
                    Log.d(TAG, "The User Accepted the Install");
                } else if (resultCode == RESULT_CANCELED)
                {
                    Log.d(TAG, "The User Cancelled the Install");
                } else if (resultCode == RESULT_FIRST_USER)
                {
                    Log.d(TAG, "Failed to Install");
                }
                break;
        }
    }

    private void copyAssets(String asset, String dir) {
        AssetManager assetManager = getAssets();
        String[] files = null;
        File dest = new File(Environment.getExternalStorageDirectory() + dir);
        if(!dest.exists())
        {
            dest.mkdir();//directory is created;
        }
        try {
            files = assetManager.list(asset);
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(asset + filename);
                File outFile = new File(dest, filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}
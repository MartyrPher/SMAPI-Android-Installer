package com.MartyrPher.smapiandroidinstaller;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button start_button = findViewById(R.id.start_button);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Making Copy of APK :)", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(getApplicationContext(), "Done :)", Toast.LENGTH_SHORT).show();

                }catch (Exception ex)
                {

                }
            }
        });
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
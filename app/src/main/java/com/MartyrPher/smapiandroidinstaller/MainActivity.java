package com.MartyrPher.smapiandroidinstaller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
    private static final String TAG = "MainActivity";

    private static final int UNINSTALL_REQUEST_CODE = 0;
    private static final int MY_PERMISSION_REQUEST_STORAGE = 2;

    private boolean hasPermissions = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();

        final Button start_button = findViewById(R.id.start_button);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermissions)
                {
                    boolean foundGame;
                    ApkExtractor apkExtractor = new ApkExtractor(MainActivity.this);
                    BackgroundTask backgroundTask = new BackgroundTask(MainActivity.this);

                    foundGame = apkExtractor.extractAPK(getApplicationContext());

                    if(foundGame)
                    {
                        copyAssets(ASSET_APK_FILES, DIR_APK_FILES);
                        copyAssets(ASSET_STARDEW_FILES, DIR_STARDEW_FILES);

                        backgroundTask.execute();
                    }
                    else
                    {
                        DialogFrag.showDialog(MainActivity.this, R.string.cant_find, 1);
                    }
                }
                else
                {
                    requestPermissions();
                }
            }
        });
    }

    public void requestPermissions()
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSION_REQUEST_STORAGE);
        }
        else
        {
            hasPermissions = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    hasPermissions = true;
                    Toast.makeText(this, "Permission Granted :)", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //Prompts the user to Uninstall the current version of the game from the device
    public void uninstallStardew()
    {
        String app_pkg_name = "com.chucklefish.stardewvalley";

        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
        intent.setData(Uri.parse("package:" + app_pkg_name));
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        startActivityForResult(intent, UNINSTALL_REQUEST_CODE);
    }

    //Copies the needed files from the APK to a local directory so they can be used
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
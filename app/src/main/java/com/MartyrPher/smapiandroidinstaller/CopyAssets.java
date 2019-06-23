package com.MartyrPher.smapiandroidinstaller;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CopyAssets {

    private final static String TAG = "CopyAssets";

    private final Context context;

    public CopyAssets(Context appContext)
    {
        this.context = appContext;
    }

    //Copies the needed files from the APK to a local directory so they can be used
    public void copyAssets(String asset, String dir) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        File dest = new File(Environment.getExternalStorageDirectory() + dir);
        if(!dest.exists())
        {
            dest.mkdir();//directory is created;
        }
        try {
            files = assetManager.list(asset);
        } catch (IOException e) {
            Log.e(TAG, "Failed to get asset file list.", e);
        }
        Log.e(TAG, "File Length: " + files.length);
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(asset + "/" + filename);
                File outFile = new File(dest, filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch(IOException e) {
                Log.e(TAG, "Failed to copy asset file: " + filename, e);
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

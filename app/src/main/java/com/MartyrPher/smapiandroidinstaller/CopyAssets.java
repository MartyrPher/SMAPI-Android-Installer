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

/**
 * Allows assets from the apk to be copied to a local folder to be worked with
 */
public class CopyAssets {

    //TAG for debugging purposes
    private final static String TAG = "CopyAssets";

    //MainActivity context
    private final Context context;

    /**
     * Constructor that sets the context
     * @param appContext = The activities context
     */
    public CopyAssets(Context appContext)
    {
        this.context = appContext;
    }

    /**
     * Copies the needed files from the APK to a local directory so they can be used
     * @param asset = The asset to copy
     * @param dir = The directory to copy the asset to
     */
    public void copyAssets(String asset, String dir) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        File dest = new File(Environment.getExternalStorageDirectory() + dir);

        //Check is the directory exists
        if(!dest.exists()) {
            dest.mkdir();//directory is created;
        }

        //Try to get the list of assets
        try {
            files = assetManager.list(asset);
        } catch (IOException e) {
            Log.e(TAG, "Failed to get asset file list.", e);
        }

        //Open the asset and copy it
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

    /**
     * Short method that copies the files using an InputStream and OutputStream
     * @param in = The input stream
     * @param out = The output stream
     * @throws IOException if one of the streams fail
     */
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

}

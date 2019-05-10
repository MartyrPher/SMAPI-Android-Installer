package com.MartyrPher.smapiandroidinstaller;

import android.os.Environment;
import android.util.Log;

import com.MartyrPher.smapiandroidinstaller.apksigner.KeyStoreFileManager;
import com.MartyrPher.smapiandroidinstaller.apksigner.ZipSigner;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;


public class SignApk {

    private static final String TAG = "SignApk";
    private static final String APK_LOCATION = Environment.getExternalStorageDirectory() + "/SMAPI Installer/base.apk_patched0.apk_patched1.apk";
    private static final String KEYSTORE_LOCATION = Environment.getExternalStorageDirectory() + "/SMAPI Installer/ApkFiles/debug.keystore";
    private static final String KEYSTORE_PASSWORD = "android";


    public SignApk()
    {
    }

    public void commitSignApk()
    {
        try {
            String inputFile = APK_LOCATION;
            String outputFile = Environment.getExternalStorageDirectory() + "/SMAPI Installer/base_signed.apk";
            
            KeyStore keyStore = KeyStoreFileManager.loadKeyStore(KEYSTORE_LOCATION,KEYSTORE_PASSWORD.toCharArray());
            String alias = keyStore.aliases().nextElement();

            X509Certificate publicKey = (X509Certificate) keyStore.getCertificate(alias);
            try
            {
                PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, KEYSTORE_PASSWORD.toCharArray());
                ZipSigner.signZip(publicKey, privateKey, "SHA1withRSA", inputFile, outputFile);
            }
            catch(NoSuchAlgorithmException nsa)
            {
                Log.e(TAG, nsa.getMessage());
            }
        }
        catch(KeyStoreException ks)
        {
            Log.e(TAG, ks.getMessage());
        }
        catch(IOException io)
        {
            Log.e(TAG, io.getMessage());
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
        }

    }

}

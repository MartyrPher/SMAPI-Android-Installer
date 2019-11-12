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

/**
 * Allows the new APK to be signed using JAR signing
 */
public class SignApk {

    //TAG used for
    private static final String TAG = "SignApk";

    //Path of the keystore
    private static final String KEYSTORE_LOCATION = Environment.getExternalStorageDirectory() + "/SMAPI Installer/ApkFiles/debug.keystore";

    //The password for the keystore
    private static final String KEYSTORE_PASSWORD = "android";

    //Empty Constructor
    public SignApk()
    {
    }

    /**
     * Attempt tp sign the APK using ZIP Signer
     */
    public void commitSignApk()
    {
        try {
            //Input APK and Output APK
            String inputFile = Environment.getExternalStorageDirectory() + "/SMAPI Installer/" + ApkExtractor.sourceApkFilename + "_patched.apk";
            String outputFile = Environment.getExternalStorageDirectory() + "/SMAPI Installer/base_signed.apk";

            //Set the keystore and alias
            KeyStore keyStore = KeyStoreFileManager.loadKeyStore(KEYSTORE_LOCATION,KEYSTORE_PASSWORD.toCharArray());
            String alias = keyStore.aliases().nextElement();

            //get the Cert from the keystore
            X509Certificate publicKey = (X509Certificate) keyStore.getCertificate(alias);
            try
            {
                //Grab the private key and then use ZipSigner to sign the apk
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

package com.MartyrPher.smapiandroidinstaller;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Writes files to the APK
 */
public class WriteApk {

    //TAG used for debug purposes
    private static final String TAG = "WriteApk";

    //Blank Constructor
    public WriteApk()
    {
    }

    /**
     * Adds the files to the APK
     * @param source = The file source
     * @param files = The file array to add to the APK
     * @param path = The paths where the files need to go in the APK
     * @param compression = Whether the file needs to be compressed inside the APK
     */
    public void addFilesToApk(File source, File[] files, String[] path, boolean[] compression){
        try
        {
            //Hmmmmmmmmm
            byte[] buffer = new byte[2048];

            //Create new Input and Output stream
            ZipInputStream zin = new ZipInputStream(new FileInputStream(source));
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream( source + "_patched.apk"));

            //Create new CRC32
            CRC32 crc = new CRC32();

            for(int i = 0; i < files.length; i++)
            {
                if (!compression[i])
                {
                    int bytesRead;
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(files[i]));
                    crc.reset();
                    while ((bytesRead = bufferedInputStream.read(buffer)) != -1)
                    {
                        crc.update(buffer, 0, bytesRead);
                    }
                }

                InputStream in = new FileInputStream(files[i]);
                ZipEntry compress = new ZipEntry(path[i] + files[i].getName());

                //If the file doesn't need to be compressed then STORE them
                if (!compression[i])
                {
                    compress.setMethod(ZipEntry.STORED);
                    compress.setCompressedSize(files[i].length());
                    compress.setSize(files[i].length());
                    compress.setCrc(crc.getValue());
                }

                out.putNextEntry(compress);
                for(int read = in.read(buffer); read > -1; read = in.read(buffer)){
                    out.write(buffer, 0, read);
                }
                out.closeEntry();
                in.close();
            }

            for(ZipEntry ze = zin.getNextEntry(); ze != null; ze = zin.getNextEntry()){
                if(!apkEntryMatch(ze.getName(), files, path)){
                    ZipEntry loc_ze = new ZipEntry(ze.getName());
                    loc_ze.setMethod(ZipEntry.DEFLATED);

                    //STORE these type of files {Assemblies, resources and typemap}
                    if (loc_ze.getName().contains("assemblies") || loc_ze.getName().contains("resources.arsc") || loc_ze.getName().contains("typemap"))
                    {
                        loc_ze.setMethod(ZipEntry.STORED);
                        loc_ze.setSize(ze.getSize());
                        loc_ze.setCompressedSize(ze.getCompressedSize());
                        loc_ze.setCrc(ze.getCrc());
                    }

                    out.putNextEntry(loc_ze);
                    ZipFile zipFile = new ZipFile(source);
                    InputStream stream = zipFile.getInputStream(ze);

                    for(int read = stream.read(buffer); read > -1; read = stream.read(buffer)){
                        out.write(buffer, 0, read);
                    }
                    out.closeEntry();
                }
            }

            //Close everything and delete the source file
            out.close();
            zin.close();
            source.delete();
        }
        catch(Exception e){
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /***********************************************************************************
    |   Check if the current entry matches a file that needs to be placed in the APK
    |    zeName = The ZipEntry Name
    |    files = The files that are being checked
    |    path = The path that is being checked
    ***********************************************************************************/
    /**
     * Check if the current entry matches a file that needs to be placed in the APK
     * @param zeName = The ZipEntry Name
     * @param files = The files that are being checked
     * @param path = The path that is being checked
     * @return true is the entry matches the file in the APK
     */
    private boolean apkEntryMatch(String zeName, File[] files, String[] path){
        for(int i = 0; i < files.length; i++){
            if((path[i] + files[i].getName()).equals(zeName)){
                return true;
            }
        }
        return false;
    }
}
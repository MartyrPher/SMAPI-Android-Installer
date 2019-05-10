package com.MartyrPher.smapiandroidinstaller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class WriteApk {

    private static final String TAG = "OpenApk";

    //Blank Constructor
    public WriteApk()
    {
    }

    public void addFilesToApk(File source, File[] files, String path, boolean compression, int count){
        try{
            byte[] buffer = new byte[4096];
            ZipInputStream zin = new ZipInputStream(new FileInputStream(source));
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream( source + "_patched" + count + ".apk"));

            for(int i = 0; i < files.length; i++){
                CRC32 crc = new CRC32();
                if (!compression)
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
                ZipEntry compress = new ZipEntry(path + files[i].getName());

                if (!compression)
                {
                    compress.setMethod(ZipEntry.STORED);
                    compress.setCompressedSize(files[i].length());
                    compress.setSize(files[i].length());
                    compress.setCrc(crc.getValue());
                }

                out.putNextEntry(new ZipEntry(compress));
                for(int read = in.read(buffer); read > -1; read = in.read(buffer)){
                    out.write(buffer, 0, read);
                }
                out.closeEntry();
                in.close();
            }
            for(ZipEntry ze = zin.getNextEntry(); ze != null; ze = zin.getNextEntry()){
                if(!apkEntryMatch(ze.getName(), files, path)){
                    out.putNextEntry(ze);
                    for(int read = zin.read(buffer); read > -1; read = zin.read(buffer)){
                        out.write(buffer, 0, read);
                    }
                    out.closeEntry();
                }
            }
            out.close();
            zin.close();
            source.delete();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private boolean apkEntryMatch(String zeName, File[] files, String path){
        for(int i = 0; i < files.length; i++){
            if((path + files[i].getName()).equals(zeName)){
                return true;
            }
        }
        return false;
    }
}
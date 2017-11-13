package com.preons.pranav.QRCodeGenerator.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import static com.preons.pranav.QRCodeGenerator.utils.c.DATE_FORMAT;

/**
 * Created on 12-03-17 at 11:23 by Pranav Raut.
 * For QRCodeProtection
 */

public class CFileHelper extends FileProvider {
    @Nullable
    private Context c;

    public CFileHelper() {
        this(null);
    }

    public CFileHelper(@Nullable Context c) {
        super();
        this.c = c;
    }

    @Nullable
    public static File tempImageFile(InputStream inputStream) {
        byte[] buffer;
        String date = DATE_FORMAT.format(new Date());
        File temp = null;
        File tempFol = new File(Environment.getExternalStorageDirectory(), "QRP_temp");
        try {
            buffer = new byte[inputStream.available()];
            boolean b = true;
            if (!tempFol.exists())
                b = tempFol.mkdir();
            if (b) {
                temp = new File(tempFol, "Image" + date + ".jpg");
                temp.deleteOnExit();
                OutputStream outStream = new FileOutputStream(temp);
                outStream.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static void copyFile(File originalApk, File tempFile) throws IOException {
        InputStream in = new FileInputStream(originalApk);
        OutputStream out = new FileOutputStream(tempFile);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }

        in.close();
        out.close();
    }

    public void scanMedia(File file) {
        Intent intent =
                new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        if (c != null) c.sendBroadcast(intent);
    }
}
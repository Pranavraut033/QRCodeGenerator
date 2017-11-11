package com.preons.pranav.QRCodeGenerator.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;

import java.io.File;
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

public class CFileHelper {
    private Context c;

    public CFileHelper(Context c) {
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

    public void scanMedia(File file) {
        Intent intent =
                new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        c.sendBroadcast(intent);
    }
}
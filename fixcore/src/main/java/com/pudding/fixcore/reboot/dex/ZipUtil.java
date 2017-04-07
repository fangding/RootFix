/*
 * DroidFix Project
 * file ZipUtil.java  is  part of DroidFix
 * The MIT License (MIT)  Copyright (c) 2015 Bunny Blue.
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */
/* Apache Harmony HEADER because the code in this class comes mostly from ZipFile, ZipEntry and
 * ZipConstants from android libcore.
 */

package com.pudding.fixcore.reboot.dex;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ZipUtil {
    private static final int ENDHDR = 22;
    private static final int ENDSIG = 101010256;
    private static final int BUFFER_SIZE = 16384;

    public ZipUtil() {
    }

    public static File copyAsset(Context context, String assetName, File dir) throws IOException {
        File outFile = new File(dir, assetName);
        if(!outFile.exists()) {
            outFile.getParentFile().mkdirs();
            AssetManager assetManager = context.getAssets();
            InputStream in = assetManager.open(assetName);
            FileOutputStream out = new FileOutputStream(outFile);
            copyFile((InputStream)in, (OutputStream)out);
            in.close();
            out.close();
        }

        return outFile;
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];

        int read;
        while((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }

    }

    public static File copyFile(File src, File dir) throws IOException {
        File outFile = new File(dir, src.getName());
        if(!outFile.exists()) {
            outFile.getParentFile().mkdirs();
            FileInputStream in = new FileInputStream(src);
            FileOutputStream out = new FileOutputStream(outFile);
            copyFile((InputStream)in, (OutputStream)out);
            in.close();
            out.close();
        }

        return outFile;
    }

    static class CentralDirectory {
        long offset;
        long size;

        CentralDirectory() {
        }
    }
}
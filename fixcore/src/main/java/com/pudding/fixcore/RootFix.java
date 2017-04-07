/*
 * DroidFix Project
 * file DroidFix.java  is  part of DroidFix
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

package com.pudding.fixcore;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.pudding.fixcore.reboot.dex.DexV14;
import com.pudding.fixcore.reboot.dex.DexV19;
import com.pudding.fixcore.reboot.dex.DexV23;
import com.pudding.fixcore.reboot.dex.DexV24;
import com.pudding.fixcore.reboot.dex.DexV4;
import com.pudding.fixcore.reboot.dex.ZipUtil;
import com.pudding.fixcore.reboot.so.SoV23;
import com.pudding.fixcore.reboot.so.SoV4;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public final class RootFix {


//        static String DROID_CODE_CACHE = "YLSDKDroidFix" + File.separator + "code_cache";


    /**
     * 初始化 RootPatch 的安装
     * @param context   上下文
     * @param WorkPath  data/data/package/file文件夹下的相对工作路径
     */
    public static void initDexPatch(Context context, String WorkPath) {
        Log.i("YLSDKDroidFix", "init");
        File dexDir = new File(context.getFilesDir().getAbsolutePath(), WorkPath);

        try {
            File e = ZipUtil.copyAsset(context, "RootPatch.apk", dexDir);
            ArrayList files = new ArrayList();
            files.add(e);
            installDex(context.getClassLoader(), dexDir, files, false);
        } catch (IOException var4) {
            var4.printStackTrace();
        } catch (InvocationTargetException var5) {
            var5.printStackTrace();
        } catch (NoSuchMethodException var6) {
            var6.printStackTrace();
        } catch (NoSuchFieldException var7) {
            var7.printStackTrace();
        } catch (InstantiationException var8) {
            var8.printStackTrace();
        } catch (IllegalAccessException var9) {
            var9.printStackTrace();
        } catch (Exception var10) {
            var10.printStackTrace();
        }

    }

    /**
     * 安装补丁包
     * @param context   上下文对象
     * @param patch     补丁包文件对象
     * @param WorkPath  data/data/package/file文件夹下的相对工作路径
     */
    public static void installDexPatch(Context context, File patch, String WorkPath) {
        Log.i("YLSDKDroidFix", "installPatch");
        File dexDir = new File(context.getFilesDir().getAbsolutePath(), WorkPath);
        ArrayList files = new ArrayList();
        files.add(patch);

        try {
            installDex(context.getClassLoader(), dexDir, files, true);
        } catch (IllegalAccessException var5) {
            var5.printStackTrace();
        } catch (NoSuchFieldException var6) {
            var6.printStackTrace();
        } catch (InvocationTargetException var7) {
            var7.printStackTrace();
        } catch (NoSuchMethodException var8) {
            var8.printStackTrace();
        } catch (IOException var9) {
            var9.printStackTrace();
        } catch (InstantiationException var10) {
            var10.printStackTrace();
        } catch (Exception var11) {
            var11.printStackTrace();
        }

    }

    private static void installDex(ClassLoader loader, File dexDir, List<File> files, boolean isHotfix) throws
            Exception {
        if(!files.isEmpty()) {
            ClassLoader classLoader = loader;
            if (Build.VERSION.SDK_INT >= 24) {
                DexV24.install(loader, files, dexDir);
            } else if(Build.VERSION.SDK_INT >= 23) {
                DexV23.install(classLoader, files, dexDir, isHotfix);
            } else if(Build.VERSION.SDK_INT >= 19) {
                DexV19.install(classLoader, files, dexDir, isHotfix);
            } else if(Build.VERSION.SDK_INT >= 14) {
                DexV14.install(classLoader, files, dexDir, isHotfix);
            } else {
                DexV4.install(classLoader, files, isHotfix);
            }
        }

    }

    /**
     * 安装so替换文件(从指定目录加载so)
     * @param classloader                    Context获取的ClassLoader对象
     * @param additionalSoPath               注意,目录要跟jniLibs下的结构一样才可以比如 armeabi/libstub.so(填写绝对路径)
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws InstantiationException
     * @throws IOException
     */
    public static void installSoPatch(ClassLoader classloader, File additionalSoPath) throws InvocationTargetException,
            NoSuchMethodException, IllegalAccessException, NoSuchFieldException, InstantiationException, IOException {
        if (additionalSoPath != null && additionalSoPath.exists()) {
            if (Build.VERSION.SDK_INT >= 23) {
                SoV23.install(classloader, additionalSoPath);
            } else if (Build.VERSION.SDK_INT >= 14) {
                SoV23.install(classloader, additionalSoPath);
            } else {
                SoV4.install(classloader, additionalSoPath);
            }
        }
    }



    /*public static void copyFile(File source, File dest) throws IOException {
        FileInputStream input = null;
        FileOutputStream output = null;
        if(!dest.exists()) {
            dest.createNewFile();

            try {
                input = new FileInputStream(source);
                output = new FileOutputStream(dest);
                byte[] buf = new byte[1024];

                int bytesRead;
                while((bytesRead = input.read(buf)) > 0) {
                    output.write(buf, 0, bytesRead);
                }
            } finally {
                if(input != null) {
                    input.close();
                }

                if(output != null) {
                    output.close();
                }

            }

        }
    }*/






}

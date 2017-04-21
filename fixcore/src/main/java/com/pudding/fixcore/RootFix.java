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
import com.pudding.fixcore.runtime.LogendUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

public final class RootFix {


//        static String DROID_CODE_CACHE = "YLSDKDroidFix" + File.separator + "code_cache";


    /**
     * 初始化 RootPatch 的安装
     *
     * @param context  上下文
     * @param WorkPath data/data/package/file文件夹下的相对工作路径
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
     *
     * @param context  上下文对象
     * @param patch    补丁包文件对象
     * @param WorkPath data/data/package/file文件夹下的相对工作路径
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
        if (!files.isEmpty()) {
            ClassLoader classLoader = loader;
            if (Build.VERSION.SDK_INT >= 24) {
                DexV24.install(loader, files, dexDir);
            } else if (Build.VERSION.SDK_INT >= 23) {
                DexV23.install(classLoader, files, dexDir, isHotfix);
            } else if (Build.VERSION.SDK_INT >= 19) {
                DexV19.install(classLoader, files, dexDir, isHotfix);
            } else if (Build.VERSION.SDK_INT >= 14) {
                DexV14.install(classLoader, files, dexDir, isHotfix);
            } else {
                DexV4.install(classLoader, files, isHotfix);
            }
        }

    }

    /**
     * 安装so替换文件(从指定目录加载so)
     *
     * @param classloader      Context获取的ClassLoader对象
     * @param additionalSoPath 注意,目录要跟jniLibs下的结构一样才可以比如 armeabi/libstub.so(填写绝对路径)
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


    /**
     * 从指定目录加载补丁
     *
     * @param context
     * @param WorkPath
     * @param dexPath
     */
    public static void installDexPatchRuntime(Context context, String WorkPath, String dexPath) {

        if (context == null) {
            return;
        } else {
            context = context.getApplicationContext();
        }
        File mOptDir = new File(context.getFilesDir().getAbsolutePath(), WorkPath);
        try {
            File file = new File(dexPath);
            File optfile = new File(mOptDir, file.getName());
            final DexFile dexFile = DexFile.loadDex(file.getAbsolutePath(), optfile.getAbsolutePath(), Context.MODE_PRIVATE);
            ClassLoader classLoader = context.getClassLoader();
            ClassLoader patchClassLoader = new ClassLoader(classLoader) {
                @Override
                protected Class<?> findClass(String className)
                        throws ClassNotFoundException {
                    Class<?> clazz = dexFile.loadClass(className, this);
                    if (clazz == null
                            && (className.startsWith("com.pudding.fixcore")
                    )) {
                        return Class.forName(className);
                    }
                    if (clazz == null) {
                        throw new ClassNotFoundException(className);
                    }
                    return clazz;
                }
            };
            Enumeration<String> entrys = dexFile.entries();
            Class<?> clazz = null;
            while (entrys.hasMoreElements()) {
                String entry = entrys.nextElement();

                clazz = dexFile.loadClass(entry, patchClassLoader);
                if (clazz != null) {
                    LogendUtils.fixClass(clazz, classLoader);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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

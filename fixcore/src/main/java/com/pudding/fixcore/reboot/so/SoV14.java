package com.pudding.fixcore.reboot.so;

import com.pudding.fixcore.reboot.RocooUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Administrator on 2017/4/7.
 */

public class SoV14 {

    private static void install(ClassLoader loader, File additionalSoPath) throws IllegalArgumentException,
            IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
        Field pathListField = RocooUtils.findField(loader, "pathList");
        Object dexPathList = pathListField.get(loader);

        Field nativeLibraryDirectoriesField = RocooUtils.findField(dexPathList, "nativeLibraryDirectories");
        File[] nativeLibraryDirectories = (File[]) nativeLibraryDirectoriesField.get(dexPathList);
        if (nativeLibraryDirectories != null) {
            File[] tmp = new File[(nativeLibraryDirectories.length + 1)];
            tmp[0] = additionalSoPath;
            System.arraycopy(nativeLibraryDirectories, 0, tmp, 1, nativeLibraryDirectories.length);
            nativeLibraryDirectoriesField.set(dexPathList, tmp);
        }
    }
}
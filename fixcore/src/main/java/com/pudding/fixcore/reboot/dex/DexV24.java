package com.pudding.fixcore.reboot.dex;

import com.pudding.fixcore.reboot.RocooUtils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import dalvik.system.DexFile;

/**
 * Created by Administrator on 2017/4/7.
 */

public class DexV24 {

    public static void install(ClassLoader loader, List<File> additionalClassPathEntries,
                                File optimizedDirectory)
            throws IllegalArgumentException, IllegalAccessException,
            NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, ClassNotFoundException {

        Field pathListField = RocooUtils.findField(loader, "pathList");
        Object dexPathList = pathListField.get(loader);
        Field dexElement = RocooUtils.findField(dexPathList, "dexElements");
        Class<?> elementType = dexElement.getType().getComponentType();
        Method loadDex = RocooUtils.findMethod(dexPathList, "loadDexFile", File.class, File.class, ClassLoader.class, dexElement.getType());
        loadDex.setAccessible(true);

        Object dex = loadDex.invoke(null, additionalClassPathEntries.get(0), optimizedDirectory, loader, dexElement.get(dexPathList));
        Constructor<?> constructor = elementType.getConstructor(File.class, boolean.class, File.class, DexFile.class);
        constructor.setAccessible(true);
        Object element = constructor.newInstance(new File(""), false, additionalClassPathEntries.get(0), dex);

        Object[] newEles = new Object[1];
        newEles[0] = element;
        RocooUtils.expandFieldArray(dexPathList, "dexElements", newEles);
    }

}

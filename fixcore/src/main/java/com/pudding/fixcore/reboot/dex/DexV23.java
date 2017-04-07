package com.pudding.fixcore.reboot.dex;

import com.pudding.fixcore.reboot.NuwaUtils;

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

public class DexV23 {
    private DexV23() {
    }

    public static void install(ClassLoader loader, List<File> additionalClassPathEntries, File optimizedDirectory, boolean isHotfix) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        Field pathListField = NuwaUtils.findField(loader, "pathList");
        Object dexPathList = pathListField.get(loader);
        Field dexElement = NuwaUtils.findField(dexPathList, "dexElements");
        Class elementType = dexElement.getType().getComponentType();
        Method loadDex = NuwaUtils.findMethod(dexPathList, "loadDexFile", new Class[]{File.class, File.class});
        loadDex.setAccessible(true);
        Object dex = loadDex.invoke((Object)null, new Object[]{additionalClassPathEntries.get(0), optimizedDirectory});
        Constructor constructor = elementType.getConstructor(new Class[]{File.class, Boolean.TYPE, File.class, DexFile.class});
        constructor.setAccessible(true);
        Object element = constructor.newInstance(new Object[]{new File(""), Boolean.valueOf(false), additionalClassPathEntries.get(0), dex});
        Object[] newEles = new Object[]{element};
        NuwaUtils.expandFieldArray(dexPathList, "dexElements", newEles, isHotfix);
    }
}
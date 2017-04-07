package com.pudding.fixcore.reboot.dex;

import com.pudding.fixcore.reboot.NuwaUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/7.
 */

public class DexV14 {
    private DexV14() {
    }

    public static void install(ClassLoader loader, List<File> additionalClassPathEntries, File optimizedDirectory, boolean isHotfix) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
        Field pathListField = NuwaUtils.findField(loader, "pathList");
        Object dexPathList = pathListField.get(loader);
        NuwaUtils.expandFieldArray(dexPathList, "dexElements", makeDexElements(dexPathList, new ArrayList(additionalClassPathEntries), optimizedDirectory), isHotfix);
    }

    public static Object[] makeDexElements(Object dexPathList, ArrayList<File> files, File optimizedDirectory) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Method makeDexElements = NuwaUtils.findMethod(dexPathList, "makeDexElements", new Class[]{ArrayList.class, File.class});
        return (Object[])((Object[])makeDexElements.invoke(dexPathList, new Object[]{files, optimizedDirectory}));
    }
}
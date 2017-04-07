package com.pudding.fixcore.reboot.dex;

import android.util.Log;

import com.pudding.fixcore.reboot.NuwaUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/4/7.
 */

public class DexV19 {
    private DexV19() {
    }

    public static void install(ClassLoader loader, List<File> additionalClassPathEntries, File optimizedDirectory, boolean isHotfix) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
        Field pathListField = NuwaUtils.findField(loader, "pathList");
        Object dexPathList = pathListField.get(loader);
        ArrayList suppressedExceptions = new ArrayList();
        NuwaUtils.expandFieldArray(dexPathList, "dexElements", makeDexElements(dexPathList, new ArrayList(additionalClassPathEntries), optimizedDirectory, suppressedExceptions), isHotfix);
        if(suppressedExceptions.size() > 0) {
            Iterator suppressedExceptionsField = suppressedExceptions.iterator();

            while(suppressedExceptionsField.hasNext()) {
                IOException dexElementsSuppressedExceptions = (IOException)suppressedExceptionsField.next();
                Log.w("YLSDKDroidFix", "Exception in makeDexElement", dexElementsSuppressedExceptions);
            }

            Field suppressedExceptionsField1 = NuwaUtils.findField(loader, "dexElementsSuppressedExceptions");
            IOException[] dexElementsSuppressedExceptions1 = (IOException[])((IOException[])suppressedExceptionsField1.get(loader));
            if(dexElementsSuppressedExceptions1 == null) {
                dexElementsSuppressedExceptions1 = (IOException[])suppressedExceptions.toArray(new IOException[suppressedExceptions.size()]);
            } else {
                IOException[] combined = new IOException[suppressedExceptions.size() + dexElementsSuppressedExceptions1.length];
                suppressedExceptions.toArray(combined);
                System.arraycopy(dexElementsSuppressedExceptions1, 0, combined, suppressedExceptions.size(), dexElementsSuppressedExceptions1.length);
                dexElementsSuppressedExceptions1 = combined;
            }

            suppressedExceptionsField1.set(loader, dexElementsSuppressedExceptions1);
        }

    }

    public static Object[] makeDexElements(Object dexPathList, ArrayList<File> files, File optimizedDirectory, ArrayList<IOException> suppressedExceptions) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Method makeDexElements = NuwaUtils.findMethod(dexPathList, "makeDexElements", new Class[]{ArrayList.class, File.class, ArrayList.class});
        return (Object[])((Object[])makeDexElements.invoke(dexPathList, new Object[]{files, optimizedDirectory, suppressedExceptions}));
    }
}

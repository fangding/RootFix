package com.pudding.fixcore.reboot.so;

import android.util.Log;

import com.pudding.fixcore.reboot.RocooUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/4/7.
 */

public class SoV23 {

    public static void install(ClassLoader loader, File additionalSoPath) throws IllegalArgumentException,
            IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException,
            InstantiationException {

        try {
            Object dexPathList = RocooUtils.findField(loader, "pathList").get(loader);
            ArrayList<File> additionalPathEntries = new ArrayList<>();
            additionalPathEntries.add(additionalSoPath);
            ArrayList<IOException> suppressedExceptions = new ArrayList<>();
            RocooUtils.expandFieldArray(dexPathList, "nativeLibraryPathElements",
                    RocooUtils.makePathElements(dexPathList, additionalPathEntries, null, suppressedExceptions));
            if (suppressedExceptions.size() > 0) {
                for (IOException e : suppressedExceptions) {
                    Log.w(TAG, "Exception in makeDexElement", e);
                }
                Field suppressedExceptionsField =
                        RocooUtils.findField(dexPathList, "dexElementsSuppressedExceptions");
                IOException[] dexElementsSuppressedExceptions =
                        (IOException[]) suppressedExceptionsField.get(dexPathList);

                if (dexElementsSuppressedExceptions == null) {
                    dexElementsSuppressedExceptions =
                            suppressedExceptions.toArray(new IOException[suppressedExceptions.size()]);
                } else {
                    IOException[] combined =
                            new IOException[suppressedExceptions.size() + dexElementsSuppressedExceptions.length];
                    suppressedExceptions.toArray(combined);
                    System.arraycopy(dexElementsSuppressedExceptions, 0, combined, suppressedExceptions.size(),
                            dexElementsSuppressedExceptions.length);
                    dexElementsSuppressedExceptions = combined;
                }

                suppressedExceptionsField.set(dexPathList, dexElementsSuppressedExceptions);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        }
    }
}
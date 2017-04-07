package com.pudding.fixcore.reboot.dex;

import com.pudding.fixcore.reboot.NuwaUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.ListIterator;
import java.util.zip.ZipFile;

import dalvik.system.DexFile;

/**
 * Created by Administrator on 2017/4/7.
 */

public class DexV4 {
    private DexV4() {
    }

    public static void install(ClassLoader loader, List<File> additionalClassPathEntries, boolean isHotfix) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, IOException {
        int extraSize = additionalClassPathEntries.size();
        Field pathField = NuwaUtils.findField(loader, "path");
        StringBuilder path = new StringBuilder((String)pathField.get(loader));
        String[] extraPaths = new String[extraSize];
        File[] extraFiles = new File[extraSize];
        ZipFile[] extraZips = new ZipFile[extraSize];
        DexFile[] extraDexs = new DexFile[extraSize];

        String entryPath;
        int index;
        for(ListIterator iterator = additionalClassPathEntries.listIterator(); iterator.hasNext(); extraDexs[index] = DexFile.loadDex(entryPath, entryPath + ".dex", 0)) {
            File additionalEntry = (File)iterator.next();
            entryPath = additionalEntry.getAbsolutePath();
            path.append(':').append(entryPath);
            index = iterator.previousIndex();
            extraPaths[index] = entryPath;
            extraFiles[index] = additionalEntry;
            extraZips[index] = new ZipFile(additionalEntry);
        }

        pathField.set(loader, path.toString());
        NuwaUtils.expandFieldArray(loader, "mPaths", extraPaths, isHotfix);
        NuwaUtils.expandFieldArray(loader, "mFiles", extraFiles, isHotfix);
        NuwaUtils.expandFieldArray(loader, "mZips", extraZips, isHotfix);
        NuwaUtils.expandFieldArray(loader, "mDexs", extraDexs, isHotfix);
    }
}
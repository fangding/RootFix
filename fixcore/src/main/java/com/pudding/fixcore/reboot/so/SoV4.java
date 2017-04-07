package com.pudding.fixcore.reboot.so;

import com.pudding.fixcore.reboot.RocooUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Administrator on 2017/4/7.
 */

public class SoV4 {
    public static void install(ClassLoader loader, File additionalSoPath) throws IllegalArgumentException,
            IllegalAccessException, NoSuchFieldException, IOException {

        Field pathField = RocooUtils.findField(loader, "path");
        List<String> libraryPathElements =
                (List<String>) RocooUtils.findField(loader, "libraryPathElements").get(loader);
        if (libraryPathElements != null) {
            libraryPathElements.add(0, additionalSoPath.getAbsolutePath());
        }
    }
}
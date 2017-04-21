package com.pudding.fixcore.runtime;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/4/7.
 */

public class LogendUtils {

    private static Map<String, Class<?>> mFixedClass = new ConcurrentHashMap<String, Class<?>>();

    public static void fixClass(Class<?> clazz, ClassLoader classLoader) {
        if (clazz == null) {
            return;
        }
        Method[] methods = clazz.getDeclaredMethods();
        try {
            Class<?> aClass = classLoader.loadClass(clazz.getName());
            String key = aClass.getName() + "@" + classLoader.toString();
            Class<?> clazzFixed = mFixedClass.get(key);
            if (clazzFixed == null) {
                Class<?> clzz = classLoader.loadClass(clazz.getName());
                // 他喵的我忘了初始化这个类了
                clazzFixed = initTargetClass(clzz);
            }
            if (clazzFixed != null) {
                mFixedClass.put(key, clazzFixed);
                for (Method fixMethod : methods) {
                    replaceMethod(aClass, fixMethod, classLoader);
                }
            }


        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    public static Class<?> initTargetClass(Class<?> clazz) {
        try {
            Class<?> targetClazz = Class.forName(clazz.getName(), true,
                    clazz.getClassLoader());
//            initFields(targetClazz);
            return targetClazz;
        } catch (Exception e) {
        }
        return null;
    }

    private static void replaceMethod(Class<?> aClass, Method fixMethod, ClassLoader classLoader) throws NoSuchMethodException {

        try {

            Method originMethod = aClass.getDeclaredMethod(fixMethod.getName(), fixMethod.getParameterTypes());
            HookManager.getDefault().hookMethod(originMethod, fixMethod);
        } catch (Exception e) {
            Log.e(TAG, "replaceMethod", e);
        }


    }
}

package com.fanding.fixcore.demo;

import android.app.Activity;
import android.content.Context;

import com.pudding.fixcore.RootFix;

/**
 * @author Pudding
 * @content
 * @time 2018/7/18
 */
public class CustomApplication extends Activity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);

        // 初始化热更
        RootFix.initDexPatch(getBaseContext(), "RootFix");
    }
}

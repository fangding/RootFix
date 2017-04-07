package com.fanding.fixcore.demo;

import android.app.Activity;
import android.os.Bundle;

import com.pudding.fixcore.RootFix;

/**
 * Created by Administrator on 2017/4/6.
 */

public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RootFix.init(getBaseContext());


    }
}

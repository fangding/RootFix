package com.fanding.fixcore.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pudding.fixcore.RootFix;

import java.io.File;

/**
 * Created by Administrator on 2017/4/6.
 */

public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.main_layout);

        Button btRestar = (Button) findViewById(R.id.button_restar);
        btRestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File fixApkFile = null; //补丁包文件对象
                RootFix.installDexPatch(MainActivity.this, fixApkFile, "RootFix");
            }
        });

        Button btRuntime = (Button) findViewById(R.id.button_restar);
        btRuntime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fixApkFilePath = null; //补丁包文件对象
                RootFix.installDexPatchRuntime(MainActivity.this, "RootFix", fixApkFilePath);
            }
        });

    }
}

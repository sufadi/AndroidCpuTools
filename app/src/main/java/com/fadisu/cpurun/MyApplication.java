package com.fadisu.cpurun;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fadisu.cpurun.util.BuildHelper;
import com.fadisu.cpurun.util.CpuUtils;
import com.fadisu.cpurun.util.ReadCpuProcFile;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }
}

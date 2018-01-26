package com.fadisu.cpurun.util;


import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 属性文件
 * /init.rc
 * <p/>
 * /default.prop
 * <p/>
 * /system/build.prop
 */
public class PropInfoUtil {

    private static final String TAG = PropInfoUtil.class.getSimpleName();

    public static List<String> getPropInfo() {
        List<String> result = new ArrayList<>();

        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader("/default.prop"));
            result.add("*** Read From /default.prop ***");
            while ((line = br.readLine()) != null) {
                result.add(line);
            }

            result.add("*** Read From /system/build.prop ***");
            br = new BufferedReader(new FileReader("/system/build.prop"));
            while ((line = br.readLine()) != null) {
                result.add(line);
            }

            /*
                (Permission denied)

                result.add("*** Read From /init.rc ***");
                Log.d(TAG, "*** Read From /init.rc ***");
                br = new BufferedReader(new FileReader("/init.rc"));
                while ((line = br.readLine()) != null) {
                    result.add(line);
                    Log.d(TAG, line);
                }
            */
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }

    public static String getJavaVM() {
        String result = null;
        result = System.getProperty("java.vm.name");
        if (result != null) {
            result = result + System.getProperty("java.vm.version");
        }

        return result;
    }
}

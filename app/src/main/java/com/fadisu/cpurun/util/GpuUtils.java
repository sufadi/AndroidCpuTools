package com.fadisu.cpurun.util;

import com.fadisu.cpurun.bean.GpuFreqInfo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GpuUtils {

    /**
     * CPU 场景配置文件
     *
     * @return
     */
    public static GpuFreqInfo getGpuFreq() {
        GpuFreqInfo result = new  GpuFreqInfo();
        BufferedReader br = null;
        try {
            String line;
            br = new BufferedReader(new FileReader("/d/ged/hal/current_freqency"));
            if ((line = br.readLine()) != null) {
                String[] info = line.split("\\s+");
                result.id = Integer.parseInt(info[0]);
                result.freq = Integer.parseInt(info[1]);
            }

            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}

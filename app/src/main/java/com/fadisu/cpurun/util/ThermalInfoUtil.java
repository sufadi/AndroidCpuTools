package com.fadisu.cpurun.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ThermalInfoUtil {

    public static List<String> getThermalInfo() {
        List<String> result = new ArrayList<>();
        BufferedReader br = null;

        try {
            File dir = new File("/sys/class/thermal/");

            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (Pattern.matches("thermal_zone[0-9]+", file.getName())) {
                        return true;
                    }
                    return false;
                }
            });

            final int SIZE = files.length;
            String line = null;
            String type = null;
            String temp = null;
            for (int i = 0; i < SIZE; i++) {
                br = new BufferedReader(new FileReader("/sys/class/thermal/thermal_zone" + i + "/type"));
                line = br.readLine();
                if (line != null) {
                    type = line;
                }

                br = new BufferedReader(new FileReader("/sys/class/thermal/thermal_zone" + i + "/temp"));
                line = br.readLine();
                if (line != null) {
                    long temperature = Long.parseLong(line);
                    if (temperature < 0) {
                        temp = "Unknow";
                    } else {
                        temp = (float) (temperature / 1000.0) + "°C";
                    }

                }

                result.add(type + " : " + temp);
            }

            br.close();
        } catch (FileNotFoundException e) {
            result.add(e.toString());
        } catch (Exception e) {
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

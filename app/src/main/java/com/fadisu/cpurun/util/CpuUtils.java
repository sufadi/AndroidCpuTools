package com.fadisu.cpurun.util;


import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CpuUtils {

    private static final String TAG = CpuUtils.class.getSimpleName();

    public static boolean isCPU64() {
        boolean result = false;
        String mProcessor = null;
        List<String> list = null;
        try {
            mProcessor = getFieldFromCpuinfo("Processor");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mProcessor != null) {
            // D/CpuUtils: isCPU64 mProcessor = AArch64 Processor rev 4 (aarch64)
            Log.d(TAG, "isCPU64 mProcessor = " + mProcessor);
            //list =  Arrays.asList(mProcessor.split("\\s"));
            if (mProcessor.contains("aarch64")) {
                result = true;
            }
        }

        return result;
    }


    /* /proc/cpuinfo

        processor       : 0
        Processor       : AArch64 Processor rev 4 (aarch64)
        model name      : AArch64 Processor rev 4 (aarch64)
        BogoMIPS        : 26.00
        Features        : fp asimd evtstrm aes pmull sha1 sha2 crc32
        CPU implementer : 0x41
        CPU architecture: 8
        CPU variant     : 0x0
        CPU part        : 0xd03
        CPU revision    : 4
    */
    public static String getFieldFromCpuinfo(String field) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("/proc/cpuinfo"));
        Pattern p = Pattern.compile(field + "\\s*:\\s*(.*)");

        try {
            String line;
            while ((line = br.readLine()) != null) {
                Matcher m = p.matcher(line);
                if (m.matches()) {
                    return m.group(1);
                }
            }
        } finally {
            br.close();
        }

        return null;
    }

    /**
     * 获取 CPU 名称
     *
     * @return
     */
    public static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the number of cores available in this device, across all processors.
     * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
     * <p/>
     * Source: http://stackoverflow.com/questions/7962155/
     *
     * @return The number of cores, or 1 if failed to get result
     */
    public static int getNumCpuCores() {
        try {
            // Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    // Check if filename is "cpu", followed by a single digit number
                    if (Pattern.matches("cpu[0-9]+", file.getName())) {
                        return true;
                    }
                    return false;
                }
            });
            // Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            // Default to return 1 core
            Log.e(TAG, "Failed to count number of cores, defaulting to 1", e);
            return 1;
        }
    }

    /**
     * Get cpu's current frequency
     * unit:KHZ
     * 获取cpu当前频率,单位KHZ
     *
     * @return
     */
    public static List<Integer> getCpuCurFreq() {
        List<Integer> results = new ArrayList<Integer>();
        String freq = "";
        FileReader fr = null;
        try {
            int cpuIndex = 0;
            Integer lastFreq = 0;
            while (true) {
                File file = new File("/sys/devices/system/cpu/cpu" + cpuIndex + "/");
                if (!file.exists()) {
                    break;
                }
                file = new File("/sys/devices/system/cpu/cpu" + cpuIndex + "/cpufreq/");
                if (!file.exists()) {
                    lastFreq = 0;
                    results.add(0);
                    cpuIndex++;
                    continue;
                }
                file = new File("/sys/devices/system/cpu/cpu" + cpuIndex + "/cpufreq/scaling_cur_freq");
                if (!file.exists()) {
                    results.add(lastFreq);
                    cpuIndex++;
                    continue;
                }
                fr = new FileReader(
                        "/sys/devices/system/cpu/cpu" + cpuIndex + "/cpufreq/scaling_cur_freq");
                BufferedReader br = new BufferedReader(fr);
                String text = br.readLine();
                freq = text.trim();
                lastFreq = Integer.valueOf(freq);
                results.add(lastFreq);
                fr.close();
                cpuIndex++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return results;
    }
}
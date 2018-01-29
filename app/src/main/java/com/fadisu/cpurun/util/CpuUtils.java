package com.fadisu.cpurun.util;


import android.content.Context;
import android.util.Log;

import com.fadisu.cpurun.R;

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
     * 64 系统判断
     *
     * @return
     */
    public static boolean isCpu64() {
        boolean result = false;
        if (BuildHelper.isCpu64() || ProcCpuInfo.isCpu64()) {
            result = true;
        }
        return result;
    }

    /**
     * CPU 最大频率
     *
     * @return
     */
    public static long getCpuMaxFreq() {
        long result = 0L;
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"));
            if ((line = br.readLine()) != null) {
                result = Long.parseLong(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * CPU 最小频率
     *
     * @return
     */
    public static long getCpuMinFreq() {
        long result = 0L;
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"));
            if ((line = br.readLine()) != null) {
                result = Long.parseLong(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 可调节 CPU 频率档位
     *
     * @return
     */
    public static String getCpuAvailableFrequenciesSimple() {
        String result = null;
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies"));
            if ((line = br.readLine()) != null) {
                result = line;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 可调节 CPU 频率档位
     *
     * @return
     */
    public static List<Long> getCpuAvailableFrequencies() {
        List<Long> result = new ArrayList<>();
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies"));
            if ((line = br.readLine()) != null) {
                String[] list = line.split("\\s+");
                for (String value : list) {
                    long freq = Long.parseLong(value);
                    result.add(freq);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * CPU 调频策略
     *
     * @return
     */
    public static String getCpuGovernor() {
        String result = null;
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor"));
            if ((line = br.readLine()) != null) {
                result = line;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * CPU 支持的调频策略
     *
     * @return
     */
    public static String getCpuAvailableGovernorsSimple() {
        String result = null;
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors"));
            if ((line = br.readLine()) != null) {
                result = line;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * CPU 支持的调频策略
     *
     * @return
     */
    public static List<String> getCpuAvailableGovernors() {
        List<String> result = new ArrayList<>();
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors"));
            if ((line = br.readLine()) != null) {
                String[] list = line.split("\\s+");
                for (String value : list) {
                    result.add(value);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Get cpu's current frequency
     * unit:KHZ
     * 获取cpu当前频率,单位 HZ
     *
     * @return
     */
    public static List<String> getCpuCurFreq(Context mContext) {
        List<String> result = new ArrayList<>();
        int mCpuCoreNumber = getNumCpuCores();
        BufferedReader br = null;

        try {
            for (int i = 0; i < mCpuCoreNumber; i++) {
                final String path = "/sys/devices/system/cpu/cpu" + i + "/cpufreq/scaling_cur_freq";
                File mFile = new File(path);
                if (mFile.exists()) {
                    br = new BufferedReader(new FileReader(path));
                    String line = br.readLine();
                    if (line != null) {
                        result.add(String.format(mContext.getResources().getString(R.string.cpu_cur_freq), i, line));
                    }
                } else {
                    result.add(String.format(mContext.getResources().getString(R.string.cpu_stoped), i));
                }
                br.close();
            }
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

    /**
     * Get cpu's current frequency
     * unit:KHZ
     * 获取cpu当前频率,单位 HZ
     *
     * @return
     */
    public static List<String> getCpuOnlineStatus(Context mContext) {
        List<String> result = new ArrayList<>();
        int mCpuCoreNumber = getNumCpuCores();
        BufferedReader br = null;

        try {
            for (int i = 0; i < mCpuCoreNumber; i++) {
                br = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu" + i + "/online"));
                String line = br.readLine();
                if (line != null) {
                    result.add(String.format(mContext.getResources().getString(R.string.cpu_online_status), i,
                            ("1".equals(line) ? mContext.getResources().getString(R.string.cpu_online_status_online) : mContext.getResources().getString(R.string.cpu_online_status_offline))));
                }

                br.close();
            }
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

    /**
     * CPU 场景配置文件
     *
     * @return
     */
    public static List<String> getCpuSceneInfo() {
        List<String> result = new ArrayList<>();
        BufferedReader br = null;
        try {
            String line;
            br = new BufferedReader(new FileReader("/system/vendor/etc/perfservscntbl.txt"));
            result.add("/system/vendor/etc/perfservscntbl.txt");
            while ((line = br.readLine()) != null) {
                result.add(line);
            }

            result.add("/system/vendor/etc/perf_whitelist_cfg.xml");
            br = new BufferedReader(new FileReader("/system/vendor/etc/perf_whitelist_cfg.xml"));
            while ((line = br.readLine()) != null) {
                result.add(line);
            }

            br.close();
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

    /**
     * CPU 电压
     *
     * @return
     */
    public static List<String> getCpuVoltage() {
        List<String> result = new ArrayList<>();
        BufferedReader br = null;
        try {
            String line;
            br = new BufferedReader(new FileReader("/proc/cpufreq/MT_CPU_DVFS_LL/cpufreq_oppidx"));
            while ((line = br.readLine()) != null) {
                result.add(line);
            }

            br.close();
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
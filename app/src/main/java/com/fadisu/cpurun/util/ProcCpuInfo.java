package com.fadisu.cpurun.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcCpuInfo {

    private static final String TAG = ProcCpuInfo.class.getSimpleName();


    public static List<String> getCpuInfo() {
        List<String> result = new ArrayList<>();

        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader("/proc/cpuinfo"));
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
            br.close();
        } catch (FileNotFoundException e) {
            result.add(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
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
    public static String getProcessor() {
        String result = null;

        try {
            result = getFieldFromCpuinfo("Processor");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static boolean isCpu64() {
        boolean result = false;
        String mProcessor = null;
        try {
            mProcessor = getFieldFromCpuinfo("Processor");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mProcessor != null) {
            // D/CpuUtils: isCPU64 mProcessor = AArch64 Processor rev 4 (aarch64)
            Log.d(TAG, "isCPU64 mProcessor = " + mProcessor);
            if (mProcessor.contains("aarch64")) {
                result = true;
            }
        }

        return result;
    }

    public static String getArchitecture() {
        String result = null;

        try {
            String mCpuPart = getFieldFromCpuinfo("CPU part");
            Log.d(TAG, "mCpuPart = " + mCpuPart);

            if (mCpuPart.startsWith("0x") || mCpuPart.startsWith("0X")) {
                mCpuPart = mCpuPart.substring(2);
            }

            int mCpuPartId = Integer.valueOf(mCpuPart, 16);

            switch (mCpuPartId) {
                case 0x920:
                    result = "ARM" + " ARM920T";
                    break;
                case 0x926:
                    result = "ARM" + " ARM926EJ";
                    break;
                case 0xB36:
                    result = "ARM" + " ARM1136";
                    break;
                case 0xB56:
                    result = "ARM" + " ARM1156";
                    break;
                case 0xB76:
                    result = "ARM" + " ARM1176";
                    break;
                case 0xC05:
                    result = "ARM" + " Cortex-A5";
                    break;
                case 0xC07:
                    result = "ARM" + " Cortex-A7";
                    break;
                case 0xC08:
                    result = "ARM" + " Cortex-A8";
                    break;
                case 0xC09:
                    result = "ARM" + " Cortex-A9";
                    break;
                case 0xC0C:
                    result = "ARM" + " Cortex-A12";
                    break;
                case 0xC0F:
                    result = "ARM" + " Cortex-A15";
                    break;
                case 0xC0E:
                    result = "ARM" + " Cortex-A17";
                    break;
                case 0xc14:
                    result = "ARM" + " Cortex-R4";
                    break;
                case 0xc15:
                    result = "ARM" + " Cortex-R5";
                    break;
                case 0xc20:
                    result = "ARM" + " Cortex-M0";
                    break;
                case 0xc21:
                    result = "ARM" + " Cortex-M1";
                    break;
                case 0xc23:
                    result = "ARM" + " Cortex-M3";
                    break;
                case 0xc24:
                    result = "ARM" + " Cortex-M4";
                    break;
                case 0xD03:
                    result = "ARM" + " Cortex-A53";
                    break;
                case 0xD07:
                    result = "ARM" + " Cortex-A57";
                    break;
                case 0x8:
                    result = "NVIDIA" + " Tegra K1";
                    break;
                case 0xf:
                    result = "Qualcomm" + " Snapdragon S1/S2";
                    break;
                case 0x2d:
                    result = "Qualcomm" + " Snapdragon S2/S3";
                    break;
                case 0x4d:
                    result = "Qualcomm" + " Snapdragon S4";
                    break;
                case 0x6F:
                    result = "Qualcomm" + " Snapdragon 200/400/600/800";
                    break;
                default:
                    result = "0x" + Integer.toHexString(mCpuPartId);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}

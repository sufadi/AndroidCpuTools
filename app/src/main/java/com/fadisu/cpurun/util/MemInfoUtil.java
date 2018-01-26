package com.fadisu.cpurun.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemInfoUtil {


    public static List<String> getMemInfo() {
        List<String> result = new ArrayList<>();

        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader("/proc/meminfo"));
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /* /proc/meminfo

        MemTotal:        2902436 kB
        MemFree:          199240 kB
        MemAvailable:    1088764 kB
        Buffers:           40848 kB
        Cached:           862908 kB
        SwapCached:        54696 kB
        Active:          1222848 kB
        Inactive:         671468 kB
        Active(anon):     758516 kB
        Inactive(anon):   242560 kB
        Active(file):     464332 kB
        Inactive(file):   428908 kB
        Unevictable:        5972 kB
        Mlocked:             256 kB
        SwapTotal:       1048572 kB
        SwapFree:         537124 kB
        Dirty:                12 kB
        Writeback:             0 kB
        AnonPages:        988820 kB
        Mapped:           508996 kB
        Shmem:              4800 kB
        Slab:             157204 kB
        SReclaimable:      57364 kB
        SUnreclaim:        99840 kB
        KernelStack:       41376 kB
        PageTables:        51820 kB
        NFS_Unstable:          0 kB
        Bounce:                0 kB
        WritebackTmp:          0 kB
        CommitLimit:     2499788 kB
        Committed_AS:   76292324 kB
        VmallocTotal:   258867136 kB
        VmallocUsed:           0 kB
        VmallocChunk:          0 kB
        CmaTotal:              0 kB
        CmaFree:               0 kB
    */
    public static String getFieldFromMeminfo(String field) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("/proc/meminfo"));
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

}

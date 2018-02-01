package com.fadisu.cpurun.service;

import com.fadisu.cpurun.service.ICpuMsgCallBack;
import java.util.List;

interface ICpuMsgService {

    void registerCallback(ICpuMsgCallBack cb);

    void unregisterCallback(ICpuMsgCallBack cb);

    void updateCpuUsage(in List<String> msg);
}

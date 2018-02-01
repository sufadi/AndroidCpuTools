package com.fadisu.cpurun.service;

import java.util.List;

interface ICpuMsgCallBack {
    void updateCpuUsage(in List<String> msg);
}

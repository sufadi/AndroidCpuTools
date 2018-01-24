package com.fadisu.cpurun.util;

public class DateTimeUtil {

    /**
     * 毫秒时间
     * Long类型时间转换成时长
     */
    public static String longToSting(long time) {
        long hour = time / (60 * 60 * 1000);
        long minute = (time - hour * 60 * 60 * 1000) / (60 * 1000);
        long second = (time - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;
        return (hour == 0 ? "00" : (hour >= 10 ? hour : ("0" + hour))) + ":" + (minute == 0 ? "00" : (minute >= 10 ? minute : ("0" + minute))) + ":" + (second == 0 ? "00" : (second >= 10 ? second : ("0" + second)));
    }

}

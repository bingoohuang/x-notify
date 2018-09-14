package com.github.bingoohuang.xnotify;

import org.joda.time.DateTime;

@XNotify
public interface Sms {
    // 某排期课的开班人数未达标时，发送提醒
    @XNotify("[`静瑜伽`]有节课人数未达标，系统已自动取消已订课会员的预约：`2018年09月26日15:00`-`16:00``阴瑜伽`（`小班课`）")
    String classNotMeetRemind(String merchantName, DateTime start, DateTime end, String scheduleName, String courseTypeName);
}

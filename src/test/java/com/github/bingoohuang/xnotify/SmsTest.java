package com.github.bingoohuang.xnotify;

import lombok.val;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class SmsTest {
    @Test
    public void firstBlood() {
        Sms sms = XNotifyFactory.create(Sms.class);
        val fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        val start = DateTime.parse("2018-09-14 22:13:31", fmt);
        val end = DateTime.parse("2018-09-14 23:13:31", fmt);
        val x = sms.classNotMeetRemind("菜鸟瑜伽", start, end, "空中瑜伽", "私教课");
        assertThat(x).isEqualTo("[菜鸟瑜伽]有节课人数未达标，系统已自动取消已订课会员的预约：2018年09月14日22:13-23:13空中瑜伽（私教课）");
    }
}

package com.github.bingoohuang.xnotify;

import com.github.bingoohuang.xnotify.smssender.AliyunSmsSender;
import com.github.bingoohuang.xnotify.smssender.TencentYunSmsSender;
import com.github.bingoohuang.xnotify.smssender.YunpianSmsSender;
import com.google.common.collect.Lists;
import lombok.val;
import org.junit.Test;

import java.util.HashMap;

public class SenderTest {
    @Test
    public void testTencent() {
        val sender = new TencentYunSmsSender("appkey", "1400055908");

        // "验证码为：#code#（15分钟内有效），验证码打死也不要告诉别人哦！#merchantName#";
        sender.send("18612345678", "移动帮", 68689, Lists.newArrayList("4321", "XX嗨"));
    }

    @Test
    public void testYunpian() {
        val sender = new YunpianSmsSender("appkey");

        sender.send("18612345678", "奕起嗨", "验证码为：#4321#（15分钟内有效），验证码打死也不要告诉别人哦！XX嗨");
    }

    @Test
    public void testAliyun() {
        val sender = new AliyunSmsSender("accessKeyId", "accessSecret");

        // 验证码为：${code}（15分钟内有效），验证码打死也不要告诉别人哦！${name}!
        sender.send("18612345678", "奕起嗨", "SMS_12841674", new HashMap<String, String>() {{
            put("code", "4331");
            put("name", "XX嗨");
        }});
    }
}

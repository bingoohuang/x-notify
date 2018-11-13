package com.github.bingoohuang.xnotify;

import com.github.bingoohuang.xnotify.sender.AliyunSmsSender;

public class AliyunSmsProvider implements XProvider {
    @Override public String getProviderName() {
        return "aliyun";
    }

    @Override public XNotifySender getSender() {
        return new AliyunSmsSender(
                "abc",
                "efg");
    }
}

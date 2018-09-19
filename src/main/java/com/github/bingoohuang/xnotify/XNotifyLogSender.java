package com.github.bingoohuang.xnotify;

import com.github.bingoohuang.xnotify.impl.XNotifyLog;

public interface XNotifyLogSender {

    /**
     * 发送消息。
     *
     * @param log 消息存储对象
     */
    void send(XNotifyLog log);
}

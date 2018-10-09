package com.github.bingoohuang.xnotify;

import com.github.bingoohuang.xnotify.impl.XNotifyLog;

import java.util.Map;

public interface XNotifySender {
    /**
     * 发送消息。
     *
     * @param target       发送对象
     * @param msgType      消息类型
     * @param signName     签名
     * @param templateCode 模板编码
     * @param params       模板参数
     * @param text         消息内容
     * @return XNotifyLog
     */
    XNotifyLog send(XNotifyTarget target, String msgType, String signName, String templateCode, Map<String, String> params, String text);
}

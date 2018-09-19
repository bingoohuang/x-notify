package com.github.bingoohuang.xnotify.impl;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.westid.WestId;
import com.github.bingoohuang.xnotify.XNotifySender;
import com.github.bingoohuang.xnotify.XProvider;
import lombok.val;
import org.joda.time.DateTime;

public abstract class SaveDbProvider implements XProvider {
    @Override public String getProviderName() {
        return "db";
    }

    @Override public XNotifySender getSender() {
        return (target, msgType, signName, templateCode, params, text) -> {
            val log = XNotifyLog.builder()
                    .logId("" + WestId.next())
                    .state(0)
                    .targetId(target.getTargetId())
                    .mobile(target.getMobile())
                    .msgtype(msgType)
                    .username(target.getUsername())
                    .openid(target.getOpenid())
                    .usergroup(target.getUsergroup())
                    .signName(signName)
                    .templateCode(templateCode)
                    .templateVars(JSON.toJSONString(params))
                    .eval(text)
                    .createTime(DateTime.now()).build();
            saveLog(log);
            return log;
        };
    }

    protected abstract void saveLog(XNotifyLog log);
}

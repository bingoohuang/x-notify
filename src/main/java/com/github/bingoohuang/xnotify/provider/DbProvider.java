package com.github.bingoohuang.xnotify.provider;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.westid.WestId;
import com.github.bingoohuang.xnotify.XNotifySender;
import com.github.bingoohuang.xnotify.XProvider;
import com.github.bingoohuang.xnotify.impl.XNotifyLog;
import org.joda.time.DateTime;

public abstract class DbProvider implements XProvider {
    @Override public String getProviderName() {
        return "db";
    }

    @Override public XNotifySender getSender() {
        return (target, msgType, signName, templateCode, params, text) -> {
            XNotifyLog log = XNotifyLog.builder()
                    .logId("" + WestId.next())
                    .state(0)
                    .targetId(target == null ? null : target.getTargetId())
                    .mobile(target == null ? null : target.getMobile())
                    .msgtype(msgType)
                    .username(target == null ? null : target.getUsername())
                    .openid(target == null ? null : target.getOpenid())
                    .usergroup(target == null ? null : target.getUsergroup())
                    .signName(signName)
                    .templateCode(templateCode)
                    .templateVars(JSON.toJSONString(params))
                    .eval(text)
                    .createTime(DateTime.now()).build();
            getXNotifyLogDao().addLog(log);
            return log;
        };
    }

    protected abstract XNotifyLogDao getXNotifyLogDao();
}

package com.github.bingoohuang.xnotify.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.joda.time.DateTime;

@Data @AllArgsConstructor @NoArgsConstructor @Builder @Accessors(chain = true)
public class XNotifyLog {
    private String logId;
    private String targetId;
    private String mobile;
    private String username;
    private String openid;
    private String usergroup;
    private String msgtype;
    private String signName;
    private String templateCode;
    private String templateVars;
    private String eval;
    private DateTime reqTime;
    private String reqSender;
    private String req;
    private String rsp;
    private String rspId;
    private DateTime rspTime;
    private int state;
    private DateTime createTime;
}

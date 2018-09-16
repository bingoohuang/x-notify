package com.github.bingoohuang.xnotify.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class SmsLog {
    private String logId;
    private String mobile;
    private String signName;
    private String templateCode;
    private String templateVars;
    private String eval;
    private DateTime reqTime;
    private String req;
    private String rsp;
    private String rspId;
    private DateTime rspTime;
    private int state;
    private DateTime createTime;
}

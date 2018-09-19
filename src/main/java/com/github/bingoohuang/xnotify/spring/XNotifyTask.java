package com.github.bingoohuang.xnotify.spring;

import com.github.bingoohuang.delayqueue.TaskItem;
import com.github.bingoohuang.delayqueue.TaskResult;
import com.github.bingoohuang.delayqueue.Taskable;
import com.github.bingoohuang.xnotify.XNotifyLogSender;
import com.github.bingoohuang.xnotify.impl.XNotifyLog;
import com.github.bingoohuang.xnotify.impl.XNotifyLogDao;
import com.github.bingoohuang.xnotify.sender.AliyunSmsSender;
import com.github.bingoohuang.xnotify.sender.TencentYunSmsSender;
import com.github.bingoohuang.xnotify.sender.WxTemplateMsgSender;
import com.github.bingoohuang.xnotify.sender.YunpianSmsSender;
import com.github.bingoohuang.xnotify.util.XNotifyConfig;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class XNotifyTask implements Taskable {
    private @Autowired XNotifyLogDao xNotifyLogDao;
    private @Autowired XNotifyConfig conf;
    private @Autowired WxTemplateMsgSender wxTemplateMsgSender;

    @Override public TaskResult run(TaskItem taskItem) {
        val log = xNotifyLogDao.findLog(taskItem.getTaskId());
        val logSender = createLogSender(log);
        logSender.send(log);

        xNotifyLogDao.finish(log);
        return TaskResult.OK;
    }

    private XNotifyLogSender createLogSender(XNotifyLog log) {
        if ("wx".equals(log.getMsgtype())) {
            return wxTemplateMsgSender;
        }

        if ("sms".equals(log.getMsgtype())) {
            if ("aliyun".equals(log.getReqSender())) {
                val accessKeyId = conf.getConfig("tencent.appKey");
                val accessSecret = conf.getConfig("tencent.sdkAppId");
                return new AliyunSmsSender(accessKeyId, accessSecret);
            }

            if ("yunpian".equals(log.getReqSender())) {
                val apikey = conf.getConfig("yunpian.apikey");
                return new YunpianSmsSender(apikey);
            }

            log.setReqSender("tencent");
            val appKey = conf.getConfig("aliyun.accessKeyId");
            val sdkAppId = conf.getConfig("aliyun.accessSecret");
            return new TencentYunSmsSender(appKey, sdkAppId);
        }

        throw new RuntimeException("unknown msg type " + log.getMsgtype());
    }
}

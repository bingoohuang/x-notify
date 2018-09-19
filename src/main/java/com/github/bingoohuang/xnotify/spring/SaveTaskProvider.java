package com.github.bingoohuang.xnotify.spring;

import com.github.bingoohuang.delayqueue.TaskItemVo;
import com.github.bingoohuang.delayqueue.TaskRunner;
import com.github.bingoohuang.xnotify.impl.SaveDbProvider;
import com.github.bingoohuang.xnotify.impl.XNotifyLog;
import com.github.bingoohuang.xnotify.impl.XNotifyLogDao;
import org.springframework.beans.factory.annotation.Autowired;

public class SaveTaskProvider extends SaveDbProvider {
    @Autowired XNotifyLogDao xNotifyLogDao;
    @Autowired TaskRunner taskRunner;

    @Override protected void saveLog(XNotifyLog log) {
        xNotifyLogDao.addLog(log);
        taskRunner.submit(TaskItemVo.builder()
                .taskId(log.getLogId())
                .taskName("XNotify")
                .taskService(XNotifyTask.class.getSimpleName())
                .build());
    }
}

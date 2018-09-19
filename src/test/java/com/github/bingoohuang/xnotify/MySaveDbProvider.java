package com.github.bingoohuang.xnotify;

import com.github.bingoohuang.xnotify.impl.SaveDbProvider;
import com.github.bingoohuang.xnotify.impl.XNotifyLog;
import lombok.val;
import org.n3r.eql.eqler.EqlerFactory;

public class MySaveDbProvider extends SaveDbProvider {
    @Override protected void saveLog(XNotifyLog log) {
        val dao = EqlerFactory.getEqler(MyXNotifyLogDao.class);
        dao.addLog(log);
    }
}

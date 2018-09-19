package com.github.bingoohuang.xnotify;

import com.github.bingoohuang.xnotify.provider.DbProvider;
import com.github.bingoohuang.xnotify.provider.XNotifyLogDao;
import org.n3r.eql.eqler.EqlerFactory;

public class MyDbProvider extends DbProvider {
    @Override protected XNotifyLogDao getXNotifyLogDao() {
        return EqlerFactory.getEqler(MyXNotifyLogDao.class);
    }
}

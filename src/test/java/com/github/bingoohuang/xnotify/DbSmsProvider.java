package com.github.bingoohuang.xnotify;

import org.n3r.eql.eqler.EqlerFactory;

public class DbSmsProvider extends com.github.bingoohuang.xnotify.smsprovider.DbSmsProvider {
    @Override protected SmsLogDao getSmsLogDao() {
        return EqlerFactory.getEqler(com.github.bingoohuang.xnotify.SmsLogDao.class);
    }
}

package com.github.bingoohuang.xnotify;

import com.github.bingoohuang.xnotify.provider.DbSmsProvider;
import org.n3r.eql.eqler.annotations.Eqler;

@Eqler
public interface SmsLogDao extends DbSmsProvider.SmsLogDao {
}

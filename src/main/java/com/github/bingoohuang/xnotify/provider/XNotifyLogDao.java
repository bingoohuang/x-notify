package com.github.bingoohuang.xnotify.provider;

import com.github.bingoohuang.xnotify.impl.XNotifyLog;
import org.n3r.eql.eqler.annotations.Sql;
import org.n3r.eql.eqler.annotations.SqlOptions;

import java.util.List;

public interface XNotifyLogDao {
    String TABLE = "x_notify_log";
    String FIELDS = "log_id, target_id, mobile, username, openid, usergroup, msgtype, sign_name, template_code, template_vars, " +
            "eval, create_time, req, req_time, req_sender, rsp_id, rsp, rsp_time, state";

    @Sql("insert into " + TABLE + "(" + FIELDS + ")" +
            "values(#logId#, #targetId#, #mobile#, #username#, #openid#, #usergroup#, #msgtype#, #signName#,  #templateCode#, #templateVars#," +
            " #eval#, #createTime#, #req#, #reqTime#, #reqSender#, #rspId#, #rsp#, #rspTime#, #state#)")
    void addLog(XNotifyLog log);

    String SELECT_CLAUSE = "select " + FIELDS + " from " + TABLE;

    @Sql(SELECT_CLAUSE + " where state = 0")
    List<XNotifyLog> queryLogs();

    @Sql(SELECT_CLAUSE + " where log_id = ##")
    XNotifyLog findLog(String logId);

    @SqlOptions("NoWhere")
    @Sql("delete from " + TABLE)
    void clearLogs();
}

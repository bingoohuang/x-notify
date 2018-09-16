package com.github.bingoohuang.xnotify.smsprovider;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.westid.WestId;
import com.github.bingoohuang.xnotify.SmsProvider;
import com.github.bingoohuang.xnotify.SmsSender;
import com.github.bingoohuang.xnotify.impl.SmsLog;
import org.joda.time.DateTime;
import org.n3r.eql.eqler.annotations.Sql;

import java.util.List;

public abstract class DbSmsProvider implements SmsProvider {
    @Override public String getProviderName() {
        return "db";
    }

    @Override public SmsSender getSmsSender() {
        return (mobile, signName, templateCode, params, text) -> {
            SmsLog log = SmsLog.builder()
                    .logId("" + WestId.next())
                    .state(0)
                    .mobile(mobile)
                    .signName(signName)
                    .templateCode(templateCode)
                    .templateVars(JSON.toJSONString(params))
                    .eval(text)
                    .createTime(DateTime.now()).build();
            getSmsLogDao().addSmsLog(log);
            return log;
        };
    }

    protected abstract SmsLogDao getSmsLogDao();


    public interface SmsLogDao {
        @Sql("insert into sms_log(log_id, `mobile`, sign_name, template_code, template_vars, `eval`, create_time, `req`, req_time, rsp_id, `rsp`, rsp_time, `state`)" +
                "values('#logId#', '#mobile#', '#signName#', '#templateCode#', '#templateVars#', '#eval#', '#createTime#', '#req#', '#reqTime#', '#rspId#', '#rsp#', '#rspTime#', '#state#')")
        void addSmsLog(SmsLog smsLog);

        String SELECT_CLAUSE = "select log_id, `mobile`, sign_name, template_code, template_vars, `eval`, create_time, `req`, req_time, rsp_id, `rsp`, rsp_time, `state` " +
                "from sms_log ";

        @Sql(SELECT_CLAUSE)
        List<SmsLog> querySmsLogs();

        @Sql(SELECT_CLAUSE + "where log_id = ##")
        SmsLog findSmsLog(String logId);
    }
}

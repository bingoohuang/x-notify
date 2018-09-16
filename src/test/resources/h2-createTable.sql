DROP TABLE IF EXISTS sms_log;

create table sms_log (
  log_id varchar(30)  comment '主键ID',
  mobile varchar(20) null comment '手机号码',
  sign_name varchar(20) null comment '签名',
  template_code varchar(200) null comment '模板编码',
  template_vars varchar(200) null comment '模板变量JSON',
  eval varchar(200) null comment '短信内容',
  create_time datetime  comment '创建时间',
  req varchar(300) null comment '请求数据',
  req_time datetime NULL comment '请求时间',
  rsp_id varchar(30) null comment '响应ID',
  rsp varchar(300) null comment '响应数据',
  rsp_time datetime NULL comment '响应时间',
  state int not null comment '0 新建，1 发送中，2 已成功 3 已失败',
  primary key(log_id)
);

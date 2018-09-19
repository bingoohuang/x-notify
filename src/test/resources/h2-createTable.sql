DROP TABLE IF EXISTS x_notify_log;

create table x_notify_log (
  log_id varchar(30)  comment '主键ID',
  target_id varchar(20) null comment '目标id, 例如user_id',
  mobile varchar(20) null comment '手机号码',
  username varchar(20) null comment '用户姓名',
  openid varchar(60) null comment '用户姓名',
  usergroup varchar(60) null comment '用户组织',
  msgtype varchar(10) not null comment '消息类型',
  sign_name varchar(20) null comment '签名',
  template_code varchar(200) null comment '模板编码',
  template_vars varchar(200) null comment '模板变量JSON',
  eval varchar(1000) null comment '内容',
  create_time datetime  comment '创建时间',
  req varchar(300) null comment '请求数据',
  req_time datetime NULL comment '请求时间',
  req_sender varchar(20) null comment '发送器',
  rsp_id varchar(30) null comment '响应ID',
  rsp varchar(300) null comment '响应数据',
  rsp_time datetime NULL comment '响应时间',
  state int not null comment '0 新建，1 发送中，2 已成功 3 已失败',
  primary key(log_id)
);

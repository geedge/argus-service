---------------------------------------------
-- Export file for user GK_LOG             --
-- Created by dell on 2016/10/18, 10:08:29 --
---------------------------------------------

spool services_request_log.log

prompt
prompt Creating table SERVICES_REQUEST_LOG
prompt ===================================
prompt
create table GK_LOG.SERVICES_REQUEST_LOG
(
  ID              NUMBER not null,
  OPERATOR        VARCHAR2(200),
  VERSION         VARCHAR2(100) not null,
  OPACTION        INTEGER not null,
  OPTIME          DATE,
  REQUEST_CONTENT CLOB,
  REQUEST_TIME    DATE not null,
  CONSUMER_TIME   NUMBER not null,
  REQUEST_IP      VARCHAR2(200)
)
;
comment on column GK_LOG.SERVICES_REQUEST_LOG.ID
  is '由数据库序列号中SEQ_SERVICES_REQUEST_LOG.nextval生成，主键';
comment on column GK_LOG.SERVICES_REQUEST_LOG.OPERATOR
  is '操作者';
comment on column GK_LOG.SERVICES_REQUEST_LOG.VERSION
  is '版本';
comment on column GK_LOG.SERVICES_REQUEST_LOG.OPACTION
  is '操作';
comment on column GK_LOG.SERVICES_REQUEST_LOG.OPTIME
  is '操作时间（来自服务请求传入）';
comment on column GK_LOG.SERVICES_REQUEST_LOG.REQUEST_CONTENT
  is '请求内容,如果是文件，写文件路径和文件MD5值';
comment on column GK_LOG.SERVICES_REQUEST_LOG.REQUEST_TIME
  is '请求到达服务器时间';
comment on column GK_LOG.SERVICES_REQUEST_LOG.CONSUMER_TIME
  is '服务器处理时间';
comment on column GK_LOG.SERVICES_REQUEST_LOG.REQUEST_IP
  is '请求者的IP';
alter table GK_LOG.SERVICES_REQUEST_LOG
  add constraint PK_SERVICES_REQUEST_LOG primary key (ID);

prompt
prompt Creating sequence SEQ_SERVICES_REQUEST_LOG
prompt ==========================================
prompt
create sequence GK_LOG.SEQ_SERVICES_REQUEST_LOG
minvalue 1
maxvalue 9999999999999999999999999999
start with 157
increment by 1
cache 20;


spool off

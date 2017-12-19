---------------------------------------------
-- Export file for user GK_LOG             --
-- Created by dell on 2014/12/23, 15:43:17 --
---------------------------------------------

spool sqlupdate(20161223)增加日志表.log

prompt
prompt Creating table DF_TUNNEL_RANDOM_LOG
prompt ===================================
prompt
create table DF_TUNNEL_RANDOM_LOG
(
  ID                NUMBER not null,
  CFG_ID            NUMBER not null,
  FOUND_TIME        DATE not null,
  RECV_TIME         DATE not null,
  OVER_ID           NUMBER not null,
  PROTOCOL          VARCHAR2(20) not null,
  SERVER_IP         VARCHAR2(64) not null,
  CLIENT_IP         VARCHAR2(64) not null,
  SERVER_PORT       INTEGER not null,
  CLIENT_PORT       INTEGER not null,
  NEST_PROTOCOL     VARCHAR2(20),
  NEST_SERVER_IP    VARCHAR2(64),
  NEST_CLIENT_IP    VARCHAR2(64),
  NEST_SERVER_PORT  INTEGER,
  NEST_CLIENT_PORT  INTEGER,
  SERVICE_TYPE      INTEGER not null,
  ENTRANCE_ID       NUMBER not null,
  CLJ_IP            VARCHAR2(64) not null,
  SCENE_FILE        VARCHAR2(256),
  ACTION            INTEGER,
  SERVER_LOCATE     VARCHAR2(1024),
  CLIENT_LOCATE     VARCHAR2(1024),
  INJECTED_PKT_FILE VARCHAR2(256),
  NEST_ADDR_LIST    VARCHAR2(1024),
  TUNNEL_NAME       VARCHAR2(1024)
)
;
alter table DF_TUNNEL_RANDOM_LOG
  add constraint PK_DF_TUNNEL_RANDOM_LOG primary key (ID);

prompt
prompt Creating sequence SEQ_DF_TUNNEL_RANDOM_LOG
prompt ==========================================
prompt
create sequence SEQ_DF_TUNNEL_RANDOM_LOG
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;


spool off

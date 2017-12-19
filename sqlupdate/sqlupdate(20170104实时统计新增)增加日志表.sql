-------------------------------------------
-- Export file for user GK_LOG           --
-- Created by dell on 2015/1/4, 11:41:22 --
-------------------------------------------

spool sqlupdate(20170104实时统计新增)增加日志表.log

prompt
prompt Creating table DF_ATTR_TYPE_REPORT
prompt ==================================
prompt
create table DF_ATTR_TYPE_REPORT
(
  STAT_ID     NUMBER not null,
  ACTIVE_SYS  INTEGER not null,
  SERVICE     INTEGER not null,
  ATTR_TYPE   INTEGER not null,
  SUM         NUMBER not null,
  REPORT_TIME DATE not null
)
;
comment on table DF_ATTR_TYPE_REPORT
  is '管控性质实时统计表 该表只在B版日志数据库中';
comment on column DF_ATTR_TYPE_REPORT.ACTIVE_SYS
  is 'A版：4 B版：2 C版：1';
comment on column DF_ATTR_TYPE_REPORT.REPORT_TIME
  is '粒度5分钟';
alter table DF_ATTR_TYPE_REPORT
  add constraint PK_DF_ATTR_TYPE_REPORT primary key (STAT_ID);

prompt
prompt Creating table DF_ENTRANCE_REPORT
prompt =================================
prompt
create table DF_ENTRANCE_REPORT
(
  STAT_ID     NUMBER not null,
  ACTIVE_SYS  INTEGER not null,
  SERVICE     INTEGER not null,
  ENTRANCE_ID NUMBER not null,
  SUM         NUMBER not null,
  REPORT_TIME DATE not null
)
;
comment on table DF_ENTRANCE_REPORT
  is '管控局点实时统计表 该表只在B版日志数据库中';
comment on column DF_ENTRANCE_REPORT.ACTIVE_SYS
  is 'A版：4 B版：2 C版：1';
comment on column DF_ENTRANCE_REPORT.REPORT_TIME
  is '粒度5分钟';
alter table DF_ENTRANCE_REPORT
  add constraint PK_DF_ENTRANCE_REPORT primary key (STAT_ID);

prompt
prompt Creating table DF_LWHH_REPORT
prompt =============================
prompt
create table DF_LWHH_REPORT
(
  STAT_ID     NUMBER not null,
  ACTIVE_SYS  INTEGER not null,
  SERVICE     INTEGER not null,
  LWHH        INTEGER not null,
  SUM         NUMBER not null,
  REPORT_TIME DATE not null
)
;
comment on table DF_LWHH_REPORT
  is '管控来文函号实时统计表 该表只在B版日志数据库中';
comment on column DF_LWHH_REPORT.ACTIVE_SYS
  is 'A版：4 B版：2 C版：1';
comment on column DF_LWHH_REPORT.REPORT_TIME
  is '粒度5分钟';
alter table DF_LWHH_REPORT
  add constraint PK_DF_LWHH_REPORT primary key (STAT_ID);

prompt
prompt Creating table DF_PVT_TAG_REPORT
prompt ================================
prompt
create table DF_PVT_TAG_REPORT
(
  STAT_ID     NUMBER not null,
  ACTIVE_SYS  INTEGER not null,
  SERVICE     INTEGER not null,
  SUM         NUMBER not null,
  REPORT_TIME DATE not null
)
;
comment on table DF_PVT_TAG_REPORT
  is '管控标签实时统计表 该表只在B版日志数据库中';
comment on column DF_PVT_TAG_REPORT.ACTIVE_SYS
  is 'A版：4 B版：2 C版：1';
comment on column DF_PVT_TAG_REPORT.REPORT_TIME
  is '粒度5分钟';
alter table DF_PVT_TAG_REPORT
  add constraint PK_DF_PVT_TAG_REPORT primary key (STAT_ID);

prompt
prompt Creating sequence SEQ_DF_ATTR_TYPE_REPORT
prompt =========================================
prompt
create sequence SEQ_DF_ATTR_TYPE_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DF_ENTRANCE_REPORT
prompt ========================================
prompt
create sequence SEQ_DF_ENTRANCE_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DF_LWHH_REPORT
prompt ====================================
prompt
create sequence SEQ_DF_LWHH_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DF_PVT_TAG_REPORT
prompt =======================================
prompt
create sequence SEQ_DF_PVT_TAG_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;


spool off

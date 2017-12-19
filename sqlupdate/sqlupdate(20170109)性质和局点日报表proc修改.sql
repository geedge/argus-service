------------------------------------------
-- Export file for user GK_LOG          --
-- Created by dell on 2015/1/9, 9:28:18 --
------------------------------------------

spool sqlupdate(20170109)性质和局点日报表proc修改.log

prompt
prompt Creating table DF_ATTR_STAT_LOG_DAILY
prompt =====================================
prompt
create table DF_ATTR_STAT_LOG_DAILY
(
  SEQ        NUMBER not null,
  ATTR_TYPE  INTEGER not null,
  ACTIVE_SYS INTEGER not null,
  SERVICE    INTEGER not null,
  SUM        NUMBER not null,
  STAT_TIME  DATE not null
)
;
alter table DF_ATTR_STAT_LOG_DAILY
  add constraint PK_DF_ATTR_STAT_LOG_DAILY primary key (SEQ);
create index DF_ATTR_STAT_TIME_DAILY on DF_ATTR_STAT_LOG_DAILY (STAT_TIME);

prompt
prompt Creating table DF_ENTR_STAT_LOG_DAILY
prompt =====================================
prompt
create table DF_ENTR_STAT_LOG_DAILY
(
  SEQ         NUMBER not null,
  ENTRANCE_ID INTEGER not null,
  ACTIVE_SYS  INTEGER not null,
  SERVICE     INTEGER not null,
  SUM         NUMBER not null,
  STAT_TIME   DATE not null
)
;
alter table DF_ENTR_STAT_LOG_DAILY
  add constraint PK_DF_ENTR_STAT_LOG_DAILY primary key (SEQ);
create index DF_ENTR_STAT_TIME_DAILY on DF_ENTR_STAT_LOG_DAILY (STAT_TIME);


spool off

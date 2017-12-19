------------------------------------------
-- Export file for user GK_LOG          --
-- Created by dell on 2017/1/8, 9:14:26 --
------------------------------------------

spool sqlupdate(20170105新增报表表结构及存储过程).log

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
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table DF_ATTR_STAT_LOG_DAILY
  add constraint PK_DF_ATTR_STAT_LOG_DAILY primary key (SEQ)
  using index 
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
create index DF_ATTR_STAT_TIME_DAILY on DF_ATTR_STAT_LOG_DAILY (STAT_TIME)
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table DF_ATTR_STAT_LOG_MONTH
prompt =====================================
prompt
create table DF_ATTR_STAT_LOG_MONTH
(
  SEQ        NUMBER not null,
  ATTR_TYPE  INTEGER not null,
  ACTIVE_SYS INTEGER not null,
  SERVICE    INTEGER not null,
  SUM        NUMBER not null,
  STAT_TIME  DATE not null
)
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table DF_ATTR_STAT_LOG_MONTH
  add constraint PK_DF_ATTR_STAT_LOG_MONTH primary key (SEQ)
  using index 
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
create index DF_ATTR_STAT_TIME_MONTH on DF_ATTR_STAT_LOG_MONTH (STAT_TIME)
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

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
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on table DF_ATTR_TYPE_REPORT
  is '管控性质实时统计表 该表只在B版日志数据库中';
comment on column DF_ATTR_TYPE_REPORT.ACTIVE_SYS
  is 'A版：4 B版：2 C版：1';
comment on column DF_ATTR_TYPE_REPORT.REPORT_TIME
  is '粒度5分钟';
alter table DF_ATTR_TYPE_REPORT
  add constraint PK_DF_ATTR_TYPE_REPORT primary key (STAT_ID)
  using index 
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

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
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on table DF_ENTRANCE_REPORT
  is '管控局点实时统计表 该表只在B版日志数据库中';
comment on column DF_ENTRANCE_REPORT.ACTIVE_SYS
  is 'A版：4 B版：2 C版：1';
comment on column DF_ENTRANCE_REPORT.REPORT_TIME
  is '粒度5分钟';
alter table DF_ENTRANCE_REPORT
  add constraint PK_DF_ENTRANCE_REPORT primary key (STAT_ID)
  using index 
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

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
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table DF_ENTR_STAT_LOG_DAILY
  add constraint PK_DF_ENTR_STAT_LOG_DAILY primary key (SEQ)
  using index 
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
create index DF_ENTR_STAT_TIME_DAILY on DF_ENTR_STAT_LOG_DAILY (STAT_TIME)
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table DF_ENTR_STAT_LOG_MONTH
prompt =====================================
prompt
create table DF_ENTR_STAT_LOG_MONTH
(
  SEQ         NUMBER not null,
  ENTRANCE_ID INTEGER not null,
  ACTIVE_SYS  INTEGER not null,
  SERVICE     INTEGER not null,
  SUM         NUMBER not null,
  STAT_TIME   DATE not null
)
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table DF_ENTR_STAT_LOG_MONTH
  add constraint PK_DF_ENTR_STAT_LOG_MONTH primary key (SEQ)
  using index 
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
create index DF_ENTR_STAT_TIME_MONTH on DF_ENTR_STAT_LOG_MONTH (STAT_TIME)
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

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
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on table DF_LWHH_REPORT
  is '管控来文函号实时统计表 该表只在B版日志数据库中';
comment on column DF_LWHH_REPORT.ACTIVE_SYS
  is 'A版：4 B版：2 C版：1';
comment on column DF_LWHH_REPORT.REPORT_TIME
  is '粒度5分钟';
alter table DF_LWHH_REPORT
  add constraint PK_DF_LWHH_REPORT primary key (STAT_ID)
  using index 
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table DF_LWHH_STAT_LOG_DAILY
prompt =====================================
prompt
create table DF_LWHH_STAT_LOG_DAILY
(
  SEQ        NUMBER not null,
  LWHH       INTEGER not null,
  ACTIVE_SYS INTEGER not null,
  SERVICE    INTEGER not null,
  SUM        NUMBER not null,
  STAT_TIME  DATE not null
)
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table DF_LWHH_STAT_LOG_DAILY
  add constraint PK_DF_LWHH_STAT_LOG_DAILY primary key (SEQ)
  using index 
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
create index DF_LWHH_STAT_TIME_DAILY on DF_LWHH_STAT_LOG_DAILY (STAT_TIME)
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table DF_LWHH_STAT_LOG_MONTH
prompt =====================================
prompt
create table DF_LWHH_STAT_LOG_MONTH
(
  SEQ        NUMBER not null,
  LWHH       INTEGER not null,
  ACTIVE_SYS INTEGER not null,
  SERVICE    INTEGER not null,
  SUM        NUMBER not null,
  STAT_TIME  DATE not null
)
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table DF_LWHH_STAT_LOG_MONTH
  add constraint PK_DF_LWHH_STAT_LOG_MONTH primary key (SEQ)
  using index 
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
create index DF_LWHH_STAT_TIME_MONTH on DF_LWHH_STAT_LOG_MONTH (STAT_TIME)
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table DF_STAT_LOG_DAILY
prompt ================================
prompt
create table DF_STAT_LOG_DAILY
(
  SEQ        NUMBER not null,
  CONFIG_ID  NUMBER not null,
  ACTIVE_SYS INTEGER not null,
  SERVICE    INTEGER not null,
  SUM        NUMBER not null,
  STAT_TIME  DATE not null
)
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table DF_STAT_LOG_DAILY
  add constraint PK_DF_STAT_LOG_DAILY primary key (SEQ)
  using index 
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
create index DF_STAT_TIME_DAILY on DF_STAT_LOG_DAILY (STAT_TIME)
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table DF_STAT_LOG_HOUR
prompt ===============================
prompt
create table DF_STAT_LOG_HOUR
(
  SEQ        NUMBER not null,
  CONFIG_ID  NUMBER not null,
  ACTIVE_SYS INTEGER not null,
  SERVICE    INTEGER not null,
  SUM        NUMBER not null,
  STAT_TIME  DATE not null
)
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table DF_STAT_LOG_HOUR
  add constraint PK_DF_STAT_LOG_HOUR primary key (SEQ)
  using index 
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
create index DF_STAT_TIME_HOUR on DF_STAT_LOG_HOUR (STAT_TIME)
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table DF_STAT_LOG_MONTH
prompt ================================
prompt
create table DF_STAT_LOG_MONTH
(
  SEQ        NUMBER not null,
  CONFIG_ID  NUMBER not null,
  ACTIVE_SYS INTEGER not null,
  SERVICE    INTEGER not null,
  SUM        NUMBER not null,
  STAT_TIME  DATE not null
)
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table DF_STAT_LOG_MONTH
  add constraint PK_DF_STAT_LOG_MONTH primary key (SEQ)
  using index 
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
create index DF_STAT_TIME_MONTH on DF_STAT_LOG_MONTH (STAT_TIME)
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table DF_TAG_REPORT
prompt ============================
prompt
create table DF_TAG_REPORT
(
  STAT_ID     NUMBER not null,
  ACTIVE_SYS  INTEGER not null,
  TAG         INTEGER not null,
  SUM         NUMBER not null,
  REPORT_TIME DATE not null,
  SERVICE     INTEGER not null
)
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on table DF_TAG_REPORT
  is '该表只在B版日志数据库中';
comment on column DF_TAG_REPORT.SERVICE
  is '界面私有标签（界面菜单ID）,值小于10000';
alter table DF_TAG_REPORT
  add constraint PK_DF_TAG_REPORT primary key (STAT_ID)
  using index 
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table DF_TAG_STAT_LOG_DAILY
prompt ====================================
prompt
create table DF_TAG_STAT_LOG_DAILY
(
  SEQ        NUMBER not null,
  TAG        INTEGER not null,
  ACTIVE_SYS INTEGER not null,
  SERVICE    INTEGER not null,
  SUM        NUMBER not null,
  STAT_TIME  DATE not null
)
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table DF_TAG_STAT_LOG_DAILY
  add constraint PK_DF_TAG_STAT_LOG_DAILY primary key (SEQ)
  using index 
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
create index DF_TAG_STAT_TIME_DAILY on DF_TAG_STAT_LOG_DAILY (STAT_TIME)
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table DF_TAG_STAT_LOG_MONTH
prompt ====================================
prompt
create table DF_TAG_STAT_LOG_MONTH
(
  SEQ        NUMBER not null,
  TAG        INTEGER not null,
  ACTIVE_SYS INTEGER not null,
  SERVICE    INTEGER not null,
  SUM        NUMBER not null,
  STAT_TIME  DATE not null
)
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table DF_TAG_STAT_LOG_MONTH
  add constraint PK_DF_TAG_STAT_LOG_MONTH primary key (SEQ)
  using index 
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
create index DF_TAG_STAT_TIME_MONTH on DF_TAG_STAT_LOG_MONTH (STAT_TIME)
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating sequence SEQ_DF_ATTR_STAT_LOG_DAILY
prompt ============================================
prompt
create sequence SEQ_DF_ATTR_STAT_LOG_DAILY
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DF_ATTR_STAT_LOG_MONTH
prompt ============================================
prompt
create sequence SEQ_DF_ATTR_STAT_LOG_MONTH
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

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
prompt Creating sequence SEQ_DF_ENTR_STAT_LOG_DAILY
prompt ============================================
prompt
create sequence SEQ_DF_ENTR_STAT_LOG_DAILY
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DF_ENTR_STAT_LOG_MONTH
prompt ============================================
prompt
create sequence SEQ_DF_ENTR_STAT_LOG_MONTH
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
prompt Creating sequence SEQ_DF_LWHH_STAT_LOG_DAILY
prompt ============================================
prompt
create sequence SEQ_DF_LWHH_STAT_LOG_DAILY
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DF_LWHH_STAT_LOG_MONTH
prompt ============================================
prompt
create sequence SEQ_DF_LWHH_STAT_LOG_MONTH
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DF_TAG_REPORT
prompt ===================================
prompt
create sequence SEQ_DF_TAG_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DF_TAG_STAT_LOG_DAILY
prompt ===========================================
prompt
create sequence SEQ_DF_TAG_STAT_LOG_DAILY
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DF_TAG_STAT_LOG_MONTH
prompt ===========================================
prompt
create sequence SEQ_DF_TAG_STAT_LOG_MONTH
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating procedure PRO_ATTR_STAT_DFLOG_DAILY
prompt ============================================
prompt
create or replace procedure PRO_ATTR_STAT_DFLOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_ATTR_STAT_DFLOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_TAG_STAT_LOG_DAILY'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
     RIQI := to_char(trunc((sysdate-NUMDAYS), 'dd'), 'yyyy-MM-dd'); -- 指定天数前的那天日期
     RIQI_S := RIQI || ' 00:00:00'; -- 指定天数前的那天日期00点
     RIQI_E := RIQI || ' 23:59:59'; -- 指定天数前的那天日期23点
     --先删除前一次统计的数据信息,避免累计统计
     delete from DF_ATTR_STAT_LOG_DAILY dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_ATTR_STAT_LOG_DAILY一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DF_ATTR_STAT_LOG_DAILY(SEQ,ACTIVE_SYS, SERVICE,ATTR_TYPE,STAT_TIME,SUM) (select SEQ_DF_ATTR_STAT_LOG_DAILY.nextval, ACTIVE_SYS, SERVICE, ATTR_TYPE, shijian, shuliang from(select ACTIVE_SYS, SERVICE, ATTR_TYPE, trunc(REPORT_TIME, ''DD'') as shijian, sum(SUM) as shuliang from DF_ATTR_REPORT where REPORT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:MI:SS'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:MI:SS'')  group by ACTIVE_SYS, SERVICE,ATTR_TYPE,trunc(REPORT_TIME, ''DD'')))';
     commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_ATTR_STAT_LOG_DAILY一天的日志结束 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
EXCEPTION
     --异常处理机制，记录相关的ora错误号以及相关异常信息
     WHEN OTHERS THEN
     BEGIN
         ROLLBACK;
         v_errortext :=SUBSTR(replace(SQLERRM,'''',''),1,900);
         v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont) VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''||CTYPE||''',''error：'||v_errortext||''')';
         EXECUTE IMMEDIATE v_log_table_sql;
         COMMIT;
     END;
end;
/

prompt
prompt Creating procedure PRO_ATTR_STAT_DFLOG_MONTH
prompt ============================================
prompt
create or replace procedure PRO_ATTR_STAT_DFLOG_MONTH(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_ATTR_STAT_DFLOG_MONTH'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_ATTR_LOG_MONTH all'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    --RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI_S := to_char(trunc((sysdate-NUMDAYS), 'month'), 'yyyy-MM-dd'); -- 前N月月初
    RIQI_E := to_char(last_day(sysdate-NUMDAYS), 'yyyy-mm-dd'); -- 上月月末
     --先删除前一次统计的数据信息,避免累计统计
     delete from DF_ATTR_STAT_LOG_MONTH dd where dd.stat_time=to_date(RIQI_S,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_ATTR_LOG_MONTH一个月的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DF_ATTR_STAT_LOG_MONTH(SEQ,ACTIVE_SYS,SERVICE,ATTR_TYPE,STAT_TIME,SUM) (select SEQ_DF_ATTR_STAT_LOG_MONTH.nextval, ACTIVE_SYS, SERVICE, ATTR_TYPE, shijian, shuliang from(select ACTIVE_SYS,SERVICE,ATTR_TYPE,trunc(STAT_TIME, ''MM'') as shijian,sum(SUM) as shuliang from DF_ATTR_STAT_LOG_DAILY where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd'') and to_date('''||RIQI_E||''',''yyyy-mm-dd'')  group by ACTIVE_SYS,SERVICE,ATTR_TYPE,trunc(STAT_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_ATTR_LOG_MONTH一个月的日志结束 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
EXCEPTION
     --异常处理机制，记录相关的ora错误号以及相关异常信息
     WHEN OTHERS THEN
     BEGIN
         ROLLBACK;
         v_errortext :=SUBSTR(replace(SQLERRM,'''',''),1,900);
         v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont) VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''||CTYPE||''',''error：'||v_errortext||''')';
         EXECUTE IMMEDIATE v_log_table_sql;
         COMMIT;
     END;
end;
/

prompt
prompt Creating procedure PRO_ENTR_STAT_DFLOG_DAILY
prompt ============================================
prompt
create or replace procedure PRO_ENTR_STAT_DFLOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_ENTR_STAT_DFLOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_TAG_STAT_LOG_DAILY'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
     RIQI := to_char(trunc((sysdate-NUMDAYS), 'dd'), 'yyyy-MM-dd'); -- 指定天数前的那天日期
     RIQI_S := RIQI || ' 00:00:00'; -- 指定天数前的那天日期00点
     RIQI_E := RIQI || ' 23:59:59'; -- 指定天数前的那天日期23点
     --先删除前一次统计的数据信息,避免累计统计
     delete from DF_ENTR_STAT_LOG_DAILY dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_ENTR_STAT_LOG_DAILY一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DF_ENTR_STAT_LOG_DAILY(SEQ,ACTIVE_SYS, SERVICE,ENTRANCE_ID,STAT_TIME,SUM) (select SEQ_DF_ENTR_STAT_LOG_DAILY.nextval, ACTIVE_SYS, SERVICE, ENTRANCE_ID, shijian, shuliang from(select ACTIVE_SYS, SERVICE, ENTRANCE_ID, trunc(REPORT_TIME, ''DD'') as shijian, sum(SUM) as shuliang from DF_ENTR_REPORT where REPORT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:MI:SS'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:MI:SS'')  group by ACTIVE_SYS, SERVICE,ENTRANCE_ID,trunc(REPORT_TIME, ''DD'')))';
     commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_ENTR_STAT_LOG_DAILY一天的日志结束 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
EXCEPTION
     --异常处理机制，记录相关的ora错误号以及相关异常信息
     WHEN OTHERS THEN
     BEGIN
         ROLLBACK;
         v_errortext :=SUBSTR(replace(SQLERRM,'''',''),1,900);
         v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont) VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''||CTYPE||''',''error：'||v_errortext||''')';
         EXECUTE IMMEDIATE v_log_table_sql;
         COMMIT;
     END;
end;
/

prompt
prompt Creating procedure PRO_ENTR_STAT_DFLOG_MONTH
prompt ============================================
prompt
create or replace procedure PRO_ENTR_STAT_DFLOG_MONTH(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_ENTR_STAT_DFLOG_MONTH'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_ENTR_LOG_MONTH all'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    --RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI_S := to_char(trunc((sysdate-NUMDAYS), 'month'), 'yyyy-MM-dd'); -- 前N月月初
    RIQI_E := to_char(last_day(sysdate-NUMDAYS), 'yyyy-mm-dd'); -- 上月月末
     --先删除前一次统计的数据信息,避免累计统计
     delete from DF_ENTR_STAT_LOG_MONTH dd where dd.stat_time=to_date(RIQI_S,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_ENTR_LOG_MONTH一个月的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DF_ENTR_STAT_LOG_MONTH(SEQ,ACTIVE_SYS,SERVICE,ENTRANCE_ID,STAT_TIME,SUM) (select SEQ_DF_ENTR_STAT_LOG_MONTH.nextval, ACTIVE_SYS, SERVICE, ENTRANCE_ID, shijian, shuliang from(select ACTIVE_SYS,SERVICE,ENTRANCE_ID,trunc(STAT_TIME, ''MM'') as shijian,sum(SUM) as shuliang from DF_ENTR_STAT_LOG_DAILY where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd'') and to_date('''||RIQI_E||''',''yyyy-mm-dd'')  group by ACTIVE_SYS,SERVICE,ENTRANCE_ID,trunc(STAT_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_ENTR_LOG_MONTH一个月的日志结束 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
EXCEPTION
     --异常处理机制，记录相关的ora错误号以及相关异常信息
     WHEN OTHERS THEN
     BEGIN
         ROLLBACK;
         v_errortext :=SUBSTR(replace(SQLERRM,'''',''),1,900);
         v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont) VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''||CTYPE||''',''error：'||v_errortext||''')';
         EXECUTE IMMEDIATE v_log_table_sql;
         COMMIT;
     END;
end;
/

prompt
prompt Creating procedure PRO_LWHH_STAT_DFLOG_DAILY
prompt ============================================
prompt
create or replace procedure PRO_LWHH_STAT_DFLOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_LWHH_STAT_DFLOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_TAG_STAT_LOG_DAILY'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
     RIQI := to_char(trunc((sysdate-NUMDAYS), 'dd'), 'yyyy-MM-dd'); -- 指定天数前的那天日期
     RIQI_S := RIQI || ' 00:00:00'; -- 指定天数前的那天日期00点
     RIQI_E := RIQI || ' 23:59:59'; -- 指定天数前的那天日期23点
     --先删除前一次统计的数据信息,避免累计统计
     delete from DF_LWHH_STAT_LOG_DAILY dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_LWHH_STAT_LOG_DAILY一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DF_LWHH_STAT_LOG_DAILY(SEQ,ACTIVE_SYS, SERVICE,LWHH,STAT_TIME,SUM) (select SEQ_DF_LWHH_STAT_LOG_DAILY.nextval, ACTIVE_SYS, SERVICE, LWHH, shijian, shuliang from(select ACTIVE_SYS, SERVICE, LWHH, trunc(REPORT_TIME, ''DD'') as shijian, sum(SUM) as shuliang from DF_LWHH_REPORT where REPORT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:MI:SS'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:MI:SS'')  group by ACTIVE_SYS, SERVICE,LWHH,trunc(REPORT_TIME, ''DD'')))';
     commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_LWHH_STAT_LOG_DAILY一天的日志结束 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
EXCEPTION
     --异常处理机制，记录相关的ora错误号以及相关异常信息
     WHEN OTHERS THEN
     BEGIN
         ROLLBACK;
         v_errortext :=SUBSTR(replace(SQLERRM,'''',''),1,900);
         v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont) VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''||CTYPE||''',''error：'||v_errortext||''')';
         EXECUTE IMMEDIATE v_log_table_sql;
         COMMIT;
     END;
end;
/

prompt
prompt Creating procedure PRO_LWHH_STAT_DFLOG_MONTH
prompt ============================================
prompt
create or replace procedure PRO_LWHH_STAT_DFLOG_MONTH(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_LWHH_STAT_DFLOG_MONTH'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_LWHH_LOG_MONTH all'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    --RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI_S := to_char(trunc((sysdate-NUMDAYS), 'month'), 'yyyy-MM-dd'); -- 前N月月初
    RIQI_E := to_char(last_day(sysdate-NUMDAYS), 'yyyy-mm-dd'); -- 上月月末
     --先删除前一次统计的数据信息,避免累计统计
     delete from DF_LWHH_STAT_LOG_MONTH dd where dd.stat_time=to_date(RIQI_S,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_LWHH_LOG_MONTH一个月的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DF_LWHH_STAT_LOG_MONTH(SEQ,ACTIVE_SYS,SERVICE,LWHH,STAT_TIME,SUM) (select SEQ_DF_LWHH_STAT_LOG_MONTH.nextval, ACTIVE_SYS, SERVICE, LWHH, shijian, shuliang from(select ACTIVE_SYS,SERVICE,LWHH,trunc(STAT_TIME, ''MM'') as shijian,sum(SUM) as shuliang from DF_LWHH_STAT_LOG_DAILY where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd'') and to_date('''||RIQI_E||''',''yyyy-mm-dd'')  group by ACTIVE_SYS,SERVICE,LWHH,trunc(STAT_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_LWHH_LOG_MONTH一个月的日志结束 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
EXCEPTION
     --异常处理机制，记录相关的ora错误号以及相关异常信息
     WHEN OTHERS THEN
     BEGIN
         ROLLBACK;
         v_errortext :=SUBSTR(replace(SQLERRM,'''',''),1,900);
         v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont) VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''||CTYPE||''',''error：'||v_errortext||''')';
         EXECUTE IMMEDIATE v_log_table_sql;
         COMMIT;
     END;
end;
/

prompt
prompt Creating procedure PRO_TAG_STAT_DFLOG_DAILY
prompt ===========================================
prompt
create or replace procedure PRO_TAG_STAT_DFLOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_TAG_STAT_DFLOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_TAG_STAT_LOG_DAILY'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
     RIQI := to_char(trunc((sysdate-NUMDAYS), 'dd'), 'yyyy-MM-dd'); -- 指定天数前的那天日期
     RIQI_S := RIQI || ' 00:00:00'; -- 指定天数前的那天日期00点
     RIQI_E := RIQI || ' 23:59:59'; -- 指定天数前的那天日期23点
     --先删除前一次统计的数据信息,避免累计统计
     delete from DF_TAG_STAT_LOG_DAILY dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_TAG_STAT_LOG_DAILY一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DF_TAG_STAT_LOG_DAILY(SEQ,ACTIVE_SYS, SERVICE,TAG,STAT_TIME,SUM) (select SEQ_DF_TAG_STAT_LOG_DAILY.nextval, ACTIVE_SYS, SERVICE, TAG, shijian, shuliang from(select ACTIVE_SYS, SERVICE, TAG, trunc(REPORT_TIME, ''DD'') as shijian, sum(SUM) as shuliang from DF_TAG_REPORT where REPORT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:MI:SS'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:MI:SS'')  group by ACTIVE_SYS, SERVICE,TAG,trunc(REPORT_TIME, ''DD'')))';
     commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_TAG_STAT_LOG_DAILY一天的日志结束 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
EXCEPTION
     --异常处理机制，记录相关的ora错误号以及相关异常信息
     WHEN OTHERS THEN
     BEGIN
         ROLLBACK;
         v_errortext :=SUBSTR(replace(SQLERRM,'''',''),1,900);
         v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont) VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''||CTYPE||''',''error：'||v_errortext||''')';
         EXECUTE IMMEDIATE v_log_table_sql;
         COMMIT;
     END;
end;
/

prompt
prompt Creating procedure PRO_TAG_STAT_DFLOG_MONTH
prompt ===========================================
prompt
create or replace procedure PRO_TAG_STAT_DFLOG_MONTH(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_TAG_STAT_DFLOG_MONTH'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_TAG_LOG_MONTH all'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    --RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI_S := to_char(trunc((sysdate-NUMDAYS), 'month'), 'yyyy-MM-dd'); -- 前N月月初
    RIQI_E := to_char(last_day(sysdate-NUMDAYS), 'yyyy-mm-dd'); -- 上月月末
     --先删除前一次统计的数据信息,避免累计统计
     delete from DF_TAG_STAT_LOG_MONTH dd where dd.stat_time=to_date(RIQI_S,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_TAG_LOG_MONTH一个月的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DF_TAG_STAT_LOG_MONTH(SEQ,ACTIVE_SYS,SERVICE,TAG,STAT_TIME,SUM) (select SEQ_DF_TAG_STAT_LOG_MONTH.nextval, ACTIVE_SYS, SERVICE, TAG, shijian, shuliang from(select ACTIVE_SYS,SERVICE,TAG,trunc(STAT_TIME, ''MM'') as shijian,sum(SUM) as shuliang from DF_TAG_STAT_LOG_DAILY where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd'') and to_date('''||RIQI_E||''',''yyyy-mm-dd'')  group by ACTIVE_SYS,SERVICE,TAG,trunc(STAT_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_TAG_LOG_MONTH一个月的日志结束 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
EXCEPTION
     --异常处理机制，记录相关的ora错误号以及相关异常信息
     WHEN OTHERS THEN
     BEGIN
         ROLLBACK;
         v_errortext :=SUBSTR(replace(SQLERRM,'''',''),1,900);
         v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont) VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''||CTYPE||''',''error：'||v_errortext||''')';
         EXECUTE IMMEDIATE v_log_table_sql;
         COMMIT;
     END;
end;
/


spool off

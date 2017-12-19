GK_LOG库
drop table DF_PZ_REPORT;
-- Create table
create table DF_PZ_REPORT
(
  STAT_ID     NUMBER not null,
  ACTIVE_SYS  INTEGER not null,
  CFG_ID      NUMBER not null,
  SUM         NUMBER not null,
  REPORT_TIME DATE not null,
  SERVICE     INTEGER default 0 not null
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
-- Add comments to the table 
comment on table DF_PZ_REPORT
  is '管控配置实时统计表 该表只在B版日志数据库中';
-- Add comments to the columns 
comment on column DF_PZ_REPORT.ACTIVE_SYS
  is 'A版：4
B版：2
C版：1
';
comment on column DF_PZ_REPORT.REPORT_TIME
  is '粒度5分钟';
-- Create/Recreate primary, unique and foreign key constraints 
alter table DF_PZ_REPORT
  add constraint PK_DF_PZ_REPORT primary key (STAT_ID)
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
--新增监测配置实时报表
-- Create table
create table DJ_PZ_REPORT
(
  STAT_ID     NUMBER not null,
  ACTIVE_SYS  INTEGER not null,
  CFG_ID      NUMBER not null,
  SUM         NUMBER not null,
  REPORT_TIME DATE not null,
  SERVICE     INTEGER default 0 not null
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
-- Add comments to the table 
comment on table DJ_PZ_REPORT
  is '管控配置实时统计表 该表只在B版日志数据库中';
-- Add comments to the columns 
comment on column DJ_PZ_REPORT.ACTIVE_SYS
  is 'A版：4
B版：2
C版：1
';
comment on column DJ_PZ_REPORT.REPORT_TIME
  is '粒度5分钟';
-- Create/Recreate primary, unique and foreign key constraints 
alter table DJ_PZ_REPORT
  add constraint PK_DJ_PZ_REPORT primary key (STAT_ID)
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

-- Create sequence 
create sequence SEQ_DJ_PZ_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

-- Create sequence 
create sequence SEQ_DF_STAT_LOG_HOUR	
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;
-- Create sequence 
create sequence SEQ_DJ_STAT_LOG_HOUR
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;
-- Create sequence 
create sequence SEQ_DJ_STAT_LOG_DAILY
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;
-- Create sequence 
create sequence SEQ_DJ_STAT_LOG_MONTH
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

先清理原来存储过程；小时统计，每小时每延迟6分钟进行统计
---------------------------------------------
-- Export file for user GK_LOG             --
-- Created by dell on 2016/11/22, 14:28:39 --
---------------------------------------------

spool 报表表结构及存储过程.log

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
prompt Creating table DJ_STAT_LOG_DAILY
prompt ================================
prompt
create table DJ_STAT_LOG_DAILY
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
alter table DJ_STAT_LOG_DAILY
  add constraint PK_DJ_STAT_LOG_DAILY primary key (SEQ)
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
create index DJ_STAT_TIME_DAILY on DJ_STAT_LOG_DAILY (STAT_TIME)
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
prompt Creating table DJ_STAT_LOG_HOUR
prompt ===============================
prompt
create table DJ_STAT_LOG_HOUR
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
alter table DJ_STAT_LOG_HOUR
  add constraint PK_DJ_STAT_LOG_HOUR primary key (SEQ)
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
create index DJ_STAT_TIME_HOUR on DJ_STAT_LOG_HOUR (STAT_TIME)
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
prompt Creating table DJ_STAT_LOG_MONTH
prompt ================================
prompt
create table DJ_STAT_LOG_MONTH
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
alter table DJ_STAT_LOG_MONTH
  add constraint PK_DJ_STAT_LOG_MONTH primary key (SEQ)
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
create index DJ_STAT_TIME_MONTH on DJ_STAT_LOG_MONTH (STAT_TIME)
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
prompt Creating procedure PRO_STAT_DFLOG_DAILY
prompt =======================================
prompt
create or replace procedure PRO_STAT_DFLOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DFSTAT_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_LOG_DAILY all'; --当前存储过程统计业务类型
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
     delete from DF_STAT_LOG_DAILY dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_LOG_DAILY一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DF_STAT_LOG_DAILY(SEQ,ACTIVE_SYS,SERVICE,CONFIG_ID,STAT_TIME,SUM) (select SEQ_DF_STAT_LOG_DAILY.nextval, ACTIVE_SYS, SERVICE, CONFIG_ID, shijian, shuliang from(select ACTIVE_SYS, SERVICE, CONFIG_ID,trunc(STAT_TIME, ''DD'') as shijian,sum(SUM) as shuliang from DF_STAT_LOG_HOUR where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:mi:ss'')  group by ACTIVE_SYS,SERVICE,CONFIG_ID,trunc(STAT_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_LOG一天的日志结束 end'')';
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
prompt Creating procedure PRO_STAT_DFLOG_HOUR
prompt ======================================
prompt
create or replace procedure PRO_STAT_DFLOG_HOUR(NUMHOURS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DFSTAT_LOG_HOUR'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_STAT_LOG_HOUR all'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMHOURS/24,'yyyy-mm-dd HH24');--指定小时前的那个小时
    RIQI_S := RIQI || ':00:00';
    RIQI_E := RIQI || ':59:59';
     --先删除前一次统计的数据信息,避免累计统计
     delete from DF_STAT_LOG_HOUR dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD HH24');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_LOG_HOUR一小时的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DF_STAT_LOG_HOUR(SEQ,ACTIVE_SYS, SERVICE,CONFIG_ID,STAT_TIME,SUM) (select SEQ_DF_STAT_LOG_DAILY.nextval, ACTIVE_SYS, SERVICE, CFG_ID, shijian, shuliang from(select ACTIVE_SYS, SERVICE, CFG_ID, trunc(REPORT_TIME, ''HH'') as shijian, sum(SUM) as shuliang from DF_PZ_REPORT where REPORT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:MI:SS'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:MI:SS'')  group by ACTIVE_SYS, SERVICE,CFG_ID,trunc(REPORT_TIME, ''HH'')))';
     commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_LOG_HOUR一小时的日志结束 end'')';
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
prompt Creating procedure PRO_STAT_DFLOG_MONTH
prompt =======================================
prompt
create or replace procedure PRO_STAT_DFLOG_MONTH(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_STAT_DFLOG_MONTH'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_LOG_MONTH all'; --当前存储过程统计业务类型
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
     delete from DF_STAT_LOG_MONTH dd where dd.stat_time=to_date(RIQI_S,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_LOG_MONTH一个月的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DF_STAT_LOG_MONTH(SEQ,ACTIVE_SYS,SERVICE,CONFIG_ID,STAT_TIME,SUM) (select SEQ_DF_STAT_LOG_MONTH.nextval, ACTIVE_SYS, SERVICE, CONFIG_ID, shijian, shuliang from(select ACTIVE_SYS,SERVICE,CONFIG_ID,trunc(STAT_TIME, ''MM'') as shijian,sum(SUM) as shuliang from DF_STAT_LOG_DAILY where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd'') and to_date('''||RIQI_E||''',''yyyy-mm-dd'')  group by ACTIVE_SYS,SERVICE,CONFIG_ID,trunc(STAT_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_LOG_MONTH一个月的日志结束 end'')';
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
prompt Creating procedure PRO_STAT_DJLOG_DAILY
prompt =======================================
prompt
create or replace procedure PRO_STAT_DJLOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DJSTAT_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_LOG_DAILY all'; --当前存储过程统计业务类型
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
     delete from DJ_STAT_LOG_DAILY dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_LOG_DAILY一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DJ_STAT_LOG_DAILY(SEQ,ACTIVE_SYS,SERVICE,CONFIG_ID,STAT_TIME,SUM) (select SEQ_DJ_STAT_LOG_DAILY.nextval, ACTIVE_SYS, SERVICE, CONFIG_ID, shijian, shuliang from(select ACTIVE_SYS, SERVICE, CONFIG_ID,trunc(STAT_TIME, ''DD'') as shijian,sum(SUM) as shuliang from DJ_STAT_LOG_HOUR where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:mi:ss'')  group by ACTIVE_SYS,SERVICE,CONFIG_ID,trunc(STAT_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_LOG一天的日志结束 end'')';
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
prompt Creating procedure PRO_STAT_DJLOG_HOUR
prompt ======================================
prompt
create or replace procedure PRO_STAT_DJLOG_HOUR(NUMHOURS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DJSTAT_LOG_HOUR'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_STAT_LOG_HOUR all'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMHOURS/24,'yyyy-mm-dd HH24');--指定小时前的那个小时
    RIQI_S := RIQI || ':00:00';
    RIQI_E := RIQI || ':59:59';
     --先删除前一次统计的数据信息,避免累计统计
     delete from DJ_STAT_LOG_HOUR dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD HH24');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_LOG_HOUR一小时的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DJ_STAT_LOG_HOUR(SEQ,ACTIVE_SYS, SERVICE,CONFIG_ID,STAT_TIME,SUM) (select SEQ_DJ_STAT_LOG_DAILY.nextval, ACTIVE_SYS, SERVICE, CFG_ID, shijian, shuliang from(select ACTIVE_SYS, SERVICE, CFG_ID, trunc(REPORT_TIME, ''HH'') as shijian, sum(SUM) as shuliang from DJ_PZ_REPORT where REPORT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:MI:SS'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:MI:SS'')  group by ACTIVE_SYS, SERVICE,CFG_ID,trunc(REPORT_TIME, ''HH'')))';
     commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_LOG_HOUR一小时的日志结束 end'')';
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
prompt Creating procedure PRO_STAT_DJLOG_MONTH
prompt =======================================
prompt
create or replace procedure PRO_STAT_DJLOG_MONTH(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_STAT_DJLOG_MONTH'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_LOG_MONTH all'; --当前存储过程统计业务类型
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
     delete from DJ_STAT_LOG_MONTH dd where dd.stat_time=to_date(RIQI_S,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_LOG_MONTH一个月的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DJ_STAT_LOG_MONTH(SEQ,ACTIVE_SYS,SERVICE,CONFIG_ID,STAT_TIME,SUM) (select SEQ_DF_STAT_LOG_MONTH.nextval, ACTIVE_SYS, SERVICE, CONFIG_ID, shijian, shuliang from(select ACTIVE_SYS,SERVICE,CONFIG_ID,trunc(STAT_TIME, ''MM'') as shijian,sum(SUM) as shuliang from DJ_STAT_LOG_DAILY where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd'') and to_date('''||RIQI_E||''',''yyyy-mm-dd'')  group by ACTIVE_SYS,SERVICE,CONFIG_ID,trunc(STAT_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_LOG_MONTH一个月的日志结束 end'')';
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

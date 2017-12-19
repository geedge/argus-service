------------------------------------------
-- Export file for user GK_LOG          --
-- Created by dell on 2017/1/8, 9:17:11 --
------------------------------------------

spool sqlupdate(20170106新增报表表结构及存储过程).log

prompt
prompt Creating table DF_DESTIP_STAT_LOG_DAILY
prompt =======================================
prompt
create table DF_DESTIP_STAT_LOG_DAILY
(
  SEQ          NUMBER not null,
  DEST_COUNTRY VARCHAR2(256) not null,
  ACTIVE_SYS   INTEGER not null,
  SERVICE      INTEGER not null,
  SUM          NUMBER not null,
  STAT_TIME    DATE not null
)
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 16K
    next 8K
    minextents 1
    maxextents unlimited
  );
alter table DF_DESTIP_STAT_LOG_DAILY
  add constraint PK_DF_DESTIP_STAT_LOG_DAILY primary key (SEQ)
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
create index DF_DESTIP_STAT_TIME_DAILY on DF_DESTIP_STAT_LOG_DAILY (STAT_TIME)
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
prompt Creating table DF_DESTIP_STAT_LOG_MONTH
prompt =======================================
prompt
create table DF_DESTIP_STAT_LOG_MONTH
(
  SEQ          NUMBER not null,
  DEST_COUNTRY VARCHAR2(256) not null,
  ACTIVE_SYS   INTEGER not null,
  SERVICE      INTEGER not null,
  SUM          NUMBER not null,
  STAT_TIME    DATE not null
)
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 16K
    next 8K
    minextents 1
    maxextents unlimited
  );
alter table DF_DESTIP_STAT_LOG_MONTH
  add constraint PK_DF_DESTIP_STAT_LOG_MONTH primary key (SEQ)
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
create index DF_DESTIP_STAT_TIME_MONTH on DF_DESTIP_STAT_LOG_MONTH (STAT_TIME)
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
prompt Creating table DF_SRCIP_STAT_LOG_DAILY
prompt ======================================
prompt
create table DF_SRCIP_STAT_LOG_DAILY
(
  SEQ          NUMBER not null,
  SRC_PROVINCE VARCHAR2(256) not null,
  SRC_CITY     VARCHAR2(256),
  ACTIVE_SYS   INTEGER not null,
  SERVICE      INTEGER not null,
  SUM          NUMBER not null,
  STAT_TIME    DATE not null
)
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 16K
    next 8K
    minextents 1
    maxextents unlimited
  );
alter table DF_SRCIP_STAT_LOG_DAILY
  add constraint PK_DF_SRCIP_STAT_LOG_DAILY primary key (SEQ)
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
create index DF_SRCIP_STAT_LOG_DAILY on DF_SRCIP_STAT_LOG_DAILY (STAT_TIME)
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
prompt Creating table DF_SRCIP_STAT_LOG_MONTH
prompt ======================================
prompt
create table DF_SRCIP_STAT_LOG_MONTH
(
  SEQ          NUMBER not null,
  SRC_PROVINCE VARCHAR2(256) not null,
  SRC_CITY     VARCHAR2(256),
  ACTIVE_SYS   INTEGER not null,
  SERVICE      INTEGER not null,
  SUM          NUMBER not null,
  STAT_TIME    DATE not null
)
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 16K
    next 8K
    minextents 1
    maxextents unlimited
  );
alter table DF_SRCIP_STAT_LOG_MONTH
  add constraint PK_DF_SRCIP_STAT_LOG_MONTH primary key (SEQ)
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
create index DF_SRCIP_STAT_TIME_MONTH on DF_SRCIP_STAT_LOG_MONTH (STAT_TIME)
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
prompt Creating sequence SEQ_DF_DESTIP_STAT_LOG_DAILY
prompt ==============================================
prompt
create sequence SEQ_DF_DESTIP_STAT_LOG_DAILY
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DF_DESTIP_STAT_LOG_MONTH
prompt ==============================================
prompt
create sequence SEQ_DF_DESTIP_STAT_LOG_MONTH
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DF_SRCIP_STAT_LOG_DAILY
prompt =============================================
prompt
create sequence SEQ_DF_SRCIP_STAT_LOG_DAILY
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DF_SRCIP_STAT_LOG_MONTH
prompt =============================================
prompt
create sequence SEQ_DF_SRCIP_STAT_LOG_MONTH
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating procedure PRO_DESTIP_STAT_DFLOG_DAILY
prompt ==============================================
prompt
create or replace procedure PRO_DESTIP_STAT_DFLOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DESTIP_STAT_DFLOG_DAILY'; --当前存储过程的名称
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
     delete from DF_DESTIP_STAT_LOG_DAILY dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_DESTIP_STAT_LOG_DAILY一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DF_DESTIP_STAT_LOG_DAILY(SEQ,ACTIVE_SYS, SERVICE,DEST_COUNTRY,STAT_TIME,SUM) (select SEQ_DF_DESTIP_STAT_LOG_DAILY.nextval, ACTIVE_SYS, SERVICE, DEST_COUNTRY, shijian, shuliang from(select ACTIVE_SYS, SERVICE, DEST_COUNTRY, trunc(REPORT_TIME, ''DD'') as shijian, sum(SUM) as shuliang from DF_DESTIP_REPORT where REPORT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:MI:SS'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:MI:SS'')  group by ACTIVE_SYS, SERVICE,DEST_COUNTRY,trunc(REPORT_TIME, ''DD'')))';
     commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_DESTIP_STAT_LOG_DAILY一天的日志结束 end'')';
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
prompt Creating procedure PRO_DESTIP_STAT_DFLOG_MONTH
prompt ==============================================
prompt
create or replace procedure PRO_DESTIP_STAT_DFLOG_MONTH(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DESTIP_STAT_DFLOG_MONTH'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_DESTIP_LOG_MONTH all'; --当前存储过程统计业务类型
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
     delete from DF_DESTIP_STAT_LOG_MONTH dd where dd.stat_time=to_date(RIQI_S,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_DESTIP_LOG_MONTH一个月的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DF_DESTIP_STAT_LOG_MONTH(SEQ,ACTIVE_SYS,SERVICE,DEST_COUNTRY,STAT_TIME,SUM) (select SEQ_DF_DESTIP_STAT_LOG_MONTH.nextval, ACTIVE_SYS, SERVICE, DEST_COUNTRY, shijian, shuliang from(select ACTIVE_SYS,SERVICE,DEST_COUNTRY,trunc(STAT_TIME, ''MM'') as shijian,sum(SUM) as shuliang from DF_DESTIP_STAT_LOG_DAILY where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd'') and to_date('''||RIQI_E||''',''yyyy-mm-dd'')  group by ACTIVE_SYS,SERVICE,DEST_COUNTRY,trunc(STAT_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_DESTIP_LOG_MONTH一个月的日志结束 end'')';
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
prompt Creating procedure PRO_SRCIP_STAT_DFLOG_DAILY
prompt =============================================
prompt
create or replace procedure PRO_SRCIP_STAT_DFLOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_SRCIP_STAT_DFLOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_SRCIP_STAT_LOG_DAILY'; --当前存储过程统计业务类型
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
     delete from DF_SRCIP_STAT_LOG_DAILY dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_SRCIP_STAT_LOG_DAILY一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DF_SRCIP_STAT_LOG_DAILY(SEQ,ACTIVE_SYS, SERVICE,SRC_PROVINCE,SRC_CITY,STAT_TIME,SUM) (select SEQ_DF_SRCIP_STAT_LOG_DAILY.nextval, ACTIVE_SYS, SERVICE, SRC_PROVINCE,SRC_CITY, shijian, shuliang from(select ACTIVE_SYS, SERVICE, SRC_PROVINCE,SRC_CITY, trunc(REPORT_TIME, ''DD'') as shijian, sum(SUM) as shuliang from DF_SRCIP_REPORT where REPORT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:MI:SS'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:MI:SS'')  group by ACTIVE_SYS, SERVICE,SRC_PROVINCE,SRC_CITY,trunc(REPORT_TIME, ''DD'')))';
     commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_SRCIP_STAT_LOG_DAILY一天的日志结束 end'')';
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
prompt Creating procedure PRO_SRCIP_STAT_DFLOG_MONTH
prompt =============================================
prompt
create or replace procedure PRO_SRCIP_STAT_DFLOG_MONTH(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_SRCIP_STAT_DFLOG_MONTH'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_SRCIP_LOG_MONTH all'; --当前存储过程统计业务类型
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
     delete from DF_SRCIP_STAT_LOG_MONTH dd where dd.stat_time=to_date(RIQI_S,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_SRCIP_LOG_MONTH一个月的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DF_SRCIP_STAT_LOG_MONTH(SEQ,ACTIVE_SYS,SERVICE,SRC_PROVINCE,SRC_CITY,STAT_TIME,SUM) (select SEQ_DF_SRCIP_STAT_LOG_MONTH.nextval, ACTIVE_SYS, SERVICE, SRC_PROVINCE,SRC_CITY, shijian, shuliang from(select ACTIVE_SYS,SERVICE,SRC_PROVINCE,SRC_CITY,trunc(STAT_TIME, ''MM'') as shijian,sum(SUM) as shuliang from DF_SRCIP_STAT_LOG_DAILY where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd'') and to_date('''||RIQI_E||''',''yyyy-mm-dd'')  group by ACTIVE_SYS,SERVICE,SRC_PROVINCE,SRC_CITY,trunc(STAT_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_SRCIP_LOG_MONTH一个月的日志结束 end'')';
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

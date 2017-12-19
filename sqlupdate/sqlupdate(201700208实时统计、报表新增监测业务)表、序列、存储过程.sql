--------------------------------------------
-- Export file for user GK_LOG            --
-- Created by dell on 2015/3/16, 11:17:23 --
--------------------------------------------

spool sqlupdate(201700208实时统计、报表新增监测业务)表、序列、存储过程.log

prompt
prompt Creating table DJ_ATTR_STAT_LOG_DAILY
prompt =====================================
prompt
create table DJ_ATTR_STAT_LOG_DAILY
(
  SEQ        NUMBER not null,
  ATTR_TYPE  INTEGER not null,
  ACTIVE_SYS INTEGER not null,
  SERVICE    INTEGER not null,
  SUM        NUMBER not null,
  STAT_TIME  DATE not null
)
;
alter table DJ_ATTR_STAT_LOG_DAILY
  add constraint PK_DJ_ATTR_STAT_LOG_DAILY primary key (SEQ);
create index DJ_ATTR_STAT_TIME_DAILY on DJ_ATTR_STAT_LOG_DAILY (STAT_TIME);

prompt
prompt Creating table DJ_ATTR_STAT_LOG_MONTH
prompt =====================================
prompt
create table DJ_ATTR_STAT_LOG_MONTH
(
  SEQ        NUMBER not null,
  ATTR_TYPE  INTEGER not null,
  ACTIVE_SYS INTEGER not null,
  SERVICE    INTEGER not null,
  SUM        NUMBER not null,
  STAT_TIME  DATE not null
)
;
alter table DJ_ATTR_STAT_LOG_MONTH
  add constraint PK_DJ_ATTR_STAT_LOG_MONTH primary key (SEQ);
create index DJ_ATTR_STAT_TIME_MONTH on DJ_ATTR_STAT_LOG_MONTH (STAT_TIME);

prompt
prompt Creating table DJ_ATTR_TYPE_REPORT
prompt ==================================
prompt
create table DJ_ATTR_TYPE_REPORT
(
  STAT_ID     NUMBER not null,
  ACTIVE_SYS  INTEGER not null,
  SERVICE     INTEGER not null,
  ATTR_TYPE   INTEGER not null,
  SUM         NUMBER not null,
  REPORT_TIME DATE not null
)
;
comment on table DJ_ATTR_TYPE_REPORT
  is '管控性质实时统计表 该表只在B版日志数据库中';
comment on column DJ_ATTR_TYPE_REPORT.ACTIVE_SYS
  is 'A版：4 B版：2 C版：1';
comment on column DJ_ATTR_TYPE_REPORT.REPORT_TIME
  is '粒度5分钟';
alter table DJ_ATTR_TYPE_REPORT
  add constraint PK_DJ_ATTR_TYPE_REPORT primary key (STAT_ID);

prompt
prompt Creating table DJ_DESTIP_COUNTRY_REPORT
prompt =======================================
prompt
create table DJ_DESTIP_COUNTRY_REPORT
(
  STAT_ID      NUMBER not null,
  ACTIVE_SYS   INTEGER not null,
  DEST_COUNTRY VARCHAR2(256) not null,
  SUM          NUMBER not null,
  REPORT_TIME  DATE not null,
  SERVICE      INTEGER not null
)
;
comment on table DJ_DESTIP_COUNTRY_REPORT
  is '该表只在B版日志数据库中';
comment on column DJ_DESTIP_COUNTRY_REPORT.SERVICE
  is '界面私有标签（界面菜单ID）,值小于10000';
alter table DJ_DESTIP_COUNTRY_REPORT
  add constraint PK_DJ_DESTIP_COUNTRY_REPORT primary key (STAT_ID);

prompt
prompt Creating table DJ_DESTIP_STAT_LOG_DAILY
prompt =======================================
prompt
create table DJ_DESTIP_STAT_LOG_DAILY
(
  SEQ          NUMBER not null,
  DEST_COUNTRY VARCHAR2(256) not null,
  ACTIVE_SYS   INTEGER not null,
  SERVICE      INTEGER not null,
  SUM          NUMBER not null,
  STAT_TIME    DATE not null
)
;
alter table DJ_DESTIP_STAT_LOG_DAILY
  add constraint PK_DJ_DESTIP_STAT_LOG_DAILY primary key (SEQ);
create index DJ_DESTIP_STAT_TIME_DAILY on DJ_DESTIP_STAT_LOG_DAILY (STAT_TIME);

prompt
prompt Creating table DJ_DESTIP_STAT_LOG_MONTH
prompt =======================================
prompt
create table DJ_DESTIP_STAT_LOG_MONTH
(
  SEQ          NUMBER not null,
  DEST_COUNTRY VARCHAR2(256) not null,
  ACTIVE_SYS   INTEGER not null,
  SERVICE      INTEGER not null,
  SUM          NUMBER not null,
  STAT_TIME    DATE not null
)
;
alter table DJ_DESTIP_STAT_LOG_MONTH
  add constraint PK_DJ_DESTIP_STAT_LOG_MONTH primary key (SEQ);
create index DJ_DESTIP_STAT_TIME_MONTH on DJ_DESTIP_STAT_LOG_MONTH (STAT_TIME);

prompt
prompt Creating table DJ_ENTR_STAT_LOG_DAILY
prompt =====================================
prompt
create table DJ_ENTR_STAT_LOG_DAILY
(
  SEQ         NUMBER not null,
  ENTRANCE_ID INTEGER not null,
  ACTIVE_SYS  INTEGER not null,
  SERVICE     INTEGER not null,
  SUM         NUMBER not null,
  STAT_TIME   DATE not null
)
;
alter table DJ_ENTR_STAT_LOG_DAILY
  add constraint PK_DJ_ENTR_STAT_LOG_DAILY primary key (SEQ);
create index DJ_ENTR_STAT_TIME_DAILY on DJ_ENTR_STAT_LOG_DAILY (STAT_TIME);

prompt
prompt Creating table DJ_ENTR_STAT_LOG_MONTH
prompt =====================================
prompt
create table DJ_ENTR_STAT_LOG_MONTH
(
  SEQ         NUMBER not null,
  ENTRANCE_ID INTEGER not null,
  ACTIVE_SYS  INTEGER not null,
  SERVICE     INTEGER not null,
  SUM         NUMBER not null,
  STAT_TIME   DATE not null
)
;
alter table DJ_ENTR_STAT_LOG_MONTH
  add constraint PK_DJ_ENTR_STAT_LOG_MONTH primary key (SEQ);
create index DJ_ENTR_STAT_TIME_MONTH on DJ_ENTR_STAT_LOG_MONTH (STAT_TIME);

prompt
prompt Creating table DJ_LWHH_REPORT
prompt =============================
prompt
create table DJ_LWHH_REPORT
(
  STAT_ID     NUMBER not null,
  ACTIVE_SYS  INTEGER not null,
  SERVICE     INTEGER not null,
  LWHH        INTEGER not null,
  SUM         NUMBER not null,
  REPORT_TIME DATE not null
)
;
comment on table DJ_LWHH_REPORT
  is '管控来文函号实时统计表 该表只在B版日志数据库中';
comment on column DJ_LWHH_REPORT.ACTIVE_SYS
  is 'A版：4 B版：2 C版：1';
comment on column DJ_LWHH_REPORT.REPORT_TIME
  is '粒度5分钟';
alter table DJ_LWHH_REPORT
  add constraint PK_DJ_LWHH_REPORT primary key (STAT_ID);

prompt
prompt Creating table DJ_LWHH_STAT_LOG_DAILY
prompt =====================================
prompt
create table DJ_LWHH_STAT_LOG_DAILY
(
  SEQ        NUMBER not null,
  LWHH       INTEGER not null,
  ACTIVE_SYS INTEGER not null,
  SERVICE    INTEGER not null,
  SUM        NUMBER not null,
  STAT_TIME  DATE not null
)
;
alter table DJ_LWHH_STAT_LOG_DAILY
  add constraint PK_DJ_LWHH_STAT_LOG_DAILY primary key (SEQ);
create index DJ_LWHH_STAT_TIME_DAILY on DJ_LWHH_STAT_LOG_DAILY (STAT_TIME);

prompt
prompt Creating table DJ_LWHH_STAT_LOG_MONTH
prompt =====================================
prompt
create table DJ_LWHH_STAT_LOG_MONTH
(
  SEQ        NUMBER not null,
  LWHH       INTEGER not null,
  ACTIVE_SYS INTEGER not null,
  SERVICE    INTEGER not null,
  SUM        NUMBER not null,
  STAT_TIME  DATE not null
)
;
alter table DJ_LWHH_STAT_LOG_MONTH
  add constraint PK_DJ_LWHH_STAT_LOG_MONTH primary key (SEQ);
create index DJ_LWHH_STAT_TIME_MONTH on DJ_LWHH_STAT_LOG_MONTH (STAT_TIME);

prompt
prompt Creating table DJ_SERVICE_REPORT
prompt ================================
prompt
create table DJ_SERVICE_REPORT
(
  STAT_ID     NUMBER not null,
  ACTIVE_SYS  INTEGER not null,
  SERVICE     INTEGER not null,
  SUM         NUMBER not null,
  REPORT_TIME DATE not null
)
;
comment on table DJ_SERVICE_REPORT
  is '该表只在B版日志数据库中';
alter table DJ_SERVICE_REPORT
  add constraint PK_DJ_SERVICE_REPORT primary key (STAT_ID);

prompt
prompt Creating table DJ_SRCIP_DOMESTIC_REPORT
prompt =======================================
prompt
create table DJ_SRCIP_DOMESTIC_REPORT
(
  STAT_ID      NUMBER not null,
  ACTIVE_SYS   INTEGER not null,
  SRC_PROVINCE VARCHAR2(256) not null,
  SRC_CITY     VARCHAR2(256),
  SUM          NUMBER not null,
  REPORT_TIME  DATE not null,
  SERVICE      INTEGER not null
)
;
comment on table DJ_SRCIP_DOMESTIC_REPORT
  is '该表只在B版日志数据库中';
comment on column DJ_SRCIP_DOMESTIC_REPORT.SERVICE
  is '界面私有标签（界面菜单ID）,值小于10000';
alter table DJ_SRCIP_DOMESTIC_REPORT
  add constraint PK_DJ_SRCIP_DOMESTIC_REPORT primary key (STAT_ID);

prompt
prompt Creating table DJ_SRCIP_STAT_LOG_DAILY
prompt ======================================
prompt
create table DJ_SRCIP_STAT_LOG_DAILY
(
  SEQ          NUMBER not null,
  SRC_PROVINCE VARCHAR2(256) not null,
  SRC_CITY     VARCHAR2(256),
  ACTIVE_SYS   INTEGER not null,
  SERVICE      INTEGER not null,
  SUM          NUMBER not null,
  STAT_TIME    DATE not null
)
;
alter table DJ_SRCIP_STAT_LOG_DAILY
  add constraint PK_DJ_SRCIP_STAT_LOG_DAILY primary key (SEQ);
create index DJ_SRCIP_STAT_LOG_DAILY on DJ_SRCIP_STAT_LOG_DAILY (STAT_TIME);

prompt
prompt Creating table DJ_SRCIP_STAT_LOG_MONTH
prompt ======================================
prompt
create table DJ_SRCIP_STAT_LOG_MONTH
(
  SEQ          NUMBER not null,
  SRC_PROVINCE VARCHAR2(256) not null,
  SRC_CITY     VARCHAR2(256),
  ACTIVE_SYS   INTEGER not null,
  SERVICE      INTEGER not null,
  SUM          NUMBER not null,
  STAT_TIME    DATE not null
)
;
alter table DJ_SRCIP_STAT_LOG_MONTH
  add constraint PK_DJ_SRCIP_STAT_LOG_MONTH primary key (SEQ);
create index DJ_SRCIP_STAT_TIME_MONTH on DJ_SRCIP_STAT_LOG_MONTH (STAT_TIME);

prompt
prompt Creating table DJ_TAG_REPORT
prompt ============================
prompt
create table DJ_TAG_REPORT
(
  STAT_ID     NUMBER not null,
  ACTIVE_SYS  INTEGER not null,
  TAG         INTEGER not null,
  SUM         NUMBER not null,
  REPORT_TIME DATE not null,
  SERVICE     INTEGER not null
)
;
comment on table DJ_TAG_REPORT
  is '该表只在B版日志数据库中';
comment on column DJ_TAG_REPORT.SERVICE
  is '界面私有标签（界面菜单ID）,值小于10000';
alter table DJ_TAG_REPORT
  add constraint PK_DJ_TAG_REPORT primary key (STAT_ID);

prompt
prompt Creating table DJ_TAG_STAT_LOG_DAILY
prompt ====================================
prompt
create table DJ_TAG_STAT_LOG_DAILY
(
  SEQ        NUMBER not null,
  TAG        INTEGER not null,
  ACTIVE_SYS INTEGER not null,
  SERVICE    INTEGER not null,
  SUM        NUMBER not null,
  STAT_TIME  DATE not null
)
;
alter table DJ_TAG_STAT_LOG_DAILY
  add constraint PK_DJ_TAG_STAT_LOG_DAILY primary key (SEQ);
create index DJ_TAG_STAT_TIME_DAILY on DJ_TAG_STAT_LOG_DAILY (STAT_TIME);

prompt
prompt Creating table DJ_TAG_STAT_LOG_MONTH
prompt ====================================
prompt
create table DJ_TAG_STAT_LOG_MONTH
(
  SEQ        NUMBER not null,
  TAG        INTEGER not null,
  ACTIVE_SYS INTEGER not null,
  SERVICE    INTEGER not null,
  SUM        NUMBER not null,
  STAT_TIME  DATE not null
)
;
alter table DJ_TAG_STAT_LOG_MONTH
  add constraint PK_DJ_TAG_STAT_LOG_MONTH primary key (SEQ);
create index DJ_TAG_STAT_TIME_MONTH on DJ_TAG_STAT_LOG_MONTH (STAT_TIME);

prompt
prompt Creating sequence SEQ_DJ_ATTR_STAT_LOG_DAILY
prompt ============================================
prompt
create sequence SEQ_DJ_ATTR_STAT_LOG_DAILY
minvalue 1
maxvalue 9999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_ATTR_STAT_LOG_MONTH
prompt ============================================
prompt
create sequence SEQ_DJ_ATTR_STAT_LOG_MONTH
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_ATTR_TYPE_REPORT
prompt =========================================
prompt
create sequence SEQ_DJ_ATTR_TYPE_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_DESTIP_COUNTRY_REPORT
prompt ==============================================
prompt
create sequence SEQ_DJ_DESTIP_COUNTRY_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_DESTIP_STAT_LOG_DAILY
prompt ==============================================
prompt
create sequence SEQ_DJ_DESTIP_STAT_LOG_DAILY
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_DESTIP_STAT_LOG_MONTH
prompt ==============================================
prompt
create sequence SEQ_DJ_DESTIP_STAT_LOG_MONTH
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_ENTR_STAT_LOG_DAILY
prompt ============================================
prompt
create sequence SEQ_DJ_ENTR_STAT_LOG_DAILY
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_ENTR_STAT_LOG_MONTH
prompt ============================================
prompt
create sequence SEQ_DJ_ENTR_STAT_LOG_MONTH
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_LWHH_REPORT
prompt ====================================
prompt
create sequence SEQ_DJ_LWHH_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_LWHH_STAT_LOG_DAILY
prompt ============================================
prompt
create sequence SEQ_DJ_LWHH_STAT_LOG_DAILY
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_LWHH_STAT_LOG_MONTH
prompt ============================================
prompt
create sequence SEQ_DJ_LWHH_STAT_LOG_MONTH
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_SERVICE_REPORT
prompt =======================================
prompt
create sequence SEQ_DJ_SERVICE_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_SRCIP_DOMESTIC_REPORT
prompt ==============================================
prompt
create sequence SEQ_DJ_SRCIP_DOMESTIC_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_SRCIP_STAT_LOG_DAILY
prompt =============================================
prompt
create sequence SEQ_DJ_SRCIP_STAT_LOG_DAILY
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_SRCIP_STAT_LOG_MONTH
prompt =============================================
prompt
create sequence SEQ_DJ_SRCIP_STAT_LOG_MONTH
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_TAG_REPORT
prompt ===================================
prompt
create sequence SEQ_DJ_TAG_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_TAG_STAT_LOG_DAILY
prompt ===========================================
prompt
create sequence SEQ_DJ_TAG_STAT_LOG_DAILY
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_TAG_STAT_LOG_MONTH
prompt ===========================================
prompt
create sequence SEQ_DJ_TAG_STAT_LOG_MONTH
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating procedure PRO_ATTR_STAT_DJLOG_DAILY
prompt ============================================
prompt
create or replace procedure PRO_ATTR_STAT_DJLOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_ATTR_STAT_DJLOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_TAG_STAT_LOG_DAILY'; --当前存储过程统计业务类型
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
     delete from DJ_ATTR_STAT_LOG_DAILY dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_ATTR_STAT_LOG_DAILY一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DJ_ATTR_STAT_LOG_DAILY(SEQ,ACTIVE_SYS, SERVICE,ATTR_TYPE,STAT_TIME,SUM) (select SEQ_DJ_ATTR_STAT_LOG_DAILY.nextval, ACTIVE_SYS, SERVICE, ATTR_TYPE, shijian, shuliang from(select ACTIVE_SYS, SERVICE, ATTR_TYPE, trunc(REPORT_TIME, ''DD'') as shijian, sum(SUM) as shuliang from DJ_ATTR_TYPE_REPORT where REPORT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:MI:SS'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:MI:SS'')  group by ACTIVE_SYS, SERVICE,ATTR_TYPE,trunc(REPORT_TIME, ''DD'')))';
     commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_ATTR_STAT_LOG_DAILY一天的日志结束 end'')';
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
prompt Creating procedure PRO_ATTR_STAT_DJLOG_MONTH
prompt ============================================
prompt
create or replace procedure PRO_ATTR_STAT_DJLOG_MONTH(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_ATTR_STAT_DJLOG_MONTH'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_ATTR_LOG_MONTH all'; --当前存储过程统计业务类型
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
     delete from DJ_ATTR_STAT_LOG_MONTH dd where dd.stat_time=to_date(RIQI_S,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_ATTR_LOG_MONTH一个月的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DJ_ATTR_STAT_LOG_MONTH(SEQ,ACTIVE_SYS,SERVICE,ATTR_TYPE,STAT_TIME,SUM) (select SEQ_DJ_ATTR_STAT_LOG_MONTH.nextval, ACTIVE_SYS, SERVICE, ATTR_TYPE, shijian, shuliang from(select ACTIVE_SYS,SERVICE,ATTR_TYPE,trunc(STAT_TIME, ''MM'') as shijian,sum(SUM) as shuliang from DJ_ATTR_STAT_LOG_DAILY where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd'') and to_date('''||RIQI_E||''',''yyyy-mm-dd'')  group by ACTIVE_SYS,SERVICE,ATTR_TYPE,trunc(STAT_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_ATTR_LOG_MONTH一个月的日志结束 end'')';
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
prompt Creating procedure PRO_DESTIP_STAT_DJLOG_DAILY
prompt ==============================================
prompt
create or replace procedure PRO_DESTIP_STAT_DJLOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DESTIP_STAT_DJLOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_TAG_STAT_LOG_DAILY'; --当前存储过程统计业务类型
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
     delete from DJ_DESTIP_STAT_LOG_DAILY dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_DESTIP_STAT_LOG_DAILY一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DJ_DESTIP_STAT_LOG_DAILY(SEQ,ACTIVE_SYS, SERVICE,DEST_COUNTRY,STAT_TIME,SUM) (select SEQ_DJ_DESTIP_STAT_LOG_DAILY.nextval, ACTIVE_SYS, SERVICE, DEST_COUNTRY, shijian, shuliang from(select ACTIVE_SYS, SERVICE, DEST_COUNTRY, trunc(REPORT_TIME, ''DD'') as shijian, sum(SUM) as shuliang from DJ_DESTIP_country_REPORT where REPORT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:MI:SS'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:MI:SS'')  group by ACTIVE_SYS, SERVICE,DEST_COUNTRY,trunc(REPORT_TIME, ''DD'')))';
     commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_DESTIP_STAT_LOG_DAILY一天的日志结束 end'')';
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
prompt Creating procedure PRO_DESTIP_STAT_DJLOG_MONTH
prompt ==============================================
prompt
create or replace procedure PRO_DESTIP_STAT_DJLOG_MONTH(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DESTIP_STAT_DJLOG_MONTH'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_DESTIP_LOG_MONTH all'; --当前存储过程统计业务类型
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
     delete from DJ_DESTIP_STAT_LOG_MONTH dd where dd.stat_time=to_date(RIQI_S,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_DESTIP_LOG_MONTH一个月的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DJ_DESTIP_STAT_LOG_MONTH(SEQ,ACTIVE_SYS,SERVICE,DEST_COUNTRY,STAT_TIME,SUM) (select SEQ_DJ_DESTIP_STAT_LOG_MONTH.nextval, ACTIVE_SYS, SERVICE, DEST_COUNTRY, shijian, shuliang from(select ACTIVE_SYS,SERVICE,DEST_COUNTRY,trunc(STAT_TIME, ''MM'') as shijian,sum(SUM) as shuliang from DJ_DESTIP_STAT_LOG_DAILY where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd'') and to_date('''||RIQI_E||''',''yyyy-mm-dd'')  group by ACTIVE_SYS,SERVICE,DEST_COUNTRY,trunc(STAT_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_DESTIP_LOG_MONTH一个月的日志结束 end'')';
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
prompt Creating procedure PRO_ENTR_STAT_DJLOG_DAILY
prompt ============================================
prompt
create or replace procedure PRO_ENTR_STAT_DJLOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_ENTR_STAT_DJLOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_TAG_STAT_LOG_DAILY'; --当前存储过程统计业务类型
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
     delete from DJ_ENTR_STAT_LOG_DAILY dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_ENTR_STAT_LOG_DAILY一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DJ_ENTR_STAT_LOG_DAILY(SEQ,ACTIVE_SYS, SERVICE,ENTRANCE_ID,STAT_TIME,SUM) (select SEQ_DJ_ENTR_STAT_LOG_DAILY.nextval, ACTIVE_SYS, SERVICE, ENTRANCE_ID, shijian, shuliang from(select ACTIVE_SYS, SERVICE, GJ_CK_ID ENTRANCE_ID, trunc(CAP_TIME, ''DD'') as shijian, sum(SUM) as shuliang from DJ_CK_STAT_LOG where CAP_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:MI:SS'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:MI:SS'')  group by ACTIVE_SYS, SERVICE,GJ_CK_ID,trunc(CAP_TIME, ''DD'')))';
     commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_ENTR_STAT_LOG_DAILY一天的日志结束 end'')';
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
prompt Creating procedure PRO_ENTR_STAT_DJLOG_MONTH
prompt ============================================
prompt
create or replace procedure PRO_ENTR_STAT_DJLOG_MONTH(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_ENTR_STAT_DJLOG_MONTH'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_ENTR_LOG_MONTH all'; --当前存储过程统计业务类型
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
     delete from DJ_ENTR_STAT_LOG_MONTH dd where dd.stat_time=to_date(RIQI_S,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_ENTR_LOG_MONTH一个月的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DJ_ENTR_STAT_LOG_MONTH(SEQ,ACTIVE_SYS,SERVICE,ENTRANCE_ID,STAT_TIME,SUM) (select SEQ_DJ_ENTR_STAT_LOG_MONTH.nextval, ACTIVE_SYS, SERVICE, ENTRANCE_ID, shijian, shuliang from(select ACTIVE_SYS,SERVICE,ENTRANCE_ID,trunc(STAT_TIME, ''MM'') as shijian,sum(SUM) as shuliang from DJ_ENTR_STAT_LOG_DAILY where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd'') and to_date('''||RIQI_E||''',''yyyy-mm-dd'')  group by ACTIVE_SYS,SERVICE,ENTRANCE_ID,trunc(STAT_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_ENTR_LOG_MONTH一个月的日志结束 end'')';
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
prompt Creating procedure PRO_LWHH_STAT_DJLOG_DAILY
prompt ============================================
prompt
create or replace procedure PRO_LWHH_STAT_DJLOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_LWHH_STAT_DJLOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_TAG_STAT_LOG_DAILY'; --当前存储过程统计业务类型
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
     delete from DJ_LWHH_STAT_LOG_DAILY dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_LWHH_STAT_LOG_DAILY一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DJ_LWHH_STAT_LOG_DAILY(SEQ,ACTIVE_SYS, SERVICE,LWHH,STAT_TIME,SUM) (select SEQ_DJ_LWHH_STAT_LOG_DAILY.nextval, ACTIVE_SYS, SERVICE, LWHH, shijian, shuliang from(select ACTIVE_SYS, SERVICE, LWHH, trunc(REPORT_TIME, ''DD'') as shijian, sum(SUM) as shuliang from DJ_LWHH_REPORT where REPORT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:MI:SS'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:MI:SS'')  group by ACTIVE_SYS, SERVICE,LWHH,trunc(REPORT_TIME, ''DD'')))';
     commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_LWHH_STAT_LOG_DAILY一天的日志结束 end'')';
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
prompt Creating procedure PRO_LWHH_STAT_DJLOG_MONTH
prompt ============================================
prompt
create or replace procedure PRO_LWHH_STAT_DJLOG_MONTH(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_LWHH_STAT_DJLOG_MONTH'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_LWHH_LOG_MONTH all'; --当前存储过程统计业务类型
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
     delete from DJ_LWHH_STAT_LOG_MONTH dd where dd.stat_time=to_date(RIQI_S,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_LWHH_LOG_MONTH一个月的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DJ_LWHH_STAT_LOG_MONTH(SEQ,ACTIVE_SYS,SERVICE,LWHH,STAT_TIME,SUM) (select SEQ_DJ_LWHH_STAT_LOG_MONTH.nextval, ACTIVE_SYS, SERVICE, LWHH, shijian, shuliang from(select ACTIVE_SYS,SERVICE,LWHH,trunc(STAT_TIME, ''MM'') as shijian,sum(SUM) as shuliang from DJ_LWHH_STAT_LOG_DAILY where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd'') and to_date('''||RIQI_E||''',''yyyy-mm-dd'')  group by ACTIVE_SYS,SERVICE,LWHH,trunc(STAT_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_LWHH_LOG_MONTH一个月的日志结束 end'')';
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
prompt Creating procedure PRO_SRCIP_STAT_DJLOG_DAILY
prompt =============================================
prompt
create or replace procedure PRO_SRCIP_STAT_DJLOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_SRCIP_STAT_DJLOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_SRCIP_STAT_LOG_DAILY'; --当前存储过程统计业务类型
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
     delete from DJ_SRCIP_STAT_LOG_DAILY dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_SRCIP_STAT_LOG_DAILY一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DJ_SRCIP_STAT_LOG_DAILY(SEQ,ACTIVE_SYS, SERVICE,SRC_PROVINCE,SRC_CITY,STAT_TIME,SUM) (select SEQ_DJ_SRCIP_STAT_LOG_DAILY.nextval, ACTIVE_SYS, SERVICE, SRC_PROVINCE,SRC_CITY, shijian, shuliang from(select ACTIVE_SYS, SERVICE, SRC_PROVINCE,SRC_CITY, trunc(REPORT_TIME, ''DD'') as shijian, sum(SUM) as shuliang from DJ_SRCIP_DOMESTIC_REPORT where REPORT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:MI:SS'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:MI:SS'')  group by ACTIVE_SYS, SERVICE,SRC_PROVINCE,SRC_CITY,trunc(REPORT_TIME, ''DD'')))';
     commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_SRCIP_STAT_LOG_DAILY一天的日志结束 end'')';
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
prompt Creating procedure PRO_SRCIP_STAT_DJLOG_MONTH
prompt =============================================
prompt
create or replace procedure PRO_SRCIP_STAT_DJLOG_MONTH(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_SRCIP_STAT_DJLOG_MONTH'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_SRCIP_LOG_MONTH all'; --当前存储过程统计业务类型
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
     delete from DJ_SRCIP_STAT_LOG_MONTH dd where dd.stat_time=to_date(RIQI_S,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_SRCIP_LOG_MONTH一个月的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DJ_SRCIP_STAT_LOG_MONTH(SEQ,ACTIVE_SYS,SERVICE,SRC_PROVINCE,SRC_CITY,STAT_TIME,SUM) (select SEQ_DJ_SRCIP_STAT_LOG_MONTH.nextval, ACTIVE_SYS, SERVICE, SRC_PROVINCE,SRC_CITY, shijian, shuliang from(select ACTIVE_SYS,SERVICE,SRC_PROVINCE,SRC_CITY,trunc(STAT_TIME, ''MM'') as shijian,sum(SUM) as shuliang from DJ_SRCIP_STAT_LOG_DAILY where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd'') and to_date('''||RIQI_E||''',''yyyy-mm-dd'')  group by ACTIVE_SYS,SERVICE,SRC_PROVINCE,SRC_CITY,trunc(STAT_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_SRCIP_LOG_MONTH一个月的日志结束 end'')';
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
prompt Creating procedure PRO_TAG_STAT_DJLOG_DAILY
prompt ===========================================
prompt
create or replace procedure PRO_TAG_STAT_DJLOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_TAG_STAT_DJLOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_TAG_STAT_LOG_DAILY'; --当前存储过程统计业务类型
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
     delete from DJ_TAG_STAT_LOG_DAILY dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_TAG_STAT_LOG_DAILY一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DJ_TAG_STAT_LOG_DAILY(SEQ,ACTIVE_SYS, SERVICE,TAG,STAT_TIME,SUM) (select SEQ_DJ_TAG_STAT_LOG_DAILY.nextval, ACTIVE_SYS, SERVICE, TAG, shijian, shuliang from(select ACTIVE_SYS, SERVICE, TAG, trunc(REPORT_TIME, ''DD'') as shijian, sum(SUM) as shuliang from DJ_TAG_REPORT where REPORT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:MI:SS'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:MI:SS'')  group by ACTIVE_SYS, SERVICE,TAG,trunc(REPORT_TIME, ''DD'')))';
     commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_TAG_STAT_LOG_DAILY一天的日志结束 end'')';
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
prompt Creating procedure PRO_TAG_STAT_DJLOG_MONTH
prompt ===========================================
prompt
create or replace procedure PRO_TAG_STAT_DJLOG_MONTH(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_TAG_STAT_DJLOG_MONTH'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_TAG_LOG_MONTH all'; --当前存储过程统计业务类型
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
     delete from DJ_TAG_STAT_LOG_MONTH dd where dd.stat_time=to_date(RIQI_S,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_TAG_LOG_MONTH一个月的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DJ_TAG_STAT_LOG_MONTH(SEQ,ACTIVE_SYS,SERVICE,TAG,STAT_TIME,SUM) (select SEQ_DJ_TAG_STAT_LOG_MONTH.nextval, ACTIVE_SYS, SERVICE, TAG, shijian, shuliang from(select ACTIVE_SYS,SERVICE,TAG,trunc(STAT_TIME, ''MM'') as shijian,sum(SUM) as shuliang from DJ_TAG_STAT_LOG_DAILY where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd'') and to_date('''||RIQI_E||''',''yyyy-mm-dd'')  group by ACTIVE_SYS,SERVICE,TAG,trunc(STAT_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_TAG_LOG_MONTH一个月的日志结束 end'')';
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

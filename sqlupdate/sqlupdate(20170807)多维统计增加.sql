-------------------------------------------
-- Export file for user GK_LOG           --
-- Created by dell on 2017/8/7, 17:25:39 --
-------------------------------------------

spool sqlupdate(20170807)多维统计增加.log

prompt
prompt Creating table DF_LWHH_TAG_REPORT
prompt =================================
prompt
create table DF_LWHH_TAG_REPORT
(
  STAT_ID     NUMBER not null,
  ACTIVE_SYS  INTEGER not null,
  LWHH        NUMBER not null,
  TAG         NUMBER not null,
  SUM         NUMBER not null,
  REPORT_TIME DATE not null,
  SERVICE     INTEGER not null
)
;
alter table DF_LWHH_TAG_REPORT
  add constraint PK_DF_LWHH_TAG_REPORT primary key (STAT_ID);
create index DF_LWHH_TAG_REPORT_INDEX on DF_LWHH_TAG_REPORT (REPORT_TIME);

prompt
prompt Creating table DF_LWHH_TAG_STAT_LOG_DAILY
prompt =========================================
prompt
create table DF_LWHH_TAG_STAT_LOG_DAILY
(
  SEQ        NUMBER not null,
  ACTIVE_SYS INTEGER not null,
  LWHH       NUMBER not null,
  TAG        NUMBER not null,
  SUM        NUMBER not null,
  STAT_TIME  DATE not null,
  SERVICE    INTEGER not null
)
;
alter table DF_LWHH_TAG_STAT_LOG_DAILY
  add constraint PK_DF_LWHH_TAG_DAILY primary key (SEQ);
create index DF_LWHH_TAG_DAILY_INDEX on DF_LWHH_TAG_STAT_LOG_DAILY (STAT_TIME);

prompt
prompt Creating table DF_LWHH_TAG_STAT_LOG_MONTH
prompt =========================================
prompt
create table DF_LWHH_TAG_STAT_LOG_MONTH
(
  SEQ        NUMBER not null,
  ACTIVE_SYS INTEGER not null,
  LWHH       NUMBER not null,
  TAG        NUMBER not null,
  SUM        NUMBER not null,
  STAT_TIME  DATE not null,
  SERVICE    INTEGER not null
)
;
alter table DF_LWHH_TAG_STAT_LOG_MONTH
  add constraint PK_DF_LWHH_TAG_MONTH primary key (SEQ);
create index DF_LWHH_TAG_MONTH_INDEX on DF_LWHH_TAG_STAT_LOG_MONTH (STAT_TIME);

prompt
prompt Creating table DF_LWHH_TYPE_REPORT
prompt ==================================
prompt
create table DF_LWHH_TYPE_REPORT
(
  STAT_ID     NUMBER not null,
  ACTIVE_SYS  INTEGER not null,
  LWHH        NUMBER not null,
  ATTR_TYPE   NUMBER not null,
  SUM         NUMBER not null,
  REPORT_TIME DATE not null,
  SERVICE     INTEGER not null
)
;
alter table DF_LWHH_TYPE_REPORT
  add constraint PK_DF_LWHH_TYPE_REPORT primary key (STAT_ID);
create index DF_LWHH_TYPE_REPORT_INDEX on DF_LWHH_TYPE_REPORT (REPORT_TIME);

prompt
prompt Creating table DF_LWHH_TYPE_STAT_LOG_DAILY
prompt ==========================================
prompt
create table DF_LWHH_TYPE_STAT_LOG_DAILY
(
  SEQ        NUMBER not null,
  ACTIVE_SYS INTEGER not null,
  LWHH       NUMBER not null,
  ATTR_TYPE  NUMBER not null,
  SUM        NUMBER not null,
  STAT_TIME  DATE not null,
  SERVICE    INTEGER not null
)
;
alter table DF_LWHH_TYPE_STAT_LOG_DAILY
  add constraint PK_DF_LWHH_TYPE_DAILY primary key (SEQ);
create index DF_LWHH_TYPE_DAILY_INDEX on DF_LWHH_TYPE_STAT_LOG_DAILY (STAT_TIME);

prompt
prompt Creating table DF_LWHH_TYPE_STAT_LOG_MONTH
prompt ==========================================
prompt
create table DF_LWHH_TYPE_STAT_LOG_MONTH
(
  SEQ        NUMBER not null,
  ACTIVE_SYS INTEGER not null,
  LWHH       NUMBER not null,
  ATTR_TYPE  NUMBER not null,
  SUM        NUMBER not null,
  STAT_TIME  DATE not null,
  SERVICE    INTEGER not null
)
;
alter table DF_LWHH_TYPE_STAT_LOG_MONTH
  add constraint PK_DF_LWHH_TYPE_MONTH primary key (SEQ);
create index DF_LWHH_TYPE_MONTH_INDEX on DF_LWHH_TYPE_STAT_LOG_MONTH (STAT_TIME);

prompt
prompt Creating table DF_SRCIP_DOMESTIC_TAG_REPORT
prompt ===========================================
prompt
create table DF_SRCIP_DOMESTIC_TAG_REPORT
(
  STAT_ID      NUMBER not null,
  ACTIVE_SYS   INTEGER not null,
  SRC_PROVINCE VARCHAR2(100) not null,
  TAG          NUMBER not null,
  SUM          NUMBER not null,
  REPORT_TIME  DATE not null,
  SERVICE      INTEGER not null
)
;
alter table DF_SRCIP_DOMESTIC_TAG_REPORT
  add constraint PDF_SRCIP_DOMESTIC_TAG_REPORT primary key (STAT_ID);
create index DF_SRCIP_TAG_REPORT_INDEX on DF_SRCIP_DOMESTIC_TAG_REPORT (REPORT_TIME);

prompt
prompt Creating table DF_SRCIP_DOMESTIC_TYPE_REPORT
prompt ============================================
prompt
create table DF_SRCIP_DOMESTIC_TYPE_REPORT
(
  STAT_ID      NUMBER not null,
  ACTIVE_SYS   INTEGER not null,
  SRC_PROVINCE VARCHAR2(100) not null,
  ATTR_TYPE    NUMBER not null,
  SUM          NUMBER not null,
  REPORT_TIME  DATE not null,
  SERVICE      INTEGER not null
)
;
alter table DF_SRCIP_DOMESTIC_TYPE_REPORT
  add constraint PDF_SRCIP_DOMESTIC_TYPE_REPORT primary key (STAT_ID);
create index DF_SRCIP_TYPE_REPORT_INDEX on DF_SRCIP_DOMESTIC_TYPE_REPORT (REPORT_TIME);

prompt
prompt Creating table DF_SRCIP_TAG_STAT_LOG_DAILY
prompt ==========================================
prompt
create table DF_SRCIP_TAG_STAT_LOG_DAILY
(
  SEQ          NUMBER not null,
  ACTIVE_SYS   INTEGER not null,
  SRC_PROVINCE VARCHAR2(100) not null,
  TAG          NUMBER not null,
  SUM          NUMBER not null,
  STAT_TIME    DATE not null,
  SERVICE      INTEGER not null
)
;
alter table DF_SRCIP_TAG_STAT_LOG_DAILY
  add constraint PK_DF_SRCIP_TAG_DAILY primary key (SEQ);
create index DF_SRCIP_TAG_DAILY_INDEX on DF_SRCIP_TAG_STAT_LOG_DAILY (STAT_TIME);

prompt
prompt Creating table DF_SRCIP_TAG_STAT_LOG_MONTH
prompt ==========================================
prompt
create table DF_SRCIP_TAG_STAT_LOG_MONTH
(
  SEQ          NUMBER not null,
  ACTIVE_SYS   INTEGER not null,
  SRC_PROVINCE VARCHAR2(100) not null,
  TAG          NUMBER not null,
  SUM          NUMBER not null,
  STAT_TIME    DATE not null,
  SERVICE      INTEGER not null
)
;
alter table DF_SRCIP_TAG_STAT_LOG_MONTH
  add constraint PK_DF_SRCIP_TAG_MONTH primary key (SEQ);
create index DF_SRCIP_TAG_MONTH_INDEX on DF_SRCIP_TAG_STAT_LOG_MONTH (STAT_TIME);

prompt
prompt Creating table DF_SRCIP_TYPE_STAT_LOG_DAILY
prompt ===========================================
prompt
create table DF_SRCIP_TYPE_STAT_LOG_DAILY
(
  SEQ          NUMBER not null,
  ACTIVE_SYS   INTEGER not null,
  SRC_PROVINCE VARCHAR2(100) not null,
  ATTR_TYPE    NUMBER not null,
  SUM          NUMBER not null,
  STAT_TIME    DATE not null,
  SERVICE      INTEGER not null
)
;
alter table DF_SRCIP_TYPE_STAT_LOG_DAILY
  add constraint PK_DF_SRCIP_TYPE_DAILY primary key (SEQ);
create index DF_SRCIP_TYPE_DAILY_INDEX on DF_SRCIP_TYPE_STAT_LOG_DAILY (STAT_TIME);

prompt
prompt Creating table DF_SRCIP_TYPE_STAT_LOG_MONTH
prompt ===========================================
prompt
create table DF_SRCIP_TYPE_STAT_LOG_MONTH
(
  SEQ          NUMBER not null,
  ACTIVE_SYS   INTEGER not null,
  SRC_PROVINCE VARCHAR2(100) not null,
  ATTR_TYPE    NUMBER not null,
  SUM          NUMBER not null,
  STAT_TIME    DATE not null,
  SERVICE      INTEGER not null
)
;
alter table DF_SRCIP_TYPE_STAT_LOG_MONTH
  add constraint PK_DF_SRCIP_TYPE_MONTH primary key (SEQ);
create index DF_SRCIP_TYPE_MONTH_INDEX on DF_SRCIP_TYPE_STAT_LOG_MONTH (STAT_TIME);

prompt
prompt Creating table DJ_LWHH_TAG_REPORT
prompt =================================
prompt
create table DJ_LWHH_TAG_REPORT
(
  STAT_ID     NUMBER not null,
  ACTIVE_SYS  INTEGER not null,
  LWHH        NUMBER not null,
  TAG         NUMBER not null,
  SUM         NUMBER not null,
  REPORT_TIME DATE not null,
  SERVICE     INTEGER not null
)
;
alter table DJ_LWHH_TAG_REPORT
  add constraint PK_DJ_LWHH_TAG_REPORT primary key (STAT_ID);
create index DJ_LWHH_TAG_REPORT_INDEX on DJ_LWHH_TAG_REPORT (REPORT_TIME);

prompt
prompt Creating table DJ_LWHH_TAG_STAT_LOG_DAILY
prompt =========================================
prompt
create table DJ_LWHH_TAG_STAT_LOG_DAILY
(
  SEQ        NUMBER not null,
  ACTIVE_SYS INTEGER not null,
  LWHH       NUMBER not null,
  TAG        NUMBER not null,
  SUM        NUMBER not null,
  STAT_TIME  DATE not null,
  SERVICE    INTEGER not null
)
;
alter table DJ_LWHH_TAG_STAT_LOG_DAILY
  add constraint PK_DJ_LWHH_TAG_DAILY primary key (SEQ);
create index DJ_LWHH_TAG_DAILY_INDEX on DJ_LWHH_TAG_STAT_LOG_DAILY (STAT_TIME);

prompt
prompt Creating table DJ_LWHH_TAG_STAT_LOG_MONTH
prompt =========================================
prompt
create table DJ_LWHH_TAG_STAT_LOG_MONTH
(
  SEQ        NUMBER not null,
  ACTIVE_SYS INTEGER not null,
  LWHH       NUMBER not null,
  TAG        NUMBER not null,
  SUM        NUMBER not null,
  STAT_TIME  DATE not null,
  SERVICE    INTEGER not null
)
;
alter table DJ_LWHH_TAG_STAT_LOG_MONTH
  add constraint PK_DJ_LWHH_TAG_MONTH primary key (SEQ);
create index DJ_LWHH_TAG_MONTH_INDEX on DJ_LWHH_TAG_STAT_LOG_MONTH (STAT_TIME);

prompt
prompt Creating table DJ_LWHH_TYPE_REPORT
prompt ==================================
prompt
create table DJ_LWHH_TYPE_REPORT
(
  STAT_ID     NUMBER not null,
  ACTIVE_SYS  INTEGER not null,
  LWHH        NUMBER not null,
  ATTR_TYPE   NUMBER not null,
  SUM         NUMBER not null,
  REPORT_TIME DATE not null,
  SERVICE     INTEGER not null
)
;
alter table DJ_LWHH_TYPE_REPORT
  add constraint PK_DJ_LWHH_TYPE_REPORT primary key (STAT_ID);
create index DJ_LWHH_TYPE_REPORT_INDEX on DJ_LWHH_TYPE_REPORT (REPORT_TIME);

prompt
prompt Creating table DJ_LWHH_TYPE_STAT_LOG_DAILY
prompt ==========================================
prompt
create table DJ_LWHH_TYPE_STAT_LOG_DAILY
(
  SEQ        NUMBER not null,
  ACTIVE_SYS INTEGER not null,
  LWHH       NUMBER not null,
  ATTR_TYPE  NUMBER not null,
  SUM        NUMBER not null,
  STAT_TIME  DATE not null,
  SERVICE    INTEGER not null
)
;
alter table DJ_LWHH_TYPE_STAT_LOG_DAILY
  add constraint PK_DJ_LWHH_TYPE_DAILY primary key (SEQ);
create index DJ_LWHH_TYPE_DAILY_INDEX on DJ_LWHH_TYPE_STAT_LOG_DAILY (STAT_TIME);

prompt
prompt Creating table DJ_LWHH_TYPE_STAT_LOG_MONTH
prompt ==========================================
prompt
create table DJ_LWHH_TYPE_STAT_LOG_MONTH
(
  SEQ        NUMBER not null,
  ACTIVE_SYS INTEGER not null,
  LWHH       NUMBER not null,
  ATTR_TYPE  NUMBER not null,
  SUM        NUMBER not null,
  STAT_TIME  DATE not null,
  SERVICE    INTEGER not null
)
;
alter table DJ_LWHH_TYPE_STAT_LOG_MONTH
  add constraint PK_DJ_LWHH_TYPE_MONTH primary key (SEQ);
create index DJ_LWHH_TYPE_MONTH_INDEX on DJ_LWHH_TYPE_STAT_LOG_MONTH (STAT_TIME);

prompt
prompt Creating table DJ_SRCIP_DOMESTIC_TAG_REPORT
prompt ===========================================
prompt
create table DJ_SRCIP_DOMESTIC_TAG_REPORT
(
  STAT_ID      NUMBER not null,
  ACTIVE_SYS   INTEGER not null,
  SRC_PROVINCE VARCHAR2(100) not null,
  TAG          NUMBER not null,
  SUM          NUMBER not null,
  REPORT_TIME  DATE not null,
  SERVICE      INTEGER not null
)
;
alter table DJ_SRCIP_DOMESTIC_TAG_REPORT
  add constraint PDJ_SRCIP_DOMESTIC_TAG_REPORT primary key (STAT_ID);
create index DJ_SRCIP_TAG_REPORT_INDEX on DJ_SRCIP_DOMESTIC_TAG_REPORT (REPORT_TIME);

prompt
prompt Creating table DJ_SRCIP_DOMESTIC_TYPE_REPORT
prompt ============================================
prompt
create table DJ_SRCIP_DOMESTIC_TYPE_REPORT
(
  STAT_ID      NUMBER not null,
  ACTIVE_SYS   INTEGER not null,
  SRC_PROVINCE VARCHAR2(100) not null,
  ATTR_TYPE    NUMBER not null,
  SUM          NUMBER not null,
  REPORT_TIME  DATE not null,
  SERVICE      INTEGER not null
)
;
alter table DJ_SRCIP_DOMESTIC_TYPE_REPORT
  add constraint PDJ_SRCIP_DOMESTIC_TYPE_REPORT primary key (STAT_ID);
create index DJ_SRCIP_TYPE_REPORT_INDEX on DJ_SRCIP_DOMESTIC_TYPE_REPORT (REPORT_TIME);

prompt
prompt Creating table DJ_SRCIP_TAG_STAT_LOG_DAILY
prompt ==========================================
prompt
create table DJ_SRCIP_TAG_STAT_LOG_DAILY
(
  SEQ          NUMBER not null,
  ACTIVE_SYS   INTEGER not null,
  SRC_PROVINCE VARCHAR2(100) not null,
  TAG          NUMBER not null,
  SUM          NUMBER not null,
  STAT_TIME    DATE not null,
  SERVICE      INTEGER not null
)
;
alter table DJ_SRCIP_TAG_STAT_LOG_DAILY
  add constraint PK_DJ_SRCIP_TAG_DAILY primary key (SEQ);
create index DJ_SRCIP_TAG_DAILY_INDEX on DJ_SRCIP_TAG_STAT_LOG_DAILY (STAT_TIME);

prompt
prompt Creating table DJ_SRCIP_TAG_STAT_LOG_MONTH
prompt ==========================================
prompt
create table DJ_SRCIP_TAG_STAT_LOG_MONTH
(
  SEQ          NUMBER not null,
  ACTIVE_SYS   INTEGER not null,
  SRC_PROVINCE VARCHAR2(100) not null,
  TAG          NUMBER not null,
  SUM          NUMBER not null,
  STAT_TIME    DATE not null,
  SERVICE      INTEGER not null
)
;
alter table DJ_SRCIP_TAG_STAT_LOG_MONTH
  add constraint PK_DJ_SRCIP_TAG_MONTH primary key (SEQ);
create index DJ_SRCIP_TAG_MONTH_INDEX on DJ_SRCIP_TAG_STAT_LOG_MONTH (STAT_TIME);

prompt
prompt Creating table DJ_SRCIP_TYPE_STAT_LOG_DAILY
prompt ===========================================
prompt
create table DJ_SRCIP_TYPE_STAT_LOG_DAILY
(
  SEQ          NUMBER not null,
  ACTIVE_SYS   INTEGER not null,
  SRC_PROVINCE VARCHAR2(100) not null,
  ATTR_TYPE    NUMBER not null,
  SUM          NUMBER not null,
  STAT_TIME    DATE not null,
  SERVICE      INTEGER not null
)
;
alter table DJ_SRCIP_TYPE_STAT_LOG_DAILY
  add constraint PK_DJ_SRCIP_TYPE_DAILY primary key (SEQ);
create index DJ_SRCIP_TYPE_DAILY_INDEX on DJ_SRCIP_TYPE_STAT_LOG_DAILY (STAT_TIME);

prompt
prompt Creating table DJ_SRCIP_TYPE_STAT_LOG_MONTH
prompt ===========================================
prompt
create table DJ_SRCIP_TYPE_STAT_LOG_MONTH
(
  SEQ          NUMBER not null,
  ACTIVE_SYS   INTEGER not null,
  SRC_PROVINCE VARCHAR2(100) not null,
  ATTR_TYPE    NUMBER not null,
  SUM          NUMBER not null,
  STAT_TIME    DATE not null,
  SERVICE      INTEGER not null
)
;
alter table DJ_SRCIP_TYPE_STAT_LOG_MONTH
  add constraint PK_DJ_SRCIP_TYPE_MONTH primary key (SEQ);
create index DJ_SRCIP_TYPE_MONTH_INDEX on DJ_SRCIP_TYPE_STAT_LOG_MONTH (STAT_TIME);

prompt
prompt Creating sequence SEQ_DF_LWHH_TAG_DAILY
prompt =======================================
prompt
create sequence SEQ_DF_LWHH_TAG_DAILY
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DF_LWHH_TAG_MONTH
prompt =======================================
prompt
create sequence SEQ_DF_LWHH_TAG_MONTH
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DF_LWHH_TAG_REPORT
prompt ========================================
prompt
create sequence SEQ_DF_LWHH_TAG_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 61
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DF_LWHH_TYPE_DAILY
prompt ========================================
prompt
create sequence SEQ_DF_LWHH_TYPE_DAILY
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DF_LWHH_TYPE_MONTH
prompt ========================================
prompt
create sequence SEQ_DF_LWHH_TYPE_MONTH
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DF_LWHH_TYPE_REPORT
prompt =========================================
prompt
create sequence SEQ_DF_LWHH_TYPE_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DF_SRCIP_TAG_DAILY
prompt ========================================
prompt
create sequence SEQ_DF_SRCIP_TAG_DAILY
minvalue 1
maxvalue 9999999999999999999999999999
start with 61
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DF_SRCIP_TAG_MONTH
prompt ========================================
prompt
create sequence SEQ_DF_SRCIP_TAG_MONTH
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DF_SRCIP_TAG_REPORT
prompt =========================================
prompt
create sequence SEQ_DF_SRCIP_TAG_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DF_SRCIP_TYPE_DAILY
prompt =========================================
prompt
create sequence SEQ_DF_SRCIP_TYPE_DAILY
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DF_SRCIP_TYPE_MONTH
prompt =========================================
prompt
create sequence SEQ_DF_SRCIP_TYPE_MONTH
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DF_SRCIP_TYPE_REPORT
prompt ==========================================
prompt
create sequence SEQ_DF_SRCIP_TYPE_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_LWHH_TAG_DAILY
prompt =======================================
prompt
create sequence SEQ_DJ_LWHH_TAG_DAILY
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_LWHH_TAG_MONTH
prompt =======================================
prompt
create sequence SEQ_DJ_LWHH_TAG_MONTH
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_LWHH_TAG_REPORT
prompt ========================================
prompt
create sequence SEQ_DJ_LWHH_TAG_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_LWHH_TYPE_DAILY
prompt ========================================
prompt
create sequence SEQ_DJ_LWHH_TYPE_DAILY
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_LWHH_TYPE_MONTH
prompt ========================================
prompt
create sequence SEQ_DJ_LWHH_TYPE_MONTH
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_LWHH_TYPE_REPORT
prompt =========================================
prompt
create sequence SEQ_DJ_LWHH_TYPE_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_SRCIP_TAG_DAILY
prompt ========================================
prompt
create sequence SEQ_DJ_SRCIP_TAG_DAILY
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_SRCIP_TAG_MONTH
prompt ========================================
prompt
create sequence SEQ_DJ_SRCIP_TAG_MONTH
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_SRCIP_TAG_REPORT
prompt =========================================
prompt
create sequence SEQ_DJ_SRCIP_TAG_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_SRCIP_TYPE_DAILY
prompt =========================================
prompt
create sequence SEQ_DJ_SRCIP_TYPE_DAILY
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_SRCIP_TYPE_MONTH
prompt =========================================
prompt
create sequence SEQ_DJ_SRCIP_TYPE_MONTH
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_DJ_SRCIP_TYPE_REPORT
prompt ==========================================
prompt
create sequence SEQ_DJ_SRCIP_TYPE_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating procedure PRO_LWHH_TAG_STAT_DFLOG_DAILY
prompt ================================================
prompt
create or replace procedure PRO_LWHH_TAG_STAT_DFLOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_LWHH_TAG_STAT_DFLOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_LWHH_TAG_STAT_DFLOG_DAILY'; --当前存储过程统计业务类型
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
     delete from DF_LWHH_TAG_STAT_LOG_DAILY dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_LWHH_TAG_STAT_LOG_DAILY一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DF_LWHH_TAG_STAT_LOG_DAILY(SEQ,ACTIVE_SYS, SERVICE,LWHH,tag,STAT_TIME,SUM) (select SEQ_DF_LWHH_TAG_DAILY.nextval, ACTIVE_SYS, SERVICE,LWHH, tag, shijian, shuliang from(select ACTIVE_SYS, SERVICE,LWHH, tag, trunc(REPORT_TIME, ''DD'') as shijian, sum(SUM) as shuliang from DF_LWHH_TAG_REPORT where REPORT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:MI:SS'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:MI:SS'')  group by ACTIVE_SYS, SERVICE,LWHH,tag,trunc(REPORT_TIME, ''DD'')))';
     commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_LWHH_TAG_STAT_LOG_DAILY一天的日志结束 end'')';
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
prompt Creating procedure PRO_LWHH_TAG_STAT_DFLOG_MONTH
prompt ================================================
prompt
create or replace procedure PRO_LWHH_TAG_STAT_DFLOG_MONTH(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_LWHH_TAG_STAT_DFLOG_MONTH'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_LWHH_TAG_LOG_MONTH all'; --当前存储过程统计业务类型
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
     delete from DF_LWHH_TAG_STAT_LOG_MONTH dd where dd.stat_time=to_date(RIQI_S,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_LWHH_TAG_LOG_MONTH一个月的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DF_LWHH_TAG_STAT_LOG_MONTH(SEQ,ACTIVE_SYS,SERVICE,LWHH,TAG,STAT_TIME,SUM) (select SEQ_DF_LWHH_TAG_MONTH.nextval, ACTIVE_SYS, SERVICE, LWHH,TAG, shijian, shuliang from(select ACTIVE_SYS,SERVICE,LWHH,TAG,trunc(STAT_TIME, ''MM'') as shijian,sum(SUM) as shuliang from DF_LWHH_TAG_STAT_LOG_DAILY where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd'') and to_date('''||RIQI_E||''',''yyyy-mm-dd'')  group by ACTIVE_SYS,SERVICE,LWHH,TAG,trunc(STAT_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_LWHH_TAG_LOG_MONTH一个月的日志结束 end'')';
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
prompt Creating procedure PRO_LWHH_TAG_STAT_DJLOG_DAILY
prompt ================================================
prompt
create or replace procedure PRO_LWHH_TAG_STAT_DJLOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_LWHH_TAG_STAT_DJLOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_LWHH_TAG_STAT_DJLOG_DAILY'; --当前存储过程统计业务类型
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
     delete from DJ_LWHH_TAG_STAT_LOG_DAILY dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_LWHH_TAG_STAT_LOG_DAILY一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DJ_LWHH_TAG_STAT_LOG_DAILY(SEQ,ACTIVE_SYS, SERVICE,LWHH,tag,STAT_TIME,SUM) (select SEQ_DJ_LWHH_TAG_DAILY.nextval, ACTIVE_SYS, SERVICE,LWHH, tag, shijian, shuliang from(select ACTIVE_SYS, SERVICE,LWHH, tag, trunc(REPORT_TIME, ''DD'') as shijian, sum(SUM) as shuliang from DJ_LWHH_TAG_REPORT where REPORT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:MI:SS'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:MI:SS'')  group by ACTIVE_SYS, SERVICE,LWHH,tag,trunc(REPORT_TIME, ''DD'')))';
     commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_LWHH_TAG_STAT_LOG_DAILY一天的日志结束 end'')';
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
prompt Creating procedure PRO_LWHH_TAG_STAT_DJLOG_MONTH
prompt ================================================
prompt
create or replace procedure PRO_LWHH_TAG_STAT_DJLOG_MONTH(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_LWHH_TAG_STAT_DJLOG_MONTH'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_LWHH_TAG_LOG_MONTH all'; --当前存储过程统计业务类型
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
     delete from DJ_LWHH_TAG_STAT_LOG_MONTH dd where dd.stat_time=to_date(RIQI_S,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_LWHH_TAG_LOG_MONTH一个月的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DJ_LWHH_TAG_STAT_LOG_MONTH(SEQ,ACTIVE_SYS,SERVICE,LWHH,TAG,STAT_TIME,SUM) (select SEQ_DJ_LWHH_TAG_MONTH.nextval, ACTIVE_SYS, SERVICE, LWHH,TAG, shijian, shuliang from(select ACTIVE_SYS,SERVICE,LWHH,TAG,trunc(STAT_TIME, ''MM'') as shijian,sum(SUM) as shuliang from DJ_LWHH_TAG_STAT_LOG_DAILY where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd'') and to_date('''||RIQI_E||''',''yyyy-mm-dd'')  group by ACTIVE_SYS,SERVICE,LWHH,TAG,trunc(STAT_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_LWHH_TAG_LOG_MONTH一个月的日志结束 end'')';
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
prompt Creating procedure PRO_LWHH_TYPE_STAT_DFLOG_DAILY
prompt =================================================
prompt
create or replace procedure PRO_LWHH_TYPE_STAT_DFLOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_LWHH_TYPE_STAT_DFLOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_LWHH_TYPE_STAT_DFLOG_DAILY'; --当前存储过程统计业务类型
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
     delete from DF_LWHH_TYPE_STAT_LOG_DAILY dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_LWHH_TYPE_STAT_LOG_DAILY一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DF_LWHH_TYPE_STAT_LOG_DAILY(SEQ,ACTIVE_SYS, SERVICE,LWHH,ATTR_TYPE,STAT_TIME,SUM) (select SEQ_DF_LWHH_TYPE_DAILY.nextval, ACTIVE_SYS, SERVICE,LWHH, ATTR_TYPE, shijian, shuliang from(select ACTIVE_SYS, SERVICE,LWHH, ATTR_TYPE, trunc(REPORT_TIME, ''DD'') as shijian, sum(SUM) as shuliang from DF_LWHH_TYPE_REPORT where REPORT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:MI:SS'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:MI:SS'')  group by ACTIVE_SYS, SERVICE,LWHH,ATTR_TYPE,trunc(REPORT_TIME, ''DD'')))';
     commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_LWHH_TYPE_STAT_LOG_DAILY一天的日志结束 end'')';
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
prompt Creating procedure PRO_LWHH_TYPE_STAT_DFLOG_MONTH
prompt =================================================
prompt
create or replace procedure PRO_LWHH_TYPE_STAT_DFLOG_MONTH(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_LWHH_TYPE_STAT_DFLOG_MONTH'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_LWHH_TYPE_LOG_MONTH all'; --当前存储过程统计业务类型
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
     delete from DF_LWHH_TYPE_STAT_LOG_MONTH dd where dd.stat_time=to_date(RIQI_S,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_LWHH_TYPE_LOG_MONTH一个月的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DF_LWHH_TYPE_STAT_LOG_MONTH(SEQ,ACTIVE_SYS,SERVICE,LWHH,ATTR_TYPE,STAT_TIME,SUM) (select SEQ_DF_LWHH_TYPE_MONTH.nextval, ACTIVE_SYS, SERVICE, LWHH,ATTR_TYPE, shijian, shuliang from(select ACTIVE_SYS,SERVICE,LWHH,ATTR_TYPE,trunc(STAT_TIME, ''MM'') as shijian,sum(SUM) as shuliang from DF_LWHH_TYPE_STAT_LOG_DAILY where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd'') and to_date('''||RIQI_E||''',''yyyy-mm-dd'')  group by ACTIVE_SYS,SERVICE,LWHH,ATTR_TYPE,trunc(STAT_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_LWHH_TYPE_LOG_MONTH一个月的日志结束 end'')';
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
prompt Creating procedure PRO_LWHH_TYPE_STAT_DJLOG_DAILY
prompt =================================================
prompt
create or replace procedure PRO_LWHH_TYPE_STAT_DJLOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_LWHH_TYPE_STAT_DJLOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_LWHH_TYPE_STAT_DJLOG_DAILY'; --当前存储过程统计业务类型
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
     delete from DJ_LWHH_TYPE_STAT_LOG_DAILY dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_LWHH_TYPE_STAT_LOG_DAILY一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DJ_LWHH_TYPE_STAT_LOG_DAILY(SEQ,ACTIVE_SYS, SERVICE,LWHH,ATTR_TYPE,STAT_TIME,SUM) (select SEQ_DJ_LWHH_TYPE_DAILY.nextval, ACTIVE_SYS, SERVICE,LWHH, ATTR_TYPE, shijian, shuliang from(select ACTIVE_SYS, SERVICE,LWHH, ATTR_TYPE, trunc(REPORT_TIME, ''DD'') as shijian, sum(SUM) as shuliang from DJ_LWHH_TYPE_REPORT where REPORT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:MI:SS'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:MI:SS'')  group by ACTIVE_SYS, SERVICE,LWHH,ATTR_TYPE,trunc(REPORT_TIME, ''DD'')))';
     commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_LWHH_TYPE_STAT_LOG_DAILY一天的日志结束 end'')';
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
prompt Creating procedure PRO_LWHH_TYPE_STAT_DJLOG_MONTH
prompt =================================================
prompt
create or replace procedure PRO_LWHH_TYPE_STAT_DJLOG_MONTH(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_LWHH_TYPE_STAT_DJLOG_MONTH'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_LWHH_TYPE_LOG_MONTH all'; --当前存储过程统计业务类型
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
     delete from DJ_LWHH_TYPE_STAT_LOG_MONTH dd where dd.stat_time=to_date(RIQI_S,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_LWHH_TYPE_LOG_MONTH一个月的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DJ_LWHH_TYPE_STAT_LOG_MONTH(SEQ,ACTIVE_SYS,SERVICE,LWHH,ATTR_TYPE,STAT_TIME,SUM) (select SEQ_DJ_LWHH_TYPE_MONTH.nextval, ACTIVE_SYS, SERVICE, LWHH,ATTR_TYPE, shijian, shuliang from(select ACTIVE_SYS,SERVICE,LWHH,ATTR_TYPE,trunc(STAT_TIME, ''MM'') as shijian,sum(SUM) as shuliang from DJ_LWHH_TYPE_STAT_LOG_DAILY where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd'') and to_date('''||RIQI_E||''',''yyyy-mm-dd'')  group by ACTIVE_SYS,SERVICE,LWHH,ATTR_TYPE,trunc(STAT_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_LWHH_TYPE_LOG_MONTH一个月的日志结束 end'')';
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
prompt Creating procedure PRO_SRC_TAG_STAT_DFLOG_DAILY
prompt ===============================================
prompt
create or replace procedure PRO_SRC_TAG_STAT_DFLOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_SRCIP_TAG_STAT_DFLOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_SRCIP_TAG_STAT_DFLOG_DAILY'; --当前存储过程统计业务类型
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
     delete from DF_SRCIP_TAG_STAT_LOG_DAILY dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_SRCIP_TAG_STAT_LOG_DAILY一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DF_SRCIP_TAG_STAT_LOG_DAILY(SEQ,ACTIVE_SYS, SERVICE,SRC_PROVINCE,tag,STAT_TIME,SUM) (select SEQ_DF_SRCIP_TAG_DAILY.nextval, ACTIVE_SYS, SERVICE,SRC_PROVINCE, tag, shijian, shuliang from(select ACTIVE_SYS, SERVICE,SRC_PROVINCE, tag, trunc(REPORT_TIME, ''DD'') as shijian, sum(SUM) as shuliang from DF_SRCIP_DOMESTIC_TAG_REPORT where REPORT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:MI:SS'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:MI:SS'')  group by ACTIVE_SYS, SERVICE,SRC_PROVINCE,tag,trunc(REPORT_TIME, ''DD'')))';
     commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_SRCIP_TAG_STAT_LOG_DAILY一天的日志结束 end'')';
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
prompt Creating procedure PRO_SRC_TAG_STAT_DFLOG_MONTH
prompt ===============================================
prompt
create or replace procedure PRO_SRC_TAG_STAT_DFLOG_MONTH(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_SRC_TAG_STAT_DFLOG_MONTH'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_SRCIP_TAG_LOG_MONTH all'; --当前存储过程统计业务类型
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
     delete from DF_SRCIP_TAG_STAT_LOG_MONTH dd where dd.stat_time=to_date(RIQI_S,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_SRCIP_TAG_LOG_MONTH一个月的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DF_SRCIP_TAG_STAT_LOG_MONTH(SEQ,ACTIVE_SYS,SERVICE,SRC_PROVINCE,TAG,STAT_TIME,SUM) (select SEQ_DF_SRCIP_TAG_MONTH.nextval, ACTIVE_SYS, SERVICE, SRC_PROVINCE,TAG, shijian, shuliang from(select ACTIVE_SYS,SERVICE,SRC_PROVINCE,TAG,trunc(STAT_TIME, ''MM'') as shijian,sum(SUM) as shuliang from DF_SRCIP_TAG_STAT_LOG_DAILY where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd'') and to_date('''||RIQI_E||''',''yyyy-mm-dd'')  group by ACTIVE_SYS,SERVICE,SRC_PROVINCE,TAG,trunc(STAT_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_SRCIP_TAG_LOG_MONTH一个月的日志结束 end'')';
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
prompt Creating procedure PRO_SRC_TAG_STAT_DJLOG_DAILY
prompt ===============================================
prompt
create or replace procedure PRO_SRC_TAG_STAT_DJLOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_SRCIP_TAG_STAT_DJLOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_SRCIP_TAG_STAT_DJLOG_DAILY'; --当前存储过程统计业务类型
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
     delete from DJ_SRCIP_TAG_STAT_LOG_DAILY dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_SRCIP_TAG_STAT_LOG_DAILY一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DJ_SRCIP_TAG_STAT_LOG_DAILY(SEQ,ACTIVE_SYS, SERVICE,SRC_PROVINCE,tag,STAT_TIME,SUM) (select SEQ_DJ_SRCIP_TAG_DAILY.nextval, ACTIVE_SYS, SERVICE,SRC_PROVINCE, tag, shijian, shuliang from(select ACTIVE_SYS, SERVICE,SRC_PROVINCE, tag, trunc(REPORT_TIME, ''DD'') as shijian, sum(SUM) as shuliang from DJ_SRCIP_DOMESTIC_TAG_REPORT where REPORT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:MI:SS'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:MI:SS'')  group by ACTIVE_SYS, SERVICE,SRC_PROVINCE,tag,trunc(REPORT_TIME, ''DD'')))';
     commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_SRCIP_TAG_STAT_LOG_DAILY一天的日志结束 end'')';
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
prompt Creating procedure PRO_SRC_TAG_STAT_DJLOG_MONTH
prompt ===============================================
prompt
create or replace procedure PRO_SRC_TAG_STAT_DJLOG_MONTH(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_SRC_TAG_STAT_DJLOG_MONTH'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_SRCIP_TAG_LOG_MONTH all'; --当前存储过程统计业务类型
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
     delete from DJ_SRCIP_TAG_STAT_LOG_MONTH dd where dd.stat_time=to_date(RIQI_S,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_SRCIP_TAG_LOG_MONTH一个月的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DJ_SRCIP_TAG_STAT_LOG_MONTH(SEQ,ACTIVE_SYS,SERVICE,SRC_PROVINCE,TAG,STAT_TIME,SUM) (select SEQ_DJ_SRCIP_TAG_MONTH.nextval, ACTIVE_SYS, SERVICE, SRC_PROVINCE,TAG, shijian, shuliang from(select ACTIVE_SYS,SERVICE,SRC_PROVINCE,TAG,trunc(STAT_TIME, ''MM'') as shijian,sum(SUM) as shuliang from DJ_SRCIP_TAG_STAT_LOG_DAILY where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd'') and to_date('''||RIQI_E||''',''yyyy-mm-dd'')  group by ACTIVE_SYS,SERVICE,SRC_PROVINCE,TAG,trunc(STAT_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_SRCIP_TAG_LOG_MONTH一个月的日志结束 end'')';
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
prompt Creating procedure PRO_SRC_TYPE_STAT_DFLOG_DAILY
prompt ================================================
prompt
create or replace procedure PRO_SRC_TYPE_STAT_DFLOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_SRCIP_TYPE_STAT_DFLOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_SRCIP_TYPE_STAT_DFLOG_DAILY'; --当前存储过程统计业务类型
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
     delete from DF_SRCIP_TYPE_STAT_LOG_DAILY dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_SRCIP_TYPE_STAT_LOG_DAILY一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DF_SRCIP_TYPE_STAT_LOG_DAILY(SEQ,ACTIVE_SYS, SERVICE,SRC_PROVINCE,ATTR_TYPE,STAT_TIME,SUM) (select SEQ_DF_SRCIP_TYPE_DAILY.nextval, ACTIVE_SYS, SERVICE,SRC_PROVINCE, ATTR_TYPE, shijian, shuliang from(select ACTIVE_SYS, SERVICE,SRC_PROVINCE, ATTR_TYPE, trunc(REPORT_TIME, ''DD'') as shijian, sum(SUM) as shuliang from DF_SRCIP_DOMESTIC_TYPE_REPORT where REPORT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:MI:SS'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:MI:SS'')  group by ACTIVE_SYS, SERVICE,SRC_PROVINCE,ATTR_TYPE,trunc(REPORT_TIME, ''DD'')))';
     commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_SRCIP_TYPE_STAT_LOG_DAILY一天的日志结束 end'')';
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
prompt Creating procedure PRO_SRC_TYPE_STAT_DFLOG_MONTH
prompt ================================================
prompt
create or replace procedure PRO_SRC_TYPE_STAT_DFLOG_MONTH(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_SRC_TYPE_STAT_DFLOG_MONTH'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_SRCIP_TYPE_LOG_MONTH all'; --当前存储过程统计业务类型
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
     delete from DF_SRCIP_TYPE_STAT_LOG_MONTH dd where dd.stat_time=to_date(RIQI_S,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_SRCIP_TYPE_LOG_MONTH一个月的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DF_SRCIP_TYPE_STAT_LOG_MONTH(SEQ,ACTIVE_SYS,SERVICE,SRC_PROVINCE,ATTR_TYPE,STAT_TIME,SUM) (select SEQ_DF_SRCIP_TYPE_MONTH.nextval, ACTIVE_SYS, SERVICE, SRC_PROVINCE,ATTR_TYPE, shijian, shuliang from(select ACTIVE_SYS,SERVICE,SRC_PROVINCE,ATTR_TYPE,trunc(STAT_TIME, ''MM'') as shijian,sum(SUM) as shuliang from DF_SRCIP_TYPE_STAT_LOG_DAILY where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd'') and to_date('''||RIQI_E||''',''yyyy-mm-dd'')  group by ACTIVE_SYS,SERVICE,SRC_PROVINCE,ATTR_TYPE,trunc(STAT_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_SRCIP_TYPE_LOG_MONTH一个月的日志结束 end'')';
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
prompt Creating procedure PRO_SRC_TYPE_STAT_DJLOG_DAILY
prompt ================================================
prompt
create or replace procedure PRO_SRC_TYPE_STAT_DJLOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_SRCIP_TYPE_STAT_DJLOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_SRCIP_TYPE_STAT_DJLOG_DAILY'; --当前存储过程统计业务类型
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
     delete from DJ_SRCIP_TYPE_STAT_LOG_DAILY dd where dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_SRCIP_TYPE_STAT_LOG_DAILY一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DJ_SRCIP_TYPE_STAT_LOG_DAILY(SEQ,ACTIVE_SYS, SERVICE,SRC_PROVINCE,ATTR_TYPE,STAT_TIME,SUM) (select SEQ_DJ_SRCIP_TYPE_DAILY.nextval, ACTIVE_SYS, SERVICE,SRC_PROVINCE, ATTR_TYPE, shijian, shuliang from(select ACTIVE_SYS, SERVICE,SRC_PROVINCE, ATTR_TYPE, trunc(REPORT_TIME, ''DD'') as shijian, sum(SUM) as shuliang from DJ_SRCIP_DOMESTIC_TYPE_REPORT where REPORT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd HH24:MI:SS'') and to_date('''||RIQI_E||''',''yyyy-mm-dd HH24:MI:SS'')  group by ACTIVE_SYS, SERVICE,SRC_PROVINCE,ATTR_TYPE,trunc(REPORT_TIME, ''DD'')))';
     commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_SRCIP_TYPE_STAT_LOG_DAILY一天的日志结束 end'')';
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
prompt Creating procedure PRO_SRC_TYPE_STAT_DJLOG_MONTH
prompt ================================================
prompt
create or replace procedure PRO_SRC_TYPE_STAT_DJLOG_MONTH(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_SRC_TYPE_STAT_DJLOG_MONTH'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_SRCIP_TYPE_LOG_MONTH all'; --当前存储过程统计业务类型
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
     delete from DJ_SRCIP_TYPE_STAT_LOG_MONTH dd where dd.stat_time=to_date(RIQI_S,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_SRCIP_TYPE_LOG_MONTH一个月的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into DJ_SRCIP_TYPE_STAT_LOG_MONTH(SEQ,ACTIVE_SYS,SERVICE,SRC_PROVINCE,ATTR_TYPE,STAT_TIME,SUM) (select SEQ_DJ_SRCIP_TYPE_MONTH.nextval, ACTIVE_SYS, SERVICE, SRC_PROVINCE,ATTR_TYPE, shijian, shuliang from(select ACTIVE_SYS,SERVICE,SRC_PROVINCE,ATTR_TYPE,trunc(STAT_TIME, ''MM'') as shijian,sum(SUM) as shuliang from DJ_SRCIP_TYPE_STAT_LOG_DAILY where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd'') and to_date('''||RIQI_E||''',''yyyy-mm-dd'')  group by ACTIVE_SYS,SERVICE,SRC_PROVINCE,ATTR_TYPE,trunc(STAT_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_SRCIP_TYPE_LOG_MONTH一个月的日志结束 end'')';
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

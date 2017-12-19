--GK_LOG库

CREATE OR REPLACE TRIGGER TRG_DF_KEY_MAIL_ADDR_B BEFORE INSERT OR UPDATE ON DF_KEY_MAIL_ADDR
FOR EACH ROW

BEGIN
     SELECT SEQ_DF_KEY_MAIL_ADDR.NEXTVAL into :NEW.ID FROM DUAL;
END;

-- Create sequence 
create sequence SEQ_DF_KEY_CONVERT_MAIL_ADDR
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;


alter table df_stat_log_daily add LOG_TYPE char(1)  default 0;
alter table df_stat_log_month add LOG_TYPE char(1)  default 0;

--更新所有报表存储过程
---------------------------------------------
-- Export file for user GK_LOG             --
-- Created by dell on 2016/11/15, 17:33:15 --
---------------------------------------------

spool 20161115更新存储过程.log

prompt
prompt Creating procedure PRO_DF_DNS_LOG_DAILY
prompt =======================================
prompt
create or replace procedure PRO_DF_DNS_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DF_DNS_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_DNS_LOG 6'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
     --先删除前一次统计的数据信息,避免累计统计
     delete from df_stat_log_daily dd where service=6 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_DNS_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 6, shijian, shuliang,0 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from df_dns_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_DNS_LOG一天的日志开始 end'')';
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
prompt Creating procedure PRO_DF_FTP_LOG_DAILY
prompt =======================================
prompt
create or replace procedure PRO_DF_FTP_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DF_FTP_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_FTP_LOG 14'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
     --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
     delete from df_stat_log_daily dd where service=14 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_FTP_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 14, shijian, shuliang,0 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from df_ftp_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_FTP_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
  end;
    ---------------------------------------
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
prompt Creating procedure PRO_DF_FTP_LOG_MONTH
prompt =======================================
prompt
create or replace procedure PRO_DF_FTP_LOG_MONTH(NUMDAYS INT)
--DF_FTP月统计，基于日报表进行，每月月初统计上个月月报
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DF_FTP_LOG_MONTH'; --当前存储过程的名称     
     CTYPE VARCHAR2(100) :='DF_FTP_LOG'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin  
  begin    
    RIQI   := to_char(trunc(sysdate-1-NUMDAYS, 'month'), 'yyyy-MM');
    RIQI_S := to_char(trunc(sysdate-1-NUMDAYS, 'month'), 'yyyy-MM-dd'); -- 前N天所在月月初
    RIQI_S := RIQI_S || ' 00:00:00';
    RIQI_E := to_char(last_day(sysdate-1-NUMDAYS), 'yyyy-mm-dd'); -- 前N天所在月月末
    RIQI_E := RIQI_E || ' 23:59:59';
     --先删除要统计月的统计的数据信息，清理之前该月统计然后重新统计，避免累计统计     
     delete from df_stat_log_month dm where dm.stat_time=to_date(RIQI,'yyyy-MM');
     commit;
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_FTP月报 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert \*+ append *\ into df_stat_log_month(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, SERVICE_TYPE, shijian, shuliang,0 from(select CFG_ID,SERVICE_TYPE,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from df_stat_log_daily where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,SERVICE_TYPE,trunc(FOUND_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_FTP月报 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
  end; 
    ---------------------------------------
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
prompt Creating procedure PRO_DF_HTTP_KEYWORD_LOG_DAILY
prompt ================================================
prompt
create or replace procedure PRO_DF_HTTP_KEYWORD_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DF_HTTP_KEYWORD_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_HTTP_KEYWORD_LOG 14'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
     delete from df_stat_log_daily dd where service=14 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_HTTP_KEYWORD_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, SERVICE_TYPE, shijian, shuliang,0 from(select CFG_ID,SERVICE_TYPE,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from df_http_keyword_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,SERVICE_TYPE,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_HTTP_KEYWORD_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
    ---------------------------------------
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
prompt Creating procedure PRO_DF_HTTP_REQ_LOG_DAILY
prompt ============================================
prompt
create or replace procedure PRO_DF_HTTP_REQ_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DF_HTTP_REQ_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_HTTP_REQ_LOG 3'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
    --df_http_req_log
     delete from df_stat_log_daily dd where service=3 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_HTTP_REQ_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 3, shijian, shuliang,0 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from df_http_req_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_HTTP_REQ_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
    /*execute immediate 'insert \*+ append *\ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM) (select seq_df_stat_log_daily.nextval, CFG_ID, SERVICE_TYPE, shijian, shuliang from(select CFG_ID,SERVICE_TYPE,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from df_http_req_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,SERVICE_TYPE,trunc(FOUND_TIME, ''DD'')))';
    commit;*/
    ---------------------------------------
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
prompt Creating procedure PRO_DF_HTTP_RES_LOG_DAILY
prompt ============================================
prompt
create or replace procedure PRO_DF_HTTP_RES_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DF_HTTP_RES_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_HTTP_RES_LOG 4'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
    --df_http_res_log
     delete from df_stat_log_daily dd where service=4 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_HTTP_RES_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 4, shijian, shuliang,0 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from df_http_res_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_HTTP_RES_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;

    ---------------------------------------
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
prompt Creating procedure PRO_DF_IPSEC_LOG_DAILY
prompt =========================================
prompt
create or replace procedure PRO_DF_IPSEC_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DF_IPSEC_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_IPSEC_LOG 9'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
    --DF_IPSEC_LOG
     delete from df_stat_log_daily dd where service=9 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_IPSEC_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
   execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 9, shijian, shuliang,0 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from df_ipsec_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_IPSEC_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
    

    ---------------------------------------
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
prompt Creating procedure PRO_DF_IP_PORT_LOG_DAILY
prompt ===========================================
prompt
create or replace procedure PRO_DF_IP_PORT_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DF_IP_PORT_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_IP_PORT_LOG 1'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
    --DF_IP_PORT_LOG
     delete from df_stat_log_daily dd where service=1 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_IP_PORT_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
   execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 1, shijian, shuliang,0 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from df_ip_port_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_IP_PORT_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
   

    ---------------------------------------
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
prompt Creating procedure PRO_DF_L2TP_LOG_DAILY
prompt ========================================
prompt
create or replace procedure PRO_DF_L2TP_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DF_L2TP_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_L2TP_LOG 8'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
    --DF_L2TP_LOG
     delete from df_stat_log_daily dd where service=8 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_L2TP_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
  execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 8, shijian, shuliang,0 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from df_l2tp_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_L2TP_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
    

    ---------------------------------------
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
prompt Creating procedure PRO_DF_MAIL_LOG_DAILY
prompt ========================================
prompt
create or replace procedure PRO_DF_MAIL_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DF_MAIL_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_MAIL_LOG 13'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
    --DF_MAIL_LOG
     delete from df_stat_log_daily dd where service=13 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_MAIL_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 13, shijian, shuliang,0 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from df_mail_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_MAIL_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
    

    ---------------------------------------
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
prompt Creating procedure PRO_DF_OPENVPN_LOG_DAILY
prompt ===========================================
prompt
create or replace procedure PRO_DF_OPENVPN_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DF_OPENVPN_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_OPENVPN_LOG 10'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
    --DF_OPENVPN_LOG
     delete from df_stat_log_daily dd where service=10 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_OPENVPN_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 10, shijian, shuliang,0 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from df_openvpn_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_OPENVPN_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
    

    ---------------------------------------
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
prompt Creating procedure PRO_DF_PPTP_LOG_DAILY
prompt ========================================
prompt
create or replace procedure PRO_DF_PPTP_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DF_PPTP_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_PPTP_LOG 7'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
    --DF_PPTP_LOG
     delete from df_stat_log_daily dd where service=7 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_PPTP_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 7, shijian, shuliang,0 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from df_pptp_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_PPTP_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
   

    ---------------------------------------
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
prompt Creating procedure PRO_DF_SSH_LOG_DAILY
prompt =======================================
prompt
create or replace procedure PRO_DF_SSH_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DF_SSH_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_SSH_LOG 11'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
    --DF_SSH_LOG
     delete from df_stat_log_daily dd where service=11 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_SSH_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 11, shijian, shuliang,0 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from df_ssh_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_SSH_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
    

    ---------------------------------------
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
prompt Creating procedure PRO_DF_SSL_LOG_DAILY
prompt =======================================
prompt
create or replace procedure PRO_DF_SSL_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DF_SSL_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_SSL_LOG 12'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
    --DF_SSL_LOG
     delete from df_stat_log_daily dd where service=12 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_SSL_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
     execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 12, shijian, shuliang,0 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from df_ssl_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_SSL_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
    
    ---------------------------------------
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
prompt Creating procedure PRO_DJ_DNS_LOG_DAILY
prompt =======================================
prompt
create or replace procedure PRO_DJ_DNS_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DJ_DNS_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_DNS_LOG 52'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
    --DJ_DNS_LOG
     delete from df_stat_log_daily dd where service=52 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_DNS_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
     execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 52, shijian, shuliang,1 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from dj_dns_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_DNS_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
   

    ---------------------------------------
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
prompt Creating procedure PRO_DJ_FTP_LOG_DAILY
prompt =======================================
prompt
create or replace procedure PRO_DJ_FTP_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DJ_FTP_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_FTP_LOG 53'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
    --DJ_FTP_LOG
     delete from df_stat_log_daily dd where service=53 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_FTP_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
     execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 53, shijian, shuliang,1 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from dj_ftp_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_FTP_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
    

    ---------------------------------------
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
prompt Creating procedure PRO_DJ_HTTP_KEYWORD_LOG_DAILY
prompt ================================================
prompt
create or replace procedure PRO_DJ_HTTP_KEYWORD_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DJ_HTTP_KEYWORD_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_HTTP_KEYWORD_LOG 51'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
    --DJ_HTTP_KEYWORD_LOG
     delete from df_stat_log_daily dd where service=51 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_HTTP_KEYWORD_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
     execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 51, shijian, shuliang,1 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from dj_http_keyword_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_HTTP_KEYWORD_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
    

    ---------------------------------------
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
prompt Creating procedure PRO_DJ_HTTP_REQ_LOG_DAILY
prompt ============================================
prompt
create or replace procedure PRO_DJ_HTTP_REQ_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DJ_HTTP_REQ_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_HTTP_REQ_LOG 49'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
    --DJ_HTTP_REQ_LOG
     delete from df_stat_log_daily dd where service=49 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_HTTP_REQ_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
     execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 49, shijian, shuliang,1 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from dj_http_req_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_HTTP_REQ_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
    

    ---------------------------------------
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
prompt Creating procedure PRO_DJ_HTTP_RES_LOG_DAILY
prompt ============================================
prompt
create or replace procedure PRO_DJ_HTTP_RES_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DJ_HTTP_RES_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_HTTP_RES_LOG 50'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
    --DJ_HTTP_RES_LOG
     delete from df_stat_log_daily dd where service=50 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_HTTP_RES_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
     execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 50, shijian, shuliang,1 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from dj_http_res_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_HTTP_RES_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
    

    ---------------------------------------
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
prompt Creating procedure PRO_DJ_IPSEC_LOG_DAILY
prompt =========================================
prompt
create or replace procedure PRO_DJ_IPSEC_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DJ_IPSEC_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_IPSEC_LOG 56'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
    --DJ_IPSEC_LOG
     delete from df_stat_log_daily dd where service=56 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_IPSEC_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
      execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 56, shijian, shuliang,1 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from dj_ipsec_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_IPSEC_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
    

    ---------------------------------------
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
prompt Creating procedure PRO_DJ_IP_PORT_LOG_DAILY
prompt ===========================================
prompt
create or replace procedure PRO_DJ_IP_PORT_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DJ_IP_PORT_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_IP_PORT_LOG 48'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
    --DJ_IP_PORT_LOG
     delete from df_stat_log_daily dd where service=48 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_IP_PORT_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
      execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 48, shijian, shuliang,1 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from dj_ip_port_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_IP_PORT_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
    
    ---------------------------------------
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
prompt Creating procedure PRO_DJ_L2TP_LOG_DAILY
prompt ========================================
prompt
create or replace procedure PRO_DJ_L2TP_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DJ_L2TP_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_L2TP_LOG 55'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
    --DJ_L2TP_LOG
     delete from df_stat_log_daily dd where service=55 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_L2TP_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
     execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 55, shijian, shuliang,1 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from dj_l2tp_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_L2TP_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
    
    ---------------------------------------
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
prompt Creating procedure PRO_DJ_MAIL_LOG_DAILY
prompt ========================================
prompt
create or replace procedure PRO_DJ_MAIL_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DJ_MAIL_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_MAIL_LOG 60'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
    --DJ_MAIL_LOG
     delete from df_stat_log_daily dd where service=60 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_MAIL_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
     execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 60, shijian, shuliang,1 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from dj_mail_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_MAIL_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
   --------------------
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
prompt Creating procedure PRO_DJ_OPENVPN_LOG_DAILY
prompt ===========================================
prompt
create or replace procedure PRO_DJ_OPENVPN_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DJ_OPENVPN_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_OPENVPN_LOG 57'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
    --DJ_OPENVPN_LOG
     delete from df_stat_log_daily dd where service=57 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_OPENVPN_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
     execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 57, shijian, shuliang,1 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from dj_openvpn_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_OPENVPN_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
    
    ---------------------------------------
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
prompt Creating procedure PRO_DJ_PPTP_LOG_DAILY
prompt ========================================
prompt
create or replace procedure PRO_DJ_PPTP_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DJ_PPTP_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_PPTP_LOG 54'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
    --DJ_PPTP_LOG
     delete from df_stat_log_daily dd where service=54 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_PPTP_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
     execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 54, shijian, shuliang,1 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from dj_pptp_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_PPTP_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
    
    ---------------------------------------
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
prompt Creating procedure PRO_DJ_SSH_LOG_DAILY
prompt =======================================
prompt
create or replace procedure PRO_DJ_SSH_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DJ_SSH_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_SSH_LOG 58'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
    --DJ_SSH_LOG
     delete from df_stat_log_daily dd where service=58 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_SSH_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
      execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 58, shijian, shuliang,1 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from dj_ssh_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
      commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_SSH_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
    ---------------------------------------
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
prompt Creating procedure PRO_DJ_SSL_LOG_DAILY
prompt =======================================
prompt
create or replace procedure PRO_DJ_SSL_LOG_DAILY(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_DJ_SSL_LOG_DAILY'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DJ_SSL_LOG 59'; --当前存储过程统计业务类型
     v_log_table_sql               VARCHAR2(5000);--写入日志log
     v_errortext varchar2(1000);   --出错的信息
    RIQI  VARCHAR(32);
    RIQI_S  VARCHAR(32);
    RIQI_E  VARCHAR(32);
begin
  --declare
  begin
    RIQI := to_char(sysdate-NUMDAYS, 'yyyy-mm-dd');
    RIQI_S := RIQI || '00:00:00';
    RIQI_E := RIQI || '23:59:59';
    --先删除要统计天统计的数据信息，避免补充历史日志统计信息时重复统计同一天数据。
    --DJ_SSL_LOG
     delete from df_stat_log_daily dd where service=59 and dd.stat_time=to_date(RIQI,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DJ_SSL_LOG一天的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
     execute immediate 'insert /*+ append */ into df_stat_log_daily(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_daily.nextval, CFG_ID, 59, shijian, shuliang,1 from(select CFG_ID,trunc(FOUND_TIME, ''DD'') as shijian,count(*) as shuliang from dj_ssl_log where FOUND_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd hh24:mi:ss'') and to_date('''||RIQI_E||''',''yyyy-mm-dd hh24:mi:ss'')  group by CFG_ID,trunc(FOUND_TIME, ''DD'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DJ_SSL_LOG一天的日志开始 end'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    end;
       ---------------------------------------
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
prompt Creating procedure PRO_STAT_LOG_MONTH
prompt =====================================
prompt
create or replace procedure PRO_STAT_LOG_MONTH(NUMDAYS INT)
Authid Current_User----使存储过程可以使用role权限
 as
     c_log_table_name              CONSTANT VARCHAR2(100) :='PRO_EXEC_LOG'; --日志记录在此表内
     c_cur_pro_name                CONSTANT VARCHAR2(100) :='PRO_STAT_LOG_MONTH'; --当前存储过程的名称
     CTYPE VARCHAR2(100) :='DF_LOG all'; --当前存储过程统计业务类型
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
     delete from df_stat_log_month dd where dd.stat_time=to_date(RIQI_S,'yyyy-MM-DD');
    --------------------------------------------------------------------------------------------------------
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''1.统计DF_LOG一个月的日志开始 begin'')';
     EXECUTE IMMEDIATE v_log_table_sql;
     COMMIT;
    execute immediate 'insert /*+ append */ into df_stat_log_month(SEQ,CONFIG_ID,SERVICE,STAT_TIME,SUM,LOG_TYPE) (select seq_df_stat_log_month.nextval, CONFIG_ID, SERVICE, shijian, shuliang,LOG_TYPE from(select SERVICE,CONFIG_ID,trunc(STAT_TIME, ''MM'') as shijian,sum(SUM) as shuliang,LOG_TYPE from df_stat_log_daily where STAT_TIME between to_date('''||RIQI_S||''',''yyyy-mm-dd'') and to_date('''||RIQI_E||''',''yyyy-mm-dd'')  group by LOG_TYPE,SERVICE,CONFIG_ID,trunc(STAT_TIME, ''MM'')))';
    commit;
     v_log_table_sql :=  'INSERT INTO '||c_log_table_name||'(id,proname,type,log_cont)
     VALUES(SEQ_PRO_EXEC_LOG.nextval,'''||c_cur_pro_name||''','''|| CTYPE ||''',''2.统计DF_LOG一个月的日志开始 end'')';
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

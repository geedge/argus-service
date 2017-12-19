--2016.10.14:2.5.30董晓燕	1.	增加5.8.	DNS欺骗IP地址池配置表DNS_FAKE_IP_POOL，业务：由界面配置IP池信息，后台根据讨论策略同步数据到DNS_FAKE_IP表
--gk_pz库
-- Create table
create table DNS_FAKE_IP_POOL
(
  ID            NUMBER not null,
  GROUP_ID      INTEGER not null,
  ADDR_TYPE     INTEGER not null,
  SRC_IP        VARCHAR2(64) not null,
  MASK_SRC_IP   VARCHAR2(64) not null,
  SRC_PORT      VARCHAR2(6) not null,
  MASK_SRC_PORT VARCHAR2(6) not null,
  DST_IP        VARCHAR2(64) not null,
  MASK_DST_IP   VARCHAR2(64) not null,
  DST_PORT      VARCHAR2(6) not null,
  MASK_DST_PORT VARCHAR2(6) not null,
  PROTOCOL      INTEGER default 0 not null,
  DIRECTION     INTEGER default 0 not null,
  IS_VALID      INTEGER not null,
  OP_TIME       DATE not null,
  YL1           INTEGER,
  YL2           INTEGER,
  YL3           VARCHAR2(80),
  YL4           VARCHAR2(80),
  YL5           VARCHAR2(80),
  LAST_UPDATE   DATE,
  PROC_SEQ      NUMBER
)
tablespace GK_PZ
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 8K
    minextents 1
    maxextents unlimited
  );
-- Add comments to the columns 
comment on column DNS_FAKE_IP_POOL.ID
  is '主键自增SEQ_DNS_FAKE_IP_POOL.nextval';
comment on column DNS_FAKE_IP_POOL.GROUP_ID
  is 'DNS_GROUP_TYPE（GROUP_ID）；
0:无策略欺骗IP，并且IP不需要发SYN_ACK欺骗
1: 无策略欺骗IP，并且IP需要发SYN_ACK欺骗
';
comment on column DNS_FAKE_IP_POOL.ADDR_TYPE
  is 'Ipv4=4,ipv6=6';
comment on column DNS_FAKE_IP_POOL.SRC_IP
  is '0.0.0.0值表示任意';
comment on column DNS_FAKE_IP_POOL.MASK_SRC_IP
  is 'IPv4:255.255.255.255表示无掩码，即精确IP匹配，0.0.0.0值表示任意；
IPv6：ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff表示无掩码，::（两个半角冒号）表示任意。
';
comment on column DNS_FAKE_IP_POOL.SRC_PORT
  is '0值表示任意';
comment on column DNS_FAKE_IP_POOL.MASK_SRC_PORT
  is '65535表示无掩码，即精确端口匹配，0表示任意';
comment on column DNS_FAKE_IP_POOL.DST_IP
  is '同源IP地址';
comment on column DNS_FAKE_IP_POOL.MASK_DST_IP
  is '同源IP掩码';
comment on column DNS_FAKE_IP_POOL.DST_PORT
  is '同源端口';
comment on column DNS_FAKE_IP_POOL.MASK_DST_PORT
  is '同源端口掩码';
comment on column DNS_FAKE_IP_POOL.PROTOCOL
  is '6表示TCP，17表示UDP，无限制默认为0';
comment on column DNS_FAKE_IP_POOL.DIRECTION
  is '0双向，1单向';
comment on column DNS_FAKE_IP_POOL.IS_VALID
  is '0无效，1有效';
comment on column DNS_FAKE_IP_POOL.OP_TIME
  is '当前时间';
comment on column DNS_FAKE_IP_POOL.LAST_UPDATE
  is '本地操作更新时间，后台生成';
comment on column DNS_FAKE_IP_POOL.PROC_SEQ
  is '版本序列号，由SEQ_MESSAGE序列生成，触发器生成';
-- Create/Recreate primary, unique and foreign key constraints 
alter table DNS_FAKE_IP_POOL
  add constraint PK_DNS_FAKE_IP_POOL primary key (ID)
  using index 
  tablespace GK_PZ
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
--gk_pz库
-- Create sequence 
create sequence SEQ_DNS_FAKE_IP_POOL
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

--gk_log库
--2016-10-13:2.5.29	2016.10.13	杨嵘	
--2.	日志公共部分中SERVER_PORT在PPTP业务中的含义为服务端CALL_ID;CLIENT_PORT在PPTP业务中的含义为客户端CALL_ID;
--该项属于业务理解范畴不调整表格和服务

--3.	在L2TP和PPTP封堵日志中增加CONTENT_TYPE字段
alter table DF_PPTP_LOG add  CONTENT_TYPE INTEGER;
alter table DJ_PPTP_LOG add  CONTENT_TYPE INTEGER;
alter table DF_L2TP_LOG add  CONTENT_TYPE INTEGER;
alter table DJ_L2TP_LOG add  CONTENT_TYPE INTEGER;

--gk_log库
--2.5.28	2016.10.13	 张成伟	DF_MAIL_LOG日志表FFROM改为MAIL_FROM，TTO改为MAIL_TO
alter table DF_MAIL_LOG rename column FFROM to MAIL_FROM;
alter table DF_MAIL_LOG rename column TTO to MAIL_TO;

--gk_log库
--2016.10.12	2.5.27	张成伟	公共库表增加封堵包INJECTED_PKT_FILE字段
alter table df_dns_log add  INJECTED_PKT_FILE VARCHAR2(256);
alter table df_ftp_log add  INJECTED_PKT_FILE VARCHAR2(256);
alter table df_http_keyword_log add  INJECTED_PKT_FILE VARCHAR2(256);
alter table df_http_req_log add  INJECTED_PKT_FILE VARCHAR2(256);
alter table df_http_res_log add  INJECTED_PKT_FILE VARCHAR2(256);
alter table df_ipsec_log add  INJECTED_PKT_FILE VARCHAR2(256);
alter table df_ip_port_log add  INJECTED_PKT_FILE VARCHAR2(256);
alter table df_l2tp_log add  INJECTED_PKT_FILE VARCHAR2(256);
alter table df_mail_log add  INJECTED_PKT_FILE VARCHAR2(256);
alter table df_openvpn_log add  INJECTED_PKT_FILE VARCHAR2(256);
alter table df_pptp_log add  INJECTED_PKT_FILE VARCHAR2(256);
alter table df_ssh_log add  INJECTED_PKT_FILE VARCHAR2(256);
alter table df_ssl_log add  INJECTED_PKT_FILE VARCHAR2(256);
--alter table dj_dns_log add  INJECTED_PKT_FILE VARCHAR2(256);
--alter table dj_ftp_log add  INJECTED_PKT_FILE VARCHAR2(256);
--alter table dj_http_keyword_log add  INJECTED_PKT_FILE VARCHAR2(256);
--alter table dj_http_req_log add  INJECTED_PKT_FILE VARCHAR2(256);
--alter table dj_http_res_log add  INJECTED_PKT_FILE VARCHAR2(256);
--alter table dj_ipsec_log add  INJECTED_PKT_FILE VARCHAR2(256);
--alter table dj_ip_port_log add  INJECTED_PKT_FILE VARCHAR2(256);
--alter table dj_l2tp_log add  INJECTED_PKT_FILE VARCHAR2(256);
--alter table dj_mail_log add  INJECTED_PKT_FILE VARCHAR2(256);
--alter table dj_openvpn_log add  INJECTED_PKT_FILE VARCHAR2(256);
--alter table dj_pptp_log add  INJECTED_PKT_FILE VARCHAR2(256);
--alter table dj_ssh_log add  INJECTED_PKT_FILE VARCHAR2(256);
--alter table dj_ssl_log add  INJECTED_PKT_FILE VARCHAR2(256);

--gk_log库
--2016-10-11:Number位数较大时，Java Integer和Long型不能满足，改为设置数据库show numw，然后代码按Bigdecimal获取按字符串返回给服务调用者；
 --设置数据库show numw
 set numw 50
 
 
--2016-10-10:杨威2.5.26版数据库涉及更新内容

对应配置库
--gk_pz库
--1。与郑超讨论后决定出于业务组织分布,封堵业务大量配置将集中在URL字段,因此将URL单独设置配置表,对应5.1和6.1分别增加DF_HTTP_URL和DJ_HTTP_URL配置表
--DF_HTTP_URL
-- Create table
create table DF_HTTP_URL
(
  REGION_ID    NUMBER not null,
  GROUP_ID     NUMBER not null,
  KEYWORDS     VARCHAR2(1024) not null,
  EXPR_TYPE    NUMBER not null,
  MATCH_METHOD NUMBER not null,
  IS_HEXBIN    NUMBER default 0 not null,
  IS_VALID     NUMBER not null,
  OP_TIME      DATE not null,
  PROC_SEQ     NUMBER,
  LAST_UPDATE  DATE
)
tablespace GK_PZ
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
-- Add comments to the columns 
comment on column DF_HTTP_URL.REGION_ID
  is '由SEQ_REGIONID.nextval生成，主键';
comment on column DF_HTTP_URL.GROUP_ID
  is '关联管控配置分组表中的group_id由SEQ_GROUPID.nextval生成';
comment on column DF_HTTP_URL.KEYWORDS
  is 'expr_type:1,时， keywords中’&’为与运算操作符,子表达式中的’&’符号用’\&’转义。';
comment on column DF_HTTP_URL.EXPR_TYPE
  is '0:无表达式,1:表示为与表达式';
comment on column DF_HTTP_URL.MATCH_METHOD
  is 'expr_type:0时有意义，其它情况必须置0。
0：子串匹配；1：右匹配；2：左匹配；3：完全匹配
';
comment on column DF_HTTP_URL.IS_HEXBIN
  is '默认为0:大小写不敏感，且非HEX
1:HEX格式二进制
2:大小写敏感，且非HEX
二进制格式是一种特殊的编码，受table_info.conf文件中do_merge控制
';
comment on column DF_HTTP_URL.IS_VALID
  is '0无效，1有效';
-- Create/Recreate primary, unique and foreign key constraints 
alter table DF_HTTP_URL
  add constraint PK_DF_HTTP_URL primary key (REGION_ID)
  using index 
  tablespace GK_PZ
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

--gk_pz库  
--对应触发器
CREATE OR REPLACE TRIGGER TRG_DJ_HTTP_URL_BEFORE BEFORE INSERT OR UPDATE ON DJ_HTTP_URL
FOR EACH ROW
BEGIN
     SELECT SEQ_MESSAGE.NEXTVAL into :NEW.PROC_SEQ FROM DUAL;
END;
--gk_pz库
CREATE OR REPLACE TRIGGER TRG_DJ_HTTP_URL_AFTER AFTER INSERT OR UPDATE ON DJ_HTTP_URL
FOR EACH ROW
DECLARE
is_exist int :=0;
BEGIN
     SELECT COUNT(*) INTO is_exist FROM MESA_PZ_ZT WHERE upper(TABLE_NAME)='DJ_HTTP_URL';
     IF is_exist>0 THEN
           UPDATE MESA_PZ_ZT SET PROC_SEQ=:NEW.PROC_SEQ WHERE upper(TABLE_NAME)='DJ_HTTP_URL';
     ELSE
           INSERT INTO MESA_PZ_ZT(TABLE_NAME,PROC_SEQ) VALUES ('DJ_HTTP_URL',:NEW.PROC_SEQ);
     END IF;
END;

--gk_pz库
--DJ_HTTP_URL
-- Create table
create table DJ_HTTP_URL
(
  REGION_ID    NUMBER not null,
  GROUP_ID     NUMBER not null,
  KEYWORDS     VARCHAR2(1024) not null,
  EXPR_TYPE    NUMBER not null,
  MATCH_METHOD NUMBER not null,
  IS_HEXBIN    NUMBER default 0 not null,
  IS_VALID     NUMBER not null,
  OP_TIME      DATE not null,
  PROC_SEQ     NUMBER,
  LAST_UPDATE  DATE
)
tablespace GK_PZ
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
-- Add comments to the columns 
comment on column DJ_HTTP_URL.REGION_ID
  is '由SEQ_REGIONID.nextval生成，主键';
comment on column DJ_HTTP_URL.GROUP_ID
  is '关联管控配置分组表中的group_id由SEQ_GROUPID.nextval生成';
comment on column DJ_HTTP_URL.KEYWORDS
  is 'expr_type:1,时， keywords中’&’为与运算操作符,子表达式中的’&’符号用’\&’转义。';
comment on column DJ_HTTP_URL.EXPR_TYPE
  is '0:无表达式,1:表示为与表达式';
comment on column DJ_HTTP_URL.MATCH_METHOD
  is 'expr_type:0时有意义，其它情况必须置0。
0：子串匹配；1：右匹配；2：左匹配；3：完全匹配
';
comment on column DJ_HTTP_URL.IS_HEXBIN
  is '默认为0:大小写不敏感，且非HEX
1:HEX格式二进制
2:大小写敏感，且非HEX
二进制格式是一种特殊的编码，受table_info.conf文件中do_merge控制
';
comment on column DJ_HTTP_URL.IS_VALID
  is '0无效，1有效';
-- Create/Recreate primary, unique and foreign key constraints 
alter table DJ_HTTP_URL
  add constraint PK_DJ_HTTP_URL primary key (REGION_ID)
  using index 
  tablespace GK_PZ
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
--gk_pz库  
--对应触发器
CREATE OR REPLACE TRIGGER TRG_DF_HTTP_URL_BEFORE BEFORE INSERT OR UPDATE ON DF_HTTP_URL
FOR EACH ROW
BEGIN
     SELECT SEQ_MESSAGE.NEXTVAL into :NEW.PROC_SEQ FROM DUAL;
END;

CREATE OR REPLACE TRIGGER TRG_DF_HTTP_URL_AFTER AFTER INSERT OR UPDATE ON DF_HTTP_URL
FOR EACH ROW
DECLARE
is_exist int :=0;
BEGIN
     SELECT COUNT(*) INTO is_exist FROM MESA_PZ_ZT WHERE upper(TABLE_NAME)='DF_HTTP_URL';
     IF is_exist>0 THEN
           UPDATE MESA_PZ_ZT SET PROC_SEQ=:NEW.PROC_SEQ WHERE upper(TABLE_NAME)='DF_HTTP_URL';
     ELSE
           INSERT INTO MESA_PZ_ZT(TABLE_NAME,PROC_SEQ) VALUES ('DF_HTTP_URL',:NEW.PROC_SEQ);
     END IF;
END;

--gk_log库  
对应日志库
-- 2.应哈工大要求,10.9增加chap_name日志字段
alter table df_l2tp_log add  CHAP_NAME VARCHAR2(256);
alter table dj_l2tp_log add  CHAP_NAME VARCHAR2(256);

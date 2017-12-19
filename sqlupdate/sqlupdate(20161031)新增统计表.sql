--soq文本数据库设计_内部开发版本
--2.5.34 
--增加实时统计所需表13.13-13.17分别为：
--DF_PZ_REPORT\DF_SERVICE_REPORT\DF_TAG_REPORT
--DF_SRCIP_DOMESTIC_REPORT\DF_DESTIP_COUNTRY_REPORT

-- Create table
create table DF_PZ_REPORT
(
  STAT_ID     NUMBER not null,
  ACTIVE_SYS  INTEGER not null,
  CFG_ID      NUMBER not null,
  SUM         NUMBER not null,
  REPORT_TIME DATE not null
)
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255;
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
  maxtrans 255;

-- Create table
create table DF_SERVICE_REPORT
(
  STAT_ID     NUMBER not null,
  ACTIVE_SYS  INTEGER not null,
  SERVICE     INTEGER not null,
  SUM         NUMBER not null,
  REPORT_TIME DATE not null
)
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table DF_SERVICE_REPORT
  is '该表只在B版日志数据库中';
-- Create/Recreate primary, unique and foreign key constraints 
alter table DF_SERVICE_REPORT
  add constraint PK_DF_SERVICE_REPORT primary key (STAT_ID)
  using index 
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255;

-- Create table
create table DF_TAG_REPORT
(
  STAT_ID     NUMBER not null,
  ACTIVE_SYS  INTEGER not null,
  TAG         INTEGER not null,
  SUM         NUMBER not null,
  REPORT_TIME DATE not null
)
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table DF_TAG_REPORT
  is '该表只在B版日志数据库中';
-- Create/Recreate primary, unique and foreign key constraints 
alter table DF_TAG_REPORT
  add constraint PK_DF_TAG_REPORT primary key (STAT_ID)
  using index 
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255;

-- Create table
create table DF_SRCIP_DOMESTIC_REPORT
(
  STAT_ID      NUMBER not null,
  ACTIVE_SYS   INTEGER not null,
  SRC_PROVINCE VARCHAR2(256) not null,
  SRC_CITY     VARCHAR2(256) not null,
  SUM          NUMBER not null,
  REPORT_TIME  DATE not null
)
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table DF_SRCIP_DOMESTIC_REPORT
  is '该表只在B版日志数据库中';
-- Create/Recreate primary, unique and foreign key constraints 
alter table DF_SRCIP_DOMESTIC_REPORT
  add constraint PK_DF_SRCIP_DOMESTIC_REPORT primary key (STAT_ID)
  using index 
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255;

-- Create table
create table DF_DESTIP_COUNTRY_REPORT
(
  STAT_ID      NUMBER not null,
  ACTIVE_SYS   INTEGER not null,
  DEST_COUNTRY VARCHAR2(256) not null,
  SUM          NUMBER not null,
  REPORT_TIME   DATE not null
)
tablespace GK_LOG
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table DF_DESTIP_COUNTRY_REPORT
  is '该表只在B版日志数据库中';
-- Create/Recreate primary, unique and foreign key constraints 
alter table DF_DESTIP_COUNTRY_REPORT
  add constraint PK_DF_DESTIP_COUNTRY_REPORT primary key (STAT_ID)
  using index 
  tablespace GK_LOG
  pctfree 10
  initrans 2
  maxtrans 255;
  
-- Create sequence 
create sequence SEQ_DF_PZ_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

-- Create sequence 
create sequence SEQ_DF_SERVICE_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

-- Create sequence 
create sequence SEQ_DF_TAG_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

-- Create sequence 
create sequence SEQ_DF_SRCIP_DOMESTIC_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

-- Create sequence 
create sequence SEQ_DF_DESTIP_COUNTRY_REPORT
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;
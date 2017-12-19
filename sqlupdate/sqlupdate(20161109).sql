JK_PZ库，调整如下：
-- Drop columns 
alter table JK_FD_ZB drop column ADDR_TYPE;
-- Add/modify columns 
alter table JK_FD_ZB add ISP_RAW raw(16) not null;


GK_LOG库调整如下：
1、新增表
-- Create table
create table DF_KEY_MAIL_ADDR
(
  ID      INTEGER not null,
  KEY_ID  INTEGER not null,
  MAIL_ADDR     VARCHAR2(1024) not null,
  OP_TIME DATE not null
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
-- Add comments to the columns 
comment on column DF_KEY_MAIL_ADDR.ID
  is '自增，序列SEQ_KEY_CONVERT_URL';
comment on column DF_KEY_MAIL_ADDR.KEY_ID
  is '命中关键字ID';
comment on column DF_KEY_MAIL_ADDR.MAIL_ADDR
  is '邮件地址';
comment on column DF_KEY_MAIL_ADDR.OP_TIME
  is '操作时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table DF_KEY_MAIL_ADDR
  add constraint PK_DF_KEY_MAIL_ADDR primary key (ID)
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
-- Create/Recreate indexes 
create index DF_KEY_MAIL_ADDR_OPTIME on DF_KEY_MAIL_ADDR (OP_TIME)
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
   
    
GK_PZ库调整如下：  
1、新增表   
-- Create table
create table SYSTEM_FUN_STATUS
(
  ID              NUMBER not null,
  FUNCTION        NUMBER not null,
  BACK_DATA       NUMBER not null,
  EFFECTIVE_RANGE VARCHAR2(32) not null,
  ACTIVE_SYS      INTEGER not null,
  IS_VALID        INTEGER not null,
  OP_TIME         DATE not null,
  PROC_SEQ        NUMBER
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
-- Add comments to the table 
comment on table SYSTEM_FUN_STATUS
  is '文本系统功能业务表';
-- Create/Recreate primary, unique and foreign key constraints 
alter table SYSTEM_FUN_STATUS
  add constraint PK_SYSTEM_FUN_STATUS primary key (ID)
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

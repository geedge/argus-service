-- Create table
create table DATADICTIONARYNAME
(
  id           NUMBER(10) not null,
  datadictname VARCHAR2(1000) not null,
  isvalid      NUMBER(2) not null
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
comment on column DATADICTIONARYNAME.id
  is '数据字典名称id';
comment on column DATADICTIONARYNAME.datadictname
  is '数据字典名称';
comment on column DATADICTIONARYNAME.isvalid
  is '数据字典是否有效';
-- Create/Recreate primary, unique and foreign key constraints 
alter table DATADICTIONARYNAME
  add primary key (ID)
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

  
  
  
  -- Create table
create table DATADICTIONARYVALUE
(
  id            NUMBER(10) not null,
  datadictid    NUMBER(10) not null,
  datadictvalue VARCHAR2(1000) not null,
  isvalid       NUMBER(2) not null
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
comment on column DATADICTIONARYVALUE.id
  is '数据字典值id';
comment on column DATADICTIONARYVALUE.datadictid
  is '数据字典名称id';
comment on column DATADICTIONARYVALUE.datadictvalue
  is '数据字典值';
comment on column DATADICTIONARYVALUE.isvalid
  is '数据字典值是否有效';
-- Create/Recreate primary, unique and foreign key constraints 
alter table DATADICTIONARYVALUE
  add primary key (ID)
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
alter table DATADICTIONARYVALUE
  add constraint FK_DATADICTVALUE_ID foreign key (DATADICTID)
  references DATADICTIONARYNAME (ID);

  
  
--创建数据字典序列
create sequence seq_datadict start with 1 increment by 1;
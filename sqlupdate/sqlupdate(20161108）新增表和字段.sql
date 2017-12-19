-- Create table
create table FX_HTTP_URL
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
comment on column FX_HTTP_URL.REGION_ID
  is '主键';
comment on column FX_HTTP_URL.GROUP_ID
  is '关联管控配置分组表中的group_id';
comment on column FX_HTTP_URL.KEYWORDS
  is 'expr_type:1,时， keywords中’,子表达式中的’&’转义。';
comment on column FX_HTTP_URL.EXPR_TYPE
  is '0:无表达式,1:表示为与表达式';
comment on column FX_HTTP_URL.MATCH_METHOD
  is 'expr_type:0时有意义，其它情况必须置0。
0：子串匹配；1：右匹配；2：左匹配；3：完全匹配
';
comment on column FX_HTTP_URL.IS_HEXBIN
  is '默认为0:大小写不敏感，且非HEX 1:HEX格式二进制2:大小写敏感，且非HEX二进制格式是一种特殊的编码，受table_info.conf文件中do_merge控制';
comment on column FX_HTTP_URL.IS_VALID
  is '0无效，1有效';
-- Create/Recreate primary, unique and foreign key constraints 
alter table FX_HTTP_URL
  add constraint PK_FX_HTTP_URL primary key (REGION_ID)
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
  
  
  
  alter table dns_response_strategy add column MIN_TTL integer not null;
  alter table dns_response_strategy add column MAX_TTL integer not null;

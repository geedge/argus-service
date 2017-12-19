-- Add/modify columns 
alter table DNS_GROUP_TYPE add group_name VARCHAR2(256) not null;
-- Add comments to the columns 
comment on column DNS_GROUP_TYPE.group_name
  is '组名称';

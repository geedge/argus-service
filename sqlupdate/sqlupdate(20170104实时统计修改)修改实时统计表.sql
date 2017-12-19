--B库更改
--1.	修改DF_DESTIP_COUNTRY_REPORT增加界面私有标签列SERVICE。
-- Add/modify columns 
alter table DF_DESTIP_COUNTRY_REPORT add SERVICE INTEGER not null;
-- Add comments to the columns 
comment on column DF_DESTIP_COUNTRY_REPORT.SERVICE
  is '界面私有标签（界面菜单ID）,值小于10000';
  
--2.	修改DF_SRCIP _DOMESTIC_REPORT增加界面私有标签列SERVICE。
  -- Add/modify columns 
alter table DF_SRCIP_DOMESTIC_REPORT add SERVICE INTEGER not null;
-- Add comments to the columns 
comment on column DF_SRCIP_DOMESTIC_REPORT.SERVICE
  is '界面私有标签（界面菜单ID）,值小于10000';
  
--3.	修改DF_TAG_REPORT,增加界面私有标签列SERVICE
   -- Add/modify columns 
alter table DF_TAG_REPORT add SERVICE INTEGER not null;
-- Add comments to the columns 
comment on column DF_TAG_REPORT.SERVICE
  is '界面私有标签（界面菜单ID）,值小于10000';
     -- Add/modify columns 
     
--4.	修改DJ_CK_STAT_LOG，增加列active_sys列
alter table DJ_CK_STAT_LOG add ACTIVE_SYS INTEGER not null;
-- Add comments to the columns 
comment on column DJ_CK_STAT_LOG.ACTIVE_SYS
  is 'A版：4 B版：2 C版：1';
  







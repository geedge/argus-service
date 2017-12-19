-- Add/modify columns 
alter table SERVICES_REQUEST_LOG add BUSINESS_CODE INTEGER ;
-- Add comments to the columns 
comment on column SERVICES_REQUEST_LOG.BUSINESS_CODE
  is '服务状态码';


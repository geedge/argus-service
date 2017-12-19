-- Add/modify columns 
alter table SERVICES_REQUEST_LOG add EXCEPTION_INFO VARCHAR2(1024) ;
-- Add comments to the columns 
comment on column SERVICES_REQUEST_LOG.EXCEPTION_INFO
  is '请求时发生的异常记录，不记录rest异常';


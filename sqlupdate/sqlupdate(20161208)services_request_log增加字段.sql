-- Add/modify columns 
alter table SERVICES_REQUEST_LOG add SERVER_IP VARCHAR2(200) ;
-- Add comments to the columns 
comment on column SERVICES_REQUEST_LOG.SERVER_IP
  is '服务器IP';


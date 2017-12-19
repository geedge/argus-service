-- Add/modify columns 
alter table df_dns_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column df_dns_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
-- Add/modify columns 
alter table df_ftp_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column df_ftp_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
-- Add/modify columns 
alter table df_http_keyword_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column df_http_keyword_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
-- Add/modify columns 
alter table df_http_req_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column df_http_req_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
-- Add/modify columns 
alter table df_http_res_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column df_http_res_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
-- Add/modify columns 
alter table df_ipsec_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column df_ipsec_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
-- Add/modify columns 
alter table df_ip_port_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column df_ip_port_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
-- Add/modify columns 
alter table df_l2tp_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column df_l2tp_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
-- Add/modify columns 
alter table df_mail_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column df_mail_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
-- Add/modify columns 
alter table df_openvpn_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column df_openvpn_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
  -- Add/modify columns 
alter table df_pptp_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column df_pptp_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
-- Add/modify columns 
alter table df_ssh_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column df_ssh_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
-- Add/modify columns 
alter table df_ssl_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column df_ssl_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
-- Add/modify columns 
alter table dj_dns_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column dj_dns_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
-- Add/modify columns 
alter table dj_ftp_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column dj_ftp_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
-- Add/modify columns 
alter table dj_http_keyword_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column dj_http_keyword_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
-- Add/modify columns 
alter table dj_http_req_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column dj_http_req_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
-- Add/modify columns 
alter table dj_http_res_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column dj_http_res_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
-- Add/modify columns 
alter table dj_ipsec_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column dj_ipsec_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
-- Add/modify columns 
alter table dj_ip_port_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column dj_ip_port_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
-- Add/modify columns 
alter table dj_l2tp_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column dj_l2tp_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
-- Add/modify columns 
alter table dj_mail_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column dj_mail_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
-- Add/modify columns 
alter table dj_openvpn_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column dj_openvpn_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
  -- Add/modify columns 
alter table dj_pptp_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column dj_pptp_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
-- Add/modify columns 
alter table dj_ssh_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column dj_ssh_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  
  
-- Add/modify columns 
alter table dj_ssl_log add NEST_ADDR_LIST VARCHAR2(1024);
-- Add comments to the columns 
comment on column dj_ssl_log.NEST_ADDR_LIST
  is '嵌套地址列表，用于辅助对准查询，可空';
  



  



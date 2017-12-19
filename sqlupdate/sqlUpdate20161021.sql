--gk_pz库
--2.5.32	2016.10.21	窦凤虎	1.	DF_L2TP_URL由增强字符域改为字符域
alter table DF_L2TP_URL drop column DISTRICT ;
--2.	DF_PPTP_URL由增强字符域改为字符域
alter table DF_PPTP_URL drop column DISTRICT ;


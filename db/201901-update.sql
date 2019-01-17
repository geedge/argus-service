--活跃端口统计增加传输层协议属性
alter table traffic_port_active_statistic add  trans_proto int(11) default 0 comment '6-tcp 17 -udp';


alter table traffic_trans_statistic add direction int(11)   comment '0-out 1-in';

alter table traffic_protocol_statistic add direction int(11)   comment '0-out 1-in',
add addr_type varchar(32),  add trans_type int(11) ;


alter table traffic_app_statistic add direction int(11)   comment '0-out 1-in',
add addr_type varchar(32),  add trans_type int(11) ;




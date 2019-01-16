--活跃端口统计增加传输层协议属性
alter table traffic_port_active_statistic add  trans_proto int(11) default 0 comment '6-tcp 17 -udp';
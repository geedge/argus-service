--活跃端口统计增加传输层协议属性
alter table traffic_port_active_statistic add  trans_proto int(11) default 0 comment '6-tcp 17 -udp';


alter table traffic_trans_statistic add direction int(11)   comment '0-out 1-in';

alter table traffic_protocol_statistic add direction int(11)   comment '0-out 1-in',
add addr_type varchar(32),  add trans_type int(11) ;


alter table traffic_app_statistic add direction int(11)   comment '0-out 1-in',
add addr_type varchar(32),  add trans_type int(11) ;





--增加界面映射字典表，需映射大屏app的名称
DROP TABLE IF EXISTS `ui_app_policy_cfg`;
CREATE TABLE `ui_app_policy_cfg`  (
  `cfg_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_code` int(11) NOT NULL,
  `behav_code` int(11) NULL DEFAULT NULL,
  `spec_service_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'specific_service_cfg .spec_service_id',
  `cfg_desc` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `action` int(11) NOT NULL,
  `is_valid` int(11) NOT NULL,
  `is_audit` int(11) NOT NULL,
  `creator_id` int(11) NOT NULL COMMENT 'sys_user.id',
  `create_time` datetime(0) NOT NULL,
  `editor_id` int(11) NULL DEFAULT NULL COMMENT '来自sys_user.id',
  `edit_time` datetime(0) NULL DEFAULT NULL,
  `auditor_id` int(11) NULL DEFAULT NULL COMMENT '来自sys_user.id',
  `audit_time` datetime(0) NULL DEFAULT NULL,
  `service_id` int(11) NOT NULL COMMENT '业务id',
  `request_id` int(11) NOT NULL COMMENT '来自request_info.id',
  `compile_id` int(11) NOT NULL,
  `is_area_effective` int(11) NOT NULL DEFAULT 0,
  `classify` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `attribute` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `lable` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `area_effective_ids` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `function_id` int(11) NOT NULL,
  `ratelimit` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '限速比例,0到1之间',
  `cfg_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `cfg_region_code` int(11) NOT NULL,
  `expr_type` int(11) NOT NULL DEFAULT 0 COMMENT '0：无表达式，1：与表达式',
  `match_method` int(11) NOT NULL DEFAULT 3 COMMENT 'expr_type:0时有意义，其它情况必须置0。0：子串匹配；1：右匹配；2：左匹配；3：完全匹配',
  `is_hexbin` int(11) NOT NULL DEFAULT 0 COMMENT '默认为0:大小写不敏感，且非HEX;1:HEX格式二进制;2:大小写敏感，且非HEX',
  `user_region1` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预留自定义域1',
  `user_region2` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预留自定义域2',
  `user_region3` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预留自定义域3',
  `user_region4` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预留自定义域4',
  `user_region5` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预留自定义域5',
  `do_log` int(11) NULL DEFAULT NULL COMMENT 'do_log:0不需要1记录所有日志2只记录结构化日志。默认是2',
  `cancel_request_id` int(11) NULL DEFAULT NULL COMMENT '取消审核来函',
  PRIMARY KEY (`cfg_id`) USING BTREE
) ENGINE=FEDERATED AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CONNECTION='mysql://root:111111@10.0.4.6:3306/ntc_db/app_policy_cfg';
;




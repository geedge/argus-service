
/*Table structure for table `area` */

DROP TABLE IF EXISTS `area`;
DROP TABLE IF EXISTS `image_url`;
DROP TABLE IF EXISTS `isp`;
DROP TABLE IF EXISTS `menu_service`;
DROP TABLE IF EXISTS `mm_audio_report`;
DROP TABLE IF EXISTS `mm_pic_report`;
DROP TABLE IF EXISTS `mm_video_report`;
DROP TABLE IF EXISTS `mm_voip_report`;
DROP TABLE IF EXISTS `nms_di_rule`;
DROP TABLE IF EXISTS `ntc_attr_type_report`;
DROP TABLE IF EXISTS `ntc_attr_type_stat_daily`;
DROP TABLE IF EXISTS `ntc_attr_type_stat_hour`;
DROP TABLE IF EXISTS `ntc_destip_country_report`;
DROP TABLE IF EXISTS `ntc_tag_report`;
DROP TABLE IF EXISTS `ntc_destip_country_stat_daily`;
DROP TABLE IF EXISTS `ntc_destip_country_stat_hour`;
DROP TABLE IF EXISTS `ntc_entrance_report`;
DROP TABLE IF EXISTS `ntc_entrance_stat_daily`;
DROP TABLE IF EXISTS `traffic_abnormal_machine`;
DROP TABLE IF EXISTS `traffic_nmsserver_statistic`;
DROP TABLE IF EXISTS `ntc_entrance_stat_hour`;
DROP TABLE IF EXISTS `ntc_lwhh_report`;
DROP TABLE IF EXISTS `ntc_lwhh_stat_daily`;
DROP TABLE IF EXISTS `ntc_lwhh_stat_hour`;
DROP TABLE IF EXISTS `ntc_pz_report`;
DROP TABLE IF EXISTS `ntc_pz_stat_daily`;
DROP TABLE IF EXISTS `ntc_pz_stat_hour`;
DROP TABLE IF EXISTS `ntc_radius_report`;
DROP TABLE IF EXISTS `ntc_reject_url_statistic`;
DROP TABLE IF EXISTS `ntc_reject_website_statistic`;
DROP TABLE IF EXISTS `ntc_service_behav_statistic`;
DROP TABLE IF EXISTS `ntc_service_dic`;
DROP TABLE IF EXISTS `ntc_service_report`;
DROP TABLE IF EXISTS `ntc_service_stat_daily`;
DROP TABLE IF EXISTS `ntc_service_stat_hour`;
DROP TABLE IF EXISTS `ntc_srcip_domestic_report`;
DROP TABLE IF EXISTS `ntc_srcip_domestic_stat_daily`;
DROP TABLE IF EXISTS `ntc_srcip_domestic_stat_hour`;
DROP TABLE IF EXISTS `ntc_tag_stat_daily`;
DROP TABLE IF EXISTS `ntc_tag_stat_hour`;
DROP TABLE IF EXISTS `ntc_total_report`;
DROP TABLE IF EXISTS `pxy_total_report`;
DROP TABLE IF EXISTS `services_config_request_log`;
DROP TABLE IF EXISTS `services_log_request_log`;
DROP TABLE IF EXISTS `services_request_log`;
DROP TABLE IF EXISTS `src_ip`;
DROP TABLE IF EXISTS `sys_area`;
DROP TABLE IF EXISTS `sys_data_dictionary_item`;
DROP TABLE IF EXISTS `sys_data_dictionary_name`;
DROP TABLE IF EXISTS `sys_inter_user_role`;
DROP TABLE IF EXISTS `sys_log`;
DROP TABLE IF EXISTS `sys_menu`;
DROP TABLE IF EXISTS `sys_office`;
DROP TABLE IF EXISTS `sys_privilege`;
DROP TABLE IF EXISTS `sys_role`;
DROP TABLE IF EXISTS `sys_user`;
DROP TABLE IF EXISTS `traffic_app_statistic`;
DROP TABLE IF EXISTS `traffic_http_statistic`;
DROP TABLE IF EXISTS `traffic_ip_active_statistic`;
DROP TABLE IF EXISTS `traffic_netflow_port_info`;
DROP TABLE IF EXISTS `traffic_port_active_statistic`;
DROP TABLE IF EXISTS `traffic_protocol_statistic`;
DROP TABLE IF EXISTS `traffic_service_statistic`;
DROP TABLE IF EXISTS `traffic_total_statistic`;
DROP TABLE IF EXISTS `traffic_trans_statistic`;
DROP TABLE IF EXISTS `traffic_ua_statistic`;


CREATE TABLE `area` (
  `area_id` bigint(20) NOT NULL COMMENT '序号',
  `area_name` varchar(128) NOT NULL COMMENT '地区名称',
  `parent_id` bigint(20) unsigned NOT NULL COMMENT '所属上级地区ID，一级目录（洲）为0',
  `area_level` int(2) NOT NULL COMMENT '地域级别,1:大洲; 2:国家; 3:省',
  `yl1` int(11) DEFAULT NULL COMMENT '预留1',
  `yl2` varchar(80) DEFAULT NULL COMMENT '预留2',
  PRIMARY KEY (`area_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `image_url` */



CREATE TABLE `image_url` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `module` varchar(255) DEFAULT NULL COMMENT '模块名称',
  `type` varchar(255) DEFAULT NULL COMMENT '类型',
  `url` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `datetime` datetime DEFAULT NULL COMMENT '上传时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `isp` */



CREATE TABLE `isp` (
  `isp_name` varchar(32) NOT NULL COMMENT '运营商名称',
  `isp_id` bigint(20) NOT NULL COMMENT '运营商值',
  `yl1` int(11) DEFAULT NULL COMMENT '预留1',
  `yl2` varchar(64) DEFAULT NULL COMMENT '预留2',
  PRIMARY KEY (`isp_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `menu_service` */



CREATE TABLE `menu_service` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `service` int(255) NOT NULL COMMENT '业务类型',
  `service_desc` varchar(255) DEFAULT NULL COMMENT '业务类型名称',
  `is_maat` int(255) NOT NULL COMMENT '0不是maat业务,1是maat业务',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `service` (`service`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `mm_audio_report` */



CREATE TABLE `mm_audio_report` (
  `STAT_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PID` varchar(128) NOT NULL,
  `SERVICE` int(11) NOT NULL,
  `NUM` bigint(20) NOT NULL,
  `LOG_URI` varchar(256) NOT NULL,
  `REPORT_TIME` datetime NOT NULL,
  PRIMARY KEY (`STAT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `mm_pic_report` */



CREATE TABLE `mm_pic_report` (
  `STAT_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PID` varchar(128) NOT NULL,
  `SERVICE` int(11) NOT NULL,
  `NUM` bigint(20) NOT NULL,
  `LOG_URI` varchar(256) NOT NULL,
  `REPORT_TIME` datetime NOT NULL,
  PRIMARY KEY (`STAT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `mm_video_report` */



CREATE TABLE `mm_video_report` (
  `STAT_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PID` varchar(128) NOT NULL,
  `SERVICE` int(11) NOT NULL,
  `NUM` bigint(20) NOT NULL,
  `LOG_URI` varchar(256) NOT NULL,
  `REPORT_TIME` datetime NOT NULL,
  PRIMARY KEY (`STAT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `mm_voip_report` */



CREATE TABLE `mm_voip_report` (
  `STAT_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ACCOUNT` varchar(128) NOT NULL,
  `SERVICE` int(11) NOT NULL,
  `NUM` bigint(20) NOT NULL,
  `LOG_URI` varchar(256) NOT NULL,
  `REPORT_TIME` datetime NOT NULL,
  PRIMARY KEY (`STAT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `nms_di_rule` */



CREATE TABLE `nms_di_rule` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `detection_info_id` bigint(20) NOT NULL,
  `ServiceIndex` bigint(20) DEFAULT NULL,
  `ServiceCode` bigint(20) DEFAULT NULL,
  `ServiceDesc` varchar(32) DEFAULT NULL,
  `agedTime` bigint(20) DEFAULT NULL,
  `ClientNum` varchar(64) DEFAULT NULL,
  `RefluxPort` varchar(64) DEFAULT NULL,
  `RuleNumber` varchar(1024) DEFAULT NULL,
  `usedRuleNum` varchar(1024) DEFAULT NULL,
  `leftRuleNum` varchar(1024) DEFAULT NULL,
  `HitTotalNum` bigint(20) DEFAULT NULL,
  `DETECTIONED_STATE` varchar(2) DEFAULT NULL,
  `SEQ_ID` int(11) DEFAULT NULL,
  `DETECTION_SET_INFO_ID` bigint(20) DEFAULT NULL,
  `data_check_time` datetime NOT NULL,
  `data_arrive_time` datetime DEFAULT NULL,
  `data_check_time_digital` bigint(20) DEFAULT NULL,
  `data_arrive_time_digital` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`data_check_time`),
  KEY `DI_SUGON_RULE_1` (`data_check_time`),
  KEY `DI_SUGON_RULE_2` (`SEQ_ID`),
  KEY `DI_SUGON_RULE_3` (`DETECTION_SET_INFO_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `ntc_attr_type_report` */



CREATE TABLE `ntc_attr_type_report` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service` int(11) NOT NULL,
  `attr_type` int(11) NOT NULL,
  `sum` bigint(20) NOT NULL,
  `report_time` datetime DEFAULT NULL,
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Table structure for table `ntc_attr_type_stat_daily` */



CREATE TABLE `ntc_attr_type_stat_daily` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id自增',
  `service` int(11) NOT NULL COMMENT '业务类型',
  `attr_type` int(11) NOT NULL COMMENT '性质',
  `sum` bigint(20) NOT NULL DEFAULT 0 COMMENT '日志数量',
  `report_time` datetime NOT NULL COMMENT '统计时间',
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='9.13.	ntc性质日报表';

/*Table structure for table `ntc_attr_type_stat_hour` */



CREATE TABLE `ntc_attr_type_stat_hour` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id自增',
  `service` int(11) NOT NULL COMMENT '业务类型',
  `attr_type` int(11) NOT NULL COMMENT '性质',
  `sum` bigint(20) NOT NULL DEFAULT 0 COMMENT '日志数量',
  `report_time` datetime NOT NULL COMMENT '统计时间',
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='9.12.	ntc性质小时报表';

/*Table structure for table `ntc_destip_country_report` */



CREATE TABLE `ntc_destip_country_report` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service` int(11) NOT NULL,
  `dest_country` varchar(255) NOT NULL,
  `sum` bigint(20) NOT NULL,
  `report_time` datetime DEFAULT NULL,
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Table structure for table `ntc_destip_country_stat_daily` */



CREATE TABLE `ntc_destip_country_stat_daily` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id自增',
  `service` int(11) NOT NULL COMMENT '业务类型',
  `dest_country` varchar(255) NOT NULL COMMENT '目的ip所属国家',
  `sum` bigint(20) NOT NULL DEFAULT 0 COMMENT '日志数量',
  `report_time` datetime NOT NULL COMMENT '统计时间',
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='9.11.	ntc国家日报表';

/*Table structure for table `ntc_destip_country_stat_hour` */



CREATE TABLE `ntc_destip_country_stat_hour` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id自增',
  `service` int(11) NOT NULL COMMENT '业务类型',
  `dest_country` varchar(255) NOT NULL COMMENT '目的ip所属国家',
  `sum` bigint(20) NOT NULL DEFAULT 0 COMMENT '日志数量',
  `report_time` datetime NOT NULL COMMENT '统计时间',
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='9.10.	ntc国家小时报表';

/*Table structure for table `ntc_entrance_report` */



CREATE TABLE `ntc_entrance_report` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service` int(11) NOT NULL,
  `entrance_id` int(11) NOT NULL,
  `sum` bigint(20) NOT NULL,
  `report_time` datetime DEFAULT NULL,
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Table structure for table `ntc_entrance_stat_daily` */



CREATE TABLE `ntc_entrance_stat_daily` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id自增',
  `service` int(11) NOT NULL COMMENT '业务类型',
  `entrance_id` int(11) NOT NULL COMMENT '局点（出入口）',
  `sum` bigint(20) NOT NULL DEFAULT 0 COMMENT '日志数量',
  `report_time` datetime NOT NULL COMMENT '统计时间',
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='9.15.	ntc局点日报表';

/*Table structure for table `ntc_entrance_stat_hour` */



CREATE TABLE `ntc_entrance_stat_hour` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id自增',
  `service` int(11) NOT NULL COMMENT '业务类型',
  `entrance_id` int(11) NOT NULL COMMENT '局点（出入口）',
  `sum` bigint(20) NOT NULL DEFAULT 0 COMMENT '日志数量',
  `report_time` datetime NOT NULL COMMENT '统计时间',
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='9.14.	ntc局点小时报表';

/*Table structure for table `ntc_lwhh_report` */



CREATE TABLE `ntc_lwhh_report` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service` int(11) NOT NULL,
  `lwhh` int(11) NOT NULL,
  `sum` bigint(20) NOT NULL,
  `report_time` datetime DEFAULT NULL,
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Table structure for table `ntc_lwhh_stat_daily` */



CREATE TABLE `ntc_lwhh_stat_daily` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id自增',
  `service` int(11) NOT NULL COMMENT '业务类型',
  `lwhh` int(11) NOT NULL COMMENT '来文函号',
  `sum` bigint(20) NOT NULL DEFAULT 0 COMMENT '日志数量',
  `report_time` datetime NOT NULL COMMENT '统计时间',
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='9.17.	ntc来文函号日报表';

/*Table structure for table `ntc_lwhh_stat_hour` */



CREATE TABLE `ntc_lwhh_stat_hour` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id自增',
  `service` int(11) NOT NULL COMMENT '业务类型',
  `lwhh` int(11) NOT NULL COMMENT '来文函号',
  `sum` bigint(20) NOT NULL DEFAULT 0 COMMENT '日志数量',
  `report_time` datetime NOT NULL COMMENT '统计时间',
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='9.16.	ntc来文函号小时报表';

/*Table structure for table `ntc_pz_report` */



CREATE TABLE `ntc_pz_report` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cfg_id` int(20) NOT NULL,
  `service` int(11) NOT NULL,
  `sum` bigint(20) NOT NULL,
  `report_time` datetime DEFAULT NULL,
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Table structure for table `ntc_pz_stat_daily` */



CREATE TABLE `ntc_pz_stat_daily` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id自增',
  `cfg_id` int(11) NOT NULL COMMENT '配置id',
  `service` int(11) NOT NULL COMMENT '业务类型',
  `sum` bigint(20) NOT NULL DEFAULT 0 COMMENT '日志数量',
  `report_time` datetime NOT NULL COMMENT '统计时间',
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='9.3.	ntc配置统计日报表';

/*Table structure for table `ntc_pz_stat_hour` */



CREATE TABLE `ntc_pz_stat_hour` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id自增',
  `cfg_id` int(11) NOT NULL COMMENT '配置id',
  `service` int(11) NOT NULL COMMENT '业务类型',
  `sum` bigint(20) NOT NULL DEFAULT 0 COMMENT '日志数量',
  `report_time` datetime NOT NULL COMMENT '统计时间',
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='9.2.	ntc配置统计小时报表';

/*Table structure for table `ntc_radius_report` */



CREATE TABLE `ntc_radius_report` (
  `STAT_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAS_IP` varchar(128) NOT NULL,
  `ACCOUNT` varchar(128) NOT NULL,
  `NUM` bigint(20) NOT NULL,
  `REPORT_TIME` datetime NOT NULL,
  PRIMARY KEY (`STAT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `ntc_reject_url_statistic` */



CREATE TABLE `ntc_reject_url_statistic` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典ID',
  `ip_addr` varchar(255) NOT NULL COMMENT 'IP址地',
  `url` varchar(2048) NOT NULL COMMENT 'URL',
  `sum` bigint(20) NOT NULL COMMENT '数量',
  `stat_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE current_timestamp() COMMENT '统计时间',
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `ntc_reject_website_statistic` */



CREATE TABLE `ntc_reject_website_statistic` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典ID',
  `ip_addr` varchar(255) NOT NULL COMMENT 'IP址地',
  `website` varchar(255) NOT NULL COMMENT '网站',
  `sum` bigint(20) NOT NULL COMMENT '数量',
  `stat_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE current_timestamp() COMMENT '统计时间',
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `ntc_service_behav_statistic` */

CREATE TABLE `ntc_service_behav_statistic` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service` int(11) NOT NULL,
  `entrance_id` int(11) NOT NULL,
  `s_ip` varchar(128) NOT NULL,
  `d_ip` varchar(128) NOT NULL,
  `sum` bigint(20) NOT NULL,
  `report_time` datetime NOT NULL,
  PRIMARY KEY (`stat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `ntc_service_dic` */

CREATE TABLE `ntc_service_dic` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `service_code` int(11) NOT NULL,
  `action` int(11) NOT NULL,
  `service_name_zh` varchar(255) DEFAULT '' COMMENT '业务-中文',
  `service_name_en` varchar(255) DEFAULT '' COMMENT '业务-英文',
  `service_name_ru` varchar(255) DEFAULT '' COMMENT '业务-俄语',
  `desc` varchar(255) DEFAULT '' COMMENT '描述',
  `is_valid` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `ntc_service_report` */


CREATE TABLE `ntc_service_report` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service` int(11) NOT NULL,
  `sum` bigint(20) NOT NULL,
  `report_time` datetime DEFAULT NULL,
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Table structure for table `ntc_service_stat_daily` */


CREATE TABLE `ntc_service_stat_daily` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id自增',
  `service` int(11) NOT NULL COMMENT '业务类型',
  `sum` bigint(20) NOT NULL DEFAULT 0 COMMENT '日志数量',
  `report_time` datetime NOT NULL COMMENT '统计时间',
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='9.5.	ntc业务类型日报表';

/*Table structure for table `ntc_service_stat_hour` */


CREATE TABLE `ntc_service_stat_hour` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id自增',
  `service` int(11) NOT NULL COMMENT '业务类型',
  `sum` bigint(20) NOT NULL DEFAULT 0 COMMENT '日志数量',
  `report_time` datetime NOT NULL COMMENT '统计时间',
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='9.4.	ntc业务类型小时报表';

/*Table structure for table `ntc_srcip_domestic_report` */


CREATE TABLE `ntc_srcip_domestic_report` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service` int(11) NOT NULL,
  `src_province` varchar(255) NOT NULL,
  `src_city` varchar(255) DEFAULT NULL,
  `sum` bigint(20) NOT NULL,
  `report_time` datetime DEFAULT NULL,
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Table structure for table `ntc_srcip_domestic_stat_daily` */



CREATE TABLE `ntc_srcip_domestic_stat_daily` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id自增',
  `service` int(11) NOT NULL COMMENT '业务类型',
  `src_province` varchar(255) NOT NULL COMMENT '源ip所属省',
  `src_city` varchar(255) DEFAULT NULL COMMENT '源ip所属市',
  `sum` bigint(20) NOT NULL DEFAULT 0 COMMENT '日志数量',
  `report_time` datetime NOT NULL COMMENT '统计时间',
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='9.9.	ntc境内日报表';

/*Table structure for table `ntc_srcip_domestic_stat_hour` */


CREATE TABLE `ntc_srcip_domestic_stat_hour` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id自增',
  `service` int(11) NOT NULL COMMENT '业务类型',
  `src_province` varchar(255) NOT NULL COMMENT '源ip所属省',
  `src_city` varchar(255) DEFAULT NULL COMMENT '源ip所属市',
  `sum` bigint(20) NOT NULL DEFAULT 0 COMMENT '日志数量',
  `report_time` datetime NOT NULL COMMENT '统计时间',
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='9.8.	ntc境内小时报表';

/*Table structure for table `ntc_tag_report` */



CREATE TABLE `ntc_tag_report` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service` int(11) NOT NULL,
  `tag` int(11) NOT NULL,
  `sum` bigint(20) NOT NULL,
  `report_time` datetime DEFAULT NULL,
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Table structure for table `ntc_tag_stat_daily` */


CREATE TABLE `ntc_tag_stat_daily` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id自增',
  `service` int(11) NOT NULL COMMENT '业务类型',
  `tag` int(11) NOT NULL COMMENT '标签',
  `sum` bigint(20) NOT NULL DEFAULT 0 COMMENT '日志数量',
  `report_time` datetime NOT NULL COMMENT '统计时间',
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='9.7.	ntc标签统计日报表';

/*Table structure for table `ntc_tag_stat_hour` */


CREATE TABLE `ntc_tag_stat_hour` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id自增',
  `service` int(11) NOT NULL COMMENT '业务类型',
  `tag` int(11) NOT NULL COMMENT '标签',
  `sum` bigint(20) NOT NULL DEFAULT 0 COMMENT '日志数量',
  `report_time` datetime NOT NULL COMMENT '统计时间',
  PRIMARY KEY (`stat_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='9.6.	ntc标签统计小时报表';

/*Table structure for table `ntc_total_report` */


CREATE TABLE `ntc_total_report` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id自增',
  `reject_num` bigint(20) NOT NULL DEFAULT 0 COMMENT '封堵数量',
  `monitor_num` bigint(20) NOT NULL DEFAULT 0 COMMENT '监测数量',
  `c2s_pkt_num` bigint(20) DEFAULT 0 COMMENT 'c2s包数',
  `s2c_pkt_num` bigint(20) DEFAULT 0 COMMENT 's2c包数',
  `c2s_byte_len` bigint(20) NOT NULL DEFAULT 0 COMMENT 'c2s字节数',
  `s2c_byte_len` bigint(20) NOT NULL DEFAULT 0 COMMENT 's2c字节数',
  `new_uni_conn_num` bigint(20) DEFAULT 0 COMMENT '新建链接数',
  `live_conn_num` bigint(20) DEFAULT 0 COMMENT '活跃链接数',
  `drop_conn_num` bigint(20) DEFAULT 0 COMMENT '丢弃链接数',
  `loop_conn_num` bigint(20) DEFAULT 0 COMMENT '回流链接数',
  `report_time` datetime NOT NULL COMMENT '统计时间',
  PRIMARY KEY (`stat_id`),
  KEY `time` (`report_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='9.1.	业务总量汇聚报表';

/*Table structure for table `pxy_total_report` */


CREATE TABLE `pxy_total_report` (
  `STAT_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MONITOR_NUM` bigint(20) NOT NULL,
  `REJECT_NUM` bigint(20) NOT NULL,
  `REPLACE_NUM` bigint(20) NOT NULL,
  `REDIRECT_NUM` bigint(20) NOT NULL,
  `REPORT_TIME` datetime NOT NULL,
  PRIMARY KEY (`STAT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `services_config_request_log` */


CREATE TABLE `services_config_request_log` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `OPERATOR` varchar(200) DEFAULT NULL COMMENT '操作人',
  `VERSION` varchar(100) NOT NULL COMMENT '版本号',
  `OPACTION` int(11) NOT NULL COMMENT '操作行为',
  `OPTIME` datetime DEFAULT NULL COMMENT '操作时间',
  `REQUEST_CONTENT` longtext DEFAULT NULL COMMENT '操作内容',
  `REQUEST_TIME` datetime DEFAULT NULL COMMENT '请求时间',
  `CONSUMER_TIME` bigint(20) NOT NULL COMMENT '处理时间',
  `REQUEST_IP` varchar(200) DEFAULT NULL COMMENT '请求ip',
  `BUSINESS_CODE` int(11) DEFAULT NULL COMMENT '业务码(用来异常处理的)',
  `EXCEPTION_INFO` text DEFAULT NULL COMMENT '错误信息',
  `SERVER_IP` varchar(200) DEFAULT NULL COMMENT '服务端ip',
  `TRACE_CODE` varchar(32) DEFAULT NULL COMMENT '追踪码(用来查请求日志)',
  `REQUEST_URI` varchar(200) DEFAULT NULL COMMENT '请求地址',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `services_log_request_log` */


CREATE TABLE `services_log_request_log` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `OPERATOR` varchar(200) DEFAULT NULL COMMENT '操作人',
  `VERSION` varchar(100) NOT NULL COMMENT '版本号',
  `OPACTION` int(11) NOT NULL COMMENT '操作行为',
  `OPTIME` datetime DEFAULT NULL COMMENT '操作时间',
  `REQUEST_CONTENT` longtext DEFAULT NULL COMMENT '操作内容',
  `REQUEST_TIME` datetime DEFAULT NULL COMMENT '请求时间',
  `CONSUMER_TIME` bigint(20) NOT NULL COMMENT '处理时间',
  `REQUEST_IP` varchar(200) DEFAULT NULL COMMENT '请求ip',
  `BUSINESS_CODE` int(11) DEFAULT NULL COMMENT '业务码(用来异常处理的)',
  `EXCEPTION_INFO` text DEFAULT NULL COMMENT '错误信息',
  `SERVER_IP` varchar(200) DEFAULT NULL COMMENT '服务端ip',
  `TRACE_CODE` varchar(32) DEFAULT NULL COMMENT '追踪码(用来查请求日志)',
  `REQUEST_URI` varchar(200) DEFAULT NULL COMMENT '请求地址',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `services_request_log` */


CREATE TABLE `services_request_log` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `OPERATOR` varchar(200) DEFAULT NULL,
  `VERSION` varchar(100) NOT NULL,
  `OPACTION` int(11) NOT NULL,
  `OPTIME` datetime DEFAULT NULL,
  `REQUEST_CONTENT` longtext DEFAULT NULL,
  `REQUEST_TIME` datetime DEFAULT NULL,
  `CONSUMER_TIME` bigint(20) NOT NULL,
  `REQUEST_IP` varchar(200) DEFAULT NULL,
  `BUSINESS_CODE` int(11) DEFAULT NULL,
  `EXCEPTION_INFO` text DEFAULT NULL,
  `SERVER_IP` varchar(200) DEFAULT '',
  `TRACE_CODE` varchar(32) DEFAULT NULL,
  `REQUEST_URI` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `src_ip` */


CREATE TABLE `src_ip` (
  `seq_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '序号',
  `ip_start_addr` int(11) unsigned NOT NULL DEFAULT 0 COMMENT 'IP起始地址,主机序',
  `ip_end_addr` int(11) unsigned NOT NULL DEFAULT 0 COMMENT 'IP结束地址,主机序',
  `ip_addr_range` int(11) unsigned NOT NULL DEFAULT 1 COMMENT 'IP地址范围(数量)',
  `ip_start_string` varchar(20) DEFAULT NULL COMMENT 'IP起始地址字符串描述',
  `ip_end_string` varchar(20) DEFAULT NULL COMMENT 'IP终止地址字符串描述',
  `port` int(2) unsigned NOT NULL DEFAULT 0 COMMENT '端口,主机序',
  `isp_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '运营商ID',
  `area_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '地区ID',
  `flag` int(11) DEFAULT 3 COMMENT '标志位， 0:可用于DDOS系统做源IP; 1:只做为通用IP库做查询;2:保留地址;3:未知或无法详细分类地址',
  `description` varchar(256) DEFAULT NULL COMMENT '纯真IP库里的原始描述信息',
  `yl1` int(11) DEFAULT NULL COMMENT '预留1',
  `yl2` varchar(100) DEFAULT NULL COMMENT '预留2',
  `yl3` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`seq_id`) USING BTREE,
  KEY `idx_src_ip_addr` (`ip_start_addr`) USING BTREE,
  KEY `idx_src_ip_dst_addr` (`ip_end_addr`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=440889 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `sys_area` */


CREATE TABLE `sys_area` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `parent_id` int(11) NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) NOT NULL COMMENT '所有父级编号',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `sort` int(2) NOT NULL COMMENT '排序',
  `code` varchar(100) DEFAULT NULL COMMENT '区域编码',
  `type` char(1) DEFAULT NULL COMMENT '区域类型',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `longitude` float DEFAULT NULL COMMENT '经度',
  `latitude` float DEFAULT NULL COMMENT '纬度',
  `del_flag` int(2) NOT NULL DEFAULT 1 COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sys_area_parent_id` (`parent_id`) USING BTREE,
  KEY `sys_area_del_flag` (`del_flag`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=46467 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='区域表';

/*Table structure for table `sys_data_dictionary_item` */


CREATE TABLE `sys_data_dictionary_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `item_code` varchar(128) NOT NULL COMMENT '编码（数据表中存储的值如：0、1、2、3）',
  `item_value` varchar(256) NOT NULL COMMENT '编码对应的值（页面上展现的具体意义）',
  `item_desc` varchar(256) DEFAULT NULL COMMENT '编码描述信息',
  `item_sort` int(2) NOT NULL COMMENT '排序序号',
  `status` int(11) DEFAULT 1 COMMENT '该条数据是否可用（1：可用 0：禁用）',
  `type` int(11) DEFAULT 1 COMMENT '该条记录页面是否可维护（1：可维护 0：不可维护）',
  `dictionary_id` int(11) DEFAULT NULL COMMENT '关联数据字典外键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=285 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='词典选项列表';

/*Table structure for table `sys_data_dictionary_name` */


CREATE TABLE `sys_data_dictionary_name` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `module_name` varchar(50) NOT NULL COMMENT '模块名称',
  `mark` varchar(50) NOT NULL COMMENT '词典标识',
  `remark` varchar(255) DEFAULT NULL COMMENT '描述信息',
  `revision` varchar(500) DEFAULT NULL COMMENT '历史记录',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '修改时间',
  `status` int(2) NOT NULL DEFAULT 1 COMMENT '词典状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统词典标识表';

/*Table structure for table `sys_inter_user_role` */


CREATE TABLE `sys_inter_user_role` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ROLE_ID` int(11) NOT NULL,
  `USER_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1124 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统权限用户角色关联表';

/*Table structure for table `sys_log` */


CREATE TABLE `sys_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `type` int(2) NOT NULL DEFAULT 1 COMMENT '日志类型',
  `title` varchar(255) NOT NULL COMMENT '日志标题',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `remote_addr` varchar(255) NOT NULL COMMENT '操作IP地址',
  `user_agent` varchar(255) DEFAULT NULL COMMENT '用户代理',
  `request_uri` varchar(255) NOT NULL COMMENT '请求URI',
  `method` varchar(5) NOT NULL COMMENT '操作方式',
  `state` int(2) NOT NULL DEFAULT 1 COMMENT '行为状态：1-成功 2-失败',
  `consumer_time` int(11) DEFAULT 0 COMMENT '消费时间',
  `params` text DEFAULT NULL COMMENT '操作提交的数据',
  `exception` text DEFAULT NULL COMMENT '异常信息',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sys_log_create_by` (`create_by`) USING BTREE,
  KEY `sys_log_request_uri` (`request_uri`) USING BTREE,
  KEY `sys_log_type` (`type`) USING BTREE,
  KEY `sys_log_create_date` (`create_date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户行为日志';

/*Table structure for table `sys_menu` */



CREATE TABLE `sys_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `parent_id` int(11) NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(1024) NOT NULL COMMENT '所有父级编号',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `sort` int(2) NOT NULL COMMENT '排序',
  `href` varchar(2000) DEFAULT NULL COMMENT '链接',
  `target` varchar(20) DEFAULT NULL COMMENT '目标',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `is_show` int(2) NOT NULL COMMENT '是否在菜单中显示',
  `permission` varchar(200) DEFAULT NULL COMMENT '权限标识',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` int(1) NOT NULL DEFAULT 1 COMMENT '删除标记',
  `menu_bg` varchar(16) DEFAULT NULL COMMENT '背景颜色，用于快捷菜单',
  `quick_action` int(2) NOT NULL DEFAULT 0 COMMENT '是否为快捷菜单：0-否 1-普通 2-是且为宽屏',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sys_menu_parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统权限功能菜单表';

/*Table structure for table `sys_office` */

CREATE TABLE `sys_office` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `parent_id` int(11) NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) NOT NULL COMMENT '所有父级编号',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `en_name` varchar(100) DEFAULT NULL COMMENT '英文名称',
  `sort` int(11) NOT NULL COMMENT '排序',
  `area_id` int(11) NOT NULL COMMENT '归属区域',
  `code` varchar(100) DEFAULT NULL COMMENT '区域编码',
  `type` int(2) NOT NULL COMMENT '机构类型（1：分公司；2：单位；3：部门 4：信访办）',
  `grade` int(2) NOT NULL COMMENT '机构等级（1：一级；2：二级；3：三级；4：四级）',
  `job_type` int(2) DEFAULT NULL COMMENT '职责分类',
  `address` varchar(255) DEFAULT NULL COMMENT '联系地址',
  `zip_code` varchar(100) DEFAULT NULL COMMENT '邮政编码',
  `master` varchar(100) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(200) DEFAULT NULL COMMENT '电话',
  `fax` varchar(200) DEFAULT NULL COMMENT '传真',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `useable` int(2) DEFAULT 1 COMMENT '是否启用',
  `primary_person` int(11) DEFAULT NULL COMMENT '主负责人',
  `deputy_person` int(11) DEFAULT NULL COMMENT '副负责人',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` int(2) NOT NULL DEFAULT 1 COMMENT '删除标记',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sys_office_parent_id` (`parent_id`) USING BTREE,
  KEY `sys_office_del_flag` (`del_flag`) USING BTREE,
  KEY `sys_office_type` (`type`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=3849 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='机构表';

/*Table structure for table `sys_privilege` */


CREATE TABLE `sys_privilege` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PRIVILEGE_MASTER` varchar(10) NOT NULL COMMENT '权限归属：用户-USER，角色-ROLE；',
  `PRIVILEGE_MASTER_VALUE` int(11) NOT NULL COMMENT '权限归属关联：用户-USERID，角色-ROLEID;',
  `PRIVILEGE_ACCESS` varchar(10) NOT NULL COMMENT '权限访问类型：菜单-1000，按钮-1001；',
  `PRIVILEGE_ACCESS_VALUE` int(11) NOT NULL COMMENT '权限访问值关联：菜单-MENUID，按钮-BTNID;',
  `PRIVILEGE_OPERATION` int(2) NOT NULL DEFAULT 1 COMMENT '访问操作：enable-1，disabled-0；',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4811 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='权限分配表';

/*Table structure for table `sys_role` */

CREATE TABLE `sys_role` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `NAME` varchar(64) NOT NULL COMMENT '角色名称',
  `DATA_SCOPE` int(2) NOT NULL COMMENT '数据范围',
  `REMARK` varchar(128) DEFAULT NULL,
  `STATUS` int(2) NOT NULL DEFAULT 1,
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `ROLE_TYPE` varchar(128) NOT NULL COMMENT '角色类型：assignment（任务分配）、security-role（管理角色）、user（普通用户）',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `UK_SYS_ROLE_NAME` (`NAME`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统权限角色表';

/*Table structure for table `sys_user` */


CREATE TABLE `sys_user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '自然主键',
  `photo` varchar(512) DEFAULT NULL COMMENT '头像',
  `LOGIN_ID` varchar(64) NOT NULL COMMENT '登录账户ID，用于权限验证的标识。',
  `NAME` varchar(64) NOT NULL COMMENT '用户名称，用于显示',
  `EMAIL` varchar(64) NOT NULL,
  `IDENTITY` int(1) NOT NULL DEFAULT 0 COMMENT '1-信访办 0-办理信访人员',
  `PASSWORD` varchar(128) NOT NULL,
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `STATUS` int(2) NOT NULL DEFAULT 1,
  `COMPANY_ID` int(11) DEFAULT NULL COMMENT '所属公司',
  `ENTITY_ID` int(11) DEFAULT NULL COMMENT '所属单位',
  `OFFICE_ID` int(11) DEFAULT NULL COMMENT '所属部门',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `AK_email_unqiue` (`EMAIL`) USING BTREE,
  UNIQUE KEY `AK_loginid_unique` (`LOGIN_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=144 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统用户表-用于系统相关权限认证方面';

/*Table structure for table `traffic_nmsserver_statistic` */



CREATE TABLE `traffic_nmsserver_statistic` (
  `id` int(11)  NOT NULL AUTO_INCREMENT,
  `commit_time` datetime DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `total` int(11) DEFAULT NULL,
  `normal` int(11) DEFAULT NULL,
  `abnormal` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='记录nms上报的服务器状态信息';

/*Table structure for table `traffic_abnormal_machine` */

CREATE TABLE `traffic_abnormal_machine` (
  `nmsServer_Id` int(11) NOT NULL,
  `hostname` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  KEY `fk_traffic_abnormal_machine` (`nmsServer_Id`),
  CONSTRAINT `fk_traffic_abnormal_machine` FOREIGN KEY (`nmsServer_Id`) REFERENCES `traffic_nmsserver_statistic` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Table structure for table `traffic_app_statistic` */


CREATE TABLE `traffic_app_statistic` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_type` int(11) NOT NULL,
  `entrance_id` int(11) DEFAULT NULL,
  `link_num` bigint(20) NOT NULL,
  `c2s_pkt_num` bigint(20) NOT NULL,
  `s2c_pkt_num` bigint(20) NOT NULL,
  `c2s_byte_len` bigint(20) NOT NULL,
  `s2c_byte_len` bigint(20) NOT NULL,
  `stat_time` datetime NOT NULL,
  PRIMARY KEY (`stat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `traffic_http_statistic` */


CREATE TABLE `traffic_http_statistic` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `web_id` int(11) NOT NULL,
  `link_num` bigint(20) NOT NULL,
  `c2s_pkt_num` bigint(20) NOT NULL,
  `s2c_pkt_num` bigint(20) NOT NULL,
  `c2s_byte_len` bigint(20) NOT NULL,
  `s2c_byte_len` bigint(20) NOT NULL,
  `stat_time` datetime NOT NULL,
  PRIMARY KEY (`stat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `traffic_ip_active_statistic` */


CREATE TABLE `traffic_ip_active_statistic` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip_addr` varchar(255) NOT NULL,
  `area_id` int(11) DEFAULT NULL,
  `link_num` bigint(20) NOT NULL,
  `c2s_pkt_num` bigint(20) NOT NULL,
  `s2c_pkt_num` bigint(20) NOT NULL,
  `c2s_byte_len` bigint(20) NOT NULL,
  `s2c_byte_len` bigint(20) NOT NULL,
  `stat_time` datetime NOT NULL,
  PRIMARY KEY (`stat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `traffic_netflow_port_info` */


CREATE TABLE `traffic_netflow_port_info` (
  `STAT_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PORT` int(2) NOT NULL,
  `NODE_NAME` varchar(64) NOT NULL,
  `NODE_IP` varchar(128) NOT NULL,
  `PORT_DESC` varchar(128) DEFAULT NULL,
  `BANDWIDTH` bigint(20) unsigned DEFAULT NULL,
  `INOCTETS` bigint(20) unsigned NOT NULL,
  `OUTOCTETS` bigint(20) unsigned NOT NULL,
  `INOCTETS_SPEED` bigint(20) unsigned NOT NULL,
  `OUTOCTETS_SPEED` bigint(20) unsigned NOT NULL,
  `INPKTS_SPEED` bigint(20) unsigned NOT NULL,
  `OUTPKTS_SPEED` bigint(20) unsigned NOT NULL,
  `RECV_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`STAT_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



/*Table structure for table `traffic_port_active_statistic` */


CREATE TABLE `traffic_port_active_statistic` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `port` int(11) NOT NULL,
  `sum` bigint(20) NOT NULL,
  `stat_time` datetime NOT NULL,
  PRIMARY KEY (`stat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `traffic_protocol_statistic` */


CREATE TABLE `traffic_protocol_statistic` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `proto_type` int(11) NOT NULL,
  `entrance_id` int(11) DEFAULT NULL,
  `link_num` bigint(20) NOT NULL,
  `c2s_pkt_num` bigint(20) NOT NULL,
  `s2c_pkt_num` bigint(20) NOT NULL,
  `c2s_byte_len` bigint(20) NOT NULL,
  `s2c_byte_len` bigint(20) NOT NULL,
  `stat_time` datetime NOT NULL,
  PRIMARY KEY (`stat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `traffic_service_statistic` */


CREATE TABLE `traffic_service_statistic` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service_type` int(11) NOT NULL,
  `entrance_id` int(11) DEFAULT NULL,
  `link_num` bigint(20) NOT NULL,
  `c2s_pkt_num` bigint(20) NOT NULL,
  `s2c_pkt_num` bigint(20) NOT NULL,
  `c2s_byte_len` bigint(20) NOT NULL,
  `s2c_byte_len` bigint(20) NOT NULL,
  `stat_time` datetime NOT NULL,
  PRIMARY KEY (`stat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `traffic_total_statistic` */


CREATE TABLE `traffic_total_statistic` (
  `STAT_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MATCH_TYPE` int(2) NOT NULL,
  `SUM` bigint(20) unsigned NOT NULL,
  `STAT_TIME` datetime NOT NULL,
  PRIMARY KEY (`STAT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Table structure for table `traffic_trans_statistic` */


CREATE TABLE `traffic_trans_statistic` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `addr_type` varchar(32) NOT NULL,
  `trans_type` int(11) NOT NULL,
  `entrance_id` int(11) DEFAULT NULL,
  `link_num` bigint(11) NOT NULL,
  `c2s_pkt_num` bigint(20) NOT NULL,
  `s2c_pkt_num` bigint(20) NOT NULL,
  `c2s_byte_len` bigint(20) NOT NULL,
  `s2c_byte_len` bigint(20) NOT NULL,
  `stat_time` datetime NOT NULL,
  PRIMARY KEY (`stat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `traffic_ua_statistic` */


CREATE TABLE `traffic_ua_statistic` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bs_type` int(11) NOT NULL,
  `os_type` int(11) NOT NULL COMMENT 'os type',
  `entrance_id` int(11) DEFAULT NULL,
  `link_num` bigint(20) NOT NULL,
  `c2s_pkt_num` bigint(20) NOT NULL,
  `s2c_pkt_num` bigint(20) NOT NULL,
  `c2s_byte_len` bigint(20) NOT NULL,
  `s2c_byte_len` bigint(20) NOT NULL,
  `stat_time` datetime NOT NULL,
  PRIMARY KEY (`stat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;



CREATE INDEX IDX_TRAFFIC_HTTP_STAT_TIME  ON traffic_http_statistic (stat_time);

CREATE INDEX IDX_TRAFFIC_APP_STAT_TIME  ON traffic_app_statistic (stat_time);

CREATE INDEX IDX_TRAFFIC_IP_ACTIVE_STAT_TIME  ON traffic_ip_active_statistic (stat_time);

CREATE INDEX IDX_NETFLOW_RECV_TIME  ON traffic_netflow_port_info (RECV_TIME);

CREATE INDEX IDX_TRAFFIC_PROTOCOL_STAT_TIME  ON traffic_protocol_statistic (stat_time);

CREATE INDEX IDX_TRAFFIC_SERVICE_STAT_TIME  ON traffic_service_statistic (stat_time);

CREATE INDEX IDX_TRAFFIC_TRANS_STAT_TIME  ON traffic_trans_statistic (stat_time);

CREATE INDEX IDX_TRAFFIC_UA_STAT_TIME  ON traffic_ua_statistic (stat_time);

CREATE INDEX IDX_NTC_PZ_REPORT_TIME  ON NTC_PZ_REPORT (report_time);

CREATE INDEX IDX_NTC_ATTR_REPORT_TIME  ON ntc_attr_type_report (report_time);

CREATE INDEX IDX_NTC_ENTRANCE_REPORT_TIME  ON ntc_entrance_report (report_time);

CREATE INDEX IDX_NTC_LWHH_REPORT_TIME  ON ntc_lwhh_report (report_time);

CREATE INDEX IDX_NTC_SERVICE_REPORT_TIME  ON ntc_service_report (report_time);

CREATE INDEX IDX_NTC_TAG_REPORT_TIME  ON ntc_tag_report (report_time);

CREATE INDEX IDX_NTC_SRC_REPORT_TIME  ON ntc_srcip_domestic_report (report_time);

CREATE INDEX IDX_NTC_DEST_REPORT_TIME  ON ntc_destip_country_report (report_time);

CREATE INDEX IDX_NTC_RADIUS_REPORT_TIME  ON NTC_RADIUS_REPORT (report_time);

CREATE INDEX IDX_SERVICE_REQ_LOG_TIME  ON SERVICES_LOG_REQUEST_LOG (OPTIME);

CREATE INDEX IDX_SERVICE_REQ_CFG_TIME  ON SERVICES_CONFIG_REQUEST_LOG (OPTIME);



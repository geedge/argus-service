/*
新增存储串联设备端口告警信息表
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for traffic_seriesdevice_port_info
-- ----------------------------
DROP TABLE IF EXISTS `traffic_seriesdevice_port_info`;
CREATE TABLE `traffic_seriesdevice_port_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `commit_time` datetime DEFAULT NULL COMMENT '时间',
  `area` varchar(255) DEFAULT '' COMMENT '区域',
  `port` varchar(255) DEFAULT NULL COMMENT '端口',
  `status` varchar(10) DEFAULT '' COMMENT 'down, up, bypass, pass',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

CREATE TABLE `traffic_asn_statistic` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `asn_type` int(11) NOT NULL COMMENT '1-src_asn 2-dst_asn',
  `asn` varchar(64) NOT NULL,
  `packets` bigint(20) NOT NULL,
  `bytes` bigint(20) NOT NULL,
  `stat_time`  datetime NOT NULL ,
  PRIMARY KEY (`stat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
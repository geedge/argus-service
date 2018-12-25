##新增统计表： 
##重点保障事件(时间粒度天)： ntc_major_task_report
CREATE TABLE `ntc_major_task_report` (
`stat_id`  int(11) NOT NULL AUTO_INCREMENT ,
`task_id`  int(20) NOT NULL ,
`task_name`  varchar(64) NOT NULL ,
`letter_num`  bigint(11) UNSIGNED NOT NULL DEFAULT 0 ,
`config_num`  bigint(11) UNSIGNED NOT NULL DEFAULT 0 ,
`monitor_num`  bigint(11) UNSIGNED NOT NULL DEFAULT 0 ,
`control_num`  bigint(11) UNSIGNED NOT NULL DEFAULT 0 ,
`report_time`  datetime NOT NULL ,
`task_time`  datetime NOT NULL ,
PRIMARY KEY (`stat_id`),
INDEX `IDX_MAJOR_TASK_REPORT_TIME` (`report_time`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci
AUTO_INCREMENT=1
ROW_FORMAT=DYNAMIC;
##事件封堵、事件监测(时间粒度小时)：ntc_task_report
CREATE TABLE `ntc_task_report` (
`stat_id`  int(11) NOT NULL AUTO_INCREMENT ,
`task_id`  int(20) NOT NULL ,
`task_name`  varchar(64) NOT NULL ,
`report_type`  int(11) NOT NULL ,#1：事件封堵；2事件监测；
`ip_num`  bigint(20) UNSIGNED NOT NULL DEFAULT 0 ,
`http_num`  bigint(11) UNSIGNED NOT NULL DEFAULT 0 ,
`mail_num`  bigint(11) UNSIGNED NOT NULL DEFAULT 0 ,
`vedio_num`  bigint(11) UNSIGNED NOT NULL DEFAULT 0 ,
`agent_num`  bigint(11) UNSIGNED NOT NULL DEFAULT 0 ,
`report_time`  datetime NOT NULL ,
`task_time`  datetime NOT NULL ,
PRIMARY KEY (`stat_id`),
INDEX `IDX_TASK_REPORT_TIME` (`report_time`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci
AUTO_INCREMENT=1
ROW_FORMAT=DYNAMIC;  
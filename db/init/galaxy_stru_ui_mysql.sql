/*Table structure for table ui -from ntc ui */
drop table if exists ui_asn_ip_cfg;
drop table if exists ui_sys_dict_info;
drop table if exists ui_code_app_dic;
drop table if exists ui_cfg_num_statistics;
drop table if exists ui_request_num_statistics;
drop table if exists ui_sys_data_dictionary_item;
drop table if exists ui_code_behavior_type_dic;
drop table if exists ui_code_browser_type_dic;
drop table if exists ui_code_os_type_dic;
drop table if exists ui_code_protocol_type_dic;
drop table if exists ui_code_service_type_dic;
drop table if exists ui_code_web_service_dic;
drop table if exists ui_service_dict_info;
drop table if exists ui_request_info;
drop table if exists ui_task_info;
drop table if exists ui_config_group_info;
drop table if exists ui_website_domain_topic;
drop table if exists ui_http_url_cfg;

CREATE TABLE `ui_asn_ip_cfg` (
    `cfg_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
    `cfg_desc` varchar(128) DEFAULT NULL,
    `ip_type` int(11) NOT NULL COMMENT 'IPV4=4,IPV6=6',
    `direction` int(11) NOT NULL COMMENT '0双向，1单向，默认缺省为双向。',
    `protocol` int(11) NOT NULL COMMENT '6表示tcp,17表示udp，0表示任意',
    `protocol_id` int(11) NOT NULL COMMENT '非0时，maat规范需写入通用IP配置表与通用协议类型配置表 此表固定写0',
    `action` int(11) NOT NULL COMMENT '1:阻断，2：监测， 5： FD 白名单，6：监测白名单，7: FD 监测都白名单，应与业务ID所代表的逻辑相匹配，8-灰名单',
    `is_valid` int(11) NOT NULL COMMENT '0无效，1有效，-1删除;1）	未审核时配置可删除;2）	审核通过，此字段置1;3）	取消审核通过，此字段置0',
    `is_audit` int(11) NOT NULL COMMENT '0未审核，1审核通过，2审核未通过，3取消审核通过;1）	审核未通过，配置可修改;2）	审核通过，配置不可删除，只能取消审核通过',
    `creator_id` int(11) NOT NULL COMMENT '取自sys_user.id',
    `create_time` datetime NOT NULL,
    `editor_id` int(11) DEFAULT NULL COMMENT '取自sys_user.id',
    `edit_time` datetime DEFAULT NULL,
    `auditor_id` int(11) DEFAULT NULL COMMENT '取自sys_user.id',
    `audit_time` datetime DEFAULT NULL,
    `service_id` int(11) NOT NULL COMMENT '参考系统业务类型管理表',
    `request_id` int(11) NOT NULL COMMENT '取自request_info.id',
    `region_id` int(11) NOT NULL COMMENT '取自服务接口返回的maat配置的域id，配置初始入库时获取',
    `is_area_effective` int(11) NOT NULL COMMENT '0否，1是',
    `classify` varchar(128) DEFAULT NULL COMMENT '分类id，多个用英文逗号分隔',
    `attribute` varchar(128) DEFAULT NULL COMMENT '性质id，多个用英文逗号分隔',
    `lable` varchar(128) DEFAULT NULL COMMENT '标签id,多个用英文逗号分隔',
    `area_effective_ids` varchar(1024) DEFAULT NULL COMMENT '多个英文逗号分隔',
    `function_id` int(11) DEFAULT NULL,
    `cfg_region_code` int(11) DEFAULT NULL,
    `cfg_type` varchar(64) DEFAULT NULL,
    `ip_pattern` int(11) NOT NULL,
    `src_ip_address` varchar(128) NOT NULL,
    `port_pattern` int(11) NOT NULL,
    `src_port` varchar(16) NOT NULL,
    `dest_ip_address` varchar(128) DEFAULT NULL,
    `dest_port` varchar(16) DEFAULT NULL,
    `ratelimit` varchar(10) DEFAULT NULL,
    `asn_ip_group` int(11) DEFAULT NULL,
    `user_region1` varchar(1024) DEFAULT '' COMMENT '预留自定义域1',
    `user_region2` varchar(1024) DEFAULT '' COMMENT '预留自定义域2',
    `user_region3` varchar(1024) DEFAULT '' COMMENT '预留自定义域3',
    `user_region4` varchar(1024) DEFAULT '' COMMENT '预留自定义域4',
    `user_region5` varchar(1024) DEFAULT '' COMMENT '预留自定义域5',
    PRIMARY KEY (`cfg_id`)
  ) ENGINE=FEDERATED AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CONNECTION='mysql://root:111111@192.168.10.204:3306/gwall/asn_ip_cfg';

CREATE TABLE `ui_sys_dict_info` (
  `sys_dict_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '字典ID',
  `item_type` int(11) DEFAULT NULL COMMENT '数据类型\r\n1：地域\r\n2：运营商\r\n3：app强特征作用域（item_code为service_id，item_value为作用域描述）\r\n',
  `item_code` varchar(64) DEFAULT NULL COMMENT '编码',
  `item_value` varchar(64) DEFAULT NULL COMMENT '英文，用于界面国际化配置文件的code',
  `item_desc` varchar(128) DEFAULT NULL COMMENT '描述信息',
  `parent_id` int(11) DEFAULT NULL COMMENT '父ID 无父属性，默认填0',
  `is_leaf` int(11) DEFAULT NULL COMMENT '是否叶子节点0-否 1-是；只有一级填0；',
  `level_no` int(11) DEFAULT NULL,
  `is_valid` int(11) DEFAULT NULL COMMENT '有效标志 1-有效 0-无效',
  `creator_id` int(11) DEFAULT NULL COMMENT '创建人员 取自sys_user.id',
  `create_time` datetime DEFAULT NULL COMMENT '配置时间',
  `editor_id` int(11) DEFAULT NULL COMMENT '修改人员取自sys_user.id',
  `edit_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_initianlize` int(11) NOT NULL DEFAULT 0 COMMENT '是否是初始化数据：1是，0否',
  PRIMARY KEY (`sys_dict_id`)
) ENGINE=FEDERATED AUTO_INCREMENT=448 DEFAULT CHARSET=utf8 CONNECTION='mysql://root:111111@192.168.10.204:3306/gwall/sys_dict_info';


  CREATE TABLE `ui_code_app_dic` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
   `app_name` varchar(255) CHARACTER SET utf8mb4 DEFAULT '',
   `app_name_zh` varchar(255) CHARACTER SET utf8mb4 DEFAULT '',
   `view_code` varchar(255) CHARACTER SET utf8mb4 DEFAULT '' COMMENT '应用编码',
   `icon` varchar(255) DEFAULT '',
   `behavior` varchar(255) CHARACTER SET utf8mb4 DEFAULT '' COMMENT '行为细分',
   `core_app` int(11) DEFAULT NULL COMMENT '是否是核心应用（1：是，0：否）',
   `app_service_id` int(10) unsigned zerofill DEFAULT NULL COMMENT '业务类型编号',
   `app_service` varchar(255) CHARACTER SET utf8mb4 DEFAULT '',
   `lib` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'lib类别',
   PRIMARY KEY (`id`)
  ) ENGINE=FEDERATED AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CONNECTION='mysql://root:111111@192.168.10.204:3306/gwall/code_app_dic';



CREATE TABLE `ui_cfg_num_statistics` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
   `function_id` int(11) NOT NULL,
   `service_id` int(11) NOT NULL,
   `action` int(11) NOT NULL,
   `cfg_state` int(11) NOT NULL COMMENT '0未审核，1已审核，2审核未通过，3审核取消，-1删除',
   `compile_id` int(11) DEFAULT 0,
   `statistic_time` datetime DEFAULT NULL COMMENT '统计时间',
   PRIMARY KEY (`id`)
 )  ENGINE=FEDERATED AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CONNECTION='mysql://root:111111@192.168.10.204:3306/gwall/cfg_num_statistics';


CREATE TABLE `ui_request_num_statistics` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `function_id` int(11) NOT NULL,
    `service_id` int(11) NOT NULL,
    `request_id` int(11) NOT NULL COMMENT '来函信息',
    `statistic_time` datetime DEFAULT NULL COMMENT '统计时间',
    `compile_id` int(11) DEFAULT 0,
    PRIMARY KEY (`id`)
  )ENGINE=FEDERATED AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CONNECTION='mysql://root:111111@192.168.10.204:3306/gwall/request_num_statistics';


CREATE TABLE `ui_sys_data_dictionary_item` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `item_code` varchar(128) NOT NULL COMMENT '编码（数据表中存储的值如：0、1、2、3）',
    `item_value` varchar(256) NOT NULL COMMENT '编码对应的值（页面上展现的具体意义）',
    `item_desc` varchar(256) DEFAULT NULL COMMENT '编码描述信息',
    `item_sort` int(2) NOT NULL COMMENT '排序序号',
    `status` int(11) DEFAULT 1 COMMENT '该条数据是否可用（1：可用 0：禁用）',
    `type` int(11) DEFAULT 1 COMMENT '该条记录页面是否可维护（1：可维护 0：不可维护）',
    `dictionary_id` int(11) DEFAULT NULL COMMENT '关联数据字典外键',
    PRIMARY KEY (`id`)
  )ENGINE=FEDERATED AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CONNECTION='mysql://root:111111@192.168.10.204:3306/gwall/sys_data_dictionary_item';

 CREATE TABLE `ui_code_behavior_type_dic` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `behavior_type` varchar(255) DEFAULT NULL,
    `view_code` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
  ) ENGINE=FEDERATED AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CONNECTION='mysql://root:111111@192.168.10.204:3306/gwall/code_behavior_type_dic';

CREATE TABLE `ui_code_browser_type_dic` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
   `browser_type` varchar(255) DEFAULT NULL,
   `view_code` varchar(255) DEFAULT NULL,
   `icon` varchar(255) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=FEDERATED AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CONNECTION='mysql://root:111111@192.168.10.204:3306/gwall/code_browser_type_dic';


CREATE TABLE `ui_code_os_type_dic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `os_type` varchar(255) DEFAULT NULL,
  `view_code` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=FEDERATED AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CONNECTION='mysql://root:111111@192.168.10.204:3306/gwall/code_os_type_dic';



CREATE TABLE `ui_code_protocol_type_dic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `protocol_type` varchar(255) DEFAULT '',
  `view_code` varchar(255) DEFAULT NULL,
  `service_id` int(11) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=FEDERATED AUTO_INCREMENT=1 DEFAULT CHARSET=utf8  CONNECTION='mysql://root:111111@10.0.4.6:3306/ntc_db/code_protocol_type_dic';


CREATE TABLE `ui_code_service_type_dic` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
   `service_type` varchar(255) DEFAULT '' COMMENT '业务类型',
   `view_code` varchar(255) DEFAULT NULL COMMENT '界面编码',
   PRIMARY KEY (`id`)
 ) ENGINE=FEDERATED AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CONNECTION='mysql://root:111111@192.168.10.204:3306/gwall/code_service_type_dic';


CREATE TABLE `ui_code_web_service_dic` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `website` varchar(255) DEFAULT '',
  `domain_name` varchar(255) DEFAULT '' COMMENT '域名',
  `view_code` varchar(255) DEFAULT '',
  `icon` varchar(255) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=FEDERATED AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CONNECTION='mysql://root:111111@192.168.10.204:3306/gwall/code_web_service_dic';



CREATE TABLE `ui_service_dict_info` (
   `service_dict_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '字典ID',
   `item_type` int(11) DEFAULT NULL COMMENT '数据类型\r\n1：分类\r\n2：性质\r\n3：标签\r\n',
   `item_code` varchar(64) DEFAULT NULL COMMENT '编码',
   `item_value` varchar(64) DEFAULT NULL COMMENT '编码对应值',
   `item_desc` varchar(128) DEFAULT NULL COMMENT '描述信息',
   `parent_id` int(11) DEFAULT NULL COMMENT '父ID无父属性，默认填0',
   `is_leaf` int(11) DEFAULT NULL COMMENT '是否叶子节点0-否 1-是；只有一级填0；',
   `level_no` int(11) DEFAULT NULL,
   `is_valid` int(11) DEFAULT NULL COMMENT '有效标志1-有效 0-无效',
   `creator_id` int(11) DEFAULT NULL COMMENT '创建人员取自sys_user.id',
   `create_time` datetime DEFAULT NULL COMMENT '配置时间',
   `editor_id` int(11) DEFAULT NULL COMMENT '修改人员取自sys_user.id',
   `edit_time` datetime DEFAULT NULL COMMENT '修改时间',
   PRIMARY KEY (`service_dict_id`)
 ) ENGINE=FEDERATED AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CONNECTION='mysql://root:111111@192.168.10.204:3306/gwall/service_dict_info';


 CREATE TABLE `ui_request_info` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `request_number` varchar(512) DEFAULT NULL COMMENT 'request_number字段与request_title互换使用，request_number当来函标题使用',
    `request_org` varchar(128) NOT NULL COMMENT '来函单位',
    `request_time` date NOT NULL COMMENT '来函时间',
    `request_title` varchar(512) NOT NULL COMMENT 'request_number字段与request_title互换使用，request_title当来函函号使用',
    `request_content` varchar(4000) NOT NULL COMMENT '来函内容',
    `is_valid` int(11) NOT NULL COMMENT '0无效，1有效，-1删除',
    `is_audit` int(11) NOT NULL COMMENT '是否审核',
    `creator_id` int(11) NOT NULL COMMENT '创建人员',
    `create_time` date NOT NULL COMMENT '配置时间',
    `editor_id` int(11) DEFAULT NULL COMMENT '修改人员',
    `edit_time` date DEFAULT NULL COMMENT '修改时间',
    `auditor_id` int(11) DEFAULT NULL COMMENT '审核人员',
    `audit_time` date DEFAULT NULL COMMENT '审核时间',
    `task_id` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `request_info_request_number` (`request_number`) USING BTREE
  ) ENGINE=FEDERATED AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CONNECTION='mysql://root:111111@192.168.10.204:3306/gwall/request_info';


  CREATE TABLE `ui_task_info` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置ID',
     `task_name` varchar(64) NOT NULL,
     `task_org` varchar(128) NOT NULL,
     `task_time` date NOT NULL,
     `task_desc` varchar(512) DEFAULT NULL,
     `is_valid` int(11) NOT NULL,
     `is_audit` int(11) NOT NULL,
     `creator_id` int(11) NOT NULL,
     `create_time` date NOT NULL,
     `editor_id` int(11) DEFAULT NULL,
     `edit_time` date DEFAULT NULL,
     `auditor_id` int(11) DEFAULT NULL,
     `audit_time` date DEFAULT NULL,
     PRIMARY KEY (`id`),
     UNIQUE KEY `task_info_task_name` (`task_name`) USING BTREE
   )  ENGINE=FEDERATED AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CONNECTION='mysql://root:111111@192.168.10.204:3306/gwall/task_info';


  CREATE TABLE `ui_config_group_info` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `group_id` bigint(20) NOT NULL,
    `group_name` varchar(128) NOT NULL,
    `is_issued` int(11) NOT NULL COMMENT '0否，1是',
    `insert_time` datetime NOT NULL,
    `update_time` datetime DEFAULT NULL,
    `group_type` int(11) DEFAULT NULL COMMENT '1:协议，2：内容，3：区域 ,4 asn',
    `compile_id` int(11) DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=FEDERATED AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CONNECTION='mysql://root:111111@192.168.10.204:3306/gwall/config_group_info';

  CREATE TABLE `ui_website_domain_topic` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `website_service_id` bigint(20) DEFAULT NULL COMMENT '网站服务Id',
    `domain` varchar(200) DEFAULT NULL COMMENT '域名',
    `topic_id` bigint(20) DEFAULT NULL COMMENT '主题Id',
    `create_time` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
    `is_valid` int(1) DEFAULT 1,
    `icon` varchar(200) DEFAULT NULL COMMENT '图标',
    `desc` varchar(400) DEFAULT NULL COMMENT '描述信息',
    `creator_id` int(11) DEFAULT NULL,
    PRIMARY KEY (`id`)
  ) ENGINE=FEDERATED AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CONNECTION='mysql://root:111111@192.168.10.204:3306/gwall/website_domain_topic';




/*Table structure for table `ui_http_url_cfg` ,for keyword to url from url whitelist */
CREATE TABLE `ui_http_url_cfg` (
   `cfg_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
   `cfg_desc` varchar(128) DEFAULT NULL,
   `cfg_keywords` varchar(2048) NOT NULL,
   `action` int(11) NOT NULL COMMENT '1:阻断，2：监测， 5： FD 白名单，6：监测白名单，7: FD 监测都白名单，应与业务ID所代表的逻辑相匹配，8-灰名单',
   `is_valid` int(11) NOT NULL COMMENT '0无效，1有效，-1删除;1）    未审核时配置可删除;2）    审核通过，此字段置1;3）    取消审核通过，此字段置0',
   `is_audit` int(11) NOT NULL COMMENT '0未审核，1审核通过，2审核未通过，3取消审核通过;1）    审核未通过，配置可修改;2）    审核通过，配置不可删除，只能取消审核通过',
   `creator_id` int(11) NOT NULL COMMENT '取自sys_user.id',
   `create_time` datetime NOT NULL,
   `editor_id` int(11) DEFAULT NULL COMMENT '取自sys_user.id',
   `edit_time` datetime DEFAULT NULL,
   `auditor_id` int(11) DEFAULT NULL COMMENT '取自sys_user.id',
   `audit_time` datetime DEFAULT NULL,
   `service_id` int(11) NOT NULL COMMENT '参考系统业务类型管理表',
   `request_id` int(11) NOT NULL COMMENT '取自request_info.id',
   `compile_id` int(11) NOT NULL COMMENT '取自服务接口返回的maat配置的编译id，配置初始入库时获取。',
   `is_area_effective` int(11) NOT NULL COMMENT '0否，1是',
   `classify` varchar(128) DEFAULT NULL COMMENT '分类id，多个用英文逗号分隔',
   `attribute` varchar(128) DEFAULT NULL COMMENT '性质id，多个用英文逗号分隔',
   `lable` varchar(128) DEFAULT NULL COMMENT '标签id,多个用英文逗号分隔',
   `expr_type` int(11) NOT NULL COMMENT '0：无表达式，1：与表达式',
   `match_method` int(11) NOT NULL COMMENT 'expr_type:0时有意义，其它情况必须置0。0：子串匹配；1：右匹配；2：左匹配；3：完全匹配',
   `is_hexbin` int(11) NOT NULL COMMENT '默认为0:大小写不敏感，且非HEX;1:HEX格式二进制;2:大小写敏感，且非HEX',
   `area_effective_ids` varchar(1024) DEFAULT NULL COMMENT '多个英文逗号分隔',
   `function_id` int(11) NOT NULL,
   `cfg_region_code` int(11) DEFAULT NULL,
   `cfg_type` varchar(64) DEFAULT NULL,
   `ratelimit` varchar(10) DEFAULT NULL COMMENT '限速比例,0到1之间',
   `user_region1` varchar(1024) DEFAULT '' COMMENT '预留自定义域1',
   `user_region2` varchar(1024) DEFAULT '' COMMENT '预留自定义域2',
   `user_region3` varchar(1024) DEFAULT '' COMMENT '预留自定义域3',
   `user_region4` varchar(1024) DEFAULT '' COMMENT '预留自定义域4',
   `user_region5` varchar(1024) DEFAULT '' COMMENT '预留自定义域5',
   PRIMARY KEY (`cfg_id`)
 ) ENGINE=FEDERATED AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CONNECTION='mysql://root:111111@192.168.10.204:3306/gwall/http_url_cfg';
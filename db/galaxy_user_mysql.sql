
-- 打开 my.ini 给 [mysqld] 增加如下配置：
-- sql_mode=ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION

set global read_only=0;

create user 'galaxy'@'%' identified by 'ceiec2018!';

create database galaxy-service DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;

grant all privileges on galaxy-service.* to 'galaxy'@'%' identified by 'ceiec2018!';

flush privileges;


-- 显示mariadb 目前支持引擎

show engines;

-- mariadb运行命令下增加federated引擎，用于跨库获取标签表 ---

INSTALL PLUGIN federated SONAME 'ha_federatedx.so';
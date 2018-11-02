#NTC Galaxy  服务
##应用简介
Galaxy 基于Springmvc 搭建 Restful 服务平台，使用方可通过Swagger API 友好界面方式了解与调试服务所提供的接口。

##内置功能

###配置加载
1. 配置解析：基于applicationConfig-parser 配置文件，提供基于MAAT结构和非MAAT结构的配置还原、规则验证处理。
2. 配置转换：基于applicationConfig-maatXX 配置文件，对解析数据根据maat规则进行格式转换，根据存储库（oracle\redis)不同，形成不同的转换规则。
3. 业务规则处理：基于applicationConfig-rule 配置文件，对处理的配置规则进行详细定义。包含业务动作关系、MAAT配置域关系、存储关系、多分发关系及域自定义关系。

###数据获取
1. 基础日志服务接口：对原始业务日志数据封装，内置性能优化、超时重试机制。
2. 报表日志服务接口：对报表进行数据进行统一封装，内置统计、多源处理机制。
3. 流量构成服务接口：对流量格式转换数据进行统一封装，内置统计、多源处理机制。

##安装部署

###ide 环境
需将如下内容放置在\<build>\</build>里。

```
 <resources>
            <resource>
                <directory>src/main/resources</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                    <include>**/*.xsd</include>
                    <include>**/*.yml</include>
                </includes>
                <filtering>true</filtering>
            </resource>

            <resource>
                <directory>src/main/resources</directory>
                <filtering>false</filtering>
                <includes>
                    <include>**/*.mmdb</include>
                    <include>**/*.dat</include>
                    <include>**/*.sh</include>
                    <include>**/*.bat</include>
                    <include>**/*.sql</include>
                </includes>
            </resource>

            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>true</filtering>
            </resource>

    </resources>

```

###数据库初始化

1. 打开 my.cnf 给 [mysqld] 增加如下配置：
```
sql_mode="ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION"
```
2. linux 系统下，运行bin\init-db.sh

###数据库更新
1. 更新pom.xml
```
<id>init-db</id> 内部增加一个更新sql语句

**注意**：初始化后，需把初始化sql注释掉，否则将对整个库进行更新

```
2. 可通过xls、csv 进行动态数据导入

    参看operation 标签的例子进行增加。
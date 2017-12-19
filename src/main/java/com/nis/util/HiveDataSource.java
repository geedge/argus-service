package com.nis.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.jolbox.bonecp.BoneCPDataSource;

public class HiveDataSource {
	private final static Logger logger = Logger.getLogger(HiveDataSource.class);
	static Connection conn = null;
	static ResultSet rsA = null;
	static Statement stA = null;

	static ResultSet rsB = null;
	static Statement stB = null;

	public static ResultSet query(String sql, String searchActiveSys) throws Exception {
		if (null != searchActiveSys && searchActiveSys.equals("4")) {// A版数据库
			logger.info("开始连接数据中心A版日志库--------------------------");
			if (conn == null || conn.isClosed()) {
				ApplicationContext ct = new ClassPathXmlApplicationContext("applicationContext-mybatis.xml");
				BoneCPDataSource datasource = (BoneCPDataSource) ct.getBean("HiveADataSource");
				conn = datasource.getConnection();
			}
			logger.info("连接数据中心A版日志库成功--------------------------");
			stA = conn.createStatement();
			String hiveAName = "use " + Constants.HIVEADBNAME;
			// stA.execute("use xa_dfbhit_p_hive");
			stA.execute(hiveAName);
			logger.info("开始执行查询数据中心A版日志库操作--------------------------" + sql);
			rsA = stA.executeQuery(sql);
			logger.info("执行查询数据中心A版日志库成功--------------------------");
			return rsA;
		} else {// 目前默认B版数据库,后期增加C版数据库
			logger.info("开始连接数据中心B版日志库--------------------------");
			if (conn == null || conn.isClosed()) {
				ApplicationContext ct = new ClassPathXmlApplicationContext("applicationContext-mybatis.xml");
				BoneCPDataSource datasource = (BoneCPDataSource) ct.getBean("HiveBDataSource");
				conn = datasource.getConnection();
			}
			logger.info("连接数据中心B版日志库成功--------------------------");
			stB = conn.createStatement();
			String hiveBName = "use " + Constants.HIVEBDBNAME;
			// stB.execute("use xa_z2_mesalog_hive");
			stB.execute(hiveBName);
			logger.info("开始执行查询数据中心B版日志库操作--------------------------" + sql);
			rsB = stB.executeQuery(sql);
			logger.info("执行查询数据中心B版日志库成功--------------------------");
			return rsB;
		}

	}

}

package com.nis.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jolbox.bonecp.BoneCPDataSource;
import com.nis.web.service.SpringContextHolder;

public class HiveDataSource {
	private final static Logger logger = LoggerFactory.getLogger(HiveDataSource.class);
	static BoneCPDataSource datasource = null;
	static Connection conn = null;
	static ResultSet rs = null;
	static Statement st = null;

	public static ResultSet query(String sql) throws Exception {
		if (datasource == null) {
			datasource = (BoneCPDataSource) SpringContextHolder.getBean("HiveDataSource");
		}
		conn = datasource.getConnection();
		logger.info("连接数据中心日志库成功--------------------------");
		st = conn.createStatement();
		// logger.info("开始选择{}数据库--------------------------", Constants.HIVEDBNAME);
		// String hiveAName = "use " + Constants.HIVEDBNAME;
		// st.execute(hiveAName);
		// logger.info("选择数据库{}成功,开始执行查询", Constants.HIVEDBNAME);
		// logger.info("选择数据库{}成功,开始执行查询", Constants.HIVEDBNAME);
		rs = st.executeQuery(sql);
		logger.info("执行查询语句成功sql={}",sql);
		return rs;

	}

	public static void closeConn() {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (st != null) {
				st.close();
				st = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
			logger.info("关闭数据中心连接成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("关闭数据中心连接失败,失败原因" + e);
		}
	}
}

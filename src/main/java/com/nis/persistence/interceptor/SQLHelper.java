package com.nis.persistence.interceptor;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.log4j.Logger;

import com.nis.datasource.CustomerContextHolder;
import com.nis.domain.Page;
import com.nis.persistence.dialect.ClusterDialect;
import com.nis.persistence.dialect.DB2Dialect;
import com.nis.persistence.dialect.DerbyDialect;
import com.nis.persistence.dialect.Dialect;
import com.nis.persistence.dialect.H2Dialect;
import com.nis.persistence.dialect.HSQLDialect;
import com.nis.persistence.dialect.MySQLDialect;
import com.nis.persistence.dialect.OracleDialect;
import com.nis.persistence.dialect.PostgreSQLDialect;
import com.nis.persistence.dialect.SQLServer2005Dialect;
import com.nis.persistence.dialect.SybaseDialect;
import com.nis.util.Configurations;
import com.nis.util.Reflections;
import com.nis.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SQL工具类
 * 
 * @author poplar.yfyang / thinkgem
 * @version 2013-8-28
 */
public class SQLHelper {
	static final Logger logger = Logger.getLogger(SQLHelper.class);
	/**
	 * 对SQL参数(?)设值,参考org.apache.ibatis.executor.parameter.DefaultParameterHandler
	 *
	 * @param ps
	 *            表示预编译的 SQL 语句的对象。
	 * @param mappedStatement
	 *            MappedStatement
	 * @param boundSql
	 *            SQL
	 * @param parameterObject
	 *            参数对象
	 * @throws java.sql.SQLException
	 *             数据库异常
	 */
	@SuppressWarnings("unchecked")
	public static void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
			Object parameterObject) throws SQLException {
		ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if (parameterMappings != null) {
			Configuration configuration = mappedStatement.getConfiguration();
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
					if (parameterObject == null) {
						value = null;
					} else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)
							&& boundSql.hasAdditionalParameter(prop.getName())) {
						value = boundSql.getAdditionalParameter(prop.getName());
						if (value != null) {
							value = configuration.newMetaObject(value)
									.getValue(propertyName.substring(prop.getName().length()));
						}
					} else {
						value = metaObject == null ? null : metaObject.getValue(propertyName);
					}
					@SuppressWarnings("rawtypes")
					TypeHandler typeHandler = parameterMapping.getTypeHandler();
					if (typeHandler == null) {
						throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName
								+ " of statement " + mappedStatement.getId());
					}
					typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
				}
			}
		}
	}

	/**
	 * 
	 * @Title: getDBType
	 * @Description: TODO(根据动态数据塬获取数据库的类型)
	 * @param @return
	 *            入参
	 * @return String 返回类型
	 * @author （darnell）
	 * @throws @date
	 *             2016年8月15日 下午6:35:58
	 * @version V1.0
	 */
	public static String getDBType() {
		String dataSource = CustomerContextHolder.getCustomerType();
		if (StringUtils.isBlank(dataSource)) {
			return "mysql";
		} else if (dataSource.equals(CustomerContextHolder.DATA_SOURCE_A)) {
			return "mysql";
		} else if (dataSource.equals(CustomerContextHolder.DATA_SOURCE_B)) {
			return "oracle";
		} else if (dataSource.equals(CustomerContextHolder.DATA_SOURCE_C)) {
			return "oracle";
		} else if (dataSource.equals(CustomerContextHolder.DATA_SOURCE_D)) {
			return "oracle";
		} else if (dataSource.equals(CustomerContextHolder.DATA_SOURCE_F)) {
			return "oracle";
		} else if (dataSource.equals(CustomerContextHolder.DATA_SOURCE_G)) {
			return "oracle";
		} else if (dataSource.equals(CustomerContextHolder.DATA_SOURCE_H)) {//神通数据库
			return "cluster";
		}else {
			return "mysql";
		}

	}

	/**
	 * 查询总纪录数
	 * 
	 * @param sql
	 *            SQL语句
	 * @param connection
	 *            数据库连接
	 * @param mappedStatement
	 *            mapped
	 * @param parameterObject
	 *            参数
	 * @param boundSql
	 *            boundSql
	 * @return 总记录数
	 * @throws SQLException
	 *             sql查询错误
	 */
	public static Long getCount(final String sql, final Connection connection, final MappedStatement mappedStatement,
			final Object parameterObject, final BoundSql boundSql, Log log) throws SQLException {
		logger.info("oracle查询count开始----"+System.currentTimeMillis());
		String dbName = getDBType();
		final String countSql;
		if ("oracle".equals(dbName)) {
			countSql = "select count(*) from (" + removeOrders(sql) + ") tmp_count";
		}  else {
			if(sql.toLowerCase().indexOf("limit") >-1){
				countSql = "select count(*) from (" +sql+ ") tmp_count";
			}else{
				countSql = "select count(*) from (" + removeOrders(sql) + ") tmp_count";
			}
		}
		System.out.println("countSql:"+countSql);
		Connection conn = connection;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (log.isDebugEnabled()) {
				log.debug("COUNT SQL: "
						+ StringUtils.replaceEach(countSql, new String[] { "\n", "\t" }, new String[] { " ", " " }));
			}
			if (conn == null) {
				conn = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
			}
			ps = conn.prepareStatement(countSql);
			BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
					boundSql.getParameterMappings(), parameterObject);
			// 解决MyBatis 分页foreach 参数失效 start
			if (Reflections.getFieldValue(boundSql, "metaParameters") != null) {
				MetaObject mo = (MetaObject) Reflections.getFieldValue(boundSql, "metaParameters");
				Reflections.setFieldValue(countBS, "metaParameters", mo);
			}
			// 解决MyBatis 分页foreach 参数失效 end
			SQLHelper.setParameters(ps, mappedStatement, countBS, parameterObject);
			rs = ps.executeQuery();
			Long count = 0l;
			if (rs.next()) {
				count = rs.getLong(1);
			}
			return count;
		} finally {
			logger.info("oracle查询count结束----"+System.currentTimeMillis());
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	/**
	 * 根据数据库方言，生成特定的分页sql
	 * 
	 * @param sql
	 *            Mapper中的Sql语句
	 * @param page
	 *            分页对象
	 * @param dialect
	 *            方言类型
	 * @return 分页SQL
	 */
	public static String generatePageSql(String sql, Page<Object> page) {

		Dialect dialect = getDBDialect();
		if (dialect.supportsLimit()) {
			return dialect.getLimitString(sql, page.getFirstResult(), page.getMaxResults());
		} else {
			return sql;
		}
	}

	/**
	 * 设置属性，支持自定义方言类和制定数据库的方式 <code>dialectClass</code>,自定义方言类。可以不配置这项
	 * <ode>dbms</ode> 数据库类型，插件支持的数据库 <code>sqlPattern</code> 需要拦截的SQL ID
	 * 
	 * @param p
	 *            属性
	 */
	public static Dialect getDBDialect() {
		Dialect dialect = null;
		String dbType = getDBType();
		if ("db2".equals(dbType)) {
			dialect = new DB2Dialect();
		} else if ("derby".equals(dbType)) {
			dialect = new DerbyDialect();
		} else if ("h2".equals(dbType)) {
			dialect = new H2Dialect();
		} else if ("hsql".equals(dbType)) {
			dialect = new HSQLDialect();
		} else if ("mysql".equals(dbType)) {
			dialect = new MySQLDialect();
		} else if ("oracle".equals(dbType)) {
			dialect = new OracleDialect();
		} else if ("postgre".equals(dbType)) {
			dialect = new PostgreSQLDialect();
		} else if ("mssql".equals(dbType) || "sqlserver".equals(dbType)) {
			dialect = new SQLServer2005Dialect();
		} else if ("sybase".equals(dbType)) {
			dialect = new SybaseDialect();
		}else if ("cluster".equals(dbType)) {
			dialect = new ClusterDialect();
		}
		if (dialect == null) {
			throw new RuntimeException("mybatis dialect error.");
		}

		return dialect;

	}

	/**
	 * 去除qlString的select子句。
	 * 
	 * @param hql
	 * @return
	 */
	@SuppressWarnings("unused")
	private static String removeSelect(String qlString) {
		int beginPos = qlString.toLowerCase().indexOf("from");
		return qlString.substring(beginPos);
	}

	/**
	 * 去除hql的orderBy子句。
	 * 
	 * @param hql
	 * @return
	 */
	@SuppressWarnings("unused")
	private static String removeOrders(String qlString) {
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(qlString);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

}

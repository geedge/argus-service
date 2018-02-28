package com.nis.web.service.restful;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.nis.domain.restful.ConfigCompile;
import com.nis.domain.restful.ConfigGroupRelation;
import com.nis.domain.restful.DigestRegion;
import com.nis.domain.restful.IpRegion;
import com.nis.domain.restful.NumRegion;
import com.nis.domain.restful.StrRegion;
import com.nis.util.Configurations;

/**
 * jdbc测试批量插入
 * 
 * @author RenKaiGe-Office
 *
 */
public class UpdateCompileByJDBCThread implements Runnable {
	private static Logger logger = Logger.getLogger(SaveCompileByJDBCThread.class);
	private static Long start;
	private CountDownLatch latch;
	private String tableName;
	private Connection conn;
	private List<ConfigCompile> compileList;
	private List<ConfigGroupRelation> groupList;
	private List<IpRegion> ipRegionList;
	private List<NumRegion> numRegionList;
	private List<StrRegion> strRegionList;
	private List<DigestRegion> digestRegionList;
	private Date opTime;

	public UpdateCompileByJDBCThread() {
		super();
	}

	public UpdateCompileByJDBCThread(Connection conn, CountDownLatch latch, Long start, Date opTime) {
		super();
		this.conn = conn;
		this.latch = latch;
		this.start = start;
		this.opTime = opTime;
	}

	public UpdateCompileByJDBCThread(String tableName, Connection conn, CountDownLatch latch, Long start, Date opTime) {
		super();
		this.tableName = tableName;
		this.conn = conn;
		this.latch = latch;
		this.start = start;
		this.opTime = opTime;
	}

	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public CountDownLatch getLatch() {
		return latch;
	}

	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public List<ConfigCompile> getCompileList() {
		return compileList;
	}

	public void setCompileList(List<ConfigCompile> compileList) {
		this.compileList = compileList;
	}

	public List<ConfigGroupRelation> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<ConfigGroupRelation> groupList) {
		this.groupList = groupList;
	}

	public List<IpRegion> getIpRegionList() {
		return ipRegionList;
	}

	public void setIpRegionList(List<IpRegion> ipRegionList) {
		this.ipRegionList = ipRegionList;
	}

	public List<NumRegion> getNumRegionList() {
		return numRegionList;
	}

	public void setNumRegionList(List<NumRegion> numRegionList) {
		this.numRegionList = numRegionList;
	}

	public List<StrRegion> getStrRegionList() {
		return strRegionList;
	}

	public void setStrRegionList(List<StrRegion> strRegionList) {
		this.strRegionList = strRegionList;
	}

	public List<DigestRegion> getDigestRegionList() {
		return digestRegionList;
	}

	public void setDigestRegionList(List<DigestRegion> digestRegionList) {
		this.digestRegionList = digestRegionList;
	}

	public synchronized static java.sql.Timestamp utileDate2TimeStamp(java.util.Date udate) {
		java.sql.Timestamp sqlDate = null;
		long t = udate.getTime();
		sqlDate = new java.sql.Timestamp(t);
		return sqlDate;

	}

	public static void updateCompile(List<ConfigCompile> compileList, Connection conn, Date opTime,
			List<Exception> msgList) {
		if (null != compileList && compileList.size() > 0) {
			try {
				StringBuffer sb = new StringBuffer();
				String tabName = Configurations.getStringProperty("compileTabName", "CONFIG_COMPILE");
				String fieldName = Configurations.getStringProperty("updateFields", "IS_VALID");
				String condition= Configurations.getStringProperty("compileUpdateCondition", "COMPILE_ID");
				sb.append("update ").append(tabName).append(" set ");
						
				String fieldNameObj [] =  fieldName.split(",");
				//加上where条件的参数名称
				String newParamsStr = fieldName+","+condition;
				for (int i = 0; i < fieldNameObj.length; i++) {
					sb.append(fieldNameObj[i]+"=?");
					if (i+1<fieldNameObj.length) {
						sb.append(",");
					}
				}
						
				//是否包含更新时间字段 值为数据库当前时间
				if (Configurations.getBooleanProperty("hasLastUpdate", true)) {
					sb.append(", LAST_UPDATE= sysdate");
				}
				sb.append(" where ");
				String [] condObjs = condition.split(",");
				for (int i = 0; i < condObjs.length; i++) {
					sb.append(condObjs[i]+"=?");
					if (i+1<condObjs.length) {
						sb.append(" and ");
					}
				}
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				
				
				for (ConfigCompile compile : compileList) {
					setPsParams(newParamsStr.split(","),ps,compile);
					ps.addBatch();
				}
				ps.executeBatch();
			} catch (Exception e) {
				logger.error(e);
				msgList.add(e);
			}
		}
	}
	
	public static void updateGroup(List<ConfigGroupRelation> groupList, Connection conn, Date opTime,
			List<Exception> msgList) {
		if (null != groupList && groupList.size() > 0) {
			try {
				StringBuffer sb = new StringBuffer();
				
				String tabName = Configurations.getStringProperty("groupTabName", "CONFIG_GROUP");
				String fieldName = Configurations.getStringProperty("updateFields", "IS_VALID");
				String condition = Configurations.getStringProperty("groupUpdateCondition", "GROUP_ID");
				
				sb.append(" update  ").append(tabName);
				sb.append(" set ");
				String fieldNameObj [] =  fieldName.split(",");
				//加上where条件的参数名称
				String newParamsStr = fieldName+","+condition;
				for (int i = 0; i < fieldNameObj.length; i++) {
					sb.append(fieldNameObj[i]+"=?");
					if (i+1<fieldNameObj.length) {
						sb.append(",");
					}
				}
						
				//是否包含更新时间字段 值为数据库当前时间
				if (Configurations.getBooleanProperty("hasLastUpdate", true)) {
					sb.append(", LAST_UPDATE= sysdate");
				}
//						+ "where GROUP_ID=? and COMPILE_ID=? ");
				sb.append(" where ");
				String [] condObjs = condition.split(",");
				for (int i = 0; i < condObjs.length; i++) {
					sb.append(condObjs[i]+"=?");
					if (i+1<condObjs.length) {
						sb.append(" and ");
					}
				}
				
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				for (ConfigGroupRelation group : groupList) {
					setPsParams(newParamsStr.split(","), ps, group);
					ps.addBatch();
				}
				ps.executeBatch();
			} catch (Exception e) {
				logger.error(e);
				msgList.add(e);
			}
		}
	}

	public static void updateIPRegion(String name, List<IpRegion> ipRegionList, Connection conn, Date opTime,
			List<Exception> msgList) {
		if (null != ipRegionList && ipRegionList.size() > 0) {
			try {
				StringBuffer sb = new StringBuffer();
				
				
				String fieldName = Configurations.getStringProperty("updateFields", "IS_VALID");
				String condition = Configurations.getStringProperty("regionId", "REGION_ID");
				sb.append("update ");
				sb.append(name);
				sb.append(" set ");
				
				String fieldNameObj [] =  fieldName.split(",");
				//加上where条件的参数名称
				String newParamsStr = fieldName+","+condition;
				for (int i = 0; i < fieldNameObj.length; i++) {
					sb.append(fieldNameObj[i]+"=?");
					if (i+1<fieldNameObj.length) {
						sb.append(",");
					}
				}
						
				//是否包含更新时间字段 值为数据库当前时间
				if (Configurations.getBooleanProperty("hasLastUpdate", true)) {
					sb.append(", LAST_UPDATE= sysdate");
				}
//						+ "where GROUP_ID=? and COMPILE_ID=? ");
				sb.append(" where ");
				String [] condObjs = condition.split(",");
				for (int i = 0; i < condObjs.length; i++) {
					sb.append(condObjs[i]+"=?");
					if (i+1<condObjs.length) {
						sb.append(" and ");
					}
				}
				
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				for (IpRegion ipRegion : ipRegionList) {
					setPsParams(newParamsStr.split(","), ps, ipRegion);
					ps.addBatch();
				}
				ps.executeBatch();
			} catch (Exception e) {
				logger.error(e);
				msgList.add(e);
			}
		}
	}

	public static void updateNumRegion(String name, List<NumRegion> numRegionList, Connection conn, Date opTime,
			List<Exception> msgList) {
		if (null != numRegionList && numRegionList.size() > 0) {
			try {
				StringBuffer sb = new StringBuffer();
				String fieldName = Configurations.getStringProperty("updateFields", "IS_VALID");
				String condition = Configurations.getStringProperty("regionId", "REGION_ID");
				sb.append("update ");
				sb.append(name);
				sb.append(" set ");
				
				String fieldNameObj [] =  fieldName.split(",");
				//加上where条件的参数名称
				String newParamsStr = fieldName+","+condition;
				for (int i = 0; i < fieldNameObj.length; i++) {
					sb.append(fieldNameObj[i]+"=?");
					if (i+1<fieldNameObj.length) {
						sb.append(",");
					}
				}
						
				//是否包含更新时间字段 值为数据库当前时间
				if (Configurations.getBooleanProperty("hasLastUpdate", true)) {
					sb.append(", LAST_UPDATE= sysdate");
				}
//						+ "where GROUP_ID=? and COMPILE_ID=? ");
				sb.append(" where ");
				String [] condObjs = condition.split(",");
				for (int i = 0; i < condObjs.length; i++) {
					sb.append(condObjs[i]+"=?");
					if (i+1<condObjs.length) {
						sb.append(" and ");
					}
				}
				
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				for (NumRegion numRegion : numRegionList) {
					setPsParams(newParamsStr.split(","), ps, numRegion);
					ps.addBatch();
				}
				ps.executeBatch();
			} catch (Exception e) {
				logger.error(e);
				msgList.add(e);
			}
		}
	}

	public static void updateStrRegion(String name, List<StrRegion> strRegionList, Connection conn, Date opTime,
			List<Exception> msgList) {
		if (null != strRegionList && strRegionList.size() > 0) {
			try {
				StringBuffer sb = new StringBuffer();
				String fieldName = Configurations.getStringProperty("updateFields", "IS_VALID");
				String condition = Configurations.getStringProperty("regionId", "REGION_ID");
				sb.append("update ");
				sb.append(name);
				sb.append(" set ");
				
				String fieldNameObj [] =  fieldName.split(",");
				//加上where条件的参数名称
				String newParamsStr = fieldName+","+condition;
				for (int i = 0; i < fieldNameObj.length; i++) {
					sb.append(fieldNameObj[i]+"=?");
					if (i+1<fieldNameObj.length) {
						sb.append(",");
					}
				}
						
				//是否包含更新时间字段 值为数据库当前时间
				if (Configurations.getBooleanProperty("hasLastUpdate", true)) {
					sb.append(", LAST_UPDATE= sysdate");
				}
//						+ "where GROUP_ID=? and COMPILE_ID=? ");
				sb.append(" where ");
				String [] condObjs = condition.split(",");
				for (int i = 0; i < condObjs.length; i++) {
					sb.append(condObjs[i]+"=?");
					if (i+1<condObjs.length) {
						sb.append(" and ");
					}
				}
				
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				for (StrRegion strRegion : strRegionList) {
					setPsParams(newParamsStr.split(","), ps, strRegion);
					ps.addBatch();
				}
				ps.executeBatch();
			} catch (Exception e) {
				logger.error(e);
				msgList.add(e);
			}
		}
	}
	
	public static void updateDigestRegion(String name, List<DigestRegion> digestRegionList, Connection conn, Date opTime,
			List<Exception> msgList) {
		if (null != digestRegionList && digestRegionList.size() > 0) {
			try {
				StringBuffer sb = new StringBuffer();
				String fieldName = Configurations.getStringProperty("updateFields", "IS_VALID");
				String condition = Configurations.getStringProperty("regionId", "REGION_ID");
				sb.append("update ");
				sb.append(name);
				sb.append(" set ");
				
				String fieldNameObj [] =  fieldName.split(",");
				//加上where条件的参数名称
				String newParamsStr = fieldName+","+condition;
				for (int i = 0; i < fieldNameObj.length; i++) {
					sb.append(fieldNameObj[i]+"=?");
					if (i+1<fieldNameObj.length) {
						sb.append(",");
					}
				}
						
				//是否包含更新时间字段 值为数据库当前时间
				if (Configurations.getBooleanProperty("hasLastUpdate", true)) {
					sb.append(", LAST_UPDATE= sysdate");
				}
//						+ "where GROUP_ID=? and COMPILE_ID=? ");
				sb.append(" where ");
				String [] condObjs = condition.split(",");
				for (int i = 0; i < condObjs.length; i++) {
					sb.append(condObjs[i]+"=?");
					if (i+1<condObjs.length) {
						sb.append(" and ");
					}
				}
				
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				for (DigestRegion digestRegion : digestRegionList) {
					setPsParams(newParamsStr.split(","), ps, digestRegion);
					ps.addBatch();
				}
				ps.executeBatch();
			} catch (Exception e) {
				logger.error(e);
				msgList.add(e);
			}
		}
	}
	/**
	 * @Description: 向PreparedStatement设置参数
	 * @author (zdx)
	 * @date 2017年10月26日 上午9:47:33
	 * @param fieldNameObj
	 * @param ps
	 * @param object
	 * @throws Exception
	 */
	private static void setPsParams(String [] fieldNameObj,PreparedStatement ps,Object object) throws Exception{
		Object[] obj = new Object[fieldNameObj.length] ;
	
		for (int i = 0; i < fieldNameObj.length; i++) {
			String name = fieldNameObj[i].toLowerCase().trim();
			name = name.substring(0,1).toUpperCase()+name.substring(1);
			if (name.indexOf("_")>-1) {
//				name = name.substring(0, name.indexOf("_"))+name.substring(name.indexOf("_")+1,name.indexOf("_")+2).toUpperCase()+name.substring(name.indexOf("_")+2);
//			}
			
				while (name.indexOf("_")>-1) {
					name = name.substring(0, name.indexOf("_"))+name.substring(name.indexOf("_")+1,name.indexOf("_")+2).toUpperCase()+name.substring(name.indexOf("_")+2);
				}
			}
			
			//需要特殊处理字段名
			if (name.equals("DoBlacklist")) {
				name = "DoBlackList";
			}else if (name.equals("AffairId")) {
				name = "AffAirId";
			}else if (name.equals("TopicId")) {
				name = "TopIcId";
			}
			
			Method method = object.getClass().getMethod("get"+name);
			obj[i]= method.invoke(object);
		}
		for (int x = 0; x < obj.length; x++) {
			
			if (obj[x] instanceof Date) {
				ps.setObject(x + 1, utileDate2TimeStamp((Date) obj[x]));							
			}else {
				ps.setObject(x + 1, obj[x]);
			}
		}
	}
	
	/**
	 * 
	 * @Description: 根据compileId更新与之对应的group关系表的生效状态
	 * @author (zdx)
	 * @date 2017年8月16日 上午9:46:44
	 * @param compileList
	 * @param conn
	 * @param opTime
	 * @param msgList
	 */
	public static void updateGroupByCompileId(List<ConfigCompile> compileList, Connection conn, Date opTime,
			List<Exception> msgList) {
		if (null != compileList && compileList.size() > 0) {
			try {
				StringBuffer sb = new StringBuffer();
				String tabName = Configurations.getStringProperty("groupTabName", "CONFIG_GROUP");
				sb.append("update  "+tabName+" set IS_VALID=?,op_time=?,LAST_UPDATE=sysdate where COMPILE_ID=? ");
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				for (ConfigCompile compile : compileList) {
					Object[] obj = new Object[] { compile.getIsValid(), opTime,compile.getCompileId() };
					for (int x = 0; x < obj.length; x++) {
						if (x == 1) {
							ps.setTimestamp(x + 1, utileDate2TimeStamp(opTime));
						} else {
							ps.setObject(x + 1, obj[x]);
						}
					}
					ps.addBatch();
				}
				ps.executeBatch();
			} catch (Exception e) {
				logger.error(e);
				msgList.add(e);
			}
		}
	}
	@Override
	public void run() {
		List<Exception> msgList = ConfigSourcesService.getMsgList();
		synchronized (msgList) {
			try {
				if (null != compileList && compileList.size() > 0) {
					updateCompile(compileList, conn, opTime, msgList);
				} else if (null != groupList && groupList.size() > 0) {
					updateGroup(groupList, conn, opTime, msgList);
				} else if (null != ipRegionList && ipRegionList.size() > 0) {
					updateIPRegion(tableName, ipRegionList, conn, opTime, msgList);
				} else if (null != strRegionList && strRegionList.size() > 0) {
					updateStrRegion(tableName, strRegionList, conn, opTime, msgList);
				} else if (null != numRegionList && numRegionList.size() > 0) {
					updateNumRegion(tableName, numRegionList, conn, opTime, msgList);
				} else if (null != digestRegionList && digestRegionList.size() > 0) {
					updateDigestRegion(tableName, digestRegionList, conn, opTime, msgList);
				}
				
				if((null !=compileList && compileList.size() > 0)&&(null != groupList && groupList.size()==0)){
					updateGroupByCompileId(compileList, conn, opTime, msgList);
				}
				
				if((null !=compileList && compileList.size() > 0)&&(null != groupList && groupList.size()==0)){
					updateGroupByCompileId(compileList, conn, opTime, msgList);
				}
				latch.countDown();
				// System.out.println("latchCount=======================" +
				// latch.getCount());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}

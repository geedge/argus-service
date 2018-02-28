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
public class SaveCompileByJDBCThread implements Runnable {
	private static Logger logger = Logger.getLogger(SaveCompileByJDBCThread.class);
	private static Long start;
	private CountDownLatch latch;
	private Boolean isStrongStr;
	private String tableName;
	private Connection conn;
	private List<ConfigCompile> compileList;
	private List<ConfigGroupRelation> groupList;
	private List<IpRegion> ipRegionList;
	private List<NumRegion> numRegionList;
	private List<StrRegion> strRegionList;
	private List<DigestRegion> digestRegionList;
	
	private Method method = null;
	public SaveCompileByJDBCThread() {
		super();
	}

	public SaveCompileByJDBCThread(Connection conn, CountDownLatch latch, Long start) {
		super();
		this.conn = conn;
		this.latch = latch;
		this.start = start;
	}

	public SaveCompileByJDBCThread(String tableName, Connection conn, CountDownLatch latch, Long start) {
		super();
		this.tableName = tableName;
		this.conn = conn;
		this.latch = latch;
		this.start = start;
	}

	public SaveCompileByJDBCThread(boolean isStrongStr, String tableName, Connection conn, CountDownLatch latch,
			Long start) {
		super();
		this.isStrongStr = isStrongStr;
		this.tableName = tableName;
		this.conn = conn;
		this.latch = latch;
		this.start = start;
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

	public Boolean getIsStrongStr() {
		return isStrongStr;
	}

	public void setIsStrongStr(Boolean isStrongStr) {
		this.isStrongStr = isStrongStr;
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

	public static java.sql.Timestamp utileDate2TimeStamp(java.util.Date udate) {
		java.sql.Timestamp sqlDate = null;
		long t = udate.getTime();
		sqlDate = new java.sql.Timestamp(t);
		return sqlDate;

	}

	public static void saveCompile(List<ConfigCompile> compileList, Connection conn, List<Exception> msgList) {
		if (null != compileList && compileList.size() > 0) {
			try {
				String tabName = Configurations.getStringProperty("compileTabName", "CONFIG_COMPILE");
				String fieldName = Configurations.getStringProperty("compileFieldName", "COMPILE_ID");
				
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(getSqlStr(tabName, fieldName));
				for (ConfigCompile compile : compileList) {
					setPsParams(fieldName.split(","),ps,compile);
					ps.addBatch();
				}
		
//zdx2017-10-25				
//				sb.append(
//						"insert into CONFIG_COMPILE (COMPILE_ID ,SERVICE ,ACTION , CONT_TYPE, ATTR_TYPE, CONT_LABEL, Task_id, Guarantee_ID, AFFAIR_ID, TOPIC_ID, DO_BLACKLIST ,DO_LOG ,EFFECTIVE_RANGE , ACTIVE_SYS, CONFIG_PERCENT ,CONFIG_OPTION ,START_TIME ,END_TIME , USER_REGION, IS_VALID,GROUP_NUM,FATHER_CFG_ID ,OP_TIME,LAST_UPDATE ) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate)");
//				conn.setAutoCommit(false);
//				PreparedStatement ps = conn.prepareStatement(sb.toString());
//				for (ConfigCompile compile : compileList) {
//					Object[] obj = new Object[] { compile.getCompileId(), compile.getService(), compile.getAction(),
//							compile.getContType(), compile.getAttrType(), compile.getContLabel(), compile.getTaskId(),
//							compile.getGuaranteeId(), compile.getAffAirId(), compile.getTopIcId(),
//							compile.getDoBlackList(), compile.getDoLog(), compile.getEffectiveRange(),
//							compile.getActiveSys(), compile.getConfigPercent(), compile.getConfigOption(),
//							compile.getStartTime(), compile.getEndTime(), compile.getUserRegion(), compile.getIsValid(),
//							compile.getGroupNum(), compile.getFatherCfgId(), compile.getOpTime() };
//
//					for (int x = 0; x < obj.length; x++) {
//						if (x == 16) {
//							ps.setTimestamp(x + 1, utileDate2TimeStamp(compile.getStartTime()));
//						} else if (x == 17) {
//							ps.setTimestamp(x + 1, utileDate2TimeStamp(compile.getEndTime()));
//						} else if (x == 22) {
//							ps.setTimestamp(x + 1, utileDate2TimeStamp(compile.getOpTime()));
//						} else {
//							ps.setObject(x + 1, obj[x]);
//						}
//					}
//					ps.addBatch();
//				}
				ps.executeBatch();
			} catch (Exception e) {
				logger.error(e);
				msgList.add(e);
			}
		}
	}

	public static void saveGroup(List<ConfigGroupRelation> groupList, Connection conn, List<Exception> msgList) {
		
		if (null != groupList && groupList.size() > 0) {
			try {
				StringBuffer sb = new StringBuffer();
				String tabName = Configurations.getStringProperty("groupTabName", "CONFIG_GROUP");
				String fieldName = Configurations.getStringProperty("groupFieldName", "ID");
				String groupSeqName = Configurations.getStringProperty("groupSeqName", "SEQ_CONFIG_GROUP");
				sb.append("insert into ").append(tabName);
				sb.append("(").append(fieldName);
				//是否包含更新时间字段 值为数据库当前时间
				if (Configurations.getBooleanProperty("hasLastUpdate", true)) {
					sb.append(", LAST_UPDATE");
				}
				sb.append(") values ( ");
				conn.setAutoCommit(false);				
				String fieldNameObj [] =  fieldName.split(",");
				for (int i = 0; i < fieldNameObj.length; i++) {
					//第一个字段必须是分组表主键
					if (i==0) {
						sb.append(groupSeqName).append(".nextval");
					}else{
						sb.append("?");
					}
					if (i+1<fieldNameObj.length) {
						sb.append(",");
					}
				}
				//是否包含更新时间字段 值为数据库当前时间
				if (Configurations.getBooleanProperty("hasLastUpdate", true)) {
					sb.append(", sysdate");
				}
				sb.append(")");
				conn.setAutoCommit(false);
				
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				
				for (ConfigGroupRelation group : groupList) {
					//setPsParams(fieldNameObj,ps,group);
					
					Object[] obj = new Object[fieldNameObj.length] ;
					
					for (int i = 1; i < fieldNameObj.length; i++) {
						String name = fieldNameObj[i].toLowerCase().trim();
						name = name.substring(0,1).toUpperCase()+name.substring(1);
						if (name.indexOf("_")>-1) {
//							name = name.substring(0, name.indexOf("_"))+name.substring(name.indexOf("_")+1,name.indexOf("_")+2).toUpperCase()+name.substring(name.indexOf("_")+2);
//						}
						
							while (name.indexOf("_")>-1) {
								name = name.substring(0, name.indexOf("_"))+name.substring(name.indexOf("_")+1,name.indexOf("_")+2).toUpperCase()+name.substring(name.indexOf("_")+2);
							}
						}
						
						Method method = group.getClass().getMethod("get"+name);
						obj[i]= method.invoke(group);
					}
					for (int x = 1; x < obj.length; x++) {
						
						if (obj[x] instanceof Date) {
							ps.setObject(x, utileDate2TimeStamp((Date) obj[x]));							
						}else {
							ps.setObject(x , obj[x]);
						}
					}
					ps.addBatch();
				}
				ps.executeBatch();
			} catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
				msgList.add(e);
			}
		}
		
		
//zdx2017-10-25	
//		if (null != groupList && groupList.size() > 0) {
//			try {
//				StringBuffer sb = new StringBuffer();
//				sb.append(
//						"insert into CONFIG_GROUP (ID,GROUP_ID, COMPILE_ID, IS_VALID, LAST_UPDATE, OP_TIME ) values ( seq_config_group.nextval, ?, ?, ?, sysdate, ?) ");
//				conn.setAutoCommit(false);
//				PreparedStatement ps = conn.prepareStatement(sb.toString());
//				for (ConfigGroupRelation group : groupList) {
//					Object[] obj = new Object[] { group.getGroupId(), group.getCompileId(), group.getIsValid(),
//							group.getOpTime() };
//					for (int x = 0; x < obj.length; x++) {
//						if (x == 3) {
//							ps.setTimestamp(x + 1, utileDate2TimeStamp(group.getOpTime()));
//						} else {
//							ps.setObject(x + 1, obj[x]);
//						}
//					}
//					ps.addBatch();
//				}
//				ps.executeBatch();
//			} catch (Exception e) {
//				logger.error(e);
//				msgList.add(e);
//			}
//		}
	}

	public static void saveIPRegion(String name, List<IpRegion> ipRegionList, Connection conn,
			List<Exception> msgList) {
		
		if (null != ipRegionList && ipRegionList.size() > 0) {
			try {
				String fieldName = Configurations.getStringProperty("ipRegionFieldName", "REGION_ID");
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(getSqlStr(name, fieldName));
				for (IpRegion ipRegion : ipRegionList) {
					setPsParams(fieldName.split(","),ps,ipRegion);
					ps.addBatch();
				}
				ps.executeBatch();
			} catch (Exception e) {
				logger.error(e);
				msgList.add(e);
			}
		}
			
			
//zdx2017-10-25		
//		if (null != ipRegionList && ipRegionList.size() > 0) {
//			try {
//				StringBuffer sb = new StringBuffer();
//				sb.append("insert into ");
//				sb.append(name);
//				sb.append(
//						"(REGION_ID, GROUP_ID, ADDR_TYPE, SRC_IP, MASK_SRC_IP, SRC_PORT, MASK_SRC_PORT, DST_IP, MASK_DST_IP, DST_PORT, MASK_DST_PORT, PROTOCOL, DIRECTION, IS_VALID, OP_TIME,LAST_UPDATE ) values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate) ");
//				conn.setAutoCommit(false);
//				PreparedStatement ps = conn.prepareStatement(sb.toString());
//				for (IpRegion ipRegion : ipRegionList) {
//					Object[] obj = new Object[] { ipRegion.getRegionId(), ipRegion.getGroupId(), ipRegion.getAddrType(),
//							ipRegion.getSrcIp(), ipRegion.getMaskSrcIp(), ipRegion.getSrcPort(),
//							ipRegion.getMaskSrcPort(), ipRegion.getDstIp(), ipRegion.getMaskDstIp(),
//							ipRegion.getDstPort(), ipRegion.getMaskDstPort(), ipRegion.getProtocol(),
//							ipRegion.getDirection(), ipRegion.getIsValid(), ipRegion.getOpTime()
//
//					};
//					for (int x = 0; x < obj.length; x++) {
//						if (x == 14) {
//							ps.setTimestamp(x + 1, utileDate2TimeStamp(ipRegion.getOpTime()));
//						} else {
//							ps.setObject(x + 1, obj[x]);
//						}
//					}
//					ps.addBatch();
//				}
//				ps.executeBatch();
//			} catch (Exception e) {
//				logger.error(e);
//				msgList.add(e);
//			}
//		}
	}

	public static void saveNumRegion(String name, List<NumRegion> numRegionList, Connection conn,
			List<Exception> msgList) {
		if (null != numRegionList && numRegionList.size() > 0) {
			try {
				String fieldName = Configurations.getStringProperty("numRegionFieldName", "REGION_ID");
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(getSqlStr(name, fieldName));
				for (NumRegion numRegion : numRegionList) {
					setPsParams(fieldName.split(","),ps,numRegion);
					ps.addBatch();
				}
				
				ps.executeBatch();
			} catch (Exception e) {
				logger.error(e);
				msgList.add(e);
			}
		}
		
		
//zdx2017-10-25		
//		if (null != numRegionList && numRegionList.size() > 0) {
//			try {
//				StringBuffer sb = new StringBuffer();
//				sb.append("insert into ");
//				sb.append(name);
//				sb.append(
//						" (REGION_ID,GROUP_ID,LOW_BOUNDARY,UP_BOUNDARY,IS_VALID,OP_TIME,LAST_UPDATE) values(?, ?, ?, ?, ?, ?, sysdate)");
//				conn.setAutoCommit(false);
//				PreparedStatement ps = conn.prepareStatement(sb.toString());
//				for (NumRegion numRegion : numRegionList) {
//					Object[] obj = new Object[] { numRegion.getRegionId(), numRegion.getGroupId(),
//							numRegion.getLowBoundary(), numRegion.getUpBoundary(), numRegion.getIsValid(),
//							numRegion.getOpTime() };
//					for (int x = 0; x < obj.length; x++) {
//						if (x == 5) {
//							ps.setTimestamp(x + 1, utileDate2TimeStamp(numRegion.getOpTime()));
//						} else {
//							ps.setObject(x + 1, obj[x]);
//						}
//					}
//					ps.addBatch();
//				}
//				ps.executeBatch();
//			} catch (Exception e) {
//				logger.error(e);
//				msgList.add(e);
//			}
//		}
	}

	public static void saveStrongStrRegion(String name, List<StrRegion> strRegionList, Connection conn,
			List<Exception> msgList) {
		
		if (null != strRegionList && strRegionList.size() > 0) {
			try {
				String fieldName = Configurations.getStringProperty("strongStrRegionFieldName", "REGION_ID");
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(getSqlStr(name, fieldName));
				for (StrRegion strRegion : strRegionList) {
					setPsParams(fieldName.split(","),ps,strRegion);
					ps.addBatch();
				}
				ps.executeBatch();
			} catch (Exception e) {
				logger.error(e);
				msgList.add(e);
			}
		}
		
		
		
		
		
		
		
//zdx2017-10-25		
//		if (null != strRegionList && strRegionList.size() > 0) {
//			try {
//				StringBuffer sb = new StringBuffer();
//				sb.append("insert into ");
//				sb.append(name);
//				sb.append(
//						"(REGION_ID, GROUP_ID, DISTRICT, KEYWORDS, EXPR_TYPE, MATCH_METHOD , IS_HEXBIN, IS_VALID, OP_TIME,LAST_UPDATE ) values(?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate) ");
//				conn.setAutoCommit(false);
//				PreparedStatement ps = conn.prepareStatement(sb.toString());
//
//				for (StrRegion strRegion : strRegionList) {
//					Object[] obj = new Object[] { strRegion.getRegionId(), strRegion.getGroupId(),
//							strRegion.getDistrict(), strRegion.getKeywords(), strRegion.getExprType(),
//							strRegion.getMatchMethod(), strRegion.getIsHexbin(), strRegion.getIsValid(),
//							strRegion.getOpTime() };
//					for (int x = 0; x < obj.length; x++) {
//						if (x == 8) {
//							ps.setTimestamp(x + 1, utileDate2TimeStamp(strRegion.getOpTime()));
//						} else {
//							ps.setObject(x + 1, obj[x]);
//						}
//					}
//					ps.addBatch();
//				}
//				ps.executeBatch();
//			} catch (Exception e) {
//				logger.error(e);
//				msgList.add(e);
//			}
//		}
	}

	public static void saveStrRegion(String name, List<StrRegion> strRegionList, Connection conn,
			List<Exception> msgList) {
		
		if (null != strRegionList && strRegionList.size() > 0) {
			try {
				String fieldName = Configurations.getStringProperty("strRegionFieldName", "REGION_ID");
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(getSqlStr(name, fieldName));
				for (StrRegion strRegion : strRegionList) {
					setPsParams(fieldName.split(","),ps,strRegion);
					ps.addBatch();
				}
				ps.executeBatch();
			} catch (Exception e) {
				logger.error(e);
				msgList.add(e);
			}
		}
		
//		
//zdx2017-10-25		
//		if (null != strRegionList && strRegionList.size() > 0) {
//			try {
//				StringBuffer sb = new StringBuffer();
//				sb.append("insert into ");
//				sb.append(name);
//				sb.append(
//						"(REGION_ID, GROUP_ID, KEYWORDS, EXPR_TYPE, MATCH_METHOD , IS_HEXBIN, IS_VALID, OP_TIME,LAST_UPDATE ) values( ?, ?, ?, ?, ?, ?, ?, ?, sysdate) ");
//				conn.setAutoCommit(false);
//				PreparedStatement ps = conn.prepareStatement(sb.toString());
//
//				for (StrRegion strRegion : strRegionList) {
//					Object[] obj = new Object[] { strRegion.getRegionId(), strRegion.getGroupId(),
//							strRegion.getKeywords(), strRegion.getExprType(), strRegion.getMatchMethod(),
//							strRegion.getIsHexbin(), strRegion.getIsValid(), strRegion.getOpTime() };
//					for (int x = 0; x < obj.length; x++) {
//						if (x == 7) {
//							ps.setTimestamp(x + 1, utileDate2TimeStamp(strRegion.getOpTime()));
//						} else {
//							ps.setObject(x + 1, obj[x]);
//						}
//					}
//					ps.addBatch();
//				}
//				ps.executeBatch();
//			} catch (Exception e) {
//				logger.error(e);
//				msgList.add(e);
//			}
//		}
	}

	public static void saveDigestRegion(String name, List<DigestRegion> digestRegionList, Connection conn,
			List<Exception> msgList) {
		if (null != digestRegionList && digestRegionList.size() > 0) {
			try {
				String fieldName = Configurations.getStringProperty("digestRegionFieldName", "REGION_ID");
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(getSqlStr(name, fieldName));
				for (DigestRegion numRegion : digestRegionList) {
					setPsParams(fieldName.split(","),ps,numRegion);
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
	 * 
	 * @Description:根据表名和字段名称构建sql
	 * @author (zdx)
	 * @date 2017年10月26日 上午9:50:16
	 * @param tabName
	 * @param fieldName
	 * @return
	 */
	private static String getSqlStr(String tabName,String fieldName){
		
		StringBuffer sb = new StringBuffer();
		sb.append("insert into ");
		sb.append(tabName);
		sb.append("(").append(fieldName);
		
		//是否包含更新时间字段 值为数据库当前时间
		if (Configurations.getBooleanProperty("hasLastUpdate", true)) {
			sb.append(", LAST_UPDATE");
		}
		sb.append(") values (");
				
		String fieldNameObj [] =  fieldName.split(",");
		for (int i = 0; i < fieldNameObj.length; i++) {
			sb.append("?");
			if (i+1<fieldNameObj.length) {
				sb.append(",");
			}
		}
		//是否包含更新时间字段 值为数据库当前时间
		if (Configurations.getBooleanProperty("hasLastUpdate", true)) {
			sb.append(", sysdate");
		}		
		
		sb.append(") ");
		return sb.toString();
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
	@Override
	public void run() {
		List<Exception> msgList = ConfigSourcesService.getMsgList();
		synchronized (msgList) {
			try {
				if (null != compileList && compileList.size() > 0) {
					saveCompile(compileList, conn, msgList);
				} else if (null != groupList && groupList.size() > 0) {
					saveGroup(groupList, conn, msgList);
				} else if (null != ipRegionList && ipRegionList.size() > 0) {
					saveIPRegion(tableName, ipRegionList, conn, msgList);
				} else if (null != strRegionList && strRegionList.size() > 0) {
					if (null != isStrongStr && isStrongStr) {
						saveStrongStrRegion(tableName, strRegionList, conn, msgList);
					} else {
						saveStrRegion(tableName, strRegionList, conn, msgList);
					}
				} else if (null != numRegionList && numRegionList.size() > 0) {
					saveNumRegion(tableName, numRegionList, conn, msgList);
				} else if (null != digestRegionList && digestRegionList.size() > 0) {
					saveDigestRegion(tableName, digestRegionList, conn, msgList);
				}
				latch.countDown();
				System.out.println("latchCount=======================" + latch.getCount());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}

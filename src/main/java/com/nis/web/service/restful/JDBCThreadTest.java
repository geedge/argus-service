package com.nis.web.service.restful;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import com.nis.domain.restful.ConfigCompile;
import com.nis.domain.restful.ConfigGroupRelation;
import com.nis.domain.restful.IpRegion;
import com.nis.domain.restful.NumRegion;
import com.nis.domain.restful.StrRegion;
import com.nis.web.service.SaveRequestLogThread;

/**
 * jdbc测试批量插入
 * 
 * @author RenKaiGe-Office
 *
 */
public class JDBCThreadTest implements Runnable {

	private static SaveRequestLogThread thread;
	private static Long start;
	private CountDownLatch latch;
	private Boolean isStrongStr;
	private String tableName;
	private int batchSize;
	private Connection conn;
	private List<ConfigCompile> compileList;
	private List<ConfigGroupRelation> groupList;
	private List<IpRegion> ipRegionList;
	private List<NumRegion> numRegionList;
	private List<StrRegion> strRegionList;

	public JDBCThreadTest() {
		super();
	}

	public JDBCThreadTest(int batchSize, Connection conn, CountDownLatch latch, SaveRequestLogThread thread,
			Long start) {
		super();
		this.batchSize = batchSize;
		this.conn = conn;
		this.latch = latch;
	}

	public JDBCThreadTest(String tableName, int batchSize, Connection conn, CountDownLatch latch,
			SaveRequestLogThread thread, Long start) {
		super();
		this.tableName = tableName;
		this.batchSize = batchSize;
		this.conn = conn;
		this.latch = latch;
	}

	public JDBCThreadTest(boolean isStrongStr, String tableName, int batchSize, Connection conn, CountDownLatch latch,
			SaveRequestLogThread thread, Long start) {
		super();
		this.isStrongStr = isStrongStr;
		this.tableName = tableName;
		this.batchSize = batchSize;
		this.conn = conn;
		this.latch = latch;
	}

	public SaveRequestLogThread getThread() {
		return thread;
	}

	public void setThread(SaveRequestLogThread thread) {
		this.thread = thread;
	}

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
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

	public static java.sql.Timestamp utileDate2TimeStamp(java.util.Date udate) {
		java.sql.Timestamp sqlDate = null;
		long t = udate.getTime();
		sqlDate = new java.sql.Timestamp(t);
		return sqlDate;

	}

	public static void saveCompile(List<ConfigCompile> compileList, Connection conn, int batchSize) {
		if (null != conn) {
			try {
				StringBuffer sb = new StringBuffer();
				sb.append(
						"insert into CONFIG_COMPILE (COMPILE_ID ,SERVICE ,ACTION , CONT_TYPE, ATTR_TYPE, CONT_LABEL, Task_id, Guarantee_ID, AFFAIR_ID, TOPIC_ID, DO_BLACKLIST ,DO_LOG ,EFFECTIVE_RANGE , ACTIVE_SYS, CONFIG_PERCENT ,CONFIG_OPTION ,START_TIME ,END_TIME , USER_REGION, IS_VALID,GROUP_NUM,FATHER_CFG_ID ,OP_TIME,LAST_UPDATE ) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate)");
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				int count = 0;
				for (ConfigCompile compile : compileList) {
					count++;
					Object[] obj = new Object[] { compile.getCompileId(), compile.getService(), compile.getAction(),
							compile.getContType(), compile.getAttrType(), compile.getContLabel(), compile.getTaskId(),
							compile.getGuaranteeId(), compile.getAffairId(), compile.getTopicId(),
							compile.getDoBlacklist(), compile.getDoLog(), compile.getEffectiveRange(),
							compile.getActiveSys(), compile.getConfigPercent(), compile.getConfigOption(),
							compile.getStartTime(), compile.getEndTime(), compile.getUserRegion(), compile.getIsValid(),
							compile.getGroupNum(), compile.getFatherCfgId(), compile.getOpTime() };

					for (int x = 0; x < obj.length; x++) {
						if (x == 16) {
							ps.setTimestamp(x + 1, utileDate2TimeStamp(compile.getStartTime()));
						} else if (x == 17) {
							ps.setTimestamp(x + 1, utileDate2TimeStamp(compile.getEndTime()));
						} else if (x == 22) {
							ps.setTimestamp(x + 1, utileDate2TimeStamp(compile.getOpTime()));
						} else {
							ps.setObject(x + 1, obj[x]);
						}
					}
					ps.addBatch();
					// if (count % batchSize == 0) {
					// ps.executeBatch();
					// ps.clearBatch();
					// }
				}
				ps.executeBatch();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					conn.close();
					conn = null;
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		// ps.close();
		// conn.commit();
	}

	public static void saveGroup(List<ConfigGroupRelation> groupList, Connection conn, int batchSize) {
		if (null != conn) {
			try {
				StringBuffer sb = new StringBuffer();
				sb.append(
						"insert into CONFIG_GROUP (ID,GROUP_ID, COMPILE_ID, IS_VALID, LAST_UPDATE, OP_TIME ) values ( seq_config_group.nextval, ?, ?, ?, sysdate, ?) ");
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				int count = 0;
				for (ConfigGroupRelation group : groupList) {
					count++;
					Object[] obj = new Object[] { group.getGroupId(), group.getCompileId(), group.getIsValid(),
							group.getOpTime() };
					for (int x = 0; x < obj.length; x++) {
						if (x == 3) {
							ps.setTimestamp(x + 1, utileDate2TimeStamp(group.getOpTime()));
						} else {
							ps.setObject(x + 1, obj[x]);
						}
					}
					ps.addBatch();
					// if (count % batchSize == 0) {
					// ps.executeBatch();
					// ps.clearBatch();
					// }
				}
				ps.executeBatch();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					conn.close();
					conn = null;
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		// ps.close();
		// conn.commit();
	}

	public static void saveIPRegion(String name, List<IpRegion> ipRegionList, Connection conn, int batchSize) {
		if (null != conn) {
			try {
				StringBuffer sb = new StringBuffer();
				sb.append("insert into ");
				sb.append(name);
				sb.append(
						"(REGION_ID, GROUP_ID, ADDR_TYPE, SRC_IP, MASK_SRC_IP, SRC_PORT, MASK_SRC_PORT, DST_IP, MASK_DST_IP, DST_PORT, MASK_DST_PORT, PROTOCOL, DIRECTION, IS_VALID, OP_TIME,LAST_UPDATE ) values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate) ");
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				int count = 0;
				for (IpRegion ipRegion : ipRegionList) {
					count++;
					Object[] obj = new Object[] { ipRegion.getRegionId(), ipRegion.getGroupId(), ipRegion.getAddrType(),
							ipRegion.getSrcIp(), ipRegion.getMaskSrcIp(), ipRegion.getSrcPort(),
							ipRegion.getMaskSrcPort(), ipRegion.getDstIp(), ipRegion.getMaskDstIp(),
							ipRegion.getDstPort(), ipRegion.getMaskDstPort(), ipRegion.getProtocol(),
							ipRegion.getDirection(), ipRegion.getIsValid(), ipRegion.getOpTime()

					};
					for (int x = 0; x < obj.length; x++) {
						if (x == 14) {
							ps.setTimestamp(x + 1, utileDate2TimeStamp(ipRegion.getOpTime()));
						} else {
							ps.setObject(x + 1, obj[x]);
						}
					}
					ps.addBatch();
					// if (count % batchSize == 0) {
					// ps.executeBatch();
					// ps.clearBatch();
					// }
				}
				ps.executeBatch();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					conn.close();
					conn = null;
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		// ps.close();
		// conn.commit();
	}

	public static void saveNumRegion(String name, List<NumRegion> numRegionList, Connection conn, int batchSize) {
		if (null != conn) {
			try {
				StringBuffer sb = new StringBuffer();
				sb.append("insert into ");
				sb.append(name);
				sb.append(
						" (REGION_ID,GROUP_ID,LOW_BOUNDARY,UP_BOUNDARY,IS_VALID,OP_TIME,LAST_UPDATE) values(?, ?, ?, ?, ?, ?, sysdate)");
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				int count = 0;
				for (NumRegion numRegion : numRegionList) {
					count++;
					Object[] obj = new Object[] { numRegion.getRegionId(), numRegion.getGroupId(),
							numRegion.getLowBoundary(), numRegion.getUpBoundary(), numRegion.getIsValid(),
							numRegion.getOpTime() };
					for (int x = 0; x < obj.length; x++) {
						if (x == 5) {
							ps.setTimestamp(x + 1, utileDate2TimeStamp(numRegion.getOpTime()));
						} else {
							ps.setObject(x + 1, obj[x]);
						}
					}
					ps.addBatch();
					// if (count % batchSize == 0) {
					// ps.executeBatch();
					// ps.clearBatch();
					// }
				}
				ps.executeBatch();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					conn.close();
					conn = null;
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		// ps.close();
		// conn.commit();
	}

	public static void saveStrongStrRegion(String name, List<StrRegion> strRegionList, Connection conn, int batchSize) {
		if (null != conn) {
			try {
				StringBuffer sb = new StringBuffer();
				sb.append("insert into ");
				sb.append(name);
				sb.append(
						"(REGION_ID, GROUP_ID, DISTRICT, KEYWORDS, EXPR_TYPE, MATCH_METHOD , IS_HEXBIN, IS_VALID, OP_TIME,LAST_UPDATE ) values(?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate) ");
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(sb.toString());

				int count = 0;
				for (StrRegion strRegion : strRegionList) {
					count++;
					Object[] obj = new Object[] { strRegion.getRegionId(), strRegion.getGroupId(),
							strRegion.getDistrict(), strRegion.getKeywords(), strRegion.getExprType(),
							strRegion.getMatchMethod(), strRegion.getIsHexbin(), strRegion.getIsValid(),
							strRegion.getOpTime() };
					for (int x = 0; x < obj.length; x++) {
						if (x == 8) {
							ps.setTimestamp(x + 1, utileDate2TimeStamp(strRegion.getOpTime()));
						} else {
							ps.setObject(x + 1, obj[x]);
						}
					}
					ps.addBatch();
					// if (count % batchSize == 0) {
					// ps.executeBatch();
					// ps.clearBatch();
					// }
				}
				ps.executeBatch();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					conn.close();
					conn = null;
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		// ps.close();
		// conn.commit();
	}

	public static void saveStrRegion(String name, List<StrRegion> strRegionList, Connection conn, int batchSize) {
		if (null != conn) {
			try {
				StringBuffer sb = new StringBuffer();
				sb.append("insert into ");
				sb.append(name);
				sb.append(
						"(REGION_ID, GROUP_ID, KEYWORDS, EXPR_TYPE, MATCH_METHOD , IS_HEXBIN, IS_VALID, OP_TIME,LAST_UPDATE ) values( ?, ?, ?, ?, ?, ?, ?, ?, sysdate) ");
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(sb.toString());

				int count = 0;
				for (StrRegion strRegion : strRegionList) {
					count++;
					Object[] obj = new Object[] { strRegion.getRegionId(), strRegion.getGroupId(),
							strRegion.getKeywords(), strRegion.getExprType(), strRegion.getMatchMethod(),
							strRegion.getIsHexbin(), strRegion.getIsValid(), strRegion.getOpTime() };
					for (int x = 0; x < obj.length; x++) {
						if (x == 7) {
							ps.setTimestamp(x + 1, utileDate2TimeStamp(strRegion.getOpTime()));
						} else {
							ps.setObject(x + 1, obj[x]);
						}
					}
					ps.addBatch();
					// if (count % batchSize == 0) {
					// ps.executeBatch();
					// ps.clearBatch();
					// }
				}
				ps.executeBatch();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					conn.close();
					conn = null;
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		// ps.close();
		// conn.commit();
	}

	@Override
	public void run() {
		synchronized (conn) {
			try {
				if (null != compileList && compileList.size() > 0) {
					saveCompile(compileList, conn, batchSize);
				} else if (null != groupList && groupList.size() > 0) {
					saveGroup(groupList, conn, batchSize);
				} else if (null != ipRegionList && ipRegionList.size() > 0) {
					saveIPRegion(tableName, ipRegionList, conn, batchSize);
				} else if (null != strRegionList && strRegionList.size() > 0) {
					if (null != isStrongStr && isStrongStr) {
						saveStrongStrRegion(tableName, strRegionList, conn, batchSize);
					} else {
						saveStrRegion(tableName, strRegionList, conn, batchSize);
					}
				}else if (null != numRegionList && numRegionList.size() > 0) {
					saveNumRegion(tableName, numRegionList,  conn, batchSize);
				}
				latch.countDown();
				System.out.println("latchCount=======================" + latch.getCount());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

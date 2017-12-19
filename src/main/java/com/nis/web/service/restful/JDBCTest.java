package com.nis.web.service.restful;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import com.nis.domain.restful.ConfigCompile;
import com.nis.domain.restful.ConfigGroupRelation;
import com.nis.domain.restful.IpRegion;
import com.nis.domain.restful.NumRegion;
import com.nis.domain.restful.StrRegion;

/**
 * jdbc测试批量插入
 * 
 * @author RenKaiGe-Office
 *
 */
public class JDBCTest {
	public static java.sql.Timestamp utileDate2TimeStamp(java.util.Date udate) {
		java.sql.Timestamp sqlDate = null;
		long t = udate.getTime();
		sqlDate = new java.sql.Timestamp(t);
		return sqlDate;

	}

	public static void saveCompile(List<ConfigCompile> compileList, Connection conn, int batchSize) throws Exception {
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
					compile.getGuaranteeId(), compile.getAffAirId(), compile.getTopIcId(), compile.getDoBlackList(),
					compile.getDoLog(), compile.getEffectiveRange(), compile.getActiveSys(), compile.getConfigPercent(),
					compile.getConfigOption(), compile.getStartTime(), compile.getEndTime(), compile.getUserRegion(),
					compile.getIsValid(), compile.getGroupNum(), compile.getFatherCfgId(), compile.getOpTime() };

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
			if (count % batchSize == 0) {
				ps.executeBatch();
				////conn.commit();
			}
		}
		ps.executeBatch();
		//conn.commit();
	}

	public static void saveGroup(List<ConfigGroupRelation> groupList, Connection conn, int batchSize) throws Exception {
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
			if (count % batchSize == 0) {
				ps.executeBatch();
				//conn.commit();
			}
		}
		ps.executeBatch();
		//conn.commit();
	}

	public static void saveIPRegion(String name, List<IpRegion> ipRegionList, Connection conn, int batchSize)
			throws Exception {
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
					ipRegion.getSrcIp(), ipRegion.getMaskSrcIp(), ipRegion.getSrcPort(), ipRegion.getMaskSrcPort(),
					ipRegion.getDstIp(), ipRegion.getMaskDstIp(), ipRegion.getDstPort(), ipRegion.getMaskDstPort(),
					ipRegion.getProtocol(), ipRegion.getDirection(), ipRegion.getIsValid(), ipRegion.getOpTime()

			};
			for (int x = 0; x < obj.length; x++) {
				if (x == 14) {
					ps.setTimestamp(x + 1, utileDate2TimeStamp(ipRegion.getOpTime()));
				} else {
					ps.setObject(x + 1, obj[x]);
				}
			}
			ps.addBatch();
			if (count % batchSize == 0) {
				ps.executeBatch();
				//conn.commit();
			}
		}
		ps.executeBatch();
		//conn.commit();
	}

	public static void saveNumRegion(String name, List<NumRegion> numRegionList, Connection conn, int batchSize)
			throws Exception {
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
			Object[] obj = new Object[] { numRegion.getRegionId(), numRegion.getGroupId(), numRegion.getLowBoundary(),
					numRegion.getUpBoundary(), numRegion.getIsValid(), numRegion.getOpTime() };
			for (int x = 0; x < obj.length; x++) {
				if (x == 5) {
					ps.setTimestamp(x + 1, utileDate2TimeStamp(numRegion.getOpTime()));
				} else {
					ps.setObject(x + 1, obj[x]);
				}
			}
			ps.addBatch();
			if (count % batchSize == 0) {
				ps.executeBatch();
				//conn.commit();
			}
		}
		ps.executeBatch();
		//conn.commit();
	}

	public static void saveStrongStrRegion(String name, List<StrRegion> strRegionList, Connection conn, int batchSize)
			throws Exception {
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
			Object[] obj = new Object[] { strRegion.getRegionId(), strRegion.getGroupId(), strRegion.getDistrict(),
					strRegion.getKeywords(), strRegion.getExprType(), strRegion.getMatchMethod(),
					strRegion.getIsHexbin(), strRegion.getIsValid(), strRegion.getOpTime() };
			for (int x = 0; x < obj.length; x++) {
				if (x == 8) {
					ps.setTimestamp(x + 1, utileDate2TimeStamp(strRegion.getOpTime()));
				} else {
					ps.setObject(x + 1, obj[x]);
				}
			}
			ps.addBatch();
			if (count % batchSize == 0) {
				ps.executeBatch();
				//conn.commit();
			}
		}
		ps.executeBatch();
		//conn.commit();
	}

	public static void saveStrRegion(String name, List<StrRegion> strRegionList, Connection conn, int batchSize)
			throws Exception {
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
			Object[] obj = new Object[] { strRegion.getRegionId(), strRegion.getGroupId(), strRegion.getKeywords(),
					strRegion.getExprType(), strRegion.getMatchMethod(), strRegion.getIsHexbin(),
					strRegion.getIsValid(), strRegion.getOpTime() };
			for (int x = 0; x < obj.length; x++) {
				if (x == 7) {
					ps.setTimestamp(x + 1, utileDate2TimeStamp(strRegion.getOpTime()));
				} else {
					ps.setObject(x + 1, obj[x]);
				}
			}
			ps.addBatch();
			if (count % batchSize == 0) {
				ps.executeBatch();
				//conn.commit();
			}
		}
		ps.executeBatch();
		//conn.commit();
	}
}

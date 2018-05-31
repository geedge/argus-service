package com.nis.web.service.restful;

import java.sql.Connection;
import java.sql.Types;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.sql.DataSource;
import org.springframework.jdbc.object.BatchSqlUpdate;
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
public class SpringJDBCThreadTest2 implements Runnable {
	private CountDownLatch latch;
	private Boolean isStrongStr;
	private String tableName;
	private int batchSize;
	private BatchSqlUpdate bsu;
	private List<ConfigCompile> compileList;
	private List<ConfigGroupRelation> groupList;
	private List<IpRegion> ipRegionList;
	private List<NumRegion> numRegionList;
	private List<StrRegion> strRegionList;

	public SpringJDBCThreadTest2() {
		super();
	}

	public SpringJDBCThreadTest2(int batchSize, BatchSqlUpdate bsu, CountDownLatch latch) {
		super();
		this.batchSize = batchSize;
		this.bsu = bsu;
		this.latch = latch;
	}

	public SpringJDBCThreadTest2(String tableName, int batchSize, BatchSqlUpdate bsu, CountDownLatch latch) {
		super();
		this.tableName = tableName;
		this.batchSize = batchSize;
		this.bsu = bsu;
		this.latch = latch;
	}

	public SpringJDBCThreadTest2(boolean isStrongStr, String tableName, int batchSize, BatchSqlUpdate bsu,
			CountDownLatch latch) {
		super();
		this.isStrongStr = isStrongStr;
		this.tableName = tableName;
		this.batchSize = batchSize;
		this.bsu = bsu;
		this.latch = latch;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	public BatchSqlUpdate getBsu() {
		return bsu;
	}

	public void setBsu(BatchSqlUpdate bsu) {
		this.bsu = bsu;
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

	public static void saveCompile(List<ConfigCompile> compileList, BatchSqlUpdate bsu, int batchSize) {
		StringBuffer sb = new StringBuffer();
		sb.append(
				"insert into CONFIG_COMPILE (COMPILE_ID ,SERVICE ,ACTION , CONT_TYPE, ATTR_TYPE, CONT_LABEL, Task_id, Guarantee_ID, AFFAIR_ID, TOPIC_ID, DO_BLACKLIST ,DO_LOG ,EFFECTIVE_RANGE , ACTIVE_SYS, CONFIG_PERCENT ,CONFIG_OPTION ,START_TIME ,END_TIME , USER_REGION, IS_VALID,GROUP_NUM,FATHER_CFG_ID ,OP_TIME,LAST_UPDATE ) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate)");

		// BatchSqlUpdate bsu = new BatchSqlUpdate(ds, sb.toString());
		bsu.setSql(sb.toString());
		bsu.setBatchSize(batchSize);
		bsu.setTypes(new int[] { Types.BIGINT, Types.BIGINT, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.VARCHAR,
				Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.VARCHAR,
				Types.INTEGER, Types.BIGINT, Types.INTEGER, Types.DATE, Types.DATE, Types.VARCHAR, Types.INTEGER,
				Types.INTEGER, Types.BIGINT, Types.DATE });
		for (ConfigCompile compile : compileList) {
			bsu.update(new Object[] { compile.getCompileId(), compile.getService(), compile.getAction(),
					compile.getContType(), compile.getAttrType(), compile.getContLabel(), compile.getTaskId(),
					compile.getGuaranteeId(), compile.getAffairId(), compile.getTopicId(), compile.getDoBlacklist(),
					compile.getDoLog(), compile.getEffectiveRange(), compile.getActiveSys(), compile.getConfigPercent(),
					compile.getConfigOption(), compile.getStartTime(), compile.getEndTime(), compile.getUserRegion(),
					compile.getIsValid(), compile.getGroupNum(), compile.getFatherCfgId(), compile.getOpTime() });
		}
		bsu.flush();
	}

	public static void saveGroup(List<ConfigGroupRelation> groupList, BatchSqlUpdate bsu, int batchSize) {
		StringBuffer sb = new StringBuffer();
		sb.append(
				"insert into CONFIG_GROUP (ID,GROUP_ID, COMPILE_ID, IS_VALID, LAST_UPDATE, OP_TIME ) values ( seq_config_group.nextval, ?, ?, ?, sysdate, ?) ");
		// BatchSqlUpdate bsu = new BatchSqlUpdate(ds, sb.toString());
		bsu.setSql(sb.toString());
		bsu.setBatchSize(batchSize);
		bsu.setTypes(new int[] { Types.BIGINT, Types.BIGINT, Types.INTEGER, Types.DATE });
		for (ConfigGroupRelation group : groupList) {
			bsu.update(
					new Object[] { group.getGroupId(), group.getCompileId(), group.getIsValid(), group.getOpTime() });
		}
		bsu.flush();

	}

	public static void saveIPRegion(String name, List<IpRegion> ipRegionList, BatchSqlUpdate bsu, int batchSize) {
		StringBuffer sb = new StringBuffer();
		sb.append("insert into ");
		sb.append(name);
		sb.append(
				"(REGION_ID, GROUP_ID, ADDR_TYPE, SRC_IP, MASK_SRC_IP, SRC_PORT, MASK_SRC_PORT, DST_IP, MASK_DST_IP, DST_PORT, MASK_DST_PORT, PROTOCOL, DIRECTION, IS_VALID, OP_TIME,LAST_UPDATE ) values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate) ");
		// BatchSqlUpdate bsu = new BatchSqlUpdate(ds, sb.toString());
		bsu.setSql(sb.toString());
		bsu.setBatchSize(batchSize);
		bsu.setTypes(new int[] { Types.BIGINT, Types.BIGINT, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER,
				Types.INTEGER, Types.DATE });
		for (IpRegion ipRegion : ipRegionList) {
			bsu.update(new Object[] { ipRegion.getRegionId(), ipRegion.getGroupId(), ipRegion.getAddrType(),
					ipRegion.getSrcIp(), ipRegion.getMaskSrcIp(), ipRegion.getSrcPort(), ipRegion.getMaskSrcPort(),
					ipRegion.getDstIp(), ipRegion.getMaskDstIp(), ipRegion.getDstPort(), ipRegion.getMaskDstPort(),
					ipRegion.getProtocol(), ipRegion.getDirection(), ipRegion.getIsValid(), ipRegion.getOpTime()

			});
		}
		bsu.flush();
	}

	public static void saveNumRegion(String name, List<NumRegion> numRegionList, BatchSqlUpdate bsu, int batchSize) {
		StringBuffer sb = new StringBuffer();
		sb.append("insert into ");
		sb.append(name);
		sb.append(
				" (REGION_ID,GROUP_ID,LOW_BOUNDARY,UP_BOUNDARY,IS_VALID,OP_TIME,LAST_UPDATE) values(?, ?, ?, ?, ?, ?, sysdate)");
		// BatchSqlUpdate bsu = new BatchSqlUpdate(ds, sb.toString());
		bsu.setSql(sb.toString());
		bsu.setBatchSize(batchSize);
		bsu.setTypes(new int[] { Types.BIGINT, Types.BIGINT, Types.BIGINT, Types.BIGINT, Types.INTEGER, Types.DATE });
		for (NumRegion numRegion : numRegionList) {
			bsu.update(new Object[] { numRegion.getRegionId(), numRegion.getGroupId(), numRegion.getLowBoundary(),
					numRegion.getUpBoundary(), numRegion.getIsValid(), numRegion.getOpTime() });
		}
		bsu.flush();
	}

	public static void saveStrongStrRegion(String name, List<StrRegion> strRegionList, BatchSqlUpdate bsu,
			int batchSize) {
		StringBuffer sb = new StringBuffer();
		sb.append("insert into ");
		sb.append(name);
		sb.append(
				"(REGION_ID, GROUP_ID, DISTRICT, KEYWORDS, EXPR_TYPE, MATCH_METHOD , IS_HEXBIN, IS_VALID, OP_TIME,LAST_UPDATE ) values(?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate) ");
		// BatchSqlUpdate bsu = new BatchSqlUpdate(ds, sb.toString());
		bsu.setSql(sb.toString());
		bsu.setBatchSize(batchSize);
		bsu.setTypes(new int[] { Types.BIGINT, Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER,
				Types.INTEGER, Types.INTEGER, Types.DATE });
		for (StrRegion strRegion : strRegionList) {
			bsu.update(new Object[] { strRegion.getRegionId(), strRegion.getGroupId(), strRegion.getDistrict(),
					strRegion.getKeywords(), strRegion.getExprType(), strRegion.getMatchMethod(),
					strRegion.getIsHexbin(), strRegion.getIsValid(), strRegion.getOpTime() });
		}
		bsu.flush();
	}

	public static void saveStrRegion(String name, List<StrRegion> strRegionList, BatchSqlUpdate bsu, int batchSize) {
		StringBuffer sb = new StringBuffer();
		sb.append("insert into ");
		sb.append(name);
		sb.append(
				"(REGION_ID, GROUP_ID, KEYWORDS, EXPR_TYPE, MATCH_METHOD , IS_HEXBIN, IS_VALID, OP_TIME,LAST_UPDATE ) values( ?, ?, ?, ?, ?, ?, ?, ?, sysdate) ");

		// BatchSqlUpdate bsu = new BatchSqlUpdate(ds, sb.toString());
		bsu.setSql(sb.toString());
		bsu.setBatchSize(batchSize);
		bsu.setTypes(new int[] { Types.BIGINT, Types.BIGINT, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER,
				Types.INTEGER, Types.DATE });

		for (StrRegion strRegion : strRegionList) {
			bsu.update(new Object[] { strRegion.getRegionId(), strRegion.getGroupId(), strRegion.getKeywords(),
					strRegion.getExprType(), strRegion.getMatchMethod(), strRegion.getIsHexbin(),
					strRegion.getIsValid(), strRegion.getOpTime() });
		}
		bsu.flush();
	}

	@Override
	public void run() {
		try {
			if (null != compileList && compileList.size() > 0) {
				saveCompile(compileList, bsu, batchSize);
			} else if (null != groupList && groupList.size() > 0) {
				saveGroup(groupList, bsu, batchSize);
			} else if (null != ipRegionList && ipRegionList.size() > 0) {
				saveIPRegion(tableName, ipRegionList, bsu, batchSize);
			} else if (null != strRegionList && strRegionList.size() > 0) {
				if (null != isStrongStr && isStrongStr) {
					saveStrongStrRegion(tableName, strRegionList, bsu, batchSize);
				} else {
					saveStrRegion(tableName, strRegionList, bsu, batchSize);
				}
			} else if (null != numRegionList && numRegionList.size() > 0) {
				saveNumRegion(tableName, numRegionList, bsu, batchSize);
			}
			latch.countDown();
			System.out.println("SpringJDBC--latchCount=======================" + latch.getCount());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

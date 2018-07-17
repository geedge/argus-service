package com.nis.web.service.restful;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nis.domain.Page;
import com.nis.domain.restful.CommonSourceFieldCfg;
import com.nis.domain.restful.ConfigCompile;
import com.nis.domain.restful.ConfigCompileTest;
import com.nis.domain.restful.ConfigGroupRelation;
import com.nis.domain.restful.DigestRegion;
import com.nis.domain.restful.IpRegion;
import com.nis.domain.restful.MaatConfig;
import com.nis.domain.restful.NumRegion;
import com.nis.domain.restful.StrRegion;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.BasicProvingUtil;
import com.nis.util.CamelUnderlineUtil;
import com.nis.util.CompileVal;
import com.nis.util.Configurations;
import com.nis.util.Constants;
import com.nis.util.JsonMapper;
import com.nis.util.OracleErrorCodeUtil;
import com.nis.util.ReadCommSourceXmlUtil;
import com.nis.util.ServiceAndRDBIndexReal;
import com.nis.util.StringUtil;
import com.nis.util.StringUtils;
import com.nis.web.dao.ConfigCompileDao;
import com.nis.web.dao.ConfigGroupRelationDao;
import com.nis.web.dao.IpRegionDao;
import com.nis.web.dao.NumRegionDao;
import com.nis.web.dao.StrRegionDao;
import com.nis.web.service.BaseService;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.SpringContextHolder;

/**
 * @ClassName: ControlService
 * @Description: TODO(编译配置持久化)
 * @author (zx)
 * @date 2016年9月6日 下午1:08:12
 * @version V1.0
 */
@Service
public class ConfigSourcesService extends BaseService {
	/**
	 * 存放多线程异常信息
	 */
	private static List<Exception> msgList = new ArrayList<Exception>();

	public static List<Exception> getMsgList() {
		return msgList;
	}

	public static void setMsgList(List<Exception> msgList) {
		ConfigSourcesService.msgList = msgList;
	}

	protected final Logger logger1 = Logger.getLogger(this.getClass());
	private Map<Integer, Map<String, String>> map;
	@Autowired
	private ConfigCompileDao configCompileDao;

	@Autowired
	private IpRegionDao ipRegionDao;
	@Autowired
	private NumRegionDao numRegionDao;

	@Autowired
	private ConfigGroupRelationDao configGroupRelationDao;
	@Autowired
	private StrRegionDao strRegionDao;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	ConfigRedisService configRedisService;

	public static Map<Integer, Map<String, String>> getTableRelation() {
		Map<Integer, Map<String, String>> tableMap = new HashMap<Integer, Map<String, String>>();
		Map<String, String> typeMap = new HashMap<String, String>();
		typeMap.put("MM_AV_IP", "ip");
		tableMap.put(80, typeMap);
		tableMap.put(144, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("MM_AV_URL", "str");
		tableMap.put(81, typeMap);
		tableMap.put(145, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("MM_PIC_IP", "ip");
		tableMap.put(82, typeMap);
		tableMap.put(146, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("MM_PIC_URL", "str");
		tableMap.put(83, typeMap);
		tableMap.put(147, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("MM_VOIP_IP", "ip");
		tableMap.put(84, typeMap);
		tableMap.put(148, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("MM_VOIP_ACCOUNT", "str");
		tableMap.put(85, typeMap);
		tableMap.put(149, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("REUSE_IP", "ip");
		typeMap.put("HID_CONTENT", "ip");
		typeMap.put("GSB_PZ_IP", "ip");
		typeMap.put("FAKE_REGION_EXTEND", "str");
		typeMap.put("KEYWORD_RANDOM_REGION", "str");
		typeMap.put("CTRL_URL_REGION", "str");
		typeMap.put("CTRL_VOIP_BLACKACCOUNT_REGION", "str");
		typeMap.put("CTRL_MEDIA_FILE_FP", "digest");
		tableMap.put(0, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("DF_IP_PORT", "ip");
		typeMap.put("GSB_PZ_IP", "ip");// IP复用
		typeMap.put("CTRL_MEDIA_FILE_FP", "digest");
		tableMap.put(1, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("FX_IP_PORT", "ip");
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("GSB_PZ_IP", "ip");// 网站伪造
		tableMap.put(2, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("CTRL_VOIP_BLACKACCOUNT_REGION", "str");
		tableMap.put(118, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("UNIVERSAL_IP", "ip");
		typeMap.put("DF_HTTP_REQ_HDR", "str");
		typeMap.put("DF_HTTP_URL", "str");
		typeMap.put("GSB_PZ_IP", "ip");// 内容篡改
		tableMap.put(3, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("UNIVERSAL_IP", "ip");
		typeMap.put("DF_HTTP_RES_HDR", "str");
		typeMap.put("GSB_PZ_IP", "ip");// 随机丢包
		typeMap.put("CTRL_URL_REGION", "str");
		tableMap.put(4, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("UNIVERSAL_IP", "ip");
		typeMap.put("DF_HTTP_REQ_BODY", "str");
		typeMap.put("DF_HTTP_RES_BODY", "str");
		tableMap.put(5, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("UNIVERSAL_IP", "ip");
		typeMap.put("DF_MAIL_HDR", "str");
		typeMap.put("DF_MAIL_BODY", "str");
		tableMap.put(13, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("UNIVERSAL_IP", "ip");
		typeMap.put("DF_DNS_REGION", "str");
		tableMap.put(6, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("UNIVERSAL_IP", "ip");
		typeMap.put("DF_FTP_URL", "str");
		tableMap.put(14, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("UNIVERSAL_IP", "ip");
		tableMap.put(7, typeMap);
		tableMap.put(8, typeMap);
		tableMap.put(9, typeMap);
		tableMap.put(10, typeMap);
		tableMap.put(11, typeMap);
		typeMap.put("DF_SSL_REGION", "str");
		tableMap.put(12, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("UNIVERSAL_IP", "ip");
		typeMap.put("FX_HTTP_REQ_HDR", "str");
		typeMap.put("FX_HTTP_URL", "str");

		typeMap.put("WHITE_LIST_IP", "ip");
		tableMap.put(15, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("UNIVERSAL_IP", "ip");
		typeMap.put("FX_DNS_REGION", "str");
		tableMap.put(16, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("DJ_IP_PORT", "ip");
		typeMap.put("DJ_IP_PKT_BIN", "str");
		tableMap.put(48, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("UNIVERSAL_IP", "ip");
		typeMap.put("DJ_HTTP_REQ_HDR", "str");
		typeMap.put("DJ_HTTP_URL", "str");
		tableMap.put(49, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("UNIVERSAL_IP", "ip");
		typeMap.put("DJ_HTTP_RES_HDR", "str");
		tableMap.put(50, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("UNIVERSAL_IP", "ip");
		typeMap.put("DJ_HTTP_REQ_BODY", "str");
		typeMap.put("DJ_HTTP_RES_BODY", "str");
		tableMap.put(51, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("UNIVERSAL_IP", "ip");
		typeMap.put("DJ_MAIL_HDR", "str");
		typeMap.put("DJ_MAIL_BODY", "str");
		tableMap.put(60, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("UNIVERSAL_IP", "ip");
		typeMap.put("DJ_DNS_RES_REGION", "str");
		typeMap.put("DJ_DNS_REQ_REGION", "str");
		tableMap.put(52, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("UNIVERSAL_IP", "ip");
		typeMap.put("DJ_FTP_URL", "str");
		tableMap.put(53, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("UNIVERSAL_IP", "ip");
		tableMap.put(54, typeMap);
		tableMap.put(55, typeMap);
		tableMap.put(56, typeMap);
		tableMap.put(57, typeMap);
		tableMap.put(58, typeMap);
		typeMap.put("DJ_SSL_REGION", "str");
		tableMap.put(59, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("UNIVERSAL_IP", "ip");
		typeMap.put("DF_L2TP_URL", "str");
		tableMap.put(32, typeMap);
		typeMap = new HashMap<String, String>();
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("UNIVERSAL_IP", "ip");
		typeMap.put("DF_PPTP_URL", "str");
		tableMap.put(33, typeMap);

		// 20161109新增业务类型
		typeMap = new HashMap<String, String>();
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("UNIVERSAL_IP", "ip");
		typeMap.put("DF_HTTP_REQ_HDR", "str");
		typeMap.put("DF_HTTP_REQ_BODY", "str");
		typeMap.put("DF_HTTP_URL", "str");
		tableMap.put(17, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("UNIVERSAL_IP", "ip");
		typeMap.put("DF_HTTP_RES_HDR", "str");
		typeMap.put("DF_HTTP_RES_BODY", "str");
		typeMap.put("DF_HTTP_URL", "str");
		tableMap.put(18, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("UNIVERSAL_IP", "ip");
		typeMap.put("DJ_HTTP_REQ_HDR", "str");
		typeMap.put("DJ_HTTP_REQ_BODY", "str");
		typeMap.put("DJ_HTTP_URL", "str");
		tableMap.put(61, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("UNIVERSAL_IP", "ip");
		typeMap.put("DJ_HTTP_RES_HDR", "str");
		typeMap.put("DJ_HTTP_RES_BODY", "str");
		typeMap.put("DJ_HTTP_URL", "str");
		tableMap.put(62, typeMap);

		typeMap = new HashMap<String, String>();
		typeMap.put("UNIVERSAL_PROTO_TYPE", "num");
		typeMap.put("UNIVERSAL_IP", "ip");
		tableMap.put(35, typeMap);
		tableMap.put(36, typeMap);
		tableMap.put(37, typeMap);
		return tableMap;
	}

	/**
	 * 批量(mybatis方式)修改配置业务逻辑-->1:先进行基础配置验证传过来的json串是否符合格式(配置分组关系前端不能设置为无效,
	 * 程序会根据传过来的编译配置和域配置等信息自动设置为无效). 符合要求调用updateConfigCompile方法继续验证
	 * 
	 * @param thread
	 *            保存异常信息入库对象
	 * @param start
	 *            进入controller的时间
	 * @param compileList
	 *            编译配置集合
	 * @return
	 * @throws Exception
	 */
	public void updateConfigSource(SaveRequestLogThread thread, long start,
			List<ConfigCompile> configSource, Date opTime, StringBuffer sb) {
		SqlSessionFactory sqlSessionFactory = SpringContextHolder
				.getBean(SqlSessionFactory.class);
		SqlSession batchSqlSession = null;
		try {
			batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH,
					false);
			if (null != configSource && configSource.size() > 0) {
				for (ConfigCompile config : configSource) {
					String msg = CompileVal.compileIsOk(config, true, sb);
					if (!"ok".equals(msg)) {
						thread.setExceptionInfo(msg + sb.toString());
						throw new RestServiceException(thread,
								System.currentTimeMillis() - start, msg
										+ sb.toString(),
										CompileVal.getBusinessCode());
					}
					updateConfigCompile(thread, start, config, batchSqlSession,
							opTime, sb);
				}

			} else {
				thread.setExceptionInfo("编译配置不能为空" + sb.toString());
				throw new RestServiceException(thread,
						System.currentTimeMillis() - start, "编译配置不能为空"
								+ sb.toString(),
						CompileVal.getBusinessCode());
			}
			logger1.info("数据保存到数据库提交事务开始");
			batchSqlSession.commit();
			logger1.info("数据保存到数据库成功");
		} catch (RestServiceException e) {
			logger1.error(e);
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause()
					+ sb.toString());
			throw new RestServiceException(thread, System.currentTimeMillis()
					- start, e.getMessage() + sb.toString(), e.getErrorCode());
		} catch (Exception e) {

			logger1.error(e);
			String errorCode = OracleErrorCodeUtil.getOraCode(e);
			if (!StringUtils.isEmpty(errorCode)) {
				logger1.error("数据更新发生异常！");
				thread.setExceptionInfo(e.getMessage() + " " + e.getCause()
						+ sb.toString());
				OracleErrorCodeUtil.throwExceptionInfo(thread,
						System.currentTimeMillis() - start, errorCode);
			} else {
				thread.setExceptionInfo("数据更新发生异常" + sb.toString());
				throw new RestServiceException(thread,
						System.currentTimeMillis() - start, "数据更新发生异常"
								+ sb.toString(),
						RestBusinessCode.unknow_error.getValue());
			}
		} finally {
			if (batchSqlSession != null) {
				batchSqlSession.close();
			}
		}

	}

	/**
	 * 批量(mybatis方式)修改配置业务逻辑-->2:判断配置在数据库中是否已经是无效了如果是无效则提示不能修改无效的配置为有效(
	 * 因为如果数据库中的编译配置与配置分组关系以及域配置均是无效
	 * ,传过来的数据是编译配置和配置分组关系是有效,域配置是无效,这样会将数据库中无效的编译配置置为有效,这是不允许的)
	 * 3:如果编译配置置为无效将所有的配置分组关系置为无效,不修改域配置状态(分组复用考虑,域可能被其他的组引用)
	 * 4:如果编译配置为有效,域配置为无效,则先获取该编译配置下各个配置分组关系下的所有有效的域配置信息和所有无效的域配置信息,
	 * 如果数据库中有效的域配置信息和前端传过来置为无效的的域配置信息个数,id都相等,则将对应域配置,配置分组关系,编译配置均置为无效
	 * 
	 * 20161124讨论暂时先去掉2步骤,因为界面目前传过来的数据编译配置,配置分组关系,域配置均是无效,所以就不执行第2步骤了影响性能(
	 * 方法内注释的代码,如果以后业务需要调整打开下面代码即可)
	 * 
	 * @param thread
	 *            保存异常信息入库对象
	 * @param start
	 *            进入controller的时间
	 * @param compileList
	 *            编译配置集合
	 * @return
	 * @throws Exception
	 */

	public void updateConfigCompile(SaveRequestLogThread thread, long start,
			ConfigCompile config, SqlSession batchSqlSession, Date opTime,
			StringBuffer sb) throws Exception {
		List<IpRegion> ipRegionList = config.getIpRegionList();
		List<StrRegion> strRegionList = config.getStrRegionList();
		List<NumRegion> numRegionList = config.getNumRegionList();
		List<ConfigGroupRelation> groupRelationList = config
				.getGroupRelationList();
		if (config.getIsValid() == 1) {
			if (groupRelationList == null || groupRelationList.size() == 0) {
				thread.setExceptionInfo("配置分组数量不能为空" + sb.toString());
				throw new RestServiceException(thread,
						System.currentTimeMillis() - start, "配置分组数量不能为空"
								+ sb.toString(),
						RestBusinessCode.config_integrity_error.getValue());
			}
			if ((ipRegionList == null || ipRegionList.size() == 0)
					&& (strRegionList == null || strRegionList.size() == 0)
					&& (numRegionList == null || numRegionList.size() == 0)) {
				thread.setExceptionInfo("域配置数量不能全部为空" + sb.toString());
				throw new RestServiceException(thread,
						System.currentTimeMillis() - start, "域配置数量不能全部为空"
								+ sb.toString(),
						RestBusinessCode.config_integrity_error.getValue());
			}
		}

		// ConfigCompile configCompile =
		// configCompileDao.queryConfigCompileById(config.getCompileId());//
		// 先判断该条编译配置数据库中状态是否为无效,如果无效则提示用户不能将无效状态改为有效状态
		// if (configCompile.getIsValid() == 1) {// 数据库中编译配置为有效状态才可以进行修改操作
		/**
		 * 1:编译配置置为无效,需要将其下所有配置分组置为无效,如果数据中将域配置置为了无效则将域配置置为无效,否则不修改域配置是否生效标志
		 * 2:编译配置置为有效,不修改编译配置状态(编译配置不能从无效变有效,只能从有效变无效)仅修改编译配置其他属性,
		 */

		List<ConfigGroupRelation> queryCompileGroupByPID = configGroupRelationDao
				.queryCompileGroupByPID(config.getCompileId());

		if (null == queryCompileGroupByPID
				|| queryCompileGroupByPID.size() == 0) {
			throw new RestServiceException(thread, System.currentTimeMillis()
					- start, "配置id为" + config.getCompileId()
					+ "的配置在数据库中找不到对应的配置分组关系,请检查json串是否正确" + sb.toString(),
					RestBusinessCode.config_integrity_error.getValue());
		}
		if (config.getIsValid() == 0) {// 编译配置修改为无效,需要将所有的配置分组置为无效
			if (null != queryCompileGroupByPID
					&& queryCompileGroupByPID.size() > 0) {
				for (ConfigGroupRelation configGroupRelation : queryCompileGroupByPID) {
					configGroupRelation.setIsValid(0);
					configGroupRelation.setOpTime(opTime);

					if (Configurations.getStringProperty("isCommit", "false")
							.equals("true")) {
						batchSqlSession.getMapper(ConfigGroupRelationDao.class)
								.updateConfigGroupRelation(configGroupRelation);
					}
				}
			}
			// config.setLastUpdate(new Date());
			if (Configurations.getStringProperty("isCommit", "false").equals(
					"true")) {
				batchSqlSession.getMapper(ConfigCompileDao.class)
						.updateConfigCompile(config);
			}

		} else {
			regionUpdate(thread, start, config, queryCompileGroupByPID,
					batchSqlSession, opTime, sb);
		}

		// } else {// 数据库中编译配置为无效状态不能进行操作
		// throw new RestServiceException(thread, System.currentTimeMillis() -
		// start, "不能修改无效的编译配置信息",
		// RestBusinessCode.missing_args.getValue());
		// }

	}

	/**
	 * 获取编译配置下所有的配置分组关系对应的有效,无效域配置id
	 * 
	 * @param thread
	 *            保存异常信息入库对象
	 * @param start
	 *            进入controller的时间
	 * @param config
	 *            编译配置对象
	 * @param queryCompileGroupByPID
	 *            当前编译下所有的配置分组关系
	 * @return
	 */
	public void regionUpdate(SaveRequestLogThread thread, long start,
			ConfigCompile config,
			List<ConfigGroupRelation> queryCompileGroupByPID,
			SqlSession batchSqlSession, Date opTime, StringBuffer sb)
			throws Exception {
		Map<String, Map<Long, List<Long>>> regionMap = getRegionList(thread,
				System.currentTimeMillis() - start, config,
				queryCompileGroupByPID, batchSqlSession, sb);
		Map<Long, List<Long>> validMap = regionMap.get("validMap");// 获取该编译配置下所有的配置分组关系及组下有效的域配置id
		Map<Long, List<Long>> noValidMap = regionMap.get("noValidMap");// 获取该编译配置下所有的配置分组关系及组下无效的域配置id
		List<IpRegion> ipRegionList = config.getIpRegionList();// 获取编译下所有ip类域配置(要修改的数据)
		List<StrRegion> strRegionList = config.getStrRegionList();// 获取编译下所有字符串类域配置(要修改的数据
		List<NumRegion> numRegionList = config.getNumRegionList();// 获取编译下所有数值类域配置(要修改的数据
		int num = 0;
		for (Long regionId : validMap.keySet()) {// 遍历配置分组
			List<Long> validList = validMap.get(regionId);// 获取配置分组下有效的域配置id
			List<Long> noValidList = noValidMap.get(regionId);// 获取配置分组下无效的域配置id
			int count = 0;
			if (null != ipRegionList && ipRegionList.size() > 0) {
				List<String> tableList = new ArrayList<String>();
				for (IpRegion ipRegion : ipRegionList) {
					if (ipRegion.getGroupId().equals(regionId)) {
						if (null != validList
								&& validList.contains(ipRegion.getRegionId())) {
							if (ipRegion.getIsValid() == 0) {
								ipRegion.setOpTime(opTime);
								ipRegion.setLastUpdate(new Date());
								if (Configurations.getStringProperty(
										"isCommit", "false").equals("true")) {
									batchSqlSession
											.getMapper(IpRegionDao.class)
											.updateIpRegion(ipRegion);
								}
								count++;
								if (!tableList
										.contains(ipRegion.getTableName())) {
									tableList.add(ipRegion.getTableName());
								}
							}
						} else if (null != noValidList
								&& noValidList.contains(ipRegion.getRegionId())) {
							String msg = "不能修改无效的ip类域配置compile为"
									+ config.getCompileId() + "regionid为"
									+ ipRegion.getRegionId() + sb.toString();
							thread.setExceptionInfo(msg);
							throw new RestServiceException(thread,
									System.currentTimeMillis() - start, msg,
									RestBusinessCode.missing_args.getValue());
						} else {
							String msg = "找不到对应的的ip类域配置compile为"
									+ config.getCompileId() + "regionid为"
									+ ipRegion.getRegionId() + sb.toString();
							thread.setExceptionInfo(msg);
							throw new RestServiceException(thread,
									System.currentTimeMillis() - start, msg,
									RestBusinessCode.missing_args.getValue());
						}
					}
				}

			}

			if (null != strRegionList && strRegionList.size() > 0) {
				List<String> tableList = new ArrayList<String>();
				for (StrRegion strRegion : strRegionList) {
					if (strRegion.getGroupId().equals(regionId)) {
						if (null != validList
								&& validList.contains(strRegion.getRegionId())) {
							if (strRegion.getIsValid() == 0) {
								strRegion.setOpTime(opTime);
								strRegion.setLastUpdate(new Date());
								if (Configurations.getStringProperty(
										"isCommit", "false").equals("true")) {
									batchSqlSession.getMapper(
											StrRegionDao.class)
											.updateStrRegion(strRegion);
								}
								count++;
								if (!tableList.contains(strRegion
										.getTableName())) {
									tableList.add(strRegion.getTableName());
								}
							}
						} else if (null != noValidList
								&& noValidList
										.contains(strRegion.getRegionId())) {
							String msg = "不能修改无效的字符串类域配置compile为"
									+ config.getCompileId() + "regionid为"
									+ strRegion.getRegionId() + sb.toString();
							thread.setExceptionInfo(msg);
							throw new RestServiceException(thread,
									System.currentTimeMillis() - start, msg,
									RestBusinessCode.missing_args.getValue());
						} else {
							String msg = "找不到对应的的字符串类域配置compile为"
									+ config.getCompileId() + "regionid为"
									+ strRegion.getRegionId() + sb.toString();
							thread.setExceptionInfo(msg);
							throw new RestServiceException(thread,
									System.currentTimeMillis() - start, msg,
									RestBusinessCode.missing_args.getValue());
						}
					}
				}

			}
			if (null != numRegionList && numRegionList.size() > 0) {
				List<String> tableList = new ArrayList<String>();
				for (NumRegion numRegion : numRegionList) {
					if (numRegion.getGroupId().equals(regionId)) {
						if (null != validList
								&& validList.contains(numRegion.getRegionId())) {
							if (numRegion.getIsValid() == 0) {
								numRegion.setOpTime(opTime);
								numRegion.setLastUpdate(new Date());
								if (Configurations.getStringProperty(
										"isCommit", "false").equals("true")) {
									batchSqlSession.getMapper(
											NumRegionDao.class)
											.updateNumRegion(numRegion);
								}
								count++;
								if (!tableList.contains(numRegion
										.getTableName())) {
									tableList.add(numRegion.getTableName());
								}

							}
						} else if (null != noValidList
								&& noValidList
										.contains(numRegion.getRegionId())) {
							String msg = "不能修改无效的数值类域配置compile为"
									+ config.getCompileId() + "regionid为"
									+ numRegion.getRegionId() + sb.toString();
							thread.setExceptionInfo(msg);
							throw new RestServiceException(thread,
									System.currentTimeMillis() - start, msg,
									RestBusinessCode.missing_args.getValue());
						} else {
							String msg = "找不到对应的的数值类域配置compile为"
									+ config.getCompileId() + "regionid为"
									+ numRegion.getRegionId() + sb.toString();
							thread.setExceptionInfo(msg);
							throw new RestServiceException(thread,
									System.currentTimeMillis() - start, msg,
									RestBusinessCode.missing_args.getValue());
						}
					}
				}

			}
			if (count == validList.size()) {// 数据库中当前配置分组下有效的域配置,均被修改为无效,所以要将该配置分组置为无效
				List<ConfigGroupRelation> groupRelationList = config
						.getGroupRelationList();
				for (ConfigGroupRelation configGroupRelation : groupRelationList) {
					if (configGroupRelation.getGroupId().equals(regionId)) {
						num++;
						configGroupRelation.setIsValid(0);
						configGroupRelation.setOpTime(opTime);
						if (Configurations.getStringProperty("isCommit",
								"false").equals("true")) {
							batchSqlSession.getMapper(
									ConfigGroupRelationDao.class)
									.updateConfigGroupRelation(
											configGroupRelation);
						}
					}
				}

			}
		}

		if (num == validMap.size()) {// 所有的域均是无效的所有的组均是无效,需要将编译配置置为无效
			config.setIsValid(0);
			if (Configurations.getStringProperty("isCommit", "false").equals(
					"true")) {
				batchSqlSession.getMapper(ConfigCompileDao.class)
						.updateConfigCompile(config);
			}
		}
	}

	public Map<String, Map<Long, List<Long>>> getRegionList(
			SaveRequestLogThread thread, long start, ConfigCompile config,
			List<ConfigGroupRelation> queryCompileGroupByPID,
			SqlSession batchSqlSession, StringBuffer sb) {
		Map<String, Map<Long, List<Long>>> map = new HashMap<String, Map<Long, List<Long>>>();
		Map<Integer, Map<String, String>> tableRelationMap = getTableRelation();
		Map<String, String> updateTableMap = tableRelationMap.get(config
				.getService().intValue());// 根据service获取需要操作的域配置表
		if (null == updateTableMap) {
			thread.setExceptionInfo("找不到编译对应的service" + sb.toString());
			throw new RestServiceException(thread, System.currentTimeMillis()
					- start, "找不到编译对应的service" + sb.toString(),
					RestBusinessCode.missing_args.getValue());
		}

		Long[] groupId = new Long[queryCompileGroupByPID.size()];
		for (int i = 0; i < queryCompileGroupByPID.size(); i++) {
			groupId[i] = queryCompileGroupByPID.get(i).getGroupId();
		}
		// 获取当前编译下所有的域配置信息
		List<IpRegion> ipRegionExistList = new ArrayList<IpRegion>();
		List<StrRegion> strRegionExistList = new ArrayList<StrRegion>();
		List<NumRegion> numRegionExistList = new ArrayList<NumRegion>();
		for (String tableName : updateTableMap.keySet()) {
			if (updateTableMap.get(tableName).equals("ip")) {
				List<IpRegion> queryIpRegionByGroupId = ipRegionDao
						.queryIpRegionByGroupId(tableName, groupId);
				ipRegionExistList.addAll(queryIpRegionByGroupId);
			}
			if (updateTableMap.get(tableName).equals("num")) {
				numRegionExistList.addAll(numRegionDao.queryNumRegionByGroupId(
						tableName, groupId));
			}
			if (updateTableMap.get(tableName).equals("str")) {
				strRegionExistList.addAll(strRegionDao.queryStrRegionByGroupId(
						tableName, groupId));
			}
		}

		Map<Long, List<Long>> validMap = new HashMap<Long, List<Long>>();// 获取当前编译下所有的组,及每个组下面的有效域配置
		Map<Long, List<Long>> noValidMap = new HashMap<Long, List<Long>>();// 获取当前编译下所有的组,及每个组下面的无效域配置
		for (IpRegion ipRegion : ipRegionExistList) {
			if (ipRegion.getIsValid() == 1) {
				List<Long> validRegionList = null;
				if (validMap.get(ipRegion.getGroupId()) == null) {
					validRegionList = new ArrayList<Long>();
					validRegionList.add(ipRegion.getRegionId());
					validMap.put(ipRegion.getGroupId(), validRegionList);
				} else {
					validRegionList = validMap.get(ipRegion.getGroupId());
					// if (!validRegionList.contains(ipRegion.getRegionId())) {
					validRegionList.add(ipRegion.getRegionId());
					validMap.put(ipRegion.getGroupId(), validRegionList);
					// }
				}
			}
			if (ipRegion.getIsValid() == 0) {
				List<Long> noValidRegionList = null;
				if (noValidMap.get(ipRegion.getGroupId()) == null) {
					noValidRegionList = new ArrayList<Long>();
					noValidRegionList.add(ipRegion.getRegionId());
					noValidMap.put(ipRegion.getGroupId(), noValidRegionList);
				} else {
					noValidRegionList = noValidMap.get(ipRegion.getGroupId());
					// if (!noValidRegionList.contains(ipRegion.getRegionId()))
					// {
					noValidRegionList.add(ipRegion.getRegionId());
					noValidMap.put(ipRegion.getGroupId(), noValidRegionList);
					// }
				}
			}
		}

		for (StrRegion strRegion : strRegionExistList) {
			if (strRegion.getIsValid() == 1) {
				List<Long> validRegionList = null;
				if (validMap.get(strRegion.getGroupId()) == null) {
					validRegionList = new ArrayList<Long>();

					validRegionList.add(strRegion.getRegionId());

					validMap.put(strRegion.getGroupId(), validRegionList);
				} else {
					validRegionList = validMap.get(strRegion.getGroupId());
					// if (!validRegionList.contains(strRegion.getRegionId())) {
					validRegionList.add(strRegion.getRegionId());
					validMap.put(strRegion.getGroupId(), validRegionList);

					// }
				}
			}
			if (strRegion.getIsValid() == 0) {
				List<Long> noValidRegionList = null;
				if (noValidMap.get(strRegion.getGroupId()) == null) {
					noValidRegionList = new ArrayList<Long>();
					noValidRegionList.add(strRegion.getRegionId());
					noValidMap.put(strRegion.getGroupId(), noValidRegionList);
				} else {
					noValidRegionList = noValidMap.get(strRegion.getGroupId());
					// if (!noValidRegionList.contains(strRegion.getRegionId()))
					// {
					noValidRegionList.add(strRegion.getRegionId());
					noValidMap.put(strRegion.getGroupId(), noValidRegionList);
					// }
				}
			}
		}
		for (NumRegion numRegion : numRegionExistList) {
			if (numRegion.getIsValid() == 1) {
				List<Long> validRegionList = null;
				if (validMap.get(numRegion.getGroupId()) == null) {
					validRegionList = new ArrayList<Long>();
					validRegionList.add(numRegion.getRegionId());
					validMap.put(numRegion.getGroupId(), validRegionList);

				} else {
					validRegionList = validMap.get(numRegion.getGroupId());
					// if (!validRegionList.contains(numRegion.getRegionId())) {
					validRegionList.add(numRegion.getRegionId());
					validMap.put(numRegion.getGroupId(), validRegionList);
					// }

				}
			}
			if (numRegion.getIsValid() == 0) {
				List<Long> noValidRegionList = null;
				if (noValidMap.get(numRegion.getGroupId()) == null) {
					noValidRegionList = new ArrayList<Long>();
					noValidRegionList.add(numRegion.getRegionId());
					noValidMap.put(numRegion.getGroupId(), noValidRegionList);
				} else {
					noValidRegionList = noValidMap.get(numRegion.getGroupId());
					// if (!noValidRegionList.contains(numRegion.getRegionId()))
					// {
					noValidRegionList.add(numRegion.getRegionId());
					noValidMap.put(numRegion.getGroupId(), noValidRegionList);
					// }
				}
			}
		}

		map.put("noValidMap", noValidMap);
		map.put("validMap", validMap);
		return map;

	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 批量插入配置编译数据在数据库中循环
	 * 
	 * @param configSource
	 * @throws Exception
	 */
	public void insertConfigSourceData(SaveRequestLogThread thread,
			long _start, List<ConfigCompile> configSource, StringBuffer sb) {
		SqlSessionFactory sqlSessionFactory = SpringContextHolder
				.getBean(SqlSessionFactory.class);
		SqlSession batchSqlSession = null;
		try {
			// batchSqlSession =
			// sqlSessionFactory.openSession(ExecutorType.SIMPLE, false);
			batchSqlSession = sqlSessionFactory.openSession();
			int index = 150;
			int count = configSource.size() % index == 0 ? configSource.size()
					/ index : configSource.size() / index + 1;
			for (int i = 0; i < count; i++) {
				int start = i * index;
				int end = i * index + index;
				if (end > configSource.size()) {
					end = configSource.size();
				}
				List<ConfigCompile> compileList = configSource.subList(start,
						end);
				insertConfigComLileData(thread, _start, compileList, sb);
				// logger1.info(insertConfigComLile);
			}

			logger1.info("数据保存到数据库提交事务开始");
			batchSqlSession.commit();
			logger1.info("数据保存到数据库成功");

		} catch (RestServiceException e) {
			logger1.error(e);
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause()
					+ sb.toString());
			throw new RestServiceException(thread, System.currentTimeMillis()
					- _start, e.getMessage() + sb.toString(), e.getErrorCode());

		} catch (Exception e) {
			logger1.error(e);
			String errorCode = OracleErrorCodeUtil.getOraCode(e);
			if (!StringUtils.isEmpty(errorCode)) {
				thread.setExceptionInfo("数据保存发生异常" + sb.toString());
				OracleErrorCodeUtil.throwExceptionInfo(thread,
						System.currentTimeMillis() - _start, errorCode);
			} else {
				thread.setExceptionInfo("数据保存发生异常" + sb.toString());
				throw new RestServiceException(thread,
						System.currentTimeMillis() - _start, "数据保存发生异常"
								+ sb.toString(),
						RestBusinessCode.unknow_error.getValue());
			}

		} finally {
			if (batchSqlSession != null) {
				batchSqlSession.close();
			}
		}

	}

	/**
	 * 验证数据是否符合要求 插入编译配置数据 验证分组配置数据 验证域配置数据 方法二 数据库中循环
	 * 
	 * @param configCompileList
	 * @param batchSqlSession
	 * @return
	 * @throws Exception
	 */
	private String insertConfigComLileData(SaveRequestLogThread thread,
			long start, List<ConfigCompile> configCompileList, StringBuffer sb)
			throws Exception {

		List<ConfigGroupRelation> groupRelationList = new ArrayList<ConfigGroupRelation>();
		Map<String, List<StrRegion>> strRegionMap = new HashMap<String, List<StrRegion>>();
		Map<String, List<StrRegion>> strStongRegionMap = new HashMap<String, List<StrRegion>>();
		Map<String, List<IpRegion>> ipRegionMap = new HashMap<String, List<IpRegion>>();
		Map<String, List<NumRegion>> numRegionMap = new HashMap<String, List<NumRegion>>();

		for (ConfigCompile configCompile : configCompileList) {
			String msg = CompileVal.compileIsOk(configCompile, false, sb);
			if (!"ok".equals(msg)) {
				logger1.error(msg);
				thread.setExceptionInfo(msg + sb.toString());
				throw new RestServiceException(thread,
						System.currentTimeMillis() - start,
						msg + sb.toString(),
						RestBusinessCode.config_integrity_error.getValue());
			}
			if (null != configCompile.getGroupRelationList()
					&& configCompile.getGroupRelationList().size() > 0) {
				groupRelationList.addAll(configCompile.getGroupRelationList());
			} else {
				logger1.error("配置分组数量不能为空" + sb.toString());
				thread.setExceptionInfo("配置分组数量不能为空" + sb.toString());
				throw new RestServiceException(thread,
						System.currentTimeMillis() - start, "配置分组数量不能为空"
								+ sb.toString(),
						RestBusinessCode.missing_args.getValue());
			}
			if (null != configCompile.getStrRegionList()
					&& configCompile.getStrRegionList().size() > 0) {
				for (StrRegion strRegion : configCompile.getStrRegionList()) {
					if (!isStrStrongRegion(strRegion.getTableName())) {
						if (strRegion.getRegionId() == null) {
							String errorMsg = "字符类域配置id不能为空 ,表名---"
									+ strRegion.getTableName() + ""
									+ sb.toString();
							logger1.error(errorMsg);
							thread.setExceptionInfo(errorMsg);
							throw new RestServiceException(thread,
									System.currentTimeMillis() - start,
									errorMsg,
									RestBusinessCode.missing_args.getValue());
						}
						if (strRegionMap.containsKey(strRegion.getTableName())) {
							strRegionMap.get(strRegion.getTableName()).add(
									strRegion);
						} else {
							List<StrRegion> strRegionList = new ArrayList<StrRegion>();
							strRegionList.add(strRegion);
							strRegionMap.put(strRegion.getTableName(),
									strRegionList);
						}
					} else {
						if (strRegion.getRegionId() == null) {
							String errorMsg = "增强字符类域配置id不能为空 ,表名---"
									+ strRegion.getTableName() + ""
									+ sb.toString();
							logger1.error(errorMsg);
							thread.setExceptionInfo(errorMsg);
							throw new RestServiceException(thread,
									System.currentTimeMillis() - start,
									errorMsg,
									RestBusinessCode.missing_args.getValue());
						}
						if (strStongRegionMap.containsKey(strRegion
								.getTableName())) {
							strStongRegionMap.get(strRegion.getTableName())
									.add(strRegion);
						} else {
							List<StrRegion> strStrongRegionList = new ArrayList<StrRegion>();
							strStrongRegionList.add(strRegion);
							strStongRegionMap.put(strRegion.getTableName(),
									strStrongRegionList);
						}

					}
				}
			}

			if (null != configCompile.getIpRegionList()
					&& configCompile.getIpRegionList().size() > 0) {
				for (IpRegion ipRegion : configCompile.getIpRegionList()) {
					if (!ipRegion.getTableName().toUpperCase()
							.equals("DJ_IP_PORT")) {
						ipRegion.setProtocol(0);
					}

					if (ipRegion.getRegionId() == null) {
						String errorMsg = "ip类域配置id不能为空 ,表名---"
								+ ipRegion.getTableName() + "" + sb.toString();
						logger1.error(errorMsg);
						thread.setExceptionInfo(errorMsg);
						throw new RestServiceException(thread,
								System.currentTimeMillis() - start, errorMsg,
								RestBusinessCode.missing_args.getValue());
					}

					if (ipRegionMap.containsKey(ipRegion.getTableName())) {
						ipRegionMap.get(ipRegion.getTableName()).add(ipRegion);
					} else {
						List<IpRegion> ipRegionList = new ArrayList<IpRegion>();
						ipRegionList.add(ipRegion);
						ipRegionMap.put(ipRegion.getTableName(), ipRegionList);
					}
				}
			}

			if (null != configCompile.getNumRegionList()
					&& configCompile.getNumRegionList().size() > 0) {
				for (NumRegion numRegion : configCompile.getNumRegionList()) {
					if (numRegion.getRegionId() == null) {
						String errorMsg = "数值类域配置id不能为空  ,表名---"
								+ numRegion.getTableName() + "" + sb.toString();
						logger1.error(errorMsg);
						thread.setExceptionInfo(errorMsg);
						throw new RestServiceException(thread,
								System.currentTimeMillis() - start, errorMsg,
								RestBusinessCode.missing_args.getValue());
					}

					if (numRegionMap.containsKey(numRegion.getTableName())) {
						numRegionMap.get(numRegion.getTableName()).add(
								numRegion);
					} else {
						List<NumRegion> numRegionList = new ArrayList<NumRegion>();
						numRegionList.add(numRegion);
						numRegionMap.put(numRegion.getTableName(),
								numRegionList);
					}
				}
			}
		}
		if (Configurations.getStringProperty("isCommit", "false")
				.equals("true")) {
			configCompileDao.saveCompileBatch(configCompileList);
			if (groupRelationList.size() > 0) {
				for (ConfigGroupRelation group : groupRelationList) {
					configGroupRelationDao.saveConfigGroupRelation(group);
				}
			}
			for (String str : strRegionMap.keySet()) {
				strRegionDao.saveStrRegionBatch(strRegionMap.get(str).get(0)
						.getTableName(), strRegionMap.get(str));
			}
			for (String strStrong : strStongRegionMap.keySet()) {
				strRegionDao.saveStrStrongRegionBatch(
						strStongRegionMap.get(strStrong).get(0).getTableName(),
						strStongRegionMap.get(strStrong));
			}
			for (String ip : ipRegionMap.keySet()) {
				ipRegionDao.saveIpRegionBatch(ipRegionMap.get(ip).get(0)
						.getTableName(), ipRegionMap.get(ip));
			}
			for (String num : numRegionMap.keySet()) {
				numRegionDao.saveNumRegionBatch(numRegionMap.get(num).get(0)
						.getTableName(), numRegionMap.get(num));
			}
		}
		return "编译配置数据验证成功";

	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public Page<ConfigCompileTest> queryAllCompile(
			Page<ConfigCompileTest> page, ConfigCompileTest entity)
			throws SQLException {
		entity.setPage(page);
		page.setList(configCompileDao.queryAllCompile(
				entity.getSearchEndTime(), entity.getSearchStartTime(),
				entity.getService(), entity.getCompileId()));
		return page;
	}

	public List<ConfigCompileTest> queryAllCompile(ConfigCompileTest entity) {
		return configCompileDao.queryAllCompile(entity.getSearchEndTime(),
				entity.getSearchStartTime(), entity.getService(),
				entity.getCompileId());
	}

	public List<ConfigCompileTest> queryConfigCompileByIdArr(String compileIdArr) {
		return configCompileDao.queryConfigCompileByIdArr(compileIdArr);
	}

	public List<ConfigGroupRelation> getGroupByCompile(long compileId) {
		return configGroupRelationDao.queryCompileGroupByPID(compileId);
	}

	public List<StrRegion> getStrRegionByGId(List<String> tableList,
			Long[] groupId) {
		List<StrRegion> strRegionList = new ArrayList<StrRegion>();
		if (null != tableList && tableList.size() > 0) {
			for (String tableName : tableList) {
				List<StrRegion> queryStrRegionByGroupId = null;
				if (isStrStrongRegion(tableName)) {
					queryStrRegionByGroupId = strRegionDao
							.queryStrStrongRegionByGroupId(tableName, groupId);
				} else {
					queryStrRegionByGroupId = strRegionDao
							.queryStrRegionByGroupId(tableName, groupId);
				}

				if (null != queryStrRegionByGroupId
						&& queryStrRegionByGroupId.size() > 0) {
					for (StrRegion strRegion : queryStrRegionByGroupId) {
						strRegion.setTableName(tableName);
						strRegionList.add(strRegion);
					}
				}

			}
		}

		return strRegionList;
	}

	public List<IpRegion> getIpRegionByGId(String table, Long[] groupId) {
		List<IpRegion> queryIpRegionByGroupId = ipRegionDao
				.queryIpRegionByGroupId(table, groupId);
		for (IpRegion ipRegion : queryIpRegionByGroupId) {
			ipRegion.setTableName(table);
		}
		return queryIpRegionByGroupId;
	}

	public List<NumRegion> getNumRegionByGId(String table, Long[] groupId) {
		List<NumRegion> queryNumRegionByGroupId = numRegionDao
				.queryNumRegionByGroupId(table, groupId);
		for (NumRegion numRegion : queryNumRegionByGroupId) {
			numRegion.setTableName(table);
		}
		return queryNumRegionByGroupId;
	}

	public static boolean isStrStrongRegion(String tableName) {
		if (null != tableName && !tableName.equals("")) {
			tableName = tableName.toUpperCase();
			List<String> tableList = new ArrayList<String>();
			tableList.add("DF_HTTP_REQ_HDR");
			tableList.add("FX_HTTP_REQ_HDR");
			tableList.add("DF_HTTP_RES_HDR");
			tableList.add("DF_DNS_REGION");
			tableList.add("FX_DNS_REGION");
			tableList.add("DF_SSL_REGION");
			tableList.add("DF_MAIL_HDR");
			tableList.add("DF_MAIL_BODY");

			tableList.add("DJ_HTTP_REQ_HDR");
			tableList.add("DJ_HTTP_RES_HDR");
			tableList.add("DJ_DNS_REQ_REGION");
			tableList.add("DJ_DNS_RES_REGION");
			tableList.add("DJ_SSL_REGION");
			tableList.add("DJ_MAIL_HDR");
			tableList.add("DJ_MAIL_BODY");
			// tableList.add("DF_L2TP_URL");//20161021数据库中该表改为普通字符串表
			// tableList.add("DF_PPTP_URL");//20161021数据库中该表改为普通字符串表
			if (tableList.contains(tableName)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	public Map<String, String> getTableMap(Integer service) {
		if (map == null || map.size() == 0) {
			map = getTableRelation();
		}
		return map.get(service);
	}

	public Map<Integer, Map<String, String>> getMap() {
		return map;
	}

	public void setMap(Map<Integer, Map<String, String>> map) {
		this.map = map;
	}

	/**
	 * 
	 * @Description:
	 * @author (zdx)
	 * @date 2018年5月25日 下午3:49:46
	 * @param thread
	 * @param start
	 * @param configCompileList
	 * @param sb
	 * @return
	 */
	public String saveMaatConfig(SaveRequestLogThread thread, long start,
			List<ConfigCompile> configCompileList, StringBuffer sb) {
		// List<MaatConfig> maatConfigList = new ArrayList<MaatConfig>();
		// List<Integer> serviceList = new ArrayList<Integer>();
		Map<Integer, List<MaatConfig>> maatMap = new HashMap<Integer, List<MaatConfig>>();
		Map<Integer, List<MaatConfig>> configMap = new HashMap<Integer, List<MaatConfig>>();
		CompileVal.setBusinessCode(null);
		try {

			for (ConfigCompile configCompile : configCompileList) {
				Integer service = Integer.valueOf(configCompile.getService()
						.toString());
				MaatConfig maatConfig = new MaatConfig();
				String msg = CompileVal.compileIsOk(configCompile, false, sb);
				if (!"ok".equals(msg)) {
					logger1.error(msg);
					thread.setExceptionInfo(msg + sb.toString());
					thread.setBusinessCode(CompileVal.getBusinessCode());
					throw new RestServiceException(thread,
							System.currentTimeMillis() - start, msg
									+ sb.toString(),
									CompileVal.getBusinessCode());
				}

				if (!(null != configCompile.getGroupRelationList() && configCompile
						.getGroupRelationList().size() > 0)) {
					logger1.error("配置分组列表不能为空" + sb.toString());
					thread.setExceptionInfo("配置分组数量不能为空" + sb.toString());
					throw new RestServiceException(thread,
							System.currentTimeMillis() - start, "配置分组列表不能为空"
									+ sb.toString(),
							RestBusinessCode.CompileGroupIsNull.getValue());
				}
				if (null != configCompile.getStrRegionList()
						&& configCompile.getStrRegionList().size() > 0) {
					for (StrRegion strRegion : configCompile.getStrRegionList()) {
						if (strRegion.getRegionId() == null) {
							String errorMsg = "字符类域配置id不能为空 ,表名---"
									+ strRegion.getTableName() + ""
									+ sb.toString();
							logger1.error(errorMsg);
							thread.setExceptionInfo(errorMsg);
							throw new RestServiceException(thread,
									System.currentTimeMillis() - start,
									errorMsg,
									RestBusinessCode.RegionIdIsNull.getValue());
						}
					}
				}

				if (null != configCompile.getIpRegionList()
						&& configCompile.getIpRegionList().size() > 0) {
					for (IpRegion ipRegion : configCompile.getIpRegionList()) {
						if (ipRegion.getRegionId() == null) {
							String errorMsg = "ip类域配置id不能为空 ,表名---"
									+ ipRegion.getTableName() + ""
									+ sb.toString();
							logger1.error(errorMsg);
							thread.setExceptionInfo(errorMsg);
							throw new RestServiceException(thread,
									System.currentTimeMillis() - start,
									errorMsg,
									RestBusinessCode.RegionIdIsNull.getValue());
						}
					}
				}

				if (null != configCompile.getNumRegionList()
						&& configCompile.getNumRegionList().size() > 0) {
					for (NumRegion numRegion : configCompile.getNumRegionList()) {
						if (numRegion.getRegionId() == null) {
							String errorMsg = "数值类域配置id不能为空  ,表名---"
									+ numRegion.getTableName() + ""
									+ sb.toString();
							logger1.error(errorMsg);
							thread.setExceptionInfo(errorMsg);
							throw new RestServiceException(thread,
									System.currentTimeMillis() - start,
									errorMsg,
									RestBusinessCode.RegionIdIsNull.getValue());
						}
					}
				}
				if (null != configCompile.getDigestRegionList()
						&& configCompile.getDigestRegionList().size() > 0) {
					for (DigestRegion digestRegion : configCompile
							.getDigestRegionList()) {
						if (digestRegion.getRegionId() == null) {
							String errorMsg = "摘要类域配置id不能为空  ,表名---"
									+ digestRegion.getTableName() + ""
									+ sb.toString();
							logger1.error(errorMsg);
							thread.setExceptionInfo(errorMsg);
							throw new RestServiceException(thread,
									System.currentTimeMillis() - start,
									errorMsg,
									RestBusinessCode.RegionIdIsNull.getValue());
						}
					}
				}

				if (null != configCompile.getIpClientRangeList()
						&& configCompile.getIpClientRangeList().size() > 0) {
					for (IpRegion ipRegion : configCompile
							.getIpClientRangeList()) {
						if (ipRegion.getRegionId() == null) {
							String errorMsg = "生效范围IP域类域配置id不能为空 ,表名---"
									+ ipRegion.getTableName() + ""
									+ sb.toString();
							logger1.error(errorMsg);
							thread.setExceptionInfo(errorMsg);
							throw new RestServiceException(thread,
									System.currentTimeMillis() - start,
									errorMsg,
									RestBusinessCode.RegionIdIsNull.getValue());
						}
					}
				}

				maatConfig.setService(service);
				// 编译
				maatConfig.setCompileMap(convertObjectToMap(configCompile,
						ConfigCompile.class));
				// 分组
				List<Map<String, String>> dstMaplList = null;
				if (!StringUtil.isEmpty(configCompile.getGroupRelationList())) {
					dstMaplList = new ArrayList<Map<String, String>>();
					for (ConfigGroupRelation group : configCompile
							.getGroupRelationList()) {
						dstMaplList.add(convertObjectToMap(group,
								ConfigGroupRelation.class));
					}
				}
				maatConfig.setGroupMapList(dstMaplList);
				// 字符串域
				dstMaplList = null;
				List<Map<String, String>> strongMapList = null;
				if (!StringUtil.isEmpty(configCompile.getStrRegionList())) {
					dstMaplList = new ArrayList<Map<String, String>>();
					for (StrRegion region : configCompile.getStrRegionList()) {
						if (StringUtil.isEmpty(region.getDistrict())) {
							dstMaplList.add(convertObjectToMap(region,
									StrRegion.class));
						} else {
							if (StringUtil.isEmpty(strongMapList)) {
								strongMapList = new ArrayList<Map<String, String>>();
							}
							strongMapList.add(convertObjectToMap(region,
									StrRegion.class));
						}
					}
				}
				maatConfig.setStrRegionMapList(dstMaplList);
				// 增强字符串域
				if (!StringUtil.isEmpty(strongMapList)
						&& strongMapList.size() > 0) {
					maatConfig.setStrStrRegionMapList((strongMapList));
				}
				// 数值域
				dstMaplList = null;
				if (!StringUtil.isEmpty(configCompile.getNumRegionList())) {
					dstMaplList = new ArrayList<Map<String, String>>();
					for (NumRegion region : configCompile.getNumRegionList()) {
						dstMaplList.add(convertObjectToMap(region,
								NumRegion.class));
					}
				}
				maatConfig.setNumRegionMapList(dstMaplList);

				// Ip域
				dstMaplList = null;
				if (!StringUtil.isEmpty(configCompile.getIpRegionList())) {
					dstMaplList = new ArrayList<Map<String, String>>();
					for (IpRegion region : configCompile.getIpRegionList()) {
						dstMaplList.add(convertObjectToMap(region,
								IpRegion.class));
					}
				}
				maatConfig.setIpRegionMapList(dstMaplList);

				// 摘要类域
				dstMaplList = null;
				if (!StringUtil.isEmpty(configCompile.getDigestRegionList())) {
					dstMaplList = new ArrayList<Map<String, String>>();
					for (DigestRegion region : configCompile
							.getDigestRegionList()) {
						dstMaplList.add(convertObjectToMap(region,
								DigestRegion.class));
					}
				}

				maatConfig.setFileDigestRegionMapList(dstMaplList);

				// 文本相似性域
				// dstMaplList = null;
				// maatConfig.setFileLikeRegionMapList(dstMaplList);

				// 生效范围IP域
				dstMaplList = null;
				if (!StringUtil.isEmpty(configCompile.getIpClientRangeList())) {
					dstMaplList = new ArrayList<Map<String, String>>();
					for (IpRegion region : configCompile.getIpClientRangeList()) {
						dstMaplList.add(convertObjectToMap(region,
								IpRegion.class));
					}
				}
				maatConfig.setIpClientRangeMapList(dstMaplList);

				if (maatMap.containsKey(service)) {
					maatMap.get(service).add(maatConfig);
				} else {
					List<MaatConfig> maatCfgList = new ArrayList<MaatConfig>();
					maatCfgList.add(maatConfig);
					maatMap.put(service, maatCfgList);

				}
			}

			// 调用接口入redis

			Iterator serviceIterator = maatMap.keySet().iterator();
			while (serviceIterator.hasNext()) {
				Integer service = Integer.valueOf(serviceIterator.next()
						.toString());
				List<Integer> dbIndexList = ServiceAndRDBIndexReal
						.getRedisDBByService(service);
				if (!StringUtil.isEmpty(dbIndexList) && dbIndexList.size() > 0) {
					for (Integer dbIndex : dbIndexList) {
						// 分发到阀门有些业务需要添加编译属性到域配置
						List<MaatConfig> newMaatConfigList = new ArrayList<MaatConfig>();
						newMaatConfigList.addAll(maatMap.get(service));
						if (dbIndex.intValue() == ServiceAndRDBIndexReal
								.getValveDBIndex().intValue()) {
							Map<Integer, Map<String, String[]>> maatToValueMap = ServiceAndRDBIndexReal
									.getMaatToValveMap();
							if (maatToValueMap.containsKey(service)) {

								Map<String, String[]> regionAndFiledMap = maatToValueMap
										.get(service);
								// for (MaatConfig maatConfig :
								// newMaatConfigList) {
								for (int i = 0; i < newMaatConfigList.size(); i++) {
									MaatConfig maatConfig = newMaatConfigList
											.get(i);
									MaatConfig newMaatConfig = (MaatConfig) JsonMapper
											.fromJsonString(JsonMapper
													.toJsonString(maatConfig),
													MaatConfig.class);
									Iterator iterator = regionAndFiledMap
											.keySet().iterator();
									while (iterator.hasNext()) {
										String regionName = iterator.next()
												.toString();
										PropertyDescriptor pd;
										try {
											pd = new PropertyDescriptor(
													regionName + "MapList",
													MaatConfig.class);
											Method method = pd.getReadMethod();
											Object object = method
													.invoke(newMaatConfig);

											if (object != null) {

												List<Map<String, String>> listMaps = new ArrayList<Map<String, String>>();
												listMaps.addAll((List<Map<String, String>>) object);
												String[] fields = regionAndFiledMap
														.get(regionName);
												for (String fieldName : fields) {
													String value = newMaatConfig
															.getCompileMap()
															.get(fieldName
																	.toLowerCase());
													if (!StringUtil
															.isEmpty(value)) {
														for (Map<String, String> map : listMaps) {
															map.put(fieldName
																	.toLowerCase(),
																	value);
														}
													}
												}
												method = pd.getWriteMethod();
												method.invoke(newMaatConfig,
														listMaps);
											}
											newMaatConfigList.set(i,
													newMaatConfig);
										} catch (Exception e) {
											// TODO Auto-generated catch block
											CompileVal.setBusinessCode(RestBusinessCode.service_runtime_error.getValue());
											e = new RuntimeException(
													"未找到域列表，请检查域类型是否正确！");
											msgList.add(e);
											return "error";
										}

									}
								}
							}
						}

						if (configMap.containsKey(dbIndex)) {
							configMap.get(dbIndex).addAll(newMaatConfigList);
						} else {
							List<MaatConfig> list = new ArrayList<MaatConfig>();
							list.addAll(newMaatConfigList);
							configMap.put(dbIndex, list);
						}

					}
				} else {
					RuntimeException e = new RuntimeException(
							"service与写入数据库序号映射关系不存在");
					CompileVal.setBusinessCode(RestBusinessCode.service_runtime_error.getValue());
					msgList.add(e);
					return "error";
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			msgList.add(e);
			//CompileVal.setBusinessCode(RestBusinessCode.unknow_error.getValue());
			return "error";
		}
		try {
			logger1.info("---------------调用maat配置新增接口---------------------");
			configRedisService.saveMaatConfig(configMap);
		} catch (Exception e) {
			// TODO: handle exception
			CompileVal.setBusinessCode(RestBusinessCode.unknow_error.getValue());
			if (e.getMessage().startsWith("后台错误:")) {
				CompileVal.setBusinessCode(RestBusinessCode.service_runtime_error.getValue());
			}
			logger1.error(e.getMessage());
			msgList.add(e);
			return "error";
		}
		return "ok";
	}

	private Map<String, String> convertObjectToMap(Object obj, Class clazz)
			throws IntrospectionException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Map<String, String> dstMap = new HashMap<String, String>();
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			if ("serialVersionUID".equals(field.getName()))
				continue;
			String dstName = CamelUnderlineUtil.camelToUnderline(field
					.getName());
			PropertyDescriptor pd;
			pd = new PropertyDescriptor(field.getName(), clazz);
			Method method = pd.getReadMethod();
			Object dstObject = method.invoke(obj);
			if (dstObject instanceof Date) {
				dstObject = convertToTimeStamp16((Date) dstObject);
			}
			if (dstObject instanceof ArrayList) {
				dstObject = "";
			}
			dstMap.put(dstName,
					StringUtil.isEmpty(dstObject) ? "" : dstObject.toString());
		}
		return dstMap;
	}

	public String updateConfigSources(SaveRequestLogThread thread, long start,
			List<ConfigCompile> compileList, Date opTime, StringBuffer sb) {
		Map<Integer, List<Long>> compileMap = new HashMap<Integer, List<Long>>();
		if (null != compileList && compileList.size() > 0) {
			for (ConfigCompile config : compileList) {
				String msg = checkCompileOptForUpdate(config);
				if (config.getOpTime() == null) {
					config.setOpTime(opTime);
				}
				if (!msg.equals("ok")) {
					thread.setExceptionInfo(msg + sb.toString());
					throw new RestServiceException(thread,
							System.currentTimeMillis() - start, msg
									+ sb.toString(),
									CompileVal.getBusinessCode());
				}

				// compileAllList.add(config);
				if (compileMap.containsKey(config.getService())) {
					compileMap.get(config.getService()).add(
							config.getCompileId());
				} else {
					List<Long> idList = new ArrayList<Long>();
					idList.add(config.getCompileId());
					compileMap.put(config.getService(), idList);
				}
			}

		} else {
			thread.setExceptionInfo("编译配置不能为空" + sb.toString());
			throw new RestServiceException(thread, System.currentTimeMillis()
					- start, "编译配置不能为空" + sb.toString(),
					RestBusinessCode.CompileIsNull.getValue());
		}
		Map<Integer, Map<Integer, List<Long>>> restMap = new HashMap<Integer, Map<Integer, List<Long>>>();
		Iterator serviceIterator = compileMap.keySet().iterator();
		while (serviceIterator.hasNext()) {
			Integer service = Integer
					.valueOf(serviceIterator.next().toString());
			List<Integer> dbIndexList = ServiceAndRDBIndexReal
					.getRedisDBByService(service);
			if (!StringUtil.isEmpty(dbIndexList) && dbIndexList.size() > 0) {
				for (Integer dbIndex : dbIndexList) {
					if (restMap.containsKey(dbIndex)) {
						restMap.get(dbIndex).put(service,
								compileMap.get(service));
					} else {
						Map<Integer, List<Long>> map = new HashMap<Integer, List<Long>>();
						map.put(service, compileMap.get(service));
						restMap.put(dbIndex, map);
					}
				}
			} else {
				RuntimeException e = new RuntimeException("service值为" + service
						+ ",与写入数据库序号映射关系不存在");
				CompileVal.setBusinessCode(RestBusinessCode.service_runtime_error.getValue());
				msgList.add(e);
				return "error";
			}
		}
		try {
			if (!configRedisService.delMaatConfig(restMap)) {
				RuntimeException e = new RuntimeException("不存在映射关系");
				CompileVal.setBusinessCode(RestBusinessCode.service_runtime_error.getValue());
				msgList.add(e);
				return "error";
			}
		} catch (RuntimeException e) {
			// TODO: handle exception
			logger1.error(e.getMessage());
			CompileVal.setBusinessCode(RestBusinessCode.unknow_error.getValue());
			if (e.getMessage().startsWith("后台错误:")) {
				CompileVal.setBusinessCode(RestBusinessCode.service_runtime_error.getValue());
			}
			msgList.add(e);
			return "error";
		}
		return "ok";
	}

	public String saveByJDBCThread(SaveRequestLogThread thread, long start,
			List<ConfigCompile> configCompileList, StringBuffer sb) {
		List<ConfigGroupRelation> groupRelationList = new ArrayList<ConfigGroupRelation>();
		Map<String, List<StrRegion>> strRegionMap = new HashMap<String, List<StrRegion>>();
		Map<String, List<StrRegion>> strStongRegionMap = new HashMap<String, List<StrRegion>>();
		Map<String, List<IpRegion>> ipRegionMap = new HashMap<String, List<IpRegion>>();
		Map<String, List<NumRegion>> numRegionMap = new HashMap<String, List<NumRegion>>();
		Map<String, List<DigestRegion>> digestRegionMap = new HashMap<String, List<DigestRegion>>();

		for (ConfigCompile configCompile : configCompileList) {
			String msg = CompileVal.compileIsOk(configCompile, false, sb);
			if (!"ok".equals(msg)) {
				logger1.error(msg);
				thread.setExceptionInfo(msg + sb.toString());
				throw new RestServiceException(thread,
						System.currentTimeMillis() - start,
						msg + sb.toString(),
						RestBusinessCode.config_integrity_error.getValue());
			}
			if (null != configCompile.getGroupRelationList()
					&& configCompile.getGroupRelationList().size() > 0) {
				groupRelationList.addAll(configCompile.getGroupRelationList());
			} else {
				logger1.error("配置分组数量不能为空" + sb.toString());
				thread.setExceptionInfo("配置分组数量不能为空" + sb.toString());
				throw new RestServiceException(thread,
						System.currentTimeMillis() - start, "配置分组数量不能为空"
								+ sb.toString(),
						RestBusinessCode.missing_args.getValue());
			}
			if (null != configCompile.getStrRegionList()
					&& configCompile.getStrRegionList().size() > 0) {
				for (StrRegion strRegion : configCompile.getStrRegionList()) {
					if (!isStrStrongRegion(strRegion.getTableName())) {
						if (strRegion.getRegionId() == null) {
							String errorMsg = "字符类域配置id不能为空 ,表名---"
									+ strRegion.getTableName() + ""
									+ sb.toString();
							logger1.error(errorMsg);
							thread.setExceptionInfo(errorMsg);
							throw new RestServiceException(thread,
									System.currentTimeMillis() - start,
									errorMsg,
									RestBusinessCode.missing_args.getValue());
						}
						if (strRegionMap.containsKey(strRegion.getTableName())) {
							strRegionMap.get(strRegion.getTableName()).add(
									strRegion);
						} else {
							List<StrRegion> strRegionList = new ArrayList<StrRegion>();
							strRegionList.add(strRegion);
							strRegionMap.put(strRegion.getTableName(),
									strRegionList);
						}
					} else {
						if (strRegion.getRegionId() == null) {
							String errorMsg = "增强字符类域配置id不能为空 ,表名---"
									+ strRegion.getTableName() + ""
									+ sb.toString();
							logger1.error(errorMsg);
							thread.setExceptionInfo(errorMsg);
							throw new RestServiceException(thread,
									System.currentTimeMillis() - start,
									errorMsg,
									RestBusinessCode.missing_args.getValue());
						}
						if (strStongRegionMap.containsKey(strRegion
								.getTableName())) {
							strStongRegionMap.get(strRegion.getTableName())
									.add(strRegion);
						} else {
							List<StrRegion> strStrongRegionList = new ArrayList<StrRegion>();
							strStrongRegionList.add(strRegion);
							strStongRegionMap.put(strRegion.getTableName(),
									strStrongRegionList);
						}

					}
				}
			}

			if (null != configCompile.getIpRegionList()
					&& configCompile.getIpRegionList().size() > 0) {
				for (IpRegion ipRegion : configCompile.getIpRegionList()) {
					if (!ipRegion.getTableName().toUpperCase()
							.equals("DJ_IP_PORT")) {
						ipRegion.setProtocol(0);
					}

					if (ipRegion.getRegionId() == null) {
						String errorMsg = "ip类域配置id不能为空 ,表名---"
								+ ipRegion.getTableName() + "" + sb.toString();
						logger1.error(errorMsg);
						thread.setExceptionInfo(errorMsg);
						throw new RestServiceException(thread,
								System.currentTimeMillis() - start, errorMsg,
								RestBusinessCode.missing_args.getValue());
					}

					if (ipRegionMap.containsKey(ipRegion.getTableName())) {
						ipRegionMap.get(ipRegion.getTableName()).add(ipRegion);
					} else {
						List<IpRegion> ipRegionList = new ArrayList<IpRegion>();
						ipRegionList.add(ipRegion);
						ipRegionMap.put(ipRegion.getTableName(), ipRegionList);
					}
				}
			}

			if (null != configCompile.getNumRegionList()
					&& configCompile.getNumRegionList().size() > 0) {
				for (NumRegion numRegion : configCompile.getNumRegionList()) {
					if (numRegion.getRegionId() == null) {
						String errorMsg = "数值类域配置id不能为空  ,表名---"
								+ numRegion.getTableName() + "" + sb.toString();
						logger1.error(errorMsg);
						thread.setExceptionInfo(errorMsg);
						throw new RestServiceException(thread,
								System.currentTimeMillis() - start, errorMsg,
								RestBusinessCode.missing_args.getValue());
					}

					if (numRegionMap.containsKey(numRegion.getTableName())) {
						numRegionMap.get(numRegion.getTableName()).add(
								numRegion);
					} else {
						List<NumRegion> numRegionList = new ArrayList<NumRegion>();
						numRegionList.add(numRegion);
						numRegionMap.put(numRegion.getTableName(),
								numRegionList);
					}
				}
			}

			if (null != configCompile.getDigestRegionList()
					&& configCompile.getDigestRegionList().size() > 0) {
				for (DigestRegion digestRegion : configCompile
						.getDigestRegionList()) {
					if (digestRegion.getRegionId() == null) {
						String errorMsg = "摘要类域配置id不能为空  ,表名---"
								+ digestRegion.getTableName() + ""
								+ sb.toString();
						logger1.error(errorMsg);
						thread.setExceptionInfo(errorMsg);
						throw new RestServiceException(thread,
								System.currentTimeMillis() - start, errorMsg,
								RestBusinessCode.missing_args.getValue());
					}

					if (digestRegionMap
							.containsKey(digestRegion.getTableName())) {
						digestRegionMap.get(digestRegion.getTableName()).add(
								digestRegion);
					} else {
						List<DigestRegion> digestRegionList = new ArrayList<DigestRegion>();
						digestRegionList.add(digestRegion);
						digestRegionMap.put(digestRegion.getTableName(),
								digestRegionList);
					}
				}
			}
		}

		if (Configurations.getStringProperty("isCommit", "false")
				.equals("true")) {
			int count = 0;
			if (null != configCompileList && configCompileList.size() > 0) {
				count += 1;
			}
			if (null != groupRelationList && groupRelationList.size() > 0) {
				count += 1;
			}
			if (null != strRegionMap && strRegionMap.size() > 0) {
				count += strRegionMap.size();
			}
			if (null != strStongRegionMap && strStongRegionMap.size() > 0) {
				count += strStongRegionMap.size();
			}
			if (null != ipRegionMap && ipRegionMap.size() > 0) {
				count += ipRegionMap.size();
			}
			if (null != numRegionMap && numRegionMap.size() > 0) {
				count += numRegionMap.size();
			}

			if (null != digestRegionMap && digestRegionMap.size() > 0) {
				count += digestRegionMap.size();
			}

			SqlSessionFactory sqlSessionFactory = SpringContextHolder
					.getBean(SqlSessionFactory.class);
			Connection conn = sqlSessionFactory.openSession().getConnection();

			CountDownLatch latch = new CountDownLatch(count);
			SaveCompileByJDBCThread compile = new SaveCompileByJDBCThread(conn,
					latch, start);
			compile.setCompileList(configCompileList);
			Thread compileThread = new Thread(compile);
			compileThread.start();
			if (groupRelationList.size() > 0) {

				SaveCompileByJDBCThread group = new SaveCompileByJDBCThread(
						conn, latch, start);
				group.setGroupList(groupRelationList);

				Thread groupThread = new Thread(group);
				groupThread.start();

			}
			if (strRegionMap.size() > 0) {
				for (String str : strRegionMap.keySet()) {

					SaveCompileByJDBCThread strRegion = new SaveCompileByJDBCThread(
							false, str, conn, latch, start);
					strRegion.setStrRegionList(strRegionMap.get(str));
					Thread strThread = new Thread(strRegion);
					strThread.start();

				}
			}
			if (strStongRegionMap.size() > 0) {
				for (String strStrong : strStongRegionMap.keySet()) {
					SaveCompileByJDBCThread strongRegion = new SaveCompileByJDBCThread(
							true, strStrong, conn, latch, start);
					strongRegion.setStrRegionList(strStongRegionMap
							.get(strStrong));
					Thread strongStrThread = new Thread(strongRegion);
					strongStrThread.start();

				}
			}
			if (ipRegionMap.size() > 0) {
				for (String ip : ipRegionMap.keySet()) {
					SaveCompileByJDBCThread ipRegion = new SaveCompileByJDBCThread(
							ip, conn, latch, start);
					ipRegion.setIpRegionList(ipRegionMap.get(ip));
					Thread ipThread = new Thread(ipRegion);
					ipThread.start();

				}
			}
			if (numRegionMap.size() > 0) {
				for (String num : numRegionMap.keySet()) {
					SaveCompileByJDBCThread numRegion = new SaveCompileByJDBCThread(
							num, conn, latch, start);
					numRegion.setNumRegionList(numRegionMap.get(num));
					Thread numThread = new Thread(numRegion);
					numThread.start();

				}
			}
			if (digestRegionMap.size() > 0) {
				for (String digest : digestRegionMap.keySet()) {
					SaveCompileByJDBCThread digestRegion = new SaveCompileByJDBCThread(
							digest, conn, latch, start);
					digestRegion.setDigestRegionList(digestRegionMap
							.get(digest));
					Thread digestThread = new Thread(digestRegion);
					digestThread.start();

				}
			}

			try {
				latch.await();
				if (msgList.size() > 0) {
					conn.rollback();
					return "error";
				} else {
					conn.commit();
				}
			} catch (Exception e) {
				thread.setExceptionInfo(e.toString());
				e.printStackTrace();
				logger1.error(e);
				if (!(e instanceof RestServiceException)) {
					e = new RestServiceException(thread,
							System.currentTimeMillis() - start, "编译配置数据插入失败",
							RestBusinessCode.unknow_error.getValue());
				}
				throw ((RestServiceException) e);
			}
		}
		return "ok";
	}

	private String convertToTimeStamp16(Date date) {
		// sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return date.getTime() + "000";
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 批量(jdbc多线程方式)修改配置业务逻辑-->1:先进行基础配置验证传过来的json串是否符合格式(配置分组关系前端不能设置为无效,
	 * 程序会根据传过来的编译配置和域配置等信息自动设置为无效).
	 * 2:判断配置在数据库中是否已经是无效了如果是无效则提示不能修改无效的配置为有效(因为如果数据库中的编译配置与配置分组关系以及域配置均是无效
	 * ,传过来的数据是编译配置和配置分组关系是有效,域配置是无效,这样会将数据库中无效的编译配置置为有效,这是不允许的)
	 * 3:如果编译配置置为无效将所有的配置分组关系置为无效,不修改域配置状态(分组复用考虑,域可能被其他的组引用)
	 * 4:如果编译配置为有效,域配置为无效,则先获取该编译配置下各个配置分组关系下的所有有效的域配置信息和所有无效的域配置信息,
	 * 如果数据库中有效的域配置信息和前端传过来置为无效的的域配置信息个数,id都相等,则将对应域配置,配置分组关系,编译配置均置为无效
	 * 
	 * 
	 * 
	 * 20161124讨论暂时先去掉2步骤,因为界面目前传过来的数据编译配置,配置分组关系,域配置均是无效,所以就不执行第2步骤了影响性能(
	 * 方法内注释的代码,如果以后业务需要调整打开下面代码即可)
	 * 
	 * @param thread
	 *            保存异常信息入库对象
	 * @param start
	 *            进入controller的时间
	 * @param compileList
	 *            编译配置集合
	 * @return
	 * @throws Exception
	 */
	public String updateByJDBCThread(SaveRequestLogThread thread, long start,
			List<ConfigCompile> compileList, Date opTime, StringBuffer sb) {
		List<ConfigCompile> compileAllList = new ArrayList<ConfigCompile>();
		List<ConfigGroupRelation> groupRelationAllList = new ArrayList<ConfigGroupRelation>();
		Map<String, List<StrRegion>> strRegionMap = new HashMap<String, List<StrRegion>>();
		Map<String, List<IpRegion>> ipRegionMap = new HashMap<String, List<IpRegion>>();
		Map<String, List<NumRegion>> numRegionMap = new HashMap<String, List<NumRegion>>();
		if (null != compileList && compileList.size() > 0) {
			for (ConfigCompile config : compileList) {
				String msg = CompileVal.compileIsOk(config, true, sb);
				if (!"ok".equals(msg)) {
					thread.setExceptionInfo(msg + sb.toString());
					throw new RestServiceException(thread,
							System.currentTimeMillis() - start, msg
									+ sb.toString(),
							RestBusinessCode.config_integrity_error.getValue());
				}
				List<IpRegion> ipRegionList = config.getIpRegionList();// 获取编译下所有ip类域配置(要修改的数据)
				List<StrRegion> strRegionList = config.getStrRegionList();// 获取编译下所有字符串类域配置(要修改的数据
				List<NumRegion> numRegionList = config.getNumRegionList();// 获取编译下所有数值类域配置(要修改的数据
				List<ConfigGroupRelation> groupRelationList = config
						.getGroupRelationList();
				if (config.getIsValid() == 1) {
					if (groupRelationList == null
							|| groupRelationList.size() == 0) {
						thread.setExceptionInfo("配置分组数量不能为空" + sb.toString());
						throw new RestServiceException(thread,
								System.currentTimeMillis() - start,
								"配置分组数量不能为空" + sb.toString(),
								RestBusinessCode.config_integrity_error
										.getValue());
					}
					if ((ipRegionList == null || ipRegionList.size() == 0)
							&& (strRegionList == null || strRegionList.size() == 0)
							&& (numRegionList == null || numRegionList.size() == 0)) {
						thread.setExceptionInfo("域配置数量不能全部为空" + sb.toString());
						throw new RestServiceException(thread,
								System.currentTimeMillis() - start,
								"域配置数量不能全部为空" + sb.toString(),
								RestBusinessCode.config_integrity_error
										.getValue());
					}
				}

				// ConfigCompile configCompile =
				// configCompileDao.queryConfigCompileById(config.getCompileId());//
				// 先判断该条编译配置数据库中状态是否为无效,如果无效则提示用户不能将无效状态改为有效状态
				// if (configCompile.getIsValid() == 1) {//
				// 数据库中编译配置为有效状态才可以进行修改操作
				/**
				 * 1:编译配置置为无效,需要将其下所有配置分组置为无效,如果数据中将域配置置为了无效则将域配置置为无效,
				 * 否则不修改域配置是否生效标志
				 * 2:编译配置置为有效,不修改编译配置状态(编译配置不能从无效变有效,只能从有效变无效)仅修改编译配置其他属性,
				 */
				List<ConfigGroupRelation> queryCompileGroupByPID = configGroupRelationDao
						.queryCompileGroupByPID(config.getCompileId());

				if (null == queryCompileGroupByPID
						|| queryCompileGroupByPID.size() == 0) {
					throw new RestServiceException(thread,
							System.currentTimeMillis() - start, "配置id为"
									+ config.getCompileId()
									+ "的配置在数据库中找不到对应的配置分组关系,请检查json串是否正确"
									+ sb.toString(),
							RestBusinessCode.config_integrity_error.getValue());
				}

				if (config.getIsValid() == 0) {// 编译配置修改为无效,需要将所有的配置分组置为无效
					if (null != queryCompileGroupByPID
							&& queryCompileGroupByPID.size() > 0) {
						for (ConfigGroupRelation configGroupRelation : queryCompileGroupByPID) {
							configGroupRelation.setIsValid(0);

							groupRelationAllList.add(configGroupRelation);
						}

					}
				} else {

					Map<String, Map<Long, List<Long>>> regionMap = getRegionList(
							thread, System.currentTimeMillis() - start, config,
							queryCompileGroupByPID, sb);
					Map<Long, List<Long>> validMap = regionMap.get("validMap");// 获取该编译配置下所有的配置分组关系及组下有效的域配置id
					Map<Long, List<Long>> noValidMap = regionMap
							.get("noValidMap");// 获取该编译配置下所有的配置分组关系及组下无效的域配置id

					int num = 0;
					for (Long regionId : validMap.keySet()) {// 遍历配置分组
						List<Long> validList = validMap.get(regionId);// 获取配置分组下有效的域配置id
						List<Long> noValidList = noValidMap.get(regionId);// 获取配置分组下无效的域配置id
						int count = 0;
						if (null != ipRegionList && ipRegionList.size() > 0) {
							for (IpRegion ipRegion : ipRegionList) {
								if (ipRegion.getGroupId().equals(regionId)) {
									if (null != validList
											&& validList.contains(ipRegion
													.getRegionId())) {
										if (ipRegion.getIsValid() == 0) {
											ipRegion.setLastUpdate(new Date());
											if (ipRegionMap
													.containsKey(ipRegion
															.getTableName())) {
												ipRegionMap
														.get(ipRegion
																.getTableName())
														.add(ipRegion);
											} else {
												List<IpRegion> ipList = new ArrayList<IpRegion>();
												ipList.add(ipRegion);
												ipRegionMap
														.put(ipRegion
																.getTableName(),
																ipList);
											}
											count++;
										}
									} else if (null != noValidList
											&& noValidList.contains(ipRegion
													.getRegionId())) {
										String errorMsg = "不能修改无效的ip类域配置compile为"
												+ config.getCompileId()
												+ "regionid为"
												+ ipRegion.getRegionId()
												+ sb.toString();
										thread.setExceptionInfo(errorMsg);
										throw new RestServiceException(thread,
												System.currentTimeMillis()
														- start, errorMsg,
												RestBusinessCode.missing_args
														.getValue());
									} else {
										String errorMsg = "找不到对应的的ip类域配置compile为"
												+ config.getCompileId()
												+ "regionid为"
												+ ipRegion.getRegionId()
												+ sb.toString();
										thread.setExceptionInfo(errorMsg);
										throw new RestServiceException(thread,
												System.currentTimeMillis()
														- start, errorMsg,
												RestBusinessCode.missing_args
														.getValue());
									}
								}
							}

						}

						if (null != strRegionList && strRegionList.size() > 0) {
							for (StrRegion strRegion : strRegionList) {
								if (strRegion.getGroupId().equals(regionId)) {
									if (null != validList
											&& validList.contains(strRegion
													.getRegionId())) {
										if (strRegion.getIsValid() == 0) {

											strRegion.setLastUpdate(new Date());
											if (strRegionMap
													.containsKey(strRegion
															.getTableName())) {
												strRegionMap
														.get(strRegion
																.getTableName())
														.add(strRegion);
											} else {
												List<StrRegion> strList = new ArrayList<StrRegion>();
												strList.add(strRegion);
												strRegionMap.put(strRegion
														.getTableName(),
														strList);
											}
											count++;
										}
									} else if (null != noValidList
											&& noValidList.contains(strRegion
													.getRegionId())) {
										String errorMsg = "不能修改无效的字符串类域配置compile为"
												+ config.getCompileId()
												+ "regionid为"
												+ strRegion.getRegionId()
												+ sb.toString();
										thread.setExceptionInfo(errorMsg);
										throw new RestServiceException(thread,
												System.currentTimeMillis()
														- start, errorMsg,
												RestBusinessCode.missing_args
														.getValue());
									} else {
										String errorMsg = "找不到对应的的字符串类域配置compile为"
												+ config.getCompileId()
												+ "regionid为"
												+ strRegion.getRegionId()
												+ sb.toString();
										thread.setExceptionInfo(errorMsg);
										throw new RestServiceException(thread,
												System.currentTimeMillis()
														- start, errorMsg,
												RestBusinessCode.missing_args
														.getValue());
									}
								}
							}

						}
						if (null != numRegionList && numRegionList.size() > 0) {
							for (NumRegion numRegion : numRegionList) {
								if (numRegion.getGroupId().equals(regionId)) {
									if (null != validList
											&& validList.contains(numRegion
													.getRegionId())) {
										if (numRegion.getIsValid() == 0) {

											numRegion.setLastUpdate(new Date());
											if (numRegionMap
													.containsKey(numRegion
															.getTableName())) {
												numRegionMap
														.get(numRegion
																.getTableName())
														.add(numRegion);
											} else {
												List<NumRegion> numList = new ArrayList<NumRegion>();
												numList.add(numRegion);
												numRegionMap.put(numRegion
														.getTableName(),
														numList);
											}
											count++;

										}
									} else if (null != noValidList
											&& noValidList.contains(numRegion
													.getRegionId())) {
										String errorMsg = "不能修改无效的数值类域配置compile为"
												+ config.getCompileId()
												+ "regionid为"
												+ numRegion.getRegionId()
												+ sb.toString();
										thread.setExceptionInfo(errorMsg);
										throw new RestServiceException(thread,
												System.currentTimeMillis()
														- start, errorMsg,
												RestBusinessCode.missing_args
														.getValue());
									} else {
										String errorMsg = "找不到对应的的数值类域配置compile为"
												+ config.getCompileId()
												+ "regionid为"
												+ numRegion.getRegionId()
												+ sb.toString();
										thread.setExceptionInfo(errorMsg);
										throw new RestServiceException(thread,
												System.currentTimeMillis()
														- start, errorMsg,
												RestBusinessCode.missing_args
														.getValue());
									}
								}
							}
						}
						if (count == validList.size()) {// 数据库中当前配置分组下有效的域配置,均被修改为无效,所以要将该配置分组置为无效
							for (ConfigGroupRelation configGroupRelation : groupRelationList) {
								if (configGroupRelation.getGroupId().equals(
										regionId)) {
									num++;
									configGroupRelation.setIsValid(0);

									groupRelationAllList
											.add(configGroupRelation);
								}
							}

						}
					}
					if (num == validMap.size()) {// 所有的域均是无效的所有的组均是无效,需要将编译配置置为无效
						config.setIsValid(0);
					}
				}

				// } else {// 数据库中编译配置为无效状态不能进行操作
				// throw new RestServiceException(thread,
				// System.currentTimeMillis() - start,"不能修改无效的编译配置信息,配置id为" +
				// configCompile.getCompileId(),RestBusinessCode.missing_args.getValue());
				// }
				compileAllList.add(config);
			}

		} else {
			thread.setExceptionInfo("编译配置不能为空" + sb.toString());
			throw new RestServiceException(thread, System.currentTimeMillis()
					- start, "编译配置不能为空" + sb.toString(),
					RestBusinessCode.config_integrity_error.getValue());
		}
		// 程序是否入库,测试分析数据的时间
		if (Configurations.getStringProperty("isCommit", "false")
				.equals("true")) {
			int count = 0;
			if (null != compileAllList && compileAllList.size() > 0) {
				count += 1;
			}
			if (null != groupRelationAllList && groupRelationAllList.size() > 0) {
				count += 1;
			}
			if (null != strRegionMap && strRegionMap.size() > 0) {
				count += strRegionMap.size();
			}
			if (null != ipRegionMap && ipRegionMap.size() > 0) {
				count += ipRegionMap.size();
			}
			if (null != numRegionMap && numRegionMap.size() > 0) {
				count += numRegionMap.size();
			}

			SqlSessionFactory sqlSessionFactory = SpringContextHolder
					.getBean(SqlSessionFactory.class);
			Connection conn = sqlSessionFactory.openSession().getConnection();
			CountDownLatch latch = new CountDownLatch(count);
			UpdateCompileByJDBCThread compileThread = new UpdateCompileByJDBCThread(
					conn, latch, start, opTime);
			compileThread.setCompileList(compileAllList);
			Thread compile = new Thread(compileThread);
			compile.start();

			if (null != groupRelationAllList && groupRelationAllList.size() > 0) {
				UpdateCompileByJDBCThread groupThread = new UpdateCompileByJDBCThread(
						conn, latch, start, opTime);
				groupThread.setGroupList(groupRelationAllList);
				Thread group = new Thread(groupThread);
				group.start();

			}
			if (null != strRegionMap && strRegionMap.size() > 0) {
				for (String str : strRegionMap.keySet()) {
					UpdateCompileByJDBCThread strRegion = new UpdateCompileByJDBCThread(
							str, conn, latch, start, opTime);
					strRegion.setStrRegionList(strRegionMap.get(str));
					Thread strThread = new Thread(strRegion);
					strThread.start();
				}
			}

			if (null != ipRegionMap && ipRegionMap.size() > 0) {
				for (String ip : ipRegionMap.keySet()) {
					UpdateCompileByJDBCThread ipRegion = new UpdateCompileByJDBCThread(
							ip, conn, latch, start, opTime);
					ipRegion.setIpRegionList(ipRegionMap.get(ip));
					Thread ipThread = new Thread(ipRegion);
					ipThread.start();

				}
			}
			if (null != numRegionMap && numRegionMap.size() > 0) {
				for (String num : numRegionMap.keySet()) {
					UpdateCompileByJDBCThread numRegion = new UpdateCompileByJDBCThread(
							num, conn, latch, start, opTime);
					numRegion.setNumRegionList(numRegionMap.get(num));
					Thread numThread = new Thread(numRegion);
					numThread.start();
				}
			}

			try {
				latch.await();
				if (msgList.size() > 0) {
					conn.rollback();
					return "error";
				} else {
					conn.commit();
				}
			} catch (Exception e) {
				thread.setExceptionInfo(e.toString());
				e.printStackTrace();
				logger1.error(e);
				if (!(e instanceof RestServiceException)) {
					e = new RestServiceException(thread,
							System.currentTimeMillis() - start, "编译配置数据修改失败",
							RestBusinessCode.unknow_error.getValue());
				}
				throw ((RestServiceException) e);
			}

		}
		return "ok";
	}

	/**
	 * 批量(jdbc多线程方式)修改配置业务逻辑-->1:先进行基础配置验证传过来的json串是否符合格式(配置分组关系前端不能设置为无效,
	 * 程序会根据传过来的编译配置和域配置等信息自动设置为无效).
	 * 2:判断配置在数据库中是否已经是无效了如果是无效则提示不能修改无效的配置为有效(因为如果数据库中的编译配置与配置分组关系以及域配置均是无效
	 * ,传过来的数据是编译配置和配置分组关系是有效,域配置是无效,这样会将数据库中无效的编译配置置为有效,这是不允许的)
	 * 3:如果编译配置置为无效将所有的配置分组关系置为无效,不修改域配置状态(分组复用考虑,域可能被其他的组引用)
	 * 4:如果编译配置为有效,域配置为无效,则先获取该编译配置下各个配置分组关系下的所有有效的域配置信息和所有无效的域配置信息,
	 * 如果数据库中有效的域配置信息和前端传过来置为无效的的域配置信息个数,id都相等,则将对应域配置,配置分组关系,编译配置均置为无效
	 * 
	 * 
	 * 
	 * 20161124讨论暂时先去掉2步骤,因为界面目前传过来的数据编译配置,配置分组关系,域配置均是无效,所以就不执行第2步骤了影响性能(
	 * 方法内注释的代码,如果以后业务需要调整打开下面代码即可)
	 * 
	 * 
	 * 
	 * 20161215讨论确定修改时前端发过来的配置都是无效的不会存在即有无效又有有效的情况,且配置中的个数与添加时保持一致,
	 * 即添加时插入了1条编译配置2条配置分组关系3条域配置,在修改时也会传过来1条编译配置2条配置分组关系3条域配置,不会多或者少配置
	 * 
	 * 
	 * 
	 * 
	 * 2017-1-16:和杨老师讨论确定存在分组复用情况,当编译配置置为无效,需要将其下所有配置分组置为无效,
	 * 如果数据中将域配置置为了无效则将域配置置为无效,否则不修改域配置是否生效标志
	 * 
	 * @param thread
	 *            保存异常信息入库对象
	 * @param start
	 *            进入controller的时间
	 * @param compileList
	 *            编译配置集合
	 * @return
	 * @throws Exception
	 */
	public String updateByJDBCThread12(SaveRequestLogThread thread, long start,
			List<ConfigCompile> compileList, Date opTime, StringBuffer sb) {
		List<ConfigCompile> compileAllList = new ArrayList<ConfigCompile>();
		List<ConfigGroupRelation> groupRelationAllList = new ArrayList<ConfigGroupRelation>();
		Map<String, List<StrRegion>> strRegionMap = new HashMap<String, List<StrRegion>>();
		Map<String, List<IpRegion>> ipRegionMap = new HashMap<String, List<IpRegion>>();
		Map<String, List<NumRegion>> numRegionMap = new HashMap<String, List<NumRegion>>();
		Map<String, List<DigestRegion>> digestRegionMap = new HashMap<String, List<DigestRegion>>();
		if (null != compileList && compileList.size() > 0) {
			for (ConfigCompile config : compileList) {
				String msg = CompileVal.compileIsOk(config, true, sb);
				if (!"ok".equals(msg)) {
					thread.setExceptionInfo(msg + sb.toString());
					throw new RestServiceException(thread,
							System.currentTimeMillis() - start, msg
									+ sb.toString(),
							RestBusinessCode.config_integrity_error.getValue());
				}

				/**
				 * 1:编译配置置为无效,需要将其下所有配置分组置为无效,如果数据中将域配置置为了无效则将域配置置为无效,
				 * 否则不修改域配置是否生效标志
				 * 2:编译配置置为有效,不修改编译配置状态(编译配置不能从无效变有效,只能从有效变无效)仅修改编译配置其他属性,
				 */
				List<ConfigGroupRelation> groupRelationList = config
						.getGroupRelationList();
				if (config.getIsValid() == 0) {// 编译配置修改为无效,需要将所有的配置分组置为无效
					if (null != groupRelationList
							&& groupRelationList.size() > 0) {
						for (ConfigGroupRelation configGroupRelation : groupRelationList) {
							configGroupRelation.setIsValid(0);
							groupRelationAllList.add(configGroupRelation);
						}
					}
					List<IpRegion> ipRegionList = config.getIpRegionList();// 获取编译下所有ip类域配置(要修改的数据)
					if (null != ipRegionList && ipRegionList.size() > 0) {
						for (IpRegion ipRegion : ipRegionList) {
							if (ipRegion.getIsValid() == 0) {
								if (ipRegionMap.containsKey(ipRegion
										.getTableName())) {
									ipRegionMap.get(ipRegion.getTableName())
											.add(ipRegion);
								} else {
									List<IpRegion> ipList = new ArrayList<IpRegion>();
									ipList.add(ipRegion);
									ipRegionMap.put(ipRegion.getTableName(),
											ipList);
								}
							}
						}
					}
					List<StrRegion> strRegionList = config.getStrRegionList();// 获取编译下所有字符串类域配置(要修改的数据
					if (null != strRegionList && strRegionList.size() > 0) {
						for (StrRegion strRegion : strRegionList) {
							if (strRegion.getIsValid() == 0) {
								if (strRegionMap.containsKey(strRegion
										.getTableName())) {
									strRegionMap.get(strRegion.getTableName())
											.add(strRegion);
								} else {
									List<StrRegion> strList = new ArrayList<StrRegion>();
									strList.add(strRegion);
									strRegionMap.put(strRegion.getTableName(),
											strList);
								}
							}
						}
					}
					List<NumRegion> numRegionList = config.getNumRegionList();// 获取编译下所有数值类域配置(要修改的数据
					if (null != numRegionList && numRegionList.size() > 0) {
						for (NumRegion numRegion : numRegionList) {
							if (numRegion.getIsValid() == 0) {
								if (numRegionMap.containsKey(numRegion
										.getTableName())) {
									numRegionMap.get(numRegion.getTableName())
											.add(numRegion);
								} else {
									List<NumRegion> numList = new ArrayList<NumRegion>();
									numList.add(numRegion);
									numRegionMap.put(numRegion.getTableName(),
											numList);
								}
							}
						}
					}

					List<DigestRegion> digestRegionList = config
							.getDigestRegionList();// 获取编译下所有摘要类域配置(要修改的数据
					if (null != digestRegionList && digestRegionList.size() > 0) {
						for (DigestRegion digestRegion : digestRegionList) {
							if (digestRegion.getIsValid() == 0) {
								if (digestRegionMap.containsKey(digestRegion
										.getTableName())) {
									digestRegionMap.get(
											digestRegion.getTableName()).add(
											digestRegion);
								} else {
									List<DigestRegion> digestList = new ArrayList<DigestRegion>();
									digestList.add(digestRegion);
									digestRegionMap.put(
											digestRegion.getTableName(),
											digestList);
								}
							}
						}
					}
					compileAllList.add(config);
				}
			}

		} else {
			thread.setExceptionInfo("编译配置不能为空" + sb.toString());
			throw new RestServiceException(thread, System.currentTimeMillis()
					- start, "编译配置不能为空" + sb.toString(),
					RestBusinessCode.config_integrity_error.getValue());
		}
		// 程序是否入库,测试分析数据的时间
		if (Configurations.getStringProperty("isCommit", "false")
				.equals("true")) {
			int count = 0;
			if (null != compileAllList && compileAllList.size() > 0) {
				count += 1;
			}
			if (null != groupRelationAllList && groupRelationAllList.size() > 0) {
				count += 1;
			}
			if (null != strRegionMap && strRegionMap.size() > 0) {
				count += strRegionMap.size();
			}
			if (null != ipRegionMap && ipRegionMap.size() > 0) {
				count += ipRegionMap.size();
			}
			if (null != numRegionMap && numRegionMap.size() > 0) {
				count += numRegionMap.size();
			}
			SqlSessionFactory sqlSessionFactory = SpringContextHolder
					.getBean(SqlSessionFactory.class);
			Connection conn = sqlSessionFactory.openSession().getConnection();
			CountDownLatch latch = new CountDownLatch(count);
			UpdateCompileByJDBCThread compileThread = new UpdateCompileByJDBCThread(
					conn, latch, start, opTime);
			compileThread.setCompileList(compileAllList);
			Thread compile = new Thread(compileThread);
			compile.start();

			if (null != groupRelationAllList && groupRelationAllList.size() > 0) {
				UpdateCompileByJDBCThread groupThread = new UpdateCompileByJDBCThread(
						conn, latch, start, opTime);
				groupThread.setGroupList(groupRelationAllList);
				Thread group = new Thread(groupThread);
				group.start();

			}
			if (null != strRegionMap && strRegionMap.size() > 0) {
				for (String str : strRegionMap.keySet()) {
					UpdateCompileByJDBCThread strRegion = new UpdateCompileByJDBCThread(
							str, conn, latch, start, opTime);
					strRegion.setStrRegionList(strRegionMap.get(str));
					Thread strThread = new Thread(strRegion);
					strThread.start();
				}
			}

			if (null != ipRegionMap && ipRegionMap.size() > 0) {
				for (String ip : ipRegionMap.keySet()) {
					UpdateCompileByJDBCThread ipRegion = new UpdateCompileByJDBCThread(
							ip, conn, latch, start, opTime);
					ipRegion.setIpRegionList(ipRegionMap.get(ip));
					Thread ipThread = new Thread(ipRegion);
					ipThread.start();

				}
			}
			if (null != numRegionMap && numRegionMap.size() > 0) {
				for (String num : numRegionMap.keySet()) {
					UpdateCompileByJDBCThread numRegion = new UpdateCompileByJDBCThread(
							num, conn, latch, start, opTime);
					numRegion.setNumRegionList(numRegionMap.get(num));
					Thread numThread = new Thread(numRegion);
					numThread.start();
				}
			}
			if (null != digestRegionMap && digestRegionMap.size() > 0) {
				for (String num : digestRegionMap.keySet()) {
					UpdateCompileByJDBCThread digestRegion = new UpdateCompileByJDBCThread(
							num, conn, latch, start, opTime);
					digestRegion.setDigestRegionList(digestRegionMap.get(num));
					Thread digestThread = new Thread(digestRegion);
					digestThread.start();
				}
			}
			try {
				latch.await();
				if (msgList.size() > 0) {
					conn.rollback();
					return "error";
				} else {
					conn.commit();
				}
			} catch (Exception e) {
				thread.setExceptionInfo(e.toString());
				e.printStackTrace();
				logger1.error(e);
				if (!(e instanceof RestServiceException)) {
					e = new RestServiceException(thread,
							System.currentTimeMillis() - start, "编译配置数据修改失败",
							RestBusinessCode.unknow_error.getValue());
				}
				throw ((RestServiceException) e);
			}

		}
		return "ok";
	}

	/**
	 * 
	 * @Description:只填写compile信息，根据compileId对compile以及与Group的对应关系进行配置状态更新操作
	 * @author (zdx)
	 * @date 2017年8月16日 上午11:21:53
	 * @param thread
	 * @param start
	 * @param compileList
	 * @param opTime
	 * @param sb
	 * @return
	 */
	public String updateByJDBCThread3(SaveRequestLogThread thread, long start,
			List<ConfigCompile> compileList, Date opTime, StringBuffer sb) {
		List<ConfigCompile> compileAllList = new ArrayList<ConfigCompile>();
		if (null != compileList && compileList.size() > 0) {
			for (ConfigCompile config : compileList) {
				String msg = checkCompileOptForUpdate(config);
				if (config.getOpTime() == null) {
					config.setOpTime(opTime);
				}
				if (!msg.equals("ok")) {
					thread.setExceptionInfo(msg + sb.toString());
					throw new RestServiceException(thread,
							System.currentTimeMillis() - start, msg
									+ sb.toString(),
							RestBusinessCode.config_integrity_error.getValue());
				}

				compileAllList.add(config);
			}

		} else {
			thread.setExceptionInfo("编译配置不能为空" + sb.toString());
			throw new RestServiceException(thread, System.currentTimeMillis()
					- start, "编译配置不能为空" + sb.toString(),
					RestBusinessCode.config_integrity_error.getValue());
		}
		// 程序是否入库,测试分析数据的时间
		if (Configurations.getStringProperty("isCommit", "false")
				.equals("true")) {
			int count = 0;
			if (null != compileAllList && compileAllList.size() > 0) {
				count += 1;
				count += 1; // 将所有compile与Group的对应关系都置为失效 zdx20170814
			}

			SqlSessionFactory sqlSessionFactory = SpringContextHolder
					.getBean(SqlSessionFactory.class);
			Connection conn = sqlSessionFactory.openSession().getConnection();
			CountDownLatch latch = new CountDownLatch(count);

			UpdateCompileByJDBCThread compileThread = new UpdateCompileByJDBCThread(
					conn, latch, start, opTime);
			compileThread.setCompileList(compileAllList);
			Thread compile = new Thread(compileThread);
			compile.start();

			UpdateCompileByJDBCThread groupThread = new UpdateCompileByJDBCThread(
					conn, latch, start, opTime);
			groupThread.setCompileList(compileAllList);
			// 仅用做判定线程执行时选择执行方法 zdx20170816
			groupThread.setGroupList(new ArrayList<ConfigGroupRelation>());
			Thread group = new Thread(groupThread);
			group.start();

			try {
				latch.await();
				if (msgList.size() > 0) {
					conn.rollback();
					return "error";
				} else {
					conn.commit();
				}
			} catch (Exception e) {
				thread.setExceptionInfo(e.toString());
				e.printStackTrace();
				logger1.error(e);
				if (!(e instanceof RestServiceException)) {
					e = new RestServiceException(thread,
							System.currentTimeMillis() - start, "编译配置数据修改失败",
							RestBusinessCode.unknow_error.getValue());
				}
				throw ((RestServiceException) e);
			}

		}
		return "ok";
	}

	/**
	 * 
	 * @Description:验证compileId+isValid
	 * @author (zdx)
	 * @date 2017年8月16日 上午11:48:46
	 * @param config
	 * @return
	 */
	private String checkCompileOptForUpdate(ConfigCompile config) {
		
		if (StringUtil.isEmpty(config.getCompileId())) {
			CompileVal.setBusinessCode(RestBusinessCode.CompileIdIsNull.getValue());
			return RestBusinessCode.CompileIdIsNull.getErrorReason();
		}

		if (StringUtil.isEmpty(config.getService())) {
			
			CompileVal.setBusinessCode(RestBusinessCode.ServiceIsNull.getValue());
			return RestBusinessCode.ServiceIsNull.getErrorReason();
		}

		if (StringUtil.isEmpty(config.getIsValid())) {
			CompileVal.setBusinessCode(RestBusinessCode.IsValidIsNull.getValue());
			return "编译配置id为" + config.getCompileId() + "的IsValid字段不能为空";
		}
		if (config.getIsValid() != 0) {
			CompileVal.setBusinessCode(RestBusinessCode.IsValidIsF.getValue());
			return "编译配置id为" + config.getCompileId() + "的配置在修改时不能为有效";
		}

		return "ok";
	}

	/**
	 * 获取编译配置下所有的配置分组关系对应的有效,无效域配置id
	 * 
	 * @param thread
	 *            保存异常信息入库对象
	 * @param start
	 *            进入controller的时间
	 * @param config
	 *            编译配置对象
	 * @param queryCompileGroupByPID
	 *            当前编译下所有的配置分组关系
	 * @return
	 */
	public Map<String, Map<Long, List<Long>>> getRegionList(
			SaveRequestLogThread thread, long start, ConfigCompile config,
			List<ConfigGroupRelation> queryCompileGroupByPID, StringBuffer sb) {
		Map<String, Map<Long, List<Long>>> map = new HashMap<String, Map<Long, List<Long>>>();
		Map<Integer, Map<String, String>> tableRelationMap = getTableRelation();

		Map<String, String> updateTableMap = tableRelationMap.get(config
				.getService().intValue());// 根据service获取需要操作的域配置表
		if (null == updateTableMap || updateTableMap.size() == 0) {
			thread.setExceptionInfo("找不到编译对应的service" + sb.toString());
			throw new RestServiceException(thread, System.currentTimeMillis()
					- start, "找不到编译对应的service" + sb.toString(),
					RestBusinessCode.missing_args.getValue());
		}

		Long[] groupId = new Long[queryCompileGroupByPID.size()];
		for (int i = 0; i < queryCompileGroupByPID.size(); i++) {
			groupId[i] = queryCompileGroupByPID.get(i).getGroupId();
		}
		// 获取当前编译下所有的域配置信息
		List<IpRegion> ipRegionExistList = new ArrayList<IpRegion>();
		List<StrRegion> strRegionExistList = new ArrayList<StrRegion>();
		List<NumRegion> numRegionExistList = new ArrayList<NumRegion>();
		for (String tableName : updateTableMap.keySet()) {
			if (updateTableMap.get(tableName).equals("ip")) {
				ipRegionExistList.addAll(ipRegionDao.queryIpRegionByGroupId(
						tableName, groupId));
			}
			if (updateTableMap.get(tableName).equals("num")) {
				numRegionExistList.addAll(numRegionDao.queryNumRegionByGroupId(
						tableName, groupId));
			}
			if (updateTableMap.get(tableName).equals("str")) {
				strRegionExistList.addAll(strRegionDao.queryStrRegionByGroupId(
						tableName, groupId));
			}
		}

		Map<Long, List<Long>> validMap = new HashMap<Long, List<Long>>();// 获取当前编译下所有的组,及每个组下面的有效域配置
		Map<Long, List<Long>> noValidMap = new HashMap<Long, List<Long>>();// 获取当前编译下所有的组,及每个组下面的无效域配置
		for (IpRegion ipRegion : ipRegionExistList) {
			if (ipRegion.getIsValid() == 1) {
				List<Long> validRegionList = null;
				if (validMap.get(ipRegion.getGroupId()) == null) {
					validRegionList = new ArrayList<Long>();
					validRegionList.add(ipRegion.getRegionId());
					validMap.put(ipRegion.getGroupId(), validRegionList);
				} else {
					validRegionList = validMap.get(ipRegion.getGroupId());
					// if (!validRegionList.contains(ipRegion.getRegionId())) {
					validRegionList.add(ipRegion.getRegionId());
					validMap.put(ipRegion.getGroupId(), validRegionList);
					// }
				}
			}
			if (ipRegion.getIsValid() == 0) {
				List<Long> noValidRegionList = null;
				if (noValidMap.get(ipRegion.getGroupId()) == null) {
					noValidRegionList = new ArrayList<Long>();
					noValidRegionList.add(ipRegion.getRegionId());
					noValidMap.put(ipRegion.getGroupId(), noValidRegionList);
				} else {
					noValidRegionList = noValidMap.get(ipRegion.getGroupId());
					// if (!noValidRegionList.contains(ipRegion.getRegionId()))
					// {
					noValidRegionList.add(ipRegion.getRegionId());
					noValidMap.put(ipRegion.getGroupId(), noValidRegionList);
					// }
				}
			}
		}

		for (StrRegion strRegion : strRegionExistList) {
			if (strRegion.getIsValid() == 1) {
				List<Long> validRegionList = null;
				if (validMap.get(strRegion.getGroupId()) == null) {
					validRegionList = new ArrayList<Long>();

					validRegionList.add(strRegion.getRegionId());

					validMap.put(strRegion.getGroupId(), validRegionList);
				} else {
					validRegionList = validMap.get(strRegion.getGroupId());
					// if (!validRegionList.contains(strRegion.getRegionId())) {
					validRegionList.add(strRegion.getRegionId());
					validMap.put(strRegion.getGroupId(), validRegionList);

					// }
				}
			}
			if (strRegion.getIsValid() == 0) {
				List<Long> noValidRegionList = null;
				if (noValidMap.get(strRegion.getGroupId()) == null) {
					noValidRegionList = new ArrayList<Long>();
					noValidRegionList.add(strRegion.getRegionId());
					noValidMap.put(strRegion.getGroupId(), noValidRegionList);
				} else {
					noValidRegionList = noValidMap.get(strRegion.getGroupId());
					// if (!noValidRegionList.contains(strRegion.getRegionId()))
					// {
					noValidRegionList.add(strRegion.getRegionId());
					noValidMap.put(strRegion.getGroupId(), noValidRegionList);
					// }
				}
			}
		}
		for (NumRegion numRegion : numRegionExistList) {
			if (numRegion.getIsValid() == 1) {
				List<Long> validRegionList = null;
				if (validMap.get(numRegion.getGroupId()) == null) {
					validRegionList = new ArrayList<Long>();
					validRegionList.add(numRegion.getRegionId());
					validMap.put(numRegion.getGroupId(), validRegionList);

				} else {
					validRegionList = validMap.get(numRegion.getGroupId());
					// if (!validRegionList.contains(numRegion.getRegionId())) {
					validRegionList.add(numRegion.getRegionId());
					validMap.put(numRegion.getGroupId(), validRegionList);
					// }

				}
			}
			if (numRegion.getIsValid() == 0) {
				List<Long> noValidRegionList = null;
				if (noValidMap.get(numRegion.getGroupId()) == null) {
					noValidRegionList = new ArrayList<Long>();
					noValidRegionList.add(numRegion.getRegionId());
					noValidMap.put(numRegion.getGroupId(), noValidRegionList);
				} else {
					noValidRegionList = noValidMap.get(numRegion.getGroupId());
					// if (!noValidRegionList.contains(numRegion.getRegionId()))
					// {
					noValidRegionList.add(numRegion.getRegionId());
					noValidMap.put(numRegion.getGroupId(), noValidRegionList);
					// }
				}
			}
		}
		map.put("noValidMap", noValidMap);
		map.put("validMap", validMap);
		return map;

	}

	// ////////////////////////////////////////////////////////////////////////////

	public String configModifySources(List<ConfigCompile> configCompileList)
			throws Exception {

		for (ConfigCompile configCompile : configCompileList) {
			Long compileId = configCompile.getCompileId();
			if (null != compileId) {
				configCompileDao.updateConfigCompile(configCompile);
			} else {
				return "idIsNull";
			}
		}
		return "ok";
	}

	public String saveCommonSources(SaveRequestLogThread thread, long start,
			String jsonString, StringBuffer sb) {
		CompileVal.setBusinessCode(null);
		JsonArray jsonObjectList = new JsonParser().parse(jsonString)
				.getAsJsonArray();
		Map<Integer, List<Map<String, String>>> dstMaps = new HashMap<Integer, List<Map<String, String>>>();
		for (int i = 0; i < jsonObjectList.size(); i++) {
			JsonObject jsonObj = (JsonObject) jsonObjectList.get(i);
			Map<String, Object> srcMap = JSONObject.fromObject(JSONObject
					.fromObject((jsonObj.toString())));
			if (srcMap.containsKey("service")) {
				Map<String, String> dstMap = new HashMap<String, String>();
				List<CommonSourceFieldCfg> commonSourceFieldCfgList = ReadCommSourceXmlUtil
						.getCommonSourceCfgByService(srcMap.get("service")
								.toString().trim());
				if (StringUtil.isEmpty(commonSourceFieldCfgList)) {
					logger1.error(RestBusinessCode.ServiceIsWrong.getErrorReason()+",请检查service配置是否正确");
					thread.setExceptionInfo("请检查service配置是否正确");
					throw new RestServiceException(thread,
							System.currentTimeMillis() - start,
							"请检查service配置是否正确",
							RestBusinessCode.ServiceIsWrong.getValue());
				}
				// 获取IP类型
				Integer ipType = null;
				String ipTypeName = "";
				
				Integer action = null;
				for (CommonSourceFieldCfg commonSourceFieldCfg : commonSourceFieldCfgList) {
					if (commonSourceFieldCfg.getDstName().equals("addr_type")) {
						String dstVal = srcMap.get(
								commonSourceFieldCfg.getSrcName()).toString();
						ipTypeName = commonSourceFieldCfg.getSrcName();
						// regexp 特殊格式正则验证
						Boolean valFlag = true;
						if (!StringUtil.isEmpty(commonSourceFieldCfg
								.getRegexp())) {
							Pattern pattern = Pattern
									.compile(commonSourceFieldCfg.getRegexp());
							Matcher matcher = pattern.matcher(dstVal);
							valFlag = valFlag & matcher.matches();
						}
						if (!valFlag) {
							logger1.error(RestBusinessCode.IpTypeIsWrong
									.getErrorReason());
							thread.setExceptionInfo(RestBusinessCode.IpTypeIsWrong
									.getErrorReason());
							throw new RestServiceException(thread,
									System.currentTimeMillis() - start,
									RestBusinessCode.IpTypeIsWrong
											.getErrorReason(),
									RestBusinessCode.IpTypeIsWrong
											.getValue());
						}
						ipType = Integer.parseInt(dstVal);
					}
					//验证service与action是否匹配
					if (commonSourceFieldCfg.getDstName().equals("action")) {
						String dstVal = srcMap.get(
								commonSourceFieldCfg.getSrcName()).toString().trim();
						if (Integer.valueOf(dstVal).compareTo(ServiceAndRDBIndexReal.getActionByService(Integer.valueOf(srcMap.get("service")
								.toString().trim())))!=0) {
							logger1.error(RestBusinessCode.ServiceUnmatchAction
									.getErrorReason());
							thread.setExceptionInfo(RestBusinessCode.ServiceUnmatchAction
									.getErrorReason());
							throw new RestServiceException(thread,
									System.currentTimeMillis() - start,
									RestBusinessCode.ServiceUnmatchAction
											.getErrorReason(),
									RestBusinessCode.ServiceUnmatchAction
											.getValue());
						}
					}
				}
				if (ipType==null) {
					ipType = 4;
				}
				for (CommonSourceFieldCfg commonSourceFieldCfg : commonSourceFieldCfgList) {
					// 是否必填
					if (commonSourceFieldCfg.getIsRequired()
							&& !srcMap.containsKey(commonSourceFieldCfg
									.getSrcName())) {
						logger1.error(commonSourceFieldCfg.getSrcName()
								+ "参数不能为空");
						thread.setExceptionInfo(commonSourceFieldCfg
								.getSrcName() + "参数不能为空");
						throw new RestServiceException(thread,
								System.currentTimeMillis() - start,
								commonSourceFieldCfg.getSrcName() + "参数不能为空",
								RestBusinessCode.FieldIsNull.getValue());
					}
					// 字段类型 String Number Date Ip Port
					String dstStr = StringUtil.isEmpty(srcMap
							.get(commonSourceFieldCfg.getSrcName())) ? commonSourceFieldCfg
							.getDefaultVal() : srcMap.get(
							commonSourceFieldCfg.getSrcName()).toString();
					if (!StringUtil.isEmpty(dstStr) && dstStr.startsWith("[")
							&& dstStr.endsWith("]")) {
						dstStr = srcMap.get(
								dstStr.substring(1, dstStr.length() - 1))
								.toString();
					}
					
					if("dstFile".equals(commonSourceFieldCfg
							.getSrcName())){
						if ("dst_file".equals(commonSourceFieldCfg
							.getDstName())) {
							String maatTableName = ServiceAndRDBIndexReal
									.getUnMaatTableName(Integer.valueOf(srcMap.get("service")
											.toString().trim()));
							dstStr="/home/mesasoft/"+maatTableName.substring(maatTableName.lastIndexOf("_")+1)+"/full/"+dstStr.substring(dstStr.lastIndexOf("/")+1);
						}else if ("file_id".equals(commonSourceFieldCfg
							.getDstName())) {
							dstStr= dstStr.substring(dstStr.indexOf("/group"));
						}
					}
					switch (commonSourceFieldCfg.getFieldType()) {
					case "Number":
						if (!StringUtil.isNumeric(dstStr)) {
							logger1.error(commonSourceFieldCfg.getSrcName()
									+ "参数不能格式不正确,必需是数值型");
							thread.setExceptionInfo(commonSourceFieldCfg
									.getSrcName() + "参数不能格式不正确,必需是数值型");
							throw new RestServiceException(thread,
									System.currentTimeMillis() - start,
									commonSourceFieldCfg.getSrcName()
											+ "参数不能格式不正确,必需是数值型",
									RestBusinessCode.MastNumberic.getValue());
						}
						break;
					case "Date":
						try {
							// sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
							System.out.println("--------------------TimeZone:"
									+ sdf.getTimeZone());
							Date date = sdf.parse(dstStr);
							dstStr = date.getTime() + "000";
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							logger1.error(commonSourceFieldCfg.getSrcName()
									+ "参数格式不正确，必须是日期型");
							thread.setExceptionInfo(commonSourceFieldCfg
									.getSrcName() + "参数格式不正确，必须是日期型");
							throw new RestServiceException(thread,
									System.currentTimeMillis() - start,
									commonSourceFieldCfg.getSrcName()
											+ "参数不能格式不正确，必须是日期型",
											RestBusinessCode.MastDate.getValue());
						}
						break;
					case "IP":
						if (!BasicProvingUtil.isIpOrIpMask(dstStr, ipType)) {
							logger1.error(commonSourceFieldCfg.getSrcName()
									+ "参数格式不正确或与" + ipTypeName + "不一致");
							thread.setExceptionInfo(commonSourceFieldCfg
									.getSrcName()
									+ "参数格式不正确或与"
									+ ipTypeName
									+ "不一致");
							throw new RestServiceException(thread,
									System.currentTimeMillis() - start,
									commonSourceFieldCfg.getSrcName()
											+ "参数格式不正确或与" + ipTypeName + "不一致",
											RestBusinessCode.IpIsUnMatchType.getValue());
						}
						break;
					case "Port":
						if (!BasicProvingUtil.isPortOrPortMask(dstStr)) {
							logger1.error(commonSourceFieldCfg.getSrcName()
									+ "参数格式不正确，不是合法的Port");
							thread.setExceptionInfo(commonSourceFieldCfg
									.getSrcName() + "参数格式不正确，不是合法的Port");
							throw new RestServiceException(thread,
									System.currentTimeMillis() - start,
									commonSourceFieldCfg.getSrcName()
											+ "参数不能格式不正确，不是合法的Port",
											RestBusinessCode.PortIsNotVal.getValue());
						}
						break;

					}
					// range取值范围验证
					if (!StringUtil.isEmpty(commonSourceFieldCfg.getRange())) {
						String[] range = commonSourceFieldCfg.getRange().split(
								"-");
						Boolean flag = false;
						if (range.length == 2) {
							if (!(Long.valueOf(range[0]).compareTo(
									Long.valueOf(dstStr)) <= 0 && Long.valueOf(
									range[1]).compareTo(Long.valueOf(dstStr)) >= 0)) {
								flag = true;
							}

						} else if (commonSourceFieldCfg.getRange().startsWith(
								"-")) {// 只有最大值限制
							if (Long.valueOf(range[0]).compareTo(
									Long.valueOf(dstStr)) <= 0) {
								flag = true;
							}
						} else if (commonSourceFieldCfg.getRange()
								.endsWith("-")) {// 只有最小值限制
							if (Long.valueOf(range[0]).compareTo(
									Long.valueOf(dstStr)) >= 0) {
								flag = true;
							}
						}
						if (flag) {
							logger1.error(commonSourceFieldCfg.getSrcName()
									+ "参数不在有效范围("
									+ commonSourceFieldCfg.getRange() + ")");
							thread.setExceptionInfo(commonSourceFieldCfg
									.getSrcName()
									+ "参数不在有效范围("
									+ commonSourceFieldCfg.getRange() + ")");
							throw new RestServiceException(thread,
									System.currentTimeMillis() - start,
									commonSourceFieldCfg.getSrcName()
											+ "参数不在有效范围("
											+ commonSourceFieldCfg.getRange()
											+ ")",
											RestBusinessCode.ValueInWrongRange.getValue());
						}
					}
					// regexp 特殊格式正则验证
					Boolean valFlag = true;
					if (!StringUtil.isEmpty(commonSourceFieldCfg.getRegexp())) {
						Pattern pattern = Pattern.compile(commonSourceFieldCfg
								.getRegexp());
						Matcher matcher = pattern.matcher(dstStr);
						valFlag = valFlag & matcher.matches();
					}
					if (valFlag) {
						dstMap.put(commonSourceFieldCfg.getDstName(), dstStr);
					} else {
						logger1.error(commonSourceFieldCfg.getSrcName()
								+ "参数格式与正则(" + commonSourceFieldCfg.getRegexp()
								+ ")不匹配");
						thread.setExceptionInfo(commonSourceFieldCfg
								.getSrcName()
								+ "参数格式与正则("
								+ commonSourceFieldCfg.getRegexp() + ")不匹配");
						throw new RestServiceException(thread,
								System.currentTimeMillis() - start,
								commonSourceFieldCfg.getSrcName() + "参数格式与正则("
										+ commonSourceFieldCfg.getRegexp()
										+ ")不匹配",
										RestBusinessCode.ValueInWrongRegexp.getValue());
					}
				}
				if (StringUtil.isEmpty(dstMaps.get(Integer.valueOf(srcMap.get(
						"service").toString())))) {
					List<Map<String, String>> list = new ArrayList<Map<String, String>>();
					list.add(dstMap);
					dstMaps.put(
							Integer.valueOf(srcMap.get("service").toString()),
							list);
				} else {
					List<Map<String, String>> list = dstMaps.get(Integer
							.valueOf(srcMap.get("service").toString()));
					list.add(dstMap);

				}
			} else {
				thread.setExceptionInfo("service参数不能为空");
				throw new RestServiceException(thread,
						System.currentTimeMillis() - start, "service参数不能为空",
						RestBusinessCode.ServiceIsNull.getValue());
			}
		}
		logger1.info("------------------调用非maat配置新增接口-------------------");
		// 按service分库
		Map<Integer, List<Map<String, String>>> configMap = new HashMap<Integer, List<Map<String, String>>>();
		Iterator serviceIterator = dstMaps.keySet().iterator();
		while (serviceIterator.hasNext()) {
			Integer service = Integer
					.valueOf(serviceIterator.next().toString());
			List<Integer> dbIndexList = ServiceAndRDBIndexReal
					.getRedisDBByService(service);
			if (!StringUtil.isEmpty(dbIndexList) && dbIndexList.size() > 0) {
				for (Integer dbIndex : dbIndexList) {
					if (configMap.containsKey(dbIndex)) {
						configMap.get(dbIndex).addAll(dstMaps.get(service));
					} else {
						List<Map<String, String>> list = new ArrayList<Map<String, String>>();
						list.addAll(dstMaps.get(service));
						configMap.put(dbIndex, list);
					}
				}
			} else {
				RuntimeException e = new RuntimeException(
						"service与写入数据库序号映射关系不存在");
				CompileVal.setBusinessCode(RestBusinessCode.service_runtime_error.getValue());
				msgList.add(e);
				return "error";
			}

		}
		try {
			configRedisService.saveUnMaatConfig(configMap);
		} catch (Exception e) {
			// TODO: handle exception
			logger1.error(e.getMessage());
			CompileVal.setBusinessCode(RestBusinessCode.unknow_error.getValue());
			if (e.getMessage().startsWith("后台错误:")) {
				CompileVal.setBusinessCode(RestBusinessCode.service_runtime_error.getValue());
			}
			msgList.add(e);
			return "error";
		}

		return "ok";
	}

	public String updateCommonSources(SaveRequestLogThread thread, long start,
			String jsonString, Date opTime, StringBuffer sb) {
		JsonArray jsonObjectList = null;
		try {
			jsonObjectList = new JsonParser().parse(jsonString)
					.getAsJsonArray();
		} catch (Exception e) {
			// TODO: handle exception
			logger1.error(e.getMessage());
			CompileVal.setBusinessCode(RestBusinessCode.service_runtime_error.getValue());
			thread.setExceptionInfo(e.getMessage() + sb.toString());
			throw new RestServiceException(thread, System.currentTimeMillis()
					- start, e.getMessage() + sb.toString(),
					CompileVal.getBusinessCode());
		}
		// <service,cfgIdList>
		Map<Integer, List<Long>> cfgMap = new HashMap<Integer, List<Long>>();
		for (int i = 0; i < jsonObjectList.size(); i++) {
			JsonObject jsonObj = (JsonObject) jsonObjectList.get(i);
			Map<String, Object> srcMap = JSONObject.fromObject(JSONObject
					.fromObject((jsonObj.toString())));
			String msg = checkOptForUpdate(srcMap);
			if (!msg.equals("ok")) {
				thread.setExceptionInfo(msg + sb.toString());
				throw new RestServiceException(thread,
						System.currentTimeMillis() - start,
						msg + sb.toString(),
						CompileVal.getBusinessCode());
			}
			Integer service = Integer.valueOf(srcMap.get("service").toString());
			Long cfgId = Long.valueOf(srcMap.get("cfgId").toString());
			if (cfgMap.containsKey(service)) {
				cfgMap.get(service).add(cfgId);
			} else {
				List<Long> idList = new ArrayList<Long>();
				idList.add(cfgId);
				cfgMap.put(service, idList);
			}
		}

		Map<Integer, Map<Integer, List<Long>>> restMap = new HashMap<Integer, Map<Integer, List<Long>>>();
		Iterator serviceIterator = cfgMap.keySet().iterator();
		while (serviceIterator.hasNext()) {
			Integer service = Integer
					.valueOf(serviceIterator.next().toString());
			List<Integer> dbIndexList = ServiceAndRDBIndexReal
					.getRedisDBByService(service);
			if (!StringUtil.isEmpty(dbIndexList) && dbIndexList.size() > 0) {
				for (Integer dbIndex : dbIndexList) {
					if (restMap.containsKey(dbIndex)) {
						restMap.get(dbIndex).put(service, cfgMap.get(service));
					} else {
						Map<Integer, List<Long>> map = new HashMap<Integer, List<Long>>();
						map.put(service, cfgMap.get(service));
						restMap.put(dbIndex, map);
					}
				}
			} else {
				RuntimeException e = new RuntimeException(
						"service与写入数据库序号映射关系不存在");
				CompileVal.setBusinessCode(RestBusinessCode.service_runtime_error.getValue());
				msgList.add(e);
				return "error";
			}
		}
		try {
			configRedisService.delUnMaatConfig(restMap);
		} catch (Exception e) {
			// TODO: handle exception
			logger1.error(e.getMessage());
			CompileVal.setBusinessCode(RestBusinessCode.unknow_error.getValue());
			if (e.getMessage().startsWith("后台错误:")) {
				CompileVal.setBusinessCode(RestBusinessCode.service_runtime_error.getValue());
			}
			msgList.add(e);
			return "error";
		}
		return "ok";
	}

	private String checkOptForUpdate(Map<String, Object> srcMap) {
		String cfgId = srcMap.get("cfgId").toString();
		String service = srcMap.get("service").toString();
		String isValid = srcMap.get("isValid").toString();
		if (StringUtil.isEmpty(cfgId)) {
			CompileVal.setBusinessCode(RestBusinessCode.CfgIdIsNull.getValue());
			return RestBusinessCode.CfgIdIsNull.getErrorReason();
		} else if (!StringUtil.isNumeric(cfgId)) {
			CompileVal.setBusinessCode(RestBusinessCode.MastNumberic.getValue());
			return "cfgId字段格式不正确,"+RestBusinessCode.MastNumberic.getErrorReason();
		}

		if (StringUtil.isEmpty(service)) {
			CompileVal.setBusinessCode(RestBusinessCode.ServiceIsNull.getValue());
			return RestBusinessCode.ServiceIsNull.getErrorReason();
		} else if (!StringUtil.isNumeric(service)) {
			CompileVal.setBusinessCode(RestBusinessCode.MastNumberic.getValue());
			return "service字段格式不正确,"+RestBusinessCode.MastNumberic.getErrorReason();
		}

		if (StringUtil.isEmpty(isValid)) {
			CompileVal.setBusinessCode(RestBusinessCode.IsValidIsNull.getValue());
			return "配置id为" + srcMap.get("cfgId") + "的isValid字段不能为空";
		} else {
			if (!StringUtil.isNumeric(service)) {
				CompileVal.setBusinessCode(RestBusinessCode.IsValidInWrongRange.getValue());
				return RestBusinessCode.IsValidInWrongRange.getErrorReason();
			} else if (!isValid.equals("0")) {
				CompileVal.setBusinessCode(RestBusinessCode.IsValidIsF.getValue());
				return RestBusinessCode.IsValidIsF.getErrorReason();
			}
		}
		return "ok";
	}

	private boolean isIp(String ipStr) {
		if (!(BasicProvingUtil.isIpOrIpMask(ipStr, 4) || BasicProvingUtil
				.isIpOrIpMask(ipStr, 6))) {
			return false;
		}
		return true;

	}

	public String setCompileInvalid(ConfigCompile configCompile)
			throws Exception {
		configCompileDao.setCompileInvalid(configCompile.getCompileId(), 0);
		return "ok";
	}

	public String setCompileValid(ConfigCompile configCompile) throws Exception {
		configCompileDao.setCompileInvalid(configCompile.getCompileId(), 1);
		return "ok";
	}

	public String getDigestGen(String realPath, String filePath)
			throws Exception {
		System.out.println("----------------getDigestGen");
		String content = "";
		String digestGenToolPath = Constants.DIGEST_GEN_TOOL_PATH;
		String chmodCommond = "chmod +x " + realPath + digestGenToolPath;// 执行权限命令
		// System.out.println("----------------chmodCommod:"+chmodCommond);
		String commondStr = realPath + digestGenToolPath + " -f " + filePath; // 执行
		// 执行摘要获取命令 digest -f /home/aa.txt
		// System.out.println("------------commondStr:"+commondStr);
		Runtime.getRuntime().exec(chmodCommond);
		Process p = Runtime.getRuntime().exec(commondStr);
		byte[] b = new byte[1024];
		StringBuffer sb = new StringBuffer();
		while (p.getInputStream().read(b) != -1) {
			sb.append(new String(b, "UTF-8"));
		}
		content = sb.toString();
		if (!StringUtil.isBlank(content)) {
			content = StringUtil.stripAll(content);
		}
		// System.out.println("-------------------->>"+content);
		String[] digestGenReslt = content.split(" ");
		if (digestGenReslt.length >= 4
				&& !StringUtil.isEmpty(digestGenReslt[3])) {
			return digestGenReslt[3];
		}
		return null;
	}

}

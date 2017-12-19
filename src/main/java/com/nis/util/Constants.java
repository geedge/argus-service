package com.nis.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class Constants {

	public static final String DEFAULT_CAPTCHA_PARAM = "captcha";
	public static final String DEFAULT_MOBILE_PARAM = "mobileLogin";
	public static final String DEFAULT_MESSAGE_PARAM = "message";

	/**
	 * 词典数据key
	 */
	public static final String CACHE_DICT_MAP = "dictMap";

	/**
	 * 上传文件基础虚拟路径
	 */
	public static final String USERFILES_BASE_URL = "/userfiles/";

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;

	public static final int LOG_ACCESS_SUCCESS = 1;
	public static final int LOG_ACCESS_EXCEPTION = 0;
	/**
	 * 默认未知方法（未添加词典或未识别）操作类型值为：unknown（8000）
	 */
	public static final int DEFAULT_METHOD_TYPE = 8000;

	public static final String SYS_BUSINESS_MENU_NAME = "信访管理";

	public static final String LABEL_DEFAULT = "label";
	public static final String LABEL_SUCCESS = "label label-success";
	public static final String LABEL_WARNING = "label label-warning";
	public static final String LABEL_IMPORTANT = "label label-important";
	public static final String LABEL_INFO = "label label-info";
	public static final String LABEL_INVERSE = "label label-inverse";
	/**
	 * 生效系统
	 */
	public static final String ACTIVESYS_A = "4";
	public static final String ACTIVESYS_B = "2";
	public static final String ACTIVESYS_C = "1";
	public static final String ACTIVESYS_ALL = "7";
	//A+B版
	public static final String ACTIVESYS_AB = "6";
	/**
	 * 数据库操作
	 */
	public static final String INSERT = "I";
	public static final String UPDATE = "U";
	public static final String DELETE = "D";
	/**
	 * 接口的操作行为opAction
	 */
	public static final int OPACTION_POST = 1;
	public static final int OPACTION_PUT = 2;
	public static final int OPACTION_DELETE = 3;
	public static final int OPACTION_GET = 4;
	/**
	 * 是/否
	 */
	public static final String YES = "1";
	public static final String NO = "0";
	/**
	* 每页最大显示数
	*/
	public static final int MAX_PAGE_SIZE = Configurations.getIntProperty("maxPageSize", 100000);
	
	/**
	 * 对/错
	 */
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	/**
	 * 服务器ip
	 */
	public static String SERVCER_HOST = null;
	/**
	 * oracle redis数据 存储时间
	 */
	public static final int ORACLE_EXPIRE = Configurations.getIntProperty("oracleExpire", 180);
	/**
	 * hive redis数据 存储时间
	 */
	public static final int HIVE_EXPIRE = Configurations.getIntProperty("hiveExpire", 180);
	/**
	 * redis开关
	 */
	public static final boolean IS_OPEN_REDIS = Configurations.getBooleanProperty("isOpenRedis", false);
	/**
	 * es开关
	 */
	public static final boolean IS_USE_ES = Configurations.getBooleanProperty("isUseES", false);

	/**
	 * 数据中心日志redis开关
	 */
	public static final boolean DATACENTER_OPEN_REDIS = Configurations.getBooleanProperty("dataCenterOpenRedis", false);
	/**
	 * 是否使用use soq_log命令
	 */
	// public static final boolean IS_USE_HIVE_DB =
	// Configurations.getBooleanProperty("isUseHiveDb", true);

	/**
	 * 是否获取数据中心查询记录的总条数
	 */

	public static final boolean IS_GET_HIVECOUNT = Configurations.getBooleanProperty("isGetHiveCount", true);
	/**
	 * 是否获取数据中心[神通]查询记录的总条数
	 */
	
	public static final boolean IS_SELECT_CLUSTER = Configurations.getBooleanProperty("isSelectCluster", false);
	
	/**
	 * 神通数据库A的数据最早时间
	 */
	public static final Long CLUSTER_A_START_TIME =  Configurations.getLongProperty("clusterAStartTime", new Date().getTime());

	/**
	 * 神通数据库B的数据最早时间
	 */
	public static final Long CLUSTER_B_START_TIME = Configurations.getLongProperty("clusterBStartTime", new Date().getTime());
	
	/**
	 * 每次获取数据中心多少条数据,咱们在对获取的数据进行分页处理
	 */
	public static final Long EVERY_GETHIVEDATANUM = Configurations.getLongProperty("everyGetHiveDataNum", 10000);

	/**
	 * 是否开启基础校验
	 */
	public static final boolean BASE_VALIDATE = Configurations.getBooleanProperty("baseValidate", true);

	public static final Long DATACENTER_TIME = Configurations.getLongProperty("dataCenterTime", 48);

	/**
	 * 是否开启业务校验
	 */
	public static final boolean SERVICE_VALIDATE = Configurations.getBooleanProperty("serviceValidate", true);
	/**
	 * 日志本地存储时间
	 */
	public static final Long LOG_LOCAL_TIME = Configurations.getLongProperty("logLocalTime", 48);
	/**
	 * 实时统计默认时间
	 */
	public static final Long REPORT_LOCAL_TIME = Configurations.getLongProperty("reportLocalTime", 1);

	/**
	 * 日志是否从hive中查询
	 */
	public static final boolean SEL_FROM_HIVE = Configurations.getBooleanProperty("selFromHive", true);
	public static final boolean ONLY_SEL_FROM_HIVE = Configurations.getBooleanProperty("onlySelFromHive", true);

	/**
	 * 跨域问题解决，允许跨域的url
	 */
	public static final String TARGET_URL = Configurations.getStringProperty("target_url", "*");
	public static final String ACCESS_CONTROL_MAX_AGE = Configurations.getStringProperty("ACCESS_CONTROL_MAX_AGE",
			"3600");
	/**
	 * elasticsearch 检索相关
	 */
	public static final String SEARCH_DATEFORMAT = Configurations.getStringProperty("search.dateformat",
			"yyyy-MM-dd HH:mm:ss");
	public static final String SEARCH_ES_HOSTANDPORT_A = Configurations.getStringProperty("search.eshostandport_A",
			null);
	public static final String SEARCH_ES_HOSTANDPORT_B = Configurations.getStringProperty("search.eshostandport_B",
			null);
	public static final String SEARCH_ES_HOSTANDPORT_C = Configurations.getStringProperty("search.eshostandport_C",
			null);
	/**
	 * 数据中心A版数据库名称,程序中每次查询时使用的数据库名称 use HIVEADBNAME
	 */
	public static final String HIVEADBNAME = Configurations.getStringProperty("jdbc.hive.AName", "xa_dfbhit_hive");
	/**
	 * 数据中心B版数据库名称,程序中每次查询时使用的数据库名称 use HIVEBDBNAME
	 */
	public static final String HIVEBDBNAME = Configurations.getStringProperty("jdbc.hive.BName", "xa_z2_mesalog_hive");

}

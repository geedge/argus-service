/**
 *@Title: RedisUtil.java
 *@Package com.nis.demo 
 *@Description TODO
 *@author dell
 *@date 2016年9月13日 下午3:05:45
 *@version  版本号
 */
package com.nis.util.redis;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

import com.nis.util.BeanHelper;
import com.nis.util.Configurations;
import com.nis.util.Constants;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * @ClassName: RedisUtil.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月13日 下午3:05:45
 * @version V1.0
 */
public class RedisDao {
	protected  final Logger logger = Logger.getLogger(RedisDao.class);
	private  JedisCluster cluster=null;
	private  JedisClusterPipeline jcp=null;
	
	public void initCluster(int type) throws Exception{
		if(cluster==null && Constants.IS_OPEN_REDIS){
			//初始化连接池配置
			GenericObjectPoolConfig config= getPoolConfig();
			//初始化节点信息
			Set<HostAndPort> jedisHostAndNodeSet= initNodes();
			int connectionTimeout=Configurations.getIntProperty("redis.cluster.connectiontimeout", 100);
			int soTimeout=Configurations.getIntProperty("redis.cluster.sotimeout", 100);
			int maxAttempts=Configurations.getIntProperty("redis.cluster.maxattempts", 3);
			//构造单例集群实例，其内部由多个节点构成，每个节点都有自己的连接池
			cluster=new JedisCluster(jedisHostAndNodeSet,connectionTimeout,soTimeout,maxAttempts,config);
		}
		logger.info("--------------------init redis cluster-----------");
	}
	/**
	 * getJedisCluster(初始化redis的cluster)
	 * (这里描述这个方法适用条件 – 可选)
	 * @return
	 * @throws Exception 
	 *JedisCluster
	 * @exception 
	 * @since  1.0.0
	 */
	@PostConstruct
	public  JedisCluster getJedisCluster() throws Exception{
		initCluster(0);
		return cluster;
	}
	/**
	 * 
	 * closeJedisCluster(关闭redis cluster的方法，在系统终止时使用)
	 * (这里描述这个方法适用条件 – 可选)
	 * @throws IOException 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	@PreDestroy
	public  void closeJedisCluster() throws IOException{
		if(cluster!=null)
			cluster.close();
	}
	/**
	 * 
	 * getPoolConfig(初始化连接池的配置，这里可以设置很多参数的，不过目前没加)
	 * (这里描述这个方法适用条件 – 可选)
	 * @return 
	 *GenericObjectPoolConfig
	 * @exception 
	 * @since  1.0.0
	 */
	private  GenericObjectPoolConfig getPoolConfig(){
		GenericObjectPoolConfig config=new GenericObjectPoolConfig();
		config.setMaxTotal(Configurations.getIntProperty("redis.pool.maxtotal", 500));//整个池的最大值
		config.setMaxIdle(Configurations.getIntProperty("redis.pool.maxidle", 100));//最大空闲
		config.setMaxWaitMillis(Configurations.getIntProperty("redis.pool.maxwaitmillis", -1));//获取不到永远等待
		config.setBlockWhenExhausted(Configurations.getBooleanProperty("redis.pool.blockwhenexhausted", true));
		config.setNumTestsPerEvictionRun(Configurations.getIntProperty("redis.pool.numtestsperevictionrun", Integer.MAX_VALUE));//always test all idle object 
		config.setTestOnBorrow(Configurations.getBooleanProperty("redis.pool.testonborrow", true));
		config.setTestOnReturn(Configurations.getBooleanProperty("redis.pool.testonreturn", false));
		config.setTestWhileIdle(Configurations.getBooleanProperty("redis.pool.testwhileidle", true));//发呆过长时间是否先test一下
		config.setTimeBetweenEvictionRunsMillis(Configurations.getLongProperty("redis.pool.timebetweenevictionrunsmillis", 60000L));//-1不启动，默认1min一次
		config.setMinEvictableIdleTimeMillis(Configurations.getLongProperty("redis.pool.minevictableidletimemillis", 60000L));//可发呆的时间，10mins
		return config;
	}
	/**
	 * 
	 * initNodes(初始化节点信息)
	 * (这里描述这个方法适用条件 – 可选)
	 * @return
	 * @throws Exception 
	 *Set<HostAndPort>
	 * @exception 
	 * @since  1.0.0
	 */
	private  Set<HostAndPort> initNodes() throws Exception{
		String hostAndPorts=Configurations.getStringProperty("redis.cluster.host_port", null);
		if(hostAndPorts==null)
			throw new RuntimeException("配置文件中redis.cluster.host_port为空！");
		String[] hostAndPort=hostAndPorts.split(",");
		Set<HostAndPort> jedisClusterNodes=new HashSet<HostAndPort>();
		for(String host_port:hostAndPort){
			String [] _host_port=host_port.split(":");
			if(_host_port.length!=2)
				throw new RuntimeException("配置文件中redis.cluster.host_port格式不正确！");
			HostAndPort node=new HostAndPort(_host_port[0],Integer.parseInt(_host_port[1]));
			jedisClusterNodes.add(node);
		}
		return jedisClusterNodes;
	}
	/**
	 * 
	 * getClusterNodes(获取redis集群中的所有节点，每个节点都是一个连接池)
	 * (这里描述这个方法适用条件 – 可选)
	 * @return 
	 *Map<String,JedisPool>
	 * @exception 
	 * @since  1.0.0
	 */
	public  Map<String, JedisPool> getClusterNodes(){
		return cluster==null?null:cluster.getClusterNodes();
	}  
	/**
	 * 
	 * getJedisClusterPipeline(获取redis cluster的 pipline.由于jedis没有实现，这里是用来一个第三方的pipline)
	 * (这里描述这个方法适用条件 – 可选)
	 * @return 
	 * @throws Exception 
	 * @exception 
	 * @since  1.0.0
	 */
	public void getJedisClusterPipeline() throws Exception{
		 if(cluster==null) cluster=getJedisCluster();;
		 jcp=JedisClusterPipeline.pipelined(cluster);
		 jcp.refreshCluster();//刷新集群状态
	}
	/**
	 * 
	 * getJedisClusterPipeline(获取redis cluster的 pipline.由于jedis没有实现，这里是用来一个第三方的pipline)
	 * (这里描述这个方法适用条件 – 可选)
	 * @return 
	 *JedisClusterPipeline
	 * @exception 
	 * @since  1.0.0
	 */
//	public  JedisClusterPipeline getJedisClusterPipeline(JedisCluster cluster){
//		JedisClusterPipeline jcp=JedisClusterPipeline.pipelined(cluster);
//		jcp.refreshCluster();
//		return jcp;
//	}
	
	
	/**
	 * 
	 * closeJedisClusterPipeline(关闭pipeline)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param jcp 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public  void closeJedisClusterPipeline(){
		if(jcp!=null)
		jcp.close();
	}
	
	/**
	 * 
	 * closeJedisClusterPipeline(关闭pipeline)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param jcp 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
//	public  void closeJedisClusterPipeline(JedisClusterPipeline jcp){
//		if(jcp!=null)
//			jcp.close();
//	}
	/**
	 * 
	 * * saveMaps(将对象转换成的Map保存到redis中)
	 * (注意Date转换成long类型的时间戳然后以字符串的形式保存，蛋疼的jedis有问题,除了字符串跟byte[]数组就没别的保存类型了)
	 * @param jcp//pipeLine
	 * @param dataList//数据集合
	 * @param tableName//实体类对应的表或者类名，用于生成唯一的hash key
	 * @param keyField //实体类字段中表示唯一字段的字段名，其值用于生成唯一的hash key
	 * @param clazz //类名
	 * @throws Exception 
	 * @exception 
	 * @since  1.0.0
	 */
	public  void saveMaps(List<? extends Object> dataList ,String tableName,Class clazz,String keyField) throws Exception{
		logger.info("save maps start");
		if(cluster==null)cluster=getJedisCluster();
		if(jcp==null)getJedisClusterPipeline();
		long start=System.currentTimeMillis();
//		try{
			for(Object data:dataList){
				Map<String,String> dataMap=BeanHelper.transportBean2Map(clazz, data);
				jcp.hmset((tableName+"_"+dataMap.get(keyField)), dataMap);
			}
			jcp.sync();
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
		long end=System.currentTimeMillis();
		jcp.close();
		jcp=null;
		logger.info("save maps end, cost:"+(end-start));
	}
	/**
	 * 
	 * updateMaps(更新redis中的对象)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param jcp
	 * @param dataList
	 * @param tableName
	 * @param clazz
	 * @param keyField
	 * @throws Exception 
	 * @exception 
	 * @since  1.0.0
	 */
	public  void updateMaps(List<? extends Object> dataList ,String tableName,Class clazz,String keyField) throws Exception{
		logger.info("update maps start");
		if(cluster==null)cluster=getJedisCluster();
		if(jcp==null)getJedisClusterPipeline();
		long start=System.currentTimeMillis();
//		try{
			for(Object data:dataList){
				Map<String,String> dataMap=BeanHelper.transportBean2Map(clazz, data);
				jcp.hmset((tableName+"_"+dataMap.get(keyField)), dataMap);
			}
			jcp.sync();
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
		long end=System.currentTimeMillis();
		jcp.close();
		jcp=null;
		logger.info("update maps end, cost:"+(end-start));
	}
	/**
	 * 
	 * getMapById(获取Id 为xxxx的对象，返回一个Map)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param tableName
	 * @param id
	 * @return 
	 *Map<String,String>
	 * @throws Exception 
	 * @exception 
	 * @since  1.0.0
	 */
	public Map<String,String> getMapById(String tableName,long id) throws Exception{
		if(cluster==null){
			cluster=getJedisCluster();
		}
		Map<String, String> result=cluster.hgetAll(tableName+"_"+id);
		return result;
	}
	/**
	 * 
	 * getMapById(获取Id 为xxxx的对象，返回一个Map)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param cluster
	 * @param tableName
	 * @param id
	 * @return 
	 *Map<String,String>
	 * @exception 
	 * @since  1.0.0
	 */
	public  Map<String,String> getMapById(JedisCluster cluster,String tableName,long id){
		Map<String, String> result=cluster.hgetAll(tableName+"_"+id);
		return result;
	}
	
	public  void saveList(List<String> jsonList,String sql,int expire) throws Exception{
		if(cluster==null)cluster=getJedisCluster();
		if(jcp==null)getJedisClusterPipeline();
		for(String json :jsonList){
			jcp.rpush(sql, json);
		}
		jcp.expire(sql, expire);
		jcp.sync();
		jcp.close();
		jcp=null;
	}
	/**
	 * 
	 * getSet(获取redis中的一个set的全部成员)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param key
	 * @return
	 * @throws Exception 
	 *Set<String>
	 * @exception 
	 * @since  1.0.0
	 */
	public Set<String> getSet(String key) throws Exception{
		if(cluster==null)cluster=getJedisCluster();
		return cluster.smembers(key);
	}
	/**
	 * 
	 * saveSet(向一个set中添加成员)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param setName
	 * @param values
	 * @throws Exception 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public void saveSet(String setName,String ...values) throws Exception{
		if(cluster==null)cluster=getJedisCluster();
		if(jcp==null)getJedisClusterPipeline();
		for(String val:values){
			jcp.sadd(setName, val);
		}
		jcp.sync();
		jcp.close();
		jcp=null;
	}
	/**
	 * 
	 * isExistsInSet(判断一个字符串是否是set的成员，可用于去重，验证唯一性)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param setName
	 * @param value
	 * @return
	 * @throws Exception 
	 *boolean
	 * @exception 
	 * @since  1.0.0
	 */
	public boolean isExistsInSet(String setName,String value) throws Exception{
		if(cluster==null)cluster=getJedisCluster();
		return cluster.sismember(setName, value);
	}
	public  List<String> getList(String sql,long start,long end) throws Exception{
		if(cluster==null){
			cluster=getJedisCluster();
		}
		List<String> data=cluster.lrange(sql, start, end);
		return data;
	}
	public void saveString(String key,String value,int expire) throws Exception{
		logger.info("save String start");
		long start=System.currentTimeMillis();
		if(cluster==null){
			cluster=getJedisCluster();
		}
		cluster.set(key, value);
		cluster.expire(key, expire);
		long end=System.currentTimeMillis();
		logger.info("save String end,cost:"+(end-start));
	}
	public String getString(String key) throws Exception{
		if(cluster==null){
			cluster=getJedisCluster();
		}
		return cluster.get(key);
	}
	/**
	 * 
	 * del(删除key)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param keys
	 * @throws Exception 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public  void del(String... keys) throws Exception{
		if(cluster==null){
			cluster=getJedisCluster();
		}
		cluster.del(keys);
	}
	/**
	 * 
	 * clearData(清空数据)
	 * (这里描述这个方法适用条件 – 可选)
	 * @throws Exception 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public void clearData() throws Exception{
		if(cluster==null){
			cluster=getJedisCluster();
		}
		logger.info("clear data start");
		long start=System.currentTimeMillis();
		Map<String, JedisPool> nodesMap=cluster.getClusterNodes();
		if(nodesMap==null){ 
			logger.error("empty redis nodes");
			return;
		}
		for(Entry<String, JedisPool> jedis:nodesMap.entrySet()){
			Jedis client=jedis.getValue().getResource();
			// only master node can flush db
			if(client.clusterInfo().indexOf("master")!=-1)
				client.flushAll();
			client.close();
		}
		long time=System.currentTimeMillis()-start;
		logger.info("clear data finish cost: "+time);
	}
	/**
	 * 
	 * exists(键是否存在)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param key
	 * @return
	 * @throws Exception 
	 *boolean
	 * @exception 
	 * @since  1.0.0
	 */
	public boolean exists(String key) throws Exception{
		if(cluster==null){
			cluster=getJedisCluster();
		}
		return cluster.exists(key);
	}
	public static void main(String[] args) {
		
	}
}

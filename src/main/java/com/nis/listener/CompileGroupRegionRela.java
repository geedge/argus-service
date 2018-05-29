package com.nis.listener;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.ContextLoaderListener;

import com.nis.domain.restful.CompileAndGroupRelations;
import com.nis.domain.restful.GroupAndRegionRelations;
import com.nis.domain.restful.MaatRelation;
import com.nis.web.service.SpringContextHolder;

/**
 * 项目启动时加载数据字典
 * 
 * @author RenKaiGe-Office
 * @Date:2018-05-24
 */
public class CompileGroupRegionRela extends ContextLoaderListener {
	private static Logger logger = LoggerFactory.getLogger(CompileGroupRegionRela.class);

	private static RedisTemplate<String, String> redisTemplate = null;
	private static JedisConnectionFactory connectionFactory = null;

	/**
	 * 存储redis所有库中,编译分组域等配置关系,key是redis库
	 */
	private static Map<Integer, MaatRelation> idRelationMap = new ConcurrentHashMap<Integer, MaatRelation>();

	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			redisTemplate = SpringContextHolder.getBean("redisTemplate");
			connectionFactory = SpringContextHolder.getBean("connectionFactory");
			for (int i = 0; i < 16; i++) {
				connectionFactory.setDatabase(i);
				redisTemplate.setConnectionFactory(connectionFactory);
				getAllId(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getAllId(int redisDBIndex) {
		Map<Long, Set<Long>> groupCompileIdMap = new ConcurrentHashMap<Long, Set<Long>>();
		Map<Long, Set<Long>> groupRegionIdMap = new ConcurrentHashMap<Long, Set<Long>>();
		Set<String> keySet = redisTemplate.keys("EFFECTIVE_RULE:*");
		if (keySet != null && keySet.size() > 0) {
			for (String keys : keySet) {
				if (!keys.toLowerCase().contains("config_compile")) {
					try {
						String value = redisTemplate.opsForValue().get(keys).toString();
						if (keys.toLowerCase().contains("config_group")) {
							String[] split = value.split("\t");
							String groupIdStr = split[0];
							String compileIdStr = split[1];
							if (groupCompileIdMap.containsKey(Long.valueOf(groupIdStr))) {
								groupCompileIdMap.get(Long.valueOf(groupIdStr)).add(Long.valueOf(compileIdStr));
							} else {
								Set<Long> list = new HashSet<Long>();
								list.add(Long.valueOf(compileIdStr));
								groupCompileIdMap.put(Long.valueOf(groupIdStr), list);
							}

						} else {
							String[] split = value.split("\t");
							String regionIdStr = split[0];
							String groupIdStr = split[1];
							if (groupRegionIdMap.containsKey(Long.valueOf(groupIdStr))) {
								groupRegionIdMap.get(Long.valueOf(groupIdStr)).add(Long.valueOf(regionIdStr));
							} else {
								Set<Long> list = new HashSet<Long>();
								list.add(Long.valueOf(regionIdStr));
								groupRegionIdMap.put(Long.valueOf(groupIdStr), list);
							}
						}
					} catch (Exception e) {
						logger.error("从redis中查询" + keys + "数据失败");
					}
				}
			}
		}

		for (Long groupId : groupCompileIdMap.keySet()) {
			if (idRelationMap.containsKey(redisDBIndex)) {
				MaatRelation maatRelation = idRelationMap.get(redisDBIndex);

				Map<Long, Set<Long>> compileAndGroupMap = maatRelation.getCompileAndGroupMap();
				Set<Long> compileIdList = groupCompileIdMap.get(groupId);
				for (Long compileId : compileIdList) {
					if (compileAndGroupMap.containsKey(compileId)) {
						compileAndGroupMap.get(compileId).add(groupId);
					} else {
						Set<Long> set = new HashSet<Long>();
						set.add(groupId);
						compileAndGroupMap.put(compileId, set);
					}
				}
				Map<Long, Set<Long>> groupAndCompileMap = maatRelation.getGroupAndCompileMap();
				if (groupAndCompileMap.containsKey(groupId)) {
					groupAndCompileMap.get(groupId).addAll(compileIdList);
				} else {
					groupAndCompileMap.put(groupId, compileIdList);
				}

				Map<Long, Set<Long>> groupAndRegionMap = maatRelation.getGroupAndRegionMap();
				if (groupAndRegionMap.containsKey(groupId)) {
					groupAndRegionMap.get(groupId).addAll(groupRegionIdMap.get(groupId));
				} else {
					groupAndRegionMap.put(groupId, groupRegionIdMap.get(groupId));
				}

			} else {
				MaatRelation maatRelation = new MaatRelation();

				Map<Long, Set<Long>> compileAndGroupMap = new ConcurrentHashMap<Long, Set<Long>>();
				Set<Long> compileIdList = groupCompileIdMap.get(groupId);
				for (Long compileId : compileIdList) {
					if (compileAndGroupMap.containsKey(compileId)) {
						compileAndGroupMap.get(compileId).add(groupId);
					} else {
						Set<Long> set = new HashSet<Long>();
						set.add(groupId);
						compileAndGroupMap.put(compileId, set);
					}
				}
				maatRelation.setCompileAndGroupMap(compileAndGroupMap);

				Map<Long, Set<Long>> groupAndCompileMap = new ConcurrentHashMap<Long, Set<Long>>();
				groupAndCompileMap.put(groupId, compileIdList);
				maatRelation.setGroupAndCompileMap(groupAndCompileMap);

				Map<Long, Set<Long>> groupAndRegionMap = new ConcurrentHashMap<Long, Set<Long>>();
				groupAndRegionMap.put(groupId, groupRegionIdMap.get(groupId));
				maatRelation.setGroupAndRegionMap(groupAndRegionMap);
				idRelationMap.put(redisDBIndex, maatRelation);
			}
		}
	}

	/**
	 * 从字典中删除编译id的关系
	 * @param redisDBIndex 哪个redis库
	 * @param compileId
	 */
	public static void delIdRelation(int redisDBIndex, long compileId) {
		MaatRelation maatRelation = idRelationMap.get(redisDBIndex);
		if (maatRelation != null) {
			removKey(maatRelation, compileId);
		} else {
			logger.error("redis" + redisDBIndex + "号库中没有id对应关系(没有配置),请检查redisDBIndex是否正确");
		}
	}

	private static void removKey(MaatRelation maatRelation, long compileId) {
		if (maatRelation.getCompileAndGroupMap().containsKey(compileId)) {
			Set<Long> list = maatRelation.getCompileAndGroupMap().get(compileId);

			if (list != null && list.size() > 0) {
				for (Long groupId : list) {
					// }
					// for (Long groupId: list.size()) {
					if (maatRelation.getGroupAndCompileMap().containsKey(groupId)) {
						// && maatRelation.getGroupAndCompileMap().get(groupId).size() == 1
						// && maatRelation.getGroupAndCompileMap().get(groupId).get(0).longValue() ==
						// compileId) {
						Set<Long> compileIdList = maatRelation.getGroupAndCompileMap().get(groupId);
						if (compileIdList.size() == 1) {// 当前的groupid只属于当前的compileid,没有被分组复用,需要将编译配置,分组关系,域配置,置无效
							for (Long id : compileIdList) {
								if (id.longValue() == compileId) {
									// 这个group没有被分组复用,所以需要将这个组下面的域配置置为无效,组也要置为无效
									if (maatRelation.getGroupAndRegionMap().containsKey(groupId)) {
										// 把当前组置为无效,把当前组下的所有域置为无效
										maatRelation.getGroupAndRegionMap().remove(groupId);// 删除组对应的域
										maatRelation.getGroupAndCompileMap().remove(groupId);// 删除当前组所对应的编译
										// maatRelation.getCompileAndGroupMap().get(compileId).indexOf(groupId);
										// maatRelation.getCompileAndGroupMap().get(compileId)
										// list.remove(maatRelation.getCompileAndGroupMap().get(compileId).indexOf(groupId));//
										// 删除编译下面的组
										list.remove(groupId);// 删除编译下面的组
									}
								}
							}
						} else {
							// 这个组对应了多个编译配置被分组复用了,这时将这个组和编译的关系置为无效,编译置为无效
							// list.remove(maatRelation.getCompileAndGroupMap().get(compileId).indexOf(groupId));//
							// 删除编译下面的这个组
							// maatRelation.getGroupAndCompileMap().get(groupId)
							// .remove(maatRelation.getGroupAndCompileMap().get(groupId).indexOf(compileId));//
							// 删除分组关系中的组和编译关系

							list.remove(groupId);// 删除编译下面的这个组
							maatRelation.getGroupAndCompileMap().get(groupId).remove(compileId);
						}
					} else {
						// 当前的groupid属于多个组,被分组复用了,所以不对这个组进行操作,也不对这个组下面的域进行操作
					}
				}
			}

		}
	}

	/**
	 * 添加配置
	 * @param redisDBIndex 向哪个库添加配置
	 * @param compileAndGroupRelations id映射关系
	 */
	public static void addIdRelation(int redisDBIndex, CompileAndGroupRelations compileAndGroupRelations) {
		MaatRelation maatRelation = idRelationMap.get(redisDBIndex);
		if (maatRelation != null) {
			addKey(maatRelation, compileAndGroupRelations);
		} else {
			maatRelation = new MaatRelation();
			addKey(maatRelation, compileAndGroupRelations);
			idRelationMap.put(redisDBIndex, maatRelation);
		}

	}

	private static void addKey(MaatRelation maatRelation, CompileAndGroupRelations compileAndGroupRelations) {
		long compileId = compileAndGroupRelations.getCompileId();
		List<GroupAndRegionRelations> groupIdList = compileAndGroupRelations.getGroupIdList();
		if (groupIdList != null && groupIdList.size() > 0) {
			for (GroupAndRegionRelations gRR : groupIdList) {
				Map<Long, Set<Long>> compileAndGroupMap2 = maatRelation.getCompileAndGroupMap();
				if (compileAndGroupMap2 != null && compileAndGroupMap2.size() > 0) {
					if (!compileAndGroupMap2.containsKey(compileId)) {
						Set<Long> set = new HashSet<Long>();
						set.add(gRR.getGroupId());
						compileAndGroupMap2.put(compileId, set);
					} else {
						compileAndGroupMap2.get(compileId).add(gRR.getGroupId());
					}
				} else {
					Map<Long, Set<Long>> compileAndGroupMap = new ConcurrentHashMap<Long, Set<Long>>();
					Set<Long> set = new HashSet<Long>();
					set.add(gRR.getGroupId());
					compileAndGroupMap.put(compileId, set);
					maatRelation.setCompileAndGroupMap(compileAndGroupMap);
				}

				Map<Long, Set<Long>> groupAndCompileMap2 = maatRelation.getGroupAndCompileMap();
				if (groupAndCompileMap2 != null && groupAndCompileMap2.size() > 0) {
					if (!groupAndCompileMap2.containsKey(gRR.getGroupId())) {
						Set<Long> set = new HashSet<Long>();
						set.add(compileId);
						groupAndCompileMap2.put(gRR.getGroupId(), set);
					} else {
						groupAndCompileMap2.get(gRR.getGroupId()).add(compileId);
					}
				} else {
					Map<Long, Set<Long>> groupAndCompileMap = new ConcurrentHashMap<Long, Set<Long>>();
					Set<Long> set = new HashSet<Long>();
					set.add(compileId);
					groupAndCompileMap.put(gRR.getGroupId(), set);

					maatRelation.setGroupAndCompileMap(groupAndCompileMap);
				}
				Map<Long, Set<Long>> groupAndRegionMap2 = maatRelation.getGroupAndRegionMap();
				if (groupAndRegionMap2 != null && groupAndRegionMap2.size() > 0) {
					if (!groupAndRegionMap2.containsKey(gRR.getGroupId())) {
						Set<Long> set = new HashSet<Long>();
						set.add(gRR.getRegionId());
						groupAndRegionMap2.put(gRR.getGroupId(), set);
					} else {
						groupAndRegionMap2.get(gRR.getGroupId()).add(gRR.getRegionId());
					}
				} else {
					Map<Long, Set<Long>> groupAndRegionMap = new ConcurrentHashMap<Long, Set<Long>>();
					Set<Long> set = new HashSet<Long>();
					set.add(gRR.getRegionId());
					groupAndRegionMap.put(gRR.getGroupId(), set);
					maatRelation.setGroupAndRegionMap(groupAndRegionMap);
				}
			}
		}
	}

	public static Map<Integer, MaatRelation> getIdRelationMap() {
		return idRelationMap;
	}

	public static void setIdRelationMap(Map<Integer, MaatRelation> idRelationMap) {
		CompileGroupRegionRela.idRelationMap = idRelationMap;
	}
}
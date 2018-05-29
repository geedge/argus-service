package com.nis.domain.restful;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MaatRelation implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 编译id与groupid的对应关系,key是编译id,value使用set不允许重复
	 */
	private Map<Long, Set<Long>> compileAndGroupMap;
	/**
	 * groupid与编译id的对应关系,key是groupid,value使用set不允许重复
	 */
	private Map<Long, Set<Long>> groupAndCompileMap;
	// private Map<Long, List<Long>> regionAndGroupMap;
	/**
	 * groupid与regionid的对应关系,key是groupid,value使用set不允许重复
	 */
	private Map<Long, Set<Long>> groupAndRegionMap;

	public Map<Long, Set<Long>> getCompileAndGroupMap() {
		return compileAndGroupMap;
	}

	public void setCompileAndGroupMap(Map<Long, Set<Long>> compileAndGroupMap) {
		this.compileAndGroupMap = compileAndGroupMap;
	}

	public Map<Long, Set<Long>> getGroupAndCompileMap() {
		return groupAndCompileMap;
	}

	public void setGroupAndCompileMap(Map<Long, Set<Long>> groupAndCompileMap) {
		this.groupAndCompileMap = groupAndCompileMap;
	}

	public Map<Long, Set<Long>> getGroupAndRegionMap() {
		return groupAndRegionMap;
	}

	public void setGroupAndRegionMap(Map<Long, Set<Long>> groupAndRegionMap) {
		this.groupAndRegionMap = groupAndRegionMap;
	}

	public static void removKey(MaatRelation maatRelation, long compileId) {
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

	public static void syso(MaatRelation maatRelation) {
		Map<Long, Set<Long>> getCompileAndGroupMap = maatRelation.getCompileAndGroupMap();
		for (Long compile : getCompileAndGroupMap.keySet()) {
			Set<Long> groupList = getCompileAndGroupMap.get(compile);
			for (Long group : groupList) {
				System.out.println("编译:" + compile + "组:" + group);
			}
		}
		Map<Long, Set<Long>> getGroupAndCompileMap = maatRelation.getGroupAndCompileMap();
		for (Long group : getGroupAndCompileMap.keySet()) {
			Set<Long> compileList = getGroupAndCompileMap.get(group);
			for (Long compile : compileList) {
				System.out.println("组:" + group + "编译:" + compile);
			}
		}
		Map<Long, Set<Long>> getGroupAndRegionMap = maatRelation.getGroupAndRegionMap();
		for (Long group : getGroupAndRegionMap.keySet()) {
			Set<Long> regionList = getGroupAndRegionMap.get(group);
			for (Long region : regionList) {
				System.out.println("组:" + group + "域:" + region);
			}
		}

		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	}

	public static void main(String[] args) {
		MaatRelation maatRelation = getMaat();
		syso(maatRelation);

		long compileId = 11;
		removKey(maatRelation, compileId);
		syso(maatRelation);

		compileId = 2;
		removKey(maatRelation, compileId);
		syso(maatRelation);

		addKey(maatRelation, setCGR());
		syso(maatRelation);
		
		
		compileId = 99999L;
		removKey(maatRelation, compileId);
		syso(maatRelation);
	}

	public static CompileAndGroupRelations setCGR() {
		CompileAndGroupRelations cGR = new CompileAndGroupRelations();
		cGR.setCompileId(99999L);

		List<GroupAndRegionRelations> groupIdList = new ArrayList<GroupAndRegionRelations>();
		GroupAndRegionRelations gRR = new GroupAndRegionRelations();
		gRR.setGroupId(100L);
		gRR.setRegionId(200L);
		groupIdList.add(gRR);

		gRR = new GroupAndRegionRelations();
		gRR.setGroupId(100L);
		gRR.setRegionId(300L);
		groupIdList.add(gRR);
		cGR.setGroupIdList(groupIdList);
		return cGR;
	}

	public static void addKey(MaatRelation maatRelation, CompileAndGroupRelations compileAndGroupRelations) {

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
				}

			}
		}
	}

	public static MaatRelation getMaat() {
		MaatRelation maatRelation = new MaatRelation();
		Map<Long, Set<Long>> compileAndGroupMap = new HashMap<Long, Set<Long>>();
		Set<Long> list = new HashSet<Long>();
		list.add(1l);
		compileAndGroupMap.put(1l, list);
		list = new HashSet<Long>();
		list.add(1l);
		compileAndGroupMap.put(2l, list);
		Map<Long, Set<Long>> groupAndCompileMap = new HashMap<Long, Set<Long>>();
		list = new HashSet<Long>();
		list.add(1l);
		list.add(2l);
		groupAndCompileMap.put(1l, list);
		// Map<Long, List<Long>> regionAndGroupMap = new HashMap<Long, List<Long>>();
		list = new HashSet<Long>();
		list.add(1l);
		// regionAndGroupMap.put(1l, list);
		Map<Long, Set<Long>> groupAndRegionMap = new HashMap<Long, Set<Long>>();
		groupAndRegionMap.put(1l, list);
		maatRelation.setCompileAndGroupMap(compileAndGroupMap);
		maatRelation.setGroupAndCompileMap(groupAndCompileMap);
		maatRelation.setGroupAndRegionMap(groupAndRegionMap);
		// maatRelation.setRegionAndGroupMap(regionAndGroupMap);

		return maatRelation;
	}

}

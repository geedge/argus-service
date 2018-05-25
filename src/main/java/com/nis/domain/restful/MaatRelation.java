package com.nis.domain.restful;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaatRelation implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<Long, List<Long>> compileAndGroupMap;
	private Map<Long, List<Long>> groupAndCompileMap;
	// private Map<Long, List<Long>> regionAndGroupMap;
	private Map<Long, List<Long>> groupAndRegionMap;

	public Map<Long, List<Long>> getCompileAndGroupMap() {
		return compileAndGroupMap;
	}

	public void setCompileAndGroupMap(Map<Long, List<Long>> compileAndGroupMap) {
		this.compileAndGroupMap = compileAndGroupMap;
	}

	public Map<Long, List<Long>> getGroupAndCompileMap() {
		return groupAndCompileMap;
	}

	public void setGroupAndCompileMap(Map<Long, List<Long>> groupAndCompileMap) {
		this.groupAndCompileMap = groupAndCompileMap;
	}

	public Map<Long, List<Long>> getGroupAndRegionMap() {
		return groupAndRegionMap;
	}

	public void setGroupAndRegionMap(Map<Long, List<Long>> groupAndRegionMap) {
		this.groupAndRegionMap = groupAndRegionMap;
	}

	public static void removKey(MaatRelation maatRelation, long compileId) {

		if (maatRelation.getCompileAndGroupMap().containsKey(compileId)) {
			List<Long> list = maatRelation.getCompileAndGroupMap().get(compileId);
			for (int i = 0; i < list.size(); i++) {
				Long groupId = list.get(i);
				// }
				// for (Long groupId: list.size()) {
				if (maatRelation.getGroupAndCompileMap().containsKey(groupId)) {
					// && maatRelation.getGroupAndCompileMap().get(groupId).size() == 1
					// && maatRelation.getGroupAndCompileMap().get(groupId).get(0).longValue() ==
					// compileId) {
					List<Long> compileIdList = maatRelation.getGroupAndCompileMap().get(groupId);
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
									list.remove(maatRelation.getCompileAndGroupMap().get(compileId).indexOf(groupId));// 删除编译下面的组
								}
							}
						}

					} else {
						// 这个组对应了多个编译配置被分组复用了,这时将这个组和编译的关系置为无效,编译置为无效
						list.remove(maatRelation.getCompileAndGroupMap().get(compileId).indexOf(groupId));// 删除编译下面的这个组
						maatRelation.getGroupAndCompileMap().get(groupId)
								.remove(maatRelation.getGroupAndCompileMap().get(groupId).indexOf(compileId));// 删除分组关系中的组和编译关系
					}
				} else {
					// 当前的groupid属于多个组,被分组复用了,所以不对这个组进行操作,也不对这个组下面的域进行操作
				}
			}

		}
	}

	public static void syso(MaatRelation maatRelation) {
		Map<Long, List<Long>> getCompileAndGroupMap = maatRelation.getCompileAndGroupMap();
		for (Long compile : getCompileAndGroupMap.keySet()) {
			List<Long> groupList = getCompileAndGroupMap.get(compile);
			for (Long group : groupList) {
				System.out.println("编译:" + compile + "组:" + group);
			}
		}
		Map<Long, List<Long>> getGroupAndCompileMap = maatRelation.getGroupAndCompileMap();
		for (Long group : getGroupAndCompileMap.keySet()) {
			List<Long> compileList = getGroupAndCompileMap.get(group);
			for (Long compile : compileList) {
				System.out.println("组:" + group + "编译:" + compile);
			}
		}
		Map<Long, List<Long>> getGroupAndRegionMap = maatRelation.getGroupAndRegionMap();
		for (Long group : getGroupAndRegionMap.keySet()) {
			List<Long> regionList = getGroupAndRegionMap.get(group);
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
	}

	public static MaatRelation getMaat() {
		MaatRelation maatRelation = new MaatRelation();
		Map<Long, List<Long>> compileAndGroupMap = new HashMap<Long, List<Long>>();
		List<Long> list = new ArrayList<Long>();
		list.add(1l);
		compileAndGroupMap.put(1l, list);
		list = new ArrayList<Long>();
		list.add(1l);
		compileAndGroupMap.put(2l, list);
		Map<Long, List<Long>> groupAndCompileMap = new HashMap<Long, List<Long>>();
		list = new ArrayList<Long>();
		list.add(1l);
		list.add(2l);
		groupAndCompileMap.put(1l, list);
		// Map<Long, List<Long>> regionAndGroupMap = new HashMap<Long, List<Long>>();
		list = new ArrayList<Long>();
		list.add(1l);
		// regionAndGroupMap.put(1l, list);
		Map<Long, List<Long>> groupAndRegionMap = new HashMap<Long, List<Long>>();
		groupAndRegionMap.put(1l, list);
		maatRelation.setCompileAndGroupMap(compileAndGroupMap);
		maatRelation.setGroupAndCompileMap(groupAndCompileMap);
		maatRelation.setGroupAndRegionMap(groupAndRegionMap);
		// maatRelation.setRegionAndGroupMap(regionAndGroupMap);

		return maatRelation;
	}

}

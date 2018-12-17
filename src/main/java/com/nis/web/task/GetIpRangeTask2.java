package com.nis.web.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.avro.file.SyncableFileOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nis.web.dao.impl.LocalLogJDBCByDruid;

@Component
@PropertySource(value = { "classpath:nis.properties", "classpath:jdbc.properties" })
public class GetIpRangeTask2 {
	@Autowired
	private LocalLogJDBCByDruid localLogJDBCByDruid;

//	@Scheduled(cron = "0/5 * * * * ?")
	public void test() {
		try {
			getAllIp4();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<Long, String> threadTest(List<String> allIp) throws InterruptedException, ExecutionException {
		Map<Long, String> map = new HashMap<>();
		int ever = 10000;
		int count = allIp.size() / ever;
		if (allIp.size() % ever != 0) {
			count++;
		}
		List<Future<Map<Long, String>>> futures = new ArrayList<>();
		ExecutorService executor = Executors.newFixedThreadPool(10);
		for (int i = 0; i < count; i++) {
			int start = i * ever;
			int end = i * ever + ever;
			if (end > allIp.size()) {
				end = allIp.size();
			}
			IpToLongThread ipToLongThread = new IpToLongThread(allIp.subList(start, end));
			Future<Map<Long, String>> res = executor.submit(ipToLongThread);// 异步提交, non blocking.
			futures.add(res);
		}
		executor.shutdown();
		System.out.println("开始关闭线程");
		for (Future<Map<Long, String>> future : futures) {
			try {
				map.putAll(future.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		System.out.println("list=" + map.size() + ",allIp=" + allIp.size());
		System.out.println("线程执行完毕");
		return map;
	}

	private static String testbb(Long[] NoNum) {
		int state = 0;
		String result = "";
		for (int i = 0; i < NoNum.length; i++) {
			if (i == NoNum.length - 1)
				state = 2;
			if (state == 0) {
				if (NoNum[i + 1].longValue() == NoNum[i].longValue() + 1) {
					result += String.valueOf(NoNum[i]);
					result += "-";
					state = 1;
				} else {
					result += String.valueOf(NoNum[i]);
					result += ",";
				}
			} else if (state == 1) {
				if (NoNum[i + 1] != NoNum[i] + 1) {
					result += String.valueOf(NoNum[i]);
					result += ",";
					state = 0;
				}
			} else {
				result += String.valueOf(NoNum[i]);
			}
		}
		return result;
	}

	public void getAllIp4() throws Exception {
		List<String> allIp = localLogJDBCByDruid.getAllIp("1");
		Map<Long, String> map = threadTest(allIp);
		Set<Long> keySet = map.keySet();
		List<Long> ipList = new ArrayList<>(keySet);
		Long[] array = new Long[ipList.size()];
		Collections.sort(ipList);
		ipList.toArray(array);
		String result = testbb(array);
		if (result != null && !result.trim().equals("")) {
			String substring = result;
			if(result.endsWith(",")) {
				substring=result.substring(0, result.length() - 1);
			}
			String[] split = substring.split(",");
			List<Long> list = new ArrayList<>();
			for (String str : split) {
				String[] ipAddr = str.split("-");
				if (ipAddr.length > 1) {
					for (String ipStr : ipAddr) {
						System.out.print(ipStr + "[" + map.get(Long.parseLong(ipStr)) + "]");
					}
					System.out.println();
				} else {
					list.add(Long.parseLong(str));
				}
			}
			Collections.sort(list);
			for (int i = 0; i < list.size(); i = i + 2) {
				System.out.print(list.get(i) + "[" + map.get(list.get(i)) + "]");
				if (i < list.size() - 1) {
					System.out.print(list.get(i + 1) + "[" + map.get(list.get(i + 1)) + "]");
				}
				System.out.println();
			}

		}
	}

	public String longToIp(long ip) {
		return ((ip >> 24) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + (ip & 0xFF);
	}

	public static void main(String[] args) {
//		Long [] NoNum = { 2449052146l,
//				2449052148l,
//				2449052136l,
//				2449052139l,
//				2449052138l,
//				2449052141l,
//				2449052128l,
//				2449052134l,
//				2449052120l,
//				2449052124l,
//				2449052127l};

		Set<Long> keySet = new HashSet<>();
		keySet.add(2449052146l);
		keySet.add(2449052136l);
		keySet.add(2449052139l);
		keySet.add(2449052120l);

		List<Long> ipList = new ArrayList<>(keySet);
		Long[] array = new Long[ipList.size()];
		Collections.sort(ipList);
		ipList.toArray(array);
		System.out.println(Arrays.toString(array));

		Long[] NoNum = { 3l, 2l, 1l };

		String testbb = testbb(NoNum);
		System.out.println(testbb);
	}

	public void a() {
		int[] NoNum = { 1, 2, 3, 5, 7, 8, 9, 10, 13 };
		int state = 0;
		String result = "";
		for (int i = 0; i < NoNum.length; i++) {
			if (i == NoNum.length - 1)
				state = 2;
			if (state == 0) {
				if (NoNum[i + 1] == NoNum[i] + 1) {
					result += Integer.toString(NoNum[i]);
					result += "-";
					state = 1;
				} else {
					result += Integer.toString(NoNum[i]);
					result += ",";
				}
			} else if (state == 1) {
				if (NoNum[i + 1] != NoNum[i] + 1) {
					result += Integer.toString(NoNum[i]);
					result += ",";
					state = 0;
				}
			} else {
				result += Integer.toString(NoNum[i]);
			}
		}
		System.out.println(result);
	}

	public static String convert(Long[] ints, int index) {
		int end = index;
		if (ints.length == index) {// 结束条件，遍历完数组
			return "";
		} else {
			for (int i = index; i < ints.length; i++) {
				if (i < ints.length - 1) {
					if (ints[i] + 1 == ints[i + 1]) {
						end = i;
					} else {
						if (i > index)
							end = end + 1;
						break;
					}
				} else {
					if (end == ints.length - 2) {
						end = ints.length - 1;
						break;
					}
				}
			}
			if (index == end)// 相等说明不连续
				return ints[index] + "," + convert(ints, end + 1);
			else// 连续
				return ints[index] + "-" + ints[end] + "," + convert(ints, end + 1);
		}

	}

	public void getAllIp() throws Exception {
		List<String> allIp = localLogJDBCByDruid.getAllIp("1");
		Map<String, Map<Integer, List<Integer>>> map = new HashMap<>();
		for (String ip : allIp) {
			String topTwo = ip.substring(0, ip.indexOf(".", ip.indexOf(".") + 1));
			String lastTwo = ip.substring(ip.indexOf(".", ip.indexOf(".") + 1) + 1);

			String[] endIpAddr = lastTwo.split("\\.");
			int third = Integer.parseInt(endIpAddr[0]);
			int fourth = Integer.parseInt(endIpAddr[1]);
			if (map.containsKey(topTwo)) {
				Map<Integer, List<Integer>> thridMap = map.get(topTwo);
				if (thridMap.containsKey(third)) {
					thridMap.get(third).add(fourth);
				} else {
					List<Integer> list = new ArrayList<>();
					list.add(fourth);
					thridMap.put(third, list);
				}
			} else {
				Map<Integer, List<Integer>> thridMap = new HashMap<>();
				List<Integer> list = new ArrayList<>();
				list.add(fourth);
				thridMap.put(third, list);
				map.put(topTwo, thridMap);
			}
		}

		Map<String, String> rangMap = new HashMap<>();

		for (Entry<String, Map<Integer, List<Integer>>> entry : map.entrySet()) {
			String topTwo = entry.getKey();
			Map<Integer, List<Integer>> endIpMap = entry.getValue();
			Set<Integer> keySet = endIpMap.keySet();
			ArrayList<Integer> arrayList = new ArrayList<Integer>(keySet);
			Collections.sort(arrayList);
			Integer minThrid = arrayList.get(0);
			Integer maxThrid = arrayList.get(arrayList.size() - 1);
			List<Integer> minThridList = endIpMap.get(minThrid);
			List<Integer> maxThridList = endIpMap.get(maxThrid);
			Collections.sort(minThridList);
			Collections.sort(maxThridList);

			if (minThridList.size() > 0 && maxThridList.size() > 0) {
				rangMap.put(topTwo + "." + minThrid + "." + minThridList.get(0),
						topTwo + "." + maxThrid + "." + maxThridList.get(maxThridList.size() - 1));
			} else {
				System.out.println("1");
			}
		}
		for (String minIp : rangMap.keySet()) {
			System.out.println(minIp + "--" + rangMap.get(minIp));

		}
	}

	public void getAllIp2() throws Exception {
		Map<String, List<Integer>> map = new HashMap<>();
		List<String> allIp = localLogJDBCByDruid.getAllIp("1");
		for (String ip : allIp) {
			String[] split = ip.split(",");
			for (int i = 0; i < split.length; i++) {
				String prev = ip.substring(0, ip.lastIndexOf("."));
				int forth = Integer.parseInt(ip.substring(ip.lastIndexOf(".") + 1));
				if (map.containsKey(prev)) {
					map.get(prev).add(forth);
				} else {
					List<Integer> list = new ArrayList<>();
					list.add(forth);
					map.put(prev, list);
				}
			}
		}
		if (map.size() > 0) {
			Map<String, String> ipMap = new HashMap<>();
			for (Entry<String, List<Integer>> entry : map.entrySet()) {
				String prev = entry.getKey();
				List<Integer> endList = entry.getValue();
				a(prev, endList, ipMap);
			}

			Map<String, List<Integer>> map2 = new HashMap<>();
			for (String minIp : ipMap.keySet()) {
				String endIp = ipMap.get(minIp);
				String prev = minIp.substring(0, minIp.lastIndexOf("."));
				int minEnd = Integer.parseInt(minIp.substring(minIp.lastIndexOf(".") + 1));
				int maxEnd = Integer.parseInt(endIp.substring(endIp.lastIndexOf(".") + 1));
				for (int i = minEnd; i <= maxEnd; i++) {
					if (map.containsKey(prev)) {
						map.get(prev).add(i);
					} else {
						List<Integer> list = new ArrayList<>();
						list.add(i);
						map.put(prev, list);
					}
				}
			}

			Map<String, String> ipMap2 = new HashMap<>();
			for (Entry<String, List<Integer>> entry : map2.entrySet()) {
				String prev = entry.getKey();
				List<Integer> endList = entry.getValue();
				a(prev, endList, ipMap2);
			}

			for (String minIp : ipMap2.keySet()) {
				System.out.println(minIp + "--" + ipMap2.get(minIp));
			}

		}
	}

	private void a(String prev, List<Integer> endList, Map<String, String> ipMap) {

		Collections.sort(endList);
		Integer start = endList.get(0);
		if (endList.size() == 256) {// 0-255
			ipMap.put(prev + "." + start, prev + ".255");
		} else {
			for (int i = 0; i < endList.size(); i++) {
				Integer curr = endList.get(i);
				if (i < endList.size() - 1) {
					Integer next = endList.get(i + 1);
					if (curr + 1 == next) {
						ipMap.put(prev + "." + start, prev + "." + next);
					} else {
						start = curr;
					}
				}
			}
		}
	}

	public void getAllIp1() throws Exception {
		Map<Integer, Map<Integer, Map<Integer, List<Integer>>>> map = new HashMap<>();
		List<String> allIp = localLogJDBCByDruid.getAllIp("1");
		for (String ip : allIp) {
			String[] split = ip.split(",");
			for (int i = 0; i < split.length; i++) {
				int first = Integer.parseInt(split[0]);
				int second = Integer.parseInt(split[1]);
				int third = Integer.parseInt(split[2]);
				int fourth = Integer.parseInt(split[3]);

				if (map.containsKey(first)) {
					Map<Integer, Map<Integer, List<Integer>>> secondMap = map.get(first);
					if (secondMap.containsKey(second)) {
						Map<Integer, List<Integer>> thridMap = secondMap.get(second);
						if (thridMap.containsKey(third)) {
							thridMap.get(third).add(fourth);
						} else {
							List<Integer> list = new ArrayList<>();
							list.add(fourth);
							thridMap.put(third, list);
						}
					} else {
						Map<Integer, List<Integer>> thridMap = new HashMap<>();
						List<Integer> list = new ArrayList<>();
						list.add(fourth);
						thridMap.put(third, list);
						secondMap.put(second, thridMap);
					}
				} else {
					Map<Integer, Map<Integer, List<Integer>>> secondMap = new HashMap<>();
					Map<Integer, List<Integer>> thridMap = new HashMap<>();
					List<Integer> list = new ArrayList<>();
					list.add(fourth);
					thridMap.put(third, list);
					secondMap.put(second, thridMap);
					map.put(first, secondMap);
				}
			}
		}

		if (map.size() > 0) {

			for (Entry<Integer, Map<Integer, Map<Integer, List<Integer>>>> entry : map.entrySet()) {
				Integer key = entry.getKey();

			}

		}
	}

}

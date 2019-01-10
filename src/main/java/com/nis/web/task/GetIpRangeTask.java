package com.nis.web.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nis.domain.restful.NtcIpRangeReport;
import com.nis.util.Constants;
import com.nis.util.ExceptionUtil;
import com.nis.web.dao.NtcReportDao;
import com.nis.web.dao.impl.LocalLogJDBCByDruid;
import com.zdjizhi.utils.IPUtil;
import com.zdjizhi.utils.IpLookup;

@Component
@PropertySource(value = { "classpath:nis.properties", "classpath:jdbc.properties" })
public class GetIpRangeTask {

	private static Logger logger = LoggerFactory.getLogger(GetIpRangeTask.class);
	@Autowired
	private LocalLogJDBCByDruid localLogJDBCByDruid;

	@Autowired
	protected NtcReportDao dao;

	private static IpLookup ipLookup = new IpLookup.Builder(false).loadDataFileV4(Constants.IPLOCATIONLIBRARYPATH)
			.loadDataFileV6(Constants.IPLOCATIONLIBRARYPATH).build();

//	@Scheduled(cron = "${getIpRangeTaskCron}")
	public void getIpRangeTask() {
		try {
			// 删除数据,重新全量导入
			dao.truncateNtcIpRange();
			getAllIp(1);
			getAllIp(2);
		} catch (Exception e) {
			logger.error("处理ip范围数据失败,失败原因{}", ExceptionUtil.getExceptionMsg(e));
		}
	}

	/**
	 * 将ip转换为数字与ip的对应关系,key是ip的数字形式
	 * 
	 * @param allIp
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public Map<Long, String> getNumAndIpReal(List<String> allIp) throws InterruptedException, ExecutionException {
		logger.info("开始将ip转换为数字");
		Map<Long, String> map = new  HashMap<>();
		int ever = 10000;// 每个线程处理10000条ip转数字
		int count = allIp.size() / ever;
		if (allIp.size() % ever != 0) {
			count++;
		}
		List<Future<Map<Long, String>>> futures = new ArrayList<>();
		ExecutorService executor = Executors.newFixedThreadPool(35);// 10个线程
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
		for (Future<Map<Long, String>> future : futures) {
			try {
				map.putAll(future.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		logger.info("{}个ip转换为数字成功,转换了{}个", allIp.size(), map.size());
		return map;
	}

	public static void main(String[] args) {
//		a();
		String ip = "2.72.0.0";
		long currentTimeMillis = System.currentTimeMillis();
		System.out.println(ipLookup.countryLookup(ip));
//		System.out.println(ipLookup.cityLookup(ip));
//		System.out.println(ipLookup.cityLatLngLookup(ip));
//		System.out.println(ipLookup.cityLookupDetail(ip));
		long end = System.currentTimeMillis();
		System.out.println(end-currentTimeMillis);

	}

	public static void b() {
		long[] arr = { 1l, 2l, 3l, 5l, 6l, 7l };
//	String groupByNumer = test(arr);
		String groupByNumer = groupByNumer(arr);
		String[] split = org.apache.commons.lang.StringUtils.split(groupByNumer, ",");
		for (String str : split) {
			System.out.println(str);
		}
	}

	public static void a() {
		int number = 2000000;
		int range = number + 1;
		long start = System.currentTimeMillis();
		Set<Long> hashSet = new LinkedHashSet<>();
		Random random = new Random();
		for (int i = 0; i < number; i++) {
			hashSet.add(Long.parseLong(random.nextInt(range) + ""));
		}
		long end1 = System.currentTimeMillis();
		System.out.println("生成数组需要" + (end1 - start) / 1000 + "秒");
		System.out.println(hashSet.size());
		long[] arr = new long[hashSet.size()];
		int i = 0;
		for (long l : hashSet) {
			arr[i] = l;
			i++;
		}
		long end2 = System.currentTimeMillis();
		System.out.println("set转数组需要" + (end2 - end1) / 1000 + "秒");
		Arrays.sort(arr);
		long end3 = System.currentTimeMillis();
		System.out.println("排序需要" + (end3 - end2) / 1000 + "秒");
		String groupByNumer = groupByNumer(arr);
		long end4 = System.currentTimeMillis();
		System.out.println("获取连续数字需要" + (end4 - end3) / 1000 + "秒");
		String[] split = org.apache.commons.lang.StringUtils.split(groupByNumer, ",");
		long end5 = System.currentTimeMillis();
		System.out.println("切割需要" + (end5 - end4) / 1000 + "秒");
		for (String str : split) {
			System.out.println(str);
		}
	}

	/**
	 * 将数组中连续的数据合并在一起输出,例如传入1,2,3,5,6,7,9,11输出1-3,4-7,9,11 .注意传入的数组必须是有序的,从小到大排序
	 * 
	 * @param noNum
	 * @return
	 */
	private static String groupByNumer(long[] sortArr) {
		int state = 0;
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < sortArr.length; i++) {
			if (i == sortArr.length - 1) {
				state = 2;
			}
			if (state == 0) {
				if (sortArr[i + 1] == sortArr[i] + 1) {
					result.append(sortArr[i]);
					result.append("-");
					state = 1;
				} else {
					result.append(sortArr[i]);
					result.append(",");

				}
			} else if (state == 1) {
				if (sortArr[i + 1] != sortArr[i] + 1) {
					result.append(sortArr[i]);
					result.append(",");
					state = 0;
				}
			} else {
				result.append(sortArr[i]);
			}
		}
		return result.toString();
	}

	public static String test(long[] sortArr) {
		int state = 0;
		String result = "";
		for (int i = 0; i < sortArr.length; i++) {
			if (i == sortArr.length - 1)
				state = 2;
			if (state == 0) {
				if (sortArr[i + 1] == sortArr[i] + 1) {
					result += String.valueOf(sortArr[i]);
					result += "-";
					state = 1;
				} else {
					result += String.valueOf(sortArr[i]);
					result += ",";
				}
			} else if (state == 1) {
				if (sortArr[i + 1] != sortArr[i] + 1) {
					result += String.valueOf(sortArr[i]);
					result += ",";
					state = 0;
				}
			} else {
				result += String.valueOf(sortArr[i]);
			}
		}
		return result;

	}

	public void getAllIp(Integer frontier) throws Exception {
		if (frontier == 1) {
			logger.info("开始获取境内ip");
		} else {
			logger.info("开始获取境外ip");
		}
		List<String> allIp = localLogJDBCByDruid.getAllIp(frontier);
		if (frontier == 1) {
			logger.info("获取境内ip成功,共{}条", allIp.size());
		} else {
			logger.info("获取境外ip成功,共{}条", allIp.size());
		}
		Map<Long, String> map = getNumAndIpReal(allIp);
		Set<Long> keySet = map.keySet();
		long[] array = new long[keySet.size()];
		int index = 0;
		for (long l : keySet) {
			array[index] = l;
			index++;
		}
		Arrays.sort(array);
		long startTime = System.currentTimeMillis();
		String result = groupByNumer(array);
		long endTime = System.currentTimeMillis();
		logger.info("解析连续数字用时{}秒", (endTime - startTime) / 1000);
//		String result = null;
		if (result != null && !result.trim().equals("")) {
			String substring = result;
			if (result.endsWith(",")) {
				substring = result.substring(0, result.length() - 1);
			}
			String[] split = substring.split(",");
			List<Long> list = new ArrayList<>();
			List<NtcIpRangeReport> ntcIpRangeReportList = new ArrayList<>();
			for (String str : split) {
				String[] ipAddr = str.split("-");
				if (ipAddr.length > 1) {
					String startIp = ipAddr[0];
					String endIp = ipAddr[1];
					try {
						NtcIpRangeReport ntcIpRangeReport = new NtcIpRangeReport();
						String startIpStr = map.get(Long.parseLong(startIp));
						String endIpStr = map.get(Long.parseLong(endIp));
						ntcIpRangeReport.setIpStart(startIpStr);
						ntcIpRangeReport.setIpEnd(endIpStr);
						ntcIpRangeReport.setIpEndNum(Long.parseLong(endIp));
						ntcIpRangeReport.setIpStartNum(Long.parseLong(startIp));
						ntcIpRangeReport.setIpSub(IPUtil.getMask(startIpStr, endIpStr));
						ntcIpRangeReport.setCountry(ipLookup.countryLookup(startIpStr));
						ntcIpRangeReport.setDesc(ipLookup.cityLookup(startIpStr));
						ntcIpRangeReport.setAreaType(frontier);
						ntcIpRangeReportList.add(ntcIpRangeReport);
					} catch (Exception e) {
						logger.error("数据转NtcIpRangeReport,失败原因:{}", ExceptionUtil.getExceptionMsg(e));
					}

				} else {
					list.add(Long.parseLong(str));
				}
			}
			Collections.sort(list);
			for (int i = 0; i < list.size(); i = i + 2) {
				System.out.print(list.get(i) + "[" + map.get(list.get(i)) + "]");
				if (i < list.size() - 1) {
					Long startIp = list.get(i);
					Long endIp = list.get(i + 1);
					try {
						NtcIpRangeReport ntcIpRangeReport = new NtcIpRangeReport();
						String startIpStr = map.get(startIp);
						String endIpStr = map.get(endIp);
						ntcIpRangeReport.setIpStart(startIpStr);
						ntcIpRangeReport.setIpEnd(endIpStr);
						ntcIpRangeReport.setIpEndNum(endIp);
						ntcIpRangeReport.setIpStartNum(startIp);
						ntcIpRangeReport.setAreaType(frontier);
						ntcIpRangeReport.setIpSub(IPUtil.getMask(startIpStr, endIpStr));
						ntcIpRangeReport.setCountry(ipLookup.countryLookup(startIpStr));
						ntcIpRangeReport.setDesc(ipLookup.cityLookup(startIpStr));
						ntcIpRangeReportList.add(ntcIpRangeReport);
					} catch (Exception e) {
						logger.error("数据转NtcIpRangeReport,失败原因:{}", ExceptionUtil.getExceptionMsg(e));
					}
				}
			}
			int size = ntcIpRangeReportList.size();
			if (size > 0) {
				int ever = 1000;// 每个线程处理10000条ip转数字
				int count = size / ever;
				if (size % ever != 0) {
					count++;
				}
				for (int i = 0; i < count; i++) {
					int start = i * ever;
					int end = i * ever + ever;
					if (end > size) {
						end = size;
					}
					dao.insertNtcIpRangeBatch(ntcIpRangeReportList.subList(start, end));
				}
			}

		}
	}

	public String longToIp(long ip) {
		return ((ip >> 24) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + (ip & 0xFF);
	}

}

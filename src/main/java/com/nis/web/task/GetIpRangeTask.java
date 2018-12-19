package com.nis.web.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	public void test() {
		try {
			// 删除数据,重新全量导入
			dao.truncateNtcIpRange();
			getAllIp(1);
			getAllIp(2);
		} catch (Exception e) {
			logger.error("处理ip范围数据失败,失败原因{}", ExceptionUtil.getExceptionMsg(e));
		}
	}

	public Map<Long, String> getNumAndIpReal(List<String> allIp) throws InterruptedException, ExecutionException {
		logger.info("开始将ip转换为数字");
		Map<Long, String> map = new HashMap<>();
		int ever = 10000;// 每个线程处理10000条ip转数字
		int count = allIp.size() / ever;
		if (allIp.size() % ever != 0) {
			count++;
		}
		List<Future<Map<Long, String>>> futures = new ArrayList<>();
		ExecutorService executor = Executors.newFixedThreadPool(10);// 10个线程
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

	/**
	 * 将数组中连续的数据合并在一起输出,例如传入1,2,3,5,6,7,9,11输出1-3,4-7,9,11 .注意传入的数组必须是有序的,从小到大排序
	 * 
	 * @param noNum
	 * @return
	 */
	private static String groupByNumer(Long[] sortArr) {
		int state = 0;
		String result = "";
		for (int i = 0; i < sortArr.length; i++) {
			if (i == sortArr.length - 1)
				state = 2;
			if (state == 0) {
				if (sortArr[i + 1].longValue() == sortArr[i].longValue() + 1) {
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
		List<String> allIp = localLogJDBCByDruid.getAllIp(frontier);
		Map<Long, String> map = getNumAndIpReal(allIp);
		Set<Long> keySet = map.keySet();
		List<Long> ipList = new ArrayList<>(keySet);
		Long[] array = new Long[ipList.size()];
		Collections.sort(ipList);
		ipList.toArray(array);
		String result = groupByNumer(array);
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
					NtcIpRangeReport ntcIpRangeReport = new NtcIpRangeReport();
					String startIpStr = map.get(Long.parseLong(startIp));
					String endIpStr = map.get(Long.parseLong(endIp));
					ntcIpRangeReport.setIpStart(startIpStr);
					ntcIpRangeReport.setIpEnd(endIpStr);
					ntcIpRangeReport.setIpEndNum(Long.parseLong(endIp));
					ntcIpRangeReport.setIpStartNum(Long.parseLong(startIp));
					ntcIpRangeReport.setIpSub(IPUtil.getMask(startIpStr, endIpStr));
					ntcIpRangeReport.setCountry(ipLookup.countryLookup(startIpStr));
					ntcIpRangeReport.setAreaType(frontier);
					ntcIpRangeReportList.add(ntcIpRangeReport);
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
					ntcIpRangeReportList.add(ntcIpRangeReport);
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

package com.nis.web.service.restful;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nis.domain.MaatXmlExpr;

import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

public class CommRegionThreadMethod {
	private static Logger logger = LoggerFactory.getLogger(CommRegionThreadMethod.class);

	protected void updateMaatInfoByPipelined(List<MaatXmlExpr> list, String maatKey, Pipeline pipelined,
			Double maatVersion, int redisDBIndex, boolean idDel) {
		if (list != null && list.size() > 0) {
			for (MaatXmlExpr maatXmlExpr : list) {
				if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_UPDATE_STATUS")) {
					if (maatKey != null) {
						String zset = null;
						if (idDel) {
							zset = maatKey.replace("OBSOLETE_RULE:", "DEL,");
						} else {
							zset = maatKey.replace("EFFECTIVE_RULE:", "ADD,");
						}
						pipelined.zadd("MAAT_UPDATE_STATUS", maatVersion, zset);
						logger.info("向{}号redis数据库更新了MAAT_UPDATE_STATUS,内容是{},SCORES是{}", redisDBIndex,
								zset.toUpperCase(), maatVersion);
					}
				} else if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_RULE_TIMER")) {
					if (maatKey != null) {
						Double score = 0d;// 界面下发的配置没有超时时间所以这里设置为0
						pipelined.zadd("MAAT_RULE_TIMER", score, maatKey);
						logger.info("向{}号redis数据库更新了MAAT_RULE_TIMER,内容是{},SCORES是{}", redisDBIndex, maatKey, score);

					}
				} else if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_VERSION_TIMER")) {
					Long nowTime = new Date().getTime();
					nowTime = nowTime / 1000l;
					Double score = nowTime.doubleValue();// 使用redis自带的time,得到当前时间的秒
					pipelined.zadd("MAAT_VERSION_TIMER", score, maatVersion + "");
					logger.info("向{}号redis数据库更新了MAAT_VERSION_TIMER,内容是{},SCORES是{}", redisDBIndex, maatVersion, score);
				}
			}

		}

	}

	protected void updateMaatInfo(List<MaatXmlExpr> list, String maatKey, Transaction transaction, Double maatVersion,
			int redisDBIndex, boolean idDel) {
		if (list != null && list.size() > 0) {
			for (MaatXmlExpr maatXmlExpr : list) {
				if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_UPDATE_STATUS")) {
					if (maatKey != null) {
						String zset = null;
						if (idDel) {
							zset = maatKey.replace("OBSOLETE_RULE:", "DEL,");
						} else {
							zset = maatKey.replace("EFFECTIVE_RULE:", "ADD,");
						}
						transaction.zadd("MAAT_UPDATE_STATUS", maatVersion, zset);
						logger.info("向{}号redis数据库更新了MAAT_UPDATE_STATUS,内容是{},SCORES是{}", redisDBIndex,
								zset.toUpperCase(), maatVersion);
					}
				} else if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_RULE_TIMER")) {
					if (maatKey != null) {
						Double score = 0d;// 界面下发的配置没有超时时间所以这里设置为0
						transaction.zadd("MAAT_RULE_TIMER", score, maatKey);
						logger.info("向{}号redis数据库更新了MAAT_RULE_TIMER,内容是{},SCORES是{}", redisDBIndex, maatKey, score);

					}
				} else if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_VERSION_TIMER")) {
					Long nowTime = new Date().getTime();
					nowTime = nowTime / 1000l;
					Double score = nowTime.doubleValue();// 使用redis自带的time,得到当前时间的秒
					transaction.zadd("MAAT_VERSION_TIMER", score, maatVersion + "");
					logger.info("向{}号redis数据库更新了MAAT_VERSION_TIMER,内容是{},SCORES是{}", redisDBIndex, maatVersion, score);
				}
			}
		}
	}
}

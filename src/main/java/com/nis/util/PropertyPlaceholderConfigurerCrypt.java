package com.nis.util;

import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.nis.crypt.AESUtil;

public class PropertyPlaceholderConfigurerCrypt extends PropertyPlaceholderConfigurer {

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {

		try {
			String productPassword = props.getProperty("jdbc.product.password");
			String productScretKey = props.getProperty("jdbc.product.key");
			if (null != productPassword) {
				props.setProperty("jdbc.product.password",
						new String(AESUtil.decrypt(Base64.decodeBase64(productPassword), productScretKey)));
			}

			String devlopPassword = props.getProperty("jdbc.devlop.password");
			String devlopScretKey = props.getProperty("jdbc.devlop.key");
			if (null != devlopPassword) {
				props.setProperty("jdbc.devlop.password",
						new String(AESUtil.decrypt(Base64.decodeBase64(devlopPassword), devlopScretKey)));
			}

			String logPassword = props.getProperty("jdbc.log.password");
			String logScretKey = props.getProperty("jdbc.log.key");
			if (null != logPassword) {
				props.setProperty("jdbc.log.password",
						new String(AESUtil.decrypt(Base64.decodeBase64(logPassword), logScretKey)));
			}
			
			// 日志A版
			String logAPassword = props.getProperty("jdbc.logA.password");
			String logAScretKey = props.getProperty("jdbc.logA.key");
			if (null != logAPassword) {
				props.setProperty("jdbc.logA.password",
						new String(AESUtil.decrypt(Base64.decodeBase64(logAPassword), logAScretKey)));
			}
			// 日志A版
			String logCPassword = props.getProperty("jdbc.logC.password");
			String logCScretKey = props.getProperty("jdbc.logC.key");
			if (null != logAPassword) {
				props.setProperty("jdbc.logC.password",
						new String(AESUtil.decrypt(Base64.decodeBase64(logCPassword), logCScretKey)));
			}
			

			// 测试使用,后期会删除
			String testPassword = props.getProperty("jdbc.test.password");
			String testScretKey = props.getProperty("jdbc.test.key");
			if (null != testPassword) {
				props.setProperty("jdbc.test.password",
						new String(AESUtil.decrypt(Base64.decodeBase64(testPassword), testScretKey)));
			}
			
			String jkPzPassword = props.getProperty("jdbc.jk.password");
			String jkPzScretKey = props.getProperty("jdbc.jk.key");
			if (null != jkPzPassword) {
				props.setProperty("jdbc.jk.password",
						new String(AESUtil.decrypt(Base64.decodeBase64(jkPzPassword), jkPzScretKey)));
			}
			//A版hive库
			String hiveAPassword = props.getProperty("jdbc.hiveA.password");
			String hiveAScretKey = props.getProperty("jdbc.hiveA.key");
			if (null != hiveAPassword) {
				props.setProperty("jdbc.hiveA.password",
						new String(AESUtil.decrypt(Base64.decodeBase64(hiveAPassword), hiveAScretKey)));
			}
			
			//B版hive库
			String hiveBPassword = props.getProperty("jdbc.hiveB.password");
			String hiveBScretKey = props.getProperty("jdbc.hiveB.key");
			if (null != hiveBPassword) {
				props.setProperty("jdbc.hiveB.password",
						new String(AESUtil.decrypt(Base64.decodeBase64(hiveBPassword), hiveBScretKey)));
			}
			
			//神通数据库库
			String clusterPassword = props.getProperty("jdbc.log.cluster.password");
			String clusterScretKey = props.getProperty("jdbc.log.cluster.key");
			if (null != clusterPassword) {
				props.setProperty("jdbc.log.cluster.password",
						new String(AESUtil.decrypt(Base64.decodeBase64(clusterPassword), clusterScretKey)));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.processProperties(beanFactoryToProcess, props);
	}

}

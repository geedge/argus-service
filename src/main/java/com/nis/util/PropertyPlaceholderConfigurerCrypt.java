package com.nis.util;

import java.util.Properties;

import com.zdjizhi.crypt.AESUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PropertyPlaceholderConfigurerCrypt extends PropertyPlaceholderConfigurer {

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {

		try {
			// mysql
			String logPassword = props.getProperty("jdbc.log.password");
			String logScretKey = props.getProperty("jdbc.log.key");
			if (null != logPassword) {
				props.setProperty("jdbc.log.password",
						new String(AESUtil.decrypt(Base64.decodeBase64(logPassword), logScretKey)));
			}

			// clickhouse
			String clickHousePassword = props.getProperty("jdbc.clickhouse.password");
			String clickHouseScretKey = props.getProperty("jdbc.clickhouse.key");
			if (null != clickHousePassword) {
				props.setProperty("jdbc.clickhouse.password",
						new String(AESUtil.decrypt(Base64.decodeBase64(clickHousePassword), clickHouseScretKey)));
			}
			
			
			
			
			
			String ckLocalPassword = props.getProperty("jdbc.ckLocal.password");
			String ckLocalScretKey = props.getProperty("jdbc.ckLocal.key");
			if (null != ckLocalPassword) {
				props.setProperty("jdbc.ckLocal.password",
						new String(AESUtil.decrypt(Base64.decodeBase64(ckLocalPassword), ckLocalScretKey)));
			}
			
			
			
			// hive
			String hivePassword = props.getProperty("jdbc.hive.password");
			String hiveScretKey = props.getProperty("jdbc.hive.key");
			if (null != clickHousePassword) {
				props.setProperty("jdbc.hive.password",
						new String(AESUtil.decrypt(Base64.decodeBase64(hivePassword), hiveScretKey)));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		super.processProperties(beanFactoryToProcess, props);
	}

}

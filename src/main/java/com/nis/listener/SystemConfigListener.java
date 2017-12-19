package com.nis.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.nis.datasource.CustomerContextHolder;
import com.nis.domain.restful.DataDictionaryValue;
import com.nis.web.service.restful.DataDictionaryService;

/**
 * 项目启动时加载数据字典
 * 
 * @author RenKaiGe-Office
 * @Date:2016-11-03
 */
public class SystemConfigListener extends ContextLoaderListener {
	private DataDictionaryService dataDictionaryService;
	private static WebApplicationContext webApplicationContext;
	private static Map<String, List<DataDictionaryValue>> dictionaryMap = new HashMap<String, List<DataDictionaryValue>>();

	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_B);
			webApplicationContext = WebApplicationContextUtils
					.getRequiredWebApplicationContext(event.getServletContext());
			dataDictionaryService = (DataDictionaryService) webApplicationContext.getBean(DataDictionaryService.class);
			loadDataDict();
			CustomerContextHolder.clearCustomerType();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadDataDict() {
		dictionaryMap = dataDictionaryService.getAllDataDict();
	}

	public static List<DataDictionaryValue> getDictValue(String key) {
		return dictionaryMap.get(key);
	}

	public static Map<String, List<DataDictionaryValue>> getDictionaryMap() {
		return dictionaryMap;
	}

	public static void setDictionaryMap(Map<String, List<DataDictionaryValue>> dictionaryMap) {
		SystemConfigListener.dictionaryMap = dictionaryMap;
	}
	
	
	
}

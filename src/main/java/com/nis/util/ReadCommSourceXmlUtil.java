/**
 * 
 */
package com.nis.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nis.domain.restful.CommonSourceFieldCfg;

/**
 * @ClassName:ReadCommSourceXmlUtil
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年5月19日 下午7:57:52
 * @version V1.0
 */
public class ReadCommSourceXmlUtil {
	private static Logger logger = LoggerFactory.getLogger(ReadCommSourceXmlUtil.class);
	private static String XMLPATH = "/commonSources/applicationConfig-parser.xml";
	public static Map<String, List<CommonSourceFieldCfg>> commonSourceCfgMap = new HashMap<String, List<CommonSourceFieldCfg>>();

	static {
		readCfgXml();
	}


	public static void main(String[] args) {
		try {
			readCfgXml();
			Set<String> keySet = commonSourceCfgMap.keySet();
			for (String key : keySet) {
				List<CommonSourceFieldCfg> fieldCfgList = commonSourceCfgMap.get(key);
				for (CommonSourceFieldCfg commonSourceFieldCfg : fieldCfgList) {
					System.out.println("srcName:"+commonSourceFieldCfg.getSrcName()+",dstName:"+commonSourceFieldCfg.getDstName()+",val:"+commonSourceFieldCfg.getRegexp());
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<CommonSourceFieldCfg> getCommonSourceCfgByService(String service) {
		return commonSourceCfgMap.get(service);
	}
	
	
	private static void readCfgXml() {
		try {

			InputStream resourceAsStream = ReadCommSourceXmlUtil.class.getResourceAsStream(XMLPATH);
			SAXReader reader = new SAXReader();
			Document read = reader.read(resourceAsStream);
			Element rootElement = read.getRootElement();
			if (rootElement != null && rootElement.elements().size() > 0) {
				List<Element> elements = rootElement.elements();
				for (Element element : elements) {
					String serviceIdsStr = element.attribute("serviceIds").getValue();
					if (serviceIdsStr != null && !serviceIdsStr.trim().equals("")) {
						String[] serviceIds = serviceIdsStr.split(",");
						for (String serviceId : serviceIds) {
							if (!StringUtil.isEmpty(serviceId.trim())) {
								List<CommonSourceFieldCfg> cfgList = new ArrayList<CommonSourceFieldCfg>();
								List<Element> fieldElmt = element.elements("field");
								for (Element field : fieldElmt) {
									CommonSourceFieldCfg fieldCfg = new CommonSourceFieldCfg();
									fieldCfg.setFieldType(field.attribute("fieldType").getValue());
									fieldCfg.setSrcName(field.attribute("srcName").getValue());
									fieldCfg.setDstName(field.attribute("dstName").getValue());
									if (!StringUtil.isEmpty(field.attribute("range"))) {
										fieldCfg.setRange(field.attribute("range").getValue());
									}
									if (!StringUtil.isEmpty(field.attribute("isRequired"))) {
										fieldCfg.setIsRequired(Boolean.valueOf(field.attribute("isRequired").getValue()));
									}
									if (!StringUtil.isEmpty(field.attribute("defaultVal"))) {
										fieldCfg.setDefaultVal(field.attribute("defaultVal").getValue());
									}
									if (!StringUtil.isEmpty(field.attribute("regexp"))) {
										fieldCfg.setRegexp(field.attribute("regexp").getValue());
									}
									cfgList.add(fieldCfg);
								}
								commonSourceCfgMap.put(serviceId.trim(), cfgList);
							}

						}

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载applicationConfig-parser.xml失败", e);
		}
	}
	
}

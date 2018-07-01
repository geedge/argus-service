package com.nis.util;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.util.XMLErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nis.domain.MaatXmlConfig;
import com.nis.domain.MaatXmlExpr;
import com.nis.domain.MaatXmlSeq;

/**
 * 
 * <p>Title: ReadMaatXmlUtil</p>
 * <p>Description: 根据xml定义的规则来读取,目前每次加载都重新加载一次xml,后面稳定后会放到listener中,项目启动的时候加载一次</p>
 * <p>Company: IIE</p>
 * @author rkg
 * @date 2018年5月17日 
 *
 */
public class ReadMaatXmlUtil {
	private static Logger logger = LoggerFactory.getLogger(ReadMaatXmlUtil.class);
	private static String XMLPATH = "/maatXml/maat.xml";
	private static String XSDPATH = "/maatXml/maat.xsd";
	public static Map<Integer, MaatXmlConfig> map = new HashMap<Integer, MaatXmlConfig>();

	static {
		readXml();
	}

	public static void main(String[] args) {
		try {
			// validateXmlByXSD();
			readXml();
			Set<Integer> keySet = map.keySet();
			for (Integer key : keySet) {
				MaatXmlConfig maatXmlConfig = map.get(key);
				List<MaatXmlExpr> expressionList = maatXmlConfig.getExpressionList();
				for (MaatXmlExpr maatXmlExpr : expressionList) {
					System.out.println("service=" + key + "    key=" + maatXmlExpr.getKeyExpression() + "   value="
							+ maatXmlExpr.getValueExpression());
				}
				List<MaatXmlSeq> sequenceList = maatXmlConfig.getSequenceList();
				for (MaatXmlSeq maatXmlSeq : sequenceList) {
					System.out.println("service=" + key + "     key=" + maatXmlSeq.getSequenceKey() + "   operation="
							+ maatXmlSeq.getOperation());
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void readXml() {
		try {

			InputStream resourceAsStream = ReadMaatXmlUtil.class.getResourceAsStream(XMLPATH);
			SAXReader reader = new SAXReader();
			Document read = reader.read(resourceAsStream);
			Element rootElement = read.getRootElement();
			if (rootElement != null && rootElement.elements().size() > 0) {
				List<Element> elements = rootElement.elements();
				for (Element element : elements) {
					String serviceStr = element.attribute("service").getValue();
					if (serviceStr != null && !serviceStr.trim().equals("")) {
						String[] split = serviceStr.split(";");
						for (String service : split) {
							if (serviceStr != null && !serviceStr.trim().equals("")) {
								List<MaatXmlSeq> seqList = new ArrayList<MaatXmlSeq>();
								List<Element> sequencesEle = element.elements("sequences");
								for (Element seqEle : sequencesEle) {
									MaatXmlSeq maatXmlSeq = new MaatXmlSeq();
									List<Element> eleList = seqEle.elements();
									for (Element ele : eleList) {
										if (ele.getName().toLowerCase().equals("operation")) {
											maatXmlSeq.setOperation(Integer.parseInt(ele.getTextTrim()));
										}
										// if (ele.getName().toLowerCase().equals("redisdb")) {
										// maatXmlSeq.setRedisDB(Integer.parseInt(ele.getTextTrim()));
										// }
										if (ele.getName().toLowerCase().equals("sequencekey")) {
											maatXmlSeq.setSequenceKey(ele.getTextTrim());
										}
									}
									seqList.add(maatXmlSeq);
								}
								List<MaatXmlExpr> exprList = new ArrayList<MaatXmlExpr>();
								List<Element> expressionsEle = element.elements("expressions");
								for (Element exprEle : expressionsEle) {
									MaatXmlExpr maatXmlExpr = new MaatXmlExpr();
									List<Element> eleList = exprEle.elements();
									for (Element ele : eleList) {
										if (ele.getName().toLowerCase().equals("keyexpression")) {
											maatXmlExpr.setKeyExpression(ele.getTextTrim());
											if (ele.getTextTrim().contains("compile_table_name")) {
												maatXmlExpr.setType(10);
											} else if (ele.getTextTrim().contains("group_table_name")) {
												maatXmlExpr.setType(11);
											} else if (ele.getTextTrim().contains("ip_region_table_name")) {
												maatXmlExpr.setType(12);
											} else if (ele.getTextTrim().contains("num_region_table_name")) {
												maatXmlExpr.setType(13);
											} else if (ele.getTextTrim().contains("str_str_region_table_name")) {
												maatXmlExpr.setType(15);
											} else if (ele.getTextTrim().contains("str_region_table_name")) {
												maatXmlExpr.setType(14);
											} else if (ele.getTextTrim().contains("file_digest_table_name")) {
												maatXmlExpr.setType(16);
											} else if (ele.getTextTrim().contains("file_like_table_name")) {
												maatXmlExpr.setType(17);
											} else if (ele.getTextTrim().contains("ip_client_range_table_name")) {
												maatXmlExpr.setType(18);
											} else if (ele.getTextTrim().contains("un_maat_table_name")) {
												maatXmlExpr.setType(0);
											}else{
												maatXmlExpr.setType(-1);
											}
										}
										if (ele.getName().toLowerCase().equals("valueexpression")) {
											maatXmlExpr.setValueExpression(ele.getTextTrim());
										}
										// if (ele.getName().toLowerCase().equals("redisdb")) {
										// maatXmlExpr.setRedisDB(Integer.parseInt(ele.getTextTrim()));
										// }

									}
									exprList.add(maatXmlExpr);
								}
								MaatXmlConfig maatXmlConfig = new MaatXmlConfig();
								maatXmlConfig.setService(Integer.parseInt(service));
								maatXmlConfig.setExpressionList(exprList);
								maatXmlConfig.setSequenceList(seqList);
								map.put(Integer.parseInt(service.trim()), maatXmlConfig);
							}

						}

					}
				}
			}
		} catch (Exception e) {
			logger.error("加载maat.xml失败", e);
		}
	}

	public static MaatXmlConfig getMaatConfigByService(int service) {
		return map.get(service);
	}

	private static void validateXmlByXSD() throws Exception {
		SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

		URL resource = ReadMaatXmlUtil.class.getResource(XSDPATH);
		Schema xsdSchema = schemaFactory.newSchema(resource);
		Validator newValidator = xsdSchema.newValidator();

		XMLErrorHandler xmlErrorHandler = new XMLErrorHandler();
		newValidator.setErrorHandler(xmlErrorHandler);
		InputStream resourceAsStream = ReadMaatXmlUtil.class.getResourceAsStream(XMLPATH);

		Source source = new StreamSource(resourceAsStream);
		newValidator.validate(source);
		XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
		if (xmlErrorHandler.getErrors().hasContent()) {
			System.out.println("校验xml失败");
			writer.write(xmlErrorHandler.getErrors());
		} else {
			System.out.println("校验xml成功");
		}

	}

}

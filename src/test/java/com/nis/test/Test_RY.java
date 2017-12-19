package com.nis.test;



import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.impl.OMNamespaceImpl;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

public class Test_RY {

	/**
	 * @param args
	 */
	// ====================新增 ========================start
	// TODO 将下面的地址改为ESB服务器提供的统一地址
	private static String endpoint = "http://10.55.0.197:7879/TongServiceProxy_doprocess/services/WebService1/";// 修改此url为服务管理系统的地址

	// TODO 将下面的命名空间修改为请求服务的命名空间
	private static String ns = "http://10.55.0.155:8080/ryService";

	// TODO 将下面的值修改为请求者在系统中的唯一标识
	private static String rid = "XFBL";

	// TODO 将下面的值修改为要请求的服务在系统中的唯一标识
	private static String sid = "s_2016_x2000";

	// TODO 将下面的值修改为本次请求的超时时间，单位秒
	private static String timeout = "10";

	// TODO 将下面的值修改为请求密码，如果使用其他加密方式，则根据要求统一修改即可
	private static String sec = "";

	// ====================新增 ========================start

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 原有业务逻辑不动
		RPCServiceClient serviceClient;
		int totalCount = -1;
		try {
			serviceClient = new RPCServiceClient();
			Options options = serviceClient.getOptions();
			options.setProperty(org.apache.axis2.transport.http.HTTPConstants.CONNECTION_TIMEOUT, new Integer(48000000));
			EndpointReference targetEPR = new EndpointReference(endpoint);

			// ====================新增 添加header ========================start
			serviceClient.addHeader(setHeader());
			// ====================新增 添加header ========================end

			// 原有业务逻辑不动
			options.setTo(targetEPR);
			QName opGetAllLegalInfor = new QName(ns,"getData");
			
			Object[] opArgs = new Object[] {"2","1",""};
			Class[] returnTypes = new Class[] { String.class };
			
			Object[] response = serviceClient.invokeBlocking(opGetAllLegalInfor, opArgs, returnTypes);
			
			System.out.println("rrrrr:"+response[0]);
			
			
			/*<returnData>
			<page>
				<currentPage>1</currentPage>
				<countPage>8039</countPage>
			</page>
			<data>
				<obj>
					<GMSFHM>110111197005314255</GMSFHM>
					<RYLB>013511</RYLB>
					<RYBH>81402011</RYBH>
					<XM>穆希坡</XM>
					<CYM>null</CYM>
					<XB>01001</XB>
					<JGID>4672</JGID>
					<SZBM>4682</SZBM>
					<MZ>null</MZ>
					<CSRQ>1970-05-31</CSRQ>
					<NL>null</NL>
					<ZZMM>null</ZZMM>
					<HYZK>null</HYZK>
					<CJGZSJ>null</CJGZSJ>
					<JRBJTSJ>null</JRBJTSJ>
					<CJDPSJ>null</CJDPSJ>
					<JRBDWSJ>null</JRBDWSJ>
					<BQYGL>null</BQYGL>
					<RYFL>3005701429</RYFL>
					<GZ>3020701771</GZ>
					<GWMC>3003700463</GWMC>
					<GRPYDJ>null</GRPYDJ>
					<RYZZBADJ>null</RYZZBADJ>
					<GJNGW>null</GJNGW>
					<SZGW>null</SZGW>
					<YGXS>null</YGXS>
					<CCLZRQ>null</CCLZRQ>
					<ZJCX>null</ZJCX>
					<JGZKH>null</JGZKH>
					<YGKGYH>null</YGKGYH>
					<YGKH>81402011</YGKH>
					<YGKXPXLH>null</YGKXPXLH>
					<GZKLX>null</GZKLX>
					<XJDJ>null</XJDJ>
					<GWGZLB>null</GWGZLB>
					<GZDC>null</GZDC>
					<XXJSXRQ>null</XXJSXRQ>
					<XGZDCSXRQ>null</XGZDCSXRQ>
					<XJJXGZLB>null</XJJXGZLB>
					<XMJP>mxp</XMJP>
					<XZZW>null</XZZW>
					<ZYMC>null</ZYMC>
					<ZYZGDJ>null</ZYZGDJ>
					<GLGZQSSJ>null</GLGZQSSJ>
					<JNDJ>null</JNDJ>
					<JNDJQDSJ>null</JNDJQDSJ>
					<ZZBADJ>null</ZZBADJ>
					<GLGPYDJ>null</GLGPYDJ>
					<ZYJSZW>null</ZYJSZW>
					<JRYBH>null</JRYBH>
					<DQZT>null</DQZT>
					<BZ>null</BZ>
					<YJLTSJ>2030-05-31</YJLTSJ>
					<ZT>1</ZT>
					<XGSJ>2014-12-04</XGSJ>
					<RYID>4503631</RYID>
				</obj>
				<obj>
					<GMSFHM>110111197412254050</GMSFHM>
					<RYLB>013511</RYLB>
					<RYBH>81402029</RYBH>
					<XM>邢祖跃</XM>
					<CYM>null</CYM>
					<XB>01001</XB>
					<JGID>4672</JGID>
					<SZBM>4680</SZBM>
					<MZ>null</MZ>
					<CSRQ>1974-12-25</CSRQ>
					<NL>null</NL>
					<ZZMM>null</ZZMM>
					<HYZK>null</HYZK>
					<CJGZSJ>null</CJGZSJ>
					<JRBJTSJ>null</JRBJTSJ>
					<CJDPSJ>null</CJDPSJ>
					<JRBDWSJ>null</JRBDWSJ>
					<BQYGL>null</BQYGL>
					<RYFL>3005701429</RYFL>
					<GZ>3020701771</GZ>
					<GWMC>3003700463</GWMC>
					<GRPYDJ>null</GRPYDJ>
					<RYZZBADJ>null</RYZZBADJ>
					<GJNGW>null</GJNGW>
					<SZGW>null</SZGW>
					<YGXS>null</YGXS>
					<CCLZRQ>null</CCLZRQ>
					<ZJCX>null</ZJCX>
					<JGZKH>null</JGZKH>
					<YGKGYH>null</YGKGYH>
					<YGKH>81402029</YGKH>
					<YGKXPXLH>null</YGKXPXLH>
					<GZKLX>null</GZKLX>
					<XJDJ>null</XJDJ>
					<GWGZLB>null</GWGZLB>
					<GZDC>null</GZDC>
					<XXJSXRQ>null</XXJSXRQ>
					<XGZDCSXRQ>null</XGZDCSXRQ>
					<XJJXGZLB>null</XJJXGZLB>
					<XMJP>xzy</XMJP>
					<XZZW>null</XZZW>
					<ZYMC>null</ZYMC>
					<ZYZGDJ>null</ZYZGDJ>
					<GLGZQSSJ>null</GLGZQSSJ>
					<JNDJ>null</JNDJ>
					<JNDJQDSJ>null</JNDJQDSJ>
					<ZZBADJ>null</ZZBADJ>
					<GLGPYDJ>null</GLGPYDJ>
					<ZYJSZW>null</ZYJSZW>
					<JRYBH>null</JRYBH>
					<DQZT>null</DQZT>
					<BZ>null</BZ>
					<YJLTSJ>2034-12-25</YJLTSJ>
					<ZT>1</ZT>
					<XGSJ>2014-12-04</XGSJ>
					<RYID>4503643</RYID>
				</obj>
			</data>
		</returnData>*/

			
			
		} catch (AxisFault e) {
			e.printStackTrace();
		}
	}

	/**
	 * 新增设置header信息，需要修改里面rid、sid对应的值、sid为服务唯一标识、 rid为请求者唯一标识
	 */
	public static OMElement setHeader() {
		OMFactory fac = OMAbstractFactory.getOMFactory();
		// OMNamespace指定此SOAP文档名称空间。
		OMNamespaceImpl omNs =  (OMNamespaceImpl)fac.createOMNamespace(ns, "ns1");
		
		// 创建header元素，并指定其在omNs指代的名称空间中,header名称固定为CyberpoliceSBReqHeader。
		OMElement method = fac.createOMElement("CyberpoliceSBReqHeader", omNs);

		// 指定元素的文本内容。
		OMElement ridE = fac.createOMElement("rid", omNs);
		// TODO将下面的值修改为请求者在系统中的唯一标识
		ridE.setText(rid);
		method.addChild(ridE);

		OMElement sidE = fac.createOMElement("sid", omNs);
		// TODO将下面的值修改要请求服务的唯一标识
		sidE.setText(sid);
		method.addChild(sidE);

		OMElement timeoutE = fac.createOMElement("timeout", omNs);
		// TODO将下面的值修改为请求的超时时间，单位秒
		timeoutE.setText(timeout);
		method.addChild(timeoutE);

		OMElement secE = fac.createOMElement("sec", omNs);
		// TODO将下面的值修改为请求密码，如果使用其他加密方式，则根据要求统一修改即可
		secE.setText(sec);
		method.addChild(secE);
		return method;
	}
}

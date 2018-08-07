<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
	<title>swagger日志管理</title>	
	<script type="text/javascript">
		$(document).ready(function(){
			$(".tooltips").tooltip();
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
<!-- 	<ul class="nav nav-tabs"> -->
<%-- 		<li class="active"><a href="${ctx}/sys/log/">日志列表</a></li> --%>
<!-- 	</ul> -->
	<form:form id="searchForm" action="${ctx}/sys/log/swaggerLogList" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
			<label>traceCode：</label><input id="traceCode" name="traceCode" type="text" maxlength="30" class="input-mini" value="${swaggerLog.traceCode}"/>
			<label>异常信息：</label><input id="exceptionInfo" name="exceptionInfo" type="text" maxlength="500" class="input-mini" value="${swaggerLog.exceptionInfo}"/>
			<label>请求ip：</label><input id="requestIp" name="requestIp" type="text" maxlength="50" class="input-mini" value="${swaggerLog.requestIp}"/>
			<label>服务端ip：</label><input id="serverIp" name="serverIp" type="text" maxlength="50" class="input-mini" value="${swaggerLog.serverIp}"/>
		</div><div style="margin-top:8px;">
			<label>请求日期范围：&nbsp;</label><input id="beginDate" name="beginDate" type="text" readonly="readonly" maxlength="20" class="input-mini Wdate"
				value="<fmt:formatDate value="${swaggerLog.beginDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			<label>&nbsp;--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label><input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="input-mini Wdate"
				value="<fmt:formatDate value="${swaggerLog.endDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>&nbsp;&nbsp;
			&nbsp;<label for="exception">
			
			<input id="exception" name="exception" type="checkbox"${swaggerLog.exception eq '1'?' checked':''} value="1"/>只查询异常信息</label>
			
			&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>请求内容</th><th>traceCode</th><th>请求方式</th><th>请求ip</th><th>服务端ip</th><th>请求时间</th></thead>
		<tbody><%request.setAttribute("strEnter", "\n");request.setAttribute("strTab", "\t");%>
		<c:forEach items="${page.list}" var="log">
			<tr>
				<td>
					<a data-original-title="${fn:escapeXml(log.requestContent) }" class="tooltips" data-flag="false"
					 data-html="true" data-placement="top">	${fn:substring(log.requestContent,0,20) }</a>	
				</td>
				<td>${log.traceCode}</td>
				<td>${log.opAction}</td>
				<td><strong>${log.requestIp}</strong></td>
				<td>${log.serverIp}</td>
				<td><fmt:formatDate value="${log.requestTime}" type="both"/></td>
			</tr>
			<c:if test="${not empty log.exceptionInfo}"><tr>
				<td colspan="8" style="word-wrap:break-word;word-break:break-all;">
<%-- 					用户代理: ${log.userAgent}<br/> --%>
<%-- 					提交参数: ${fns:escapeHtml(log.params)} <br/> --%>
					异常信息: <br/>
					${fn:replace(fn:replace(fns:escapeHtml(log.exceptionInfo), strEnter, '<br/>'), strTab, '&nbsp; &nbsp; ')}</td>
			</tr></c:if>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
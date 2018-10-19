<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
	<title>swagger日志管理</title>	
	<script type="text/javascript">
		$(document).ready(function(){
// 			$(".tooltips").tooltip();
			$("[data-toggle='popover']").popover();
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
	<form:form modelAttribute="swaggerLog" id="searchForm" action="${ctx}/sys/log/swaggerLogList" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>		
			<label>操作行为：</label>
<!-- 			<select name="opAction"  class="input-medium"> -->
<!-- 				<option value="" selected="selected">请选择</option> -->
<!-- 				<option value="1">插入</option> -->
<!-- 				<option value="2">更新</option> -->
<!-- 				<option value="3">删除</option> -->
<!-- 				<option value="4">查询</option> -->
<!-- 			</select> -->
			
			<form:select path="opAction" class="input-medium">
					<form:option value="">请选择</form:option>
					<form:option value="1">插入</form:option>
					<form:option value="2">更新</form:option>
					<form:option value="3">删除</form:option>
					<form:option value="4">查询</form:option>
			</form:select>
			
			
			<label>请求内容：</label><input id="requestContent" name="requestContent" type="text" class="input-mini" value="<c:out value="${swaggerLog.requestContent}"></c:out>"/>
			<label>TRACECODE：</label><input id="traceCode" name="traceCode" type="text" maxlength="30" class="input-mini" value="${swaggerLog.traceCode}"/>
			<label>BUSINESSCODE：</label><input id="businessCode" name="businessCode" type="text" maxlength="30" class="input-mini" value="${swaggerLog.businessCode}"/>
			<label>异常信息：</label><input id="exceptionInfo" name="exceptionInfo" type="text" maxlength="500" class="input-mini" value="${swaggerLog.exceptionInfo}"/>
			<label>请求ip：</label><input id="requestIp" name="requestIp" type="text" maxlength="50" class="input-mini" value="${swaggerLog.requestIp}"/>
			<label>服务端ip：</label><input id="serverIp" name="serverIp" type="text" maxlength="50" class="input-mini" value="${swaggerLog.serverIp}"/>
		</div><div style="margin-top:8px;">
			<label>请求日期范围：&nbsp;</label><input id="beginDate"  name="beginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${swaggerLog.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			<label>&nbsp;--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label><input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${swaggerLog.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>&nbsp;&nbsp;
			&nbsp;<label for="exception">
			
			<input id="exception" name="exception" type="checkbox" ${swaggerLog.exception eq '1'?' checked':''} value="1"/>只查询异常信息</label>
			
			&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;<label style="color: red">默认<strong style="font-size: 20px;">不显示查询类</strong>日志,如需显示请选择操作行为</label>
		</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>请求地址</th><th>请求内容</th><th>TRACECODE</th><th>BUSINESSCODE</th><th>操作行为</th><th>请求ip</th><th>服务端ip</th><th>请求时间</th></thead>
		<tbody><%request.setAttribute("strEnter", "\n");request.setAttribute("strTab", "\t");%>
		<c:forEach items="${page.list}" var="swaggerLog">
			<tr>
				<td>${swaggerLog.requestUri}</td>
				<td>
<%-- 					<a data-original-title="${fn:escapeXml(swaggerLog.requestContent) }" class="tooltips" data-flag="false" --%>
<%-- 					 data-placement="bottom">	${fn:substring(swaggerLog.requestContent,0,20) }</a>	 --%>
<%-- 					 <a title="${fn:escapeXml(swaggerLog.requestContent) }" class="tooltips" data-toggle="tooltip" --%>
<%-- 					 data-placement="top">	${fn:substring(swaggerLog.requestContent,0,20) }</a>	 --%>


						<a  title="${fn:escapeXml(swaggerLog.requestContent) }" data-container="body" data-toggle="popover"
						 data-placement="bottom">	${fn:substring(swaggerLog.requestContent,0,20) }</a>	
				</td>
				<td>${swaggerLog.traceCode}</td>
				<td>${swaggerLog.businessCode}</td>
				<td>
					<c:if test="${swaggerLog.opAction eq 1}">插入</c:if>
					<c:if test="${swaggerLog.opAction eq 2}">更新</c:if>
					<c:if test="${swaggerLog.opAction eq 3}">删除</c:if>
					<c:if test="${swaggerLog.opAction eq 4}">查询</c:if>
				
				</td>
				<td><strong>${swaggerLog.requestIp}</strong></td>
				<td>${swaggerLog.serverIp}</td>
				<td><fmt:formatDate value="${swaggerLog.requestTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			</tr>
			<c:if test="${not empty swaggerLog.exceptionInfo}"><tr>
				<td colspan="8" style="word-wrap:break-word;word-break:break-all;">
					异常信息: <br/>
					${fn:replace(fn:replace(fns:escapeHtml(swaggerLog.exceptionInfo), strEnter, '<br/>'), strTab, '&nbsp; &nbsp; ')}</td>
			</tr></c:if>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
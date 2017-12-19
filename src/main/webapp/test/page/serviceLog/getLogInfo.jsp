<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/test/common/taglib.jsp"%>
<%@ page session="false"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String id = request.getParameter("id");
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>系统日志详情</title>


<%@include file="/test/common/bootstrap_css_js.jsp"%>

<script type="text/javascript">
	$(function() {
		setLogInfo();
	})
	function setLogInfo() {
		$.ajax({
			type : 'post',
			url : '/gk/service/log/v1/getAllLog',
			data : "id="+<%=id%>,
			async : false,
			success : function(data, textStatus) {
				if (data.data.list.length > 0) {
					var html = '';
					var obj = data.data.list[0];
					$("#logId").val(obj.id);
					$("#requestContent").val(obj.requestContent);
					$("#exceptionInfo").val(obj.exceptionInfo);
					$("#operator").val(obj.operator);
					$("#version").val(obj.version);
					$("#opAction").val(convertOpt(obj.opAction));
					$("#opTime").val(getLocalTime(obj.opTime));
					$("#requestTime").val(getLocalTime(obj.requestTime));
					$("#consumerTime").val(obj.consumerTime);
					$("#requestIp").val(obj.requestIp);
					$("#businessCode").val(obj.businessCode);
				}
			}
		})

	}
	
	function convertOpt(info) {
		if (info != null && info == "1") {
			return "插入";
		} else if (info != null && info == "2") {
			return "修改";
		} else if (info != null && info == "3") {
			return "删除";
		} else if (info != null && info == "4") {
			return "查询";
		}
	}
	
	function getLocalTime(ns) {
		if (ns != null && ns != "") {
			//return new Date(parseInt(ns)).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
			//return new Date(parseInt(ns)).toLocaleString().replace(/:\d{1,2}$/," ");
			return new Date(parseInt(ns)).toLocaleString();
		}
		return "";

	}

	function getAllLog(){
		window.location.href = '<%=basePath%>test/page/serviceLog/getAllLog.jsp';//
	}

	function closePage() {
		window.open("about:blank", "_self").close();
	}
</script>

</head>
<body style="background-color: #2D3233">
	<div class="row">
		<div class="col-md-12" style="height: 800px">
			<!-- BEGIN EXAMPLE TABLE PORTLET-->
			<div class="portlet light">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-cogs font-green-sharp"></i> <span id="spanText"
							class="caption-subject font-green-sharp bold uppercase">系统日志详情</span>
					</div>
				</div>
				<div class="portlet-body form">
					<!-- BEGIN FORM-->
					<form action="/gk/service/cfg/v1/addDictName"
						class="form-horizontal" method="post" name="taskMainForm"
						enctype="multipart/form-data">
						<input type="hidden" value="add" id="addOrUpdate"> <input
							type="hidden" id="dictId">
						<div class="form-body">
							<div class="form-group">
								<label class="control-label">&nbsp;&nbsp;&nbsp;id:<span class="required">
										* </span></label> <input class="textarea" id="logId" readonly="readonly" />
							</div>
							<div class="form-group">
								<label class="control-label">&nbsp;操作人:<span class="required">
										* </span></label> <input class="textarea" id="operator" readonly="readonly" />
							</div>
							<div class="form-group">
								<label class="control-label">请求时间:<span class="required">
										* </span></label> <input class="textarea" id="requestTime" readonly="readonly" />
							</div>
							<div class="form-group">
								<label class="control-label">请求内容:<span class="required">
										* </span></label>
								<textarea class="textarea" id="requestContent"
									style="width: 500px; height: 500px;" readonly="readonly"></textarea>
							</div>

							<div class="form-group">
								<label class="control-label">处理结果:<span class="required">
										* </span></label>
								<textarea style="width: 500px; height: 100px;" class="textarea"
									id="exceptionInfo" readonly="readonly"></textarea>
							</div>
							<div class="form-group">
								<label class="control-label">&nbsp;&nbsp;版本:<span class="required">
										* </span></label> <input class="textarea" id="version" readonly="readonly" />
							</div>
							<div class="form-group">
								<label class="control-label">操作行为:<span class="required">
										* </span></label> <input class="textarea" id="opAction" readonly="readonly" />
							</div>
							<div class="form-group">
								<label class="control-label">操作时间:<span class="required">
										* </span></label> <input class="textarea" id="opTime" readonly="readonly" />
							</div>
							<div class="form-group">
								<label class="control-label">处理时间:<span class="required">
										* </span></label> <input class="textarea" id="consumerTime" readonly="readonly" />
							</div>
							<div class="form-group">
								<label class="control-label">&nbsp;请求ip:<span class="required">
										* </span></label> <input class="textarea" id="requestIp" readonly="readonly" />
							</div>
							<div class="form-group">
								<label class="control-label">服务状态码:<span class="required">
										* </span></label> <input class="textarea" id="businessCode" readonly="readonly" />
							</div>
							

							<div class="form-group">
								<label class="control-label col-md-6">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
								<div class="form-group">
<!-- 									<a href="javascript:window.opener=null;window.close();"class="btn btn-success">关闭 </a>  -->
									<a onclick="getAllLog()" class="btn btn-success">返回 </a>
								</div>
							</div>

						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
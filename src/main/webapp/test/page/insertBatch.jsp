<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.nis.util.*"%>
<%@include file="/test/common/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="cn" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="cn" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="cn" class="no-js">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<base href="<%=basePath%>">
<meta charset="utf-8">
<title>批量提交</title>
<%@include file="/test/common/bootstrap_css_js.jsp"%>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport">
<meta content="" name="description">
<meta content="" name="author">
<link rel="shortcut icon" href="favicon.ico">

<!-- start link CSS and JS -->


<link rel="stylesheet" type="text/css"
	href="<c:url value='/test/page/css/daterangepicker-bs3.css'/>">
<script type="text/javascript"
	src="<c:url value='/test/page/js/moment.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/test/page/js/daterangepicker.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/test/page/js/dateRangePickAttr.js'/>"></script>

<script type="text/javascript"
	src="<c:url value='/test/page/js/dateRangePickAttr.js'/>"></script>
<script
	src="<c:url value='/test/js/bootstrap-menu/BootstrapMenu.min.js'/>"
	type="text/javascript"></script>


<script type="text/javascript">
	function setActionVal(obj) {
		var objVal = $(obj).val();
		var action = '0';
		var zeroArr = [ '1', '3', '4', '5', '6', '7', '8', '9', '10', '11',
				'12', '13', '14' ];
		var oneArr = [ '15', '16', '32', '33', '48', '49', '50', '51', '52',
				'53', '54', '55', '56', '57', '58', '59', '60' ];
		var twoArr = [ '2' ];
		if (zeroArr.indexOf(objVal) != -1) {
			action = '0';
		} else if (oneArr.indexOf(objVal) != -1) {
			action = '1';
		} else if (twoArr.indexOf(objVal) != -1) {
			action = '2';
		}
		$("#action").val(action);
	}

	function submitForm() {

		var timeRange = $("#reservationtime").val();
		var timeArr = timeRange.split("to");
		$("#startTime").val($.trim(timeArr[0]));
		$("#endTime").val($.trim(timeArr[1]));
		//document.form2.submit();
		var configCount = $("#configCount").val();
		if (configCount == "") {
			alert("请输入增加条数");

		} else if ($("#endTime").val() == "") {
			alert("请选择时间");
		} else {
			$("#form2").submit();
		}
	}
</script>

<!-- end link CSS and JS -->
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body style="background-color: #2D3233">
	<!-- BEGIN PAGE CONTENT INNER -->
	<div class="row">
		<div class="col-md-12">

			<form name="form2" id="form2"
				action="<c:url value='/service/cfg/v1/testSaveByJDBCThread'/>"
				method="get">
				<input type="hidden" id="action" name="action"> <input
					type="hidden" id="startTime" name="startTime"> <input
					type="hidden" id="endTime" name="endTime">
				<div class="portlet light">
					<div class="portlet-title">
						<div class="caption">
							<i class="fa fa-cogs font-green-sharp"></i> <span
								class="caption-subject font-green-sharp bold uppercase">批量提交(编译配置条数=批量增加条数*35,所有配置条数=批量增加条数*186)</span>
						</div>
					</div>
					<div class="form-inline">
						<div class="form-group">
							<label class="col-md-6 control-label">批量增加条数</label> <input
								type="text" class="form-control task-input-small"
								name="configCount" id="configCount" placeholder="批量增加条数"
								maxlength="64">
						</div>

						<div class="form-group">
							<label class="control-label col-md-6">开始结束时间</label>
							<div class="controls">
								<div class="input-prepend input-group">
									<input type="text" name="reservation"
										class="form-control input-large" id="reservationtime" value="" readonly="readonly"/>
								</div>
							</div>
						</div>
						<div class="form-group ">
							<label class="control-label col-md-9">
								&nbsp;&nbsp;&nbsp;&nbsp;</label> <a onclick="submitForm();"
								class="btn btn-success">提交 </a> <a
								href="<%=basePath%>test/page/findAndSave.jsp"
								class="btn btn-success">查询 </a>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
<!-- END BODY -->
</html>
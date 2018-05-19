<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.nis.util.*"%>
<%@ include file="/test/common/taglib.jsp"%>
<%@ page session="false"%>
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
<title>系统日志</title>
<%@include file="/test/common/bootstrap_css_js.jsp"%>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport">
<meta content="" name="description">
<meta content="" name="author">
<link rel="shortcut icon" href="favicon.ico">

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
<script type="text/javascript">
	$(function() {
		submitForm();
	});


	function submitForm() {
		//document.form2.submit();
		var timeRange = $("#reservationtime").val();
		var timeArr = timeRange.split("to");
		$("#requestStartTime").val($.trim(timeArr[0]));
		$("#requestEndTime").val($.trim(timeArr[1]));
		$
				.ajax({
					type : 'post',
					url : '/galaxy/service/log/v1/getAllLog',
					data : $("#form2").serialize(),
					//dataType : 'json',
					async : false,
					success : function(data, textStatus) {
						if (data.data.list.length > 0) {
							var html = '';
							for (var i = 0; i < data.data.list.length; i++) {
								var obj = data.data.list[i];
								html += "<tr><td><div align='center'><input class='checkboxes' type='checkbox' name='td_ck' onclick='cancleTHCheck(this)' value='"
										+ obj.id + "' /> " + (i + 1);
								html += "<input type='hidden' value='"+i+"' name='updateSign'></div></td>";

								html += "<td><div align='center' title="+obj.id;
								html+=" style='width: 90px; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;'>";
								html += "<span class='STYLE1'>" + obj.id
										+ "</span></div></td>";

								html += "<td><div align='center'><span class='STYLE1'>"
										+ rmvNull(obj.operator)
										+ "</span></div></td>";

								html += "	<td><div align='center'><span class='STYLE1'>"
										+ rmvNull(obj.version)
										+ "</span></div></td>";

								html += "	<td><div align='center'><span class='STYLE1'>"
										+ convertOpt(rmvNull(obj.opAction))
										+ "</span></div></td>";

								html += "	<td><div align='center'><span class='STYLE1'>"
										+ getLocalTime(rmvNull(obj.opTime))
										+ "</span></div></td>";

								html += "	<td><div align='center'><span class='STYLE1'>"
										+ rmvNull(obj.consumerTime)
										+ "</span></div></td>";
								html += "	<td><div align='center'><span class='STYLE1'>"
										+ rmvNull(obj.requestIp)
										+ "</span></div></td>";

								html += "	<td><div align='center'><span class='STYLE1'>"
										+ rmvNull(obj.businessCode)
										+ "</span></div></td>";

								html += "	<td><div align='center'><span class='STYLE1'>"
										+ getLocalTime(rmvNull(obj.requestTime))
										+ "</span></div></td>";
								html += "	<td><div align='center'><a class='STYLE1' onclick='getLogInfo(\""
										+ obj.id + "\")'>查看详情</a></div></td>";

								html += "</tr>";
							}
							$("#compileTbody").html(html);
							$("#count").html(data.data.count);
							$("#currentPage").html(data.data.pageNo);
							if ((data.data.count % data.data.pageSize) == 0) {
								$("#allPageNo").val(
										parseInt(data.data.count
												/ data.data.pageSize));
							} else {
								$("#allPageNo")
										.val(
												parseInt((data.data.count / data.data.pageSize)) + 1);
							}

						} else {
							//alert("");
							$("#compileTbody")
									.html(
											"<tr><td colspan='14' style='text-align: center'><span >没有数据</span></td><tr>");
						}
					}
				})

		//$("#form2").submit();
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

	function getLogInfo(id) {
		window.location.href = '<%=basePath%>test/page/serviceLog/getLogInfo.jsp?id='+ id;//
	}

	function rmvNull(info) {
		if (info == null) {
			return "";
		}
		return info;
	}
	function getLittleInfo(info) {
		if (info != null && info != "") {
			if (info.length > 20) {
				return info.substr(0, 20);
			} else {
				return info;
			}
		}
		return "";
	}

	function setPageSize(obj) {
		$("#pageSize").val($(obj).val());
	}

	function getPage(obj) {
		if ($("#count")[0].innerHTML != '') {
			if (obj == 'f') {
				$("#pageNo").val("1");
			} else if (obj == 'p') {
				if (parseInt($("#pageNo").val()) > 1) {
					$("#pageNo").val(parseInt($("#pageNo").val()) - 1);
				} else {
					return;
				}

			} else if (obj == 'n') {
				if (parseInt($("#pageNo").val()) < parseInt($("#allPageNo")
						.val())) {
					$("#pageNo").val(parseInt($("#pageNo").val()) + 1);
				} else {
					return;
				}

			} else if (obj == 'e') {
				$("#pageNo").val($("#allPageNo").val());
			}
			submitForm();
		}
	}

	function cancleTHCheck(obj) {
		if ($(obj).attr("checked") == "checked") {
		} else {// 如果有一行checkbox没有被选中则上面序号处的checkbox就不被选中
			$("#th_ck").attr("checked", false);
			$("#th_ck").parent().attr("class", "");
		}
		var checkNum = 0;// 记录当前有多少行的checkbox被选中
		$("input[type='checkbox'][name='td_ck']").each(function(i) {
			if ($(this).attr("checked") == "checked") {
				checkNum = Number(checkNum) + Number("1");
			}
		});
		var tdCheckBoxNum = $('input[type="checkbox"][name="td_ck"]').length;
		if (Number(checkNum) == Number(tdCheckBoxNum)) {// 如果都被选中，则最上面的序号处的checkbox也被选中
			$("#th_ck").attr("checked", true);
			$("#th_ck").parent().attr("class", "checked")
		}
	}

	function selectAll(obj) {
		if ($(obj).attr("checked") == "checked") {// 全选
			$('input[type="checkbox"][name="td_ck"]').each(function() {// 由于bootstrap为checkbox添加了父标签span并且通过span的class="checked"设置checkbox是否被选中
				$(this).parent().attr('class', 'checked');
				$(this).attr("checked", true);
			})
		} else {// 取消全选
			$('input[type="checkbox"][name="td_ck"]').each(function() {
				$(this).parent().attr('class', '');
				$(this).attr("checked", false);
			})
		}

	}
	function delLogInfo(obj) {
		id = "";
		url = '';
		if (obj == 'select') {
			var checkedNum = $('input[type="checkbox"][name="td_ck"]:checked').length;
			if (checkedNum > 0) {
				var cancelState = 0;
				var id = "[";
				$('input[type="checkbox"][name="td_ck"]:checked').each(
						function() {
							if ($(this).parent().parent().next().val() == '5') {
								cancelState = cancelState + 1;
							}
							id = id + "," + $(this).val();
						})
			} else {
				alert("请先选择数据在删除");
				return;
			}
			url = '/galaxy/service/log/v1/deleteById';
			$.ajax({
				type : 'get',
				url : url,
				data : "id=" + id,
				//dataType : 'json',
				async : false,
				success : function(data, textStatus) {
					if (data == 'ok') {
						alert("删除日志成功");
					} else {
						alert("删除日志失败,请联系开发人员");
					}

				}
			});

		} else if (obj == 'all') {
			url = '/galaxy/service/log/v1/deleteAll';
			$.ajax({
				type : 'get',
				url : url,
				async : false,
				success : function(data, textStatus) {
					if (data == 'ok') {
						alert("删除所有日志成功");
					} else {
						alert("删除所有日志失败,请联系开发人员");
					}

				}
			})

		}
		submitForm();
	}
</script>

</head>
<body style="background-color: #2D3233">
	<div class="row">
		<div class="col-md-12">

			<form name="form2" id="form2"
				action="<c:url value='/service/test/v1/saveAllByQuery'/>"
				method="get">
				<input type="hidden" id="requestStartTime" name="requestStartTime">
				<input type="hidden" id="requestEndTime" name="requestEndTime">
				<input type="hidden" id="allPageNo"> <input type="hidden"
					id="pageNo" name="pageNo" value="1">
				<div class="portlet light">
					<div class="portlet-title">
						<div class="caption">
							<!-- 							<i class="fa fa-cogs font-green-sharp"></i>  -->
							<span class="caption-subject font-green-sharp bold uppercase">系统日志查询</span>
						</div>

					</div>
					<div class="form-inline">
						<div class="form-group">
							<label class="col-md-6 control-label">id</label> <input
								type="text" class="form-control task-input-small" name="id"
								id="id" placeholder="id" maxlength="64">
						</div>
						<div class="form-group">
							<label class="col-md-6 control-label">操作人</label> <input
								type="text" class="form-control task-input-small"
								name="operator" id="operator" placeholder="operator"
								maxlength="64">
						</div>
						<div class="form-group">
							<label class="col-md-6 control-label">版本</label> <input
								type="text" class="form-control task-input-small" name="version"
								id="version" placeholder="version" maxlength="64">
						</div>
						<div class="form-group">
							<label class="col-md-6 control-label">操作行为</label> <select
								class="form-control input-small" name="opAction"
								onchange="setColumnShow(this);" id="opAction">
								<option value="">请选择</option>
								<option value="1">添加</option>
								<option value="2">修改</option>
								<option value="3">删除</option>
								<option value="4">查询</option>
							</select>
						</div>
						<div class="form-group">
							<label class="col-md-6 control-label">请求内容</label> <input
								type="text" class="form-control task-input-small"
								name="requestContent" id="requestContent"
								placeholder="requestContent" maxlength="64">
						</div>
						<div class="form-group">
							<label class="col-md-6 control-label">请求者ip</label> <input
								type="text" class="form-control task-input-small"
								name="requestIp" id="requestIp" placeholder="requestIp"
								maxlength="64">
						</div>
						<div class="form-group">
							<label class="col-md-6 control-label">服务状态码</label> <input
								type="text" class="form-control task-input-small"
								name="businessCode" id="businessCode" placeholder="businessCode"
								maxlength="64">
						</div>
						<div class="form-group">
							<label class="col-md-6 control-label">异常信息</label> <input
								type="text" class="form-control task-input-small"
								name="exceptionInfo" id="exceptionInfo"
								placeholder="exceptionInfo" maxlength="64">
						</div>

						<div class="form-group">
							<label class="control-label col-md-6">请求时间</label>
							<div class="controls">
								<div class="input-prepend input-group">
									<input type="text" name="reservation"
										class="form-control input-large" id="reservationtime" value="" readonly="readonly"/>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-md-6">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
							<div class="form-group">
								<a onclick="submitForm();" class="btn btn-success">查询 </a> <a
									onclick="delLogInfo('select');" class="btn btn-success">删除选择数据
								</a> <a onclick="delLogInfo('all');" class="btn btn-success">删除所有数据</a>
							</div>
						</div>

					</div>

					<div class="portlet-body">
						<table class="table table-bordered " id="">
							<thead>
								<tr>
									<th><div align="center">
											<input class="checkboxes" type="checkbox" id="th_ck"
												name="id" onclick="selectAll(this)" />序号&nbsp;&nbsp;
										</div></th>
									<th><div align="center">id</div></th>
									<th><div align="center">操作人</div></th>
									<th><div align="center">版本</div></th>
									<th><div align="center">操作行为</div></th>
									<th><div align="center">操作时间</div></th>
									<th><div align="center">服务器处理时间</div></th>
									<th><div align="center">请求ip</div></th>
									<th><div align="center">服务状态码</div></th>
									<th><div align="center">请求到达时间</div></th>
									<th><div align="center">操作</div></th>
								</tr>
							</thead>
							<tbody id="compileTbody">
								<tr>

									<td colspan="11"><div align="center">
											<span class="STYLE1">没有查询到数据</span>
										</div></td>
								</tr>

							</tbody>
						</table>
						<div id="divBoot">
							<div class="row">
								<div class="col-md-5 col-sm-12">
									<div class="">
										共有<span id="count"></span> 条记录， 当前第 <span id="currentPage">
										</span>页
									</div>
								</div>
								<div class="col-md-5 col-sm-12">
									每页显示 <select name="pageSize" onchange="setPageSize(this)"
										id="newPageSize"
										class="form-control input-xsmall input-inline">
										<option value="10">10</option>
										<option value="20">20</option>
										<option value="30">30</option>
										<option value="50">50</option>
										<option value="100">100</option>
										<option value="200">200</option>
									</select> 条
								</div>
								<div class="dataTables_paginate ">
									<ul class="pagination">
										<li class="first" style='cursor: pointer'><a
											style="color: #fff;" onclick="getPage('f')"> <span
												class="hidden-480">首页</span>
										</a></li>
										<li class="prev" style='cursor: pointer'><a
											style="color: #fff;" onclick="getPage('p')"> <span
												class="hidden-480">上一页</span>
										</a></li>
										<li class="next" style='cursor: pointer'><a
											style="color: #fff;" onclick="getPage('n')"> <span
												class="hidden-480">下一页</span>
										</a></li>
										<li class="last" style='cursor: pointer'><a
											style="color: #fff;" onclick="getPage('e')"> <span
												class="hidden-480">尾页</span>
										</a></li>
									</ul>
								</div>

							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
<!-- END BODY -->
</html>
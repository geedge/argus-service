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
<title>查询所有编译配置</title>
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
<!-- start link CSS and JS -->

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
		$("#searchStartTime").val($.trim(timeArr[0]));
		$("#searchEndTime").val($.trim(timeArr[1]));

		//document.form2.submit();
		$
				.ajax({
					type : 'post',
					url : '/gk/service/cfg/v1/findCompile',
					data : $("#form2").serialize(),
					//dataType : 'json',
					async : false,
					success : function(data, textStatus) {
						if (data.data.list.length > 0) {
							var html = '';
							for (var i = 0; i < data.data.list.length; i++) {
								var obj = data.data.list[i];
								html += "<tr><td><div align='center'><input class='checkboxes' type='checkbox' name='td_ck' onclick='cancleTHCheck(this)' value='"
										+ obj.compileId + "' /> " + (i + 1);
								html += "<input type='hidden' value='"+i+"' name='updateSign'></div></td>";

								html += "<td><div align='center' title="+obj.compileId;
								html+="style='width: 90px; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;'>";
								html += "<span class='STYLE1'>" + obj.compileId
										+ "</span></div></td>";

								html += "	<td><div align='center'><span class='STYLE1'>"
										+ obj.service + "</span></div></td>";

								html += "	<td><div align='center'><span class='STYLE1'>"
										+ obj.action + "</span></div></td>";

								html += "	<td><div align='center'><span class='STYLE1'>"
										+ obj.contType + "</span></div></td>";

								html += "	<td><div align='center'><span class='STYLE1'>"
										+ obj.attrType + "</span></div></td>";

								html += "	<td><div align='center'><span class='STYLE1'>"
										+ obj.contLabel + "</span></div></td>";

								html += "	<td><div align='center'><span class='STYLE1'>"
										+ obj.taskId + "</span></div></td>";
								html += "	<td><div align='center'><span class='STYLE1'>"
										+ obj.guaranteeId
										+ "</span></div></td>";

								html += "	<td><div align='center'><span class='STYLE1'>"
										+ obj.affAirId + "</span></div></td>";
								html += "	<td><div align='center'><span class='STYLE1'>"
										+ obj.topIcId + "</span></div></td>";
								html += "	<td><div align='center'><span class='STYLE1'>"
										+ obj.doBlackList
										+ "</span></div></td>";
								html += "	<td><div align='center'><span class='STYLE1'>"
										+ obj.groupNum + "</span></div></td>";
								html += "	<td><div align='center'><span class='STYLE1'>"
										+ obj.opTime + "</span></div></td>";

								html += "</tr>";
							}
							$("#compileTbody").html(html);
							$("#totalNum").html(data.data.count);
							$("#currentPage").html(data.data.pageNo);
							if ((data.data.count % data.data.pageSize) == 0) {
								$("#totalCount").val(
										data.data.count / data.data.pageSize);
							} else {
								$("#totalCount")
										.val(
												(data.data.count / data.data.pageSize) + 1);
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

	function clearInput() {

	}

	function setPageSize(obj) {
		$("#pagesSize").val($(obj).val());
	}

	function getPage(obj) {
		if ($("#totalCount").val() != '') {
			if (obj == 'f') {
				$("#pagesNo").val("1");
			} else if (obj == 'p') {
				if (parseInt($("#pagesNo").val()) > 1) {
					$("#pagesNo").val(parseInt($("#pagesNo").val()) - 1);
				} else {
					return;
				}

			} else if (obj == 'n') {
				if (parseInt($("#pagesNo").val()) < parseInt($("#totalCount")
						.val())) {
					$("#pagesNo").val(parseInt($("#pagesNo").val()) + 1);
				} else {
					return;
				}

			} else if (obj == 'e') {
				$("#pagesNo").val($("#totalCount").val());
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
	function saveCompile(obj) {
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
				alert("请先选择数据在插入");
				return;
			}
			url = '/gk/service/cfg/v1/saveByQuery';
			$.ajax({
				type : 'post',
				url : url,
				data : "id=" + id,
				//dataType : 'json',
				async : true,
				success : function(data, textStatus) {
					if (data == 'ok') {
						alert("插入成功,请去gk_pz数据库中查询相应数据");
					} else {
						alert("插入失败");
					}

				}
			});

		} else if (obj == 'all') {
			url = '/gk/service/cfg/v1/saveAllByQuery';
			$.ajax({
				type : 'post',
				url : url,
				data : $("#form2").serialize(),
				//dataType : 'json',
				async : true,
				success : function(data, textStatus) {
					if (data == 'ok') {
						alert("插入成功,请去gk_pz数据库中查询相应数据");
					} else {
						alert("插入失败");
					}

				}
			})

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
				action="<c:url value='/service/test/v1/saveAllByQuery'/>"
				method="post">
				<input type="hidden" id="action" name="action"> <input
					type="hidden" id="searchStartTime" name="searchStartTime">
				<input type="hidden" id="searchEndTime" name="searchEndTime">
				<input type="hidden" id="pagesNo" name="pagesNo" value="1">
				<input type="hidden" id="pagesSize" name="pagesSize" value="10">
				<input type="hidden" id="totalCount" name="totalCount">
				<div class="portlet light">
					<div class="portlet-title">
						<div class="caption">
							<!-- 							<i class="fa fa-cogs font-green-sharp"></i>  -->
							<span class="caption-subject font-green-sharp bold uppercase">查询所有编译配置</span>
						</div>

					</div>
					<div class="form-inline">
						<div class="form-group">
							<label class="col-md-6 control-label">配置id</label> <input
								type="text" class="form-control task-input-small"
								name="compileId" id="compileId" placeholder="配置id"
								maxlength="64">
						</div>
						<div class="form-group">
							<label class="col-md-6 control-label">业务类型</label> <select
								class="form-control task-input-small" name="service"
								onchange="setActionVal(this);" id="service">
								<option value="" selected="selected">请选择</option>
								<option value="1">基于端口的封堵</option>
								<option value="2">IP白名单配置</option>
								<option value="3">HTTP协议请求封堵配置</option>
								<option value="4">HTTP协议响应封堵配置</option>
								<option value="5">HTTP关键字封堵配置</option>
								<option value="6">DNS协议封堵配置</option>
								<option value="7">PPTP协议封堵配置</option>
								<option value="8">L2TP的协议封堵配置</option>
								<option value="9">IPSec的协议封堵配置</option>
								<option value="10">OpenVPN协议封堵配置</option>
								<option value="11">SSH协议的封堵配置</option>
								<option value="12">SSL协议封堵配置</option>
								<option value="13">MAIL封堵配置</option>
								<option value="14">FTP协议封堵配置</option>
								<option value="15">HTTP请求头域白名单配置</option>
								<option value="16">DNS白名单配置</option>
								<option value="32">L2TP协议封堵配置</option>
								<option value="33">PPTP协议URL封堵配置</option>

								<option value="48">IP端口监测配置</option>
								<option value="49">HTTP协议的监测请求配置</option>
								<option value="50">HTTP协议的监测响应配置</option>
								<option value="51">HTTP协议关键字监测配置</option>
								<option value="52">DNS的协议监测配置</option>
								<option value="53">FTP的协议监测配置</option>
								<option value="54">PPTP协议监测配置</option>
								<option value="55">L2TP的协议监测配置</option>
								<option value="56">IPSec密钥交互协议监测配置</option>
								<option value="57">OpenVPN协议监测配置</option>
								<option value="58">SSH协议监测配置</option>
								<option value="59">SSL协议监测配置</option>
								<option value="60">MAIL协议监测配置</option>
							</select>
						</div>
						<div class="form-group">
							<label class="control-label col-md-4">操作时间</label>
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
								class="btn btn-success">查询 </a> <a
								href="<%=basePath%>test/page/insertBatch.jsp"
								class="btn btn-success">添加 </a>
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
									<th>
										<div align="center">配置id</div>
									</th>
									<th><div align="center">业务id</div></th>
									<th><div align="center">动作</div></th>
									<th><div align="center">分类</div></th>
									<th><div align="center">性质</div></th>
									<th><div align="center">标签</div></th>
									<th><div align="center">任务</div></th>
									<th><div align="center">保障期</div></th>
									<th><div align="center">事件</div></th>
									<th><div align="center">子话题</div></th>
									<th><div align="center">是否黑名单</div></th>
									<th><div align="center">包含分组数量</div></th>
									<th><div align="center">操作时间</div></th>
								</tr>
							</thead>
							<tbody id="compileTbody">
								<tr>

									<td colspan="14"><div align="center">
											<span class="STYLE1">没有查询到数据</span>
										</div></td>
								</tr>

							</tbody>
						</table>
						<div id="divBoot">
							<div class="row">
								<div class="col-md-5 col-sm-12">
									<div class="">
										共有<span id="totalNum"></span> 条记录， 当前第 <span id="currentPage">
											<span id="currentPage"> </span>
										</span>页
									</div>
								</div>
								<div class="col-md-5 col-sm-12">
									每页显示 <select name="pagesSize" onchange="setPageSize(this)"
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
											onclick="getPage('f')"> <span class="hidden-480">首页</span>
										</a></li>
										<li class="prev" style='cursor: pointer'><a
											onclick="getPage('p')"> <span class="hidden-480">上一页</span>
										</a></li>
										<li class="next" style='cursor: pointer'><a
											onclick="getPage('n')"> <span class="hidden-480">下一页</span>
										</a></li>
										<li class="last" style='cursor: pointer'><a
											onclick="getPage('e')"> <span class="hidden-480">尾页</span>
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
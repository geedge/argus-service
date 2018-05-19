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
<title>数据字典</title>
<%@include file="/test/common/bootstrap_css_js.jsp"%>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport">
<meta content="" name="description">
<meta content="" name="author">
<link rel="shortcut icon" href="favicon.ico">

<!-- start link CSS and JS -->

<script type="text/javascript">
	$(function() {
		getAllDictName();
		submitForm();
	})

	function getAllDictName() {
		$.ajax({
			type : 'get',
			url : '/galaxy/service/cfg/v1/getAllDictName',
			async : false,
			success : function(data, textStatus) {
				if (data.data.length > 0) {
					var option = "";
					for (var i = 0; i < data.data.length; i++) {
						var obj = data.data[i];
						option += "	<option value="+obj.dictNameId+">"
								+ obj.dataDictName + "</option>"
					}
					$("#dictNameId").append(option);
				}
			}
		})

	}

	function submitForm() {
		//document.form2.submit();
		$
				.ajax({
					type : 'post',
					url : '/galaxy/service/cfg/v1/findAllDataDict',
					data : $("#form2").serialize(),
					//dataType : 'json',
					async : false,
					success : function(data, textStatus) {
						if (data.data.list.length > 0) {
							var html = '';
							var count = 0;
							var state = 0;
							for (var i = 0; i < data.data.list.length; i++) {
								var obj = data.data.list[i];
								count++;
								html += "<tr><td><div align='center'><input class='checkboxes' type='checkbox' name='td_ck' onclick='cancleTHCheck(this)' value='"
										+ obj.dictValueId + "' /> " + count;
								//html += "<input type='hidden' value='"+x+"' name='updateSign'></div></td>";
								html += "</div></td>";
								html += "<td><div align='center' title="+obj.dataDictName;
									html+="style='width: 90px; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;'>";
								html += "<span class='STYLE1'>"
										+ obj.dataDictName
										+ "</span></div></td>";
								html += "	<td><div align='center'><span class='STYLE1'>"
										+ obj.dataDictValue
										+ "</span></div></td>";
								html += "</tr>";
							}
							$("#compileTbody").html(html);
							$("#totalNum").html(data.data.count);
						} else {
							$("#totalNum").html("0");
							$("#compileTbody")
									.html(
											"<tr><td colspan='3' style='text-align: center'><span >没有数据</span></td></tr>");
						}

						$("#totalNum").html(data.data.count);
						$("#currentPage").html(data.data.pageNo);
						if ((data.data.count % data.data.pageSize) == 0) {
							var totalPage = parseInt(data.data.count
									/ data.data.pageSize);
							$("#totalCount").val(totalPage);
							$("#totalPage").html(totalPage);
						} else {
							var totalPage = parseInt(data.data.count
									/ data.data.pageSize);
							$("#totalCount").val(totalPage + 1);
							$("#totalPage").html(totalPage + 1);
						}
					}
				})
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

	function delDataDict() {
		var checkedNum = $('input[type="checkbox"][name="td_ck"]:checked').length;
		if (checkedNum > 0) {
			var id = "[";
			$('input[type="checkbox"][name="td_ck"]:checked').each(function() {

				id = id + "," + $(this).val();
			})
			if (window.confirm("确定删除吗？")) {
				id += ",]";
				delForAjax(id);
			}
		} else {
			alert("请选择数据");
		}

	}

	function delForAjax(id) {
		$.ajax({
			type : 'post',
			url : '/galaxy/service/cfg/v1/delDictVal',
			data : {
				"dictValId" : id
			},
			dataType : 'json',
			async : false,
			success : function(data, textStatus) {
				if (data.msg.indexOf("成功") != -1) {
					alert("删除成功");
					window.location.reload(true);
				} else {
					alert("删除失败");
				}
			}
		})
	}
	function updateDataDict() {
		var checkedNum = $('input[type="checkbox"][name="td_ck"]:checked').length;

		if (checkedNum > 0) {
			if (checkedNum == 1) {
				var id = "[";
				$('input[type="checkbox"][name="td_ck"]:checked').each(
						function() {
							id = id + "," + $(this).val();
						})
				id += ",]";
				window.location.href = '<%=basePath%>test/page/dataDict/addOrUpdateDataDict.jsp?isUpdate=1&dictValId='+ id;//
			} else {
				alert("每次只能修改一条数据");
				return;
			}
		} else {
			alert("请选择数据");
			return;
		}

	}
	
	function setDataDictEffective(){
		$.ajax({
			type : 'post',
			url : '/galaxy/service/cfg/v1/setDataDictEffective',
			dataType : 'json',
			async : false,
			success : function(data, textStatus) {
				if (data.msg.indexOf("成功") != -1) {
					alert(data.msg);
					window.location.reload(true);
				} else {
					alert(data.msg);
				}
			}
		})
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

			<form name="form2" id="form2" method="post">
				<input type="hidden" id="pagesNo" name="pagesNo" value="1">
				<input type="hidden" id="pagesSize" name="pagesSize" value="10">
				<input type="hidden" id="totalCount" name="totalCount">
				<div class="portlet light">
					<div class="portlet-title">
						<div class="caption">
							<i class="fa fa-cogs font-green-sharp"></i> <span id="spanText"
								class="caption-subject font-green-sharp bold uppercase">数据字典值管理</span>
						</div>
					</div>
					<div class="form-inline">
						<div class="form-group">
							<label class="col-md-5 control-label">数据字典名称</label> <select
								class="form-control task-input-small" name="dictNameId"
								id="dictNameId">
								<option value="" selected="selected">请选择</option>
							</select>
						</div>
						<div class="form-group">
							<label class="col-md-5 control-label">数据字典值</label> <input
								type="text" class="form-control task-input-small"
								name="dataDictValue" id="dataDictValue" placeholder="数据字典值"
								maxlength="64">
						</div>
						<!-- <div class="form-group">
							<label class="col-md-4 control-label">数据字典值</label> <select
								class="form-control task-input-small" name="service"
								id="service">
								<option value="" selected="selected">请选择</option>
								<option value="1">基于端口的封堵</option>
								<option value="2">IP白名单配置</option>
								<option value="3">HTTP协议请求封堵配置</option>
								<option value="4">HTTP协议响应封堵配置</option>
								<option value="5">HTTP关键字封堵配置</option>

							</select>
						</div>
 						-->
						<div class="form-group ">
							<a onclick="submitForm();" class="btn btn-success">查询 </a> <a
								onclick="delDataDict();" class="btn btn-success">删除 </a> <a
								onclick="updateDataDict();" class="btn btn-success">修改 </a> <a
								href="<%=basePath%>test/page/dataDict/addOrUpdateDataDict.jsp"
								class="btn btn-success">添加数据字典 </a> <a
								href="<%=basePath%>test/page/dataDict/allDataDictName.jsp"
								class="btn btn-success">管理数据字典名称 </a> <a
								onclick="setDataDictEffective()"
								class="btn btn-success">生效</a>
						</div>
					</div>
				</div>
			</form>
			<div class="portlet light">
				<div class="portlet-body">
					<table class="table table-bordered " id="">
						<thead>
							<tr>
								<th><div align="center">
										<input class="checkboxes" type="checkbox" id="th_ck" name="id"
											onclick="selectAll(this)" />序号&nbsp;&nbsp;
									</div></th>
								<th>
									<div align="center">数据字典名称</div>
								</th>
								<th><div align="center">数据字典值</div></th>

							</tr>
						</thead>
						<tbody id="compileTbody">
							<tr>

								<td colspan="13"><div align="center">
										<span class="STYLE1">没有查询到数据</span>
									</div></td>
							</tr>

						</tbody>
					</table>
					<div id="divBoot">
						<div class="row">
							<div class="col-md-5 col-sm-12">
								<div class="">
									共有<span id="totalPage"></span>页,<span id="totalNum"></span>条记录,当前第<span
										id="currentPage"></span>页
								</div>
							</div>
							<div class="col-md-5 col-sm-12">
								每页显示 <select name="pagesSize" onchange="setPageSize(this)"
									id="newPageSize" class="form-control input-xsmall input-inline">
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

		</div>
	</div>
</body>
<!-- END BODY -->
</html>
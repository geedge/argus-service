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
<title>管控实时统计查询</title>
<%@include file="/test/common/bootstrap_css_js.jsp"%>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport">
<meta content="" name="description">
<meta content="" name="author">
<link rel="shortcut icon" href="favicon.ico">

<!-- start link CSS and JS -->

<script type="text/javascript">
	function submitForm() {
		var gkType = $("#gkType").val();
		var url = getUrl(gkType) + "&pageNo=" + $("#pageNo").val()
				+ "&pageSize=" + $("#pageSize").val() + "&orderBy="
				+ $("#orderBy").val() + "&fields=" + $("#fields").val();
		console.log(url);
		$
				.ajax({
					type : 'get',
					url : url,
					async : false,
					dataType : 'json',
					success : function(data, textStatus) {
						if (data.data.list.length > 0) {
							setColumn(data.data.list, gkType);
							$("#totalNum").html(data.data.count);
							$("#totalPageCount").html(data.data.last);
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
							$("#compileTbody")
									.html(
											"<tr><td colspan='10' style='text-align: center'><span >没有数据</span></td><tr>");
						}
					},
					error : function(data, textStatus) {
						$("#compileTbody").html(
								"<tr><td colspan='10' style='text-align: center'><span >"
										+ data.responseJSON.msg
										+ "</span></td><tr>");
					}
				})
	}
	function setColumn(objList, gkType) {
		html = '';
		if (gkType == '1') {
			for (var i = 0; i < objList.length; i++) {
				var obj = objList[i];
				html += "<tr><td><div align='center'><input class='checkboxes' type='checkbox' name='td_ck' onclick='cancleTHCheck(this)' value='"
						+ obj.statId + "' /> " + (i + 1);
				html += "<input type='hidden' value='"+i+"' name='updateSign'></div></td>";

				html += "<td><div align='center' title="+obj.statId;
		html+="style='width: 90px; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;'>";
				html += "<span class='STYLE1'>" + obj.statId
						+ "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.fl + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.xz + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.reportTime + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.nation + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.province + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.city + "</span></div></td>";
				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.sum + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ getServiceType(obj.service) + "</span></div></td>";
				html += "</tr>";
			}
		} else if (gkType == '2') {
			for (var i = 0; i < objList.length; i++) {
				var obj = objList[i];
				html += "<tr><td><div align='center'><input class='checkboxes' type='checkbox' name='td_ck' onclick='cancleTHCheck(this)' value='"
						+ obj.statId + "' /> " + (i + 1);
				html += "<input type='hidden' value='"+i+"' name='updateSign'></div></td>";

				html += "<td><div align='center' title="+obj.statId;
		html+="style='width: 90px; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;'>";
				html += "<span class='STYLE1'>" + obj.statId
						+ "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.fl + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.xz + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.reportTime + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.nation + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.province + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.city + "</span></div></td>";
				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.sum + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ getServiceType(obj.service) + "</span></div></td>";
				html += "</tr>";
			}
		} else if (gkType == '3') {
			for (var i = 0; i < objList.length; i++) {
				var obj = objList[i];
				html += "<tr><td><div align='center'><input class='checkboxes' type='checkbox' name='td_ck' onclick='cancleTHCheck(this)' value='"
						+ obj.statId + "' /> " + (i + 1);
				html += "<input type='hidden' value='"+i+"' name='updateSign'></div></td>";

				html += "<td><div align='center' title="+obj.statId;
		html+="style='width: 90px; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;'>";
				html += "<span class='STYLE1'>" + obj.statId
						+ "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.affair + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.topic + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.reportTime + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.nation + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.province + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.city + "</span></div></td>";
				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.sum + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ getServiceType(obj.service) + "</span></div></td>";
				html += "</tr>";
			}

		} else if (gkType == '4') {

			for (var i = 0; i < objList.length; i++) {
				var obj = objList[i];
				html += "<tr><td><div align='center'><input class='checkboxes' type='checkbox' name='td_ck' onclick='cancleTHCheck(this)' value='"
						+ obj.statId + "' /> " + (i + 1);
				html += "<input type='hidden' value='"+i+"' name='updateSign'></div></td>";

				html += "<td><div align='center' title="+obj.statId;
		html+="style='width: 90px; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;'>";
				html += "<span class='STYLE1'>" + obj.statId
						+ "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.affair + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.topic + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.reportTime + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.nation + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.province + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.city + "</span></div></td>";
				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.sum + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ getServiceType(obj.service) + "</span></div></td>";
				html += "</tr>";
			}

		} else if (gkType == '5') {
			for (var i = 0; i < objList.length; i++) {
				var obj = objList[i];
				html += "<tr><td><div align='center'><input class='checkboxes' type='checkbox' name='td_ck' onclick='cancleTHCheck(this)' value='"
						+ obj.statId + "' /> " + (i + 1);
				html += "<input type='hidden' value='"+i+"' name='updateSign'></div></td>";

				html += "<td><div align='center' title="+obj.statId;
		html+="style='width: 90px; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;'>";
				html += "<span class='STYLE1'>" + obj.statId
						+ "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.mission + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.reportTime + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.nation + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.province + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.city + "</span></div></td>";
				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.sum + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ getServiceType(obj.service) + "</span></div></td>";
				html += "</tr>";
			}
		} else if (gkType == '6') {
			for (var i = 0; i < objList.length; i++) {
				var obj = objList[i];
				html += "<tr><td><div align='center'><input class='checkboxes' type='checkbox' name='td_ck' onclick='cancleTHCheck(this)' value='"
						+ obj.statId + "' /> " + (i + 1);
				html += "<input type='hidden' value='"+i+"' name='updateSign'></div></td>";

				html += "<td><div align='center' title="+obj.statId;
		html+="style='width: 90px; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;'>";
				html += "<span class='STYLE1'>" + obj.statId
						+ "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.mission + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.reportTime + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.nation + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.province + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.city + "</span></div></td>";
				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.sum + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ getServiceType(obj.service) + "</span></div></td>";
				html += "</tr>";
			}
		} else if (gkType == '7') {
			for (var i = 0; i < objList.length; i++) {
				var obj = objList[i];
				html += "<tr><td><div align='center'><input class='checkboxes' type='checkbox' name='td_ck' onclick='cancleTHCheck(this)' value='"
						+ obj.statId + "' /> " + (i + 1);
				html += "<input type='hidden' value='"+i+"' name='updateSign'></div></td>";

				html += "<td><div align='center' title="+obj.statId;
		html+="style='width: 90px; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;'>";
				html += "<span class='STYLE1'>" + obj.statId
						+ "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.guarantee + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.reportTime + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.nation + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.province + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.city + "</span></div></td>";
				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.sum + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ getServiceType(obj.service) + "</span></div></td>";
				html += "</tr>";
			}
		} else if (gkType == '8') {
			for (var i = 0; i < objList.length; i++) {
				var obj = objList[i];
				html += "<tr><td><div align='center'><input class='checkboxes' type='checkbox' name='td_ck' onclick='cancleTHCheck(this)' value='"
						+ obj.statId + "' /> " + (i + 1);
				html += "<input type='hidden' value='"+i+"' name='updateSign'></div></td>";

				html += "<td><div align='center' title="+obj.statId;
		html+="style='width: 90px; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;'>";
				html += "<span class='STYLE1'>" + obj.statId
						+ "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.guarantee + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.reportTime + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.nation + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.province + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.city + "</span></div></td>";
				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.sum + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ getServiceType(obj.service) + "</span></div></td>";
				html += "</tr>";
			}
		} else if (gkType == '9') {
			for (var i = 0; i < objList.length; i++) {
				var obj = objList[i];
				html += "<tr><td><div align='center'><input class='checkboxes' type='checkbox' name='td_ck' onclick='cancleTHCheck(this)' value='"
						+ obj.statId + "' /> " + (i + 1);
				html += "<input type='hidden' value='"+i+"' name='updateSign'></div></td>";

				html += "<td><div align='center' title="+obj.statId;
		html+="style='width: 90px; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;'>";
				html += "<span class='STYLE1'>" + obj.statId
						+ "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.tag + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.reportTime + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.nation + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.province + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.city + "</span></div></td>";
				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.sum + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ getServiceType(obj.service) + "</span></div></td>";
				html += "</tr>";
			}
		} else if (gkType == '10') {
			for (var i = 0; i < objList.length; i++) {
				var obj = objList[i];
				html += "<tr><td><div align='center'><input class='checkboxes' type='checkbox' name='td_ck' onclick='cancleTHCheck(this)' value='"
						+ obj.statId + "' /> " + (i + 1);
				html += "<input type='hidden' value='"+i+"' name='updateSign'></div></td>";

				html += "<td><div align='center' title="+obj.statId;
		html+="style='width: 90px; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;'>";
				html += "<span class='STYLE1'>" + obj.statId
						+ "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.tag + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.reportTime + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.nation + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.province + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.city + "</span></div></td>";
				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.sum + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ getServiceType(obj.service) + "</span></div></td>";
				html += "</tr>";
			}
		} else if (gkType == '11') {
			for (var i = 0; i < objList.length; i++) {
				var obj = objList[i];
				html += "<tr><td><div align='center'><input class='checkboxes' type='checkbox' name='td_ck' onclick='cancleTHCheck(this)' value='"
						+ obj.statId + "' /> " + (i + 1);
				html += "<input type='hidden' value='"+i+"' name='updateSign'></div></td>";

				html += "<td><div align='center' title="+obj.statId;
		html+="style='width: 90px; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;'>";
				html += "<span class='STYLE1'>" + obj.statId
						+ "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.id + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.reportTime + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.nation + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.province + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.city + "</span></div></td>";
				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.sum + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ getServiceType(obj.service) + "</span></div></td>";
				html += "</tr>";
			}
		} else if (gkType == '12') {
			for (var i = 0; i < objList.length; i++) {
				var obj = objList[i];
				html += "<tr><td><div align='center'><input class='checkboxes' type='checkbox' name='td_ck' onclick='cancleTHCheck(this)' value='"
						+ obj.statId + "' /> " + (i + 1);
				html += "<input type='hidden' value='"+i+"' name='updateSign'></div></td>";

				html += "<td><div align='center' title="+obj.statId;
		html+="style='width: 90px; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;'>";
				html += "<span class='STYLE1'>" + obj.statId
						+ "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.id + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.reportTime + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.nation + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.province + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.city + "</span></div></td>";
				html += "	<td><div align='center'><span class='STYLE1'>"
						+ obj.sum + "</span></div></td>";

				html += "	<td><div align='center'><span class='STYLE1'>"
						+ getServiceType(obj.service) + "</span></div></td>";
				html += "</tr>";
			}
		}

		$("#compileTbody").html(html);
	}

	function clearInput() {

	}

	function setPageSize(obj) {
		$("#pageSize").val($(obj).val());
	}

	function getPage(obj) {
		if ($("#totalCount").val() != '') {
			if (obj == 'f') {
				$("#pageNo").val("1");
			} else if (obj == 'p') {
				if (parseInt($("#pageNo").val()) > 1) {
					$("#pageNo").val(parseInt($("#pageNo").val()) - 1);
				} else {
					return;
				}

			} else if (obj == 'n') {
				if (parseInt($("#pageNo").val()) < parseInt($("#totalCount")
						.val())) {
					$("#pageNo").val(parseInt($("#pageNo").val()) + 1);
				} else {
					return;
				}

			} else if (obj == 'e') {
				$("#pageNo").val($("#totalCount").val());
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
		if (Number(checkNum) == Number(tdCheckBoxNum)) {// 如果都被选中,则最上面的序号处的checkbox也被选中
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

	function getUrl(objVal) {
		var url = $("#urlAddr").val();
		if (objVal == '1') {
			url += "?searchFl=" + $("#searchFl").val() + "&searchXz="
					+ $("#searchXz").val() + "&searchReportStartTime="
					+ $("#searchReportStartTime").val()
					+ "&searchReportEndTime=" + $("#searchReportEndTime").val()
					+ "&searchService=" + $("#searchService").val();
		} else if (objVal == '2') {
			url += "?searchFl=" + $("#searchFl").val() + "&searchXz="
					+ $("#searchXz").val() + "&searchReportStartTime="
					+ $("#searchReportStartTime").val()
					+ "&searchReportEndTime=" + $("#searchReportEndTime").val()
					+ "&searchService=" + $("#searchService").val();
		} else if (objVal == '3') {
			url += "?searchAffair=" + $("#searchAffair").val()
					+ "&searchTopic=" + $("#searchTopic").val()
					+ "&searchReportStartTime="
					+ $("#searchReportStartTime").val()
					+ "&searchReportEndTime=" + $("#searchReportEndTime").val()
					+ "&searchService=" + $("#searchService").val();
		} else if (objVal == '4') {
			url += "?searchAffair=" + $("#searchAffair").val()
					+ "&searchTopic=" + $("#searchTopic").val()
					+ "&searchReportStartTime="
					+ $("#searchReportStartTime").val()
					+ "&searchReportEndTime=" + $("#searchReportEndTime").val()
					+ "&searchService=" + $("#searchService").val();
		} else if (objVal == '5') {
			url += "?searchMission=" + $("#searchMission").val()
					+ "&searchReportStartTime="
					+ $("#searchReportStartTime").val()
					+ "&searchReportEndTime=" + $("#searchReportEndTime").val()
					+ "&searchService=" + $("#searchService").val();
		} else if (objVal == '6') {
			url += "?searchMission=" + $("#searchMission").val()
					+ "&searchReportStartTime="
					+ $("#searchReportStartTime").val()
					+ "&searchReportEndTime=" + $("#searchReportEndTime").val()
					+ "&searchService=" + $("#searchService").val();
		} else if (objVal == '7') {
			url += "?searchGuarantee=" + $("#searchGuarantee").val()
					+ "&searchReportStartTime="
					+ $("#searchReportStartTime").val()
					+ "&searchReportEndTime=" + $("#searchReportEndTime").val()
					+ "&searchService=" + $("#searchService").val();
		} else if (objVal == '8') {
			url += "?searchGuarantee=" + $("#searchGuarantee").val()
					+ "&searchReportStartTime="
					+ $("#searchReportStartTime").val()
					+ "&searchReportEndTime=" + $("#searchReportEndTime").val()
					+ "&searchService=" + $("#searchService").val();
		} else if (objVal == '9') {
			url += "?searchTag=" + $("#searchTag").val()
					+ "&searchReportStartTime="
					+ $("#searchReportStartTime").val()
					+ "&searchReportEndTime=" + $("#searchReportEndTime").val()
					+ "&searchService=" + $("#searchService").val();
		} else if (objVal == '10') {
			url += "?searchTag=" + $("#searchTag").val()
					+ "&searchReportStartTime="
					+ $("#searchReportStartTime").val()
					+ "&searchReportEndTime=" + $("#searchReportEndTime").val()
					+ "&searchService=" + $("#searchService").val();
		} else if (objVal == '11') {
			url += "?searchId=" + $("#searchId").val()
					+ "&searchReportStartTime="
					+ $("#searchReportStartTime").val()
					+ "&searchReportEndTime=" + $("#searchReportEndTime").val()
					+ "&searchService=" + $("#searchService").val();
		} else if (objVal == '12') {
			url += "?searchId=" + $("#searchId").val()
					+ "&searchReportStartTime="
					+ $("#searchReportStartTime").val()
					+ "&searchReportEndTime=" + $("#searchReportEndTime").val()
					+ "&searchService=" + $("#searchService").val();
		}
		return url;
	}

	function setColumnShow(obj) {
		$("#compileTbody").html("");
		$("#totalNum").html("");
		$("#totalPageCount").html("");
		$("#currentPage").html("");

		var objVal = $(obj).val();
		if (objVal == '1') {
			$("#urlAddr").val("/gk/service/log/v1/dfJitFlSrcReportSources");

			var cl1 = '<label class="col-md-5 control-label">分类</label> <input type="text" class="form-control task-input-small"	name="searchFl" id="searchFl" placeholder="分类" maxlength="64">';
			$("#cl1").html(cl1);

			var cl2 = '<label class="col-md-5 control-label">性质</label> <input type="text" class="form-control task-input-small" name="searchXz" id="searchXz" placeholder="性质" maxlength="64">';
			$("#cl2").html(cl2);

			var th1 = '<div align="center">分类</div>';
			$("#th1").html(th1);

			$("#th2").show();
			var th2 = '<div align="center">性质</div>';
			$("#th2").html(th2);

			$("#ipCountry").text("源IP所属国家");
			$("#ipProvince").text("源IP所属省");
			$("#ipCity").text("源IP所属市");

		} else if (objVal == '2') {
			$("#urlAddr").val("/gk/service/log/v1/dfJitFlDestReportSources");
			var cl1 = '<label class="col-md-5 control-label">分类</label> <input type="text" class="form-control task-input-small"	name="searchFl" id="searchFl" placeholder="分类" maxlength="64">';
			$("#cl1").html(cl1);

			var cl2 = '<label class="col-md-5 control-label">性质</label> <input type="text" class="form-control task-input-small" name="searchXz" id="searchXz" placeholder="性质" maxlength="64">';
			$("#cl2").html(cl2);

			var th1 = '<div align="center">分类</div>';
			$("#th1").html(th1);

			$("#th2").show();
			var th2 = '<div align="center">性质</div>';
			$("#th2").html(th2);

			$("#ipCountry").text("目的IP所属国家");
			$("#ipProvince").text("目的IP所属省");
			$("#ipCity").text("目的IP所属市");
		} else if (objVal == '3') {

			$("#urlAddr").val("/gk/service/log/v1/dfJitAffairSrcReportSources");
			var cl1 = '<label class="col-md-5 control-label">事件</label> <input type="text" class="form-control task-input-small"	name="searchAffair" id="searchAffair" placeholder="事件" maxlength="64">';
			$("#cl1").html(cl1);

			var cl2 = '<label class="col-md-5 control-label">子话题</label> <input type="text" class="form-control task-input-small" name="searchTopic" id="searchTopic" placeholder="子话题" maxlength="64">';
			$("#cl2").html(cl2);

			var th1 = '<div align="center">事件</div>';
			$("#th1").html(th1);

			$("#th2").show();
			var th2 = '<div align="center">子话题</div>';
			$("#th2").html(th2);

			$("#ipCountry").text("源IP所属国家");
			$("#ipProvince").text("源IP所属省");
			$("#ipCity").text("源IP所属市");

		} else if (objVal == '4') {
			$("#urlAddr")
					.val("/gk/service/log/v1/dfJitAffairDestReportSources");
			var cl1 = '<label class="col-md-5 control-label">事件</label> <input type="text" class="form-control task-input-small"	name="searchAffair" id="searchAffair" placeholder="事件" maxlength="64">';
			$("#cl1").html(cl1);

			var cl2 = '<label class="col-md-5 control-label">子话题</label> <input type="text" class="form-control task-input-small" name="searchTopic" id="searchTopic" placeholder="子话题" maxlength="64">';
			$("#cl2").html(cl2);

			var th1 = '<div align="center">事件</div>';
			$("#th1").html(th1);

			$("#th2").show();
			var th2 = '<div align="center">子话题</div>';
			$("#th2").html(th2);

			$("#ipCountry").text("目的IP所属国家");
			$("#ipProvince").text("目的IP所属省");
			$("#ipCity").text("目的IP所属市");

		} else if (objVal == '5') {
			$("#urlAddr")
					.val("/gk/service/log/v1/dfJitMissionSrcReportSources");
			var cl1 = '<label class="col-md-5 control-label">任务</label> <input type="text" class="form-control task-input-small"	name="searchMission" id="searchMission" placeholder="任务" maxlength="64">';
			$("#cl1").html(cl1);

			var cl2 = '';
			$("#cl2").html(cl2);

			var th1 = '<div align="center">任务</div>';
			$("#th1").html(th1);

			var th2 = '';
			$("#th2").hide();

			$("#ipCountry").text("源IP所属国家");
			$("#ipProvince").text("源IP所属省");
			$("#ipCity").text("源IP所属市");

		} else if (objVal == '6') {
			$("#urlAddr").val(
					"/gk/service/log/v1/dfJitMissionDestReportSources");
			var cl1 = '<label class="col-md-5 control-label">任务</label> <input type="text" class="form-control task-input-small"	name="searchMission" id="searchMission" placeholder="任务" maxlength="64">';
			$("#cl1").html(cl1);

			var cl2 = '';
			$("#cl2").html(cl2);

			var th1 = '<div align="center">任务</div>';
			$("#th1").html(th1);

			var th2 = '';
			$("#th2").hide();

			$("#ipCountry").text("目的IP所属国家");
			$("#ipProvince").text("目的IP所属省");
			$("#ipCity").text("目的IP所属市");
		} else if (objVal == '7') {
			$("#urlAddr").val(
					"/gk/service/log/v1/dfJitGuaranteeSrcReportSources");
			var cl1 = '<label class="col-md-5 control-label">保障期</label> <input type="text" class="form-control task-input-small"	name="searchGuarantee" id="searchGuarantee" placeholder="保障期" maxlength="64">';
			$("#cl1").html(cl1);

			var cl2 = '';
			$("#cl2").html(cl2);

			var th1 = '<div align="center">保障期</div>';
			$("#th1").html(th1);

			var th2 = '';
			$("#th2").hide();

			$("#ipCountry").text("源IP所属国家");
			$("#ipProvince").text("源IP所属省");
			$("#ipCity").text("源IP所属市");

		} else if (objVal == '8') {
			$("#urlAddr").val(
					"/gk/service/log/v1/dfJitGuaranteeDestReportSources");
			var cl1 = '<label class="col-md-5 control-label">保障期</label> <input type="text" class="form-control task-input-small"	name="searchGuarantee" id="searchGuarantee" placeholder="保障期" maxlength="64">';
			$("#cl1").html(cl1);

			var cl2 = '';
			$("#cl2").html(cl2);

			var th1 = '<div align="center">保障期</div>';
			$("#th1").html(th1);

			var th2 = '';
			$("#th2").hide();

			$("#ipCountry").text("目的IP所属国家");
			$("#ipProvince").text("目的IP所属省");
			$("#ipCity").text("目的IP所属市");

		} else if (objVal == '9') {
			$("#urlAddr").val("/gk/service/log/v1/dfJitTagSrcReportSources");
			var cl1 = '<label class="col-md-5 control-label">标签</label> <input type="text" class="form-control task-input-small"	name="searchTag" id="searchTag" placeholder="标签" maxlength="64">';
			$("#cl1").html(cl1);

			var cl2 = '';
			$("#cl2").html(cl2);

			var th1 = '<div align="center">标签</div>';
			$("#th1").html(th1);

			var th2 = '';
			$("#th2").hide();

			$("#ipCountry").text("源IP所属国家");
			$("#ipProvince").text("源IP所属省");
			$("#ipCity").text("源IP所属市");
		} else if (objVal == '10') {
			$("#urlAddr").val("/gk/service/log/v1/dfJitTagDestReportSources");
			var cl1 = '<label class="col-md-5 control-label">标签</label> <input type="text" class="form-control task-input-small"	name="searchTag" id="searchTag" placeholder="标签" maxlength="64">';
			$("#cl1").html(cl1);

			var cl2 = '';
			$("#cl2").html(cl2);

			var th1 = '<div align="center">标签</div>';
			$("#th1").html(th1);

			var th2 = '';
			$("#th2").hide();

			$("#ipCountry").text("目的IP所属国家");
			$("#ipProvince").text("目的IP所属省");
			$("#ipCity").text("目的IP所属市");

		} else if (objVal == '11') {
			$("#urlAddr").val("/gk/service/log/v1/dfJitIdSrcReportSources");
			var cl1 = '<label class="col-md-5 control-label">配置ID</label> <input type="text" class="form-control task-input-small"	name="searchId" id="searchId" placeholder="配置ID" maxlength="64">';
			$("#cl1").html(cl1);

			var cl2 = '';
			$("#cl2").html(cl2);

			var th1 = '<div align="center">配置ID</div>';
			$("#th1").html(th1);

			var th2 = '';
			$("#th2").hide();

			$("#ipCountry").text("源IP所属国家");
			$("#ipProvince").text("源IP所属省");
			$("#ipCity").text("源IP所属市");

		} else if (objVal == '12') {
			$("#urlAddr").val("/gk/service/log/v1/dfJitIdDestReportSources");
			var cl1 = '<label class="col-md-5 control-label">配置ID</label> <input type="text" class="form-control task-input-small"	name="searchId" id="searchId" placeholder="配置ID" maxlength="64">';
			$("#cl1").html(cl1);

			var cl2 = '';
			$("#cl2").html(cl2);

			var th1 = '<div align="center">配置ID</div>';
			$("#th1").html(th1);

			var th2 = '';
			$("#th2").hide();

			$("#ipCountry").text("目的IP所属国家");
			$("#ipProvince").text("目的IP所属省");
			$("#ipCity").text("目的IP所属市");

		}

	}

	function getServiceType(obj) {
		if (obj == "1") {
			return "基于端口的封堵"
		} else if (obj == "2") {
			return "IP白名单配置"
		} else if (obj == "3") {
			return "HTTP协议请求封堵配置"
		} else if (obj == "4") {
			return "HTTP协议响应封堵配置"
		} else if (obj == "5") {
			return "HTTP关键字封堵配置"
		} else if (obj == "6") {
			return "DNS协议封堵配置"
		} else if (obj == "7") {
			return "PPTP协议封堵配置"
		} else if (obj == "8") {
			return "L2TP的协议封堵配置"
		} else if (obj == "9") {
			return "IPSec的协议封堵配置"
		} else if (obj == "10") {
			return "OpenVPN协议封堵配置"
		} else if (obj == "11") {
			return "SSH协议的封堵配置"
		} else if (obj == "12") {
			return "SSL协议封堵配置"
		} else if (obj == "13") {
			return "MAIL封堵配置"
		} else if (obj == "14") {
			return "FTP协议封堵配置"
		} else if (obj == "15") {
			return "HTTP请求头域白名单配置"
		} else if (obj == "16") {
			return "DNS白名单配置"
		} else if (obj == "32") {
			return "L2TP协议封堵配置"
		} else if (obj == "33") {
			return "PPTP协议URL封堵配置"

		} else if (obj == "48") {
			return "IP端口监测配置"
		} else if (obj == "49") {
			return "HTTP协议的监测请求配置"
		} else if (obj == "50") {
			return "HTTP协议的监测响应配置"
		} else if (obj == "51") {
			return "HTTP协议关键字监测配置"
		} else if (obj == "52") {
			return "DNS的协议监测配置"
		} else if (obj == "53") {
			return "FTP的协议监测配置"
		} else if (obj == "54") {
			return "PPTP协议监测配置"
		} else if (obj == "55") {
			return "L2TP的协议监测配置"
		} else if (obj == "56") {
			return "IPSec密钥交互协议监测配置"
		} else if (obj == "57") {
			return "OpenVPN协议监测配置"
		} else if (obj == "58") {
			return "SSH协议监测配置"
		} else if (obj == "59") {
			return "SSL协议监测配置"
		} else if (obj == "60") {
			return "MAIL协议监测配置"
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

			<form name="form2" id="form2">
				<input type="hidden" id="urlAddr" name="urlAddr"
					value="/gk/service/log/v1/dfJitFlSrcReportSources"> <input
					type="hidden" id="pageNo" name="pageNo" value="1"> <input
					type="hidden" id="pageSize" name="pageSize" value="30"> <input
					type="hidden" id="totalCount" name="totalCount">
				<div class="portlet light">
					<div class="portlet-title">
						<div class="caption">
							<!-- 							<i class="fa fa-cogs font-green-sharp"></i>  -->
							<span class="caption-subject font-green-sharp bold uppercase">管控实时统计</span>
						</div>

					</div>
					<div class="form-inline">
						<div class="form-group" id="gkTypeDiv">
							<label class="col-md-3 control-label">管控实时统计类型</label> <select
								class="form-control input-small" name="gkType"
								onchange="setColumnShow(this);" id="gkType">
								<option value="1">管控分类性质源IP实时统计</option>
								<option value="2">管控分类性质目的IP实时统计</option>
								<option value="3">管控事件子话题源IP实时统计</option>
								<option value="4">管控事件子话题目的IP实时统计</option>
								<option value="5">管控任务源IP实时统</option>
								<option value="6">管控任务目的IP实时统计</option>
								<option value="7">管控保障期源IP实时统计</option>
								<option value="8">管控保障期目的IP实时统计</option>
								<option value="9">管控标签源IP实时统计</option>
								<option value="10">管控标签目的IP实时统计</option>
								<option value="11">管控规则源IP实时统计</option>
								<option value="12">管控规则目的IP实时统计</option>
							</select>
						</div>

						<div class="form-group" id="cl1">
							<label class="col-md-5 control-label">分类</label> <input
								type="text" class="form-control task-input-small"
								name="searchFl" id="searchFl" placeholder="配置id" maxlength="64">
						</div>
						<div class="form-group" id="cl2">
							<label class="col-md-5 control-label">性质</label> <input
								type="text" class="form-control task-input-small"
								name="searchXz" id="searchXz" placeholder="配置id" maxlength="64">
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label">业务类型</label> <select
								class="form-control task-input-small" name="searchService"
								id="searchService">
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
							<label class="control-label col-md-5">统计开始时间</label>
							<div class="input-group date-picker input-daterange"
								data-date="2012-10-11 20:10:01" data-date-format="yyyy-mm-dd">
								<input type="text" class="form-control  input-small"
									name="searchReportStartTime" id="searchReportStartTime">
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-md-5">统计结束时间</label>
							<div class="input-group date-picker input-daterange"
								data-date="2012-10-11 20:10:01"
								data-date-format="yyyy-mm-dd hh:ii:ss">
								<input type="text"
									class="form-control datetimepicker input-small"
									name="searchReportEndTime" id="searchReportEndTime">
							</div>
						</div>
						<div class="form-group" id="cl2">
							<label class="col-md-5 control-label">查询哪些字段</label> <input
								type="text" class="form-control task-input-small" name="fields"
								id="fields" placeholder="默认所有" maxlength="64">
						</div>
						<div class="form-group" id="cl2">
							<label class="col-md-5 control-label">排序</label> <input
								type="text" class="form-control task-input-small" name="orderBy"
								id="orderBy" placeholder="" maxlength="64">
						</div>


						<div class="form-group ">
							<a onclick="submitForm();" class="btn btn-success">查询 </a>
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
										<div align="center">id</div>
									</th>
									<th id="th1"><div align="center">分类</div></th>
									<th id="th2"><div align="center">性质</div></th>
									<th><div align="center">统计时间</div></th>
									<th><div align="center" id="ipCountry">源IP所属国家</div></th>
									<th><div align="center" id="ipProvince">源IP所属省</div></th>
									<th><div align="center" id="ipCity">源IP所属市</div></th>
									<th><div align="center">日志数量</div></th>
									<th><div align="center">业务类型</div></th>
								</tr>
							</thead>
							<tbody id="compileTbody">
								<tr>
									<td colspan="10"><div align="center">
											<span class="STYLE1">没有查询到数据</span>
										</div></td>
								</tr>

							</tbody>
						</table>
						<div id="divBoot">
							<div class="row">
								<div class="col-md-5 col-sm-12">
									<div class="">
										总共<span id="totalPageCount"></span>页,<span id="totalNum"></span>
										条数据, 当前第 <span id="currentPage"> <span id="currentPage">
										</span>
										</span>页
									</div>
								</div>
								<div class="col-md-5 col-sm-12">
									每页显示 <select onchange="setPageSize(this)" id="newPageSize"
										class="form-control input-xsmall input-inline">
										<option value="10">10</option>
										<option value="20">20</option>
										<option value="30" selected="selected">30</option>
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
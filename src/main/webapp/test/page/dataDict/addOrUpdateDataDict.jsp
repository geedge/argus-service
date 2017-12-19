<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/test/common/taglib.jsp"%>
<%@ page session="false"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String dictValId = request.getParameter("dictValId");
	String update = request.getParameter("isUpdate");
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>添加数据字典</title>


<%@include file="/test/common/bootstrap_css_js.jsp"%>

<script type="text/javascript">
	$(function() {
		setDictName();
		if (<%=update%>== 1) {
			setDict();
			$("#spanText").text("修改数据字典");
			$("#addOrUpdate").val("update");
		}
	})

	function setDict() {
		$.ajax({
					type : 'post',
					url : '/gk/service/cfg/v1/getDict',
					data : "dictValId=" +<%=dictValId%>	,
					async : false,
					success : function(data, textStatus) {
						$("#dictVal").tagsinput("add", data.data.dataDictValue);
						$("#dataDictId").select2().select2("val",
								data.data.dictNameId);
						$("#dataDictId").attr("disabled", "true");
						$("#addDict").hide();
						$("#dictId").val(data.data.dictValueId);
					}
			  })

	}

	function setDictName() {
		$.ajax({
			type : 'get',
			url : '/gk/service/cfg/v1/getAllDictName',
			async : false,
			success : function(data, textStatus) {
				var option = "<option value=''>请选择</option>";
				if (data.data.length > 0) {
					for (var i = 0; i < data.data.length; i++) {
						var obj = data.data[i];
						option += "	<option value=" + obj.dictNameId+">"
								+ obj.dataDictName + "</option>"
					}

				}
				$("#dataDictId").append(option);
			}
		})

	}

	function showNewDictName() {
		var displayVal = $("#newDictName").css("display");
		if (displayVal == 'none') {
			$("#newDictName").show();
			$("#addDict").html("取消新增");
			$("#dataDictId").attr("disabled", "true");
		} else if (displayVal == 'block') {
			$("#newDictName").hide();
			$("#addDict").html("不存在?新增");
			$("#dataDictId").removeAttr("disabled");
		}
	}

	function submitForm() {

		var addDictName = $("#addDictName").val();
		var name = $("#dataDictId").find("option:selected").text();
		if (name == "请选择" && addDictName == "") {
			alert("请选择或新增数据字典名称");
			return;
		}
		var dictVal = $("#dictVal").val();
		if (dictVal == "" && name != "请选择") {
			alert("数据字典值不能为空");
			return;
		}

		var dataDictId = $("#dataDictId").val();
		var addOrUpdate=$("#addOrUpdate").val();
		var data = "";
		var url = '/gk/service/cfg/v1/addDictName';
		if (addOrUpdate == "update") {
			url = '/gk/service/cfg/v1/updateDictName';
			data = "dictId=" + $("#dictId").val() + "&dictVal=" + dictVal;
		} else {
			data = "dataDictId=" + dataDictId + "&addDictName=" + addDictName
					+ "&dictVal=" + dictVal;
		}
		$.ajax({
			type : 'post',
			url : url,
			data : data,
			async : false,
			success : function(data, textStatus) {
				if (data.msg.indexOf("成功") != -1) {
					alert(data.msg);
					//setDictName();
					if (addOrUpdate == "update") {
						window.location.href = '<%=basePath%>test/page/dataDict/allDataDictVal.jsp';
					}else{
						window.location.reload(true);
					}
				} else {
					alert(data.msg);
				}
			}
		})

	}
	
	
	function setDataDictEffective(){
		$.ajax({
			type : 'post',
			url : '/gk/service/cfg/v1/setDataDictEffective',
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

</head>
<body style="background-color: #2D3233">
	<div class="row">
		<div class="col-md-12" style="height: 800px">
			<!-- BEGIN EXAMPLE TABLE PORTLET-->
			<div class="portlet light">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-cogs font-green-sharp"></i> <span id="spanText"
							class="caption-subject font-green-sharp bold uppercase">添加数据字典</span>
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
								<label class="control-label col-md-3">数据字典名称:<span
									class="required"> * </span></label>
								<div class="col-md-1">
									<select id="dataDictId" name="dataDictId"
										class="form-control select2 col-md-1">
									</select>
								</div>
								<label class="control-label col-md-1"></label> <a id="addDict"
									href="javascript:void(0)" class="btn btn-success col-md-1"
									onclick="showNewDictName()"> 不存在?新增 </a>
							</div>
							<div class="form-group" id="newDictName" style="display: none;">
								<label class="control-label col-md-3">新增数据字典名称:<span
									class="required"> * </span></label>
								<div class="col-md-1">
									<input type="text" class="form-control" name="addDictName"
										id="addDictName" maxlength="64" />
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3">数据字典值:<span
									class="required"> * </span></label>
								<div class="col-md-9">
									<input type="text" data-role="tagsinput" id="dictVal"
										name="dictVal">
								</div>
							</div>

							<div class="form-group">
								<div class="col-md-3"></div>
								<div class="col-md-1">
									<a onclick="submitForm();" class="btn btn-success">提交数据字典</a>
								</div>
								<div class="col-md-1">
									<a href="<%=basePath%>test/page/dataDict/allDataDictVal.jsp"
										class="btn btn-success">查询数据字典 </a> 
								</div>
								<div class="col-md-1">
									<a onclick="setDataDictEffective()" class="btn btn-success">生效</a>
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
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机构管理</title>
<link href="${ctxStatic}/treeTable/themes/vsStyle/treeTable.min.css" rel="stylesheet" type="text/css" />
<script src="${ctxStatic}/treeTable/jquery.treeTable.min.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		var tpl = $("#treeTableTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		
		var data = ${fns:toJson(list)}, rootId = "${not empty sysOffice.id ? sysOffice.id : 0}";
		
		addRow("#treeTableList", tpl, data, rootId, true);
		
		$("#treeTable").treeTable({expandLevel : 5});
		
		
	});
	
	

	function addRow(list, tpl, data, pid, root){
		
		for (var i=0; i<data.length; i++){
			var row = data[i];
			
			var value = ${fns:jsGetVal('row.parentId')};
			if(!value){
				value = 0;
			}
			
			if (value == pid){
				$(list).append(Mustache.render(tpl, {
					dict: {
						type: getDictLabel(${fns:toJson(fns:getDictList('SYS_OFFICE_TYPE'))}, row.type),
						grade: getDictLabel(${fns:toJson(fns:getDictList('SYS_OFFICE_GRADE'))}, row.grade),
						jobType: getDictLabel(${fns:toJson(fns:getDictList('SYS_JOB_TYPE'))}, row.jobType)
					}, 
					pid: (root?0:pid), 
					row: row
				}));
				
				addRow(list, tpl, data, row.id);
			}
		}
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/office/list?id=${sysOffice.id}&parentIds=${sysOffice.parentIds}">机构列表</a></li>
		<shiro:hasPermission name="sys:office:edit"><li><a href="${ctx}/sys/office/form?parent.id=${sysOffice.id}">机构添加</a></li></shiro:hasPermission>
	</ul>

	<sys:message content="${message}"/>
	<table id="treeTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>机构名称</th><th>归属区域</th><th>机构编码</th><th>所属级别</th><th>机构类型</th><th>机构职责分类</th><th>备注</th><shiro:hasPermission name="sys:office:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody id="treeTableList"></tbody>
	</table>
	<script type="text/template" id="treeTableTpl">
		<tr id="{{row.id}}" pId="{{pid}}">
			<td><a href="${ctx}/sys/office/form?id={{row.id}}">{{row.name}}</a></td>
			<td>{{row.area.name}}</td>
			<td>{{row.code}}</td>
			<td>{{dict.grade}}</td>
			<td>{{dict.type}}</td>
			<td>{{dict.jobType}}</td>
			<td>{{row.remarks}}</td>
			<shiro:hasPermission name="sys:office:edit"><td>
				<a href="${ctx}/sys/office/form?id={{row.id}}">修改</a>
				<a href="${ctx}/sys/office/delete?id={{row.id}}" onclick="return confirmx('要删除该机构及所有子机构项吗？', this.href)">删除</a>
				<a href="${ctx}/sys/office/form?parent.id={{row.id}}">添加下级机构</a> 
			</td></shiro:hasPermission>
		</tr>
	</script>
	
</body>
</html>
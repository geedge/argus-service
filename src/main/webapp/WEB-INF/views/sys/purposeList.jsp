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
		
		var data = ${fns:toJson(list)}, rootId = "${not empty regPurposeInfo.id ? regPurposeInfo.id : 0}";
		
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
		<li class="active"><a href="${ctx}/sys/purpose/list?id=${regPurposeInfo.id}&parentIds=${regPurposeInfo.parentIds}">信访目的列表</a></li>
		 <li><a href="${ctx}/sys/purpose/form?parentId=1">信访目的添加</a></li> 
	</ul>
 
	
	<sys:message content="${message}"/>
	<table id="treeTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>信访名称</th>
				<th>备注</th>
			 
					<th>操作</th>
				 
			</tr>
		</thead>
		<tbody id="treeTableList"></tbody>
	</table>
	<script type="text/template" id="treeTableTpl">
		<tr id="{{row.id}}" pId="{{pid}}">
			<td><a href="${ctx}/sys/purpose/form?id={{row.id}}">{{row.name}}</a></td>
			<td>{{row.remark}}</td>
			<td>
				<a href="${ctx}/sys/purpose/form?id={{row.id}}">修改</a>
				<a href="${ctx}/sys/purpose/delete?id={{row.id}}" onclick="return confirmx('要删除该机构及所有子目的项吗？', this.href)">删除</a>
				<a href="${ctx}/sys/purpose/form?parentId={{row.id}}">添加下级目的</a> 
 			</td>
		</tr>
	</script>
	
</body>
</html>
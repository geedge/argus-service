<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title>字典管理</title>
<style type="text/css">
	
	.revisionBox {
		margin: 5px;
	}
	
	span.label.label-success{
		width: 250px;
	}
	.popover {
	  max-width: 800px;
	}
	
	.btn.purple-stripe {
	    border-left: 3px solid #852B99;
	}

</style>
<script type="text/javascript">

	$(document).ready(function() {
		
		$(".revision").popover('destroy');
		$(".revision").popover({
			animation:true,
			html:true,
			placement:'left',
			title:'修改记录',
			content:function(){
				var html = []; 
				var content = $(this).attr("data-content");
				if(!(content=="" || content==null)){
					var strs = content.split("|");
  					for(i=0;i<strs.length;i++){
  						if(!(strs[i]=="" || strs[i]==null)){
  						 	html.push('<span class="label label-success">'+strs[i]+'</span>'); 
  							html.push('\t');
  							if((i+1)%2==0){
  							 	html.push('<br/>'); 
  							}
  						}
  					}
				}else {
					html.push('<span class="label label-success">暂无修改记录！</span>'); 
				}
				return html.join('');
				
			}			
		});
	});
	
	function dictDetail(dictId){
		$.ajax({
  			type:'post',
			url:'${ctx}/sys/dict/loadDataDict',
			data:{"id":dictId},
			dataType:'json',
			async:false,
			success:function(data,textStatus){
				if(textStatus=="success"){

					var revisions = []; 
      					var content = data.revision;
      					if(!(content=="" || content==null)){
      						var strs = content.split("|");
	      					for(i=0;i<strs.length;i++){
	      						if(!(strs[i]=="" || strs[i]==null)){
	      						 	revisions.push('<span class="label label-success" style="width: 250px;">'+strs[i]+'</span>'); 
	      							 revisions.push('\t');
	      							 if((i+1)%2==0){
	      							 	 revisions.push('<br/>'); 
	      							 }
	      						}
	      					}
      					}else {
      						revisions.push('<span class="label label-success">暂无修改记录！</span>'); 
      					}
      					
      					
					
					
					var html = ""
      					html+="<div style='font-size: 14px;'>";
		      			html+="<table class='table table-bordered table-condensed'>";
		      			html+="<tbody>";
		      			
		      			html+="<tr>";
		      			html+="<th>模块名称：";
		      			html+="</th>";
		      			html+="<td colspan='3'>"+data.moduleName;
		      			html+="</td>";
						html+="</tr>";
		      			html+="<tr>";
		      			html+="<th>字典标识：";
		      			html+="</th>";
		      			html+="<td colspan='3'>"+data.mark;
		      			html+="</td>";
						html+="</tr>";
		      			
						
						html+="<tr>";
		      			html+="<th>运行状态：";
		      			html+="</th>";
		      			html+="<td colspan='3'>";
		      			 	html+="<table class='table table-bordered table-condensed'>";
		      			  	html+="<thead>";
		      			  		html+="<th>标识</th><th>值</th><th>描述</th><th>是否可用</th><th>是否可维护</th>";
		      			   	html+="</thead>";
		      			   	html+="<tbody>";
		      			   	for(i=0;i<data.dictItemList.length;i++){
		      			   		html+="<tr>";
		      			   		html+="<td class='taskStatus'>"+data.dictItemList[i].itemCode;
		      					html+="</td>";
		      					html+="<td class='taskStatus'>"+data.dictItemList[i].itemValue;
		      					html+="</td>";
		      					html+="<td class='taskStatus'>"+data.dictItemList[i].itemDesc;
		      					html+="</td>";
		      					html+="<td class='taskStatus'>";
		      					if(data.dictItemList[i].status==1){
		      						html+="<span class='in-progress'>是</span>";
		      					}else {
		      						html+="<span class='pending'>否</span>";
		      					}	
		      					html+="</td>";
		      					html+="<td class='taskStatus'>";
		      					if(data.dictItemList[i].type==1){
		      						html+="<span class='in-progress'>是</span>";
		      					}else {
		      						html+="<span class='pending'>否</span>";
		      					}	
		      					html+="</td>";
		      			   		html+="</tr>";
		      			   		
		      			   		
		      			   	}
		      			   	html+="</tbody>";
		      			 	html+="</table>";
                		 
		      			html+="</td>";
						html+="</tr>";
						
						
						
						html+="<tr>";
		      			html+="<th>创建时间：";
		      			html+="</th>";
		      			html+="<td>"+data.createTime;
		      			html+="</td>";
		      			html+="<th>最后修改时间：";
		      			html+="</th>";
		      			html+="<td>"+data.modifyTime;
		      			html+="</td>";
						html+="</tr>";
						
						html+="<tr>";
		      			html+="<th>描述：";
		      			html+="</th>";
		      			html+="<td colspan='3'>"+data.remark;
		      			html+="</td>";
						html+="</tr>";
						
						
						html+="<tr>";
		      			html+="<th>字典修改记录：";
		      			html+="</th>";
		      			html+="<td colspan='3'>"+revisions.join('');
		      			html+="</td>";
						html+="</tr>";
						html+="</tbody>";	              
						html+="</table>";
						html+="</div>";  
						
						top.$.jBox(html,{width: $(document).width()*0.6,height: 480,title:"字典详情", buttons:{"关闭":true}});
						
			
				
							
				}
					 
			}
	  
	  });

	}	
	
	
	function page(n,s){
		if(n) $("#pageNo").val(n);
		if(s) $("#pageSize").val(s);
		$("#searchForm").attr("action","${ctx}/sys/dict/list");
		$("#searchForm").submit();
    	return false;
    }
	
	
	
</script>
</head>
<body>
	
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/dict/list">字典列表</a></li>
			<shiro:hasPermission name="sys:dict:edit"></shiro:hasPermission>
			<li><a href="${ctx}/sys/dict/form">字典添加</a></li>
	</ul>
	

	<form:form id="searchForm" action="${ctx}/sys/dict/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<div>
			<label>模块名称：</label><input id="moduleName" name="moduleName" type="text" maxlength="50" class="input-medium" value="${sysDataDictionaryName.moduleName}"/>
			<label>字典标识：</label><input id="mark" name="mark" type="text" maxlength="50" class="input-medium" value="${sysDataDictionaryName.mark}"/>

			<label>创建时间：&nbsp;</label><input id="beginDate" name="beginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${sysDataDictionaryName.beginDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			<label>&nbsp;--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label><input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${sysDataDictionaryName.endDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		</div>
	</form:form>
	
	<sys:message content="${message}"/>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th class="sort-column module_name" width="15%">模块名</th>
				<th class="sort-column mark"  width="20%">字典标识</th>
				<th class="sort-column create_time" width="15%">创建时间</th>
				<th  width="17%">描述</th>
				<th  width="18%">修改记录</th>
				<shiro:hasPermission name="sys:dict:edit">
				<th width="15%">操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="dict">
			<tr>
				
				<td>${dict.moduleName}</td>
				<td>${dict.mark}</td>
				<td><fmt:formatDate value="${dict.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td title="${dict.remark }">${fns:abbr(dict.remark,15)}</td>
				<td>
					<button class="btn revision popovers purple-stripe" data-content="${dict.revision}">修改记录查看</button>
				</td>
				
				<td>
	
					<div class="btn-group">
				      <a class="btn btn-primary" href="#"><i class="icon-cogs"></i> 操作</a>
				      <a class="btn btn-primary dropdown-toggle" data-toggle="dropdown" href="#"><span class="caret"></span></a>
				      <ul class="dropdown-menu">
				      
			        	<li><a href="javascript:dictDetail('${dict.id}')"><i class="icon-list"></i> 详情</a></li>
			        	<li class="divider"></li>
				       
				        <shiro:hasPermission name="sys:dict:edit">
					        <li><a href="${ctx}/sys/dict/form?id=${dict.id}" onclick="return confirmx('数据字典在系统中非常重要，您确认要修改吗？', this.href)"><i class="icon-edit"></i> 修改</a></li>
					        <li class="divider"></li>
				        </shiro:hasPermission>
				        <shiro:hasPermission name="sys:dict:edit">
				       	 	<li><a href="${ctx}/sys/dict/delete?id=${dict.id}" onclick="return confirmx('数据字典在系统中非常重要，您确认要删除吗？', this.href)"><i class="icon-trash"></i> 删除</a></li>
						</shiro:hasPermission>
				    
				      </ul>
				    </div>
				    
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>	
</body>
</html>
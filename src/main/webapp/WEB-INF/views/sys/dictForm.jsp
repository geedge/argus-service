<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<style type="text/css">
		#items p input{
			margin-left:0px;
			width:80px;
		}
		#items p select{
			width:50px;
		}
	</style>
	<script type="text/javascript">
		$(document).ready(function() {
			
			$("#btn_add_item").click(function(){
				var itemCopy = $("#itemModel").clone();
				itemCopy.show();
				$("#items").append(itemCopy);	
			});
			
			//删除子项
			$(".delItem").live("click",function(){
				$(this).parent().remove();
			});
			
		    var $inputForm = $("#inputForm");
		    var $error = $('.alert-error', $inputForm);
            var $success = $('.alert-success', $inputForm);
            
            $inputForm.validate({
				rules: {
					'moduleName':{
						required:true
					},
					'mark':{
						required:true
					}
			
				},
				messages: {
					'moduleName':{
						required:'模块名称必须填写'
					},
					'mark':{
						required:'字典标识必须填写'
					}
				},
				submitHandler: function(form){
					
					if(!validateItem()) {
						return false;
					}

					loading('正在提交，请稍等...');
					form.submit();
	                
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
		});
		
		function validateItem(){

			var $itemsObject = $("#items >p:visible");
			if($itemsObject.length==0){
				$("#messageBox").text("保存信息出现问题，请添加词条项！").show();
				return false;
				
			}else {
				
				var flag = true;
				$.each($itemsObject,function(i,obj){
					var code = $(obj).find(".itemCode").val();
					var value = $(obj).find(".itemValue").val();
					if(code==""||value==""){
						flag = false;
						return false;
					}
				});
				
				if(!flag){
					$("#messageBox").text("保存信息出现问题，每个词条标识与值都不能为空！").show();
					return false;
				}
				
				//给每个对象设置name属性
				$.each($itemsObject,function(index,obj){
					$(obj).find(".itemCode").attr("name","dictItemList["+index+"].itemCode");
					$(obj).find(".itemValue").attr("name","dictItemList["+index+"].itemValue");
					$(obj).find(".itemDesc").attr("name","dictItemList["+index+"].itemDesc");
					$(obj).find(".itemStatus").attr("name","dictItemList["+index+"].status");
					$(obj).find(".itemType").attr("name","dictItemList["+index+"].type");
					
				});
			}
			
			return true;
			
		}

		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/dict/list">字典列表</a></li>
		<li class="active"><a href="${ctx}/sys/dict/form?id=${sysDataDictionaryName.id}">字典
			<shiro:hasPermission name="sys:dict:edit"></shiro:hasPermission>${not empty sysDataDictionaryName.id?'修改':'添加'}
			<shiro:lacksPermission name="sys:dict:edit">查看</shiro:lacksPermission>
		</a></li>
	</ul><br/>
	
	
	<form:form id="inputForm" modelAttribute="sysDataDictionaryName" action="${ctx}/sys/dict/saveOrUpdate" method="post" class="form-horizontal">
	
	
		<form:hidden path="id"/>
		<form:hidden path="revision"/>
		<sys:message content="${message}"/>
		
	
		<div class="alert alert-error hide">
			<button class="close" data-dismiss="alert"></button>
			提交配置出现问题，请看错误提示！
		</div>

		<div class="alert alert-success hide">

			<button class="close" data-dismiss="alert"></button>
			表单验证成功！
		</div>
									
		<div class="control-group">
			<label class="control-label">模块名:</label>
			<div class="controls">
				<form:input path="moduleName" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">字典标识:</label>
			<div class="controls">
				<form:input path="mark" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div class="control-group">

			<label class="control-label">词条项</label>

			<div class="controls">
				<span type="button" class="btn black" title="点击添加词条项" id="btn_add_item">
					<i class="icon-plus"></i> 添加词条
				</span>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
			
			 <div id="items" class="controls">
               <p id="itemModel" style="display: none;margin-top: 3px;">
               		标识 <input type="text"  class="itemCode" autocomplete="off"/>
               		   值 <input type="text"  class="itemValue" autocomplete="off"/>
               		 描述 <input type="text"  class="itemDesc" autocomplete="off"/>
               		  	可用
               		  	<select class="itemStatus noSelect2">
               		  		<option value="1">是</option>
               		  		<option value="0">否</option>
               		  	</select>
					   可维护 
						<select class="itemType noSelect2">
               		  		<option value="1">是</option>
               		  		<option value="0">否</option>
               		  	</select>
					   <input type="button" class="btn red delItem"  value="删除"/>
               </p>
               
            	<c:forEach items="${sysDataDictionaryName.dictItemList }" var="dictItem">
	            	 <p id="itemModel" style="margin-top: 3px;" >
	               		标识 <input type="text"  class="itemCode" value="${dictItem.itemCode}" autocomplete="off" <c:if test="${dictItem.type==0}">readonly="readonly"</c:if>/>
	               		   值 <input type="text"  class="itemValue" value="${dictItem.itemValue}" autocomplete="off" <c:if test="${dictItem.type==0}">readonly="readonly"</c:if>/>
	               		 描述 <input type="text"  class="itemDesc" value="${dictItem.itemDesc}" autocomplete="off" <c:if test="${dictItem.type==0}">readonly="readonly"</c:if>/>
	               		  	可用
	               		  	<select class="itemStatus noSelect2" <c:if test="${dictItem.type==0}">readonly="readonly"</c:if>>
	               		  		<option value="1" <c:if test="${dictItem.status==1}">selected</c:if>>是</option>
	               		  		<option value="0" <c:if test="${dictItem.status==0}">selected</c:if>>否</option>
	               		  	</select>
						   可维护 
							<select class="itemType noSelect2" <c:if test="${dictItem.type==0}">readonly="readonly"</c:if>>
	               		  		<option value="1" <c:if test="${dictItem.type==1}">selected</c:if>>是</option>
	               		  		<option value="0" <c:if test="${dictItem.type==0}">selected</c:if>>否</option>
	               		  	</select>
	               		  	<c:if test="${dictItem.type==1}">
	               		  		<input type="button" class="btn red delItem"  value="删除"/>
	               		  	</c:if>
						   
	               </p>
	            </c:forEach>
               
               </div>

		</div>
		
		<div class="control-group">
			<label class="control-label">描述信息:</label>
			<div class="controls">
				<form:textarea path="remark" htmlEscape="false" rows="3" maxlength="200"  class="span6"/>
			</div>
		</div>
		
		<div id="messageBox" class="alert alert-error hide"></div>
		
		<div class="form-actions">
			<shiro:hasPermission name="sys:dict:edit"></shiro:hasPermission>
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
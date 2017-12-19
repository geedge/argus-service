<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
	<title>信访目的管理</title>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
				 
					if(validateForm()){
						$("input[type='submit']").attr("disabled",true);
						loading('正在提交，请稍等...');
						form.submit();
						parent.location.reload();
					}
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
		
		
		function validateForm(){
			purposeId=$("#id").val();
			purposeParentId=$("#parentId").val();
		 
			if(purposeId==purposeParentId){
				
				$("#messageBox").text("上级信访目的不能是自己").show();
				return false;
			}
			
			return true;
			
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		 
		<li class="active"><a href="#">目的${not empty regPurposeInfo.id?'修改':'添加'}</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="sysOffice" action="${ctx}/sys/purpose/updateOrSave" method="post" class="form-horizontal">
	 <input type="hidden" id="id" name="id" value="${regPurposeInfo.id}">
	  <input type="hidden" id="saveParentId" name="saveParentId" value="${regPurposeInfo.parentId}">
		<sys:message content="${message}"/>
		<div id="messageBox" class="alert alert-error hide"></div>
		<c:if test="${update }">
			<div class="control-group">
				<label class="control-label">上级目的:</label>
				<div class="controls">
	                <sys:treeselect id="parent" name="parentId" value="${regPurposeInfo.parentId}" labelName="" labelValue="${parentName}"  
						title="目的" url="/letter/reg/purposeTreeData"  />
				</div>
			</div>
		</c:if>
		<c:if test="${add}">
			<div class="control-group">
				<label class="control-label">上级目的:</label>
				<div class="controls">
	             ${parentName}
				</div>
			</div>
		</c:if>
		<div class="control-group">
			<label class="control-label">目的名称:</label>
			<div class="controls">
				<input type="text"   name="name"  value="${regPurposeInfo.name}" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		 

	 <c:if test="${update }">
		<div class="control-group">
			<label class="control-label">创建时间:</label>
			<div class="controls">
			 
			<c:if test="${ empty createTime}">
			<div  style="margin-top:5px;">无</div>	
			</c:if>
			<c:if test="${not empty createTime}">
			 <div  style="margin-top:5px;">${createTime}</div> 
			</c:if>
			</div>
		</div>
	</c:if>
	
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<textarea class="span12" rows="3" name="remark" id="remark"   >${regPurposeInfo.remark}</textarea>
			 
			</div>
		</div>
		 
		<div class="form-actions">
	 
			 <input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp; 
		
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
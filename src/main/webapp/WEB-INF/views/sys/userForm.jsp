<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#no").focus();
			
			$("#inputForm").validate({
				rules: {
					loginId: {remote: "${ctx}/sys/user/checkLoginName?oldLoginId=" + encodeURIComponent('${user.loginId}')},
					'office.name': {officeIsValid: true }
				},
				messages: {
					loginId: {remote: "用户登录名已存在"},
					'office.name': {officeIsValid: "部门不再公司范围内"},
					'entity.name': {required: "需选择所在单位"},
					confirmNewPassword: {equalTo: "输入与上面相同的密码"}
				},
				submitHandler: function(form){
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
			
			
			 var isValid;//是否有效
			   $.validator.addMethod(
					"officeIsValid", 
					function(value, element,param) { 
						var companyId=$("#companyId").val();
						var officeId=$("#officeId").val();
						if(param){
						
							$.ajax({
		  						type:'post',
		 						url:'${ctx}/sys/user/checkOffice',
		 						data:{officeId:officeId, companyId:companyId},
		 						dataType:'text',
		  						async:false,
		  						success:function (data,textStatus){	
									if(textStatus=="success"){
										if(data=='true'){
											isValid= true;
										}else {
											isValid= false;
										}
									}
								}
							});
							
							return isValid;
						}
					}
				);
			
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/user/list">用户列表</a></li>
		<li class="active"><a href="${ctx}/sys/user/form?id=${user.id}">用户
			<shiro:hasPermission name="sys:user:edit"></shiro:hasPermission>${not empty user.id?'修改':'添加'}
			<shiro:lacksPermission name="sys:user:edit">查看</shiro:lacksPermission>
		</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="sysUser" action="${ctx}/sys/user/saveOrUpdate" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		
		<div class="control-group">
			<label class="control-label">头像:</label>
			<div class="controls">
				<form:hidden id="nameImage" path="photo" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="nameImage" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">登录名:</label>
			<div class="controls">
				<input id="oldLoginId" name="oldLoginId" type="hidden" value="${user.loginId}">
				<c:if test="${not empty user.id}" var="exisitUser">
					<form:input path="loginId" htmlEscape="false" maxlength="50" readonly="true" class="required userName"/>
				</c:if>
				
				<c:if test="${!exisitUser}">
					<form:input path="loginId" htmlEscape="false" maxlength="50" class="required userName"/>
				</c:if>
				
				
				
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">归属公司:</label>
			<div class="controls">
                <sys:treeselect id="company" name="company.id" value="${user.company.id}" labelName="company.name" labelValue="${user.company.name}"
					title="公司" url="/sys/office/treeData?type=1" cssClass="required" notAllowSelectRoot="true"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">归属单位:</label>
			<div class="controls">
                <sys:treeselect id="entity" name="entity.id" value="${user.entity.id}" labelName="entity.name" labelValue="${user.entity.name}"
					title="单位" url="/sys/office/treeData?type=2" cssClass="required" notAllowSelectRoot="true"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">归属部门:</label>
			<div class="controls">
                <sys:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}"
					title="部门" url="/sys/office/treeData?type=3" cssClass="required" notAllowSelectRoot="true"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">姓名:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">密码:</label>
			<div class="controls">
				<input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="3" class="${empty user.id?'required':''}"/>
				<c:if test="${empty user.id}"><span class="help-inline"><font color="red">*</font> </span></c:if>
				<c:if test="${not empty user.id}"><span class="help-inline">若不修改密码，请留空。</span></c:if>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">确认密码:</label>
			<div class="controls">
				<input id="confirmNewPassword" name="confirmNewPassword" type="password" value="" maxlength="50" minlength="3" equalTo="#newPassword"/>
				<c:if test="${empty user.id}"><span class="help-inline"><font color="red">*</font> </span></c:if>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">身份标识:</label>
			<div class="controls">
				<form:select path="identity" class="input-medium">
					<form:option value="0">普通人员</form:option>
					<form:option value="1">管理人员</form:option>
				</form:select>
				<span class="help-inline">管理人员需指定身份标识为“管理人员”，否则影响信访审核流程！</span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">邮箱:</label>
			<div class="controls">
				<form:input path="email" htmlEscape="false" maxlength="100" class="required email"/><span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label">用户角色:</label>
			<div class="controls">
				<form:checkboxes path="roleIdList" items="${allRoles}" itemLabel="name" itemValue="id" htmlEscape="false" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<c:if test="${not empty user.id}">
			<div class="control-group">
				<label class="control-label">创建时间:</label>
				<div class="controls">
					<label class="lbl"><fmt:formatDate value="${user.createTime}" type="both" dateStyle="full"/></label>
				</div>
			</div>
			
		</c:if>
		<div class="form-actions">
			<shiro:hasPermission name="sys:user:edit"></shiro:hasPermission>
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
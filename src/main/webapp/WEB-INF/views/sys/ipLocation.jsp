<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
	<title>ip定位</title>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			jQuery.validator.addMethod("ip", function(value, element) {    
			      return this.optional(element) || /^(([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.)(([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.){2}([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))$/.test(value);    
			    }, "请填写正确的IP地址。");
			
			$("#inputForm").validate({
				submitHandler: function(form){
					form.submit();
				},
				rules: {
					
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
	</script>
</head>
<body>
	
	<form:form id="inputForm"  action="${ctx}/sys/ipLocation/search" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		
		<div class="control-group">
			<label class="control-label">IP地址:</label>
			<div class="controls">
				<input name="searchIp" id="searchIp"  class="span6 required"/>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			</div>
		</div>
		
		
	</form:form>
</body>
</html>
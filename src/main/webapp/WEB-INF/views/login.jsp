<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="shortcut icon" href="${pageContext.request.contextPath}/static/images/mvc_frame.ico"/>
	<script src="${pageContext.request.contextPath }/static/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
	<link href="${pageContext.request.contextPath }/static/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
	<script src="${pageContext.request.contextPath }/static/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>
	<link href="${pageContext.request.contextPath }/static/common/login.css" type="text/css" rel="stylesheet" />
	<script src="${pageContext.request.contextPath }/static/common/cloud.js" type="text/javascript"></script>
	<title>${fns:getStringProperty('productName','NIS')}-登录平台</title>
</head>
<body>
	 <div id="mainBody">
      <div id="cloud1" class="cloud"></div>
      <div id="cloud2" class="cloud"></div>
    </div> 
	 
	 <div class="loginbody">
    	<span class="systemlogo"></span> 
    	<form id="loginForm" class="form-signin" action="${pageContext.request.contextPath }/login" method="post">
	    	<div class="loginbox loginbox2">
			    <ul>
				    <li><input id="username" name="username" type="text" class="loginuser required" value="${username}" placeholder="请填写登录账号"/></li>
				    <li><input type="password"  class="loginpwd required"  id="password" name="password" placeholder="请填写登录密码"/></li>
				  	<c:if test="${not empty isValidateCodeLogin or isValidateCodeLogin==true}">
			             <li class="yzm ${ (empty isValidateCodeLogin or isValidateCodeLogin==false) ?'hide':''  }">
				   	 		<span><input id="captcha" name="captcha" class="required" type="text" placeholder="验证码"/></span><cite><img alt="未获取验证码" src="${pageContext.request.contextPath}/captcha-image" id="captacha_image" class="mid"/></cite> 
				    	</li>
			             
					 </c:if>
				    
				    
				    <li><input  type="submit" class="loginbtn" value="登录"/>
				    	<label><input  id="rememberMe" name="rememberMe" ${rememberMe ? 'checked' : ''} type="checkbox" />记住我（公共场所慎用）</label>
				    </li>
			    </ul>
			     
			    <div class="bottom">
					<div id="messageBox" class="alert alert-error ${empty message ? 'hide' : ''}">
						<label id="loginError" class="error">${message}</label>
					</div>
				</div>
			    
	    	</div>
	    </form>
    
    </div>
    

 	<div class="loginbm">
		Copyright &copy; 2015-${fns:getStringProperty('copyrightYear','2015')} <a href="${pageContext.request.contextPath}">${fns:getStringProperty('productName','NIS')}</a> - Powered By <a href="${pageContext.request.contextPath}" target="_blank">NIS</a> ${fns:getStringProperty('version','1.0.0')}
    </div>
        <script src="${pageContext.request.contextPath}/static/flash/zoom.min.js" type="text/javascript"></script>
        <script type="text/javascript">
            $(document).ready(function(){
            	
            	 $('.loginbox').css({'position':'absolute','left':($(window).width()-692)/2});
            	 $(window).resize(function(){  
            	    $('.loginbox').css({'position':'absolute','left':($(window).width()-692)/2});
            	 })  
            	
            	$("#captacha_image").on("click",function(){
            		$(this).hide().attr("src","${pageContext.request.contextPath }/captcha-image?"+ Math.floor(Math.random()*100))
            			.fadeIn();
            	});
            	
            	
            	$("#loginForm").validate({
    				rules: {
    					username: { required: true},
                        password: { required: true},
                        captcha: {remote: "${pageContext.request.contextPath}/validateCode"}
    				},
    				messages: {
    					username: {required: "请填写用户名..."},password: {required: "请填写密码..."},
    					captcha: {remote: "验证码不正确...", required: "请填写验证码..."}
    				},
    				errorLabelContainer: "#messageBox",
    				errorPlacement: function(error, element) {
    					error.appendTo($("#loginError").parent());
    				} 
    			});
                
                
                
             // 如果在框架或在对话框中，则弹出提示并跳转到首页
        		if(self.frameElement && self.frameElement.tagName == "IFRAME" || $('#left').length > 0 || $('.jbox').length > 0){
        			alert('未登录或登录超时。请重新登录，谢谢！');
        			top.location = "${pageContext.request.contextPath }";
        		}
                
                
            });
        </script>

</body>
</html>
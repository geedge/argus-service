<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--[if IE 8]> <html class="ie8"> <![endif]-->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><sitemesh:write property="title"/></title>
<%@include file="/WEB-INF/include/header.jsp" %>
<sitemesh:write property="head"/>
</head>
<body>
	<sitemesh:write property="body"/>
</body>
</html>
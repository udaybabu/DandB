<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<spring:url value="/login/auth" var="actionUrl" />

<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="0">
<title>Login</title>
<link rel="icon" href="<%=request.getContextPath()%>/resources/images/dnbFavicon.png" type="image/x-icon">
<link href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/resources/css/font-awesome.min.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/resources/css/style.css" rel="stylesheet">
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>
</head>
<body class="login">
<s:form action="${actionUrl}" method="post" id="userLoginForm" commandName="userLoginForm">
<div class="container">
<div class="row" style="padding-top:15%;">
<div class="col-lg-6 col-md-6">
<img alt="Logo" src="<%=request.getContextPath()%>/resources/images/loginLogo.png">
</div>
<div class="col-lg-6 col-md-6" style="border-left:1px solid #35424b">
<div class="form-group">
		<c:forEach items="${message}" var="var">
		<font color="red" style="font-size:16px;">${var}</font>
		</c:forEach>
		</div>
 <div class="form-group">
      <s:input path="userName" cssClass="form-control required" maxlength="20" placeholder="Login Id"/>
</div>
 <div class="form-group" style="margin-bottom:30px;">
      <s:password path="password" cssClass="form-control required" maxlength="30" placeholder="Password"/>
    </div>
    <div class="form-group">  
    <button type="submit" class="btn btn-primary btn-login"><spring:message code="common.button.login" /></button>
  </div>
</div>

</div>
</div>

<%-- <div class="row">
<div class="col-lg-4 col-md-4 col-lg-offset-4 col-md-offset-4 text-center login_bg">
<p style="margin-bottom:30px;"><img src="<%=request.getContextPath()%>/resources/images/app_Logo.png" alt=""/></p>
<p class="fa fa-user-circle fa-4x" style="color:#556080;margin-bottom:30px;"></p>
 <div class="form-group">
    <div class="input-group">
      <s:input path="userName" cssClass="form-control required" maxlength="20" placeholder="Login Id"/>
      <div class="input-group-addon" style="padding-left: 16px;"><i class="fa fa-user"></i></div>
    </div>
</div>
<div class="form-group">
		<c:forEach items="${message}" var="var">
		<font color="red" style="font-size:16px;">${var}</font>
		</c:forEach>
		</div>
 <div class="form-group" style="margin-bottom:30px;">
    <div class="input-group">
      <s:password path="password" cssClass="form-control required" maxlength="30" placeholder="Password"/>
      <div class="input-group-addon"><i class="fa fa-key"></i></div>
    </div>
</div>
<div class="form-group">  
    <button type="submit" class="btn btn-primary"><spring:message code="common.button.login" /></button>
  </div>
</div>
</div> --%>
</s:form>

<footer class="footer">
      <div class="container text-center">
     	<a href="http://orientindia.com/" class="text-center"  target="_blank" style="margin-bottom:0;color:#f7f7f7">© <%= new java.text.SimpleDateFormat("yyyy").format(new java.util.Date()) %> <spring:message code="footer.orient.message"></spring:message></a>
      </div>
      
    </footer>
    <script>
$(document).ready(function(){
	$("#userLoginForm").validate({
		rules: {
			userName: {required: true},
			password:{required:true}
		}
	});
});

</script>
</body>
</html>
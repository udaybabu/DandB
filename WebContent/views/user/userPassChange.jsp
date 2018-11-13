<%@page import="in.otpl.dnb.util.URLConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<spring:url value="<%= URLConstants.USER_PASSWORD_UPDATE %>" var="userPassUpdate"/>
<script type="text/javascript">

function trimValue(field) {
	var val = field.value;
	field.value = $.trim(val);
}

function validateUser(){
	var password = $("#password").val();
	var oldPassword = $("#oldPassword").val();
	var confirmPassword = $("#confirmPassword").val();
	if(oldPassword == null || oldPassword.trim() == ""){
		$("#oldPassword").focus();
		bootbox.alert('<spring:message code="user.errors.password.curr.required"/>');
	    $("#submit-dis").attr("disabled", false);
		return false;
	}
	if(password == null || password.trim() == ""){
		$("#password").focus();
		bootbox.alert('<spring:message code="user.errors.password.required"/>');
	    $("#submit-dis").attr("disabled", false);
		return false;
	}
	if(password != null && password.trim() != "" && password.length < 6){
		bootbox.alert('<spring:message code="user.errors.newpassword.minLength"/>');
		$("#submit-dis").attr("disabled", false);
		$("#password").focus();
		return false;
	}
	if(confirmPassword == null || confirmPassword.trim() == ""){
		$("#confirmPassword").focus();
		bootbox.alert('<spring:message code="user.errors.confirmNewPassword.match"/>');
	    $("#submit-dis").attr("disabled", false);
		return false;
	}
	if(password != confirmPassword){
		$("#confirmPassword").val("");
		bootbox.alert('<spring:message code="user.errors.confirmpassword.match"/>');
		$("#submit-dis").attr("disabled", false);
		$("#password").focus();
		return false;
	}
	
}
</script>
 <div class="container-fluid">
<div class="row">
    <div class="col-lg-12 col-md-12 text-center">
    <c:forEach items="${succMessage}" var="var">
    <font color="green">${var}</font><br>
    </c:forEach>
    <c:forEach items="${errMessage}" var="var">
    <font color="red">${var}</font><br>
    </c:forEach>
    </div>
</div>
</div>

<s:form action="${userPassUpdate}" method="post" commandName="userForm" onsubmit="return validateUser();">
     <div class="container-fluid">
   	<div class="panel panel-default">
   	<div class="panel-heading">Change Password
 	</div>
  	<div class="panel-body" style="padding-top:0">
    <div class="row">
    <div class="col-lg-12" style="padding-top:15px;">
    <div class="row">
    <div class="col-lg-4">
    <div class="form-group">
    <label><spring:message code="user.label.password.current"/><sup class="text-danger">*</sup></label> 
    <spring:message code="user.label.password.current" var="passCurr"/>
	<s:password path="oldPassword" id="oldPassword" class="form-control" placeholder="${passCurr}" onblur="trimValue(this)"/>
	<span style="color:red;"><s:errors path="oldPassword" cssClass="error" /></span>
    </div>
    </div>
    <div class="col-lg-4">
    <div class="form-group">
    <label><spring:message code="user.label.password.new"/><sup class="text-danger">*</sup></label> 
    <spring:message code="user.label.password.new" var="passNew"/>
	<s:password path="password" id="password" class="form-control" placeholder="${passNew}" onblur="trimValue(this)"/>
	<span style="color:red;"><s:errors path="password" cssClass="error" /></span>
    </div>
    </div>
    <div class="col-lg-4">
    <div class="form-group">
    <label><spring:message code="user.label.confirmpassword"/><sup class="text-danger">*</sup></label> 
    <spring:message code="user.label.confirmpassword" var="confirmPassword"/>
	<s:password path="confirmPassword" id="confirmPassword" class="form-control" placeholder="${confirmPassword}" onblur="trimValue(this)"/>
    <span style="color:red;"><s:errors path="confirmPassword" cssClass="error" /></span>
    </div>
    </div>
    </div>
    <div class="row">
    <div class="col-lg-12 col-md-12 text-center">
    <button type="submit" id="submit-dis" class="btn btn-submit btn-md"><spring:message code="common.button.update"/></button>
    </div>
    </div>
    </div>
    </div>
    <div class="row">
    <div class="col-lg-12 col-md-12">
    <p style="font-size:12px"><span class="text-danger"><spring:message code="common.message.mandatorymessage"/></span></p>
    </div>
    </div>
    </div>
    </div>
    </div>
</s:form>    
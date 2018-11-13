<%@page import="in.otpl.dnb.util.URLConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<spring:url value="<%= URLConstants.USER_SAVE %>" var="userSave"/>
<spring:url value="<%= URLConstants.USER_LIST %>" var="userList"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/bootstrap-multiselect.css">
<script src="<%=request.getContextPath()%>/resources/js/bootstrap-multiselect.js"></script>
<script type="text/javascript">
function cancel(){
	window.location.href = "${userList}";
}

$(document).ready(function() {
	$("#userTypeId").multiselect({
		includeSelectAllOption : false,
		enableCaseInsensitiveFiltering: true,
		enableFiltering : true,
		buttonWidth : '100%',
		maxHeight : 300,
		dropRight : true
		});
	 $(".allowAlphaNumeric").keypress(function (e) {    
 		if (e.which != 8 && e.which != 0
					&& (e.which < 48 || e.which > 57)&& (e.which < 65 || e.which > 90)
					&& (e.which < 97 || e.which > 122)){
 			return false;
 		}
 	 });
});

function trimValue(field){
    var val = field.value;  
    field.value = trim(val);
}

function trim(val){
   return val.replace(/^\s+|\s+$/g,"")
}
function validateUser(){
	 var firstName = $("#firstName").val();
	 var lastName = $("#lastName").val();
	 var loginName = $("#loginName").val();
	 var password = $("#password").val();
	 var confirmPassword = $("#confirmPassword").val();
	 var employeeNumber = $("#employeeNumber").val();
	 var emailAddress = $("#emailAddress").val();
	 var mobileNumber = $("#ptn").val();
	 var designation = $("#designation").val();
	 var userTypeId = $("#userTypeId").val();
	 var regex = /^[a-zA-Z ]*$/;
	 var regexMobile = "^[1-9][0-9]+$";
	 var digit= new RegExp("^[0-9]+$|^[+][0-9]+$");
	 var alphaNumber = /^[A-Za-z0-9 ]+$/;
	 var spchars ="`()\\~^+\"|=<>;{},/.[]?'%&*";
	 var spcharsPincode ="`()\\~!@^&*+\"|:=<>#$%?;{}/.[]'-_ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"; 
	 var emailChar=emailAddress.charAt(0);
	 var spcharsMail ="`()\\~!@^&*+\"|:=<>#$%?;{}/.[]'-_0123456789";
	 var spcharsName ="`()\\~!@^&*+\"|:=<>#$%?;{}/.[]'-_0123456789";
	 var spcharsDesignation = "`\\~^\"|<>;{},/[]";
	 var spcharscode ="`()\\~!@^&*\"|:=<>#$%?;{}/.[]'_1234567890";
	 	if(firstName == null || firstName.trim() == ""){
    		$("#firstName").focus();
    		bootbox.alert('<spring:message code="user.errors.firstname.required"/>');
    		return false;
    	}
    	
	 for(var i=0;i<firstName.length;i++){
		 for(var j=0;j<spcharsName.length;j++){
			if(firstName.charAt(i) == spcharsName.charAt(j)){
	     		bootbox.alert('<spring:message code="user.errors.firstname.format"/>');
	     		$("#firstName").focus();
	     		return false;
	   		}
	   	}
	}
	 if(lastName == null || lastName.trim() == ""){
 		$("#lastName").focus();
 		bootbox.alert('<spring:message code="user.errors.lastname.required"/>');
 		return false;
 	}
	for(var i=0;i<lastName.length;i++){
		for(var j=0;j<spcharsName.length;j++){
			if(lastName.charAt(i) == spcharsName.charAt(j)){
	     		bootbox.alert('<spring:message code="user.errors.lastname.format"/>');
	     		$("#lastName").focus(); 
	     		return false;
	     	}
	     }
	}
	 if(userTypeId == 0){
		bootbox.alert('<spring:message code="user.errors.userTypeId.required"/>');
		return false;
	}
	
	if(emailAddress == null || emailAddress.trim() == ""){
		$("#emailAddress").focus();
		bootbox.alert('<spring:message code="user.errors.email.required"/>');
		return false;
	}
	if(emailAddress != null && emailAddress.trim() != ""){
		for (var j = 0; j < spcharsMail.length; j++) {
			if (emailChar == spcharsMail.charAt(j)) {
				bootbox.alert('<spring:message code="user.errors.email.format"/>');
				$("#emailAddress").focus();
				return false;
			}
		}
		
	 	var atpos = emailAddress.indexOf("@");
	    var dotpos = emailAddress.lastIndexOf(".");
	    if(emailAddress.length>0){
    if (atpos<1 || dotpos<atpos+2 || dotpos+2>=emailAddress.length) {
    	bootbox.alert('<spring:message code="user.errors.email.notvalid"/>');
    	$("#emailAddress").focus();
        return false;
    } 
	    }
	}
	if(loginName == null || loginName.trim() == ""){
		$("#loginName").focus();
		bootbox.alert('<spring:message code="user.errors.loginid.required"/>');
		return false;
	}
	if(loginName != null || loginName.trim() != ""){
		for(var i=0;i<loginName.length;i++){
			 for(var j=0;j<spchars.length;j++){
				if(loginName.charAt(i) == spchars.charAt(j)){
		     		bootbox.alert('<spring:message code="user.errors.loginid.format"/>');
		     		$("#loginName").focus();
		     		return false;
		   		}
		   	}
		}
	}
	
	if(employeeNumber == null || employeeNumber.trim() == ""){
		$("#employeeNumber").focus();
		bootbox.alert('<spring:message code="user.errors.employeenumber.required"/>');
	    $("#submit-dis").attr("disabled", false);
		return false;
	}
	if(employeeNumber != null && employeeNumber.trim() != ""){
		if(!employeeNumber.match(alphaNumber)){
			bootbox.alert('<spring:message code="user.errors.employeeNumber.format"/>');
			$("#ptn").focus();
			return false;
		}
	}
	if(password == null || password.trim() == ""){
		$("#password").focus();
		bootbox.alert('<spring:message code="user.errors.password.required"/>');
		return false;
	}
	if(password != null || password.trim() != ""){
	if(password.length < 6){
		bootbox.alert('<spring:message code="user.errors.password.minLength"/>');
		$("#password").focus();
		return false;
	}
	}
	if(confirmPassword == null || confirmPassword.trim() == ""){
		$("#confirmPassword").focus();
		bootbox.alert('<spring:message code="user.errors.confirmpassword.required"/>');
		return false;
	}
	
	if(password != confirmPassword){
		$("#confirmPassword").val("");
		bootbox.alert('<spring:message code="user.errors.confirmpassword.match"/>');
		$("#password").focus();
		return false;
	}
	
	if(mobileNumber == null || mobileNumber.trim() == ""){
		$("#ptn").focus();
		bootbox.alert('<spring:message code="user.errors.phonenumber.required"/>');
		return false;
	}
	if(mobileNumber != null && mobileNumber.trim() != ""){
		if(!mobileNumber.match(regexMobile)){
			bootbox.alert('<spring:message code="user.errors.onlydigitallowedphonenum"/>');
	 		return false;
		}
	if(mobileNumber.length < 8){
		bootbox.alert('<spring:message code="user.error.mobile.lessDigits"/>');
		$("#ptn").focus();
		return false;
	}		
	if(!digit.test(mobileNumber)){
		bootbox.alert('<spring:message code="user.errors.phoneNumber.format"/>');
		$("#ptn").focus();
		return false;
	}
	}
	
	if (designation == null || designation.trim() == "") {
		bootbox.alert('<spring:message code="user.errors.designation.required"/>');
        $("#designation").focus();
        return false;
	}
	if(designation != null && designation.trim() != ""){
		for(var i=0;i<designation.length;i++){
			 for(var j=0;j<spcharsDesignation.length;j++){
				if(designation.charAt(i) == spcharsDesignation.charAt(j)){
					bootbox.alert("<spring:message code='user.errors.designation.format'/>");
		     		$("#designation").focus();
		     		return false;
		   		}
		   	}
		}
	}
	
	if($("#imei").val() == ""){
		 bootbox.alert('<spring:message code="user.errors.imei.enter"/>');
	      $("#imei").focus();
	      return false;
	 }
	 if($("#imei").val() != null && $("#imei").val()!= ""){
		    if(!alphaNumber.test($("#imei").val())){
		    	bootbox.alert('<spring:message code="user.errors.imei.format"/>');
		        $("#imei").focus();
		        return false;
		    }
		    if($("#imei").val().length < 15){
		    	bootbox.alert('<spring:message code="user.errors.imei.minLength"/>');
		        $("#imei").focus();
		        return false;
		    }
		    }
}
</script>
 <div class="container-fluid">
<div class="row">
    <div class="col-lg-12 col-md-12 text-center">
     <div class="row">
    <c:forEach items="${succMessage}" var="var">
     <div class="alert alert-success" role="alert">
    <font color="green">${var}</font><br>
     </div>
    </c:forEach>
   </div>
    </div>
</div>
<div class="row">
    <div class="col-lg-12 col-md-12 text-center">
     <div class="row">
    <c:forEach items="${failureMsg}" var="var">
     <div class="alert alert-success errorMessage" role="alert">
    <font color="red">${var}</font><br>
     </div>
    </c:forEach>
   </div>
    </div>
</div>
</div>
    <s:form action="${userSave}" method="post" commandName="userForm" onsubmit="return validateUser();" >
     <div class="container-fluid">
   	<div class="panel panel-default">
   	<div class="panel-heading">User Create
 	</div>
  	<div class="panel-body" style="padding-top:0">
    <div class="row">
    <div class="col-lg-12 col-md-12">
    <div id="first" style="padding-top:15px;">
    <div class="row">
    <div class="col-lg-3 col-md-3">
	<div class="form-group">
    <label><spring:message code="user.label.firstname"/><sup class="text-danger">*</sup></label>
    <spring:message code="user.label.firstname" var="fname"/>
    <s:input path="firstName" maxlength="50" id="firstName" class="form-control" placeholder="${fname}" onblur="trimValue(this)"/>
    <span style="color:red;"><s:errors path="firstName" cssClass="error" /></span>
    </div>
    </div>
    <div class="col-lg-3 col-md-3">
	<div class="form-group">
    <label><spring:message code="user.label.lastname"/><sup class="text-danger">*</sup></label>
    <spring:message code="user.label.lastname" var="lname"/>
    <s:input path="lastName" maxlength="50" id="lastName" class="form-control" placeholder="${lname}" onblur="trimValue(this)"/>
     <span style="color:red;"><s:errors path="lastName" cssClass="error" /></span>
    </div>
    </div>
    <div class="col-lg-3 col-md-3">
    <div class="form-group">
    <label><spring:message code="user.label.usertype"/><sup class="text-danger">*</sup></label> 
 	<s:select path="userTypeId" id="userTypeId" class="form-control">
 	<s:option selected="selected" value="0"> <spring:message code="user.label.select" /></s:option>
 	<c:forEach items="${userTypes}" var="usertype">
    <s:option value="${usertype.id}">${usertype.description}</s:option>
    </c:forEach>
    </s:select>
    </div>
    </div>
    <div class="col-lg-3 col-md-3">
    <div class="form-group">
    <label><spring:message code="user.label.emailid"/><sup class="text-danger">*</sup></label> 
    <spring:message code="user.label.emailid" var="emailId"/>
	<s:input path="emailAddress" maxlength="50" id="emailAddress" class="form-control" placeholder="${emailId}" onblur="trimValue(this)"/>
	<span style="color:red;"><s:errors path="emailAddress" cssClass="error" /></span>
	<span style="color:red;"><c:if test="${not empty emailMsg}">
    ${emailMsg}
    </c:if></span>
    </div>
    </div>
    </div>
    <div class="row">
    <div class="col-lg-3 col-md-3">
	<div class="form-group">
    <label><spring:message code="user.label.loginid"/><sup class="text-danger">*</sup></label>
    <spring:message code="user.label.loginid" var="loginName"/>
    <s:input path="loginName" maxlength="50" id="loginName" class="form-control" placeholder="${loginName}" onblur="trimValue(this)"/>
    <span style="color:red;"><s:errors path="loginName" cssClass="error" /></span>
    <span style="color:red;"><c:if test="${not empty loginNameMsg}">
      ${loginNameMsg}
    </c:if></span>
    </div>
    </div>
    <div class="col-lg-3 col-md-3">
	<div class="form-group">
    <label><spring:message code="user.label.employeenumber"/><sup class="text-danger">*</sup></label>
    <spring:message code="user.label.employeenumber" var="employeeNumber"/>
    <s:input path="employeeNumber" maxlength="50"  id="employeeNumber" class="form-control allowAlphaNumeric" placeholder="${employeeNumber}" onblur="trimValue(this)"/>
    <span style="color:red;"><s:errors path="employeeNumber" cssClass="error" /></span>
    <span style="color:red;"><c:if test="${not empty empNoMsg}">
     ${empNoMsg}
    </c:if></span>
    </div>
    </div>
    <div class="col-lg-3 col-md-3">
    <div class="form-group">
    <label><spring:message code="user.label.password"/><sup class="text-danger">*</sup></label> 
    <spring:message code="user.label.password" var="password"/>
	<s:password path="password" maxlength="25" id="password" class="form-control" placeholder="${password}" onblur="trimValue(this)"/>
	<span style="color:red;"><s:errors path="password" cssClass="error" /></span>
    </div>
    </div>
    <div class="col-lg-3 col-md-3">
    <div class="form-group">
    <label><spring:message code="user.label.confirmpassword"/><sup class="text-danger">*</sup></label> 
    <spring:message code="user.label.confirmpassword" var="confirmPassword"/>
	<s:password path="confirmPassword" maxlength="25" id="confirmPassword" class="form-control" placeholder="${confirmPassword}" onblur="trimValue(this)"/>
    <span style="color:red;"><s:errors path="confirmPassword" cssClass="error" /></span>
    </div>
    </div>
    </div>
    <div class="row">
    <div class="col-lg-3 col-md-3">
    <div class="form-group">
    <label><spring:message code="user.label.phone"/><sup class="text-danger">*</sup></label> 
    <spring:message code="user.label.phone" var="mobileNumber"/>
	<s:input path="ptn" id="ptn" maxlength="16"  class="form-control allowNumber" placeholder="${mobileNumber}"  onblur="return allownumeric();"/>
	<span style="color:red;"><s:errors path="ptn" cssClass="error" /></span>
	<span style="color:red;"><c:if test="${not empty phoneMsg}">
     ${phoneMsg}
    </c:if>
    </span>
    </div>
    </div>
    <div class="col-lg-3 col-md-3">
    <div class="form-group">
    <label><spring:message code="user.label.IMEI.number"/><sup class="text-danger">*</sup></label> 
    <spring:message code="user.label.IMEI.number" var="imeiNumber"/>
	<s:input path="imei" id="imei" maxlength="200" class="form-control" placeholder="${imeiNumber}" onblur="trimValue(this)"/>
	<span style="color:red;"><s:errors path="imei" cssClass="error" /></span>
	<span style="color:red;"><c:if test="${not empty imeiMsg}">
    ${imeiMsg}
    </c:if></span>
    </div>
    </div>
    <div class="col-lg-3 col-md-3">
	<div class="form-group">
    <label><spring:message code="user.label.designation"/><sup class="text-danger">*</sup></label>
    <spring:message code="user.label.designation" var="designation"/>
    <s:input path="designation" maxlength="50"  id="designation" class="form-control" placeholder="${designation}" onblur="trimValue(this)"/>
    <span style="color:red;"><s:errors path="designation" cssClass="error" /></span>
    </div>
    </div>
    </div>
    <div class="row">
    <div class="col-lg-12 col-md-12 text-center">
    <button type="submit" id="submit-dis" class="btn btn-submit btn-md" style="border-radius:2px;"><spring:message code="common.button.create"/></button>
    <span>
    <a class="btn btn-danger btn-md" href="javascript:cancel();" style="border-radius:2px;"><spring:message code="common.button.cancel"/></a></span>
    </div>
    </div>
    </div>
    </div>
    </div>
    </div>

    <div class="row mandatory">
    <div class="col-lg-12 col-md-12">
    <p style="font-size:12px"><span class="text-danger"><spring:message code="common.message.mandatorymessage"/></span></p>
    </div>
    </div>
    </div>
    </div>
</s:form>    
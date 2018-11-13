<%@page import="in.otpl.dnb.util.ServerConstants"%>
<%@page import="in.otpl.dnb.util.URLConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<spring:url value="<%= URLConstants.USER_CREATE %>" var="createUser"/>
<spring:url value="<%= URLConstants.USER_EDIT %>" var="editUser"/>
<spring:url value="<%= URLConstants.USER_LIST %>" var="userList"/>
<spring:url value="<%= URLConstants.USER_STATUS %>" var="statusUrl"/>
<spring:url value="<%= URLConstants.USER_UPLOAD %>" var="userUpload"/>

<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/bootstrap-multiselect.css">
<script src="<%=request.getContextPath()%>/resources/js/bootstrap-multiselect.js"></script>
<script type="text/javascript">
function createUser(){
	window.location.href = "${createUser}";
}
function editUser(userId){
	window.location.href = "${editUser}"+userId;
}
function uploaduser(){
	window.location.href = "${userUpload}";
}
function trimValue(field){
    var val = field.value;  
    field.value = trim(val);
}

function trim(val){
   return val.replace(/^\s+|\s+$/g,"")
} 
function changeStatus(id,status,count) {
	var statusActive = '<spring:message code="common.button.activate"/>';
	var statusInactive = '<spring:message code="common.button.deactivate"/>';
	 $.post("${statusUrl}",{id:id, status:status}, function(response){
		if(response != null && response != ""){
	if(response == "1"){
   		$('#stat'+count).html("<li class='fa fa-star fa-lg' aria-hidden='true' style='cursor:pointer' data-toggle='tooltip' data-original-title='"+statusActive+"' title='' onclick='return changeStatus("+id+",1,"+count+");'></i>");
   	 }else{
   		$('#stat'+count).html("<i class='fa fa-star-o fa-lg' aria-hidden='true' style='cursor:pointer' data-toggle='tooltip' data-original-title='"+statusInactive+"' title='' onclick='return changeStatus("+id+",0,"+count+");'></i>");
   	 }
		}
	 });
	 return false;
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
	
		$("#teamId").multiselect({
			includeSelectAllOption : false,
			enableCaseInsensitiveFiltering: true,
			enableFiltering : true,
			buttonWidth : '100%',
			maxHeight : 300,
			dropRight : true
			});
	
	$("#rowcount").multiselect({
		includeSelectAllOption : false,
		enableFiltering : false,
		buttonWidth : '100%',
		maxHeight : 300,
		dropRight : true
		});
});

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
<s:form action="${userList}" method="post" commandName="userForm">
    <div class="container-fluid">
   	<div class="panel panel-default">
   	<div class="panel-heading">User List
   <span class="pull-right" style="color:#333;margin-top:-4px">
       <a class="btn btn-success" onclick="createUser();"><span class="fa fa-user-plus fa-lg" data-toggle="tooltip" title="" data-original-title="<spring:message code="common.button.create"/>"></span></a>
       <a class="btn btn-warning" onclick="uploaduser();"><span class="fa fa-upload fa-lg" data-toggle="tooltip" title="" data-original-title="<spring:message code="common.button.upload"/>"></span></a>
    </span>
  </div>
  	<div class="panel-body" style="padding-top:0">
    <div class="row">
    <div class="col-lg-12" style="padding-top:15px;">
    <div class="row">
    <div class="col-lg-3">
	<div class="form-group">
    <label><spring:message code="user.label.firstname"/></label>
    <spring:message code="user.placeholder.firstname" var="fname"/>
    <s:input path="firstName" id="firstName" class="form-control" placeholder="${fname}" onblur="trimValue(this);"/>
    </div>
    </div>
    <div class="col-lg-3">
	<div class="form-group">
    <label><spring:message code="user.label.lastname"/></label>
    <spring:message code="user.placeholder.lastname" var="lname"/>
    <s:input path="lastName" id="lastName" class="form-control" placeholder="${lname}" onblur="trimValue(this)"/>
    </div>
    </div>
     <div class="col-lg-3">
    <label><spring:message code="common.label.status"/></label>
    <div class="form-group">
					<label class="radio-inline" style="display: inline-block"><s:radiobutton path="status" value="2" checked="true" /><spring:message code="common.label.all"/></label>
					<label class="radio-inline" style="display: inline-block"><s:radiobutton path="status" value="1" /><spring:message code="common.button.activate"/></label>
					<label class="radio-inline" style="display: inline-block"><s:radiobutton path="status" value="0" /><spring:message code="common.button.deactivate"/></label>
				</div>
    </div>
    <div class="col-lg-3">
    <div class="form-group">
    <label><spring:message code="user.label.usertype"/><sup class="text-danger">*</sup></label> 
 	<s:select path="userTypeId" id="userTypeId" class="form-control">
 	<s:option value="0"> <spring:message code="common.label.all" /></s:option>
 	<c:forEach items="${userTypes}" var="usertype">
    <s:option value="${usertype.id}">${usertype.description}</s:option>
    </c:forEach>
    </s:select>
    </div>
    </div>
    </div>
    
    
    <div class="row">
    <div class="col-lg-3">
	<div class="form-group">
    <label><spring:message code="user.label.loginname"/></label>
    <spring:message code="user.placeholder.loginname" var="userId"/>
    <s:input path="loginName" id="loginName" class="form-control" placeholder="${userId}" onblur="trimValue(this)"/>
    </div>
    </div>
    <div class="col-lg-3">
	<div class="form-group">
    <label><spring:message code="user.label.employeenumber"/></label>
    <spring:message code="user.placeholder.employeenumber" var="employeeNumber"/>
    <s:input path="employeeNumber" id="employeeNumber" class="form-control" placeholder="${employeeNumber}" onblur="trimValue(this)"/>
    </div>
    </div>
     <div class="col-lg-3">
    <div class="form-group">
    <label><spring:message code="user.label.emailid"/></label> 
    <spring:message code="user.placeholder.emailid" var="emailId"/>
	<s:input path="emailAddress" id="emailAddress" class="form-control" placeholder="${emailId}" onblur="trimValue(this)"/>
    </div>
    </div>
    <div class="col-lg-3">
    <div class="form-group">
    <label><spring:message code="user.label.team"/></label> 
	<s:select path="teamId" id="teamId" class="form-control">
    <option selected="selected" value="0"> <spring:message code="common.label.all" /></option>
    <c:forEach items="${teamList}" var="data" >
    <s:option value="${data.id}">${data.name}</s:option>
    </c:forEach></s:select>
    <s:hidden path="teamId"/>
    </div>
    </div>
    </div>
     <div class="row">
    <div class="col-lg-3">
    <div class="form-group">
    <label><spring:message code="user.label.phoneversion"/></label> 
    <spring:message code="user.placeholder.phoneversion" var="phoneversion"/>
	<s:input path="phoneAppVersion" id="phoneAppVersion" class="form-control" placeholder="${phoneversion}" onblur="trimValue(this)"/>
    </div>
    </div>
    <div class="col-lg-3">
    <div class="form-group">
    <label><spring:message code="user.label.phone"/></label> 
    <spring:message code="user.placeholder.phone" var="mobileNumber"/>
	<s:input path="ptn" id="ptn" class="form-control" placeholder="${mobileNumber}" onblur="trimValue(this)"/>
    </div>
    </div>
     <div class="col-lg-3">
    <div class="form-group">
    <label><spring:message code="user.label.IMEI.number"/></label> 
    <spring:message code="user.label.IMEI.number" var="imeiNumber"/>
	<s:input path="imei" id="imei" maxlength="50numeric" class="form-control" placeholder="${imeiNumber}" onblur="trimValue(this)"/>
    </div>
    </div>
    <div class="col-lg-3">
	<div class="form-group">
    <label><spring:message code="user.label.designation"/></label>
    <spring:message code="user.placeholder.designation" var="designation"/>
    <s:input path="designation" id="designation" class="form-control" placeholder="${designation}" onblur="trimValue(this)"/>
    </div>
    </div>
    </div>
    <div class="row">
     <div class="col-lg-3">
    <div class="form-group">
    <label><spring:message code="user.label.itemsPerPage"></spring:message></label>
	<s:select path="rowcount" id="rowcount" cssClass="form-control pageCount">
	<s:options items="${itemsPerPageList}" />
	</s:select>
	</div>
    </div>
    </div>
    </div>
    <div class="row">
    <div class="form-group">  
    <div class="col-lg-12 col-md-12 text-center" style="margin-top:15px;">
    <button type="submit" class="btn btn-submit btn-md"><spring:message code="common.button.show"/></button>
    </div>
    </div>
    </div>
    </div>
    </div>
    <div class="row mandatory">
    <div class="col-lg-12 col-md-12">
    <p style="font-size:12px">
    <span class="text-danger"><spring:message code="common.message.mandatorymessage"/></span>
    </p>
    </div>
    </div>
    </div>
    <c:if test="${data != null}">
    <div class="panel panel-default">
  <div class="panel-heading">
  <div class="row">
    <div class="col-lg-12 col-md-12 text-right">
    				<c:if test="${not empty data}">
						<jsp:include page="/views/common/pagination.jsp">
							<jsp:param name="totalRows" value="${totalRows}" />
							<jsp:param name="currentPage" value="${currentPage}" />
							<jsp:param name="actionName" value="<%= URLConstants.USER_LISTS %>" />
							<jsp:param name="rowcount" value="${rowcount}" />
							<jsp:param name="excelAvailable" value="<%= URLConstants.USER_DOWNLOAD_EXCEL %>" />
							<jsp:param name="pdfAvailable" value="<%= URLConstants.USER_DOWNLOAD_PDF %>" />
							<jsp:param name="params" value="firstName,lastName,status,userTypeId,loginName,employeeNumber,emailAddress,teamId,phoneAppVersion,ptn,imei,designation" />
							<jsp:param name="firstName" value="${userForm.firstName}" />
							<jsp:param name="lastName" value="${userForm.lastName}" />
							<jsp:param name="status" value="${userForm.status}" />
							<jsp:param name="userTypeId" value="${userForm.userTypeId}" />
							<jsp:param name="loginName" value="${userForm.loginName}" />
							<jsp:param name="employeeNumber" value="${userForm.employeeNumber}" />
							<jsp:param name="emailAddress" value="${userForm.emailAddress}" />
							<jsp:param name="teamId" value="${userForm.teamId}" />
							<jsp:param name="phoneAppVersion" value="${userForm.phoneAppVersion}" />
							<jsp:param name="ptn" value="${userForm.ptn}" />
							<jsp:param name="imei" value="${userForm.imei}" />
							<jsp:param name="designation" value="${userForm.designation}" />
						</jsp:include>
				</c:if>
    </div>
    </div>
  </div>
  <div class="panel-body" style="padding:0;overflow-x:scroll">
        <table class="table">
    <thead>
   <tr style="background:#004e6a;color:#fff;">
    <th><spring:message code="common.label.actions"/></th>
    <th><spring:message code="user.label.name"/></th>
    <th><spring:message code="user.label.loginid"/></th>
    <th><spring:message code="user.label.usertype"/></th>
    <th><spring:message code="user.label.team"/></th>
    <th><spring:message code="user.label.employeenumber"/></th>
    <th><spring:message code="user.label.emailid"/></th>
    <th><spring:message code="user.label.phone"/></th>
    <th><spring:message code="user.label.designation"/></th>
    <th><spring:message code="user.label.IMEI.number"/></th>
    <th><spring:message code="user.label.phoneversion"/></th>
    <th><spring:message code="user.label.phonemodal"/></th>
    <th><spring:message code="user.label.defupdatedatetime"/></th>
    </tr>
    </thead>
    <c:if test="${not empty data}">
    <% int count = 0; %>
	<c:forEach items="${data}" var="listdata"><% count++; %>
    <tbody>
    <tr>
   <td>
	<span id="stat<%=count%>">
	<c:choose>
	<c:when test="${listdata.status == 1}">
	<i class="fa fa-star fa-lg" aria-hidden="true" style="cursor:pointer" data-toggle="tooltip" data-original-title="<spring:message code="common.label.active"/>" title='Active' onclick="return changeStatus(${listdata.id},${listdata.status},<%=count%>);"></i>
	</c:when>
	<c:otherwise>
	<i class="fa fa-star-o fa-lg" aria-hidden="true" style="cursor:pointer" data-toggle="tooltip" data-original-title="<spring:message code="common.label.inactive"/>" title='Inactive' onclick="return changeStatus(${listdata.id},${listdata.status},<%=count%>);"></i>
	</c:otherwise>
	</c:choose>
	</span>
	<i class="fa fa-pencil fa-lg" aria-hidden="true" style="cursor:pointer" data-toggle="tooltip" data-original-title="<spring:message code="user.label.edit"/>" title='Edit' onclick="editUser(${listdata.id});"></i>
	</td>
   
    <td>${listdata.name}</td>
    <td>${listdata.loginName}</td>
    <td>${listdata.userType}</td>
    <td>${listdata.teamName}</td>
    <td>${listdata.employeeNumber}</td>
    <td>${listdata.emailAddress}</td>
    <td>${listdata.ptn}</td>
    <td>${listdata.designation}</td>
    <td>${listdata.imei}</td>
    <td>${listdata.phoneAppVersion}</td>
    <td>${listdata.handsetDetails}</td>
   <td>${listdata.appVersionUpdateTime}</td>
    </tr>
    </tbody>
    </c:forEach>
    </c:if>
    <c:if test="${empty data}">
    <tbody>
    <tr>
	<td colspan="13" align="center"><spring:message code="common.message.nodatafound"></spring:message></td>
	</tr>
    </tbody>
    </c:if>
    </table>
  </div>
   <div class="panel-footer">
    <div class="row">
    <div class="col-lg-12 col-md-12 text-right">
   				<c:if test="${not empty data}">
						<jsp:include page="/views/common/pagination.jsp">
							<jsp:param name="totalRows" value="${totalRecords}" />
							<jsp:param name="currentPage" value="${currentPage}" />
							<jsp:param name="actionName" value="<%= URLConstants.USER_LISTS %>" />
							<jsp:param name="rowcount" value="${rowcount}" />
							<jsp:param name="excelAvailable" value="<%= URLConstants.USER_DOWNLOAD_EXCEL %>" />
							<jsp:param name="pdfAvailable" value="<%= URLConstants.USER_DOWNLOAD_PDF %>" />
							<jsp:param name="params" value="firstName,lastName,status,userTypeId,loginName,employeeNumber,emailAddress,teamId,phoneAppVersion,ptn,imei,designation" />
							<jsp:param name="firstName" value="${userForm.firstName}" />
							<jsp:param name="lastName" value="${userForm.lastName}" />
							<jsp:param name="status" value="${userForm.status}" />
							<jsp:param name="userTypeId" value="${userForm.userTypeId}" />
							<jsp:param name="loginName" value="${userForm.loginName}" />
							<jsp:param name="employeeNumber" value="${userForm.employeeNumber}" />
							<jsp:param name="emailAddress" value="${userForm.emailAddress}" />
							<jsp:param name="teamId" value="${userForm.teamId}" />
							<jsp:param name="phoneAppVersion" value="${userForm.phoneAppVersion}" />
							<jsp:param name="ptn" value="${userForm.ptn}" />
							<jsp:param name="imei" value="${userForm.imei}" />
							<jsp:param name="designation" value="${userForm.designation}" />
						</jsp:include>
				</c:if>
				</div>
				</div>
   </div>
</div>
   </c:if>
   </div>
</s:form>
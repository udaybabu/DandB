<%@page import="in.otpl.dnb.util.URLConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<spring:url value="<%= URLConstants.USER_UPLOAD_FILE %>" var="uploadUser"/>
<spring:url value="<%= URLConstants.USER_LIST %>" var="userShow"/>
<spring:url value="<%= URLConstants.USER_DOWNLOAD_SAMPLE %>" var="downloadUserSample"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/fileUpload.css">

<script type="text/javascript">
function cancel(){
	window.location.href = "${userShow}";
}

function validate_form() {
	if ($("#file").val() == '') {
		bootbox.alert('<spring:message code="user.file.message.selectFile"/>');		
	  return false;
	}
	if($("#file").val()!=''){
	    var validExts = new Array(".xlsx");
	    var fileExt = $("#file").val()
	    fileExt = fileExt.substring(fileExt.lastIndexOf('.'));
	    if (validExts.indexOf(fileExt) < 0) {
	    	bootbox.alert("Invalid file selected, valid files are of " +
	               validExts.toString() + " types.");
	      return false;
	       }
		}
}

function downloadUserSample(){
	window.location.href = "${downloadUserSample}";
}
</script>
    <s:form action="${uploadUser}" method="post" enctype="multipart/form-data" commandName="userForm" onsubmit="return validate_form();" >
     <div class="container-fluid">
   	<div class="panel panel-default">
   		<div class="panel-heading">Upload User
 	</div>
  	<div class="panel-body">
    <div class="row">
    <div class="col-lg-4 col-md-4 col-sm-12  col-lg-offset-2 col-md-offset-2">
    <input type="file" name="file" id="file" class="inputfile inputfile-6"  />
	<label for="file"><span></span> 
	<strong><svg xmlns="http://www.w3.org/2000/svg" width="20" height="17" viewBox="0 0 20 17">
	<path d="M10 0l-5.2 4.9h3.3v5.1h3.8v-5.1h3.3l-5.2-4.9zm9.3 11.5l-3.2-2.1h-2l3.4 2.6h-3.5c-.1 0-.2.1-.2.1l-.8 2.3h-6l-.8-2.2c-.1-.1-.1-.2-.2-.2h-3.6l3.4-2.6h-2l-3.2 2.1c-.4.3-.7 1-.6 1.5l.6 3.1c.1.5.7.9 1.2.9h16.3c.6 0 1.1-.4 1.3-.9l.6-3.1c.1-.5-.2-1.2-.7-1.5z"/></svg> 
	Choose a file&hellip;</strong></label>
    
    
		<%-- <label for="file"><spring:message code="user.label.file"/><sup class="text-danger">*</sup></label> --%>
	</div>
    <div class="col-lg-4 col-md-4 col-sm-12">
	<div class="form-group">
	<a style="cursor:pointer;" class="btn btn-default" onclick="downloadUserSample();"><span class="fa fa-download"></span> <spring:message code="user.label.downloadSample" /></a>
    </div>
    </div>
    </div>
    
    <div class="row" style="padding-top: 20px;">
    <div class="col-lg-12 col-md-12 text-center">
    <button type="submit" id="submit-dis" class="btn btn-submit btn-md" style="border-radius:2px;"><spring:message code="common.button.upload"/></button>
    <span style="padding-left:10px;">
    <a class="btn btn-danger btn-md" style="border-radius:2px;" href="javascript:cancel();"><spring:message code="common.button.cancel"/></a></span>
    </div>
    </div>
    <c:if test="${not empty userNameMsg}">
	<div class="row alert alert-danger" style="margin-bottom:0;">
    <div class="col-lg-12 col-md-12 text-center">
    <c:forEach items="${userNameMsg}" var="var">
    <font color="red">${var}</font><br>
    </c:forEach>
    </div>
    </div>
    </c:if>
    
   
    <c:forEach items="${succMessage}" var="var">
    <div class="row alert alert-success" style="margin-bottom:0;">
    <div class="col-lg-12 col-md-12 text-center">
    <font color="green">${var}</font><br>
    </div>
	</div>
    </c:forEach>
   
    <div class="row mandatory">
    <div class="col-lg-12 col-md-12">
    <p style="font-size:12px"><span class="text-danger"><spring:message code="common.message.mandatorymessage"/></span><br>
    <span class="text-danger"><spring:message code="common.label.note"/></span>: <span><spring:message code="user.label.uploadNote"/></span></p>
    </div>
    </div>
    </div>
    </div>
    </div>
 </s:form>
 <script src="<%=request.getContextPath()%>/resources/js/fileUpload.js"></script>
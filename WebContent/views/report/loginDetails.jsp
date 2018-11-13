<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="in.otpl.dnb.util.URLConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<spring:url value="<%=URLConstants.REPORT_LOGIN_LIST%>" var="showUrl" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/bootstrap-multiselect.css">
<script src="<%=request.getContextPath()%>/resources/js/bootstrap-multiselect.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	$("#typeData").multiselect({
		includeSelectAllOption : false,
		enableFiltering : false,
		enableCaseInsensitiveFiltering: true,
		buttonWidth : '100%',
		maxHeight : 300,
		dropRight : true
		});
	$("#rowcount").multiselect({
		includeSelectAllOption : false,
		enableFiltering : false,
		enableCaseInsensitiveFiltering: true,
		buttonWidth : '100%',
		maxHeight : 300,
		dropRight : true
		});
});
</script>

<s:form action="${showUrl}" method="post" commandName="loginDetailsForm" id="loginDetailsForm">
<div class="container-fluid">
			<div class="panel panel-default">
			 <div class="panel-heading">Log In Details</div>
				<div class="panel-body">
				<div class="row">
				<div class="col-lg-4 col-md-4 col-lg-offset-2 col-md-offset-2">
				<div class="form-group">
<label><spring:message code="login.label.type" /></label>
<s:select path="typeData" cssClass="form-control" id="typeData">
<s:option value="1"><spring:message code="login.details.logged" /></s:option>
<s:option value="2"><spring:message code="login.details.notlogged" /></s:option>
<s:option value="3"><spring:message code="login.details.html" /></s:option>
<s:option value="4"><spring:message code="login.details.mobile" /></s:option>
</s:select>
</div>
</div>
<div class="col-lg-4 col-md-4">
<div class="form-group">
<label><spring:message code="login.label.itemPerPage" /></label>
<s:select path="rowcount" cssClass="form-control" id="rowcount"> 
<s:options items="${itemsPerPageList}" />
</s:select>
</div>
</div>
</div>
<div class="row">
<div class="col-lg-12 col-md-12 text-center">
<button type="submit" id="submit" class="btn btn-submit btn-lg"><spring:message code="login.label.show" /></button>
</div>
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
							<jsp:param name="actionName" value="<%= URLConstants.REPORT_LOGIN_LISTS %>" />
							<jsp:param name="rowcount" value="${rowcount}" />
							<jsp:param name="excelAvailable" value="<%= URLConstants.REPORT_LOGIN_DOWNLOAD_EXCEL %>" />
							<jsp:param name="pdfAvailable" value="<%= URLConstants.REPORT_LOGIN_DOWNLOAD_PDF %>" />
							<jsp:param name="params" value="typeData" />
							<jsp:param name="typeData" value="${loginDetailsForm.typeData}" />
						</jsp:include>
				</c:if>
                </div>
                </div>
			    </div>
			 <div class="panel-body" style="padding:0;">
			      <table class="table">
    <thead>
    <tr style="background:#004e6a;color:#fff;">
    <th><spring:message code="login.details.name"/></th>
    <th><spring:message code="login.details.team"/></th>
    <c:if test="${action != '2'}">
    <th><spring:message code="login.details.date"/></th>
    <th><spring:message code="login.details.days"/></th>
    </c:if>
    </tr>
    </thead>
    <tbody>
    <c:if test="${not empty data}">
	<c:forEach items="${data}" var="listdata">
    <tr>
    <td>${listdata.userName}</td>
    <td>${listdata.teamName}</td>
    <c:if test="${action != '2'}">
    <td>${listdata.modificationTime}</td>
    <td>${listdata.noDays}</td>
    </c:if>
    </tr>
    </c:forEach>
    </c:if>
    </tbody>
    <c:if test="${empty data}">
    <tbody>
    <tr>
    <c:if test="${action == '2'}">
	<td colspan="2" align="center"><spring:message code="common.message.nodatafound"></spring:message></td>
	</c:if>
	<c:if test="${action != '2'}">
	<td colspan="4" align="center"><spring:message code="common.message.nodatafound"></spring:message></td>
	</c:if>
	</tr>
    </tbody>
    </c:if>
    </table>
	</div>
	<c:if test="${not empty data}">
			 <div class="panel-footer">
			 <div class="row">
             <div class="col-lg-12 col-md-12 text-right">
     					<jsp:include page="/views/common/pagination.jsp">
							<jsp:param name="totalRows" value="${totalRows}" />
							<jsp:param name="currentPage" value="${currentPage}" />
							<jsp:param name="actionName" value="<%= URLConstants.REPORT_LOGIN_LISTS %>" />
							<jsp:param name="rowcount" value="${rowcount}" />
							<jsp:param name="excelAvailable" value="<%= URLConstants.REPORT_LOGIN_DOWNLOAD_EXCEL %>" />
							<jsp:param name="pdfAvailable" value="<%= URLConstants.REPORT_LOGIN_DOWNLOAD_PDF %>" />
							<jsp:param name="params" value="typeData" />
							<jsp:param name="typeData" value="${loginDetailsForm.typeData}" />
						</jsp:include>
	               </div>
                </div>
			 </div>
				</c:if>
    		 </div>
   </c:if>
   </div>
</s:form>
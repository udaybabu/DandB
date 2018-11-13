<%@page import="in.otpl.dnb.util.ServerConstants"%>
<%@page import="in.otpl.dnb.util.URLConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<spring:url value="<%=URLConstants.TEAM_CREATE%>" var="createUrl" />
<spring:url value="<%=URLConstants.TEAM_EDIT%>" var="editUrl" />
<spring:url value="<%=URLConstants.TEAM_LIST%>" var="listUrlShow" />
<spring:url value="<%=URLConstants.TEAM_STATUS%>" var="statusUrl" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/bootstrap-multiselect.css">
<script
	src="<%=request.getContextPath()%>/resources/js/bootstrap-multiselect.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#rowcount").multiselect({
		includeSelectAllOption : false,
		enableFiltering : false,
		enableCaseInsensitiveFiltering: false,
		buttonWidth : '100%',
		maxHeight : 300,
		dropRight : true
		});
	
}); 
function teamCreate(){
	window.location.href = "${createUrl}";	
}

function teamEdit(id){
	window.location.href = "${editUrl}"+ "/" +id;
}

var sel;
var get;
function changeStatus(el,id,status,name,count) {
	 var hidEdit = $(el).parent().next();
	 var statusActive = '<spring:message code="customer.label.active"/>';
	 var statusInactive = '<spring:message code="customer.label.inactive"/>';
	  $.post("${statusUrl}",{name:name,id:id,status:status},function(response) {
	  var $this = $(this);
	  if(response != null && response != ""){
	     if(response == "1"){
	     $('#stat'+count).html("<i class='fa fa-star fa-lg newStar' aria-hidden='true' style='cursor:pointer' data-toggle='tooltip' data-original-title='"+statusActive+"' title='' onclick='return changeStatus(this,"+id+",1,\""+name+"\","+count+");'></i>");
	     hidEdit.show();
	     }else{
	     $('#active'+count ).find('p').hide();
	     $('#stat'+count).html("<i class='fa fa-star-o fa-lg newStar' aria-hidden='true' style='cursor:pointer' data-toggle='tooltip' data-original-title='"+statusInactive+"' title='' onclick='return changeStatus(this,"+id+",0,\""+name+"\","+count+");'></i>");
	     hidEdit.hide();
	     }
	  }
	  });
	  return false;
	  }
	  
	  $(document).ready(function(){ 		
		 $(".newStar").each(function(){
			 var star =  $(this).attr("data-original-title");
			 if(star == "Inactive"){
					$(this).parent().next().hide();
				}
				else{
					$(this).parent().next().show();
				}
		        });
	  });
	  
</script>
<div class="container-fluid">
	<div class="panel panel-default">
		<div class="panel-heading">
			<span class="heading"><spring:message
					code="team.label.listheader" /></span> <span class="pull-right"
				style="color: #333; margin-top: -4px"> <a
				class="btn btn-success" onclick="return teamCreate();"><span
					class="glyphicon glyphicon-plus fa-4px" data-toggle="tooltip"
					title=""
					data-original-title="<spring:message code="common.button.create"/>"></span></a>
			</span>
			<div class="row">
				<div class="col-lg-12 col-md-12 text-center">

					<c:forEach items="${succMessage}" var="var">
						<div class="alert alert-success" role="alert">
							<font color="green">${var}</font><br>
						</div>
					</c:forEach>

					<c:forEach items="${errMessage}" var="var">
						<div class="alert alert-danger" role="alert">
							<font color="red">${var}</font><br>
						</div>
					</c:forEach>

				</div>
			</div>
		</div>
		<s:form action="${listUrlShow}" method="post" commandName="teamForm"
			id="teamForm" style="margin-bottom:0px;">
			<div class="panel-body">
				<div class="row">
					<div class="col-lg-3">
						<div class="form-group">
							<label><spring:message code="team.label.teamName" /></label>
							<spring:message code="team.placeholder.teamName" var="tName" />
							<s:input path="name" id="name" maxlength="50"
								cssClass="text_field required form-control"
								placeholder="${tName}" />
						</div>
					</div>
					<div class="col-lg-3">
						<div class="form-group">
							<label><spring:message code="team.label.teamLead" /></label>
							<spring:message code="team.placeholder.teamLead" var="lName" />
							<s:input path="leadName" id="leadName" maxlength="50"
								cssClass="text_field required form-control"
								placeholder="${lName}" />
						</div>
					</div>
					<div class="col-lg-3">
						<div class="form-group">
							<label><spring:message code="team.label.teamMember" /></label>
							<spring:message code="team.placeholder.teamMember" var="tMem" />
							<s:input path="memberName" id="memberName" maxlength="50"
								cssClass="text_field required form-control"
								placeholder="${tMem}" />
						</div>
					</div>
					<div class="col-lg-3">
						<div class="form-group">
							<label><spring:message code="team.label.itemsperpage" /></label>
							<spring:message code="team.title.itemsperpage" var="title" />
							<s:select path="rowcount" cssClass="form-control" id="rowcount">
								<s:options items="${itemPerPageList}" />
							</s:select>
						</div>
					</div>
               </div>
		       <div class="row">
					<div class="col-lg-3">
						<div class="form-group">
							<label><spring:message code="customer.label.status" /></label>
							<div class="form-group">
								<label class="radio-inline" style="display: inline-block"><s:radiobutton
										path="status" value="2" checked="checked" /> <spring:message
										code="common.label.all" /></label> <label class="radio-inline"
									style="display: inline-block"><s:radiobutton
										path="status" value="1" /> <spring:message
										code="common.button.activate" /></label> <label class="radio-inline"
									style="display: inline-block"><s:radiobutton
										path="status" value="0" /> <spring:message
										code="common.button.deactivate" /></label>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-12 col-md-12 text-center">
						<button type="submit" id="submit" class="btn btn-submit btn-md">
							<spring:message code="team.button.showTeam" />
						</button>
					</div>

				</div>
			</div>
		</s:form>
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
								<jsp:param name="actionName" value="<%= URLConstants.TEAM_LISTS %>" />
								<jsp:param name="rowcount" value="${rowcount}" />
								<jsp:param name="excelAvailable" value="<%= URLConstants.TEAM_DOWNLOAD_XLSX %>" />
								<jsp:param name="pdfAvailable" value="<%= URLConstants.TEAM_DOWNLOAD_PDF %>" />
								<jsp:param name="params" value="name,leadName,memberName,status" />
								<jsp:param name="name" value="${teamForm.name}" />
								<jsp:param name="leadName" value="${teamForm.leadName}" />
								<jsp:param name="memberName" value="${teamForm.memberName}" />
								<jsp:param name="status" value="${teamForm.status}" />
							</jsp:include>
						</c:if>
					</div>
				</div>
			</div>
			<div class="panel-body" style="padding: 0;">
				<table class="table report-data">
					<thead>
						<tr style="background:#004e6a;color:#fff;">
							<th><spring:message code="common.label.actions"></spring:message></th>
							<th><spring:message code="team.label.teamName" /></th>
							<th><spring:message code="team.label.teamLead" /></th>
							<th><spring:message code="team.label.memberName" /></th>
						</tr>
					</thead>
					<tbody>
						<%
							int count = 0;
						%>
						<c:if test="${not empty data}">
							<c:forEach items="${data}" var="listdata">
								<%
									count++;
								%>
								<tr>
									<td><span id="stat<%=count%>"> <c:choose>
												<c:when test="${listdata.status == 1}">
													<i class="fa fa-star fa-lg newStar" aria-hidden="true"
														style="cursor: pointer" data-toggle="tooltip"
														data-original-title="<spring:message code="common.label.active"/>"
														title=''
														onclick="return changeStatus(this,${listdata.id},${listdata.status},'${listdata.name}',<%=count%>);"></i>
												</c:when>
												<c:otherwise>
													<i class="fa fa-star-o fa-lg newStar" aria-hidden="true"
														style="cursor: pointer" data-toggle="tooltip"
														data-original-title="<spring:message code="common.label.inactive"/>"
														title=''
														onclick="return changeStatus(this,${listdata.id},${listdata.status},'${listdata.name}',<%=count%>);"></i>
												</c:otherwise>
											</c:choose>
									</span> <i class="fa fa-pencil fa-lg" aria-hidden="true"
										style="cursor: pointer" data-toggle="tooltip"
										data-original-title="<spring:message code="team.label.edit"/>"
										title='' onclick="return teamEdit(${listdata.id});"></i></td>
									<td><c:out value="${listdata.name}" /></td>
									<td><c:out value="${listdata.leadName}" /></td>
								<td id = "active<%=count%>"><p><c:out value="${listdata.members}" /></p></td>
								</tr>
							</c:forEach>
						</c:if>
						<c:if test="${empty data}">
							<tr class="row1">
								<td colspan="4" align="center"><spring:message
										code="common.message.nodatafound"></spring:message></td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>
			<c:if test="${not empty data}">
				<div class="panel-footer">
					<div class="row">
						<div class="col-lg-12 col-md-12 text-right">
							<jsp:include page="/views/common/pagination.jsp">
								<jsp:param name="totalRows" value="${totalRows}" />
								<jsp:param name="currentPage" value="${currentPage}" />
								<jsp:param name="actionName" value="<%= URLConstants.TEAM_LISTS %>" />
								<jsp:param name="rowcount" value="${rowcount}" />
								<jsp:param name="excelAvailable" value="<%= URLConstants.TEAM_DOWNLOAD_XLSX %>" />
								<jsp:param name="pdfAvailable" value="<%= URLConstants.TEAM_DOWNLOAD_PDF %>" />
								<jsp:param name="params" value="name,leadName,memberName,status" />
								<jsp:param name="name" value="${teamForm.name}" />
								<jsp:param name="leadName" value="${teamForm.leadName}" />
								<jsp:param name="memberName" value="${teamForm.memberName}" />
								<jsp:param name="status" value="${teamForm.status}" />
							</jsp:include>
						</div>
					</div>
				</div>
			</c:if>

		</div>
	</c:if>
</div>
<%@page import="in.otpl.dnb.util.ServerConstants"%>
<%@page import="in.otpl.dnb.util.URLConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<spring:url value="<%= URLConstants.TEAM_UPDATE %>" var="updateUrl" />
<spring:url value="<%= URLConstants.TEAM_LIST %>" var="listUrlShow" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/bootstrap-multiselect.css">
<script src="<%=request.getContextPath()%>/resources/js/bootstrap-multiselect.js"></script>
<script>
$(document).ready(function(){
$("#teamLeadId").multiselect({
	includeSelectAllOption : false,
	enableFiltering : true,
	enableCaseInsensitiveFiltering: true,
	buttonWidth : '100%',
	maxHeight : 300,
	dropRight : true
	});

});
function teamListing(){
	window.location.href = "${listUrlShow}";	
}

$(document).on("click", ".addRow", function(){
	var count=0;
	var $delete = '<h4 class="glyphicon glyphicon-minus-sign deleteRow" style="color:#f44336;cursor:pointer;"></h4>';
	var $clone = $(this).parent().parent();
	$clone.find(".addRow").remove();
	$clone.find("td:eq(0)").append($delete);
	$(".selected table").append($clone);
	var selection = $("#selectedList").find("table td input");
	count++;
	selection.attr("id", "selected" + count);
	selection.attr("name", "selected");
	$(".deleteRow").on("click", function(){
		var $add = '<h4 class="glyphicon glyphicon-plus-sign addRow" style="color:#32c24d;cursor:pointer"></h4>';
		var newClone = $(this).parent().parent();
		newClone.find(".deleteRow").remove();
		newClone.find("td:eq(0)").append($add);
		$(".available table").append(newClone);
	});
});
	
$(document).on("click", ".deleteRow", function(){
	var count=0;
	var $add = '<h4 class="glyphicon glyphicon-plus-sign addRow" style="color:#32c24d;cursor:pointer"></h4>';
	var newClone = $(this).parent().parent();
	newClone.find(".deleteRow").remove();
	newClone.find("td:eq(0)").append($add);
	$(".available table").append(newClone);
	var availableValue = $("#availableList").find("table td input");
	count++;
	availableValue.attr("id", "available" + count);
	availableValue.attr("name", "available");
	$(".addRow").on("click", function(){
		var $delete = '<h4 class="glyphicon glyphicon-minus-sign deleteRow" style="color:#f44336;cursor:pointer;"></h4>';
		var $clone = $(this).parent().parent();
		$clone.find(".addRow").remove();
		$clone.find("td:eq(0)").append($delete);
		$(".selected table").append($clone);
	});
});

function validateTeam(){
	var count=0;
	var tName=$('#teamName').val();
	var selection = $("#selectedList").find("table td input");
	var b = selection.attr("value");
	if( tName.trim()== ""){
		bootbox.alert('<spring:message code="team.message.namerequired" />');
		return false;
	}else {
		var spcharsName ='`\\~^\/"|<>;{}[]';
		for(var i=0;i<tName.length;i++){
			for(var j=0;j<spcharsName.length;j++){
				if(tName.charAt(i) == spcharsName.charAt(j)){
					bootbox.alert("<spring:message code='team.error.specialcharacters' />");
					return false;
		     	}
		     }
		}
	}
	if($('#teamLeadId').val() == "0"){
		bootbox.alert('<spring:message code="team.message.selectTeamlead" />');
		return false;
	}
	if(b == undefined){
		bootbox.alert('<spring:message code="team.message.selectUser" />');
		return false;
	} 
} 
</script>

<div class="container-fluid">
<div class="panel panel-default">						

<div class="panel-heading">
<span class="heading"><spring:message code="team.label.headeredit"/></span>


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

<s:form action="${updateUrl}" method="post" commandName="teamForm" id="teamForm" onsubmit="return validateTeam();">
	
   <div class="panel-body">
    <div class="row">
    <div class="col-lg-4 col-sm-12 col-md-4">
    <div class="form-group">
    <label><spring:message code="team.label.teamName"/><sup class="text-danger">*</sup></label>
    <spring:message code="team.placeholder.teamName" var="tname"/>
    <s:input path="teamName" type="text" maxlength="50" id="teamName" cssClass="form-control" placeholder="${tname}"/>
    <span class="text-danger"><s:errors path="teamName" cssClass="error" />
    <span class="text-danger"><c:if test="${not empty teamAlreadyExisted}">${teamAlreadyExisted}</c:if></span>
	</span>
	</div>
    </div>
    <s:input path="teamId" type="hidden"/>
    <div class="col-lg-4 col-md-4">
    <div class="form-group">
    <label><spring:message code="team.label.teamLead"/><sup class="text-danger">*</sup></label>
	<s:select path="teamLeadId" cssClass="form-control" id="teamLeadId" >
    <c:if test="${not empty teamLeads}">
	<c:forEach items="${teamLeads}" var="teamdata">
	<s:option value="${teamdata.id}" label="${teamdata.name}" />
	</c:forEach>
	</c:if>
    </s:select>
    <span class="text-danger"><s:errors path="teamLeadId" cssClass="error" /></span>
    </div>
    </div>
    
    </div>
	<div class="row">
  	<div class="col-lg-4 col-md-4 col-lg-offset-2 col-md-offset-2 available" id="availableList" style="max-height:300px;overflow-y:auto;margin-bottom:15px;">
    <label><spring:message code="team.label.availableMembers"/></label>
    <table class="table table-bordered">
    <tbody>
    <c:if test="${not empty availableUsers}" >
    <c:forEach items="${availableUsers}" var="availableUsers" >
    <tr>
    <td class="imgCont"> <h4 class="glyphicon glyphicon-plus-sign addRow" style="color:#32c24d;cursor:pointer"></h4></td>
    <td class="txtCont"><h6><c:out value="${availableUsers.name}" />
    <s:hidden path="available" id="available" value="${availableUsers.id}" />
    </h6></td>
    </tr> 
    </c:forEach>
    </c:if>
    </tbody>
    </table>
    </div>
    <div class="col-lg-4 col-md-4 selected " id="selectedList" style="max-height:300px;overflow-y:auto;margin-bottom:15px;">
    <label><spring:message code="team.label.selectedMembers"/></label>
    <table class="table table-bordered">
    <tbody>
    <c:if test="${not empty selectedUsers}" >
    <c:forEach items="${selectedUsers}" var="selectedUsers" >
    <tr>
    <td class="imgCont"><h4 class="glyphicon glyphicon-minus-sign deleteRow" style="color:#f44336;cursor:pointer;"></h4></td>
    <td class="txtCont">
    <h6><c:out value="${selectedUsers.name}" />
    <s:hidden path="selected" id="selected" value="${selectedUsers.id}" />
    </h6></td>
    </tr> 
    </c:forEach>
    </c:if>
    </tbody>
    </table>
    </div>
  	</div>
    <div class="row">
    <div class="col-lg-12 col-md-12 text-center">
    <button type="submit" class="btn btn-md btn-submit"><spring:message code="team.label.update"/></button>
    <span style="padding-left: 5px; padding-bottom: 0px; margin-top: 6px;">
	<a class="btn btn-danger" style="border-radius:2px;" onclick="return teamListing();"><spring:message code="team.label.back" /></a></span>
    </div>
    </div>
    <div class="row mandatory">
    <div class="col-lg-12 col-md-12">
    <p style="font-size:12px">
    <span class="text-danger">*<spring:message code="team.label.mandatoryFields"/></span><br>
    </p>
    </div>
    </div>
    </div> 
   
</s:form>
</div>
</div>
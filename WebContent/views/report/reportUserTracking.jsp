<%@page import="in.otpl.dnb.util.URLConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<spring:url value="<%=URLConstants.REPORT_USER_TRACKING_LIST%>" var="viewReport" />
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/datepicker.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/bootstrap-multiselect.css">
<script src="<%=request.getContextPath()%>/resources/js/bootstrap-multiselect.js"></script>

<script type="text/javascript">
$(document).ready(function() {
	
	$("#userId").multiselect({
		includeSelectAllOption : false,
		enableFiltering : true,
		buttonWidth : '100%',
		maxHeight : 300,
		enableCaseInsensitiveFiltering: true,
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
function validateForm(form){		
	if($('#userId').val() == '0'){     	
 		bootbox.alert('<spring:message code="user.tracking.report.validate.user"></spring:message>');
 		return false;    	   
	  } 
}
$(function() {

	$("#selectall").change(function () {
    $("input:checkbox").prop('checked', $(this).prop("checked"));
});
}); 
$(document).ready(function(){
	$(".mapDataDistance").click(function(){
		var checkBoxElement=document.forms[0].mapData;
		var min = 0;
		var max = 0;
		 var count = 0;
			for (var i = checkBoxElement.length - 1 ; i >= 0 ; i--) {
			    if (checkBoxElement[i].checked) {
			        count++;
			        if(max == 0){
			        	max = i+1;	
			        }		
			        min = i+1;
			      }      
			 }
			 if(count >=2){
				for(var j=min; j<= max; j++){
					var id = "mapData"+j;
					document.getElementById(id).checked=true;
				}
			} 
	});
});
function calculate(fieldName){
	var checkBoxElement=fieldName;
	if(checkBoxElement.length==0)
		{
		bootbox.alert('<spring:message code="user.tracking.report.map.validate"></spring:message>');
		}
	 if(!checkBoxElement.length){
		checkBoxElement=[checkBoxElement];
	} 
	
	var count = 0;
	for (var i = checkBoxElement.length - 1 ; i >= 0 ; i--) {
	    if (checkBoxElement[i].checked) {
	        	count++;
	        
	      }      
	 } 
	
	 if(count < 2){
		 bootbox.alert('<spring:message code="activityreport.select.msg1" />');
     	return false;
     }else{
    	 var lat0,lat1,lat2,long0,long1,long2,dist;
    	 
    	 var index = 0;
    	 for (var i = checkBoxElement.length - 1 ; i >= 0 ; i--) {
    		    if (checkBoxElement[i].checked) {
    		    	var val = checkBoxElement[i].value;
    		    	console.log(val);
		    		var stringArray = val.split(",");
		    		if(index == 0){
    		    		lat0=stringArray[0];
    		    		long0=stringArray[1];
    		    	}else if(index == 1){
    		    		lat1=stringArray[0];
    		    		long1=stringArray[1];
    		    		dist=distance(lat0,long0,lat1,long1);
    		    	}else{
    		    		lat2=stringArray[0];
    		    		long2=stringArray[1];
    		    		lat0=lat1;
    		    		lat1=lat2;
    		    		long0=long1;
    		    		long1=long2;
    		    		if(lat0 != lat1 && long0 != long1){
	    		    		dist=dist+distance(lat0,long0,lat1,long1);
    		    		}
    		    	}
    		    	index++;
    		    	
    		      }      
    		 }
    	 var dist1;
    	 if(dist == 0 ){
    		 bootbox.alert('<spring:message code="user.tracking.report.map.totalDistance"></spring:message>');
    	 }else{
    		 dist1=Math.round(dist*1000)/1000;
    		 bootbox.alert('<spring:message code="user.tracking.report.map.totalDistance.travelled"></spring:message>'+dist1  +'<spring:message code="user.tracking.report.map.distanceIn"></spring:message>');
    	 }
    	//document.getElementById["calc"].innerHTML="Total Distance Travelled  " +dist1+ " KM";
     }
}
function distance(lat0,long0,lat1,long1){
	var R = 6371;
	var dist;
	var dlon=long1 - long0;
	dist=Math.acos(Math.cos(Math.PI * (90-lat0)/180) *Math.cos(Math.PI *(90-lat1)/180) +Math.sin(Math.PI * (90-lat0)/180) *Math.sin(Math.PI * (90-lat1)/180) *Math.cos(Math.PI*(dlon)/180)) *R;
			return dist;
}
</script>
<s:form action="${viewReport}" method="post" commandName="userTrackingForm" id="userTrackingForm" onsubmit="return validateForm(this);">
  <div class="container-fluid">
		<div class="panel panel-default">
			<div class="panel-heading">User(s) Tracking Report
 	</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-lg-3">
						<div class="form-group">
							<label><spring:message code="user.tracking.lable.user"></spring:message><sup class="text-danger">*</sup></label>
							 <spring:message code="user.tracking.lable.user" var="title"></spring:message> 
							 <s:select path="userId" cssClass="text_field form-control required" id="userId">
									<s:option value="0"><spring:message code="user.tracking.option.select" /></s:option>
									<c:if test="${not empty userList}">
										<c:forEach items="${userList}" var="accdata">
											<s:option value="${accdata.id}" label="${accdata.firstName} ${accdata.lastName}" />
										</c:forEach>
									</c:if>
							</s:select>   
						</div>
					</div> 
					<div class="col-lg-3">
						<div class="form-group">
							<label><spring:message code="user.tracking.report.dateFrom" /><sup class="text-danger">*</sup></label>
							<div class="input-group">
								<s:input path="dateFrom" type="text" cssClass="form-control flatpickr" id="dateFrom" data-input="" data-time_24hr="true" data-timeFormat="H:i"/>
								<div class="input-group-addon from-addon" style="cursor:pointer;" data-toggle="">
									<span class="glyphicon glyphicon-calendar"></span>
								</div>
							</div> 
						</div>
					</div>
					<div class="col-lg-3">
						<div class="form-group">
							<label><spring:message code="user.tracking.report.dateTo" /><sup class="text-danger">*</sup></label>
							<div class="input-group">
								<s:input path="dateTo" type="text" cssClass="form-control flatpickr" id="dateTo" data-input="" data-time_24hr="true" data-timeFormat="H:i" />
								<div class="input-group-addon to-addon" style="cursor:pointer;" data-toggle="">
								<span class="glyphicon glyphicon-calendar"></span>
								</div>
							</div> 
						</div>
					</div>	
					<div class="col-lg-3">
						<div class="form-group">
							<label><spring:message code="user.tracking.report.itemsperpage" /></label>
									<spring:message code="user.tracking.report.itemsperpage" var="title"/>
							<s:select path="rowcount" cssClass="form-control" data-toggle="tooltip" title="${title}" id="rowcount">
								<s:options items="${itemsPerPageList}" />
							</s:select>
						</div>
					</div>
				</div>
				<div class="row">
				<div class="col-lg-12 col-md-12 text-center">
				<label>&nbsp;</label> <br>
							<button type="submit" name="submit" id="submit" class="btn btn-submit btn-lg">
								<spring:message code="user.tracking.report.show.report" />
							</button>
							</div>
				</div>
				<div class="row mandatory">
					<div class="col-lg-12 col-md-12">
						<p style="font-size: 12px">
							<span class="text-danger">*<spring:message code="user.tracking.report.mandatory.message" /></span>
						</p>
					</div>
				</div>
			</div>
		</div>
		
	<c:if test="${data != null}">
	<div class="panel panel-default">
  <div class="panel-heading">
  <div class="row">
<div class="col-lg-4 col-md-4 text-left">
<a class="btn btn-success" onClick="showMap(document.forms[0].case);">
 <span class="fa fa-map-marker fa-2x" data-toggle="tooltip" title="" data-original-title="<spring:message code="user.tracking.report.map"/>"></span>
</a> 
<a class="btn btn-default" onclick="javascript:calculate(document.forms[0].case)"> 
<span><spring:message code="user.tracking.report.calculateDistance" /> </span>
</a>
</div>
<div class="col-lg-8 col-md-8 text-right">
<c:if test="${not empty data}">
									
<jsp:include page="/views/common/pagination.jsp">
<jsp:param name="totalRows" value="${totalRecords}" />
<jsp:param name="currentPage" value="${currentPage}" />
<jsp:param name="actionName" value="<%= URLConstants.REPORT_USER_TRACKING_LIST_PAGINATION %>" />
<jsp:param name="rowcount" value="${rowcount}" />
<jsp:param name="excelAvailable" value="<%= URLConstants.REPORT_USER_TRACKING_DOWNLOAD_EXCEL %>" />
<jsp:param name="pdfAvailable" value="<%= URLConstants.REPORT_USER_TRACKING_DOWNLOAD_PDF %>" />
<jsp:param name="params" value="userId,dateFrom,dateTo" />
<jsp:param name="reportType" value="${userTrackingForm.userId}" />
 <jsp:param name="dateFrom" value="${userTrackingForm.dateFrom}" />
 <jsp:param name="dateTo" value="${userTrackingForm.dateTo}" />
</jsp:include>
</c:if>		
</div>
  </div>
  </div>
  <div class="panel-body" style="padding:0;">
   							<table class="table">						
								<thead>
									<tr style="background:#004e6a;color:#fff;">
									    <th style="width:35px"><input type="checkbox" id ="selectall" /></th>
										<th><spring:message code="user.tracking.report.date&Time"></spring:message></th>
										<th><spring:message code="user.tracking.report.user"></spring:message></th>
										<th><spring:message code="user.tracking.report.description"></spring:message></th>
										<th><spring:message code="user.tracking.report.status"></spring:message></th>
										<th><spring:message code="user.tracking.report.latitude"></spring:message></th>
										<th><spring:message code="user.tracking.report.longitude"></spring:message></th>
										<th><spring:message code="user.tracking.report.speed"></spring:message></th>
										<th><spring:message code="user.tracking.report.accuracy"></spring:message></th>
									</tr>
								</thead>
								<tbody>
								<% int count=0; %>
								<c:if test="${not empty data}">
									<c:forEach items="${data}" var="listdata">
										<tr>
										    <td colspan="1" align="center">
										    <c:if test="${listdata.latitude!='NA'}">
										    <%count++;%>
										    <input type="checkbox" class="case" name="case" value="${listdata.latitude},${listdata.longitude}"/>
										    <span id="checkCounter_<%=count%>">
										    </span>
										    </c:if>
										    </td>
											<td>${listdata.clientTime}</td>
											<td>${listdata.fullName}</td>												
											<td>${listdata.eventName}</td>
											<td>${listdata.bccStatus} </td>
											<td>${listdata.latitude} </td>
											<td>${listdata.longitude}</td>												
											<td>${listdata.speed}</td>
											<td>${listdata.accuracy}</td>	
										</tr>
										</c:forEach>
								</c:if>			
								<c:if test="${empty data}">
							<tr class="row1">
								<td colspan="9" align="center"><spring:message code="common.message.nodatafound"></spring:message></td>
							</tr>					
							  	</c:if>
						</tbody>			
							</table>
  </div>
   <div class="panel-footer">
  <div class="row">
<div class="col-lg-4 col-md-4 text-left">
<a class="btn btn-success" onClick="showMap(document.forms[0].case);">
 <span class="fa fa-map-marker fa-2x" data-toggle="tooltip" title="" data-original-title="<spring:message code="user.tracking.report.map"/>"></span>
</a> 
<a class="btn btn-default" onclick="javascript:calculate(document.forms[0].case)"> 
<span><spring:message code="user.tracking.report.calculateDistance" /> </span>
</a>
</div>
<div class="col-lg-8 col-md-8 text-right">
<c:if test="${not empty data}">
									
<jsp:include page="/views/common/pagination.jsp">
<jsp:param name="totalRows" value="${totalRecords}" />
<jsp:param name="currentPage" value="${currentPage}" />
<jsp:param name="actionName" value="<%= URLConstants.REPORT_USER_TRACKING_LIST_PAGINATION %>" />
<jsp:param name="rowcount" value="${rowcount}" />
<jsp:param name="excelAvailable" value="<%= URLConstants.REPORT_USER_TRACKING_DOWNLOAD_EXCEL %>" />
<jsp:param name="pdfAvailable" value="<%= URLConstants.REPORT_USER_TRACKING_DOWNLOAD_PDF %>" />
<jsp:param name="params" value="userId,dateFrom,dateTo" />
<jsp:param name="reportType" value="${userTrackingForm.userId}" />
 <jsp:param name="dateFrom" value="${userTrackingForm.dateFrom}" />
 <jsp:param name="dateTo" value="${userTrackingForm.dateTo}" />
</jsp:include>
</c:if>		
</div>
</div>
</div>
</div>

	</c:if>
	<input type="hidden" id="polyline" value="1">
	<jsp:include page="../common/googleMapApi3Report.jsp"/>
	</div>
</s:form>
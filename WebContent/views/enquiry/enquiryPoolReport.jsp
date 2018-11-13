<%@page import="in.otpl.dnb.util.ServerConstants"%>
<%@page import="in.otpl.dnb.util.URLConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<spring:url value="<%= URLConstants.ENQUIRY_DETAILS %>" var="enquiryDetails" />
<spring:url value="<%= URLConstants.ENQUIRY_SAVE %>" var="saveEnquiry" />
<spring:url value="<%= URLConstants.ENQUIRY_CASE_HISTORY %>" var="caseHistory" />
<spring:url value="<%= URLConstants.ENQUIRY_ALL_USERS %>" var="allActiveUsers" />
<spring:url value="<%= URLConstants.ENQUIRY_CASE_ASSIGNMENT_HISTORY %>" var="caseAssignmentHistory" />
<spring:url value="<%= URLConstants.ENQUIRY_DOCUMENT_LIST %>" var="enquiryDocumentList" />
<spring:url value="<%= URLConstants.ENQUIRY_LIST %>" var="listUrlShow" />
<spring:url value="<%= URLConstants.ENQUIRY_DOWNLOAD_EXCEL %>" var="downloadExcel" />
<spring:url value="<%= URLConstants.ENQUIRY_DOWNLOAD_PDF %>" var="downloadPdf" />
<spring:url value="<%= URLConstants.ENQUIRY_REPORT_PDF %>" var="pdfLink" />
<spring:url value="<%= URLConstants.ENQUIRY_REPORT_HTML %>" var="linkUrl" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/bootstrap-multiselect.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/bootstrap-multiselect.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/datepicker.js"></script>
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

	$("#selUser").multiselect({
		includeSelectAllOption : false,
		enableFiltering : true,
		enableCaseInsensitiveFiltering: true,
		buttonWidth : '100%',
		maxHeight : 300,
		dropRight : true
		});
	
	$("#caseStatus").multiselect({
		includeSelectAllOption : false,
		enableFiltering : false,
		enableCaseInsensitiveFiltering: false,
		buttonWidth : '100%',
		maxHeight : 300,
		dropRight : true
		});
	
	$("#dnbMid").val('');
	
}); 
	
function validateEnquiry(){
	if($('#dnbMid').val()==''){
		$('#dnbMid').val('0')
}
}

function trimValue(field) {
	var val = field.value;
	field.value = trim(val);
}
function trim(val){
	return val.replace(/^\s+|\s+$/g,"")
}

function webLinkReport(dnbMasterDataId) {
	var url="${linkUrl}"+dnbMasterDataId;
	var newTabUrl=window.open(url,'_blank');
	newTabUrl.location;
}
function createEnquiry(enquiryNo,dateFrom,dnbMasterId,asphId,isReassigned,enquiryDetailsId){
	var ASSIGN_FORM = '<form method="post" action="#">\
				       <div class="row">\
				       <input type="hidden" name="dnbMasterId" id="dnbMasterId"/>\
				       <input type="hidden" name="asphId" id="asphId"/>\
				       <input type="hidden" name="enquiryDetailsId" id="enquiryDetailsId"/>\
				       <input type="hidden" name="isReassigned" id="isReassigned"/>\
				       <input type="hidden" name="en" id="en"/>\
								<div class="col-lg-4 col-md-4">\
								<div class="form-group">\
								<label>Assignment Enquiry Name<span class="error">*</span></label>\
								<input name="assignmentName" class="form-control" size="20" maxlength="50" id="enquiryName" readonly="true"/>\
								</div></div>\
								<div class="col-lg-4 col-md-4">\
								<div class="form-group">\
								<label style="display:block">Enquiry Start Time<span class="error">*</span></label>\
								<input name="dateFrom" class="form-control" size="20"  id="enquiryDate" readonly="true"/>\
								</div></div>\
								<div class="col-lg-4 col-md-4" id="user-data">\
								<div class="form-group">\
								<label>User</label>\
								<select name="uId" class="form-control muId" style="min-width: 100px;" id="uId"><option value="0">Select</option>';
				var postData = ' ';
				var userList = ajaxMethod(postData,'json','${allActiveUsers}');
				if(userList != null && userList.length > 0){
					for(var i = 0 ; i < userList.length ; i++){
						ASSIGN_FORM +='<option value='+userList[i].id+'>'+userList[i].uname+'</option>';				
											}
				}
				
		ASSIGN_FORM	 +='</select></div></div></div>\
		  				<div class="submitRow" align="center" style="padding-top: 10px;"><button type="button" class="btn btn-primary btn-lg" onclick="saveEnquiry('+enquiryNo+',0)">Save</button></div>\
	               </form>';
	               
    	$("#assignment-data").html(ASSIGN_FORM);
    	$('#asphId').val(asphId);
    	$('#enquiryDate').val(dateFrom);
    	$('#isReassigned').val(isReassigned);
    	$('#dnbMasterId').val(dnbMasterId);
    	$('#enquiryDetailsId').val(enquiryDetailsId);
    	$('#enquiryName').val('Enquiry - '+enquiryNo);
    	$('#ass-header').html('Assign Enquiry - '+enquiryNo);
    	$(".muId").multiselect({
    		includeSelectAllOption : false,
    		enableFiltering : true,
    		enableCaseInsensitiveFiltering: true,
    		buttonWidth : '100%',
    		maxHeight : 300,
    		dropRight : true
    		});
}

function reAssignEnquiry(enquiryNo,dateFrom,dnbMasterId,asphId,isReassigned,assignedUserId,enquiryDetailsId){
						var ASSIGN_FORM = '<form method="post" action="#">\
						       <div class="row">\
						       <input type="hidden" name="dnbMasterId" id="dnbMasterId"/>\
						       <input type="hidden" name="asphId" id="asphId"/>\
						       <input type="hidden" name="enquiryDetailsId" id="enquiryDetailsId"/>\
						       <input type="hidden" name="isReassigned" id="isReassigned"/>\
						       <input type="hidden" name="assignedUserId" id="assignedUserId"/>\
										<div class="col-lg-4 col-md-4">\
										<div class="form-group">\
										<label>Assignment Enquiry Name<span class="error">*</span></label>\
										<input name="enquiryName" size="20" class="form-control" maxlength="50" id="enquiryName" readonly="true"/>\
										</div></div>\
										<div class="col-lg-4 col-md-4">\
										<div class="form-group">\
										<label style="display:block">Enquiry Start Time<span class="error">*</span></label>\
										<input name="dateFrom" class="form-control" size="20"  id="enquiryDate" readonly="true"/>\
										</div></div>\
										<div class="col-lg-4 col-md-4" id="user-data">\
										<div class="form-group">\
										<label>User</label>\
										<select name="uId" style="min-width: 100px;" class="form-control muId" id="uId"><option value="0">Select</option>';
										
						var postData = ' ';
						var userList = ajaxMethod(postData,'json','${allActiveUsers}');
						if(userList != null && userList.length > 0){
							for(var i = 0 ; i < userList.length ; i++){
								ASSIGN_FORM +='<option value='+userList[i].id+'>'+userList[i].uname+'</option>';				
										}
						}
						
					ASSIGN_FORM	 +='</select></div></div></div>\
								<div class="submitRow" align="center" style="padding-top: 10px;"><button type="button" class="btn btn-primary btn-lg" onclick="saveEnquiry('+enquiryNo+',1)">Reassign</button></div>\
					    </form>';
					    
					$("#assignment-data").html(ASSIGN_FORM);
					$('#asphId').val(asphId);
					$('#enquiryDate').val(dateFrom);
					$('#isReassigned').val(isReassigned);
					$('#dnbMasterId').val(dnbMasterId);
					$('#enquiryDetailsId').val(enquiryDetailsId);
					$('#assignedUserId').val(assignedUserId);
					$('#enquiryName').val('Enquiry - '+enquiryNo);
	    			$('#ass-header').html('Reassign Enquiry - '+enquiryNo);
	    			$("#uId option[value='"+assignedUserId+"']").attr("selected","selected");
	    			$(".muId").multiselect({
	    				includeSelectAllOption : false,
	    				enableFiltering : true,
	    				enableCaseInsensitiveFiltering: true,
	    				buttonWidth : '100%',
	    				maxHeight : 300,
	    				dropRight : true
	    				});
}

function saveEnquiry(enquiryNo,type){
	var enquiryName = $('#enquiryName').val();
	var uid = $("#uId").val();
	var date = $('#enquiryDate').val();
	var startDate = $('#enquiryDate').val();
	var asphId = $('#asphId').val();
	var userName = $("#uId option:selected").text();
	var dnbMasterId = $('#dnbMasterId').val();
	var isReassigned = $('#isReassigned').val();
	var enquiryDetailsId = $('#enquiryDetailsId').val();
	
	
	if(uid == 0){
		bootbox.alert('Please Select atleast One User');
 		return false;
	}
	
	
	if(uid == $('#assignedUserId').val() && type == 1){
		bootbox.alert('Reassignment to same user is not allowed');
 		return false;
	}
	
	$.post("${saveEnquiry}",{uid:uid,startDate:startDate,enquiryName:enquiryName,
		                     dnbMasterId:dnbMasterId,
							 asphId:asphId,isReassigned:isReassigned,
							 enquiryDetailsId:enquiryDetailsId},
	    function(data){
	    	if(data > 0){
	    		var newEnquiryDetailId = data;
				$('#enquiryDetailsId').val(newEnquiryDetailId);
				$('#assignedUserId').val(uId);
	    		$('#userName-'+asphId).html(userName);
	    		$('#caseStatus-'+asphId).html('');
	    		$("#modal_2").hide();
	    		var HTML_CONTENT = '<button type="button" title="Reassign Enquiry" id="open_modal" data-toggle="modal" data-backdrop="static" data-keyboard="false" data-target="#modal_2" onclick="reAssignEnquiry('+enquiryNo+',\''+date+'\','+dnbMasterId+','+asphId+',1,'+uId+','+newEnquiryDetailId+')" style="cursor: pointer;"><span class="glyphicon glyphicon-pencil"></span>\
 				</button>';
				$('#assigned-'+asphId).html('Assigned '+HTML_CONTENT);
	    		bootbox.alert(enquiryName+' has been assigned successfully');
	    	$(".modal-backdrop").hide();
	    	}else{
	    		$("#modal_2").modal('hide');
	    		bootbox.alert('Unable to assign enquiry !!.. Please try again ');
	    	}
	    }
    );
	return false;	
}

function ajaxMethod(postData,dataType,urlName){
	var returnData = null;
	$.ajax({url:urlName,type:'POST',cache : false, dataType:'json',async:false,data:postData,
		success: function(data){
			returnData = data;
		}
	});
	return returnData;
}

function showEnquiryDetails(dnbMasterId,enquiryNum){
	var postData = 'dnbMasterId='+dnbMasterId;
	var enquiryDetails = ajaxMethod(postData,'json','${enquiryDetails}');
	$('#h4-content').html('Enquiry Details - '+enquiryNum);
	
	
	var HTML_TEMPLATE='<table class="table table-condensed table-bordered table-responsive show-table">\
					  <thead><tr><th>Case Id</th><th>Entity Company Name</th>\
        			  <th>Entity Address</th><th>Contact Person Name</th><th>Contact Number</th><th>Customer Reference Number</th>\
                      <th>Customer Email Address</th><th>Corporate Name</th></tr></thead>';
      if(enquiryDetails != null){
    	  HTML_TEMPLATE+='<tbody><tr><td>'+enquiryDetails.case_id+'</td><td>'+enquiryDetails.entityCompName+'</td><td>'+enquiryDetails.entityAddress+'</td><td>'+enquiryDetails.contactPersonName+'</td><td>'+enquiryDetails.contactNumber+'</td><td>'+enquiryDetails.custCRNNumber+'</td><td>'+enquiryDetails.custEmailAddress+'</td><td>'+enquiryDetails.corporateName+'</td></tr></tbody></table>';
      }else{
    	  HTML_TEMPLATE+='<tbody><tr><td colspan="7" style="text-align:center;">No Data Found</td></tr></tbody></table>';
      }                
	$("#modal-popbody").html(HTML_TEMPLATE);
}

function showDocumentList(caseId,enquiryNum){
	var postData = 'caseId='+caseId;
	var documentDetails = ajaxMethod(postData,'json','${enquiryDocumentList}');
	$('#h4-content').html('Available Document List - '+enquiryNum);
	var counter = 0 ; 
	var HTML_TEMPLATE='<table class="table table-condensed table-bordered table-responsive show-table">\
					  <thead><tr><th>Sr No</th><th>Document Id</th>\
        			  <th>Document Description</th></tr></thead><tbody>';
	if(documentDetails.length > 0){
		var count=0;
		for(var j=0 ; j < documentDetails.length;j++){
			count++;
			HTML_TEMPLATE +='<tr><td>'+count+'</td><td>'+documentDetails[j].doc_id+'</td>\
							<td>'+documentDetails[j].doc_desc+'</td></tr>';
		}
			HTML_TEMPLATE+='</tbody></table>';
	}else{
		HTML_TEMPLATE +='<tr><td colspan="3" style="text-align:center;">No Data Found</td></tr></tbody></table>';
	}                
	$("#modal-popbody").html(HTML_TEMPLATE);
}


function showCaseAssignmentHistory(dnbMasterId,enquiryNum){
	var postData = 'dnbMasterId='+dnbMasterId;
	var caseAssignedHistoryDetails = ajaxMethod(postData,'json','${caseAssignmentHistory}');
	
	$('#h4-content').html('Case Assignment History - '+enquiryNum);
	var HTML_TEMPLATE ='<table class="table table-condensed table-bordered table-responsive show-table">\
		  <thead><tr><th>Sr No</th><th>Lead</th><th>FOS</th><th>Assigned Time</th><th>Is Reassigned</th></tr></thead><tbody>';
	if(caseAssignedHistoryDetails.length > 0){
		var count=0;
		for(var j=0 ; j < caseAssignedHistoryDetails.length;j++){
			count++;
			HTML_TEMPLATE +='<tr><td>'+count+'</td><td>'+caseAssignedHistoryDetails[j].lead_name+'</td>\
							<td>'+caseAssignedHistoryDetails[j].fos_name+'</td><td>'+caseAssignedHistoryDetails[j].assigned_time+'</td>\
			                <td>'+caseAssignedHistoryDetails[j].is_reassigned+'</td></tr>';
		}
		HTML_TEMPLATE+='</tbody></table>';
	}else{
		HTML_TEMPLATE +='<tr><td colspan="6" style="text-align:center;">No Data Found</td></tr></tbody></table>';
	}
	$("#modal-popbody").html(HTML_TEMPLATE);
}


function showCaseHistory(dnbMasterId,enquiryNum){
   var dnbMasterIds=dnbMasterId;
   if(dnbMasterIds != "0"){
	var postData = '&dnbMasterId='+dnbMasterId;
	var url = "${caseHistory}";
	$.ajax({url:url,type:'POST',cache : false,dataType:'json',async:false,data:postData,
		success:function(response){
			if(response != null && response != ""){
	        		  $('#h4-content').html('Case History - '+enquiryNum);
	        		  var HTML_TEMPLATE ='<table class="table table-condensed table-bordered table-responsive show-table">\
		        		  <thead><tr><th>Sr No</th><th>History Id</th><th>Lead</th><th>FOS</th><th>Date Of Site Visit</th><th>Report</th></tr></thead><tbody>';
		        	  if(response.length > 0){
				  	var count=0;
				  	$.each(response, function (key, value) {
		        	var id = value.id;
		        	var assignedBy=value.assignedBy;
		        	var assignedTo=value.assignedTo;
		        	var dos=value.dos;
		        	var dnbMasterDataId=value.dnbMasterDataId;
				  	count++;
				  	HTML_TEMPLATE +='<tr><td>'+count+'</td><td>'+id+'</td><td>'+assignedBy+'</td>\
				  		<td>'+assignedTo+'</td><td>'+dos+'</td>\
				  	    <td>';
				  	if(dnbMasterDataId > 0){
				  		HTML_TEMPLATE += '<a class="fa fa-eye " aria-hidden="true" style="cursor:pointer" data-toggle="tooltip" title = "Report" onclick="return webLinkReport(\''+dnbMasterDataId+'\');"></a>';
				  	}
				  	HTML_TEMPLATE += '</td>';
		        });
		        	  }else{
		  			HTML_TEMPLATE +='<tr><td colspan="6" style="text-align:center;">No Data Found</td></tr></tbody></table>';
		  		}
		        	  HTML_TEMPLATE+='</tbody></table>';
		  		$("#modal-popbody").html(HTML_TEMPLATE);
			}
		}
	 });
	}
}

</script>
<div class="container-fluid">
<div class="panel panel-default">						
<div class="panel-heading">
<span class="heading"><spring:message code="enquirypool.report.header"/></span>
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
<s:form action="${listUrlShow}" method="post" commandName="enquiryForm" id="enquiryForm" onsubmit="return validateEnquiry();" style="margin-bottom:0px;">
  	<div class="panel-body">
    <div class="row">
    	<div class="col-lg-3 col-md-3 col-xs-12 col-sm-12">
	<div class="form-group">
    <label><spring:message code="enquirypool.report.caseStatus" /></label>
    <s:select path="caseStatus" cssClass="form-control Multiselect" id="caseStatus">
   	<s:option value="0">All</s:option>
		<s:option value="1"><spring:message code="enquirypool.label.assigned"/></s:option>
		<s:option value="2"><spring:message code="enquirypool.label.notAssigned"/></s:option>
		<s:option value="3"><spring:message code="enquirypool.label.completed"/></s:option>
		<s:option value="4"><spring:message code="enquirypool.label.inProgress"/></s:option>
		<s:option value="5"><spring:message code="enquirypool.label.rejected"/></s:option>
		<s:option value="6"><spring:message code="enquirypool.label.accepted"/></s:option>
	   </s:select>
    </div> 
    </div>
    <div class="col-lg-3 col-md-3 col-xs-12 col-sm-12">
	<div class="form-group">
    <label><spring:message code="enquirypool.report.user" /></label>
    <s:select path="selUser" cssClass="form-control Multiselect" id="selUser">
    <s:option value="0">All</s:option>
	<c:if test="${not empty userList}">									
	<c:forEach items="${userList}" var="userdata">
	<s:option value="${userdata.id}" label="${userdata.name}" />
	</c:forEach>
	</c:if>
    </s:select>
    </div> 
    </div>
    <div class="col-lg-3 col-md-3 col-xs-12 col-sm-12">
    <div class="form-group">
    <label><spring:message code="enquirypool.report.datefrom"/></label>
    <div class="input-group">
	<s:input path="dateFrom" cssClass="form-control flatpickr" id="dateFrom" data-input="" data-time_24hr="true" data-timeFormat="H:i" />
	<div class="input-group-addon from-addon"  style="cursor:pointer;" data-toggle=""> <span class="glyphicon glyphicon-calendar"></span>
	</div>
	</div>
    </div>
    </div>
    <div class="col-lg-3 col-md-3 col-xs-12 col-sm-12">
    <div class="form-group">
    <label><spring:message code="enquirypool.report.dateto"/></label>
    <div class="input-group">
	<s:input path="dateTo" cssClass="form-control flatpickr" id="dateTo" data-input="" data-time_24hr="true" data-timeFormat="H:i" />
	<div class="input-group-addon to-addon" style="cursor:pointer;" data-toggle=""> <span class="glyphicon glyphicon-calendar"></span>
	</div>
	</div>
    </div>
    </div>
    </div>
    <div class="row">
    <div class="col-lg-3">
	<div class="form-group">
    <label><spring:message code="enquirypool.report.dnbMasterId"/></label>
    <spring:message code="enquirypool.report.dnbMasterId" var="dnbmid"/>
    <s:input path="dnbMasterId" id="dnbMid" class="form-control" placeholder="${dnbmid}" onblur="trimValue(this)"/>
    </div>
    </div>
    <div class="col-lg-3">
	<div class="form-group">
    <label><spring:message code="enquirypool.report.enquiryNo"/></label>
    <spring:message code="enquirypool.report.enquiryNo" var="enqNo"/>
    <s:input path="enquiryNo" id="enquiryNo" class="form-control" placeholder="${enqNo}" onblur="trimValue(this)"/>
    </div>
    </div>
    <div class="col-lg-3">
    <div class="form-group"><label><spring:message code="team.label.itemsperpage" /></label>
	<spring:message code="enquiry.title.itemsperpage" var="title"/>
	<s:select path="rowcount" cssClass="form-control" id="rowcount">
	<s:options items="${itemPerPageList}" />
	</s:select>
	</div>
    </div>
    </div>
    <div class="row">
    <div class="col-lg-12 col-md-12 text-center">
    <button type="submit" id="submit" class="btn btn-submit btn-md"><spring:message code="enquirypool.report.show"/></button>
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
						<jsp:param name="actionName" value="<%= URLConstants.ENQUIRY_LISTS %>" />
						<jsp:param name="rowcount" value="${rowcount}" />
            	        <jsp:param name="params" value="selUser,caseStatus,dateFrom,dateTo,dnbMasterId,enquiryNo" />
						<jsp:param name="selUser" value="${enquiryForm.selUser}" />
						<jsp:param name="caseStatus" value="${enquiryForm.caseStatus}" />
						<jsp:param name="dateFrom" value="${enquiryForm.dateFrom}" />
						<jsp:param name="dateTo" value="${enquiryForm.dateTo}" />
						<jsp:param name="dnbMasterId" value="${enquiryForm.dnbMasterId}" />
						<jsp:param name="enquiryNo" value="${enquiryForm.enquiryNo}" />
					</jsp:include> 
                </c:if>
</div>
</div>
</div>
<div class="panel-body" style="padding:0;width:100%;overflow-x:scroll;">
			<table class="table report-data" >
				<thead>
				<tr style="background:#3095b3;color:#fff;">
				<th><spring:message code="enquirypool.label.dnbMid"/></th>
                <th><spring:message code="enquirypool.label.currentQfStatus"/></th>
				<th><spring:message code="enquirypool.label.wfmStatus"/></th>
				<th><spring:message code="enquirypool.label.caseHistory"/></th>
				<th><spring:message code="enquirypool.label.caseAssignmentHitory"/></th>
				<th><spring:message code="enquirypool.label.leadEmployeeName"/></th>
			    <th><spring:message code="enquirypool.label.fosName"/></th>
				<th><spring:message code="enquirypool.label.enquiryName"/></th>
				<th><spring:message code="enquirypool.label.caseType"/></th>
				<th><spring:message code="enquirypool.label.createddate"/></th>
				<th><spring:message code="enquirypool.label.enquiryReceivedTime"/></th>
				<th><spring:message code="enquirypool.label.dateOfSiteVisit"/></th>
				<th><spring:message code="enquirypool.label.dateOfSubmission"/></th>
				<th>Report</th>
				</tr>
				</thead>
				<tbody>
				<% int count = 0; %>
				<c:if test="${not empty data}">
				<c:forEach items="${data}" var="listdata"><% count++; %>
					<tr>
					<td><span style="color:#23527c">${listdata.dnbMasterId}</span> / <span style="color:#23527c">${listdata.asphId}</span></td>
					<td id="assigned-${listdata.asphId}">
					<c:choose>
					<c:when test="${listdata.dnbMasterDataId == 0 && listdata.isDeleted == 0 && listdata.userType == 'Lead'}">
					<c:if test="${listdata.isProcessed == '0'}">
					<button type="button" class="btn btn-default" title="Assign Enquiry" id="open_modal" data-toggle="modal" data-backdrop="static" data-keyboard="false" data-target="#modal_2"
					onclick="createEnquiry(${listdata.enquiryNo},'${listdata.dateFrom}',${listdata.dnbMasterId},${listdata.asphId},0,0)" style="cursor: pointer;"><span class="glyphicon glyphicon-plus"></span>
					</button>
					</c:if>
					<c:if test="${listdata.isProcessed == '1'}">
	                <span id="stat<%=count%>">Assigned</span>
	                <button type="button" class="btn btn-default" title="Reassign Enquiry" id="open_modal" data-toggle="modal" data-backdrop="static" data-keyboard="false" data-target="#modal_2"
                    onclick="reAssignEnquiry(${listdata.enquiryNo}, '${listdata.dateFrom}',${listdata.dnbMasterId},${listdata.asphId},1 ,${listdata.userId},${listdata.enquiryDetailsId})" style="cursor: pointer;"><span class="glyphicon glyphicon-pencil"></span>
		            </button>
					</c:if>
					</c:when>
					<c:otherwise>
					<c:out value="${listdata.qfStatus}" />
					</c:otherwise>
					</c:choose>
					</td>
					<td id="caseStatus-<c:out value="${listdata.asphId}" />">
				    <c:out value="${listdata.caseStatusVal}" />
				    </td>
				    <td><a href="#" data-toggle="modal" data-target="#modal_1" onclick="showCaseHistory(${listdata.dnbMasterId},${listdata.enquiryNo})">View</a></td>
				    <td><a href="#" data-toggle="modal" data-target="#modal_1" onclick="showCaseAssignmentHistory(${listdata.dnbMasterId},${listdata.enquiryNo})">View</a></td>
				    <td><c:out value="${listdata.leadEmpName}" /></td> 
				    <td id="userName-<c:out value="${listdata.asphId}" />"><c:out value="${listdata.userName}" /></td>
				    <td><a href="#" data-toggle="modal" data-target="#modal_1" onclick="showEnquiryDetails(${listdata.dnbMasterId},${listdata.enquiryNo})">
				        ${listdata.enquiryNo}</a> / <a href="#" data-toggle="modal" data-target="#modal_1" onclick="showDocumentList(${listdata.caseId},${listdata.enquiryNo})">${listdata.caseId}</a></td>
			        <td><c:out value="${listdata.caseType}" /></td>
			        <td><c:out value="${listdata.createdDate}" /></td>
				    <td><c:out value="${listdata.eqnuiryReceivedTime}" /></td>
				    <td><c:out value="${listdata.dateOfSiteVisit}" /></td>
					<td><c:out value="${listdata.dateOfSubmission}" /></td>
			      	<td align="center">
			      	<c:if test="${listdata.dnbMasterDataId > 0}">
			      	<a class="fa fa-eye " aria-hidden="true" style="cursor:pointer" data-toggle="tooltip" title = "${listdata.dnbMasterDataId}" onclick="return webLinkReport(${listdata.dnbMasterDataId});"></a>
			      	</c:if>
			      	</td> 
					</tr>
					</c:forEach>
					</c:if>
					<c:if test="${empty data}">
					<tr class="row1">
						<td colspan="14" align="center"><spring:message code="common.message.nodatafound"></spring:message></td>
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
						<jsp:param name="actionName" value="<%= URLConstants.ENQUIRY_LISTS %>" />
						<jsp:param name="rowcount" value="${rowcount}" />
            	        <jsp:param name="params" value="selUser,caseStatus,dateFrom,dateTo,dnbMasterId,enquiryNo" />
						<jsp:param name="selUser" value="${enquiryForm.selUser}" />
						<jsp:param name="caseStatus" value="${enquiryForm.caseStatus}" />
						<jsp:param name="dateFrom" value="${enquiryForm.dateFrom}" />
						<jsp:param name="dateTo" value="${enquiryForm.dateTo}" />
						<jsp:param name="dnbMasterId" value="${enquiryForm.dnbMasterId}" />
						<jsp:param name="enquiryNo" value="${enquiryForm.enquiryNo}" />
					</jsp:include>
				</div>
				</div>
				</div>
			</c:if>
</div>
</c:if>
</div>
<div class="modal fade bs-gallery-modal-lg" id="modal_1" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
<div class="modal-dialog modal-lg" >
<div class="modal-content" id="modal_data">
<div class="modal-header">
<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
<h4 class="modal-title" id="h4-content" style="text-align: center;"></h4>
</div>
<div id="modal-popbody" style="overflow-x: scroll;">
<!-- Data goes here -->
</div>
</div>
</div>
</div>

<div class="modal fade bs-gallery-modal-lg" id="modal_2" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
<div class="modal-dialog modal-lg" >
<div class="modal-content">
<div class="modal-header"><h5 class="text-center" id="ass-header"></h5>
<button type="button" class="close" style="margin-top:-30px;" data-dismiss="modal" aria-label="Close" onclick="$('.xdsoft_datetimepicker').hide();"><span aria-hidden="true">&times;</span></button></div>
<div class="modal-body" id="assignment-data" style="height:148px;">
<!-- Data goes here -->
</div>
</div>
</div>
</div>
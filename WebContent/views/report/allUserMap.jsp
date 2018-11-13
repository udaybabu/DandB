<%@page import="in.otpl.dnb.util.PlatformUtil"%>
<%@page import="in.otpl.dnb.util.ConfigManager"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.otpl.dnb.gw.CurrentUserTrackingForm"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<style>
.boxstyle{
height:200px;
overflow-y:auto;
background:#f0f4f5;
padding:5px;
}
.boxstyle ul li{
display:block;
}
</style>
<script type="text/javascript">
function onPageUnload(){
	if(typeof GUnload != "undefined"){
		GUnload();
	}
}
</script>
<div class="container-fluid">
<div class="panel panel-default">						
<div class="panel-heading">
<span class="glyphicon glyphicon-user fa-4px" style="color:#00b259;margin-top:-4px"></span>
<span class="heading"><spring:message code="menu.label.allusertracking"/></span>
</div>

<s:form method="post" action="/showallUserTrackingReport" commandName="userMapForm" id="userMapForm">
	<!-- content box-->
   <div class="panel-body" style="padding-top:0">
    <div class="row">
     <div class="col-xs-8 col-sm-8 col-md-8 col-lg-8">
   	<h6><spring:message code="common.label.legend.tracking" /></h6>
	<img src="resources/images/all_user_map-left.png">
	</div>
    <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
	<h6><spring:message code="common.label.legend.assignment" /></h6>
	<img src="resources/images/all_user_map-right.png">
	</div>
	</div>
    
    <div>		
				<%
					List<CurrentUserTrackingForm> list = (List<CurrentUserTrackingForm>) request.getAttribute("data");
						int customerId = (Integer) request.getAttribute("customerId");
						int tdCount = 0;
						int sizeOfData = list.size();
						if (sizeOfData > 0) {
						StringBuffer table = new StringBuffer();
						HashMap<Long, String> teamMap = (HashMap<Long, String>) request.getAttribute("teamDetails");
						HashMap<String, ArrayList> teamUserMap = new HashMap<String, ArrayList>();
						for (int i = 0; i < list.size(); i++) {
							CurrentUserTrackingForm form = (CurrentUserTrackingForm) list.get(i);
							String teamName = teamMap.get(form.getTeamId());
							if (teamUserMap.containsKey(teamName)) {
								teamUserMap.get(teamName).add(form);
							} else {
								ArrayList userList = new ArrayList();
								userList.add(form);
								teamUserMap.put(teamName, userList);
							}
						}
						table.append("<div class='row'><div class='col-lg-12 col-md-12' style='padding-top:15px;'><input type='checkbox' name='selectAll' checked='checked' onClick='checkAll(this.checked)' />&nbsp;Select All</h4><br></div></div>");
						long currentTime = new Date().getTime();
						Set teamKeySet = teamUserMap.keySet();
						Iterator<String> iter = teamKeySet.iterator();
						int userIdleShort = ConfigManager.userShortIdleTime;
						int userIdleMedium = ConfigManager.userMediumIdleTime;
						int userIdleLong = ConfigManager.userLongIdleTime;
						table.append("<div class='row'>");
						while (iter.hasNext()) {
							String teamName = iter.next();
							ArrayList userList = teamUserMap.get(teamName);
							if (teamName == null) {
								teamName = "NA";
							}
							if (userList == null) {
								continue;
							}
							table.append("<div class='col-lg-3 col-md-3' style='padding-top:15px;'><div class='boxstyle'>");
							table.append("<input type = \"checkbox\" id =\""
									+ teamName
									+ "\" checked=\"checked\" name = \"mapData\" value = '"
									+ teamName
									+ "' onclick=\"checkTeamAll(this.checked,this.value,"
									+ userList.size()
									+ ")\"/>&nbsp;"
									+ teamName);
							table.append("<br>");
							table.append("<ul>");
							for (int j = 0; j < userList.size(); j++) {
								String idleColourStatus = "blue";
								CurrentUserTrackingForm form = (CurrentUserTrackingForm) userList.get(j);
							    Date dateModification=null;
							    String modificationDate=form.getModificationTime();
							   try{
							    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							    dateModification = dateFormat.parse(modificationDate);
							    }
							    catch(Exception e){
							   }
						
							long userTime = dateModification.getTime();
								float timeDiff = (float) (currentTime - userTime)/ (1000 * 60 * 60);
								if (timeDiff >= userIdleLong) {
									idleColourStatus = "red";
								} else if (timeDiff >= userIdleMedium) {
									idleColourStatus = "orange";
								} else if (timeDiff >= userIdleShort) {
									idleColourStatus = "yellow";
								}
								String modificationTime =PlatformUtil.formatDate(dateModification, PlatformUtil.SHORT_TIME);							
								String value = form.getFirstName() + " "
										+ form.getLastName() + ","
										+ form.getLattitude() + ","
										+ form.getLongitude() + ","
										+ form.getIsCellsite() + ","
										+ modificationTime
										+ "," + teamName + "," 
										+ idleColourStatus + ","
										+ form.getAssignmentStatus() + ","
										+ form.getLandmarkLatitude() + ","
										+ form.getLandmarkLongitude() + ","
										+ form.getLandmarkName();
								StringBuffer tableData = new StringBuffer();
								tableData.append("<li class='actions'>");
								tableData.append("<input type = \"checkbox\" id =\""
												+ teamName
												+ "_"
												+ j
												+ "\" checked=\"checked\" name = \"mapData\" value = '"
												+ value + "' />" + form.getFirstName()
												+ "" + form.getLastName() + "</li>");
								tdCount++;
								table.append(tableData.toString());
							}
							table.append("</ul>");
							table.append("</div></div>");
						}
						table.append("</div>");
						out.write(table.toString());
						}
				%>
	</div>
	<c:choose>
	<c:when test="${data != null && not empty data}">
	<div class="row">
	<div class="col-lg-12 col-md-12 text-center" style="padding-top:20px;">
	<span><button type="button" class="btn btn-md btn-submit" value="show" onclick="javascript:myMap();"><spring:message code="activityreport.label.showTrackingMap" /></button></span>
	</div>
	</div>
	</c:when>
	<c:otherwise>
	<div class="row">
	<div class="col-lg-12 col-md-12 text-center" style="padding-top:20px;">
	<spring:message code="common.message.nodatafound" />
	</div>
	</div>
	</c:otherwise>
	</c:choose>
	<div class="row">
    <div class="col-lg-12 col-md-12">
    <p style="font-size:12px"><span class="text-danger"><spring:message code="label.allusertracking.note" /></span></p>
    </div>
    </div>
	</div>
</s:form>

<jsp:include page="../common/googleMapApi3AllUserMap.jsp"></jsp:include>
</div>	
</div>
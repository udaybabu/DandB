<%@page import="in.otpl.dnb.util.URLConstants"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="net.sf.json.JSONObject"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
final Logger log = Logger.getLogger("enquiryLinkReport.jsp");
JSONObject svrObj = null,ccamObj=null;
JSONArray docArray = null;
String personSig ="",correspondentSig="";
String svr = (String) request.getAttribute("svrData");
String ccamData = (String) request.getAttribute("ccamData");
String docData = (String) request.getAttribute("docData");
String uniqueKey = (String) request.getAttribute("uniqueKey");
String enquiryId = (String) request.getAttribute("enquiryId");
String assignmentId = (String) request.getAttribute("assignmentId");
String ccamComments = (String)request.getAttribute("ccamComments");
JSONArray docMediaArray = (JSONArray)request.getAttribute("docMediaArray");

if(svr !=null  && !svr.equals("")) svrObj = JSONObject.fromObject(svr);
if(ccamData !=null && !ccamData.equals("")) ccamObj = JSONObject.fromObject(ccamData);
if(docData !=null && !docData.equals("")) docArray = JSONArray.fromObject(docData);

if(docMediaArray !=null && docMediaArray.size() >0){
	for(int t = 0 ; t < docMediaArray.size() ; t++){
		if(docMediaArray.getJSONObject(t).containsKey("2")){
			JSONArray sigArray = docMediaArray.getJSONObject(t).getJSONArray("2");
			for(int k=0;k < sigArray.size() ; k++){
				JSONObject sigObj = sigArray.getJSONObject(k);
				if(sigObj.containsKey("1")) 
					personSig = sigObj.getString("1");
				else if(sigObj.containsKey("2")) 
					correspondentSig = sigObj.getString("2");
			}
		}
	}
}
%>
<script>
function printFunction() {
	$('#printButton').hide();
	window.print();
}
</script>
<div class="col-lg-12 col-md-12 text-right">
<button id="printButton" onclick="printFunction()"><spring:message code="enquirypool.label.print"/></button>
</div>
<html>
<head>
<style type="text/css">
h2 {
	color:#000;
	text-align: center;
}
h3,h5 {
	color:#444;
}
table tr th{
	background-color:#CCC;color:#444;width:auto;font-size: 11px;border: 1px solid #ddd;padding: 5px;
}
table tr td{
	text-align:center;width:auto;border: 1px solid #ddd;padding:4px;font-size: 10px;
}
</style>
</head>
<body>
<p style="text-align: center;"><img alt="image" src="<%= request.getContextPath() %>/resources/images/logo.png"/></p>
<h2>Enquiry Number - <%=enquiryId%></h2>
<h4>SITE VISIT REPORT</h4>
<%if(svrObj !=null && svrObj.size() > 0){ %>
<table cellpadding="0" cellspacing="0">
<tr><th style="width: 450px;">Details to be obtained during Site Visit of</th><td style="width: 350px;"><%if(svrObj.containsKey("svr-name-1")){%><%=svrObj.getString("svr-name-1") %><%}else{ %><%} %></td></tr>
<tr><th>Complete mailing address of the subject</th><td><%if(svrObj.containsKey("svr-name-2")){%><%=svrObj.getString("svr-name-2") %><%}else{ %><%} %></td></tr>
<tr><th>Subject is located in which area?</th><td><%if(svrObj.containsKey("svr-name-3")){%><%=svrObj.getString("svr-name-3") %><%}else{ %><%} %></td></tr>
<tr><th>Whether the subject is located on the main road or in side lanes</th><td><%if(svrObj.containsKey("svr-name-4")){%><%=svrObj.getString("svr-name-4") %><%}else{ %><%} %></td></tr>
<tr><th>The location is used as</th><td><%if(svrObj.containsKey("svr-name-5")){%><%=svrObj.getString("svr-name-5") %><%}else{ %><%} %></td></tr>
<tr><th>Specify Details If Others</th><td><%if(svrObj.containsKey("svr-name-6")){%><%=svrObj.getString("svr-name-6") %><%}else{ %><%} %></td></tr>
<tr><th>What are the kinds of buildings seen in vicinity</th><td><%if(svrObj.containsKey("svr-name-7")){%><%=svrObj.getString("svr-name-7") %><%}else{ %><%} %></td></tr>
<tr><th>How many floors the building has</th><td><%if(svrObj.containsKey("svr-name-8")){%><%=svrObj.getString("svr-name-8") %><%}else{ %><%} %></td></tr>
<tr><th>The subject is located on which floor of the building</th><td><%if(svrObj.containsKey("svr-name-9")){%><%=svrObj.getString("svr-name-9") %><%}else{ %><%} %></td></tr>
<tr><th>What is the approximate area of the subject's office</th><td><%if(svrObj.containsKey("svr-name-10")){%><%=svrObj.getString("svr-name-10") %><%}else{ %><%} %></td></tr>
<tr><th>Is the proposed location owned in the name of the</th><td><%if(svrObj.containsKey("svr-name-11")){%><%=svrObj.getString("svr-name-11") %><%}else{ %><%} %></td></tr>
<tr><th>Is the office shared by group companies or any other businesses</th><td><%if(svrObj.containsKey("svr-name-12")){%><%=svrObj.getString("svr-name-12") %><%}else{ %><%} %></td></tr>
<tr>
<th>Name of company</th>
<td>
	<%if(svrObj.getString("svr-name-12").equalsIgnoreCase("yes") && svrObj.containsKey("svr-name-13") && !svrObj.getString("svr-name-13").equalsIgnoreCase("")){ %>
				<table><tr>
				<% 
				String values[] = svrObj.getString("svr-name-13").split("~~~");
				   for(String val :values){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
			
		<%} %>
</td>
</tr>
<tr>
<th>Line of business</th>
<td>
	<%if(svrObj.getString("svr-name-12").equalsIgnoreCase("yes") && svrObj.containsKey("svr-name-14") && !svrObj.getString("svr-name-14").equalsIgnoreCase("")){ %>
				<table><tr>
				<% 
				String values[] = svrObj.getString("svr-name-14").split("~~~");
				   for(String val :values){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
			
		<%} %>
</td>
</tr>
<tr><th>Is the office well furnished</th><td><%if(svrObj.containsKey("svr-name-15")){%><%=svrObj.getString("svr-name-15") %><%}else{ %><%} %></td></tr>
<tr><th>Does the owner have separate cabin</th><td><%if(svrObj.containsKey("svr-name-16")){%><%=svrObj.getString("svr-name-16") %><%}else{ %><%} %></td></tr>
<tr><th>Do they have any fire extinguisher</th><td><%if(svrObj.containsKey("svr-name-17")){%><%=svrObj.getString("svr-name-17") %><%}else{ %><%} %></td></tr>
<tr><th>How many fire extinguishers do they have</th><td><%if(svrObj.containsKey("svr-name-18")){%><%=svrObj.getString("svr-name-18") %><%}else{ %><%} %></td></tr>
<tr><th>Is the power supply to the site continuous or are there frequent lapses of power</th><td><%if(svrObj.containsKey("svr-name-19")){%><%=svrObj.getString("svr-name-19") %><%}else{ %><%} %></td></tr>
<tr><th>How many people are working at this location</th><td><%if(svrObj.containsKey("svr-name-20")){%><%=svrObj.getString("svr-name-20") %><%}else{ %><%} %></td></tr>
<tr><th>Is the site equipped with following facilities</th><td><%if(svrObj.containsKey("svr-name-21")){ %>  <%if(!svrObj.getString("svr-name-21").equalsIgnoreCase("null")){ %> <%=svrObj.getString("svr-name-21") %> <%}else{ %><%} %> <%}else {%> <%} %></td></tr>
<tr><th>What facilities are available for disposal of waste generated in the production process</th><td><%if(svrObj.containsKey("svr-name-22")){%><%=svrObj.getString("svr-name-22") %><%}else{ %><%} %></td></tr>
<tr><th>Does the owner also sit at this place</th><td><%if(svrObj.containsKey("svr-name-23")){%><%=svrObj.getString("svr-name-23") %><%}else{ %><%} %></td></tr>
<tr><th>How many years the company is in operation</th><td><%if(svrObj.containsKey("svr-name-24")){%><%=svrObj.getString("svr-name-24") %><%}else{ %><%} %></td></tr>
<tr><th>Since how many years does the subject occupy the current location</th><td><%if(svrObj.containsKey("svr-name-25")){%><%=svrObj.getString("svr-name-25") %><%}else{ %><%} %></td></tr>
<tr><th>Has there been any change of location</th><td><%if(svrObj.containsKey("svr-name-26")){%><%=svrObj.getString("svr-name-26") %><%}else{ %><%} %></td></tr>
<tr><th>Former address</th><td><%if(svrObj.containsKey("svr-name-27")){%><%=svrObj.getString("svr-name-27") %><%}else{ %><%} %></td></tr>
<tr>
	<th>Are the owners involved in any other business</th>
	<td><%if(svrObj.containsKey("svr-name-28")){%><%=svrObj.getString("svr-name-28") %><%}else{ %><%} %></td>
</tr>
<tr>
<th>Name of company</th>
<td>
	<%if(svrObj.getString("svr-name-28").equalsIgnoreCase("yes") && svrObj.containsKey("svr-name-29") && !svrObj.getString("svr-name-29").equalsIgnoreCase("")){ %>
				<table><tr>
				<% 
				String values[] = svrObj.getString("svr-name-29").split("~~~");
				   for(String val :values){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
			
		<%} %>
</td>
</tr>
<tr>
<th>Line of business</th>
<td>
	<%if(svrObj.getString("svr-name-28").equalsIgnoreCase("yes") && svrObj.containsKey("svr-name-30") && !svrObj.getString("svr-name-30").equalsIgnoreCase("")){ %>
				<table><tr>
				<% 
				String values[] = svrObj.getString("svr-name-30").split("~~~");
				   for(String val :values){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
			
		<%} %>
</td>
</tr>
<tr><th>Are there any competitors around</th><td><%if(svrObj.containsKey("svr-name-31")){%><%=svrObj.getString("svr-name-31") %><%}else{ %><%} %></td></tr>
<tr><th>Former address</th><td><%if(svrObj.containsKey("svr-name-32")){%><%=svrObj.getString("svr-name-32") %><%}else{ %><%} %></td></tr>
<tr><th>Date of Site Visit</th><td><%if(svrObj.containsKey("svr-name-33")){%><%=svrObj.getString("svr-name-33") %><%}else{ %><%} %></td></tr>
<tr><th>Are there any boards/name plates outside the premises? If yes, please provide the details found thereon. (eg. Name of any other entity, name of the bank to which the stocks are hypothecated, name of any other dealership/distributorship etc) Name Plate</th><td><%if(svrObj.containsKey("svr-name-34")){%><%=svrObj.getString("svr-name-34") %><%}else{ %><%} %></td></tr>
<tr><th>Was the owner/management present at the time of interview</th><td><%if(svrObj.containsKey("svr-name-35")){%><%=svrObj.getString("svr-name-35") %><%}else{ %><%} %></td></tr>
<tr><th>How will you describe condition of the premise</th><td><%if(svrObj.containsKey("svr-name-36")){%><%=svrObj.getString("svr-name-36") %><%}else{ %><%} %></td></tr>
<tr><th>Any other observations during the site visit & interview</th><td><%if(svrObj.containsKey("svr-name-37")){%><%=svrObj.getString("svr-name-37") %><%}else{ %><%} %></td></tr>
<tr><th>Feedback: Your experience with the client during the site visit</th><td><%if(svrObj.containsKey("svr-name-38")){%><%=svrObj.getString("svr-name-38") %><%}else{ %><%} %></td></tr>
<tr><th>If the person interviewed declines the information, please mention the reason below</th><td><%if(svrObj.containsKey("svr-name-39")){%> <%=svrObj.getString("svr-name-39") %><%}else{ %><%} %></td></tr>
<tr><th>Is there any outstanding Details</th><td><%if(svrObj.containsKey("svr-name-40")){%> <%=svrObj.getString("svr-name-40") %><%}else{ %><%} %></td></tr>
<%if(svrObj.containsKey("svr-name-40")){ if(svrObj.getString("svr-name-40").equalsIgnoreCase("yes")) { %>
<tr><th>Enter Amount</th><td><%if(svrObj.containsKey("svr-name-321")){%> <%=svrObj.getString("svr-name-321") %><%}else{ %><%} %></td></tr>
<%}else if(svrObj.getString("svr-name-40").equalsIgnoreCase("No")){ %>
<tr><th>Comments</th><td><%if(svrObj.containsKey("svr-name-323")){%> <%=svrObj.getString("svr-name-323") %><%}else{ %><%} %></td></tr>
<%}%>
<%}%>
<tr>
	<th>Person Interviewed</th>
	<td>
		<%if(!svrObj.getString("p-name").equals("") || !svrObj.getString("p-desg").equals("") || !personSig.equals("") ){ %>
			<table cellpadding="0" cellspacing="0" style="width: 100%">
				<tr>
					<td>Name</td>
					<td>Designation</td>
					<td>Signature</td>
				</tr>
				<tr>
					<td><%if(svrObj.containsKey("p-name")){%> <%=svrObj.getString("p-name") %><%}else{ %><%} %></td>
					<td><%if(svrObj.containsKey("p-desg")){%> <%=svrObj.getString("p-desg") %><%}else{ %><%} %></td>
					<td><a href="<%=personSig %>" style="color: #1679ad;" target="_blank"><img src="<%=personSig %>" style="width: 50px;"></a></td>
				</tr>
			</table>
		<%}%>
	</td>
</tr>

<tr>
	<th>Correspondent Interviewed</th>
	<td>
		<%if(!svrObj.getString("C-name").equals("") || !svrObj.getString("c-desg").equals("") || !correspondentSig.equals("") ){ %>
			<table cellpadding="0" cellspacing="0" style="width: 100%">
				<tr>
					<td>Name</td>
					<td>Designation</td>
					<td>Signature</td>
				</tr>
				<tr>
					<td><%if(svrObj.containsKey("C-name")){%> <%=svrObj.getString("C-name") %><%}else{ %><%} %></td>
					<td><%if(svrObj.containsKey("c-desg")){%> <%=svrObj.getString("c-desg") %><%}else{ %><%} %></td>
					<td><a href="<%=correspondentSig %>" style="color: #1679ad;" target="_blank"><img src="<%=correspondentSig %>" style="width: 50px;"></a></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>

</table>
<%}else {%>
<table>
<tr>
<td colspan='2' style="text-align: center;color: red;font-size: 10px;">No Data Found</td>
</tr>
</table>
<%} %>
<br/>
  
<h3>CCAM REPORT:</h3><br/>
<%if(ccamObj !=null && ccamObj.size() > 0){ %>
<h3>1. General Information</h3>
<table>
<tr><th>Name of the Entity</th><td style="width: 350px;"><%if(ccamObj.containsKey("giname")){%><%=ccamObj.getString("giname")%><%}else{ %><%} %></td></tr>
<tr><th>Tradestyle</th><td><%if(ccamObj.containsKey("gitradestyle")){%><%=ccamObj.getString("gitradestyle") %><%}else{ %><%} %></td></tr>
<tr><th>Address(Head Office)</th><td><%if(ccamObj.containsKey("giaddress")){%><%=ccamObj.getString("giaddress") %><%}else{ %><%} %></td></tr>
<tr><th>Telephone (Head Office)</th>
<td>
	<%if(!ccamObj.getString("telephone2").equals("") || !ccamObj.getString("telephone3").equals("") || !ccamObj.getString("telephone5").equals("") || ccamObj.getString("telephone6").equals("")) {%>
		<table cellpadding="0" cellspacing="0" style="width: 100%">
			<tr>
				<td>Telephone 1</td>
				<td>Telephone 2</td>
			</tr>
			<tr>
				<td>
					<%if(ccamObj.containsKey("telephone1")){%><%=ccamObj.getString("telephone1") %><%}else{ %><%} %> |
					<%if(ccamObj.containsKey("telephone2")){%><%=ccamObj.getString("telephone2") %><%}else{ %>NA<%} %> |
					<%if(ccamObj.containsKey("telephone3")){%><%=ccamObj.getString("telephone3") %><%}else{ %>NA<%} %>
				</td>
				<td>
					<%if(ccamObj.containsKey("telephone4")){%><%=ccamObj.getString("telephone4") %><%}else{ %><%} %> |
					<%if(ccamObj.containsKey("telephone5")){%><%=ccamObj.getString("telephone5") %><%}else{ %>NA<%} %> |
					<%if(ccamObj.containsKey("telephone6")){%><%=ccamObj.getString("telephone6") %><%}else{ %>NA<%} %>
				</td>
			</tr>
		</table>
	<%} %>	
</td>
</tr>
<tr>
<th>Fax (Head Office)</th>
<td>
	<%if(!ccamObj.getString("telephone8").equals("") || !ccamObj.getString("telephone9").equals("")) {%>
		<%if(ccamObj.containsKey("telephone7")){%><%=ccamObj.getString("telephone7") %><%}else{ %>NA<%} %> |
		<%if(ccamObj.containsKey("telephone8")){%><%=ccamObj.getString("telephone8") %><%}else{ %>NA<%} %> |
		<%if(ccamObj.containsKey("telephone9")){%><%=ccamObj.getString("telephone9") %><%}else{ %>NA<%} %>
	<%} %>	
</td>
</tr>

<tr>
<th>Address(Regd Office)</th>
<td><%if(ccamObj.containsKey("gireof")){%><%=ccamObj.getString("gireof") %><%}else{ %><%} %></td>
</tr>

<tr>
<th>Telephone (Regd Office)</th>
<td>
	<%if(!ccamObj.getString("telephone11").equals("") || !ccamObj.getString("telephone12").equals("")) {%>
		91 |
		<%if(ccamObj.containsKey("telephone11")){%><%=ccamObj.getString("telephone11") %><%}else{%>NA<%} %> |
		<%if(ccamObj.containsKey("telephone12")){%><%=ccamObj.getString("telephone12") %><%}else{%>NA<%} %>
	<%} %>	
</td>
</tr>

<tr>
<th>Fax (Regd Office)</th>
<td>
	<%if(!ccamObj.getString("telephone13").equals("") || !ccamObj.getString("telephone14").equals("")) {%>
		91 |
		<%if(ccamObj.containsKey("telephone13")){%><%=ccamObj.getString("telephone13") %><%}else{%>NA<%} %> |
		<%if(ccamObj.containsKey("telephone14")){%><%=ccamObj.getString("telephone14") %><%}else{%>NA<%} %>
	<%} %>
	
</td>
</tr>
<tr><th> E-mail </th><td><%if(ccamObj.containsKey("gimail")){%><%=ccamObj.getString("gimail") %><%}else{ %><%} %></td></tr>
<tr><th>Website</th><td><%if(ccamObj.containsKey("giws")){%><%=ccamObj.getString("giws") %><%}else{ %><%} %></td></tr>
<tr>
<th>Chief Executive</th>
<td>
<%if(ccamObj.containsKey("gice")){%><%=ccamObj.getString("gice") %><%}else{ %><%} %>
<%if(ccamObj.containsKey("gice1")){%><%=ccamObj.getString("gice1") %><%}else{ %><%} %>
<%if(ccamObj.containsKey("gice2")){%><%=ccamObj.getString("gice2") %><%}else{ %><%} %>
</td>
</tr>
<tr><th>Title</th><td><%if(ccamObj.containsKey("title")){%><%=ccamObj.getString("title") %><%}else{ %><%} %></td></tr>
</table><br/>

<h3>2. Business Activites</h3>
<table>
<tr><th>Line of Business - Please list major line first</th><td style="width: 350px;"><%if(ccamObj.containsKey("lob")){%><%=ccamObj.getString("lob") %><%}else{ %><%} %></td></tr>
<%if(ccamObj.getString("lob").equalsIgnoreCase("others")){ %>
<tr><th>Others specification</th><td><%if(ccamObj.containsKey("ba-of21")){%><%=ccamObj.getString("ba-of21") %><%}else{ %><%} %></td></tr>
<%} %>
<tr><th>Of</th><td><%if(ccamObj.containsKey("baof1")){%><%=ccamObj.getString("baof1") %><%}else{ %><%} %></td></tr>
<tr><th>Line of Business - Please list secondary line</th><td><%if(ccamObj.containsKey("lob-and")){%><%=ccamObj.getString("lob-and") %><%}else{ %><%} %></td></tr>
<%if(ccamObj.getString("lob-and").equalsIgnoreCase("others")){ %>
<tr><th>Others specification</th><td><%if(ccamObj.containsKey("ba-other-name3")){%><%=ccamObj.getString("ba-other-name3") %><%}else{ %><%} %></td></tr>
<%} %>
<tr><th>Of</th><td><%if(ccamObj.containsKey("baof2")){%><%=ccamObj.getString("baof2") %><%}else{ %><%} %></td></tr>
</table><br/>


<h3>3. Number of Employees </h3>
<table>
<tr>
<th>Type of Employees</th>
<th>At Location</th>
<th colspan="2" style="font-size: 13px;">Accross Country (including at location)</th>
</tr>

<tr>
  <th></th>
  <th>Current Year</th>
  <th>Current year</th>
  <th>Previous year</th>
</tr>

   <tr>
	  <td>1. Production Workers</td>
	  <td><%if(ccamObj.containsKey("ne-curr-year")){%><%=ccamObj.getString("ne-curr-year") %><%}else{ %><%} %></td>
	  <td><%if(ccamObj.containsKey("ne-curr-year1")){%><%=ccamObj.getString("ne-curr-year1") %><%}else{ %><%} %></td>
	  <td><%if(ccamObj.containsKey("ne-curr-year1")){%><%=ccamObj.getString("ne-prev-year") %><%}else{ %><%} %></td>
  </tr>
  
  <tr>
	  <td>2. Full Time</td>
	  <td><%if(ccamObj.containsKey("ne-ft-1")){%><%=ccamObj.getString("ne-ft-1") %><%}else{ %><%} %></td>
	  <td><%if(ccamObj.containsKey("ne-ft-2")){%><%=ccamObj.getString("ne-ft-2") %><%}else{ %><%} %></td>
	  <td><%if(ccamObj.containsKey("ne-ft-3")){%><%=ccamObj.getString("ne-ft-3") %><%}else{ %><%} %></td>
  </tr>
  
  <tr>
	  <td>3. Part Time</td>
	  <td><%if(ccamObj.containsKey("ne-pt-1")){%><%=ccamObj.getString("ne-pt-1") %><%}else{ %><%} %></td>
	  <td><%if(ccamObj.containsKey("ne-pt-2")){%><%=ccamObj.getString("ne-pt-2") %><%}else{ %><%} %></td>
	  <td><%if(ccamObj.containsKey("ne-pt-3")){%><%=ccamObj.getString("ne-pt-3") %><%}else{ %><%} %></td>
  </tr>
  
  <tr>
	  <td>4. Seasonal Employees</td>
	  <td><%if(ccamObj.containsKey("ne-se-1")){%><%=ccamObj.getString("ne-se-1") %><%}else{ %><%} %></td>
	  <td><%if(ccamObj.containsKey("ne-se-2")){%><%=ccamObj.getString("ne-se-2") %><%}else{ %><%} %></td>
	  <td><%if(ccamObj.containsKey("ne-se-3")){%><%=ccamObj.getString("ne-se-3") %><%}else{ %><%} %></td>
  </tr>
  
  <tr>
	  <td>Total</td>
	  <td><%if(ccamObj.containsKey("ne-tt-1")){%><%=ccamObj.getString("ne-tt-1") %><%}else{ %><%} %></td>
	  <td><%if(ccamObj.containsKey("ne-tt-2")){%><%=ccamObj.getString("ne-tt-2") %><%}else{ %><%} %></td>
	  <td><%if(ccamObj.containsKey("ne-tt-3")){%><%=ccamObj.getString("ne-tt-3") %><%}else{ %><%} %></td>
  </tr>
  <tr>
  	<th>Is staff shared with Related/ Group Companies ?</th>
  	<td colspan="3"><%if(ccamObj.containsKey("staff-shared")){%><%=ccamObj.getString("staff-shared") %><%}else{ %><%} %></td>
  </tr>
</table><br/>

<h3>4. Purchases</h3>
<table>
<tr><th>Local Source of Raw Materials/ Merchandise</th><td style="width: 350px;"><%if(ccamObj.containsKey("local-international")){%><%=ccamObj.getString("local-international") %><%}else{ %><%} %></td></tr>
<tr><th>International Source of Raw Materials/ Merchandise</th><td><%if(ccamObj.containsKey("local-international1")){%><%=ccamObj.getString("local-international1") %><%}else{ %><%} %></td></tr>
<tr><th>Merchandise/ Raw Marerials are sourced from India</th><td><%if(ccamObj.containsKey("india")){%><%=ccamObj.getString("india") %><%}else{ %><%} %></td></tr>
<tr><th>Imported from Japan</th><td><%if(ccamObj.containsKey("japan")){%><%=ccamObj.getString("japan") %><%}else{ %><%} %></td></tr>
<tr><th>Imported from Taiwan</th><td><%if(ccamObj.containsKey("taiwan")){%><%=ccamObj.getString("taiwan") %><%}else{ %><%} %></td></tr>
<tr><th>Imported from Europe</th><td><%if(ccamObj.containsKey("europe")){%><%=ccamObj.getString("europe") %><%}else{ %><%} %></td></tr>
<tr><th>Imported from Germany</th><td><%if(ccamObj.containsKey("germany")){%><%=ccamObj.getString("germany") %><%}else{ %><%} %></td></tr>
<tr><th>Imported from UK</th><td><%if(ccamObj.containsKey("uk")){%><%=ccamObj.getString("uk") %><%}else{ %><%} %></td></tr>
<tr><th>Imported from Far-East</th><td><%if(ccamObj.containsKey("far-east")){%><%=ccamObj.getString("far-east") %><%}else{ %><%} %></td></tr>
<%-- <tr><th>Imported from Italy</th><td><%if(ccamObj.containsKey("italy")){%><%=ccamObj.getString("italy") %><%}else{ %><%} %></td></tr> --%>
<tr><th>Imported from Australia</th><td><%if(ccamObj.containsKey("australia")){%><%=ccamObj.getString("australia") %><%}else{ %><%} %></td></tr>
<tr><th>Imported from Mid-East</th><td><%if(ccamObj.containsKey("mid-east")){%><%=ccamObj.getString("mid-east") %><%}else{ %><%} %></td></tr>
<tr><th>Imported from China</th><td><%if(ccamObj.containsKey("china")){%><%=ccamObj.getString("china") %><%}else{ %><%} %></td></tr>
<tr><th>Imported from Other Country 1</th><td><%if(ccamObj.containsKey("country2")){%><%=ccamObj.getString("country2") %><%}else{ %><%} %></td></tr>
<tr><th>Imported from USA</th><td><%if(ccamObj.containsKey("usa")){%><%=ccamObj.getString("usa") %><%}else{ %><%} %></td></tr>
<tr><th>Imported from Skorea</th><td><%if(ccamObj.containsKey("skorea")){%><%=ccamObj.getString("skorea") %><%}else{ %><%} %></td></tr>
<tr><th>Imported from Other Country 1</th><td><%if(ccamObj.containsKey("country3")){%><%=ccamObj.getString("country3") %><%}else{ %><%} %></td></tr>
</table><br/>

<h5>Buying Terms (average credit period)</h5>
<table>
<tr><th>Local</th><td style="width: 350px;"><%if(ccamObj.containsKey("local1")){%><%=ccamObj.getString("local1") %><%}else{ %><%} %></td></tr>
<tr><th>Cash/Credit/L/C</th><td><%if(ccamObj.containsKey("local-1")){%><%=ccamObj.getString("local-1") %><%}else{ %><%} %></td></tr>
<tr><th>Overseas</th><td><%if(ccamObj.containsKey("overseas1")){%><%=ccamObj.getString("overseas1") %><%}else{ %><%} %></td></tr>
<tr><th>Cash/Credit/L/C</th><td><%if(ccamObj.containsKey("overseas-1")){%><%=ccamObj.getString("overseas-1") %><%}else{ %><%} %></td></tr>
<tr><th>Others</th><td><%if(ccamObj.containsKey("bt-others")){%><%=ccamObj.getString("bt-others") %><%}else{ %><%} %></td></tr>
</table><br/>

<h3>5. Sales</h3>
<table>
<tr><th>Territory Sold Local</th><td style="width: 350px;"><%if(ccamObj.containsKey("sa-txt1")){%><%=ccamObj.getString("sa-txt1") %><%}else{ %><%} %></td></tr>
<tr><th>Territory Sold International</th><td><%if(ccamObj.containsKey("sa-txt2")){%><%=ccamObj.getString("sa-txt2") %><%}else{ %><%} %></td></tr>
<tr><th>Merchandise/ finished goods are sold in India</th><td><%if(ccamObj.containsKey("sa-txt3")){%><%=ccamObj.getString("sa-txt3") %><%}else{ %><%} %></td></tr>
<tr><th>Exported to Japan</th><td><%if(ccamObj.containsKey("sa-txt4")){%><%=ccamObj.getString("sa-txt4") %><%}else{ %><%} %></td></tr>
<tr><th>Exported to Taiwan</th><td><%if(ccamObj.containsKey("sa-txt5")){%><%=ccamObj.getString("sa-txt5") %><%}else{ %><%} %></td></tr>
<tr><th>Exported to Europe</th><td><%if(ccamObj.containsKey("sa-txt6")){%><%=ccamObj.getString("sa-txt6") %><%}else{ %><%} %></td></tr>
<tr><th>Exported to Germany</th><td><%if(ccamObj.containsKey("sa-germany")){%><%=ccamObj.getString("sa-germany") %><%}else{ %><%} %></td></tr>
<tr><th>Exported to UK</th><td><%if(ccamObj.containsKey("sa-txt7")){%><%=ccamObj.getString("sa-txt7") %><%}else{ %><%} %></td></tr>
<tr><th>Exported to Far-East</th><td><%if(ccamObj.containsKey("sa-txt8")){%><%=ccamObj.getString("sa-txt8") %><%}else{ %><%} %></td></tr>
<%-- <tr><th>Exported to Italy</th><td><%if(ccamObj.containsKey("sa-txt9")){%><%=ccamObj.getString("sa-txt9") %><%}else{ %><%} %></td></tr> --%>
<tr><th>Exported to Australia</th><td><%if(ccamObj.containsKey("sa-txt155")){%><%=ccamObj.getString("sa-txt155") %><%}else{ %><%} %></td></tr>
<tr><th>Exported to Mid-East</th><td><%if(ccamObj.containsKey("sa-txt12")){%><%=ccamObj.getString("sa-txt12") %><%}else{ %><%} %></td></tr>
<tr><th>Exported to China</th><td><%if(ccamObj.containsKey("sa-txt13")){%><%=ccamObj.getString("sa-txt13") %><%}else{ %><%} %></td></tr>
<tr><th>Exported to Other Country 1</th><td><%if(ccamObj.containsKey("sa-txt14")){%><%=ccamObj.getString("sa-txt14") %><%}else{ %><%} %></td></tr>
<tr><th>Exported to USA</th><td><%if(ccamObj.containsKey("sa-txt15")){%><%=ccamObj.getString("sa-txt15") %><%}else{ %><%} %></td></tr>
<tr><th>Exported to Skorea</th><td><%if(ccamObj.containsKey("sa-txt16")){%><%=ccamObj.getString("sa-txt16") %><%}else{ %><%} %></td></tr>
<tr><th>Exported to Other Country 2</th><td><%if(ccamObj.containsKey("sa-txt17")){%><%=ccamObj.getString("sa-txt17") %><%}else{ %><%} %></td></tr>
</table><br/>

<h5>Sellings Terms (average credit period)</h5>
<table>
<tr><th>Local</th><td style="width: 350px;"><%if(ccamObj.containsKey("local2")){%><%=ccamObj.getString("local2") %><%}else{ %><%} %></td></tr>
<tr><th>Cash/Credit/L/C</th><td><%if(ccamObj.containsKey("local-2")){%><%=ccamObj.getString("local-2") %><%}else{ %><%} %></td></tr>
<tr><th>Overseas</th><td><%if(ccamObj.containsKey("overseas2")){%><%=ccamObj.getString("overseas2") %><%}else{ %><%} %></td></tr>
<tr><th>Cash/Credit/L/C</th><td><%if(ccamObj.containsKey("overseas-2")){%><%=ccamObj.getString("overseas-2") %><%}else{ %><%} %></td></tr>
<tr><th>Others</th><td><%if(ccamObj.containsKey("sa-txt20")){%><%=ccamObj.getString("sa-txt20") %><%}else{ %><%} %></td></tr>
</table><br/>

<h3>6. Customer Details</h3>
<table>
<tr><th>Type of Customers</th><td style="width: 350px;"><%if(ccamObj.containsKey("toc")){%><%=ccamObj.getString("toc") %><%}else{ %><%} %></td></tr>
<tr><th>Number of Customer(s) are</th><td><%if(ccamObj.containsKey("cd-txt1")){%><%=ccamObj.getString("cd-txt1") %><%}else{ %><%} %></td></tr>
<tr><th>Or ranges from</th><td><%if(ccamObj.containsKey("cd-txt2")){%><%=ccamObj.getString("cd-txt2") %><%}else{ %><%} %></td></tr>
<tr><th>To</th><td><%if(ccamObj.containsKey("cd-to")){%><%=ccamObj.getString("cd-to") %><%}else{ %><%} %></td></tr>

<tr><td colspan='2' style="color: #1679ad;text-align: center;font-size:13px;">Customer's details</td></tr>
<tr>
	<th>Name of Customer</th>
	<td>
			<%
			if(ccamObj.containsKey("cd-txt3") && !ccamObj.getString("cd-txt3").equalsIgnoreCase("")){ %>
				<table><tr>
				<% 
				String custName[] = ccamObj.getString("cd-txt3").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
			
		<%} %>
	</td>
</tr>
<tr>
	<th>Contact Person</th>
	<td>
		<%if(ccamObj.containsKey("cd-txt4") && !ccamObj.getString("cd-txt4").equalsIgnoreCase("")){ %>
				<table><tr>
				<% String custName[] = ccamObj.getString("cd-txt4").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
			
		<%} %>
	</td>
</tr>
<tr>
	<th>Tel no. (with std code)</th>
	<td>
		<%if(ccamObj.containsKey("cd-txt5") && !ccamObj.getString("cd-txt5").equalsIgnoreCase("")){ %>
				<table><tr>
				<% String custName[] = ccamObj.getString("cd-txt5").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
			
		<%} %>
	</td>
</tr>
<tr>
	<th>% In total sales</th>
	<td>
		<%if(ccamObj.containsKey("cd-txt6") && !ccamObj.getString("cd-txt6").equalsIgnoreCase("")){ %>
				<table><tr>
				<% String custName[] = ccamObj.getString("cd-txt6").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
			
		<%} %>
	</td>
</tr>
<tr><th>Business Relationships</th><td><%if(ccamObj.containsKey("br-agencies")){%><%=ccamObj.getString("br-agencies") %><%}else{ %><%} %></td></tr>
<tr>
	<th>Principal Names</th>
	<td>
		<%if(ccamObj.containsKey("cd-txt7") && !ccamObj.getString("cd-txt7").equalsIgnoreCase("")){ %>
				<table><tr>
				<% String custName[] = ccamObj.getString("cd-txt7").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
			
		<%} %>
	</td>
</tr>
<tr>
	<th>For Which Product</th>
	<td>
		<%if(ccamObj.containsKey("cd-txt8") && !ccamObj.getString("cd-txt8").equalsIgnoreCase("")){ %>
				<table><tr>
				<% String custName[] = ccamObj.getString("cd-txt8").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
			
		<%} %>
	</td>
</tr>
</table>

<h3>7. Seasonal Operations</h3>
<table>
<tr><th>Whether seasonal operations</th><td style="width: 350px;"><%if(ccamObj.containsKey("cd-rad1")){%><%=ccamObj.getString("cd-rad1") %><%}else{ %><%} %></td></tr>
<tr><th>Peak Season</th><td><%if(ccamObj.containsKey("cd-rad2")){%><%=ccamObj.getString("cd-rad2") %><%}else{ %><%} %></td></tr>
<tr><th>Peak Period From</th><td><%if(ccamObj.containsKey("cd-rad3")){%><%=ccamObj.getString("cd-rad3") %><%}else{ %><%} %></td></tr>
<tr><th>Peak Period To</th><td><%if(ccamObj.containsKey("cd-rad4")){%><%=ccamObj.getString("cd-rad4") %><%}else{ %><%} %></td></tr>
<tr><td colspan='2' style="text-align: center;color: #1679ad;font-size: 13px;">Production Details (in case of manufacturer) (Capacity in units per annum)</td></tr>
<tr>
	<th>Type of goods</th>
	<td>
		<%if(ccamObj.containsKey("cd-rad5") && !ccamObj.getString("cd-rad5").equalsIgnoreCase("")){ %>
				<table><tr>
				<% String custName[] = ccamObj.getString("cd-rad5").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
		<%} %>
	</td>
</tr>
<tr>
	<th>Unit of measurements</th>
	<td>
		<%if(ccamObj.containsKey("cd-rad6") && !ccamObj.getString("cd-rad6").equalsIgnoreCase("")){ %>
				<table><tr>
				<% String custName[] = ccamObj.getString("cd-rad6").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>			
		<%} %>
	</td>
</tr>
<tr>
	<th>Installed capacity</th>
	<td>
		<%if(ccamObj.containsKey("cd-rad7") && !ccamObj.getString("cd-rad7").equalsIgnoreCase("")){ %>
				<table><tr>
				<% String custName[] = ccamObj.getString("cd-rad7").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
		<%} %>
	</td>
</tr>
<tr>
	<th>Actual Production</th>
	<td>
		<%if(ccamObj.containsKey("cd-rad8") && !ccamObj.getString("cd-rad8").equalsIgnoreCase("")){ %>
				<table><tr>
				<% String custName[] = ccamObj.getString("cd-rad8").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
		<%} %>
	</td>
</tr>
<tr>
	<th>No. of sheets</th>
	<td>
		<%if(ccamObj.containsKey("cd-rad9") && !ccamObj.getString("cd-rad9").equalsIgnoreCase("")){ %>
				<table><tr>
				<% String custName[] = ccamObj.getString("cd-rad9").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
		<%} %>
	</td>
</tr>
<tr><th>Period</th><td><%if(ccamObj.containsKey("cd-rad10")){%><%=ccamObj.getString("cd-rad10") %><%}else{ %><%} %></td></tr>
<tr><td colspan='2' style="text-align: center;color: #1679ad;font-size: 13px;">Contractor (Applicable for Business Entities executing any civil contracts)</td></tr>
<tr><th>Whether</th><td><%if(ccamObj.containsKey("contractor")){%><%=ccamObj.getString("contractor") %><%}else{ %><%} %></td></tr>
<tr><th>Whether Registered Contractor</th><td><%if(ccamObj.containsKey("Registered-Contractor")){%><%=ccamObj.getString("Registered-Contractor") %><%}else{ %><%} %></td></tr>
<tr><th>Registration Body</th><td><%if(ccamObj.containsKey("cd-rad11")){%><%=ccamObj.getString("cd-rad11") %><%}else{ %><%} %></td></tr>
<tr><th>Category</th><td><%if(ccamObj.containsKey("cd-rad12")){%><%=ccamObj.getString("cd-rad12") %><%}else{ %><%} %></td></tr>
<tr><th>Financial Grade</th><td><%if(ccamObj.containsKey("cd-rad13")){%><%=ccamObj.getString("cd-rad13") %><%}else{ %><%} %></td></tr>

<tr><td colspan='2' style="text-align: center;color: #1679ad;font-size: 13px;">Details of projects completed in the last six months</td></tr>
<tr><th>Project Nature</th><td><%if(ccamObj.containsKey("cd-rad14")){%><%=ccamObj.getString("cd-rad14") %><%}else{ %><%} %></td></tr>
<tr><th>Project Location</th><td><%if(ccamObj.containsKey("cd-rad15")){%><%=ccamObj.getString("cd-rad15") %><%}else{ %><%} %></td></tr>
<tr><th>Completed Date</th><td><%if(ccamObj.containsKey("cd-rad16")){%><%=ccamObj.getString("cd-rad16") %><%}else{ %><%} %></td></tr>

<tr><td colspan='2' style="text-align: center;color: #1679ad;font-size: 13px;">Details of current projects in hand</td></tr>
<tr><th>Project Nature</th><td><%if(ccamObj.containsKey("cd-rad17")){%><%=ccamObj.getString("cd-rad17") %><%}else{ %><%} %></td></tr>
<tr><th>Project Location</th><td><%if(ccamObj.containsKey("cd-rad18")){%><%=ccamObj.getString("cd-rad18") %><%}else{ %><%} %></td></tr>
<tr><th>Completed Date</th><td><%if(ccamObj.containsKey("cd-rad19")){%><%=ccamObj.getString("cd-rad19") %><%}else{ %><%} %></td></tr>
</table><br/>

<h3>8. Tax Incentives</h3>
<table>
<tr><th>Tax Incentives</th><td style="width: 350px;"><%if(ccamObj.containsKey("tax-incentive")){%><%=ccamObj.getString("tax-incentive") %><%}else{ %><%} %></td></tr>
<tr><th>Tax Incentives Others</th><td><%if(ccamObj.containsKey("ti-txt1")){%><%=ccamObj.getString("ti-txt1") %><%}else{ %><%} %></td></tr>
<tr><td colspan='2' style="font-size: 13px;color: #1679ad;text-align: center;">ISO Certificates</td></tr>
<tr><th>Number</th><td><%if(ccamObj.containsKey("ti-txt2")){%><%=ccamObj.getString("ti-txt2") %><%}else{ %><%} %></td></tr>
<tr><th>Start Date</th><td><%if(ccamObj.containsKey("ti-dat1")){%><%=ccamObj.getString("ti-dat1") %><%}else{ %><%} %></td></tr>
<tr><th>Expiry Date</th><td><%if(ccamObj.containsKey("ti-dat2")){%><%=ccamObj.getString("ti-dat2") %><%}else{ %><%} %></td></tr>
<tr><td colspan='2' style="font-size: 13px;color: #1679ad;text-align: center;">Import & Export value (For last three years)</td></tr>
<tr>
<th>Year Values</th>
	<td>
		<%if(!ccamObj.getString("ti-txt01").equals("") || !ccamObj.getString("ti-txt01").equals("") || !ccamObj.getString("ti-txt01").equals("")) {%>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("ti-txt01")){%><%=ccamObj.getString("ti-txt01") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("ti-txt02")){%><%=ccamObj.getString("ti-txt02") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("ti-txt03")){%><%=ccamObj.getString("ti-txt03") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
<th>FOB value of exports</th>
	<td>
		<%if(!ccamObj.getString("ti-txt3").equals("") || !ccamObj.getString("ti-txt4").equals("") || !ccamObj.getString("ti-txt5").equals("")) {%>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("ti-txt3")){%><%=ccamObj.getString("ti-txt3") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("ti-txt4")){%><%=ccamObj.getString("ti-txt4") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("ti-txt5")){%><%=ccamObj.getString("ti-txt5") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>CIF value of imports</th>
	<td>
		<%if(!ccamObj.getString("ti-txt6").equals("") || !ccamObj.getString("ti-txt7").equals("") || !ccamObj.getString("ti-txt8").equals("")) {%>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("ti-txt6")){%><%=ccamObj.getString("ti-txt6") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("ti-txt7")){%><%=ccamObj.getString("ti-txt7") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("ti-txt8")){%><%=ccamObj.getString("ti-txt8") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr><td colspan='2' style="font-size: 13px;color: #1679ad;text-align: center;">Insurance</td></tr>
<tr>
	<th>Name of the insurance company</th>
	<td>
		<%if(ccamObj.containsKey("ti-txt9") && !ccamObj.getString("ti-txt9").equalsIgnoreCase("")){ %>
				<table><tr>
				<% String custName[] = ccamObj.getString("ti-txt9").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
		<%} %>
	</td>
</tr>
<tr>
	<th>Type of insurance</th>
	<td>
		<%if(ccamObj.containsKey("ti-txt10") && !ccamObj.getString("ti-txt10").equalsIgnoreCase("")){ %>
				<table><tr>
				<% String custName[] = ccamObj.getString("ti-txt10").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
		<%} %>
	</td>
</tr>
<tr>
	<th>Item insured</th>
	<td>
		<%if(ccamObj.containsKey("ti-txt11") && !ccamObj.getString("ti-txt11").equalsIgnoreCase("")){ %>
				<table><tr>
				<% String custName[] = ccamObj.getString("ti-txt11").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
		<%} %>
	</td>
</tr>
<tr>
	<th>Sum insured in lakhs</th>
	<td>
		<%if(ccamObj.containsKey("ti-txt12") && !ccamObj.getString("ti-txt12").equalsIgnoreCase("")){ %>
				<table><tr>
				<% String custName[] = ccamObj.getString("ti-txt12").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
		<%} %>
	</td>
</tr>
<tr>
	<th>Valid from</th>
	<td>
		<%if(ccamObj.containsKey("ti-txt13") && !ccamObj.getString("ti-txt13").equalsIgnoreCase("")){ %>
				<table><tr>
				<% String custName[] = ccamObj.getString("ti-txt13").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
		<%} %>
	</td>
</tr>
<tr>
	<th>Valid upto</th>
	<td>
		<%if(ccamObj.containsKey("ti-txt14") && !ccamObj.getString("ti-txt14").equalsIgnoreCase("")){ %>
				<table><tr>
				<% String custName[] = ccamObj.getString("ti-txt14").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
		<%} %>
	</td>
</tr>
<tr><td colspan='2' style="font-size: 13px;text-align:center;color: #1679ad;">Location Details</td></tr>
<tr>
	<th>Registered Office</th>
	<td style="padding: 0px;">
		<table cellpadding="0" cellspacing="0" style="width: 100%">
			<tr>
				<td>Address</td>
				<td>Rented/ Owned/ Leased</td>
				<td>Area</td>
			</tr>
			<tr>
				<td><%if(ccamObj.containsKey("ti-txt15")){%><%=ccamObj.getString("ti-txt15") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt16")){%><%=ccamObj.getString("ti-txt16") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt17")){%><%=ccamObj.getString("ti-txt17") %><%}else{ %><%} %></td>
			</tr>
			<tr>
				<td><%if(ccamObj.containsKey("ti-txt18")){%><%=ccamObj.getString("ti-txt18") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt19")){%><%=ccamObj.getString("ti-txt19") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt20")){%><%=ccamObj.getString("ti-txt20") %><%}else{ %><%} %></td>
			</tr>
			<tr>
				<td><%if(ccamObj.containsKey("ti-txt21")){%><%=ccamObj.getString("ti-txt21") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt22")){%><%=ccamObj.getString("ti-txt22") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt23")){%><%=ccamObj.getString("ti-txt23") %><%}else{ %><%} %></td>
			</tr>
			<tr>
				<td><%if(ccamObj.containsKey("ti-txt24")){%><%=ccamObj.getString("ti-txt24") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt25")){%><%=ccamObj.getString("ti-txt25") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt26")){%><%=ccamObj.getString("ti-txt26") %><%}else{ %><%} %></td>
			</tr>
		</table>
	</td>
</tr>
<tr>
	<th>Head Office</th>
	<td style="padding: 0px;">
		<table cellpadding="0" cellspacing="0" style="width: 100%">
			<tr>
				<td>Address</td>
				<td>Rented/ Owned/ Leased</td>
				<td>Area</td>
			</tr>
			<tr>
				<td><%if(ccamObj.containsKey("ti-txt27")){%><%=ccamObj.getString("ti-txt27") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt28")){%><%=ccamObj.getString("ti-txt28") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt29")){%><%=ccamObj.getString("ti-txt29") %><%}else{ %><%} %></td>
			</tr>
			<tr>
				<td><%if(ccamObj.containsKey("ti-txt30")){%><%=ccamObj.getString("ti-txt30") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt31")){%><%=ccamObj.getString("ti-txt31") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt32")){%><%=ccamObj.getString("ti-txt32") %><%}else{ %><%} %></td>
			</tr>
			<tr>
				<td><%if(ccamObj.containsKey("ti-txt33")){%><%=ccamObj.getString("ti-txt33") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt34")){%><%=ccamObj.getString("ti-txt34") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt35")){%><%=ccamObj.getString("ti-txt35") %><%}else{ %><%} %></td>
			</tr>
			<tr>
				<td><%if(ccamObj.containsKey("ti-txt36")){%><%=ccamObj.getString("ti-txt36") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt37")){%><%=ccamObj.getString("ti-txt37") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("hd-txt38")){%><%=ccamObj.getString("hd-txt38") %><%}else{ %><%} %></td>
			</tr>
		</table>
	</td>
</tr>
<tr>
	<th>Branches</th>
	<td style="padding: 0px;">
		<table cellpadding="0" cellspacing="0" style="width: 100%">
			<tr>
				<td>Address</td>
				<td>Rented/ Owned/ Leased</td>
				<td>Area</td>
			</tr>
			<tr>
				<td><%if(ccamObj.containsKey("ti-txt38")){%><%=ccamObj.getString("ti-txt38") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt39")){%><%=ccamObj.getString("ti-txt39") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt40")){%><%=ccamObj.getString("ti-txt40") %><%}else{ %><%} %></td>
			</tr>
			<tr>
				<td><%if(ccamObj.containsKey("ti-txt41")){%><%=ccamObj.getString("ti-txt41") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt42")){%><%=ccamObj.getString("ti-txt42") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt43")){%><%=ccamObj.getString("ti-txt43") %><%}else{ %><%} %></td>
			</tr>
			<tr>
				<td><%if(ccamObj.containsKey("ti-txt44")){%><%=ccamObj.getString("ti-txt44") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt45")){%><%=ccamObj.getString("ti-txt45") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt46")){%><%=ccamObj.getString("ti-txt46") %><%}else{ %><%} %></td>
			</tr>
			<tr>
				<td><%if(ccamObj.containsKey("ti-txt47")){%><%=ccamObj.getString("ti-txt47") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt48")){%><%=ccamObj.getString("ti-txt48") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt49")){%><%=ccamObj.getString("ti-txt49") %><%}else{ %><%} %></td>
			</tr>
		</table>
	</td>
</tr>
<tr>
	<th>Divisions</th>
	<td style="padding: 0px;">
		<table cellpadding="0" cellspacing="0" style="width: 100%">
			<tr>
				<td>Address</td>
				<td>Rented/ Owned/ Leased</td>
				<td>Area</td>
			</tr>
			<tr>
				<td><%if(ccamObj.containsKey("ti-txt50")){%><%=ccamObj.getString("ti-txt50") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt51")){%><%=ccamObj.getString("ti-txt51") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt52")){%><%=ccamObj.getString("ti-txt52") %><%}else{ %><%} %></td>
			</tr>
			<tr>
				<td><%if(ccamObj.containsKey("ti-txt53")){%><%=ccamObj.getString("ti-txt53") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt54")){%><%=ccamObj.getString("ti-txt54") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt55")){%><%=ccamObj.getString("ti-txt55") %><%}else{ %><%} %></td>
			</tr>
			<tr>
				<td><%if(ccamObj.containsKey("ti-txt56")){%><%=ccamObj.getString("ti-txt56") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt57")){%><%=ccamObj.getString("ti-txt57") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt58")){%><%=ccamObj.getString("ti-txt58") %><%}else{ %><%} %></td>
			</tr>
			<tr>
				<td><%if(ccamObj.containsKey("ti-txt59")){%><%=ccamObj.getString("ti-txt59") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt60")){%><%=ccamObj.getString("ti-txt60") %><%}else{ %><%} %></td>
				<td><%if(ccamObj.containsKey("ti-txt61")){%><%=ccamObj.getString("ti-txt61") %><%}else{ %><%} %></td>
			</tr>
		</table>
	</td>
</tr>

<tr><th>Annexures</th><td><%if(ccamObj.containsKey("ti-txt62")){%><%=ccamObj.getString("ti-txt62") %><%}else{ %><%} %></td></tr>
<tr><th>Bankers Details (as on date)</th><td><%if(ccamObj.containsKey("ti-txt63")){%><%=ccamObj.getString("ti-txt63") %><%}else{ %><%} %></td></tr>
<tr><th>Rs. in</th><td><%if(ccamObj.containsKey("ti-txt64")){%><%=ccamObj.getString("ti-txt64") %><%}else{ %><%} %></td></tr>
<tr>
	<th>Name & address of bank</th>
	<td>
		<%if(ccamObj.containsKey("ti-txt65") && !ccamObj.getString("ti-txt65").equalsIgnoreCase("")){ %>
				<table><tr>
				<% String custName[] = ccamObj.getString("ti-txt65").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
		<%} %>
	</td>
</tr>
<tr>
	<th>Type of facilities</th>
	<td>
		<%if(ccamObj.containsKey("ti-txt66") && !ccamObj.getString("ti-txt66").equalsIgnoreCase("")){ %>
				<table><tr>
				<% String custName[] = ccamObj.getString("ti-txt66").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
		<%} %>
	</td>
</tr>
<tr>
	<th>Amount sanctioned</th>
	<td>
		<%if(ccamObj.containsKey("ti-txt67") && !ccamObj.getString("ti-txt67").equalsIgnoreCase("")){ %>
				<table><tr>
				<% String custName[] = ccamObj.getString("ti-txt67").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
		<%} %>
	</td>
</tr>
<tr>
	<th>Amount disbursed</th>
	<td>
		<%if(ccamObj.containsKey("ti-txt68") && !ccamObj.getString("ti-txt68").equalsIgnoreCase("")){ %>
				<table><tr>
				<% String custName[] = ccamObj.getString("ti-txt68").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
		<%} %>
	</td>
</tr>
<tr>
	<th>Outstanding</th>
	<td>
		<%if(ccamObj.containsKey("ti-txt69") && !ccamObj.getString("ti-txt69").equalsIgnoreCase("")){ %>
				<table><tr>
				<% String custName[] = ccamObj.getString("ti-txt69").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
		<%} %>
	</td>
</tr>

<tr><td style="font-size: 13px;color: #1679ad;text-align: center;" colspan="2">Principals</td></tr>
<tr><th>Prefix</th><td><%if(ccamObj.containsKey("prefix1")){%><%=ccamObj.getString("prefix1") %><%}else{ %><%} %></td></tr>
<tr><th>First Name</th><td><%if(ccamObj.containsKey("ti-txt70")){%><%=ccamObj.getString("ti-txt70") %><%}else{ %><%} %></td></tr>
<tr><th>Middle Name</th><td><%if(ccamObj.containsKey("ti-txt701")){%><%=ccamObj.getString("ti-txt701") %><%}else{ %><%} %></td></tr>
<tr><th>Last Name</th><td><%if(ccamObj.containsKey("ti-txt702")){%><%=ccamObj.getString("ti-txt702") %><%}else{ %><%} %></td></tr>
<tr><th>Title/ (Designation)</th><td><%if(ccamObj.containsKey("ti-txt71")){%><%=ccamObj.getString("ti-txt71") %><%}else{ %><%} %></td></tr>
</table><br/>

<h3>9. Background Information on Chief Executive Officer/ Directors</h3>
<table>
<tr><td colspan="2" style="font-size: 13px;text-align: center;color: #1679ad;">Background Information 1</td></tr>
<tr><th>Prefix</th><td style="width: 350px;"><%if(ccamObj.containsKey("bi1")){%><%=ccamObj.getString("bi1") %><%}else{ %><%} %></td></tr>
<tr><th>First Name</th><td><%if(ccamObj.containsKey("bi1-f")){%><%=ccamObj.getString("bi1-f") %><%}else{ %><%} %></td></tr>
<tr><th>Middle Name</th><td><%if(ccamObj.containsKey("bi101")){%><%=ccamObj.getString("bi101") %><%}else{ %><%} %></td></tr>
<tr><th>Last Name</th><td><%if(ccamObj.containsKey("bi100")){%><%=ccamObj.getString("bi100") %><%}else{ %><%} %></td></tr>
<tr><th>Designation</th><td><%if(ccamObj.containsKey("bi-designation")){%><%=ccamObj.getString("bi-designation") %><%}else{ %><%} %></td></tr>
<tr><th>Address</th><td><%if(ccamObj.containsKey("bi3")){%><%=ccamObj.getString("bi3") %></td><%}else{ %><%} %></tr>
<tr><th>Telephone</th><td><%if(ccamObj.containsKey("bi4")){%><%=ccamObj.getString("bi4") %></td><%}else{ %><%} %></tr>
<tr><th>Email</th><td><%if(ccamObj.containsKey("bi5")){%><%=ccamObj.getString("bi5") %><%}else{ %><%} %></td></tr>
<tr><th>Paasport No.</th><td><%if(ccamObj.containsKey("bi6")){%><%=ccamObj.getString("bi6") %><%}else{ %><%} %></td></tr>
<tr><th>Date of Birth</th><td><%if(ccamObj.containsKey("bi7")){%><%=ccamObj.getString("bi7") %></td><%}else{ %><%} %></tr>
<tr><th>Educational Qualifications</th><td><%if(ccamObj.containsKey("bi8")){%><%=ccamObj.getString("bi8") %><%}else{ %><%} %></td></tr>
<tr><th>Date of Joining the Subject </th><td><%if(ccamObj.containsKey("bi9")){%><%=ccamObj.getString("bi9") %></td><%}else{ %><%} %></tr>
<tr><th>Date on which assumed the current position </th><td><%if(ccamObj.containsKey("bi10-d")){%><%=ccamObj.getString("bi10-d") %><%}else { %> <%} %></td></tr>
<tr><th>Whether Founder of Subject </th><td><%if(ccamObj.containsKey("founder")){%><%=ccamObj.getString("founder") %><%}else{ %><%} %></td></tr>
<tr><th>Years in Related Fields </th><td><%if(ccamObj.containsKey("bi11")){%><%=ccamObj.getString("bi11") %><%}else{ %><%} %></td></tr>
<tr><th>Total Experience </th><td><%if(ccamObj.containsKey("bi12")){%><%=ccamObj.getString("bi12") %></td><%}else{ %><%} %></tr>
<tr><th>Whether active in day to day operations/ business</th><td><%if(ccamObj.containsKey("d-to-d")){%><%=ccamObj.getString("d-to-d") %><%}else{ %><%} %></td></tr>
<tr><th>Whether Retired or Resigned</th><td><%if(ccamObj.containsKey("bi13")){%><%=ccamObj.getString("bi13") %><%}else{ %><%} %></td></tr>
<tr><td colspan="2" style="font-size: 13px;text-align: center;">Past Work Expereince</td></tr>
<tr><th>Name of the Company</th><td><%if(ccamObj.containsKey("bi14")){%><%=ccamObj.getString("bi14") %><%}else{ %><%} %></td></tr>
<tr><th>Last Position Held</th><td><%if(ccamObj.containsKey("bi15")){%><%=ccamObj.getString("bi15") %><%}else{ %><%} %></td></tr>
<tr><th>Period</th><td><%if(ccamObj.containsKey("bi16")){%><%=ccamObj.getString("bi16") %><%}else{ %><%} %></td></tr>
<tr><th>Other Directorships (Please give particulars)</th><td><%if(ccamObj.containsKey("bi17")){%><%=ccamObj.getString("bi17") %><%}else{ %><%} %></td></tr>
</table><br/>

<table>
<tr><td colspan="2" style="font-size: 13px;text-align: center;color: #1679ad;">Background Information 2</td></tr>
<tr><th>Prefix</th><td style="width: 350px;"><%if(ccamObj.containsKey("bi12p")){%><%=ccamObj.getString("bi12p") %><%}else{ %><%} %></td></tr>
<tr><th>First Name</th><td><%if(ccamObj.containsKey("bi1-f2")){%><%=ccamObj.getString("bi1-f2") %><%}else{ %><%} %></td></tr>
<tr><th>Middle Name</th><td><%if(ccamObj.containsKey("bi1012")){%><%=ccamObj.getString("bi1012") %><%}else{ %><%} %></td></tr>
<tr><th>Last Name</th><td><%if(ccamObj.containsKey("bi1002")){%><%=ccamObj.getString("bi1002") %><%}else{ %><%} %></td></tr>
<tr><th>Designation</th><td><%if(ccamObj.containsKey("bi-designation2")){%><%=ccamObj.getString("bi-designation2") %><%}else{ %><%} %></td></tr>
<tr><th>Address</th><td><%if(ccamObj.containsKey("bi32")){%><%=ccamObj.getString("bi32") %></td><%}else{ %><%} %></tr>
<tr><th>Telephone</th><td><%if(ccamObj.containsKey("bi42")){%><%=ccamObj.getString("bi42") %></td><%}else{ %><%} %></tr>
<tr><th>Email</th><td><%if(ccamObj.containsKey("bi52")){%><%=ccamObj.getString("bi52") %><%}else{ %><%} %></td></tr>
<tr><th>Paasport No.</th><td><%if(ccamObj.containsKey("bi62")){%><%=ccamObj.getString("bi62") %><%}else{ %><%} %></td></tr>
<tr><th>Date of Birth</th><td><%if(ccamObj.containsKey("bi72")){%><%=ccamObj.getString("bi72") %></td><%}else{ %><%} %></tr>
<tr><th>Educational Qualifications</th><td><%if(ccamObj.containsKey("bi82")){%><%=ccamObj.getString("bi82") %><%}else{ %><%} %></td></tr>
<tr><th>Date of Joining the Subject </th><td><%if(ccamObj.containsKey("bi92")){%><%=ccamObj.getString("bi92") %></td><%}else{ %><%} %></tr>
<tr><th>Date on which assumed the current position </th><td><%if(ccamObj.containsKey("bi102")){%><%=ccamObj.getString("bi102") %><%}else { %> <%} %></td></tr>
<tr><th>Whether Founder of Subject </th><td><%if(ccamObj.containsKey("founder2")){%><%=ccamObj.getString("founder2") %><%}else{ %><%} %></td></tr>
<tr><th>Years in Related Fields </th><td><%if(ccamObj.containsKey("bi112")){%><%=ccamObj.getString("bi112") %><%}else{ %><%} %></td></tr>
<tr><th>Total Experience </th><td><%if(ccamObj.containsKey("bi122")){%><%=ccamObj.getString("bi122") %></td><%}else{ %><%} %></tr>
<tr><th>Whether active in day to day operations/ business</th><td><%if(ccamObj.containsKey("d-to-d2")){%><%=ccamObj.getString("d-to-d2") %><%}else{ %><%} %></td></tr>
<tr><th>Whether Retired or Resigned</th><td><%if(ccamObj.containsKey("bi132")){%><%=ccamObj.getString("bi132") %><%}else{ %><%} %></td></tr>
<tr><td colspan="2" style="font-size: 13px;text-align: center;">Past Work Expereince</td></tr>
<tr><th>Name of the Company</th><td><%if(ccamObj.containsKey("bi142")){%><%=ccamObj.getString("bi142") %><%}else{ %><%} %></td></tr>
<tr><th>Last Position Held</th><td><%if(ccamObj.containsKey("bi152")){%><%=ccamObj.getString("bi152") %><%}else{ %><%} %></td></tr>
<tr><th>Period</th><td><%if(ccamObj.containsKey("bi162")){%><%=ccamObj.getString("bi162") %><%}else{ %><%} %></td></tr>
<tr><th>Other Directorships (Please give particulars)</th><td><%if(ccamObj.containsKey("bi172")){%><%=ccamObj.getString("bi172") %><%}else{ %><%} %></td></tr>
</table><br/>

<h3>10. Organisation</h3>
<table>
<tr><th>Legal Status</th><td><%if(ccamObj.containsKey("legal")){%><%=ccamObj.getString("legal") %><%}else{ %><%} %></td></tr>
<tr><th>Date of Incorporated/ Established</th><td><%=ccamObj.getString("org-txt1") %></td></tr>
<tr><th>Incorporation / Registration No.</th><td><%=ccamObj.getString("org-txt2") %></td></tr>
<tr><th>In the State of</th><td><%=ccamObj.getString("org-txt3") %></td></tr>
<tr><th>Previous Legal Structure</th><td><%=ccamObj.getString("org-txt4") %></td></tr>
<tr><th>Legal Structure Change date</th><td><%=ccamObj.getString("ls") %></td></tr>
<tr><th>Previous Name of the Company</th><td><%=ccamObj.getString("ls1") %></td></tr>
<tr><th>Company name change date</th><td><%=ccamObj.getString("changedate") %></td></tr>

<tr><td colspan='2' style="font-size: 13px;text-align: center;color: #1679ad;">Change of State of Registration</td></tr>

<tr><th>Date of Change</th><td><%=ccamObj.getString("org-txt5") %></td></tr>
<tr><th>SSI</th><td><%=ccamObj.getString("org-rad1") %></td></tr>
<tr><th>SSI Reg No</th><td><%=ccamObj.getString("org-txt8") %></td></tr>

<tr><td colspan='2' style="font-size: 13px;text-align: center;color: #1679ad;">Capital Structure</td></tr>
<tr>
	<th>Authorized Capital</th>
	<td>
		<%if(!ccamObj.getString("org-ch7").equals("") || !ccamObj.getString("org-txt9").equals("") || !ccamObj.getString("org-txt10").equals("")){ %>
			<table>
				<tr>
					<td>Value</td>
					<td>At par value of</td>
					<td>Per equity share</td>
				</tr>
				<tr>
					<td><%if(ccamObj.containsKey("org-ch7")){ %><%=ccamObj.getString("org-ch7") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("org-txt9")){ %><%=ccamObj.getString("org-txt9") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("org-txt10")){ %><%=ccamObj.getString("org-txt10") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>
	</td>
</tr>
<tr>
	<th> Issued Capital - Equity Shares</th>
	<td>
		<%if(!ccamObj.getString("org-ch8").equals("") || !ccamObj.getString("org-txt11").equals("") || !ccamObj.getString("org-txt12").equals("")){ %>
			<table>
				<tr>
					<td>Value</td>
					<td>At par value of</td>
					<td>Per equity share</td>
				</tr>
				<tr>
					<td><%if(ccamObj.containsKey("org-ch8")){ %><%=ccamObj.getString("org-ch8") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("org-txt11")){ %><%=ccamObj.getString("org-txt11") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("org-txt12")){ %><%=ccamObj.getString("org-txt12") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th> Issued Capital - Preference Shares</th>
	<td>
		<%if(!ccamObj.getString("org-ch9").equals("") || !ccamObj.getString("org-txt13").equals("") || !ccamObj.getString("org-txt14").equals("")){ %>
			<table>
				<tr>
					<td>Value</td>
					<td>At par value of</td>
					<td>Per equity share</td>
				</tr>
				<tr>
					<td><%if(ccamObj.containsKey("org-ch9")){ %><%=ccamObj.getString("org-ch9") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("org-txt13")){ %><%=ccamObj.getString("org-txt13") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("org-txt14")){ %><%=ccamObj.getString("org-txt14") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th> Paid Up Capital – Equity Shares</th>
	<td>
		<%if(!ccamObj.getString("org-ch10").equals("") || !ccamObj.getString("org-txt15").equals("") || !ccamObj.getString("org-txt16").equals("")){ %>
			<table>
				<tr>
					<td>Value</td>
					<td>At par value of</td>
					<td>Per equity share</td>
				</tr>
				<tr>
					<td><%if(ccamObj.containsKey("org-ch10")){ %><%=ccamObj.getString("org-ch10") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("org-tx15")){ %><%=ccamObj.getString("org-txt15") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("org-txt16")){ %><%=ccamObj.getString("org-txt16") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr><th>As On</th><td><%if(ccamObj.containsKey("org-txt17")){ %><%=ccamObj.getString("org-txt17") %><%}else{ %><%} %></td></tr>
<tr><th>Change in Capital</th><td><%if(ccamObj.containsKey("org-txt18")){ %><%=ccamObj.getString("org-txt18") %><%}else{ %><%} %></td></tr>
<tr><th>Change in Capital – Details</th><td><%if(ccamObj.containsKey("org-txt18-ccd")){ %><%=ccamObj.getString("org-txt18-ccd") %><%}else{ %><%} %></td></tr>

<tr><td colspan='2' style="font-size: 13px;color: #1679ad;text-align: center;">Listing Details (in case of Public Companies)</td></tr>
<tr>
	<th>Name of the Stock Exchange on which Listed</th>
	<td>
		<%if(ccamObj.containsKey("stock-exchange") && !ccamObj.getString("stock-exchange").equalsIgnoreCase("")){ %>
				<table><tr>
				<% String custName[] = ccamObj.getString("stock-exchange").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
		<%} %>
	</td>
</tr>
<tr>
	<th>Date listed</th>
	<td>
		<%if(ccamObj.containsKey("org-txt20") && !ccamObj.getString("org-txt20").equalsIgnoreCase("")){ %>
				<table><tr>
				<% String custName[] = ccamObj.getString("org-txt20").split("~~~");
				   for(String val :custName){%>
					  <td><%=val %></td>
				   <%}%>
				   </tr></table>
		<%}else{ %>
		<%} %>
	</td>
</tr>
<tr><th>Any proposed delisting</th><td><%=ccamObj.getString("org-txt21") %></td></tr>
<tr><td colspan='2' style="font-size: 13px;color: #1679ad;text-align: center;">Shareholding Pattern</td></tr>
<tr>
	<%if(ccamObj.containsKey("org-txt222") && !ccamObj.getString("org-txt222").equalsIgnoreCase("")){ 
		String prefix[] = ccamObj.getString("prefix102").split("~~~");
		String name[] = ccamObj.getString("org-txt222").split("~~~");
		String nationality[] = ccamObj.getString("org-txt252").split("~~~");
		String ord[] = ccamObj.getString("org-txt262").split("~~~");
		String pref[] = ccamObj.getString("org-txt272").split("~~~");
		String totOrd[] = ccamObj.getString("org-txt282").split("~~~");
		String totPref[] = ccamObj.getString("org-txt292").split("~~~");
	%>
			<th>Shareholder</th>
			<td>
				<%if(name.length > 0){ %>
					<table cellpadding="0" cellspacing="0">
						<tr>
							<td>Prefix</td>
							<td>Name</td>
							<td>Nationality</td>
							<td>Ordinary Shares</td>
							<td>Preference Shares</td>
							<td>Ordinary Equity</td>
							<td>Preference Equity</td>
						</tr>
						<%for(int k = 0 ; k < name.length ; k++){ %>
						<tr>
							<td><%=prefix[k] %></td>
							<td><%=name[k] %></td>
							<td><%=nationality[k] %></td>
							<td><%=ord[k] %></td>
							<td><%=pref[k] %></td>
							<td><%=totOrd[k]%></td>
							<td><%=totPref[k] %></td>
						</tr>
						<%}%>
					</table>
				<%} %>
		   </td>
	<%}else{%>
		<th>Shareholder</th><td></td>
	<%} %>
</tr>
<tr><th>As On</th><td><%if(ccamObj.containsKey("org-txt30")){%><%=ccamObj.getString("org-txt30") %><%}else{ %><%} %></td></tr>
<tr><td colspan='2' style="text-align: center;color: #1679ad;font-size: 13px;">Other Details</td></tr>
<tr><th>Last AGM Date</th><td><%if(ccamObj.containsKey("org-txt31")){%><%=ccamObj.getString("org-txt31") %><%}else{ %><%} %></td></tr>
<tr><th>Last Annual Return Date filed</th><td><%if(ccamObj.containsKey("org-txt32")){%><%=ccamObj.getString("org-txt32") %><%}else{ %><%} %></td></tr>
<tr><th>Last Financial Statement date</th><td><%if(ccamObj.containsKey("org-txt33")){%><%=ccamObj.getString("org-txt33") %><%}else{ %><%} %></td></tr>
<tr><th>Dormant Period</th><td><%if(ccamObj.containsKey("org-txt34")){%><%=ccamObj.getString("org-txt34") %><%}else{ %><%} %></td></tr>
<tr><td colspan='2' style="text-align: center;color: #1679ad;font-size: 13px;">Business Restructuring</td></tr>
<tr><th>Nature of Restructuring</th><td><%if(ccamObj.containsKey("restructuring")){%><%=ccamObj.getString("restructuring") %><%}else{ %><%} %></td></tr>
<tr><th>If others Selected</th><td><%if(ccamObj.containsKey("org-txt35")){%><%=ccamObj.getString("org-txt35") %><%}else{ %><%} %></td></tr>
<tr><th>Provide details</th><td><%if(ccamObj.containsKey("org-txt36")){%><%=ccamObj.getString("org-txt36") %><%}else{ %><%} %></td></tr>
<tr><td colspan='2' style="text-align: center;color: #1679ad;font-size: 13px;">Auditors</td></tr>
<tr><th>Name of Auditor/s</th><td><%if(ccamObj.containsKey("org-txt37")){%><%=ccamObj.getString("org-txt37") %><%}else{ %><%} %></td></tr>
<tr><th>Address of Auditor/s</th><td><%if(ccamObj.containsKey("org-txt370")){%><%=ccamObj.getString("org-txt370") %><%}else{ %><%} %></td></tr>
<tr><td colspan='2' style="text-align: center;color: #1679ad;font-size: 13px;">Related Companies</td></tr>
<tr>
	<th>Parent Company</th>
	<td>
		<%if(!ccamObj.getString("org-txt38").equals("") || !ccamObj.getString("org-txt39").equals("") || !ccamObj.getString("org-txt40").equals("")){ %>
			<table>
				<tr>
					<td>Company name</td>
					<td>Address</td>
					<td>% of Shareholding</td>
				</tr>
				<tr>
					<td><%if(ccamObj.containsKey("org-txt38")){%><%=ccamObj.getString("org-txt38")%><%}else{%>NA<%}%></td>
					<td><%if(ccamObj.containsKey("org-txt49")){%><%=ccamObj.getString("org-txt39")%><%}else{%>NA<%}%></td>
					<td><%if(ccamObj.containsKey("org-txt40")){%><%=ccamObj.getString("org-txt40")%><%}else{%>NA<%}%></td>
				</tr>
			</table>
		<%}%>	
	</td>
</tr>
<tr>
	<th>Immediate</th>
	<td>
		<%if(!ccamObj.getString("org-txt41").equals("") || !ccamObj.getString("org-txt42").equals("") || !ccamObj.getString("org-txt43").equals("")){ %>
			<table>
				<tr>
					<td>Company name</td>
					<td>Address</td>
					<td>% of Shareholding</td>
				</tr>
				<tr>
					<td><%if(ccamObj.containsKey("org-txt41")){%><%=ccamObj.getString("org-txt41")%><%}else{%>NA<%}%></td>
					<td><%if(ccamObj.containsKey("org-txt42")){%><%=ccamObj.getString("org-txt42")%><%}else{%>NA<%}%></td>
					<td><%if(ccamObj.containsKey("org-txt43")){%><%=ccamObj.getString("org-txt43")%><%}else{%>NA<%}%></td>
				</tr>
			</table>
		<%}%>	
	</td>
</tr>
<tr>
	<th>Ultimate</th>
	<td>
		<%if(!ccamObj.getString("org-txt44").equals("") || !ccamObj.getString("org-txt45").equals("") || !ccamObj.getString("org-txt46").equals("")){ %>
			<table>
				<tr>
					<td>Company name</td>
					<td>Address</td>
					<td>% of Shareholding</td>
				</tr>
				<tr>
					<td><%if(ccamObj.containsKey("org-txt44")){%><%=ccamObj.getString("org-txt44")%><%}else{%>NA<%}%></td>
					<td><%if(ccamObj.containsKey("org-txt45")){%><%=ccamObj.getString("org-txt45")%><%}else{%>NA<%}%></td>
					<td><%if(ccamObj.containsKey("org-txt46")){%><%=ccamObj.getString("org-txt46")%><%}else{%>NA<%}%></td>
				</tr>
			</table>
		<%}%>	
	</td>
</tr>
<tr>
	<th>Subsidiaries</th>
	<td>
		<%if(!ccamObj.getString("org-txt47").equals("") || !ccamObj.getString("org-txt48").equals("") || !ccamObj.getString("org-txt49").equals("")){ %>
			<table>
				<tr>
					<td>Company name</td>
					<td>Address</td>
					<td>% of Shareholding</td>
				</tr>
				<tr>
					<td><%if(ccamObj.containsKey("org-txt47")){%><%=ccamObj.getString("org-txt47")%><%}else{%>NA<%}%></td>
					<td><%if(ccamObj.containsKey("org-txt48")){%><%=ccamObj.getString("org-txt48")%><%}else{%>NA<%}%></td>
					<td><%if(ccamObj.containsKey("org-txt49")){%><%=ccamObj.getString("org-txt49")%><%}else{%>NA<%}%></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Subsidiaries 1</th>
	<td>
		<%if(!ccamObj.getString("org-txt51").equals("") || !ccamObj.getString("org-txt52").equals("") || !ccamObj.getString("org-txt53").equals("")){ %>
			<table>
				<tr>
					<td>Company name</td>
					<td>Address</td>
					<td>% of Shareholding</td>
				</tr>
				<tr>
					<td><%if(ccamObj.containsKey("org-txt51")){%><%=ccamObj.getString("org-txt51")%><%}else{%>NA<%}%></td>
					<td><%if(ccamObj.containsKey("org-txt52")){%><%=ccamObj.getString("org-txt52")%><%}else{%>NA<%}%></td>
					<td><%if(ccamObj.containsKey("org-txt53")){%><%=ccamObj.getString("org-txt53")%><%}else{%>NA<%}%></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Affiliates</th>
	<td>
		<%if(!ccamObj.getString("org-txt54").equals("") || !ccamObj.getString("org-txt55").equals("") || !ccamObj.getString("org-txt56").equals("")){ %>
			<table>
				<tr>
					<td>Company name</td>
					<td>Address</td>
					<td>% of Shareholding</td>
				</tr>
				<tr>
					<td><%if(ccamObj.containsKey("org-txt54")){%><%=ccamObj.getString("org-txt54")%><%}else{%>NA<%}%></td>
					<td><%if(ccamObj.containsKey("org-txt55")){%><%=ccamObj.getString("org-txt55")%><%}else{%>NA<%}%></td>
					<td><%if(ccamObj.containsKey("org-txt56")){%><%=ccamObj.getString("org-txt56")%><%}else{%>NA<%}%></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Affiliates 1</th>
	<td>
		<%if(!ccamObj.getString("org-txt58").equals("") || !ccamObj.getString("org-txt59").equals("") || !ccamObj.getString("org-txt60").equals("")){ %>
			<table>
				<tr>
					<td>Company name</td>
					<td>Address</td>
					<td>% of Shareholding</td>
				</tr>
				<tr>
					<td><%if(ccamObj.containsKey("org-txt58")){%><%=ccamObj.getString("org-txt58")%><%}else{%>NA<%}%></td>
					<td><%if(ccamObj.containsKey("org-txt59")){%><%=ccamObj.getString("org-txt59")%><%}else{%>NA<%}%></td>
					<td><%if(ccamObj.containsKey("org-txt60")){%><%=ccamObj.getString("org-txt60")%><%}else{%>NA<%}%></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Group Companies</th>
	<td>
		<%if(!ccamObj.getString("org-txt61").equals("") || !ccamObj.getString("org-txt62").equals("") || !ccamObj.getString("org-txt63").equals("")){ %>
			<table>
				<tr>
					<td>Company name</td>
					<td>Address</td>
					<td>% of Shareholding</td>
				</tr>
				<tr>
					<td><%if(ccamObj.containsKey("org-txt61")){%><%=ccamObj.getString("org-txt61")%><%}else{%>NA<%}%></td>
					<td><%if(ccamObj.containsKey("org-txt62")){%><%=ccamObj.getString("org-txt62")%><%}else{%>NA<%}%></td>
					<td><%if(ccamObj.containsKey("org-txt63")){%><%=ccamObj.getString("org-txt63")%><%}else{%>NA<%}%></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Group Companies 1</th>
	<td>
		<%if(!ccamObj.getString("org-txt65").equals("") || !ccamObj.getString("org-txt66").equals("") || !ccamObj.getString("org-txt67").equals("")){ %>
			<table>
				<tr>
					<td>Company name</td>
					<td>Address</td>
					<td>% of Shareholding</td>
				</tr>
				<tr>
					<td><%if(ccamObj.containsKey("org-txt65")){%><%=ccamObj.getString("org-txt65")%><%}else{%>NA<%}%></td>
					<td><%if(ccamObj.containsKey("org-txt66")){%><%=ccamObj.getString("org-txt66")%><%}else{%>NA<%}%></td>
					<td><%if(ccamObj.containsKey("org-txt67")){%><%=ccamObj.getString("org-txt67")%><%}else{%>NA<%}%></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>


</table><br/>

<h3>11. Finacial Information</h3>
<table>
<tr>
	<th>Financial Statement Date (dd-mmm-yyyy)</th>
	<td style="width:100px;">
		<%if(!ccamObj.getString("fi-txt2").equals("") || !ccamObj.getString("fi-txt21").equals("") || !ccamObj.getString("fi-txt22s").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt2")){%><%=ccamObj.getString("fi-txt2") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt21")){%><%=ccamObj.getString("fi-txt21") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt22s")){%><%=ccamObj.getString("fi-txt22s") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Sales (Net of Excise)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt4").equals("") || !ccamObj.getString("fi-txt42").equals("") || !ccamObj.getString("fi-txt43s").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt4")){%><%=ccamObj.getString("fi-txt4") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt42")){%><%=ccamObj.getString("fi-txt42") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt43s")){%><%=ccamObj.getString("fi-txt43s") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Other Income</th>
	<td style="width: 350px;">
		<%if(!ccamObj.getString("fi-txt6").equals("") || !ccamObj.getString("fi-txt62").equals("") || !ccamObj.getString("fi-txt63s").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt6")){%><%=ccamObj.getString("fi-txt6") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt62")){%><%=ccamObj.getString("fi-txt62") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt63s")){%><%=ccamObj.getString("fi-txt63s") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Mfg / Exps cost of Operations (including Employee cost) Other operating Exps (including Marketing costs)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt8").equals("") || !ccamObj.getString("fi-txt82").equals("") || !ccamObj.getString("fi-txt83").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt8")){%><%=ccamObj.getString("fi-txt8") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt82")){%><%=ccamObj.getString("fi-txt82") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt83")){%><%=ccamObj.getString("fi-txt83") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Extraordinary Expenses</th>
	<td>
		<%if(!ccamObj.getString("fi-txt10").equals("") || !ccamObj.getString("fi-txt101").equals("") || !ccamObj.getString("fi-txt102").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt10")){%><%=ccamObj.getString("fi-txt10") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt101")){%><%=ccamObj.getString("fi-txt101") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt102")){%><%=ccamObj.getString("fi-txt102") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Depreciation</th>
	<td>
		<%if(!ccamObj.getString("fi-txt12").equals("") || !ccamObj.getString("fi-txt122").equals("") || !ccamObj.getString("fi-txt121").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt12")){%><%=ccamObj.getString("fi-txt12") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt122")){%><%=ccamObj.getString("fi-txt122") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt121")){%><%=ccamObj.getString("fi-txt121") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Interest</th>
	<td>
		<%if(!ccamObj.getString("fi-txt14").equals("") || !ccamObj.getString("fi-txt141").equals("") || !ccamObj.getString("fi-txt142").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt14")){%><%=ccamObj.getString("fi-txt14") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt141")){%><%=ccamObj.getString("fi-txt141") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt142")){%><%=ccamObj.getString("fi-txt142") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Tax</th>
	<td>
		<%if(!ccamObj.getString("fi-txt16").equals("") || !ccamObj.getString("fi-txt161").equals("") || !ccamObj.getString("fi-txt162").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt16")){%><%=ccamObj.getString("fi-txt16") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt161")){%><%=ccamObj.getString("fi-txt161") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt162")){%><%=ccamObj.getString("fi-txt162") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Profit / (Loss) after tax</th>
	<td>
		<%if(!ccamObj.getString("fi-txt18").equals("") || !ccamObj.getString("fi-txt181").equals("") || !ccamObj.getString("fi-txt182").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt18")){%><%=ccamObj.getString("fi-txt18") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt181")){%><%=ccamObj.getString("fi-txt181") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt182")){%><%=ccamObj.getString("fi-txt182") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>ASSETS</th>
	<td>
		<%if(!ccamObj.getString("fi-txt20").equals("") || !ccamObj.getString("fi-txt201").equals("") || !ccamObj.getString("fi-txt202").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt20")){%><%=ccamObj.getString("fi-txt20") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt201")){%><%=ccamObj.getString("fi-txt201") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt202")){%><%=ccamObj.getString("fi-txt202") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Cash and Bank(1)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt22").equals("") || !ccamObj.getString("fi-txt221").equals("") || !ccamObj.getString("fi-txt222").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt22")){%><%=ccamObj.getString("fi-txt22") %><%}else{ %><%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt221")){%><%=ccamObj.getString("fi-txt221") %><%}else{ %><%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt222")){%><%=ccamObj.getString("fi-txt222") %><%}else{ %><%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Accounts receivable(2)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt24").equals("") || !ccamObj.getString("fi-txt241").equals("") || !ccamObj.getString("fi-txt242").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt24")){%><%=ccamObj.getString("fi-txt24") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt241")){%><%=ccamObj.getString("fi-txt241") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt242")){%><%=ccamObj.getString("fi-txt242") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Inventory(3)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt26").equals("") || !ccamObj.getString("fi-txt262").equals("") || !ccamObj.getString("fi-txt263").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt26")){%><%=ccamObj.getString("fi-txt26") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt262")){%><%=ccamObj.getString("fi-txt262") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt263")){%><%=ccamObj.getString("fi-txt263") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Prepayments(4)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt28").equals("") || !ccamObj.getString("fi-txt281").equals("") || !ccamObj.getString("fi-txt282").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt28")){%><%=ccamObj.getString("fi-txt28") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt281")){%><%=ccamObj.getString("fi-txt281") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt282")){%><%=ccamObj.getString("fi-txt282") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Other(5)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt30").equals("") || !ccamObj.getString("fi-txt301").equals("") || !ccamObj.getString("fi-txt302").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt30")){%><%=ccamObj.getString("fi-txt30") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt301")){%><%=ccamObj.getString("fi-txt301") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt302")){%><%=ccamObj.getString("fi-txt302") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Total Current Assets (1+2+3+4+5)=A</th>
	<td>
		<%if(!ccamObj.getString("fi-txt32").equals("") || !ccamObj.getString("fi-txt321").equals("") || !ccamObj.getString("fi-txt322").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt32")){%><%=ccamObj.getString("fi-txt32") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt321")){%><%=ccamObj.getString("fi-txt321") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt322")){%><%=ccamObj.getString("fi-txt322") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>
	</td>
</tr>
<tr>
	<th>Fixtures & Equipment(7)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt34").equals("") || !ccamObj.getString("fi-txt341").equals("") || !ccamObj.getString("fi-txt342").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt34")){%><%=ccamObj.getString("fi-txt34") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt341")){%><%=ccamObj.getString("fi-txt341") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt342")){%><%=ccamObj.getString("fi-txt342") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Land & Buildings(8)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt36").equals("") || !ccamObj.getString("fi-txt361").equals("") || !ccamObj.getString("fi-txt362").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt36")){%><%=ccamObj.getString("fi-txt36") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt361")){%><%=ccamObj.getString("fi-txt361") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt362")){%><%=ccamObj.getString("fi-txt362") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Plant & Machinery(9)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt37").equals("") || !ccamObj.getString("fi-txt371").equals("") || !ccamObj.getString("fi-txt372").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt37")){%><%=ccamObj.getString("fi-txt37") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt371")){%><%=ccamObj.getString("fi-txt371") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt372")){%><%=ccamObj.getString("fi-txt372") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Investment(10)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt39").equals("") || !ccamObj.getString("fi-txt391").equals("") || !ccamObj.getString("fi-txt392").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt39")){%><%=ccamObj.getString("fi-txt39") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt391")){%><%=ccamObj.getString("fi-txt391") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt392")){%><%=ccamObj.getString("fi-txt392") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Others(11)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt41").equals("") || !ccamObj.getString("fi-txt411").equals("") || !ccamObj.getString("fi-txt412").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt41")){%><%=ccamObj.getString("fi-txt41") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt411")){%><%=ccamObj.getString("fi-txt411") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt412")){%><%=ccamObj.getString("fi-txt412") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>TOTAL FIXED ASSETS (7+8+9+10+11)=B</th>
	<td>
		<%if(!ccamObj.getString("fi-txt43").equals("") || !ccamObj.getString("fi-txt431").equals("") || !ccamObj.getString("fi-txt432").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt43")){%><%=ccamObj.getString("fi-txt43") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt431")){%><%=ccamObj.getString("fi-txt431") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt432")){%><%=ccamObj.getString("fi-txt432") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Intangibles =C</th>
	<td>
		<%if(!ccamObj.getString("fi-txt45").equals("") || !ccamObj.getString("fi-txt451").equals("") || !ccamObj.getString("fi-txt452").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt45")){%><%=ccamObj.getString("fi-txt45") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt451")){%><%=ccamObj.getString("fi-txt451") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt452")){%><%=ccamObj.getString("fi-txt452") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Total Assets(A+B+C)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt47").equals("") || !ccamObj.getString("fi-txt471s").equals("") || !ccamObj.getString("fi-txt472s").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt47")){%><%=ccamObj.getString("fi-txt47") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt471s")){%><%=ccamObj.getString("fi-txt471s") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt472s")){%><%=ccamObj.getString("fi-txt472s") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>LIABILITIES</th>
	<td>
		<%if(!ccamObj.getString("fi-txt49").equals("") || !ccamObj.getString("fi-txt491").equals("") || !ccamObj.getString("fi-txt492").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt49")){%><%=ccamObj.getString("fi-txt49") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt491")){%><%=ccamObj.getString("fi-txt491") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt492")){%><%=ccamObj.getString("fi-txt492") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Accounts Payable(1)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt51").equals("") || !ccamObj.getString("fi-txt511").equals("") || !ccamObj.getString("fi-txt512").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt51")){%><%=ccamObj.getString("fi-txt51") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt511")){%><%=ccamObj.getString("fi-txt511") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt512")){%><%=ccamObj.getString("fi-txt512") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Short Term Secured Loans(2)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt53").equals("") || !ccamObj.getString("fi-txt531").equals("") || !ccamObj.getString("fi-txt532").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt53")){%><%=ccamObj.getString("fi-txt53") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt531")){%><%=ccamObj.getString("fi-txt531") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt532")){%><%=ccamObj.getString("fi-txt532") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Short Term Unsecured Loans(3)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt55").equals("") || !ccamObj.getString("fi-txt551").equals("") || !ccamObj.getString("fi-txt552").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt55")){%><%=ccamObj.getString("fi-txt55") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt551")){%><%=ccamObj.getString("fi-txt551") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt552")){%><%=ccamObj.getString("fi-txt552") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Provision for Taxes(4)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt57").equals("") || !ccamObj.getString("fi-txt571").equals("") || !ccamObj.getString("fi-txt572").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt57")){%><%=ccamObj.getString("fi-txt57") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt571")){%><%=ccamObj.getString("fi-txt571") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt572")){%><%=ccamObj.getString("fi-txt572") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Other Provisions(5)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt59").equals("") || !ccamObj.getString("fi-txt591").equals("") || !ccamObj.getString("fi-txt592").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt59")){%><%=ccamObj.getString("fi-txt59") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt591")){%><%=ccamObj.getString("fi-txt591") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt592")){%><%=ccamObj.getString("fi-txt592") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Other Current Liabilities(6)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt61").equals("") || !ccamObj.getString("fi-txt611").equals("") || !ccamObj.getString("fi-txt612").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt61")){%><%=ccamObj.getString("fi-txt61") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt611")){%><%=ccamObj.getString("fi-txt611") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt612")){%><%=ccamObj.getString("fi-txt612") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Total Current Liabilities (1+2+3+4+5+6)=A</th>
	<td>
		<%if(!ccamObj.getString("fi-txt63s").equals("") || !ccamObj.getString("fi-txt631").equals("") || !ccamObj.getString("fi-txt632").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt63s")){%><%=ccamObj.getString("fi-txt63s") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt631")){%><%=ccamObj.getString("fi-txt631") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt632")){%><%=ccamObj.getString("fi-txt632") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Long Term Secured Loans(7)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt65").equals("") || !ccamObj.getString("fi-txt651").equals("") || !ccamObj.getString("fi-txt652").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt65")){%><%=ccamObj.getString("fi-txt65") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt651")){%><%=ccamObj.getString("fi-txt651") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt652")){%><%=ccamObj.getString("fi-txt652") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Long Term Unsecured Loans(8)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt67").equals("") || !ccamObj.getString("fi-txt671").equals("") || !ccamObj.getString("fi-txt672").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt67")){%><%=ccamObj.getString("fi-txt67") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt671")){%><%=ccamObj.getString("fi-txt671") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt672")){%><%=ccamObj.getString("fi-txt672") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Other Long term Liabilities(9)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt69").equals("") || !ccamObj.getString("fi-txt691").equals("") || !ccamObj.getString("fi-txt692").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt69")){%><%=ccamObj.getString("fi-txt69") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt691")){%><%=ccamObj.getString("fi-txt691") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt692")){%><%=ccamObj.getString("fi-txt692") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Issued Capital(10)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt71").equals("") || !ccamObj.getString("fi-txt711").equals("") || !ccamObj.getString("fi-txt712").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt71")){%><%=ccamObj.getString("fi-txt71") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt711")){%><%=ccamObj.getString("fi-txt711") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt712")){%><%=ccamObj.getString("fi-txt712") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Retained Earnings(11)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt73").equals("") || !ccamObj.getString("fi-txt731").equals("") || !ccamObj.getString("fi-txt732").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt73")){%><%=ccamObj.getString("fi-txt73") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt731")){%><%=ccamObj.getString("fi-txt731") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt732")){%><%=ccamObj.getString("fi-txt732") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>Total Liabilities & Equity (A+7+8+9+10+11+12)</th>
	<td>
		<%if(!ccamObj.getString("fi-txt77").equals("") || !ccamObj.getString("fi-txt771").equals("") || !ccamObj.getString("fi-txt772").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt77")){%><%=ccamObj.getString("fi-txt77") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt771")){%><%=ccamObj.getString("fi-txt771") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt772")){%><%=ccamObj.getString("fi-txt772") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr>
	<th>NETWORTH (10+11+12)=B</th>
	<td>
		<%if(!ccamObj.getString("fi-txt79").equals("") || !ccamObj.getString("fi-txt791").equals("") || !ccamObj.getString("fi-txt792").equals("")){ %>
			<table>
				<tr>
					<td><%if(ccamObj.containsKey("fi-txt79")){%><%=ccamObj.getString("fi-txt79") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt791")){%><%=ccamObj.getString("fi-txt791") %><%}else{ %>NA<%} %></td>
					<td><%if(ccamObj.containsKey("fi-txt792")){%><%=ccamObj.getString("fi-txt792") %><%}else{ %>NA<%} %></td>
				</tr>
			</table>
		<%} %>	
	</td>
</tr>
<tr><th>THE ABOVE ACCOUNTS ARE</th><td><%if(ccamObj.containsKey("fi-rad1")){%> <%=ccamObj.getString("fi-rad1") %> <%}else{%><%}%></td></tr>
</table><br/>

<h3>12. Supplier's References</h3>
<table>
<tr><th> Name of supplier </th><td style="width: 350px;"><%if(ccamObj.containsKey("sr-txt1")){%><%=ccamObj.getString("sr-txt1") %><%}else{ %><%} %></td></tr>
<tr><th> Contact Person </th><td><%if(ccamObj.containsKey("sr-txt2")){%><%=ccamObj.getString("sr-txt2") %><%}else{ %><%} %></td></tr>
<tr><th> Tel No (with STD code) </th><td><%if(ccamObj.containsKey("sr-txt3")){%><%=ccamObj.getString("sr-txt3") %><%}else{ %><%} %></td></tr>
<tr><th> % in total sales </th><td><%if(ccamObj.containsKey("sr-txt4")){%><%=ccamObj.getString("sr-txt4") %><%}else{ %><%} %></td></tr>
<tr><th>History of the firm i.e. who started, when, management change, profile of the principals and key executives etc</th><td><%if(ccamObj.containsKey("sr-txt5")){%><%=ccamObj.getString("sr-txt5") %><%}else{ %><%} %></td></tr>
<tr><th>Quality certificates, export awards won, membership of associations</th><td><%if(ccamObj.containsKey("sr-txt6")){%><%=ccamObj.getString("sr-txt6") %><%}else{ %><%} %></td></tr>
<tr><th>Any other information that would enable us to understand your business better</th><td><%if(ccamObj.containsKey("sr-txt7")){%><%=ccamObj.getString("sr-txt7") %><%}else{ %><%} %></td></tr>
<tr><th>Questionnaire Filled-up By </th><td><%if(ccamObj.containsKey("sr-txt8")){%><%=ccamObj.getString("sr-txt8") %><%}else{ %><%} %></td></tr>
<tr><th>Person Designation </th><td><%if(ccamObj.containsKey("sr-txt9")){%><%=ccamObj.getString("sr-txt9") %><%}else{ %><%} %></td></tr>
</table><br/>

<%} else{ %>
<table>
<tr>
<%if(ccamComments !=null && !ccamComments.equals("")){ %>
<td colspan='2' style="text-align: center;font-size: 10px;">Comments - <%=ccamComments%></td>
<%}else{ %>
<td colspan='2' style="text-align: center;color: red;font-size: 10px;">No Data Found</td>
<%} %>

</tr>
</table>
<%} %>
<br/>

<h3>Documents Collected:</h3>
<%if(docArray != null && docArray.size() > 0){
for(int i = 0; i < docArray.size(); i++){
JSONObject docObj = docArray.getJSONObject(i);
String docId = docObj.getString("docId");
String docName = (docObj.containsKey("docName") ? docObj.getString("docName"):"");
%>
<p style="color: #444;">
	<%if(!docName.equals("")){ %>
		<%=i+1%>. <%=docName%>
	<%}else{ %>
		<%=i+1%>. <spring:message code='<%=docObj.getString("docId")%>'/>
	<%} %>
</p>
<table>
<tr><th>Action</th><td><%if(docObj.containsKey("action")){%> <%=docObj.getString("action") %><% }else {%> <%} %></td></tr>
<%if(docObj.getString("actionId").equals("1")){//Received %>
<tr><th>Images</th>
	<td style="width: 320px;color: blue;">
	<%if(docMediaArray != null && docMediaArray.size() > 0){
	for(int a=0; a < docMediaArray.size(); a++){
		JSONObject mediaObj = docMediaArray.getJSONObject(a);
		if(mediaObj.containsKey(docId)){
			JSONArray mediaLinkArray = mediaObj.getJSONArray(docId);
			if(mediaLinkArray != null){
				for(int l=0; l < mediaLinkArray.size() ; l++){ %>
					<a href="<%=mediaLinkArray.get(l) %>" style="color: #1679ad;"><img src="<%=mediaLinkArray.get(l) %>" ></a><br/>
				<% }}else{ %>
					No Document available
		<%} }%>
	<%}}else { %>
	No Document available
	<%} %>
	</td>
</tr>	 
<%}else if(docObj.getString("actionId").equals("2") || docObj.getString("actionId").equals("4")){//Declined & N/A %>
	<tr><th>Comments</th><td><%if(docObj.containsKey("comments")){%> <%=docObj.getString("comments") %> <% }else {%> <%} %></td></tr>	
<%}else if(docObj.getString("actionId").equals("3")){//Differed %>
	<tr><th>Comments</th><td><%if(docObj.containsKey("comments")){%> <%=docObj.getString("comments") %> <% }else {%> <%} %></td></tr>
	<tr><th>Next FollowUp Date</th><td><%if(docObj.containsKey("nextFollowUpDate")){%> <%=docObj.getString("nextFollowUpDate") %> <% }else {%> <%} %></td></tr>
<%} %>
<%-- <%if(docObj.getString("actionId").equals("0")){//Received %>
<tr><th>Images</th>
	<td style="width: 320px;color: blue;">
	<%if(docMediaArray != null && docMediaArray.size() > 0){
	for(int a=0; a < docMediaArray.size(); a++){
		JSONObject mediaObj = docMediaArray.getJSONObject(a);
		if(mediaObj.containsKey(docId)){
			JSONArray mediaLinkArray = mediaObj.getJSONArray(docId);
			if(mediaLinkArray != null){
				for(int l=0; l < mediaLinkArray.size() ; l++){ %>
					<a href="<%=mediaLinkArray.get(l) %>" style="color: #1679ad;"><img src="<%=mediaLinkArray.get(l) %>" style="width: 200px;"></a><br><br>
				<% }}else{ %>
					No Document available
		<%} }%>
	<%}}else { %>
	No Document available
	<%} %>
	</td>
</tr>	 
<%}else if(docObj.getString("actionId").equals("1") || docObj.getString("actionId").equals("3")){//Declined & N/A %>
	<tr><th>Comments</th><td><%if(docObj.containsKey("comments")){%> <%=docObj.getString("comments") %> <% }else {%> <%} %></td></tr>	
<%}else if(docObj.getString("actionId").equals("2")){//Differed %>
	<tr><th>Comments</th><td><%if(docObj.containsKey("comments")){%> <%=docObj.getString("comments") %> <% }else {%> <%} %></td></tr>
	<tr><th>Next FollowUp Date</th><td><%if(docObj.containsKey("nextFollowUpDate")){%> <%=docObj.getString("nextFollowUpDate") %> <% }else {%> <%} %></td></tr>
<%} %> --%>
</table>
<%}}else{ %>
<table>
<tr>
<td colspan='2' style="text-align: center;color: red;font-size: 10px;">No Data Found</td>
</tr>
</table><br/>
<%} %>
</body>
</html>
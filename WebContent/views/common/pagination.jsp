<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="in.otpl.dnb.util.PlatformUtil"%>
<%@page import="java.util.Enumeration"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
Logger log = Logger.getLogger("pagination.jsp");
	String queryString = "";
	/* Enumeration<String> params = request.getParameterNames();
	//Enumeration<String> params = request.getAttributeNames();
	Set<String> set = new HashSet<String>();
	
	while (params.hasMoreElements()) {
		String param = (String) params.nextElement();log.info(param);
		set.add(param);
	}
	Iterator<String> iterator = set.iterator();
	while(iterator.hasNext()){
		String param = iterator.next();
		if (!param.equals("totalRows") && !param.equals("currentPage") && !param.equals("rowcount")
				&& !param.equals("actionName") && !param.equals("excelAvailable") && !param.equals("pdfAvailable")
				&& PlatformUtil.isNotEmpty(request.getParameter(param))) {
			log.info(param);
			String[] values = request.getParameterValues(param);
			for (int i = 0; i < values.length; i++) {
				log.info(values[i]);
				queryString += "/" + values[i];
			}
		}
	} */
	String params = request.getParameter("params");
	if(params != null){
		String[] paramList = params.split(",");
		for(String param:paramList){
			String value = (String) request.getParameter(param);
			if(value != null && !value.trim().equals("") && !value.trim().equals("null")){
				if(queryString != "")
					queryString+="&";
				queryString += param+"="+value;
			}
		}
	}
	
	if(queryString != "")
		queryString = "?"+queryString;

	int totalRows = 0, offset = 0, rowcount = 0, currentPage = 0;
	String actionName = "";
	try {
		totalRows = (Integer) request.getAttribute("totalRows");
		currentPage = (Integer) request.getAttribute("currentPage");
		rowcount = (Integer) request.getAttribute("rowcount");
		actionName = request.getParameter("actionName");
	} catch (Exception e) {
		e.printStackTrace();
	}
	if (rowcount == 0)
		rowcount = totalRows;
	if (actionName == null) {
		actionName = "";
	}
	offset = currentPage - 1;
	int startRow = offset * rowcount + 1;
	int nextRows = offset * rowcount + rowcount;
	int lastRow = (totalRows > nextRows ? nextRows : totalRows);
%>

<nav aria-label="...">
	<ul class="pagination">
		<li><span style="font-size: 14px;"><%=startRow%> - <%=lastRow%>&nbsp;
			<spring:message code="common.message.paginationOf" />&nbsp;<%=totalRows%>&nbsp;
			<spring:message code="common.message.paginationRecords" /></span>
		</li>
		<%
			int pageNums = 0;
			try {
				pageNums = totalRows / rowcount;
				if (totalRows % rowcount > 0) {
					pageNums = pageNums + 1;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(pageNums > 1){
			if (currentPage > 2) {
				String firstPageAction = actionName + "1" + "/" + rowcount + queryString;
		%>
		<spring:url value="<%=firstPageAction%>" var="firstPage" />
		<li><a href="${firstPage}" title='<spring:message code="common.label.first"/>'>&lt;&lt;</a></li>
		<%
			}

			if (currentPage > 1 && currentPage <= pageNums) {
				int prev = currentPage - 1;
				String prePageAction = actionName + prev + "/" + rowcount + queryString;
		%>
		<spring:url value="<%=prePageAction%>" var="prePage" />
		<li><a href="${prePage}" title='<spring:message code="common.label.Previous"/>'><span aria-hidden="true">&lt;</span></a></li>
		<%
			}

			int currentPrePrePage = currentPage-2;//1
			if(currentPage == pageNums && currentPrePrePage > 0){
				String currentPrePrePageAction = actionName + currentPrePrePage + "/" + rowcount + queryString;
		%>
		<spring:url value="<%=currentPrePrePageAction%>" var="currentPrePrePage" />
		<li><a href="${currentPrePrePage}" title='<%=currentPrePrePage%>'><span aria-hidden="true"><%=currentPrePrePage%></span></a></li>
		<%
			}
			
			int currentPrePage = currentPage-1;//2
			if(currentPrePage > 0){
				String currentPrePageAction = actionName + currentPrePage + "/" + rowcount + queryString;
		%>
		<spring:url value="<%=currentPrePageAction%>" var="currentPrePage" />
		<li><a href="${currentPrePage}" title='<%=currentPrePage%>'><span aria-hidden="true"><%=currentPrePage%></span></a></li>
		<%
			}
			
			String currentPageAction = actionName + currentPage + "/" + rowcount + queryString;//3
		%>
		<spring:url value="<%=currentPageAction%>" var="currentPage" />
		<li><a style="background:#23527c;color:#fff" href="${currentPage}"><%=currentPage%></a></li>
		
		<%
			int currentNextPage = currentPage+1;//4
			if(currentNextPage <= pageNums){
				String currentNextPageAction = actionName + currentNextPage + "/" + rowcount + queryString;
		%>
		<spring:url value="<%=currentNextPageAction%>" var="currentNextPage" />
		<li><a href="${currentNextPage}" title='<%=currentNextPage%>'><span aria-hidden="true"><%=currentNextPage%></span></a></li>
		<%
			}
			
			int currentNextNextPage = currentPage+2;//5
			if(currentPage == 1 && currentNextNextPage <= pageNums){
				String currentNextNextPageAction = actionName + currentNextNextPage + "/" + rowcount + queryString;
		%>
		<spring:url value="<%=currentNextNextPageAction%>" var="currentNextNextPage" />
		<li><a href="${currentNextNextPage}" title='<%=currentNextNextPage%>'><span aria-hidden="true"><%=currentNextNextPage%></span></a></li>
		<%
			}
			
			if (currentPage < pageNums) {
				int next = currentPage + 1;
				int last = pageNums;
				String nextPageAction = actionName + next + "/" + rowcount + queryString;
				String lastPageAction = actionName + last + "/" + rowcount + queryString;
		%>
		<spring:url value="<%=nextPageAction%>" var="nextPage" />
		<spring:url value="<%=lastPageAction%>" var="lastPage" />
		<li><a href="${nextPage}" title='<spring:message code="common.label.Next"/>'><span aria-hidden="true">&gt;</span></a></li>
		<li><a href="${lastPage}" title='<spring:message code="common.label.Last"/>'><span aria-hidden="true">&gt;&gt;</span></a></li>
		<%
			}
			}
			
			String excelAvailable = request.getParameter("excelAvailable");
			if(excelAvailable != null){
				String excelDownloadAction = excelAvailable + queryString;
		%>
		<spring:url value="<%=excelDownloadAction%>" var="excelDownload"/>
		<li><a href="${excelDownload}"><img src="resources/images/excel.png" data-toggle="tooltip" title="<spring:message code="common.download.excel"/>" width="19" height="20"/></a></li>
		<%-- <li><a href="#" onclick="return downloadExcel();"><img src="resources/images/excel.png" data-toggle="tooltip" title="<spring:message code="common.download.excel"/>" width="19" height="20"/></a></li> --%>
		<%	
			}
			
			String pdfAvailable = request.getParameter("pdfAvailable");
			if(pdfAvailable != null){
				String pdfDownloadAction = pdfAvailable + queryString;
		%>
		<spring:url value="<%=pdfDownloadAction%>" var="pdfDownload"/>
		<li><a href="${pdfDownload}"><img src="resources/images/pdf.png" width="15" height="20" data-toggle="tooltip" title="<spring:message code="common.download.pdf"/>"/></a></li>
		<%-- <li><a href="#" onclick="return downloadPdf();"><img src="resources/images/pdf.png" width="15" height="20" data-toggle="tooltip" title="<spring:message code="common.download.pdf"/>"/></a></li> --%>
		<%	
			}
			
			String xmlAvailable = request.getParameter("xmlAvailable");
			if(xmlAvailable != null){
				String xmlDownloadAction = xmlAvailable + queryString;
		%>
		<spring:url value="<%=xmlDownloadAction%>" var="xmlDownload"/>
		<li><a href="${xmlDownload}"><img src="resources/images/xml.png" width="15" height="20" data-toggle="tooltip" title="<spring:message code="common.download.xml"/>"/></a></li>
		<%-- <li><a href="#" onclick="return downloadXml();"><img src="resources/images/pdf.png" width="15" height="20" data-toggle="tooltip" title="<spring:message code="common.download.pdf"/>"/></a></li> --%>
		<%	
			}
		%>
	</ul>
</nav>
<script type="text/javascript">
function downloadExcel(){window.location.href = "${excelDownload}";}
function downloadPdf(){window.location.href = "${pdfDownload}";}
function downloadXml(){window.location.href = "${xmlDownload}";}
</script>
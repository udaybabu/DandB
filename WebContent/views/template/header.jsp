<%@page import="in.otpl.dnb.util.SessionConstants"%>
<%@page import="in.otpl.dnb.util.URLConstants"%>
<%@page import="in.otpl.dnb.util.ServerConstants"%>
<%
boolean isAdmin = false, isLead = false,isHR=false, isUser=false;
Integer accId = (Integer) request.getSession(true).getAttribute(SessionConstants.CUSTOMER_ID);
Integer userTypeId = (Integer) request.getSession(true).getAttribute(SessionConstants.USER_TYPE_ID);
if(userTypeId != null && userTypeId == SessionConstants.USER_TYPE_ID_ADMIN)
	isAdmin = true;
else if(userTypeId != null && userTypeId == SessionConstants.USER_TYPE_ID_LEAD)
	isLead = true;
%>
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="<%= request.getContextPath() %><%= URLConstants.DASHBOARD%>" ><img src="resources/images/logo.png"  alt=""/></a>
    </div>

    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav navbar-right">
        <li><a href="<%= request.getContextPath() %><%= URLConstants.DASHBOARD%>"><span class="glyphicon glyphicon-home"></span> Dashboard</a></li>
 	   <% if(isLead || isAdmin){ %>
 	<li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><i class="fa fa-file-text"></i> Report(s)<span class="caret"></span></a>
          <ul class="dropdown-menu">
           <li><a tabindex="-1" href="<%=request.getContextPath() %><%= URLConstants.REPORT_USER_TRACKING %>">User Tracking Report</a></li>
            <% if(isAdmin){ %>
			<li><a tabindex="-1" href="<%= request.getContextPath() %><%= URLConstants.REPORT_ALLUSERMAP %>">All User Map Report</a></li>
			<li><a tabindex="-1" href="<%=request.getContextPath() %><%= URLConstants.REPORT_LOGIN %>">Login Details</a></li>
          <% } %>
          </ul>
        </li>
         <% } %>
        <% if(isAdmin){ %>
         <li><a href="<%= request.getContextPath() %><%= URLConstants.USER %>"><i class="fa fa-user"></i> User</a></li>
         <li><a href="<%= request.getContextPath() %><%= URLConstants.TEAM %>"><i class="fa fa-users"></i> Team</a></li>
         <% } %>
         <li><a href="<%= request.getContextPath() %><%= URLConstants.ENQUIRY %>"><i class="fa fa-circle-o-notch fa-spin fa-1x fa-fw"></i> <span class="sr-only">Loading...</span> Enquiry Pool</a></li>
        <li class="dropdown">
       <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="fa fa-address-card"></span> <%= request.getSession(true).getAttribute(SessionConstants.USER_FIRST_NAME) %> <span class="caret"></span></a>
           <ul class="dropdown-menu">
          <li><a>A/C: <%= request.getSession(true).getAttribute(SessionConstants.CUSTOMER_NAME) %></a></li>
            <% if(isLead || isAdmin){ %>
           <li><a href="<%= request.getContextPath() %><%= URLConstants.USER_PASSWORD_CHANGE %>">Change Password</a></li>
            <% } %>
            <li role="separator" class="divider"></li>
            <li><a href="<%= request.getContextPath() %><%= URLConstants.LOGOUT %>">Logout</a></li>
          </ul>
        </li>
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>
<%@page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <meta name="keywords" content="Dun and Bradstreet"/>   
	<meta name="description" content="" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<base href="<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/"%>" />
	<title><tiles:insertAttribute name="title" ignore="true" /></title>
 	<link rel="icon" href="<%=request.getContextPath()%>/resources/images/dnbFavicon.png" type="image/x-icon">
    <link href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/resources/css/flatpickr.min.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/resources/css/font-awesome.min.css" rel="stylesheet"> 
    <link href="<%=request.getContextPath()%>/resources/css/style.css" rel="stylesheet">
    
   	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-3.1.1.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/bootbox.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/flatpickr.min.js"></script>
</head>

<body class="app">
	    <tiles:insertAttribute name="header"/>
	    <tiles:insertAttribute name="body"/>
    <tiles:insertAttribute name="footer" />
   
</body>

</html>
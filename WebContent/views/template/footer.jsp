<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<footer class="footer">
	<div class="container text-center">
 	<a href="http://orientindia.com/" class="text-center"  target="_blank" style="margin-bottom:0;color:#f7f7f7">© <%= new java.text.SimpleDateFormat("yyyy").format(new java.util.Date()) %> <spring:message code="footer.orient.message"></spring:message></a>
	</div>
</footer>
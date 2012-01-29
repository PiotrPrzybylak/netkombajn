<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sklep" %>

<html xmlns="http://www.w3.org/1999/xhtml" lang="pl">
<head>

<title>${configuration.shopName}</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="-1" />
<meta name="author" content="" />

<link rel="stylesheet" type="text/css" href="${configuration.externalCssFileUrl}" />
<meta name="keywords" content="" />
<meta name="description" content="" />

<script type="text/javascript" src="../js/lightbox/prototype.js"></script>
<script type="text/javascript" src="resources/js/jquery-1.3.2.js"></script>
<script type="text/javascript" src="../js/custom.js"></script>
<script type="text/javascript" src="resources/js/sklep3.js"></script>
<!-- START light box settings -->
<script type="text/javascript" src="../js/lightbox/scriptaculous.js?load=effects,builder"></script>
<script type="text/javascript" src="../js/lightbox/lightbox.js"></script>
<link rel="stylesheet" href="../layout/lightbox.css" type="text/css" media="screen" />
<!-- END light box settings -->


${css_for_ie.text}

</head>
<body>

<div id="wrapper">

<div id="header">

<c:choose>
	<c:when test="${!empty custom_header.text}">
		${custom_header.text} 			
	</c:when>
	<c:otherwise>
		<div id="shoplogo">
			<h1><a href="home.html" title="Przejdź na stronę główną sklepu"><span>${configuration.shopName}</span></a></h1>
		</div>
	</c:otherwise>
</c:choose>

</div>
<!-- header -->


<div id="main_wrapper">

<div id="main">

<jsp:include page="../bricks/searchBrick.jsp"/>

<tiles:insertAttribute name="main" />

</div><!-- main -->

</div><!-- main_wrapper -->

<div id="footer">

${custom_footer.text}

<p><strong>© 2009 Wszelkie prawa zastrzeżone</strong>. Sklep działa
na platformie <a class="logo" href="http://www.netolution.pl/">NetKombajn</a></p>
 
</div><!-- footer -->


</div><!-- wrapper -->

<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
try {
var pageTracker = _gat._getTracker("${configuration.analyticsCode}");
pageTracker._trackPageview();
} catch(err) {}</script>

</body>
</html>
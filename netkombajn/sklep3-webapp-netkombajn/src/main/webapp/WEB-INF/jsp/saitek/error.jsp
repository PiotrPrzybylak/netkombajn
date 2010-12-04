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

<script type="text/javascript" src="resources/js/jquery-1.3.2.js"></script>
<script type="text/javascript" src="resources/js/sklep3.js"></script>
<script type="text/javascript" src="../js/custom.js"></script>

${css_for_ie.text} 	



</head>
<body>

<div id="wrapper">

<div id="header">


		<div id="shoplogo">
			<h1><a href="home.html" title="Przejdź na stronę główną sklepu"><span>Błąd!</span></a></h1>
		</div>

</div>
<!-- header -->

<div id="main">

<div class="box">
	<h2>Przepraszamy wystapił błąd</h2>
	<div class="box-content">
		<a class="important" href="home.html"> Powróć na stronę główną.</a>
	</div>
</div>

</div><!-- main -->
<div id="footer">

<p><strong>© 2009 Wszelkie prawa zastrzeżone</strong>. Sklep działa
na platformie <a class="logo" href="http://www.netolution.pl/">NetKombajn</a></p>
 
</div><!-- footer -->


</div><!-- wrapper -->
</body>
</html>

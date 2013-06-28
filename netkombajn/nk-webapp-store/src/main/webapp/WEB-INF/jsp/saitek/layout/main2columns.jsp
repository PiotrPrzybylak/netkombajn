<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>



<div id="sidebar">
	<div id="sidebar_header"></div>
	<div id="sidebar_content">
		<jsp:include page="../bricks/cartPreviewBrick.jsp"/>
		<jsp:include page="../bricks/categoriesBrick.jsp"/>
		<jsp:include page="../bricks/searchBrick.jsp"/>
		<jsp:include page="../bricks/informationBrick.jsp"/>
		<jsp:include page="../bricks/contactBrick.jsp"/>
	</div>
</div><!-- sidebar -->
	

<div id="content-two-column">
	<div id="content_header"></div>
	<div id="content_content">
	<tiles:insertAttribute name="content" />
</div><!-- content -->

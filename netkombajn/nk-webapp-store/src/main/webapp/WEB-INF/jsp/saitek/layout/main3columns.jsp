<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>



<div id="sidebar">

	<jsp:include page="../bricks/categoriesBrick.jsp"/>
	<jsp:include page="../bricks/searchBrick.jsp"/>
	<jsp:include page="../bricks/informationBrick.jsp"/>
	<jsp:include page="../bricks/contactBrick.jsp"/>

</div><!-- sidebar -->

<div id="content">
<tiles:insertAttribute name="content" />
</div><!-- content -->

<div id="sidebar2">
	<jsp:include page="../bricks/cartPreviewBrick.jsp"/>
</div><!-- sidebar2 -->

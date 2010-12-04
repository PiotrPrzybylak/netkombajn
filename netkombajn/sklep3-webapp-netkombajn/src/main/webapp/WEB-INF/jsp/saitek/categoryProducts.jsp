<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="categoryProducts" class="box">
	<h2>Katalog Produktów</h2>
		<div class="box-content">
		
		<div class="box-teaser">
			${choosenCategory.name}
		</div>
			<jsp:include page="productsList.jsp"/>
		</div>
	
</div>

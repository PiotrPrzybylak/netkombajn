<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sklep" %>

<div id="categories" class="box">


<h2>Kategorie</h2>
<div class="box-content">

<c:set var="contextPath">${pageContext.request.contextPath}</c:set> 
<ul>
	<c:forEach items="${categories}" var="category">
		
		<sklep:category category="${category}" chosenCategory="${chosenCategory}"/>

	</c:forEach>
</ul>
</div>
</div>

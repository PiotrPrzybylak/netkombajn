<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag body-content="empty"  %>
<%@ attribute name="category" required="true" type="pl.netolution.sklep3.domain.Category" %>
<%@ attribute name="chosenCategory" required="true" type="pl.netolution.sklep3.domain.Category" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sklep" %>

		<c:choose>
			<c:when test="${category.id==choosenCategory.id}">
				<c:set var="categoryClass" value="choosenCategory"/>
			</c:when>
			<c:otherwise>
				<c:set var="categoryClass" value=""/>
			</c:otherwise>
		</c:choose>

		<c:if test="${category.id==choosenCategory.rootCategory.id}">
				<c:set var="categoryClass" value="${categoryClass} chosenRootCategory"/>	
		</c:if>
		
		<li class="${categoryClass}">
			<span class="categoryToogleGadget"><span>&raquo;</span></span>
			<a href="category.html?categoryId=${category.id}">${category.name} (${category.visibleProductsNumber})</a>
				<ul>
					<c:forEach items="${category.children}" var="child">
						<sklep:category category="${child}" chosenCategory="${chosenCategory}"/>
					</c:forEach>
				</ul>
		</li>		
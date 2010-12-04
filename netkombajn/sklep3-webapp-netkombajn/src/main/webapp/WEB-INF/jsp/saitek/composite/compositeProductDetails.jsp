<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sklep" %>
<%@ taglib prefix="string" uri="http://jakarta.apache.org/taglibs/string-1.1" %>

<script type="text/javascript" src="../js/compositeProduct.js"></script>


<div class="box">
<h2>Zestaw komputerowy ${compositeProduct.name}</h2>
<div class="box-content" id="compositeProductDetails">
	
	<div id="compositePrice">
		<span>Cena zestawu: </span> 
		<span id="compositePriceValue"><sklep:price price="${compositeProduct.calculatedInitialPrice}"/></span>
	</div>
	
	<div>${compositeProduct.description}</div>
	
	<form action="addCompositeProductToCart.do" method="post">
		<input type="hidden" name="compositeProductId" id="compositeProductId" value="${compositeProduct.id}"/>
		<%--iteracja po grupach --%>
		<c:forEach items="${compositeProduct.productGroups}" var="group">
				<c:set var="groupNameWithoutSpaces"><string:replace replace=" " with="">${group.name}</string:replace></c:set>
				
				<h3>${group.name}</h3>
					<input type="hidden" id="${groupNameWithoutSpaces}" class="groupMarker" name="productGroup" value="${group.firstProduct.id}"/>
					
					<%--iteracja po produktach --%>
					<c:forEach items="${group.products}" var="product" varStatus="varStatus">
						
						
						<%--sprawdzamy jaka widocznosc w layoucie ustawic dla produktu --%>
						
						<c:set var="visibilityClass">hidden</c:set>
						
						<c:choose>
							<c:when test="${not empty group.primaryProduct}">
									<c:if test="${group.primaryProduct.id==product.id}">
										<c:set var="visibilityClass">compositeProductPart</c:set>
									</c:if>
							</c:when>
							<c:otherwise>
									<c:if test="${varStatus.index==0}">
										<c:set var="visibilityClass">compositeProductPart</c:set>
									</c:if>
							</c:otherwise>
						</c:choose>
						
						
						<c:set var="product" value="${product}" scope="request"/>
						<c:set var="group" value="${group}" scope="request"/>
						
						
						
						<jsp:include page="compositeProductPart.jsp">
									<jsp:param name="visibilityClass" value="${visibilityClass}"/>
									<jsp:param name="counter" value="${varStatus.index}"/>
									<jsp:param name="isLast" value="${varStatus.last}"/>
						</jsp:include>
					</c:forEach>
		</c:forEach>
		
		<input type="submit" id="compositeProductToCart" value="Do koszyka">
		
	</form>
	
</div>

</div>
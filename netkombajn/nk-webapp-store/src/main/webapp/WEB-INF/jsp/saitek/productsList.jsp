<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sklep" %>
<%@ taglib uri="/WEB-INF/jsp/sklep3.tld" prefix="s3" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<fmt:setBundle basename="resources/locale/availability"/>
<div id="productsList">


<c:if test="${!empty products}">
<div id="productsListHeader">
	<span id="foto">Foto</span>
	<span id="listHeaderWrapper">
		<span id="productNameHeader"><s3:sort orderName="name" label="Nazwa"/></span>
		<span id="productPriceHeader"><s3:sort orderName="price" label="Cena"/></span>
	</span>
</div>
</c:if>
	
	<c:forEach items="${products}" var="product">
		<div class="product">
			<div class="product-small-picture">
				<a href="product.html?productId=${product.id}">
					<sklep:productPicture product="${product}" format="productslist"/>
				</a>
			</div>
			<div class="product-information">
				
			<div class="product-name"><a href="product.html?productId=${product.id}">${product.name}</a></div>
			
			<div class="product-price">
				<sklep:price price="${product.defaultSku.finalPrice}"/>
			</div>
			<div class="cart">
				
<c:if test="${product.defaultSku.available}">
				<form action="addProductToCart.do" method="post">
					<div>
						<input type="text" name="quantity" class="product-amount" value="1" class="textInput" />
					</div>
					<input type="hidden" name="skuId" value="${product.defaultSku.id}"/>
					<input type="hidden" name="returnPage" value="category.html?id=${category.id}" />
					<div>
						<input type="submit" name="adToCart" alt="dodaj do koszyka" value="" class="cartButton"/>
					</div> 
				</form>
</c:if>					
				</div>
				<div class="product-description-wrapper">
					<div class="product-short-desc">${product.shortDescription}</div>
				</div>
			</div>
		</div>
		<div>
	</div>
	

		<div class="productListAvailability">
			 <span class="availabilityInList AVAILABILITY_${product.defaultSku.availability}"/>Dostępność:</span>
		</div>

	</c:forEach>
	
</div>

<s3:siteNavigation items="${products}" numberOfAllItems="${totalProductsNumber}" currentStart="${param.start}" fixedItemsOnPage="${productsOnPage}">
	<div id="productsPageNavigation">
		<div id="paginatorWrapper">
			<div id="prevProductsNavigation"> 
				<c:if test="${showPrevLink}"><a href="${prevLink}">&lt;&lt;poprzednie</a></c:if>
			</div>
			
			<div id="paginator">
				<c:if test="${showPrevLink}">
						<a href="${beginingLink}">początek</a> ... 
				</c:if>
				<c:forEach items="${paginators}" var="paginator">
					<c:choose>
						<c:when test="${paginator.current}">
							<b><a href="${paginator.url}">${paginator.subpageNumber}</a></b>
						</c:when>
						<c:otherwise>
							<a href="${paginator.url}">${paginator.subpageNumber}</a>					
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<c:if test="${showNextLink}">
						... <a href="${endLink}">koniec</a> 
				</c:if>
			</div>
			
			<div id="nextProductsNavigation">
				<c:if test="${showNextLink}"><a href="${nextLink}"> następne&gt;&gt;</a></c:if>
			</div>
		</div>
	</div>
	
</s3:siteNavigation>
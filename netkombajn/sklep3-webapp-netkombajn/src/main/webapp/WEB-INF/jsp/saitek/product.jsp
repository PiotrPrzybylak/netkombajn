<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sklep" %>



<div id="product" class="box">
<h2>Informacje o produkcie</h2>
<div class="box-content">

<div id="productHeader">

<h3>${product.name}</h3>
<span id="productPicture">
		<sklep:productPicture product="${product}" format="productdetailsmain" lightbox="true"/></span>

<div id="productDetails">
	<div id="productDetailsElements">
		<div class="productDetail">
			<span class="detailDescription">Numer katalogowy</span><span class="detailValue">${product.catalogNumber}</span>
		</div>
		
		<c:if test="${! empty product.manufacturer}">
			<div class="productDetail">
				<span class="detailDescription">Producent</span><span class="detailValue">${product.manufacturer}</span>
			</div>
		</c:if>

		<div class="productDetail">
			<span class="detailDescription">Dostępność</span>
			<span class="detailValue"><span class="availability AVAILABILITY_${product.defaultSku.availability}"><span>
			<fmt:bundle basename="resources/locale/availability">
				<fmt:message key="${product.defaultSku.availability}" />
			</fmt:bundle>

		</span></span></span>
		</div>
	</div>

	<div id="productPriceDetails">

	<c:choose>
		<c:when test="${product.priceLowerThenSuggested}">
			<span>Cena katalogowa:</span><span><sklep:price price="${product.suggestedPrice}"/></span>
			<span>Nasza cena:</span><span class="price"><sklep:price price="${product.defaultSku.finalPrice}"/></span>
			<span>Oszczędzasz:</span><span id="savingAmount"><sklep:price price="${product.savings}"/></span>
		</c:when>
		<c:otherwise>
			<span>Nasza cena:</span><span class="price"><sklep:price price="${product.defaultSku.finalPrice}"/></span>
		</c:otherwise>
	</c:choose>

	</div>
	<!-- productPriceDetails -->

</div>

<!-- productDetails -->
</div>

<div id="productThumbnails">
 <c:forEach var="thumbnail" items="${product.pictures}">
 		<sklep:picture picture="${thumbnail}"  lightbox="true" format="adminsmall"/>
 </c:forEach>
 

</div>

<div id="addToCartComponent">
<c:choose>
<c:when test="${product.defaultSku.available}">
<form action="addProductToCart.do" method="post">
<input type="hidden" name="skuId" value="${product.defaultSku.id}" />
<label for="quantity">Zamawiana ilość:</label><input name="quantity" value="1" maxlength="4" size="4" class="textInput" /> <button type="submit" class="addToCartButton2"><span>Dodaj do koszyka</span></button>
</form>
</c:when>
<c:otherwise></c:otherwise>
</c:choose>
</div>

<div id="productDescription">
<h4>Opis produktu</h4>
<div>${product.description}</div>
</div><!-- productDescription -->

</div><!-- box-contnet -->
</div><!-- box product -->




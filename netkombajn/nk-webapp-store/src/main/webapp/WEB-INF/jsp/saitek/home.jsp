<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sklep" %>

<c:if test="${!empty hit}">

<div id="hit" class="box">
<h2>Nasz Hit</h2>
<div class="box-content">
<h3>${hit.name}</h3>
<span class="hitShortDescription">${hit.shortDescription} ... <a href="product.html?productId=${hit.id}">więcej</a></span>
<span class="hitPrice">
	<sklep:price price="${hit.defaultSku.finalPrice}"/>
</span>
<form action="addProductToCart.do" method="post">
<input type="hidden" name="skuId" value="${hit.defaultSku.id}"/>
<a href="product.html?productId=${hit.id}" class="detailsButton"><span>Zobacz</span></a>
<button type="submit" class="addToCartButton"><span>Do koszyka</span></button>
</form>
<span>
<a href="product.html?productId=${hit.id}"><sklep:productPicture product="${hit}" format="hit"/></a>
</span>
</div>
</div>

</c:if>

<div id="news" class="box">
<h2>Nowości</h2>
<div class="box-content">

<ul>


<c:forEach var="product" items="${newProducts}" >

<li>
<h3><a href="product.html?productId=${product.id}">${product.name}</a></h3>
<a href="product.html?productId=${product.id}"><sklep:productPicture product="${product}" format="news"/></a>
<p>Cena: <strong class="price"><sklep:price price="${product.defaultSku.finalPrice}"/></strong></p>
<form action="addProductToCart.do" method="post">
<input type="hidden" name="skuId" value="${product.defaultSku.id}" />
<a href="product.html?productId=${product.id}" class="detailsButton"><span>Zobacz</span></a>
<button type="submit" class="buyNowButton"><span>Kup teraz!</span></button>
</form>
</li>



</c:forEach>

</ul>

<div style="clear: both;"></div>
</div>
</div>



<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sklep" %>
<div id="basket-preview" class="box">
<h2>Koszyk</h2>
<div class="box-content">
	<c:choose>
	<c:when test="${shoppingCart.itemCount > 0}">

		<ul id="cartPreviewProducts">
		<c:forEach items="${shoppingCart.skuItems}" var="item">
			<li>
				<span class="quantity">${item.quantity} x</span>
				<span class="productName">${item.sku.name}</span> 
				<span class="productPrice"><sklep:price price="${item.totalPrice}"/></span>
			</li>
		</c:forEach>
		<c:forEach items="${shoppingCart.compositeOrderItems}" var="item">
			<li>
				<span class="quantity">1 x</span>
				<span class="productName"> ${item.compositeProduct.name}</span>
				<span class="productPrice"><sklep:price price="${item.unitPrice}"/></span> 
			</li>
		</c:forEach>
		</ul>
	
		<div class="totalPrice"><sklep:price price="${shoppingCart.totalPrice}"/></div>

		<c:if test="${!param.hideButtons}">
			<div id="cart_buttons">
				<a href="cart.html" class="toCartButton"  title="Do koszyka">
					<span class="buttonText">Do koszyka/kasy</span>
				</a>
			</div>
		</c:if>
		

	</c:when>
	<c:otherwise>
		... jest pusty
	</c:otherwise>
</c:choose></div>
</div>
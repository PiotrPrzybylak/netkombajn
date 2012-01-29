<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sklep" %>


<div id="cart" class="box">
<h2>Co jest w moim koszyku?</h2>
<div class="box-content">

<form method="post" action="updateCart.do">
<input type="hidden" name="method" value="doUpdateCart"/>
<table border="0" width="100%" cellspacing="0" cellpadding="4" class="productListing"> 
  <tr> 
    <td class="productListing-heading">Nazwa</td> 
    <td align="center" class="productListing-heading">Usuń</td> 
    <td align="center" class="productListing-heading">Produkty</td> 
    <td align="right" class="productListing-heading">Suma</td> 
  </tr> 

<c:forEach var="item" items="${shoppingCart.skuItems}" varStatus="i">
  <tr class="productListing-${(i.index) % 2 == 0 ? 'even' : 'odd'}"> 
   
    <td class="productListing-data productNameCell">
    	<a href="product.html?productId=${item.sku.parentProduct.id}"><span class="productName">${item.sku.parentProduct.name}</span><sklep:productPicture product="${item.sku.parentProduct}" format="cart"/></a>
    </td> 
    <td align="center" class="productListing-data" valign="top">
    <!--input type="checkbox" name="sku_cart_delete[]" value="${item.sku.id}" / -->
    <a href="updateCart.do?method=doRemove&sku_cart_delete[]=${item.sku.id}" >usuń</a>
    </td> 
    <td align="center" class="productListing-data" valign="top">
    	<input type="text" name="cart_quantity[]" value="${item.quantity}" size="4" class="textInput" />
    	<input type="hidden" name="sku_id[]" value="${item.sku.id}" />
    </td> 
    <td align="right" class="productListing-data" valign="top"><b><sklep:price price="${item.totalPrice}"/></b></td> 
  </tr> 
</c:forEach>

<c:forEach var="compositeItem" items="${shoppingCart.compositeOrderItems}" varStatus="i">
 	<tr>
 		<td align="center" class="productListing-data" valign="top">
 			<input type="checkbox" name="composite_cart_delete[]" value="${compositeItem.compositeProduct.id}" />
 		</td> 
    	 <td class="productListing-data">
    	 	<span class="important">${compositeItem.compositeProduct.name}</span>
    	 </td>
    	 <td align="center" class="productListing-data">
 			1	
 		</td> 
 		 <td align="right" class="productListing-data">
 		 	<b><sklep:price price="${compositeItem.unitPrice}"/></b>	
 		</td>
 	</tr>
 	<tr>
 		<td></td>
 		<td colspan="3">Zestaw zawiera : </td>
 	</tr>
 	
	<c:forEach var="singleItem" items="${compositeItem.singleOrderItems}" varStatus="i">
	 	<tr>	
	 		<td></td>
	 		<td colspan="3">
	 			${singleItem.product.name}
	 		</td>
	 	</tr>
 	</c:forEach>
 	
</c:forEach>

</table> 

<span class="totalPrice">Razem: <sklep:price price="${shoppingCart.totalPrice}"/></span>

<button type="submit" class="updateCartButton"><span class="buttonText">Aktualizuj koszyk</span></button>
<a href="${configuration.checkout}" class="checkoutButton"><span class="buttonText">Do kasy</span></a>

<div class="clear"></div>
</form>
</div>
</div><!-- cart -->

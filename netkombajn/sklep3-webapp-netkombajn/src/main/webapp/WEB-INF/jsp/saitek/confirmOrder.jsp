<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sklep" %>

<div id="confirmOrder" class="box">
<h2>Potwierdź zamówienie</h2>
<div class="box-content">

<div class="bread-crumbs confirmOrder-step">
<span class="breadcrumbs-shipmentAddress"><a href="${flowExecutionUrl}&_eventId=breadCrumbs-shipmentAddress" >Adres dostawy</a></span> &raquo;
<span class="breadcrumbs-shipmentOption"><a href="${flowExecutionUrl}&_eventId=breadCrumbs-shipmentOptions" >Sposób wysyłki</a> </span>&raquo;
<span class="breadcrumbs-paymentOption"><a href="${flowExecutionUrl}&_eventId=breadCrumbs-paymentOptions" >Forma płatności</a></span> &raquo;
<span class="breadcrumbs-confirmOrder">Podsumowanie</span>
</div>


	 <table border="0" width="100%" cellspacing="1" cellpadding="3" id="confirmOrderDetailsTable"> 
       <tr> 
        <td valign="top" width="30%" class="ProductHead"><b>Adres dostawy</b></td> 
        <td class="ProductHead" valign="top" width="70%">${orderModel.order.recipient.firstName} ${orderModel.order.recipient.lastName}<br>
			ul. ${orderModel.order.recipient.shipmentAddress.streetName} ${orderModel.order.recipient.shipmentAddress.streetNumber}<c:if test="${not empty orderModel.order.recipient.shipmentAddress.flatNumber}">/${orderModel.order.recipient.shipmentAddress.flatNumber}
			</c:if>			 
			 <br>
			${orderModel.order.recipient.shipmentAddress.city}  ${orderModel.order.recipient.shipmentAddress.postalCode}<br>
<%--	woj. ${orderModel.order.recipient.shipmentAddress.voivodeship.name} --%>		

</td> 
       </tr> 
 
 
<c:if test="${orderModel.sendInvoiceAddress}">
       <tr> 
        <td valign="top" width="30%" class="ProductHead"><b>Dane do faktury</b></td> 
 		 <td class="ProductHead" valign="top" width="70%">${orderModel.invoice.companyName}<br>
			ul. ${orderModel.invoice.invoiceAddress.streetName} ${orderModel.invoice.invoiceAddress.streetNumber}<c:if test="${not empty orderModel.invoice.invoiceAddress.flatNumber}">/${orderModel.invoice.invoiceAddress.flatNumber}
			</c:if>			 
			 <br>
			 ${orderModel.invoice.nip}
			 <br>
			${orderModel.invoice.invoiceAddress.city}  ${orderModel.invoice.invoiceAddress.postalCode}<br>
<%--
        <td class="ProductHead" valign="top" width="70%">${orderModel.order.customer.payerName}<br>
        <c:if test="${!empty orderModel.order.customer.nip}">${orderModel.order.customer.nip}<br></c:if>
		ul. ${orderModel.order.shipmentAddress.street}<br> ${orderModel.order.shipmentAddress.city}  ${orderModel.order.shipmentAddress.postalCode}<br></td>
		
		 --%>
       </tr> 
       
</c:if>
 
       <tr> 
    	<td valign="top" width="30%" class="ProductHead"><b>Sposób wysyłki</b></td> 
        <td class="ProductHead" valign="top" width="70%">${orderModel.order.shipmentOption.name}</td> 
       </tr> 
       

       <tr> 
        <td valign="top" width="30%" class="ProductHead"><b>Sposób zapłaty</b></td> 
        <td class="ProductHead" valign="top" width="70%"><fmt:message key="PaymentOption.${orderModel.order.payment.form}"/></td> 
       </tr> 

     </table> 



<table width="100%" cellspacing="1" cellpadding="2" border="0" id="confirmOrderProductsTable">
	<tbody>
    	<tr>
           <th align="center" class="ProductHead"><b><u>Nazwa produktu</u></b></th>
           <th align="center" class="ProductHead"><b><u>Ilość</u></b></th>
           <th align="right" class="ProductHead"><b><u>Wartość brutto</u></b></th>
        </tr>

		<c:forEach items="${orderModel.order.skuOrderItems}" var="item">
			<tr>
				<td width="70%" valign="top" class="main">${item.sku.parentProduct.name}</td>
				<td width="5%" valign="top" align="center" class="main">${item.quantity}</td>
				<td width="25%" valign="top" align="right" class="main"><sklep:price price="${item.totalPrice}"/>
				
				<c:if test="${not item.theSamePrice}">
						<span class="warning">(cena produktu zmieniła się! Aktualna wartosć w sklepie to <sklep:price price="${item.product.defaultSku.finalPrice}"/>)</span>
				</c:if>
				
				</td>
				
			</tr>
		</c:forEach>
		<c:forEach items="${orderModel.order.compositeOrderItems}" var="compositeItem">
			<tr>
				<td width="70%" valign="top" class="main"><span class="important">${compositeItem.compositeProduct.name}</span></td>
				<td width="5%" valign="top" align="center" class="main">1</td>
				<td width="25%" valign="top" align="right" class="main"><sklep:price price="${compositeItem.unitPrice}"/></td>
			</tr>
			<tr>
		 		<td colspan="3">Zestaw zawiera : </td>
		 	</tr>
		 	
			<c:forEach var="singleItem" items="${compositeItem.singleOrderItems}" varStatus="i">
			 	<tr>	
			 		<td colspan="3">
			 			${singleItem.product.name}
			 		</td>
			 	</tr>
		 	</c:forEach>
		</c:forEach>

	</tbody>
</table>


<div id="orderCostSum">
	<p><span>Wartość zamówionych towarów:</span><span class="totalPrice"><sklep:price price="${orderModel.order.totalWithoutShipping}"/></span></p>
	<p><span>Koszty wysłyki:</span><span class="totalPrice"><sklep:price price="${orderModel.order.shippingCost}"/></span></p>
	<p><span>Razem wartość zamówienia brutto:</span><span class="totalPrice"><sklep:price price="${orderModel.order.totalWithShipping}"/></span></p>	
	<div class="clear"></div>
</div>

<form:form>
<div id="checkoutFlowNavigation">
	<button type="submit" class="backButton" name="_eventId_back"><span class="buttonText">Cofnij</span></button>
	<c:choose>
	<c:when test="${orderModel.order.notEmpty}">
		<button type="submit" class="confirmOrderButton" name="_eventId_continue"><span class="buttonText">Potwierdź zamówienie</span></button>
	</c:when>
	<c:otherwise>
		Koszyk jest pusty! Dodaj produkty do koszyka i zatwierdź zamówienie.
	</c:otherwise>
	</c:choose>
</div>
</form:form>






</div>
</div> <!-- "confirmOrder"  -->

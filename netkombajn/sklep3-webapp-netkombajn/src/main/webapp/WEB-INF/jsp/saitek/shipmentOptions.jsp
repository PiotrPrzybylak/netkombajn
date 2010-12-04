<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sklep" %>

<div id="shipmentOptions" class="box">
<h2>Informacje o dostawie</h2>
<div class="box-content">

<div class="bread-crumbs shipmentOption-step" >
<span class="breadcrumbs-shipmentAddress"><a href="${flowExecutionUrl}&_eventId=breadCrumbs-shipmentAddress" >Adres dostawy</a></span> &raquo;
<span class="breadcrumbs-shipmentOption">Sposób wysyłki</span> &raquo;
<span class="breadcrumbs-paymentOption">Forma płatności</span> &raquo;
<span class="breadcrumbs-confirmOrder">Podsumowanie</span>
</div>

<form:form commandName="orderModel" >
 	<form:errors path="*" cssClass="formErrors"/>


            				<ul>
            					<c:forEach items="${shipmentOptions}" var="shipmentOption">
            						<li>
            						<input name="order.shipmentOption" type="radio" value="${shipmentOption.id}" ${shipmentOption.id == orderModel.order.shipmentOption.id ? 'checked="checked"' : ''}/> 
            						
            							${shipmentOption.name} <sklep:shipmentPrice shipment="${shipmentOption}" totalWeight="${orderModel.order.totalWeight}"/> --  ${shipmentOption.cost}
            						</li>
            					</c:forEach>
            				
            						
            				</ul>


<h5>Dodaj komentarz do zamówienia</h5>
<form:textarea path="order.comments"/>


<div id="checkoutFlowNavigation">
	<button type="submit" class="backButton" name="_eventId_back"><span class="buttonText">Cofnij</span></button>
	<button type="submit" class="continueButton" name="_eventId_continue"><span class="buttonText">Kontynuuj</span></button>
</div>


</form:form>

</div>
</div><!-- shipmentOptions -->


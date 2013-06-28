<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div id="paymentOptions" class="box">
<h2>Informacje o płatności</h2>
<div class="box-content">

<div class="bread-crumbs paymentOption-step">
<span class="breadcrumbs-shipmentAddress"><a href="${flowExecutionUrl}&_eventId=breadCrumbs-shipmentAddress" >Adres dostawy</a></span> &raquo;
<span class="breadcrumbs-shipmentOption"><a href="${flowExecutionUrl}&_eventId=breadCrumbs-shipmentOptions" >Sposób wysyłki</a> </span>&raquo;
<span class="breadcrumbs-paymentOption">Forma płatności</span> &raquo;
<span class="breadcrumbs-confirmOrder">Podsumowanie</span>
</div>

<form:form commandName="orderModel">
 	 	<form:errors path="*" cssClass="formErrors"/>

	<div class="paymentHeader">Sposób zapłaty</div>


<ul>
<c:forEach items="${orderModel.order.shipmentOption.availablePaymentForms}" var="paymentForm">
	<li>
		<span class="paymentOptionText">
			<fmt:message key="PaymentOption.${paymentForm}"/>
		</span>
		<span>
			<form:radiobutton path="order.payment.form" value="${paymentForm}"/>
		</span>
	</li>
</c:forEach>

<c:if test="${configuration.testPaymentEnabled}"> 
	<li>
		<span class="paymentOptionText">
			<fmt:message key="PaymentOption.TEST"/>
		</span>
		<span>
			<form:radiobutton path="order.payment.form" value="TEST"/>
		</span>
	</li>
</c:if>

</ul>
	
<div id="checkoutFlowNavigation">
	<button type="submit" class="backButton" name="_eventId_back"><span class="buttonText">Cofnij</span></button>
	<button type="submit" class="continueButton" name="_eventId_continue"><span class="buttonText">Kontynuuj</span></button>
</div>

</form:form>



</div>
</div><!-- paymentOptions -->
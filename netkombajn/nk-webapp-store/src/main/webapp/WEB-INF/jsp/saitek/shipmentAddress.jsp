	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<div id="shipmentData" class="box">
<h2>Podaj swoje dane</h2>
<div class="box-content">

<div class="bread-crumbs shipmentAddress-step" >
<span class="breadcrumbs-shipmentAddress">Adres dostawy</span> &raquo;
<span class="breadcrumbs-shipmentOption">Sposób wysyłki</span> &raquo;
<span class="breadcrumbs-paymentOption">Forma płatności</span> &raquo;
<span class="breadcrumbs-confirmOrder">Podsumowanie</span>
</div>

<form:form commandName="orderModel">



<span id="inputRequirementLegend" class="inputRequirement">
Pola znaczone * są obowiązkowe do wypełnienia  
</span>



		<form:errors path="*" cssClass="formErrors"></form:errors>

<fieldset>
<legend>Adres dostawy</legend>
<p class="formTextField"><label for="order.recipient.firstName">Imię:</label><form:input path="order.recipient.firstName" cssClass="textInput" cssErrorClass="fieldWithError textInput" /><span class="inputRequirement">*</span></p>
<p class="formTextField"><label for="order.recipient.lastName">Nazwisko:</label><form:input path="order.recipient.lastName" cssClass="textInput" cssErrorClass="fieldWithError textInput" /><span class="inputRequirement">*</span></p>
<p class="formTextField"><label for="order.recipient.shipmentAddress.streetName">Ulica:</label><form:input path="order.recipient.shipmentAddress.streetName" cssClass="textInput" cssErrorClass="fieldWithError textInput" /><span class="inputRequirement">*</span></p>
<p class="formTextField"><label for="order.recipient.shipmentAddress.streetNumber">Numer:</label><form:input path="order.recipient.shipmentAddress.streetNumber" cssClass="textInput" cssErrorClass="fieldWithError textInput" /><span class="inputRequirement">*</span></p>
<p class="formTextField"><label for="order.recipient.shipmentAddress.flatNumber">Numer mieszkania :</label><form:input path="order.recipient.shipmentAddress.flatNumber" cssClass="textInput" cssErrorClass="fieldWithError textInput" /></p>
<p class="formTextField"><label for="order.recipient.shipmentAddress.postalCode">Kod pocztowy:</label><form:input path="order.recipient.shipmentAddress.postalCode" cssClass="textInput" cssErrorClass="fieldWithError textInput" /><span class="inputRequirement">*</span></p>
<p class="formTextField"><label for="order.recipient.shipmentAddress.city">Miasto:</label><form:input path="order.recipient.shipmentAddress.city" cssClass="textInput" cssErrorClass="fieldWithError textInput" /><span class="inputRequirement">*</span></p>
<p class="formTextField"><label for="customerEmail">E-mail:</label><form:input path="customerEmail" cssClass="textInput" cssErrorClass="fieldWithError textInput" /><span class="inputRequirement">*</span></p>
<p class="formTextField"><label for="order.recipient.phone">Telefon:</label><form:input path="order.recipient.phone" cssClass="textInput" cssErrorClass="fieldWithError textInput" /><span class="inputRequirement">*</span></p>

</fieldset>


<form:checkbox  value="true" class="checkbox2" path="sendInvoiceAddress" id="invoiceCheckbox"/>
Chcę otrzymać fakturę VAT

<fieldset id="invoiceData">
<legend>Dane do faktury</legend>
<p class="formTextField"><label for="invoice.companyName">Nazwa firmy:</label><form:input path="invoice.companyName" cssClass="textInput" cssErrorClass="textInput fieldWithError"/><span class="inputRequirement">*</span></p>
<p class="formTextField"><label for="invoice.nip">Nip:</label><form:input path="invoice.nip" cssClass="textInput" cssErrorClass="fieldWithError textInput" /><span class="inputRequirement">*</span></p>
<p class="formTextField"><label for="invoice.invoiceAddress.streetName">Ulica:</label><form:input path="invoice.invoiceAddress.streetName" cssClass="textInput" cssErrorClass="fieldWithError textInput" /><span class="inputRequirement">*</span></p>
<p class="formTextField"><label for="invoice.invoiceAddress.streetNumber">Numer:</label><form:input path="invoice.invoiceAddress.streetNumber" cssClass="textInput" cssErrorClass="fieldWithError textInput" /><span class="inputRequirement">*</span></p>
<p class="formTextField"><label for="invoice.invoiceAddress.flatNumber">Numer mieszkania:</label><form:input path="invoice.invoiceAddress.flatNumber" cssClass="textInput" cssErrorClass="fieldWithError textInput" /></p>
<p class="formTextField"><label for="invoice.invoiceAddress.postalCode">Kod pocztowy:</label><form:input path="invoice.invoiceAddress.postalCode" cssClass="textInput" cssErrorClass="fieldWithError textInput" /><span class="inputRequirement">*</span></p>
<p class="formTextField"><label for="invoice.invoiceAddress.city">Miasto:</label><form:input path="invoice.invoiceAddress.city" cssClass="textInput" cssErrorClass="fieldWithError textInput" /><span class="inputRequirement">*</span></p>
</fieldset>


<fieldset>
<legend>Oświadczenie</legend>

<div id="acceptPolicy">
<form:checkbox path="rulesAccepted" cssClass="checkbox"/><span class="inputRequirement">*</span>
${acceptShopPolicyText.text}

</fieldset>
 </div>

<div id="checkoutFlowNavigation">
	<button type="submit" class="backButton" name="_eventId_back"><span class="buttonText">Cofnij</span></button>
	<button type="submit" class="continueButton" name="_eventId_continue"><span class="buttonText">Kontynuuj</span></button>
	

</div>

</form:form>
</div>
</div><!-- shipmentData -->


<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<div id="shipmentData" class="box">
<h2>Podaj swoje dane</h2>
<div class="box-content">


<form:form commandName="orderModel">

<%-- 
Proszę wybrać rodzaj osobowości prawnej:

<form:radiobutton id="personCustmerTypeButton" value="PERSON" path="order.customer.customerType" />
Osoba fizyczna        
<form:radiobutton id="companyCustmerTypeButton" value="COMPANY" path="order.customer.customerType"/>
Firma
--%>

<span id="inputRequirementLegend" class="inputRequirement">
Pola znaczone * są obowiązkowe do wypełnienia  
</span>



		<form:errors path="*" cssClass="formErrors"></form:errors>

<fieldset>
<legend>Dane osobowe</legend>
<p class="formTextField"><label for="order.recipient.firstName">Imię:</label><form:input path="order.recipient.firstName" cssClass="textInput" cssErrorClass="fieldWithError textInput" /><span class="inputRequirement">*</span></p>
<p class="formTextField"><label for="order.recipient.lastName">Nazwisko:</label><form:input path="order.recipient.lastName" cssClass="textInput" cssErrorClass="fieldWithError textInput" /><span class="inputRequirement">*</span></p>
<p class="formTextField"><label for="order.recipient.email">Email:</label><form:input path="order.recipient.email" cssClass="textInput" cssErrorClass="fieldWithError textInput" /><span class="inputRequirement">*</span></p>
<p class="formTextField"><label for="order.recipient.phone">Telefon:</label><form:input path="order.recipient.phone" cssClass="textInput" cssErrorClass="fieldWithError textInput" /><span class="inputRequirement">*</span></p>
<p class="formTextField"><label for="order.recipient.companyName">Nazwa Firmy:</label><form:input path="order.recipient.companyName" cssClass="textInput" cssErrorClass="fieldWithError textInput" /></p>


</fieldset>
<fieldset>
<legend>Uwagi</legend>
<p class="formTextField"><label for="order.comments">Komentarz:</label><form:textarea path="order.comments" cssClass="textInput" cssErrorClass="fieldWithError textInput" rows="5" cols="33" /></p>
</fieldset>

<%-- 


<fieldset>
<legend>Dane Kontaktowe</legend>
<p class="formTextField"><label for="order.customer.email">E-mail:</label><form:input path="order.customer.email" cssClass="textInput" cssErrorClass="fieldWithError textInput" /><span class="inputRequirement">*</span></p>
<p class="formTextField"><label for="emailConfirmation">Potwierdź adres e-mail:</label><form:input path="emailConfirmation" cssClass="textInput" cssErrorClass="fieldWithError textInput" /><span class="inputRequirement">*</span></p>
<p class="formTextField"><label for="order.customer.phoneNumber">Telefon:</label><form:input path="order.customer.phoneNumber" cssClass="textInput" cssErrorClass="fieldWithError textInput" /><span class="inputRequirement">*</span></p>
<p class="formTextField"><label for="order.customer.fax">Faks:</label><form:input path="order.customer.fax" cssClass="textInput" cssErrorClass="fieldWithError textInput" /></p>
</fieldset>



<fieldset>
<legend>Oświadczenie</legend>
<p><form:checkbox path="rulesAccepted" cssClass="checkbox"/><span class="inputRequirement">*</span>Oświadczam, że zapoznałem się regulaminem i akceptuję jego warunki.</p>
<p>Wyrażam zgodę na przetwarzanie moich danych osobowych w celu realizacji zamówienia przez firmę Power Computer Marcin Jabłoński zgodnie z ustawą z dnia 29 sierpnia 1997r. o ochronie danych osobowych (tekst.jedn.Dz.U.02.101.926 z póżn.zm). Oświadczam, że zostałem poinformowany, iż podanie moich danych osobowych ma charakter dobrowolny oraz, że przysługuje mi prawo do wglądu, korekty, usunięcia oraz kontroli swoich danych osobowych. Zlecenie usunięcia danych osobowych może odbyć się poprzez wysłanie zlecenia w formie pisemnej na adres: Al. Piłsudskiego 142 92-230 Łódź (042) 674-33-18 Ul. Rzgowska 56A 934-172 Łódź (042) 663-75-74, lub droga elektroniczną na adres e-mail: info@saitek.az.pl.</p> 
</fieldset>

--%>
 
<div id="checkoutFlowNavigation">
	<button type="submit" class="backButton" name="_eventId_back"><span class="buttonText">Cofnij</span></button>
	<button type="submit" class="continueButton" name="_eventId_continue"><span class="buttonText">Kontynuuj</span></button>
	

</div>

</form:form>
</div>
</div><!-- shipmentData -->


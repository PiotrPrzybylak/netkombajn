package webflow.validation;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang.StringUtils;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;

import pl.netolution.sklep3.domain.ShipmentOption;
import pl.netolution.sklep3.service.ValidationService;
import pl.netolution.sklep3.web.domain.WebOrder;

//TODO zabezpieczenia przed xss
public class WebOrderValidator {

	private ValidationService validationService;

	public void validatePayment(WebOrder webOrder, MessageContext context) {
		validatePaymentForm(webOrder, context);
	}

	public void validatePaymentOptions(WebOrder webOrder, MessageContext context) {
		validatePaymentForm(webOrder, context);
	}

	private void validatePaymentForm(WebOrder webOrder, MessageContext context) {
		if (webOrder.getOrder().getPayment().getForm() == null) {
			context.addMessage(new MessageBuilder().error().source("order.payment.form").code("missing_payment_form").defaultText(
					"Wybierz formę płatności").build());
		}
	}

	public void validateShipment(WebOrder webOrder, MessageContext context) {
		validateShipmentOption(webOrder.getOrder().getShipmentOption(), context);
		validateStreetNumber(webOrder, context);
		validatePostalCode(webOrder, context);
		validateInvoiceData(webOrder, context);
	}

	public void validateShipmentOptions(WebOrder webOrder, MessageContext context) {
		validateShipmentOption(webOrder.getOrder().getShipmentOption(), context);
	}

	private void validatePostalCode(WebOrder webOrder, MessageContext context) {
		if (!validationService.isPostalCodeValid(webOrder.getOrder().getRecipient().getShipmentAddress().getPostalCode())) {
			context.addMessage(new MessageBuilder().error().source("order.recipient.shipmentAddress.postalCode").code("postalCode_format")
					.defaultText("Zły format pola 'Kod pocztowy'.(prawidłowe to np. (11-111, 11111)").build());
		}
	}

	private void validateStreetNumber(WebOrder webOrder, MessageContext context) {
		if (!validationService.isStreetNumberValid(webOrder.getOrder().getRecipient().getShipmentAddress().getStreetNumber())) {
			context.addMessage(new MessageBuilder().error().source("order.recipient.shipmentAddress.streetNumber").code(
					"streetNumber_format")
					.defaultText("Zły format pola numer ulicy.(prawidłowe to np. 11, 11/13)").build());
		}
	}

	private void validateShipmentOption(ShipmentOption shipmentOption, MessageContext context) {
		if (shipmentOption == null) {
			context.addMessage(new MessageBuilder().error().source("order.shipmentOption").code("orderShipmentOption.required")
					.defaultText("Musisz wybrac sposób dostawy.").build());
		}
	}

	public void validateShipmentAddress(WebOrder webOrder, MessageContext context) {
		if (!webOrder.isRulesAccepted()) {
			context.addMessage(new MessageBuilder().error().source("rulesAccepted").code("rulesAccepted.required")
					.build());
		}
		validatePostalCode(webOrder, context);
		validateStreetNumber(webOrder, context);
		validateEmail(context, webOrder.getCustomerEmail());
		validateInvoiceData(webOrder, context);
	}

	public void validateInvoiceData(WebOrder webOrder, MessageContext context) {
		if (webOrder.isSendInvoiceAddress()) {
			validateInvoiceCity(webOrder, context);
			validateInvoicePostalCode(webOrder, context);
			validateInvoiceStreetName(webOrder, context);
			validateInvoiceStreetNumber(webOrder, context);
			validateInvoiceNip(webOrder, context);
			validateInvoiceCompanyName(webOrder, context);
		}
	}

	private void validateInvoiceStreetNumber(WebOrder webOrder, MessageContext context) {

		if (!validationService.isStreetNumberValid(webOrder.getInvoice().getInvoiceAddress().getStreetNumber())) {
			context.addMessage(new MessageBuilder().error().source("invoice.invoiceAddress.streetNumber").code(
					"invoiceAddress.streetNumber").defaultText(
					"Pole 'Numer Ulicy' w adresie faktury ma zły format(prawidłowe to np. 11, 11/13)").build());
		}
	}

	private void validateInvoiceStreetName(WebOrder webOrder, MessageContext context) {
		if (StringUtils.isEmpty(webOrder.getInvoice().getInvoiceAddress().getStreetName())) {
			context.addMessage(new MessageBuilder().error().source("invoice.invoiceAddress.streetName").code(
					"invoiceAddress.streetName").defaultText("Pole 'Ulica' w adresie faktury jest wymagane.").build());
		}
	}

	private void validateInvoicePostalCode(WebOrder webOrder, MessageContext context) {
		if (!validationService.isPostalCodeValid(webOrder.getInvoice().getInvoiceAddress().getPostalCode())) {
			context.addMessage(new MessageBuilder().error().source("invoice.invoiceAddress.postalCode").code(
					"invoiceAddress.postalCode").defaultText("Pole 'Kod pocztowy' w adresie faktury ma zły format(11-111, 11111)")
					.build());
		}
	}

	private void validateInvoiceCity(WebOrder webOrder, MessageContext context) {
		if (StringUtils.isEmpty(webOrder.getInvoice().getInvoiceAddress().getCity())) {
			context.addMessage(new MessageBuilder().error().source("invoice.invoiceAddress.city").code("invoiceAddress.address.city")
					.defaultText("Pole 'Miasto' w adresie faktury jest wymagane.").build());
		}
	}

	private void validateInvoiceNip(WebOrder webOrder, MessageContext context) {
		if (webOrder.getInvoice().getNip() == null) {
			context.addMessage(new MessageBuilder().error().source("invoice.nip").code("invoice.nip.required")
					.defaultText("Pole 'NIP' w danych do faktury jest wymagane.").build());
		}
	}

	private void validateInvoiceCompanyName(WebOrder webOrder, MessageContext context) {
		if (StringUtils.isEmpty(webOrder.getInvoice().getCompanyName())) {
			context.addMessage(new MessageBuilder().error().source("invoice.companyName").code(
					"invoice.companyName").defaultText("Pole 'Nazwa firmy' w danych do faktury jest wymagane.").build());
		}
	}

	//TODO Jedna walidacja dla jednego typu zakupu
	/*
	 * public void validateShipmentOptions(WebOrder webOrder, MessageContext context) {
	 * 
	 * validateEmail(context);
	 * 
	 * if (!rulesAccepted) { context.addMessage(new
	 * MessageBuilder().error().source("rulesAccepted").code("rulesAccepted.required").defaultText(
	 * "Musisz zaakceptować regulamin.").build()); }
	 * 
	 * if (CustomerType.COMPANY.equals(getOrder().getCustomer().getCustomerType())) {
	 * 
	 * if (getOrder().getCustomer().getNip() == null) { context.addMessage(new
	 * MessageBuilder().error().source("order.customer.nip").code("nip.requiredForCompany").defaultText(
	 * "NIP is required for a company.").build()); }
	 * 
	 * if (!StringUtils.hasText(getOrder().getCustomer().getCompanyName())) { context.addMessage(new
	 * MessageBuilder().error().source("order.customer.companyName").code("companyName.requiredForCompany")
	 * .defaultText("Company name is required.").build()); }
	 * 
	 * } }
	 * 
	 * 
	 * public void validateShipmentOptions(WebOrder webOrder, MessageContext context) { if (webOrder.getOrder().getShipmentOption() == null)
	 * { context.addMessage(new MessageBuilder().error().source("order.shipmentOption").code("orderShipmentOption.required")
	 * .defaultText("You must choose shipment option.").build()); } }
	 * 
	 * public void validatePaymentOptions(WebOrder webOrder, MessageContext context) { if (webOrder.getOrder().getPaymentOption() == null) {
	 * context.addMessage(new MessageBuilder().error().source("order.paymentOption").code("order.paymentOption.required").defaultText(
	 * "You must choose payment option.").build()); }
	 * 
	 * if (webOrder.getOrder().getSaleDocument() == null) { context.addMessage(new
	 * MessageBuilder().error().source("order.paymentOption").code("order.saleDocument.required").defaultText(
	 * "You must choose sale document type.").build()); } }
	 */

	//	private void validateEmail(MessageContext context, WebOrder webOrder) {
	//		boolean isNotEmptyEmail =
	//				webOrder.getOrder().getCustomer().getEmail() != null;
	//
	//		if (!validationService.isEmailformatProper(webOrder.getOrder().getCustomer().getEmail())) {
	//			context.addMessage(new
	//					MessageBuilder().error().source("order.customer.email").code("order.customer.email.incorrect")
	//					.defaultText("Incorrect email format.").build());
	//		} else {
	//			validateRepeatedEmail(context, webOrder);
	//			validateEmailDomain(context,
	//					isNotEmptyEmail, webOrder);
	//		}
	//
	//	}

	//	private void validateRepeatedEmail(MessageContext context, WebOrder webOrder) {
	//		if (!StringUtils.equals(webOrder.getOrder().getCustomer().getEmail(), webOrder.getEmailConfirmation())) {
	//			context.addMessage(new
	//					MessageBuilder().error().source("emailConfirmation").code("emailsNotEqual").defaultText(
	//					"Podane adresy emaile nie są jednakowe.").build());
	//		}
	//	}

	//	private void validateEmailDomain(MessageContext context, WebOrder webOrder) {
	//
	//		String email = webOrder.getOrder().getCustomer().getEmail();
	//		String emailDomain = email.replaceAll(".@", "");
	//
	//		boolean isDomainValid = isDomainValid(emailDomain);
	//
	//		if (!isDomainValid) {
	//			context.addMessage(new
	//					MessageBuilder().error().source("order.customer.email").code("emailsDomainNotValid").defaultText(
	//					"Podane konto email nie istnieje.").build());
	//		}
	//
	//	}

	private void validateEmailDomain(MessageContext context, String email) {
		if (StringUtils.isEmpty(email)) {
			context.addMessage(new
					MessageBuilder().error().source("order.recipient.email").code("emailsDomainNotValid").defaultText(
					"Pole 'Email' jest wymagane.").build());
			return;
		}
		String emailDomain = email.substring(email.indexOf("@")+1);

		boolean isDomainValid = isDomainValid(emailDomain);

		if (!isDomainValid) {
			context.addMessage(new
					MessageBuilder().error().source("order.recipient.email").code("emailsDomainNotValid").defaultText(
					"Podane konto email nie istnieje.").build());
		}

	}

	// TODO sprobowac napisac te funkcje bez wykorzystywania wyjatkow
	private boolean isDomainValid(String domain) {
		try {
			InetAddress.getByName(domain);
		} catch (UnknownHostException e) {
			return false;
		}
		return true;
	}

	public void setValidationService(ValidationService validationService) {
		this.validationService = validationService;
	}

	public void validateSimpleCheckout(WebOrder webOrder, MessageContext context) {

		String email = webOrder.getOrder().getRecipient().getEmail();

		validateEmail(context, email);

	}

	private void validateEmail(MessageContext context, String email) {
		if (!StringUtils.isEmpty(email)) {
			if (!validationService.isEmailformatProper(email)) {
				context.addMessage(new MessageBuilder().error().source("customerEmail").code("customerEmail.incorrect")
						.defaultText("Błędny format emaila.").build());
			} else {
				validateEmailDomain(context, email);
			}
		}else{
			context.addMessage(new MessageBuilder().error().source("customerEmail").code("customerEmail.required")
					.defaultText("Pole Email jest wymagane").build());
		}
		
	}

}

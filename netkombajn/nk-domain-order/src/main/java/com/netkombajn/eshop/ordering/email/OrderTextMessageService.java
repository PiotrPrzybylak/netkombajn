package com.netkombajn.eshop.ordering.email;

import java.util.Map;

import org.apache.log4j.Logger;

import com.netkombajn.eshop.ordering.address.Address;
import com.netkombajn.eshop.ordering.invoice.Invoice;
import com.netkombajn.eshop.ordering.order.Order;
import com.netkombajn.eshop.ordering.order.item.SkuOrderItem;

import pl.netolution.sklep3.dao.TextDao;
import pl.netolution.sklep3.domain.Text;
import pl.netolution.sklep3.domain.enums.PaymentFormType;
import pl.netolution.sklep3.service.TextMessageService;

public class OrderTextMessageService {

	private static final String SIMPLE_CHECKOUT = "SIMPLE_ORDER";

	private static final String FULL_CHECKOUT_DEFAULT_EMAIL = "FULL_CHECKOUT_DEFAULT_EMAIL";

	private static final Logger log = Logger.getLogger(TextMessageService.class);

	private TextDao textDao;

	private Map<String, Map<PaymentFormType, String>> orderEmailsNames;

	public String getSimpleOrderEmail(Order order) {

		Text emailTemplate = textDao.findTextByName(SIMPLE_CHECKOUT);

		log.debug("Email template: " + emailTemplate + " found for name: " + SIMPLE_CHECKOUT);

		String resultText = emailTemplate.getText().replaceAll("#TOTAL_PRICE#", order.getTotalWithoutShipping().toString()).replaceAll(
				"#TOTAL_COST_WITH_SHIPPING#", order.getTotalWithShipping().toString()).replaceAll("#NUMER_ZAMOWIENIA#",
				order.getId().toString()).replaceAll("#ZAMOWIONE_POZYCJE#", getOrderItemsAsHtml(order));

		return resultText;
	}

	public String getOrderEmail(Order order) {
		String emailTemplateName = getEmailTemplateName(order);

		Text emailTemplate = textDao.findTextByName(emailTemplateName);

		if (emailTemplate == null) {
			emailTemplate = textDao.findTextByName(FULL_CHECKOUT_DEFAULT_EMAIL);
		}

		log.debug("Email template: " + emailTemplate + " found for name: " + emailTemplateName);

		String resultText = emailTemplate.getText().replaceAll("#TOTAL_PRICE#", order.getTotalWithoutShipping().toString()).replaceAll(
				"#TOTAL_COST_WITH_SHIPPING#", order.getTotalWithShipping().toString()).replaceAll("#NUMER_ZAMOWIENIA#",
				order.getId().toString()).replaceAll("#DANE_DO_FAKTURY#", getDaneDoFakturyAsHtml(order)).replaceAll("#TRANSPORT_PRICE#",
				order.getShipmentOption().getCost().toString()).replaceAll("#ADRES_DOSTAWY#",
				getAddressAsHtml(order.getRecipient().getShipmentAddress())).replaceAll("#ZAMOWIONE_POZYCJE#", getOrderItemsAsHtml(order));

		return resultText;
	}

	private String getDaneDoFakturyAsHtml(Order order) {
		StringBuilder stringBuilder = new StringBuilder();
		Invoice invoice = order.getInvoice();
		if (invoice != null) {
			stringBuilder.append("Dane do faktury:<br/>");
			stringBuilder.append(invoice.getCompanyName()).append("<br/>");
			stringBuilder.append("NIP: ").append(invoice.getNip().getCanonicalForm()).append("<br/>");
			;
			stringBuilder.append(getAddressAsHtml(invoice.getInvoiceAddress()));

		}
		return stringBuilder.toString();
	}

	private String getAddressAsHtml(Address invoiceAddress) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("ul. ").append(invoiceAddress.getStreetName()).append(" ").append(invoiceAddress.getStreetNumber());
		if (invoiceAddress.getFlatNumber() != null) {
			stringBuilder.append("/").append(invoiceAddress.getFlatNumber().toString());
		}
		stringBuilder.append("<br/>");

		stringBuilder.append(invoiceAddress.getCity()).append(" ");
		if (invoiceAddress.getPostalCity() != null) {
			stringBuilder.append(invoiceAddress.getPostalCity()).append(" ");
		}
		stringBuilder.append(invoiceAddress.getPostalCode()).append("<br/>");
		return stringBuilder.toString();
	}

	private String getOrderItemsAsHtml(Order order) {
		StringBuilder stringBuilder = new StringBuilder();
		for (SkuOrderItem orderItem : order.getSkuOrderItems()) {
			stringBuilder.append(orderItem.getSku().getName()).append(" (").append(orderItem.getUnitPrice()).append(" PLN) x ").append(
					orderItem.getQuantity()).append(" = ").append(orderItem.getTotalPrice()).append(" PLN").append("<br/>");
		}
		return stringBuilder.toString();
	}

	private String getEmailTemplateName(Order order) {
		String shipmentOptionCode = order.getShipmentOption().getCode();
		Map<PaymentFormType, String> emailNamesDependOnPayment = orderEmailsNames.get(shipmentOptionCode);
		PaymentFormType paymentFormType = order.getPayment().getForm().getType();
		String emailName = emailNamesDependOnPayment.get(paymentFormType);
		log.debug("Email template name found: " + emailName + " for shipmentOptionCode: " + shipmentOptionCode + " and paymentFormType: "
				+ paymentFormType);
		return emailName;
	}

	public void setTextDao(TextDao textDao) {
		this.textDao = textDao;
	}

	public void setOrderEmailsNames(Map<String, Map<PaymentFormType, String>> orderEmailsNames) {
		this.orderEmailsNames = orderEmailsNames;
	}

}

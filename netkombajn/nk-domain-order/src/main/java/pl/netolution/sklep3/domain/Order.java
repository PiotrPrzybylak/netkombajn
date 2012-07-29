package pl.netolution.sklep3.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import com.netkombajn.store.domain.shared.price.Price;

import pl.netolution.sklep3.domain.payment.InternalPayment;

@SuppressWarnings("serial")
@Entity
@Table(name = "orders")
public class Order implements Serializable {

	private static final String TO_FULFIL = "--";

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	@Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	private Customer customer;

	@ManyToOne
	@JoinColumn(name = "shipment_option_id")
	private ShipmentOption shipmentOption;

	private Date created;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	private boolean bulletin;

	private String comments;

	private String saleDocument;

	@OneToMany
	@JoinColumn(name = "order_id")
	@Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	private List<CompositeOrderItem> compositeOrderItems = new ArrayList<CompositeOrderItem>();

	@OneToMany
	@JoinColumn(name = "order_id")
	@Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	private List<SkuOrderItem> skuOrderItems = new ArrayList<SkuOrderItem>();

	@OneToMany
	@JoinColumn(name = "order_id")
	@Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@OrderBy("changeDate")
	private final List<OrderHistoryRecord> orderHistory = new LinkedList<OrderHistoryRecord>();

	private Invoice invoice;

	private Recipient recipient = new Recipient();

	private String description;
	@Transient
	private double totalWeight;

	@OneToOne(mappedBy = "order")
	@Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	private InternalPayment payment;

	public Order() {
		setPayment(new InternalPayment());
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		if (this.status != status) {
			this.status = status;
			addRecordToHistory(status, null);
		}
	}

	public void setStatus(OrderStatus status, String description) {
		if (this.status != status) {
			this.status = status;
			addRecordToHistory(status, description);
		}
	}

	// TODO extract history functionality out of order class.
	private void addRecordToHistory(OrderStatus status, String description) {
		OrderHistoryRecord record = new OrderHistoryRecord(status, this);
		record.setDescription(description);
		orderHistory.add(record);
	}

	public Price getTotalWithoutShipping() {

		Price total = new Price();

		total = addCompositeProductPrice(total);
		total = addSkuPrices(total);

		return total;
	}

	private Price addSkuPrices(Price total) {
		for (SkuOrderItem skuOrderItem : skuOrderItems) {
			total = total.add(skuOrderItem.getTotalPrice());
		}
		return total;
	}

	private Price addCompositeProductPrice(Price total) {
		for (CompositeOrderItem compositeOrderItem : compositeOrderItems) {
			total = total.add(compositeOrderItem.getUnitPrice());
		}
		return total;
	}

	public void fillFromShoppingCart(ShoppingCart shoppingCart) {
		fillCompositeItem(shoppingCart);
		fillSkuItems(shoppingCart);
		this.totalWeight = shoppingCart.getTotalWeight();
	}

	private void fillSkuItems(ShoppingCart shoppingCart) {
		skuOrderItems.clear();
		for (SkuOrderItem skuitem : shoppingCart.getSkuItems()) {
			skuOrderItems.add(skuitem);
		}
	}

	private void fillCompositeItem(ShoppingCart shoppingCart) {
		compositeOrderItems.clear();

		for (CompositeOrderItem compositeOrderItem : shoppingCart.getCompositeOrderItems()) {
			compositeOrderItems.add(compositeOrderItem);
		}
	}

	private int getItemCount() {
		int count = 0;
		count = addSkuNumber(count);
		count = addCompositeProductNumber(count);

		return count;
	}

	private int addCompositeProductNumber(int count) {
		count += getCompositeOrderItems().size();
		return count;
	}

	private int addSkuNumber(int count) {
		for (SkuOrderItem item : skuOrderItems) {
			count += item.getQuantity();
		}
		return count;
	}

	public boolean isNotEmpty() {

		return getItemCount() != 0;
	}

	public boolean isComposite() {
		return compositeOrderItems.size() > 0;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ShipmentOption getShipmentOption() {
		return shipmentOption;
	}

	public void setShipmentOption(ShipmentOption shipmentOption) {
		this.shipmentOption = shipmentOption;
	}

	public List<SkuOrderItem> getSkuOrderItems() {
		return skuOrderItems;
	}

	public void setSkuOrderItems(List<SkuOrderItem> skuOrderItems) {
		this.skuOrderItems = skuOrderItems;
	}

	public List<CompositeOrderItem> getCompositeOrderItems() {
		return compositeOrderItems;
	}

	public void setCompositeOrderItems(List<CompositeOrderItem> compositeOrderItems) {
		this.compositeOrderItems = compositeOrderItems;
	}

	public void addSkuOderItem(SkuOrderItem skuOrderItem) {
		skuOrderItems.add(skuOrderItem);
	}

	public void addCompositeOrderItem(CompositeOrderItem compositeOrderItem) {
		compositeOrderItems.add(compositeOrderItem);
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public boolean isBulletin() {
		return bulletin;
	}

	public void setBulletin(boolean bulletin) {
		this.bulletin = bulletin;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getSaleDocument() {
		return saleDocument;
	}

	public void setSaleDocument(String saleDocument) {
		this.saleDocument = saleDocument;
	}

	public Price getTotalWithShipping() {
		if (shipmentOption != null) {
			return getTotalWithoutShipping().add(getShippingCost());
		} else {
			return getTotalWithoutShipping();
		}
	}

	public Price getShippingCost() {
		return getShipmentOption().getPriceForWeight(totalWeight);
	}

	public List<OrderHistoryRecord> getOrderHistory() {
		return Collections.unmodifiableList(orderHistory);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomerWithEmail(Customer customer, String email) {
		if (customer == null) {
			this.customer = new Customer(email, TO_FULFIL, TO_FULFIL);
		} else {
			this.customer = customer;
		}
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public InternalPayment getPayment() {
		return payment;
	}

	public void setPayment(InternalPayment payment) {
	//	payment.setOrder(this);
		this.payment = payment;
	}

	public void updatePaymentAmount() {
		payment.setAmount(getTotalWithShipping().getMoneyAmount());
	}

	public Recipient getRecipient() {
		return recipient;
	}

	public void setRecipient(Recipient recipient) {
		this.recipient = recipient;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public double getTotalWeight() {
		return totalWeight;
	}

}

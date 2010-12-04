package pl.netolution.sklep3.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;

@Entity
@Table(name = "order_items")
public class OrderItem extends OrderItemBase implements Serializable, Cloneable {

	private static final Logger log = Logger.getLogger(OrderItem.class);

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	private int quantity;

	public OrderItem(OrderItem shoppingCartItem) {
		setProduct(shoppingCartItem.getProduct());
		setQuantity(shoppingCartItem.getQuantity());
		setUnitPrice(shoppingCartItem.getUnitPrice());
		setUnitWholesaleNetPrice(shoppingCartItem.getUnitWholesaleNetPrice());
	}

	public OrderItem(Product product, int quantity) {
		setProduct(product);
		setQuantity(quantity);
		setUnitPrice(product.getRetailGrossPrice());
		setUnitWholesaleNetPrice(product.getWholesaleNetPrice());
	}

	public OrderItem(Product product) {
		setProduct(product);
		setQuantity(1);
		setUnitPrice(product.getRetailGrossPrice());
		setUnitWholesaleNetPrice(product.getWholesaleNetPrice());
	}

	@SuppressWarnings("unused")
	// For hibernate only
	private OrderItem() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		if (product == null) {
			throw new IllegalArgumentException("Product canot be null!");
		}
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		if (quantity < 1) {
			throw new IllegalArgumentException("Quantity must be > 0");
		}
		this.quantity = quantity;
	}

	public Price getTotalPrice() {
		return unitPrice.multiply(new BigDecimal(quantity));
	}

	@Override
	public OrderItem clone() {

		try {
			return (OrderItem) super.clone();
		} catch (CloneNotSupportedException ex) {
			log.error("Error while cloning OrderItem", ex);
			throw new RuntimeException(ex);
		}

	}

	public void increaseQuantity(int quantity) {
		if (quantity < 1) {
			throw new RuntimeException("Quantity must be > 0");
		}
		setQuantity(getQuantity() + quantity);

	}

	public boolean isTheSamePrice() {
		return product.getRetailGrossPrice().equals(unitPrice);

	}
}

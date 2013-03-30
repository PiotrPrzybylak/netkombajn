package com.netkombajn.eshop.ordering.order.item;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import pl.netolution.sklep3.domain.StockKeepingUnit;

import com.netkombajn.store.domain.shared.price.Price;

@Entity
@Table(name = "order_items")
//TODO jeśli zwykłe OrderItem z produktem zostawnie to wydzilić typ generyczny
//tak jak w przypadku dao
public class SkuOrderItem extends OrderItemBase implements Serializable, Cloneable {

	private static final Logger log = Logger.getLogger(SkuOrderItem.class);

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "sku_id")
	private StockKeepingUnit sku;

	private int quantity;

	public SkuOrderItem(StockKeepingUnit sku, int quantity) {
		setSku(sku);
		setQuantity(quantity);
		setUnitPrice(sku.getFinalPrice());
	}

	@SuppressWarnings("unused")
	// For hibernate only
	private SkuOrderItem() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StockKeepingUnit getSku() {
		return sku;
	}

	public void setSku(StockKeepingUnit sku) {
		if (sku == null) {
			throw new IllegalArgumentException("Sku canot be null!");
		}

//		sku.addOrderItem(this);
		this.sku = sku;
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
		return sku.getFinalPrice().equals(unitPrice);

	}
}

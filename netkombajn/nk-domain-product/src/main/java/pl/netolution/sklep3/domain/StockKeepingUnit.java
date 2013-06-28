package pl.netolution.sklep3.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.netkombajn.store.domain.shared.price.Price;

import pl.netolution.sklep3.domain.Product.Availability;

@SuppressWarnings("serial")
@Entity
@Table(name = "sku")
public class StockKeepingUnit implements Serializable {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@Type(type = "pl.netolution.sklep3.dao.hibernate.userTypes.PriceUserType")
	private Price retailGrossPrice;

	private Long quantityInStock;

	@Enumerated(EnumType.STRING)
	private Availability availability;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product parentProduct;

	private Integer priority;

//	@OneToMany(mappedBy = "sku")
//	private List<SkuOrderItem> skuOrderItems = new ArrayList<SkuOrderItem>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOriginalPrice(Price retailGrossPrice) {
		this.retailGrossPrice = retailGrossPrice;
	}

	public Price getOriginalPrice() {
		return retailGrossPrice;
	}

	public Price getFinalPrice() {
		Price finalPrice = parentProduct.getFinalPrice(retailGrossPrice);

		return finalPrice;
	}

	public Long getQuantityInStock() {
		return quantityInStock;
	}

	public void setQuantityInStock(Long quantityInStock) {
		this.quantityInStock = quantityInStock;
	}

	public Availability getAvailability() {
		if (availability != null) {
			return availability;
		}

		if (quantityInStock == null) {
			return Availability.NOT_AVAILABLE;
		}

		if (quantityInStock < 0) {
			throw new RuntimeException("quantityInStock < 0");
		}
		if (quantityInStock == 0) {
			return Availability.NOT_AVAILABLE;
		} else if (quantityInStock < 4) {
			return Availability.LOW;
		} else if (quantityInStock < 10) {
			return Availability.MEDIUM;
		} else {
			return Availability.HIGH;
		}
	}

	public void setAvailability(Availability availability) {
		this.availability = availability;
	}

	public Product getParentProduct() {
		return parentProduct;
	}

	public void setParentProduct(Product product) {
		this.parentProduct = product;
	}

	@Override
	public String toString() {

		return "Sku name : " + name;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public boolean isAvailable() {
		Availability availability = getAvailability();
		switch (availability) {
		case HIGH:
		case MEDIUM:
		case LOW:
		case ON_ORDER:
		case ON_ORDER_ONLY_PERSONALLY:
		case SENT_IN_48H:
		case SENT_IN_4_WEEKS:
			return true;
		case NOT_AVAILABLE:
		case SOON:
		case TEMPORARY_SHORTAGE:
			return false;

		}

		throw new RuntimeException("Unpredicted availability: " + availability);
	}

//	public void addOrderItem(SkuOrderItem skuOrderItem) {
//		skuOrderItems.add(skuOrderItem);
//	}

	public boolean isDeletable() {
		// TODO check if it is possible to delete this SKU.
		return true;
		//	return skuOrderItems.isEmpty();
	}
}

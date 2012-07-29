package pl.netolution.sklep3.domain;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.netkombajn.store.domain.shared.price.Price;

@Entity
@Table(name = "composite_order_items")
public class CompositeOrderItem extends OrderItemBase {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "composite_product_id")
	private CompositeProduct compositeProduct;

	@OneToMany
	@JoinColumn(name = "composite_order_item_id")
	@Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	private List<OrderItem> singleOrderItems = new LinkedList<OrderItem>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CompositeProduct getCompositeProduct() {
		return compositeProduct;
	}

	public void setCompositeProduct(CompositeProduct compositeProduct) {
		this.compositeProduct = compositeProduct;
	}

	public void addSingleOrderItem(Product product) {
		if (product == null) {
			throw new RuntimeException("product was null");
		}

		singleOrderItems.add(new OrderItem(product));

		recountPrices();

	}

	public List<OrderItem> getSingleOrderItems() {
		return Collections.unmodifiableList(singleOrderItems);
	}

	private void recountPrices() {

		initializePrices();

		for (OrderItem orderItem : singleOrderItems) {
			unitPrice = unitPrice.add(orderItem.getUnitPrice());
			unitWholesaleNetPrice = unitWholesaleNetPrice.add(orderItem.getUnitWholesaleNetPrice());
		}
		unitPrice = unitPrice.changeByPercentage(compositeProduct.getProfitPercent());
		unitWholesaleNetPrice = unitWholesaleNetPrice.changeByPercentage(compositeProduct.getProfitPercent());
	}

	private void initializePrices() {
		unitPrice = new Price();
		unitWholesaleNetPrice = new Price();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompositeOrderItem other = (CompositeOrderItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}

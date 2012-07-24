package pl.netolution.sklep3.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "composite_product")
@SuppressWarnings("serial")
public class CompositeProduct implements Serializable {

	@Id
	@GeneratedValue
	private long id;

	private String name;

	private String description;

	private int profitPercent = 0;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "composite_product_id")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OrderBy("priority")
	private List<ProductGroup> productGroups = new LinkedList<ProductGroup>();

	public Price getCalculatedInitialPrice() {
		Price price = new Price();

		for (ProductGroup group : productGroups) {
			price = price.add(group.getFirstProduct().getRetailGrossPrice());
		}

		return price.changeByPercentage(profitPercent);
	}

	public int getNumberOfGroups() {
		return productGroups.size();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getProfitPercent() {
		return profitPercent;
	}

	public void setProfitPercent(int profitPercent) {
		this.profitPercent = profitPercent;
	}

	public void addGroup(ProductGroup productGroup) {
		if (productGroup == null)
			throw new IllegalArgumentException("productGroup is null");

		this.productGroups.add(productGroup);

	}

	public void removeGroup(ProductGroup group) {
		productGroups.remove(group);
	}

	public List<ProductGroup> getProductGroups() {
		return Collections.unmodifiableList(productGroups);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

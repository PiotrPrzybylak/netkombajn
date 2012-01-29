package pl.netolution.sklep3.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "product_group")
@SuppressWarnings("serial")
public class ProductGroup implements Serializable {

	@Id
	@GeneratedValue
	private long id;

	private String name;

	private int priority;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "primary_product_id")
	private Product primaryProduct;

	@ManyToMany
	@JoinTable(name = "product_group_relation", joinColumns = { @JoinColumn(name = "group_id") }, inverseJoinColumns = { @JoinColumn(name = "product_id") })
	@OrderBy("retailGrossPrice")
	private Set<Product> products = new LinkedHashSet<Product>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Product getPrimaryProduct() {
		return primaryProduct;
	}

	public void setPrimaryProduct(Product product) {
		this.primaryProduct = product;
	}

	public void addProduct(Product product) {
		if (product == null)
			throw new IllegalArgumentException("product is null");

		this.products.add(product);

	}

	public void removeProduct(Product product) {
		this.products.remove(product);
	}

	public Set<Product> getProducts() {
		return Collections.unmodifiableSet(products);
	}

	public int getNumberOfProducts() {
		return products.size();
	}

	public Product getFirstProduct() {
		if (primaryProduct == null && products.size() > 0) {
			return products.iterator().next();
		}

		return primaryProduct;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		ProductGroup other = (ProductGroup) obj;
		if (id != other.id)
			return false;
		return true;
	}
}

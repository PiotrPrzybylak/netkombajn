package pl.netolution.sklep3.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "product_family")
public class ProductFamily extends BaseEntity {

	private String name;

	@ManyToMany
	@JoinTable(name = "products_families_relation", joinColumns = { @JoinColumn(name = "product_family_id") }, inverseJoinColumns = { @JoinColumn(name = "product_id") })
	private Set<Product> products = new HashSet<Product>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Product> getProducts() {
		return Collections.unmodifiableSet(products);
	}

	public void addProduct(Product product) {
		if (product == null) {
			throw new RuntimeException("product is null");
		}

		products.add(product);
	}

	public void removeProduct(Product product) {

		products.remove(product);
	}
}

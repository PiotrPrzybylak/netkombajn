package pl.netolution.sklep3.domain;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "designers")
public class Designer implements Serializable {

	@Id
	@GeneratedValue
	private long id;

	private String name;

	@OneToMany(mappedBy = "designer")
	@JoinColumn(name = "designer_id")
	private List<Product> products = new LinkedList<Product>();

	public int getProductsNumber() {
		return products.size();
	}

	@Override
	public String toString() {
		return name;
	}

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

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
}

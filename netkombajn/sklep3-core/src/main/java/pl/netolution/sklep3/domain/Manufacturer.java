package pl.netolution.sklep3.domain;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@SuppressWarnings("serial")
@Entity
@Table(name = "manufacturer")
public class Manufacturer implements Serializable, Comparable<Manufacturer> {

	@Id
	@GeneratedValue
	private long id;

	private String name;

	@OneToMany(mappedBy = "manufacturer")
	@JoinColumn(name = "manufacturer_id")
	private List<Product> products = new LinkedList<Product>();

	@OneToOne
	@JoinColumn(name = "picture_id")
	@Cascade(CascadeType.DELETE)
	private Picture picture;

	public Manufacturer() {
	}

	public Manufacturer(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Manufacturer other = (Manufacturer) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public int compareTo(Manufacturer manufacturer2) {

		if (manufacturer2 == null) {
			return 1;
		}

		return this.name.compareTo(manufacturer2.name);
	}

	public long getId() {
		return id;
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

	public int getProductsNumber() {
		return products.size();
	}

	@Override
	public String toString() {
		return name;
	}

	public Picture getPicture() {
		return picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

}

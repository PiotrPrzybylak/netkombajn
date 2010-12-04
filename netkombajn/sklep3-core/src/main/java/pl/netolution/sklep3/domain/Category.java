package pl.netolution.sklep3.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import pl.netolution.sklep3.domain.Product.Availability;

@Entity
@Table(name = "categories")
public class Category implements Serializable {

	@Id
	@GeneratedValue
	private long id;

	private String name;

	private String externalId;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Category parent;

	@OneToMany(mappedBy = "parent")
	@OrderBy("position asc, name asc")
	private final List<Category> children = new LinkedList<Category>();

	@OneToMany(mappedBy = "category")
	@JoinColumn(name = "category_id")
	@Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@OrderBy("name")
	private final Set<Product> products = new LinkedHashSet<Product>();

	@ManyToMany(mappedBy = "linkedCategories")
	private Set<Product> linkedProducts;

	private Integer profitMargin;

	private Double weight;

	@Enumerated(EnumType.STRING)
	private Availability defaultManualAvailability;

    private int position;

    public long getId() {
        return id;
    }

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getFullPath() {
		return getParentPath() + getName();
	}

	private String getParentPath() {
		if (getParent() == null) {
			return "";
		}

		return getParent().getFullPath() + " > ";
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addProduct(Product product) {
		if (product == null)
			throw new IllegalArgumentException("added product is null");

		product.setCategory(this);
		products.add(product);
	}

	public void removeProduct(Product product) {
		product.setCategory(null);
		products.remove(product);
	}

	public Set<Product> getProducts() {
		return Collections.unmodifiableSet(products);
	}

	public Set<Product> getVisibleProducts() {
		return getProductsFromBranch(true);
	}

	public Set<Product> getAllProducts() {
		return getProductsFromBranch(false);
	}

	private Set<Product> getProductsFromBranch(boolean onlyVisibleProducts) {
		Set<Product> productsFromBranch = new HashSet<Product>();
		for (Product product : products) {
			if (!onlyVisibleProducts) {
				productsFromBranch.add(product);
			} else if (onlyVisibleProducts && product.isVisible()) {
				productsFromBranch.add(product);
			}
		}

		for (Category child : children) {
			productsFromBranch.addAll(child.getProductsFromBranch(onlyVisibleProducts));
		}

		return Collections.unmodifiableSet(productsFromBranch);
	}

	public int getVisibleProductsNumber() {
		return getVisibleProducts().size();
	}

	public int getAllProductsNumber() {
		return getAllProducts().size();
	}

	public boolean isDeletable() {
		return getAllProductsNumber() == 0 && getLinkedProductsNumber() == 0 && !hasChildren();
	}

	public int getLinkedProductsNumber() {
		if (linkedProducts == null || linkedProducts.isEmpty()) {
			return 0;
		}
		Set<Product> onlyLinkedProducts = new HashSet<Product>(linkedProducts);
		onlyLinkedProducts.removeAll(getAllProducts());
		return onlyLinkedProducts.size();
	}

	public String getExternalId() {
		return externalId;
	}
 
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public List<Category> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public void addChild(Category category) {
		if (category == null)
			throw new IllegalArgumentException("child category can not be null");

		category.setParent(this);
		this.children.add(category);

	}

	public Set<Category> getDescendants() {
		Set<Category> descendants = new HashSet<Category>();
		for (Category child : children) {
			descendants.add(child);
			descendants.addAll(child.getDescendants());
		}
		return descendants;
	}

	public Category getRootCategory() {
		Category parent = getParent();
		if (parent == null) {
			return this;
		}
		return parent.getRootCategory();
	}

	public boolean hasChildren() {
		return children != null && children.size() > 0;
	}

	public Integer getProfitMargin() {
		return profitMargin;
	}

	public void setProfitMargin(Integer profitMargin) {
		this.profitMargin = profitMargin;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Availability getDefaultManualAvailability() {
		return defaultManualAvailability;
	}

	public void setDefaultManualAvailability(Availability defaultManualAvailability) {
		this.defaultManualAvailability = defaultManualAvailability;
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
		Category other = (Category) obj;
		if (id != other.id)
			return false;
		return true;
	}

    public void setPosition( int position ) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

}

package pl.netolution.sklep3.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Type;

@SuppressWarnings("serial")
@Entity
@Table(name = "products")
public class Product implements Serializable, Cloneable {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String description;

	@Type(type = "pl.netolution.sklep3.dao.hibernate.userTypes.PriceUserType")
	private Price retailGrossPrice;

	@Type(type = "pl.netolution.sklep3.dao.hibernate.userTypes.PriceUserType")
	private Price wholesaleNetPrice;

	private String catalogNumber;

	@ManyToOne
	@JoinColumn(name = "manufacturer_id", nullable = true)
	private Manufacturer manufacturer;

	@ManyToOne
	@JoinColumn(name = "designer_id", nullable = true)
	private Designer designer;

	private boolean available;

	private String shortDescription;

	// TODO to tez jako klasa price
	private BigDecimal suggestedPrice;

	private Long quantityInStock;

	private boolean visible;

	private Double weight;

	private Integer warranty;

	private boolean hit;

	private String externalPictureUrl;

	private boolean useExternalPicture;

	private String source;

	private Date creation;

	private Date lastUpdate;

	@Enumerated(EnumType.STRING)
	private Availability manualAvailability;

	@OneToMany(mappedBy = "product")
	@Cascade(CascadeType.DELETE)
	@OrderBy("pictureOrder")
	private final List<ProductPicture> pictures = new LinkedList<ProductPicture>();

	private Integer discountInPercents;

	private boolean onSale;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@OneToMany(mappedBy = "parentProduct")
	@Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@OrderBy("priority")
	private final List<StockKeepingUnit> skus = new ArrayList<StockKeepingUnit>();

	@OneToOne
	@JoinColumn(name = "default_sku_id")
	private StockKeepingUnit defaultSku;

	@ManyToMany
	@JoinTable(name = "products_linked_categories", joinColumns = { @JoinColumn(name = "product_id") }, inverseJoinColumns = { @JoinColumn(name = "category_id") })
	private final Set<Category> linkedCategories = new HashSet<Category>();

	@ManyToMany(mappedBy = "products")
	private List<ProductFamily> families;

	public Product() {
	}

	public Product(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		if (category == null) {
			this.category = null;
			return;
		}

		this.category = category;
		addLinkedCategory(category);
	}

	@Override
	public String toString() {
		return super.toString() + " Product id: " + id + " \nProduct name " + name;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public String getCatalogNumber() {
		return catalogNumber;
	}

	public void setCatalogNumber(String catalogNumber) {
		this.catalogNumber = catalogNumber;
	}

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public boolean isPriceLowerThenSuggested() {
		if (null == suggestedPrice) {
			return false;
		}
		return retailGrossPrice.getValue().compareTo(suggestedPrice) < 0;
	}

	public BigDecimal getSavings() {
		if (!isPriceLowerThenSuggested()) {
			return BigDecimal.ZERO;
		}
		return suggestedPrice.subtract(retailGrossPrice.getValue());
	}

	public Price getRetailGrossPrice() {
		return retailGrossPrice;
	}

	public Price getPrice() {
		Price basePrice = getOriginalPrice();

		return getFinalPrice(basePrice);
	}

	public Price getOriginalPrice() {
		return getDefaultSku().getOriginalPrice();
	}

	public Price getFinalPrice(Price basePrice) {
		if (!onSale || discountInPercents == null) {
			return basePrice;
		}

		return basePrice.changeByPercentage(-discountInPercents);
	}

	public void setRetailGrossPrice(Price retailGrossPrice) {
		this.retailGrossPrice = retailGrossPrice;
	}

	public Price getWholesaleNetPrice() {
		return wholesaleNetPrice;
	}

	public void setWholesaleNetPrice(Price wholesalesNetPrice) {
		this.wholesaleNetPrice = wholesalesNetPrice;
	}

	// TODO tez jako obiekt price przedstawic?
	public BigDecimal getSuggestedPrice() {
		return suggestedPrice;
	}

	public void setSuggestedPrice(BigDecimal suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}

	public Long getQuantityInStock() {
		long totalQuantity = 0;
		// for (StockKeepingUnit sku : getSkus()) {
		// totalQuantity = totalQuantity + sku.getQuantityInStock();
		// }

		return totalQuantity;
	}

	public void setQuantityInStock(Long quantityInStock) {
		this.quantityInStock = quantityInStock;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Integer getWarranty() {
		return warranty;
	}

	public void setWarranty(Integer warranty) {
		this.warranty = warranty;
	}

	public boolean isHit() {
		return hit;
	}

	public void setHit(boolean hit) {
		this.hit = hit;
	}

	public String getExternalPictureUrl() {
		return externalPictureUrl;
	}

	public void setExternalPictureUrl(String externalPictureUrl) {
		this.externalPictureUrl = externalPictureUrl;
	}

	public boolean isUseExternalPicture() {
		return useExternalPicture;
	}

	public void setUseExternalPicture(boolean useExternalPicture) {
		this.useExternalPicture = useExternalPicture;
	}

	public Picture getMainPicture() {
		if (!pictures.isEmpty()) {
			return pictures.get(0);
		} else {
			return Picture.PICTURE_NONE;
		}

	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Availability getManualAvailability() {
		return manualAvailability;
	}

	public void setManualAvailability(Availability manualAvailability) {
		this.manualAvailability = manualAvailability;
	}

	public boolean isBuyable() {
		if (quantityInStock != null) {
			return quantityInStock > 0;
		} else {
			// TODO Ask the custmer about the value returned here, the same with getAvailability()
			return false;
		}

	}

	public static enum Availability {

		NOT_AVAILABLE(100, false), LOW(300, true), MEDIUM(300, true), HIGH(400, true), TEMPORARY_SHORTAGE(190, false), ON_ORDER(120, false), ON_ORDER_ONLY_PERSONALLY(
				125, false), SOON(130, false), SENT_IN_48H(140, true), SENT_IN_4_WEEKS(150, false);
		Integer ordinal;

		private final boolean instantAvailable;

		private Availability(Integer oridinal, boolean instantAvailable) {
			this.ordinal = oridinal;
			this.instantAvailable = instantAvailable;
		}

		public boolean isInstantAvailable() {
			return instantAvailable;
		}

		public Integer getOrdinal() {
			return ordinal;
		}

		public String getLiteral() {
			return this.name();
		}
	}

	// TODO do celowo tylko w sku
	public Availability getAvailability() {

		if (manualAvailability != null) {
			return manualAvailability;
		}

		if (quantityInStock == null) {
			return null;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((catalogNumber == null) ? 0 : catalogNumber.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((manufacturer == null) ? 0 : manufacturer.hashCode());
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
		Product other = (Product) obj;
		if (catalogNumber == null) {
			if (other.catalogNumber != null)
				return false;
		} else if (!catalogNumber.equals(other.catalogNumber))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (manufacturer == null) {
			if (other.manufacturer != null)
				return false;
		} else if (!manufacturer.equals(other.manufacturer))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public Integer getMarkup() {
		return retailGrossPrice.countMarkup(wholesaleNetPrice);
	}

	public Integer getProfitMargin() {
		return retailGrossPrice.countProfitMargin(wholesaleNetPrice);
	}

	public boolean wasAvailableYesterday() {
		if (!wasAvailableDuringLastImport()) {
			return false;
		}

		if (!lastImportWasYesterDayOrToday()) {
			return false;
		}

		return true;
	}

	private boolean lastImportWasYesterDayOrToday() {

		Calendar yesterdayCalendar = getYesterdayCalendar();
		Calendar lastUpdateCalendar = getLastUpdateCalendar();

		int time = lastUpdateCalendar.get(Calendar.DAY_OF_YEAR) - yesterdayCalendar.get(Calendar.DAY_OF_YEAR);
		if (time < 0) {
			return false;
		}
		return true;
	}

	private Calendar getYesterdayCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return calendar;
	}

	private Calendar getLastUpdateCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lastUpdate);
		return calendar;
	}

	private boolean wasAvailableDuringLastImport() {
		return quantityInStock != null && quantityInStock > 0;
	}

	public List<ProductPicture> getPictures() {

		return Collections.unmodifiableList(pictures);

	}

	public List<ProductPicture> initPictures() {
		return pictures;
	}

	public void addPicture(ProductPicture picture) {
		if (picture == null) {
			throw new IllegalArgumentException("picture can not be null");
		}
		picture.setProduct(this);
		pictures.add(picture);
	}

	public void removePicture(Picture picture) {
		pictures.remove(picture);
	}

	public Designer getDesigner() {
		return designer;
	}

	public void setDesigner(Designer designer) {
		this.designer = designer;
	}

	public Integer getDiscountInPercents() {
		return discountInPercents;
	}

	public void setDiscountInPercents(Integer discountInPercents) {
		this.discountInPercents = discountInPercents;
	}

	public boolean isOnSale() {
		return onSale;
	}

	public void setOnSale(boolean onSale) {
		this.onSale = onSale;
	}

	public void addSku(StockKeepingUnit stockKeepingUnit) {
		if (stockKeepingUnit == null) {
			throw new RuntimeException("sku was null");
		}

		stockKeepingUnit.setParentProduct(this);
		skus.add(stockKeepingUnit);
	}

	public List<StockKeepingUnit> getSkus() {
		return skus;
	}

	public void removeSku(StockKeepingUnit editedStockKeepingUnit) {
		if (getDefaultSku().equals(editedStockKeepingUnit)) {
			throw new RuntimeException("You can not delete default sku");
		}
		skus.remove(editedStockKeepingUnit);
	}

	public void removeLinkedCategory(Category linkedCategory) {
		if (this.category.equals(linkedCategory)) {
			throw new RuntimeException("You can not delete main category");
		}

		linkedCategories.remove(linkedCategory);
	}

	public void removeFromCategory() {
		this.category.removeProduct(this);
	}

	public StockKeepingUnit createSkuFromLegacyData() {
		StockKeepingUnit defualtSku = new StockKeepingUnit();
		defualtSku.setAvailability(getManualAvailability());
		defualtSku.setName(getName());
		defualtSku.setOriginalPrice(getRetailGrossPrice());
		defualtSku.setQuantityInStock(getQuantityInStock());
		return defualtSku;
	}

	public void addDefaultSkuIfNecessary() {
		if (getDefaultSku() != null) {
			return;
		}

		if (skus.isEmpty()) {
			StockKeepingUnit defaultSku = createSkuFromLegacyData();
			addSku(defaultSku);
			this.defaultSku = defaultSku;
		} else {
			this.defaultSku = skus.get(0);
		}

	}

	public StockKeepingUnit getDefaultSku() {

		return defaultSku;
	}

	public void setDefaultSku(StockKeepingUnit defaultSku) {
		defaultSku.setParentProduct(this);
		this.defaultSku = defaultSku;
	}

	@Override
	public Product clone() {

		Product product = new Product();
		product.setAvailable(this.isAvailable());
		product.setCatalogNumber(this.getCatalogNumber());
		product.setDescription(this.getDescription());
		product.setDesigner(this.getDesigner());
		product.setDiscountInPercents(this.getDiscountInPercents());
		product.setExternalPictureUrl(this.getExternalPictureUrl());
		product.setHit(this.isHit());
		product.setManualAvailability(this.getManualAvailability());
		product.setManufacturer(this.getManufacturer());
		product.setName(this.getName());
		product.setOnSale(this.isOnSale());
		product.setQuantityInStock(this.getQuantityInStock());
		product.setRetailGrossPrice(this.getRetailGrossPrice());
		product.setShortDescription(this.getShortDescription());
		product.setSource(this.getSource());
		product.setSuggestedPrice(this.getSuggestedPrice());
		product.setUseExternalPicture(this.isUseExternalPicture());
		product.setVisible(this.isVisible());
		product.setWarranty(this.getWarranty());
		product.setWeight(this.getWeight());
		product.setWholesaleNetPrice(this.getWholesaleNetPrice());

		product.addDefaultSkuIfNecessary();

		for (Category category : this.linkedCategories) {
			product.addLinkedCategory(category);
		}

		product.setCategory(this.getCategory());

		return product;

	}

	public Set<Category> getLinkedCategories() {
		return Collections.unmodifiableSet(linkedCategories);
	}

	public void addLinkedCategory(Category category) {
		if (category == null) {
			throw new RuntimeException("linked category was null");
		}

		linkedCategories.add(category);
	}

	public ProductFamily getDefaultFamily() {
		if (families == null || families.size() == 0) {
			return new ProductFamily();
		}

		return families.get(0);
	}

	public boolean isDeletable() {

		if (!getDefaultSku().isDeletable()) {
			return false;
		}
		for (StockKeepingUnit sku : skus) {
			if (!sku.isDeletable()) {
				return false;
			}

		}
		return true;
	}
}

package pl.netolution.sklep3.utils;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.domain.Designer;
import pl.netolution.sklep3.domain.Manufacturer;
import pl.netolution.sklep3.domain.Price;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.enums.SortDirection;

public class DetachedCriteriaProductsQueryBuilder implements IProductsQueryBuilder {

	protected DetachedCriteria criteria;

	protected String searchPhrase;

	protected Category category;

	private Designer designer;

	private Manufacturer manufacturer;

	private int minPrice;

	private int maxPrice;

	private SortDirection sortDirection;

	private ProductSortProperty sortProperty;

	/** default true */
	private boolean showOnlyVisible = true;

	public DetachedCriteria buildCriteria() {
		criteria = DetachedCriteria.forClass(Product.class);

		addPriceCriteria();
		addKeywordCriteria();
		addCategoryCriteria();
		addDesignerCriteria();
		addManufacturerCriteria();
		addOrder();
		addVisibilityRestricion();

		return criteria;
	}

	protected void addVisibilityRestricion() {
		if (showOnlyVisible) {
			criteria.add(Restrictions.eq("visible", true));
		}
	}

	protected void addOrder() {
		String sortPropertyLiteral;

		if (sortProperty != null) {

			switch (sortProperty) {
			case price:
				criteria.createAlias("defaultSku", "defaultSku");
				sortPropertyLiteral = "defaultSku.retailGrossPrice";
				break;
			default:
				sortPropertyLiteral = "name";
			}

			if (sortDirection == null || SortDirection.asc == sortDirection) {
				criteria.addOrder(Order.asc(sortPropertyLiteral));
			} else {
				criteria.addOrder(Order.desc(sortPropertyLiteral));
			}
		}
	}

	private void addCategoryCriteria() {
		if (category != null) {
			Set<Category> productCategories = category.getDescendants();

			productCategories.add(category);
			DetachedCriteria categoriesCriteria = criteria.createCriteria("linkedCategories");
			categoriesCriteria.add(Restrictions.in("id", generateCategoriesIdsSet(productCategories)));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		}
	}

	private Set<Long> generateCategoriesIdsSet(Set<Category> productCategories) {
		Set<Long> categorisIds = new HashSet<Long>();
		for (Category category : productCategories) {
			categorisIds.add(category.getId());
		}

		return categorisIds;
	}

	private void addDesignerCriteria() {
		if (designer != null) {
			criteria.add(Restrictions.eq("designer", designer));
		}
	}

	private void addManufacturerCriteria() {
		if (manufacturer != null) {
			criteria.add(Restrictions.eq("manufacturer", manufacturer));
		}
	}

	private void addKeywordCriteria() {
		if (StringUtils.isNotBlank(searchPhrase)) {
			criteria.createAlias("designer", "designer");
			criteria.createAlias("manufacturer", "manufacturer");
			SimpleExpression phraseInName = createSearchExpressionForProperty("name");
			SimpleExpression phraseInDescription = createSearchExpressionForProperty("description");
			SimpleExpression phraseInManufacturer = createSearchExpressionForProperty("manufacturer.name");
			SimpleExpression phraseInDesigner = createSearchExpressionForProperty("designer.name");
			criteria.add(Restrictions.or(Restrictions.or(phraseInName, phraseInDescription), Restrictions.or(phraseInDesigner,
					phraseInManufacturer)));
		}
	}

	private SimpleExpression createSearchExpressionForProperty(String propertyName) {
		return Restrictions.like(propertyName, "%" + searchPhrase + "%").ignoreCase();
	}

	protected void addPriceCriteria() {
		if (minPrice > 0) {
			criteria.createAlias("defaultSku", "defaultSku");
			criteria.add(Restrictions.gt("defaultSku.retailGrossPrice", new Price(minPrice)));
		}

		if (maxPrice > 0) {
			criteria.createAlias("defaultSku", "defaultSku");
			criteria.add(Restrictions.lt("defaultSku.retailGrossPrice", new Price(maxPrice)));
		}
	}

	/* (non-Javadoc)
	 * @see pl.netolution.sklep3.utils.IProductsQueryBuilder#addMinPrice(int)
	 */
	public IProductsQueryBuilder addMinPrice(int price) {
		this.minPrice = price;
		return this;
	}

	/* (non-Javadoc)
	 * @see pl.netolution.sklep3.utils.IProductsQueryBuilder#addMaxPrice(int)
	 */
	public IProductsQueryBuilder addMaxPrice(int price) {
		this.maxPrice = price;
		return this;
	}

	/* (non-Javadoc)
	 * @see pl.netolution.sklep3.utils.IProductsQueryBuilder#addSearchPhrase(java.lang.String)
	 */
	public IProductsQueryBuilder addSearchPhrase(String searchPhrase) {
		this.searchPhrase = searchPhrase;
		return this;
	}

	/* (non-Javadoc)
	 * @see pl.netolution.sklep3.utils.IProductsQueryBuilder#addCategory(pl.netolution.sklep3.domain.Category)
	 */
	public IProductsQueryBuilder addCategory(Category category) {
		this.category = category;
		return this;
	}

	/* (non-Javadoc)
	 * @see pl.netolution.sklep3.utils.IProductsQueryBuilder#addDesigner(pl.netolution.sklep3.domain.Designer)
	 */
	public IProductsQueryBuilder addDesigner(Designer designer) {
		this.designer = designer;
		return this;
	}

	/* (non-Javadoc)
	 * @see pl.netolution.sklep3.utils.IProductsQueryBuilder#addManufacturer(pl.netolution.sklep3.domain.Manufacturer)
	 */
	public IProductsQueryBuilder addManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
		return this;
	}

	/* (non-Javadoc)
	 * @see pl.netolution.sklep3.utils.IProductsQueryBuilder#addSortProperty(pl.netolution.sklep3.utils.ProductsQueryBuilder.ProductSortProperty)
	 */
	public IProductsQueryBuilder addSortProperty(ProductSortProperty sortProperty) {
		this.sortProperty = sortProperty;
		return this;
	}

	/* (non-Javadoc)
	 * @see pl.netolution.sklep3.utils.IProductsQueryBuilder#setSortDirection(pl.netolution.sklep3.domain.enums.SortDirection)
	 */
	public IProductsQueryBuilder setSortDirection(SortDirection sortDirection) {
		this.sortDirection = sortDirection;
		return this;
	}

	public enum ProductSortProperty {
		name, price, availability;
	}

	public boolean isShowOnlyVisible() {
		return showOnlyVisible;
	}

	/* (non-Javadoc)
	 * @see pl.netolution.sklep3.utils.IProductsQueryBuilder#setShowOnlyVisible(boolean)
	 */
	public void setShowOnlyVisible(boolean showOnlyVisible) {
		this.showOnlyVisible = showOnlyVisible;
	}

}

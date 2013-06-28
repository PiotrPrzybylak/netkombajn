package pl.netolution.sklep3.web.controller;

import com.netkombajn.store.domain.shared.sort.SortDirection;

import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.domain.Designer;
import pl.netolution.sklep3.domain.Manufacturer;
import pl.netolution.sklep3.utils.ProductSortProperty;
import pl.netolution.sklep3.utils.ProductsQueryBuilder;

public class TestProductsQueryBuilder implements ProductsQueryBuilder {

	public ProductsQueryBuilder addMinPrice(int price) {
		return this;
	}

	public ProductsQueryBuilder addMaxPrice(int price) {
		return this;
	}

	public ProductsQueryBuilder addSearchPhrase(String searchPhrase) {
		return this;
	}

	public ProductsQueryBuilder addCategory(Category category) {
		return this;
	}

	public ProductsQueryBuilder addDesigner(Designer designer) {
		return this;
	}

	public ProductsQueryBuilder addManufacturer(Manufacturer manufacturer) {
		return this;
	}

	public ProductsQueryBuilder addSortProperty(ProductSortProperty sortProperty) {
		return this;
	}

	public ProductsQueryBuilder setSortDirection(SortDirection sortDirection) {
		return this;
	}

	public void setShowOnlyVisible(boolean showOnlyVisible) {

	}

}

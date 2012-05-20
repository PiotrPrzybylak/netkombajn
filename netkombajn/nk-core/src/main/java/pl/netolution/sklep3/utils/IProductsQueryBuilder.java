package pl.netolution.sklep3.utils;

import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.domain.Designer;
import pl.netolution.sklep3.domain.Manufacturer;
import pl.netolution.sklep3.domain.enums.SortDirection;
import pl.netolution.sklep3.utils.ProductsQueryBuilder.ProductSortProperty;

public interface IProductsQueryBuilder {

	IProductsQueryBuilder addMinPrice(int price);

	IProductsQueryBuilder addMaxPrice(int price);

	IProductsQueryBuilder addSearchPhrase(String searchPhrase);

	IProductsQueryBuilder addCategory(Category category);

	IProductsQueryBuilder addDesigner(Designer designer);

	IProductsQueryBuilder addManufacturer(Manufacturer manufacturer);

	IProductsQueryBuilder addSortProperty(ProductSortProperty sortProperty);

	IProductsQueryBuilder setSortDirection(SortDirection sortDirection);

	void setShowOnlyVisible(boolean showOnlyVisible);

}
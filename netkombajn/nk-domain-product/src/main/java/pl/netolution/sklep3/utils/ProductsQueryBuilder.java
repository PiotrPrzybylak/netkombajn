package pl.netolution.sklep3.utils;

import com.netkombajn.store.domain.shared.sort.SortDirection;

import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.domain.Designer;
import pl.netolution.sklep3.domain.Manufacturer;

public interface ProductsQueryBuilder {

	ProductsQueryBuilder addMinPrice(int price);

	ProductsQueryBuilder addMaxPrice(int price);

	ProductsQueryBuilder addSearchPhrase(String searchPhrase);

	ProductsQueryBuilder addCategory(Category category);

	ProductsQueryBuilder addDesigner(Designer designer);

	ProductsQueryBuilder addManufacturer(Manufacturer manufacturer);

	ProductsQueryBuilder addSortProperty(ProductSortProperty sortProperty);

	ProductsQueryBuilder setSortDirection(SortDirection sortDirection);

	void setShowOnlyVisible(boolean showOnlyVisible);

}
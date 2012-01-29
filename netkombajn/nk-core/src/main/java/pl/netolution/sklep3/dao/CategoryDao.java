package pl.netolution.sklep3.dao;

import java.util.List;

import pl.netolution.sklep3.domain.Category;

public interface CategoryDao extends BaseDao<Category> {

	Category findByName(String name);

	Category findByExternalId(String externalId);

	List<Category> getRootCategories();
}

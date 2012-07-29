package pl.netolution.sklep3.dao;

import java.util.List;

import com.netkombajn.store.domain.shared.dao.BaseDao;

import pl.netolution.sklep3.domain.Category;

public interface CategoryDao extends BaseDao<Category> {

	Category findByName(String name);

	Category findByExternalId(String externalId);

	List<Category> getRootCategories();
}

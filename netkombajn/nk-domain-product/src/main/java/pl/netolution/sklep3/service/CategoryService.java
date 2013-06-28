package pl.netolution.sklep3.service;

import pl.netolution.sklep3.dao.CategoryDao;
import pl.netolution.sklep3.domain.Category;

public class CategoryService {

	private CategoryDao categoryDao;
	
	public CategoryService(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	public Category createSubcategory(Category parent, String name) {
		Category child = new Category();
		child.setName(name);
		if (parent != null) {
			Category freshParent = categoryDao.findById(parent.getId());
			freshParent.addChild(child);
		}

		categoryDao.makePersistent(child);

		return child;
	}
	
}

package pl.netolution.sklep3.service;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import pl.netolution.sklep3.dao.CategoryDao;
import pl.netolution.sklep3.domain.Category;

public class CategoryServiceTest {

	private CategoryDao categoryDao = mock(CategoryDao.class);
	private CategoryService categoryService = new CategoryService(categoryDao);

	@Test
	public void test_shouldAddNewsubcategory() {
		// given
		Category parent = new Category();
		parent.setId(123L);

		Category freshParent = new Category();
		when(categoryDao.findById(123L)).thenReturn(freshParent);

		// when
		Category category = categoryService.createSubcategory(parent, "new subcategory");

		// then
		assertSame(category.getParent(), freshParent);
	}

}

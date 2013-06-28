package pl.netolution.sklep3.web.jsf;

import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;

import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;

import pl.netolution.sklep3.dao.CategoryDao;
import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.service.CategoryService;

public class CategoryBackingBean {

	private CategoryDao categoryDao;

	private TreeNode<Category> categoriesTreeModel;

	private Category choosenCategory;

	private String newSubcategory;

	private CategoryService categoryService;

	//TODO tam metoda jest taka sama jak ta z ProductBackingBean
	public void chooseCategory(ActionEvent actionEvent) {
		Map<String, Object> attributes = actionEvent.getComponent().getAttributes();
		long categoryId = Long.parseLong(attributes.get("categoryId").toString());
		choosenCategory = categoryDao.findById(categoryId);
	}

	public void chooseRootCategory(ActionEvent actionEvent) {
		cleanBean();
	}

	public TreeNode<Category> getCategoriesTreeModel() {

		initializeCategoriesTree();

		return categoriesTreeModel;
	}

	public void updateCategory(ActionEvent actionEvent) {
		categoryDao.makePersistent(choosenCategory);
	}

	public void deleteCategory(ActionEvent actionEvent) {
		categoryDao.delete(choosenCategory);
		cleanBean();
	}

	private void cleanBean() {
		choosenCategory = null;
	}

	private void initializeCategoriesTree() {

		TreeNode<Category> rootNode = createRootNode();

		List<Category> rootCategoryChildren = categoryDao.getRootCategories();

		for (Category category : rootCategoryChildren) {

			TreeNode<Category> categoryNode = new TreeNodeImpl<Category>();
			categoryNode.setData(category);
			rootNode.addChild(category.getName(), categoryNode);
			addChildren(categoryNode, category.getChildren());
		}

		categoriesTreeModel = rootNode;
	}

	private TreeNode<Category> createRootNode() {
		Category root = new Category();
		root.setName("root");
		TreeNode<Category> rootNode = new TreeNodeImpl<Category>();
		rootNode.setData(root);
		return rootNode;
	}

	private void addChildren(TreeNode<Category> categoryParentNode, List<Category> children) {
		if (children == null || children.size() == 0) {
			return;
		}

		for (Category category : children) {
			TreeNode<Category> categoryChildNode = new TreeNodeImpl<Category>();
			categoryChildNode.setData(category);

			categoryParentNode.addChild(category.getName(), categoryChildNode);
			addChildren(categoryChildNode, category.getChildren());
		}
	}

	public String addSubcategory() {
		categoryService.createSubcategory(choosenCategory, newSubcategory);
		initializeCategoriesTree();
		newSubcategory = null;
		return null;
	}

	public void setCategoryDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	public Category getChoosenCategory() {
		return choosenCategory;
	}

	public void setChoosenCategory(Category choosenCategory) {
		this.choosenCategory = choosenCategory;
	}

	public String getNewSubcategory() {
		return newSubcategory;
	}

	public void setNewSubcategory(String newSubcategory) {
		this.newSubcategory = newSubcategory;
	}

	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
}

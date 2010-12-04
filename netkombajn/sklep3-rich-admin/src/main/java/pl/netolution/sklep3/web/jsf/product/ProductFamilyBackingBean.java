package pl.netolution.sklep3.web.jsf.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.faces.event.ActionEvent;

import org.richfaces.model.selection.Selection;

import pl.netolution.sklep3.dao.CategoryDao;
import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.ProductFamily;
import pl.netolution.sklep3.web.jsf.GenericEntityBackingBean;
import pl.netolution.sklep3.web.jsf.JsfRequestResolver;

//TODO refaktoring cześći wspólnej z compositeProduct
public class ProductFamilyBackingBean extends GenericEntityBackingBean<ProductFamily> {

	private static final String FAMILY_DETAILS = "familyDetails";

	private long familyId;

	private ProductFamily productFamilyDetails;

	private Selection productSelection;

	private Category category;

	private CategoryDao categoryDao;

	private ProductDao productDao;

	private JsfRequestResolver jsfRequestResolver;

	public String familyDetails() {
		productFamilyDetails = getEntityDao().findById(familyId);
		return FAMILY_DETAILS;
	}

	public ProductFamily getProductFamilyDetails() {
		return productFamilyDetails;
	}

	public void chooseCategory() {
		String categoryId = jsfRequestResolver.getParameter("categoryId");
		this.category = categoryDao.findById(Long.parseLong(categoryId));

	}

	@SuppressWarnings("unchecked")
	public List<Product> getCategoryItems() {
		if (category != null) {
			initializeSkus();

			return new ArrayList<Product>(category.getAllProducts());
		}
		return Collections.EMPTY_LIST;
	}

	private void initializeSkus() {
		for (Product product : category.getAllProducts()) {
			product.getSkus().size();
		}
	}

	public void takeSelection() {
		List<Product> values = getCategoryItems();

		Iterator<Object> iterator = productSelection.getKeys();

		while (iterator.hasNext()) {
			Integer rowKey = (Integer) iterator.next();
			productFamilyDetails.addProduct(values.get(rowKey));
		}

		getEntityDao().makePersistent(productFamilyDetails);
	}

	public void removeProductFromList(ActionEvent actionEvent) {
		Product product = getProductFromRequest();
		productFamilyDetails.removeProduct(product);

		getEntityDao().makePersistent(productFamilyDetails);
	}

	protected Product getProductFromRequest() {
		long productId = Long.parseLong(jsfRequestResolver.getParameter("productId"));
		return productDao.findById(productId);
	}

	public List<Product> getFamilyItems() {
		return new LinkedList<Product>(productFamilyDetails.getProducts());
	}

	public void setProductFamilyDetails(ProductFamily productFamilyForProducts) {
		this.productFamilyDetails = productFamilyForProducts;
	}

	public long getFamilyId() {
		return familyId;
	}

	public void setFamilyId(long familyId) {
		this.familyId = familyId;
	}

	public Selection getProductSelection() {
		return productSelection;
	}

	public void setProductSelection(Selection productSelection) {
		this.productSelection = productSelection;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public CategoryDao getCategoryDao() {
		return categoryDao;
	}

	public void setCategoryDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	public JsfRequestResolver getJsfRequestResolver() {
		return jsfRequestResolver;
	}

	public void setJsfRequestResolver(JsfRequestResolver jsfRequestResolver) {
		this.jsfRequestResolver = jsfRequestResolver;
	}

	public ProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}
}

package pl.netolution.sklep3.web.jsf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.richfaces.model.selection.Selection;

import pl.netolution.sklep3.dao.CompositeProductDao;
import pl.netolution.sklep3.dao.ProductGroupDao;
import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.domain.CompositeProduct;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.ProductGroup;

public class CompositeProductBackingBean extends ProductBackingBase {

	private static final String GROUP_DETAILS = "groupDetails";

	private static final String COMPOSITE_NEW = "compositeNew";

	private CompositeProductDao compositeProductDao;

	private ProductGroupDao productGroupDao;

	private CompositeProduct compositeProduct = new CompositeProduct();

	private ProductGroup productGroup = new ProductGroup();

	private ProductGroup productGroupDetails = new ProductGroup();

	private Category category;

	private String infoMessage;

	private Selection selectionForGroupProducts;

	private boolean compositeProductDirty;

	public String updateCompositeProduct() {

		compositeProductDao.makePersistent(compositeProduct);

		SimpleDateFormat hourMinutesDateFormat = new SimpleDateFormat("k:mm", new Locale("PL"));
		infoMessage = "Zestaw " + compositeProduct.getName() + " został poprawnie zapisany o godzinie "
				+ hourMinutesDateFormat.format(new Date());

		compositeProductDirty = false;

		return COMPOSITE_NEW;
	}

	public String addGroup() {

		compositeProduct.addGroup(productGroup);

		productGroup = new ProductGroup();

		setCompositeProductToDirtyState();

		return COMPOSITE_NEW;
	}

	public void removeGroup(ActionEvent actionEvent) {
		Map<String, Object> attributes = actionEvent.getComponent().getAttributes();
		ProductGroup group = (ProductGroup) attributes.get("group");
		compositeProduct.removeGroup(group);

		setCompositeProductToDirtyState();
	}

	public void deleteCompositeProduct(ActionEvent actionEvent) {
		Map<String, Object> attributes = actionEvent.getComponent().getAttributes();
		CompositeProduct compositeProduct = (CompositeProduct) attributes.get("compositeProduct");
		compositeProductDao.delete(compositeProduct);
	}

	public String chooseCategory() {
		String categoryId = jsfRequestResolver.getParameter("categoryId");
		this.category = categoryDao.findById(Long.parseLong(categoryId));

		return GROUP_DETAILS;
	}

	public void selectionChanged(ValueChangeEvent evt) {
		String[] selectedValues = (String[]) evt.getNewValue();

		for (String productId : selectedValues) {
			productGroupDetails.addProduct(productDao.findById(Long.parseLong(productId)));
		}

		productGroupDao.makePersistent(productGroupDetails);
	}

	public void takeSelection() {
		List<Product> values = getCategoryItems();

		Iterator<Object> iterator = selectionForGroupProducts.getKeys();

		while (iterator.hasNext()) {
			Integer rowKey = (Integer) iterator.next();
			productGroupDetails.addProduct(values.get(rowKey));
		}

		productGroupDao.makePersistent(productGroupDetails);
	}

	@SuppressWarnings("unchecked")
	public List<Product> getCategoryItems() {
		if (category != null) {
			return new ArrayList<Product>(category.getAllProducts());
		}
		return Collections.EMPTY_LIST;
	}

	public void removeProductFromList(ActionEvent actionEvent) {
		Product product = getProductFromRequest();
		productGroupDetails.removeProduct(product);

		productGroupDao.makePersistent(productGroupDetails);
	}

	public String newProduct() {
		this.compositeProduct = new CompositeProduct();
		return COMPOSITE_NEW;
	}

	public void choosePrimaryProduct(ActionEvent actionEvent) {
		Map<String, Object> attributes = actionEvent.getComponent().getAttributes();
		Product primaryProduct = (Product) attributes.get("primaryProduct");

		productGroupDetails.setPrimaryProduct(primaryProduct);

		productGroupDao.makePersistent(productGroupDetails);
	}

	private void setCompositeProductToDirtyState() {
		compositeProductDirty = true;
		infoMessage = null;
	}

	public CompositeProduct getCompositeProduct() {
		return compositeProduct;
	}

	public void setCompositeProduct(CompositeProduct compositeProduct) {
		this.compositeProduct = compositeProduct;
	}

	public ProductGroup getProductGroup() {
		return productGroup;
	}

	public void setProductGroup(ProductGroup productGroup) {
		this.productGroup = productGroup;
	}

	public CompositeProductDao getCompositeProductDao() {
		return compositeProductDao;
	}

	public void setCompositeProductDao(CompositeProductDao compositeProductDao) {
		this.compositeProductDao = compositeProductDao;
	}

	public ProductGroupDao getProductGroupDao() {
		return productGroupDao;
	}

	public void setProductGroupDao(ProductGroupDao productGroupDao) {
		this.productGroupDao = productGroupDao;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public ProductGroup getProductGroupDetails() {
		return productGroupDetails;
	}

	public List<Product> getProductGroupItems() {
		return new LinkedList<Product>(productGroupDetails.getProducts());
	}

	public void setProductGroupDetails(ProductGroup productGroupDetails) {
		this.productGroupDetails = productGroupDetails;
	}

	public String getInfoMessage() {
		return infoMessage;
	}

	public void setInfoMessage(String saveMessage) {
		this.infoMessage = saveMessage;
	}

	public boolean isCompositeProductDirty() {
		return compositeProductDirty;
	}

	public void setCompositeProductDirty(boolean compositeProductDirty) {
		this.compositeProductDirty = compositeProductDirty;
	}

	public Selection getSelectionForGroupProducts() {
		return selectionForGroupProducts;
	}

	public void setSelectionForGroupProducts(Selection selectionForGroupProducts) {
		this.selectionForGroupProducts = selectionForGroupProducts;
	}
}

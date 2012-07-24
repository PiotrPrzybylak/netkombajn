package pl.netolution.sklep3.web.jsf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.richfaces.model.selection.Selection;

import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.domain.Price;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.service.ProductService;
import pl.netolution.sklep3.utils.ProductsQueryBuilder;
import pl.netolution.sklep3.web.jsf.utils.MessageHelper;

public class ProductBackingBean extends ProductBackingBase {

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ProductBackingBean.class);

	private static final String PRODUCT_DETAILS_VIEW = "productDetails";

	private static final String PRODUCTS_VIEW = "products";

	private Category choosenMassEditCategory = new Category();

	private int rowsOnPage;

	private Set<Product> productsForMassEdit = new HashSet<Product>();

	private Product templateMassEditProduct = new Product();

	private Price massEditPriceAddition;

	private int massEditionPercentageChange;

	private MessageHelper messageHelper;

	private Selection selectionForMassEdit;

	private ProductService productService;

	public List<Product> getProducts() {

		return obtainProducts();

	}

	@SuppressWarnings("unchecked")
	private List<Product> obtainProducts() {
		if (choosenCategory != null) {
			return getProductsByCategory();
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	public void chooseGroupCategory(ActionEvent actionEvent) {
		Map<String, Object> attributes = actionEvent.getComponent().getAttributes();
		long categoryId = Long.parseLong(attributes.get("categoryId").toString());
		choosenMassEditCategory = categoryDao.findById(categoryId);
	}

	public String chooseProduct() {
		product = getProductFromRequest();
		return PRODUCT_DETAILS_VIEW;
	}

	public String deleteProduct() {

		long productId = getProductIdFromRequest();
		productService.deleteProduct(productId);
		return PRODUCTS_VIEW;
	}

	public void removeProductFromList() {
		Product product = getProductFromRequest();
		productsForMassEdit.remove(product);
	}

	public String hideProduct() {
		productService.hideProduct(getProductIdFromRequest());
		return null;
	}

	public String unhideProduct() {
		productService.unhideProduct(getProductIdFromRequest());
		return null;
	}

	public void takeSelection() {
		List<Product> values = getMassEditCategoryProducts();

		Iterator<Object> iterator = selectionForMassEdit.getKeys();

		while (iterator.hasNext()) {
			Integer rowKey = (Integer) iterator.next();
			productsForMassEdit.add(values.get(rowKey));
		}
	}

	public void editGroupManualAvailability() {
		for (Product product : productsForMassEdit) {
			product.setManualAvailability(templateMassEditProduct.getManualAvailability());

			//TODO batch update?
			productDao.makePersistent(product);
		}

		messageHelper.addMessageToContext("Zakończono grupową edycję produktów : ręczny status - "
				+ templateMassEditProduct.getManualAvailability());
	}

	public void editGroupWeight() {
		for (Product product : productsForMassEdit) {
			product.setWeight(templateMassEditProduct.getWeight());

			//TODO batch update?
			productDao.makePersistent(product);
		}

		messageHelper.addMessageToContext("Zakończono grupową edycję produktów: waga - " + templateMassEditProduct.getWeight());

	}

	public void editGroupPrice() {
		for (Product product : productsForMassEdit) {
			Price newProductPrice = product.getRetailGrossPrice().add(massEditPriceAddition);

			if (isNewPriceValid(product, newProductPrice)) {
				product.setRetailGrossPrice(newProductPrice);
			}

			//TODO batch update?
			productDao.makePersistent(product);
		}

		messageHelper.addMessageToContext("Zakończono grupową edycję produktów : zmiana o cenę  " + massEditPriceAddition);
	}

	public void editGroupPriceByPercentage() {
		for (Product product : productsForMassEdit) {
			Price newProductPrice = product.getRetailGrossPrice().changeByPercentage(massEditionPercentageChange);

			if (isNewPriceValid(product, newProductPrice)) {
				product.setRetailGrossPrice(newProductPrice);
			}

			//TODO batch update?
			productDao.makePersistent(product);
		}

		messageHelper.addMessageToContext("Zakończono grupową edycję produktów : zmiana procentowa  " + massEditionPercentageChange);
	}

	public void editGroupManufacturer() {
		for (Product product : productsForMassEdit) {
			product.setManufacturer(templateMassEditProduct.getManufacturer());

			//TODO batch update?
			productDao.makePersistent(product);
		}

		messageHelper.addMessageToContext("Zakończono grupową edycję produktów : producent - " + templateMassEditProduct.getManufacturer());
	}

	private boolean isNewPriceValid(Product product, Price newProductPrice) {
		Integer newProductMarkup = newProductPrice.countMarkup(product.getWholesaleNetPrice());

		if (newProductMarkup != null && newProductMarkup <= 0) {
			messageHelper.addMessageToContext("Cena po edycji byłaby zbyt mała dla produktu " + product.getName() + " : id="
					+ product.getId());
			return false;
		}

		return true;
	}

	private List<Product> getProductsByCategory() {
		List<Product> products;
		ProductsQueryBuilder queryBuilder = productDao.getOldSkulNoLucenceProductsQueryBuilder();
		queryBuilder.addCategory(choosenCategory);
		queryBuilder.setShowOnlyVisible(false);
		products = productDao.searchProducts(queryBuilder);
		return products;
	}

	public List<Product> getMassEditCategoryProducts() {
		Set<Product> products = choosenMassEditCategory.getAllProducts();

		return new ArrayList<Product>(products);
	}

	public Category getChoosenCategory() {
		return choosenCategory;
	}

	public int getRowsOnPage() {
		return rowsOnPage;
	}

	public void setRowsOnPage(int rowsOnSingelPage) {
		this.rowsOnPage = rowsOnSingelPage;
	}

	public List<Product> getProductsForMassEdit() {
		return new LinkedList<Product>(productsForMassEdit);
	}

	public void setProductsForMassEdit(Set<Product> productsGroup) {
		this.productsForMassEdit = productsGroup;
	}

	public Category getChoosenMassEditCategory() {
		return choosenMassEditCategory;
	}

	public void setChoosenMassEditCategory(Category choosenCategory2) {
		this.choosenMassEditCategory = choosenCategory2;
	}

	public Product getTemplateMassEditProduct() {
		return templateMassEditProduct;
	}

	public void setTemplateMassEditProduct(Product templateGroupProduct) {
		this.templateMassEditProduct = templateGroupProduct;
	}

	public Price getMassEditPriceAddition() {
		return massEditPriceAddition;
	}

	public void setMassEditPriceAddition(Price additionToGroupPrice) {
		this.massEditPriceAddition = additionToGroupPrice;
	}

	public int getMassEditionPercentageChange() {
		return massEditionPercentageChange;
	}

	public void setMassEditionPercentageChange(int massEditionPercentageChange) {
		this.massEditionPercentageChange = massEditionPercentageChange;
	}

	public void setMessageHelper(MessageHelper messageHelper) {
		this.messageHelper = messageHelper;
	}

	public Selection getSelectionForMassEdit() {
		return selectionForMassEdit;
	}

	public void setSelectionForMassEdit(Selection selectionForMassEdit) {
		this.selectionForMassEdit = selectionForMassEdit;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

}

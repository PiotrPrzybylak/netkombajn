package pl.netolution.sklep3.web.jsf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import pl.netolution.sklep3.dao.CategoryDao;
import pl.netolution.sklep3.dao.DesignerDao;
import pl.netolution.sklep3.dao.ManufacturerDao;
import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.Product.Availability;

public class ProductBackingBase {

	private static final String PRODUCT_ID_PARAM_NAME = "productId";
	protected ProductDao productDao;
	protected CategoryDao categoryDao;
	protected ManufacturerDao manufacturerDao;
	protected DesignerDao designerDao;
	protected Product product;
	protected JsfRequestResolver jsfRequestResolver;
	protected Category choosenCategory;

	public ProductBackingBase() {
		super();
	}

	protected Product getProductFromRequest() {
		long productId = getProductIdFromRequest();
		return productDao.findById(productId);
	}

	protected long getProductIdFromRequest() {
		long productId = Long.parseLong(jsfRequestResolver.getParameter(PRODUCT_ID_PARAM_NAME));
		return productId;
	}

	public List<SelectItem> getAvailabilityStatuses() {

		List<SelectItem> items = new ArrayList<SelectItem>();
		ResourceBundle availabilityBundle = ResourceBundle.getBundle("resources.locale.availability");
		for (Availability availability : Availability.values()) {
			items.add(new SelectItem(availability, availabilityBundle.getString(availability.toString())));
		}
		items.add(new SelectItem(null, "Automatyczna"));
		return items;
	}

	public Product getProduct() {
		return product;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public void setJsfRequestResolver(JsfRequestResolver jsfRequestResolver) {
		this.jsfRequestResolver = jsfRequestResolver;
	}

	public void setCategoryDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	public void setManufacturerDao(ManufacturerDao manufacturerDao) {
		this.manufacturerDao = manufacturerDao;
	}

	public void setDesignerDao(DesignerDao designerDao) {
		this.designerDao = designerDao;
	}

	public void chooseCategory(ActionEvent actionEvent) {
		Map<String, Object> attributes = actionEvent.getComponent().getAttributes();
		long categoryId = Long.parseLong(attributes.get("categoryId").toString());
		choosenCategory = categoryDao.findById(categoryId);
	}

}
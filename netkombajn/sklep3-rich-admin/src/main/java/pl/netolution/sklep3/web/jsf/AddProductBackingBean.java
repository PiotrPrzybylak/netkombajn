package pl.netolution.sklep3.web.jsf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import pl.netolution.sklep3.dao.DesignerDao;
import pl.netolution.sklep3.dao.ManufacturerDao;
import pl.netolution.sklep3.dao.PictureDao;
import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.domain.Designer;
import pl.netolution.sklep3.domain.Manufacturer;
import pl.netolution.sklep3.domain.Picture;
import pl.netolution.sklep3.domain.Price;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.ProductPicture;
import pl.netolution.sklep3.domain.StockKeepingUnit;
import pl.netolution.sklep3.service.PictureManager;

public class AddProductBackingBean extends ProductBackingBase {

	private static final String DETAILS_TAB_NAME = "details";

	private static final String PICTURES_TAB_NAME = "pictures";

	private static final String CATEGORIES_TAB_NAME = "categoories";

	private static final Logger log = Logger.getLogger(AddProductBackingBean.class);

	private Long categoryId;

	private Long manufacturerId;

	private Long designerId;

	private PictureManager pictureManager;

	//TODO zostawic tylko pictureManagera
	private PictureDao pictureDao;

	private String selectedTabName = DETAILS_TAB_NAME;

	private StockKeepingUnit editedStockKeepingUnit;

	private StockKeepingUnit addedStockKeepingUnit = new StockKeepingUnit();

	public void addLinkedCategory(ActionEvent actionEvent) {
		super.chooseCategory(actionEvent);
		product.addLinkedCategory(choosenCategory);
		productDao.makePersistent(product);
		selectedTabName = CATEGORIES_TAB_NAME;
	}

	public void removeLinkedCategory(ActionEvent actionEvent) {
		super.chooseCategory(actionEvent);
		product.removeLinkedCategory(choosenCategory);
		productDao.makePersistent(product);
	}

	public void updateSku() {
		productDao.makePersistent(product);
		editedStockKeepingUnit = null;
	}

	public void addNewSku() {
		product.addSku(addedStockKeepingUnit);
		productDao.makePersistent(product);
		addedStockKeepingUnit = createNewSkuTemplate(addedStockKeepingUnit);

	}

	private StockKeepingUnit createNewSkuTemplate(StockKeepingUnit oldSku) {
		StockKeepingUnit newSku = new StockKeepingUnit();
		newSku.setName(oldSku.getName());
		newSku.setAvailability(oldSku.getAvailability());
		newSku.setOriginalPrice(oldSku.getOriginalPrice());
		return newSku;
	}

	public void deleteSku() {
		product.removeSku(editedStockKeepingUnit);
		productDao.makePersistent(product);
		editedStockKeepingUnit = null;
	}

	public String saveProduct() {

		//TODO zaimplementowac to w oparciu o narzut
		//		if (product.getPrice().getProfitMargin() < 0) {
		//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Wprowadzona cena brutto jest mniejsza od ceny hurtowej!"));
		//			return "addProduct";
		//		}

		setManufacturerOnProduct();
		setDesignerOnProduct();
		setCategoryOnProduct();

		product.setLastUpdate(new Date());

		String returnViewName = resolveReturnViewName();

		productDao.makePersistent(product);

		selectedTabName = PICTURES_TAB_NAME;

		return returnViewName;
	}

	private String resolveReturnViewName() {
		String returnViewName;
		if (product.getId() != null) {
			returnViewName = "products";
		} else {
			returnViewName = "addProduct";
		}
		return returnViewName;
	}

	private void setCategoryOnProduct() {
		Category category = categoryDao.findById(categoryId);
		log.debug("Setting category: " + category + " on product: " + product);
		product.setCategory(category);
	}

	private void setManufacturerOnProduct() {
		Manufacturer manufacturer = manufacturerDao.findById(manufacturerId);
		product.setManufacturer(manufacturer);
		log.debug("Setting manufacturer: " + manufacturer + " on product: " + product);
	}

	private void setDesignerOnProduct() {
		Manufacturer manufacturer = manufacturerDao.findById(manufacturerId);
		product.setManufacturer(manufacturer);

		Designer designer = designerDao.findById(designerId);
		product.setDesigner(designer);
	}

	public String cloneProduct() {
		editProduct();
		// TODO umiescic tu funkcjonalnosc ktora jest teraz w clone
		product = product.clone();
		product.setCreation(new Date());
		return "addProduct";
	}

	public String addProduct() {
		selectedTabName = DETAILS_TAB_NAME;
		product = new Product();
		product.setVisible(true);
		product.setRetailGrossPrice(new Price(0));
		product.addDefaultSkuIfNecessary();
		product.setCreation(new Date());
		product.setSource("MANUAL");
		editedStockKeepingUnit = null;
		return "addProduct";
	}

	public String editProduct() {
		selectedTabName = DETAILS_TAB_NAME;
		product = getProductFromRequest();
		initializeCategoryId();
		initializeManufacturerId();
		initializeDesignerId();
		initializeSku();
		initializeLinkedCategories();

		return "addProduct";
	}

	private void initializeCategoryId() {
		categoryId = product.getCategory().getId();
	}

	private void initializeManufacturerId() {
		if (product.getManufacturer() != null) {
			manufacturerId = product.getManufacturer().getId();
		}
	}

	private void initializeDesignerId() {
		if (product.getDesigner() != null) {
			designerId = product.getDesigner().getId();
		}
	}

	private void initializeSku() {
		product.addDefaultSkuIfNecessary();
		editedStockKeepingUnit = null;
		StockKeepingUnit lastAddedSku = product.getSkus().get(product.getSkus().size() - 1);
		addedStockKeepingUnit = createNewSkuTemplate(lastAddedSku);
	}

	private void initializeLinkedCategories() {
		product.getLinkedCategories().size();
	}

	public void uploadListener(UploadEvent event) {

		for (UploadItem uploadedPicture : event.getUploadItems()) {
			ProductPicture picture = new ProductPicture();
			picture.setOriginalName(uploadedPicture.getFileName());
			product.addPicture(picture);
			pictureDao.makePersistent(picture);
			pictureManager.savePictureFromTempFile(picture, uploadedPicture.getFile());
		}
	}

	public void deletePicture(ActionEvent actionEvent) {
		Map<String, Object> attributes = actionEvent.getComponent().getAttributes();
		long pictureId = Long.parseLong(attributes.get("pictureId").toString());

		Picture picture = getPictureFromProductById(pictureId);
		product.removePicture(picture);
		pictureManager.deletePicture(picture);
		pictureDao.delete(picture);

	}

	// TODO Override method "equals" on Picture class properly instead 
	private Picture getPictureFromProductById(Long pictureId) {
		for (Picture productPicture : product.getPictures()) {
			if (productPicture.getId().equals(pictureId)) {
				return productPicture;
			}
		}
		throw new RuntimeException("No picture with id " + pictureId + " found in product with id " + product.getId());
	}

	public List<SelectItem> getManufacturers() {

		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem(0L, "--"));
		for (Manufacturer manufacturer : manufacturerDao.getAllSortedBy(ManufacturerDao.NAME)) {
			items.add(new SelectItem(manufacturer.getId(), manufacturer.getName()));
		}
		return items;
	}

	public List<SelectItem> getDesigners() {

		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem(0L, "--"));
		for (Designer designer : designerDao.getAllSortedBy(DesignerDao.NAME)) {
			items.add(new SelectItem(designer.getId(), designer.getName()));
		}
		return items;
	}

	public List<SelectItem> getCategories() {

		List<SelectItem> items = new ArrayList<SelectItem>();
		for (Category category : categoryDao.getAll()) {
			items.add(new SelectItem(category.getId(), category.getFullPath()));
		}
		return items;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public void setPictureManager(PictureManager pictureManager) {
		this.pictureManager = pictureManager;
	}

	public Long getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Long manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public Long getDesignerId() {
		return designerId;
	}

	public void setDesignerId(Long designerId) {
		this.designerId = designerId;
	}

	public void setPictureDao(PictureDao pictureDao) {
		this.pictureDao = pictureDao;
	}

	public String getSelectedTabName() {
		return selectedTabName;
	}

	public void setSelectedTabName(String selectedTabName) {
		this.selectedTabName = selectedTabName;
	}

	public StockKeepingUnit getEditedStockKeepingUnit() {
		return editedStockKeepingUnit;
	}

	public void setEditedStockKeepingUnit(StockKeepingUnit editedStockKeepingUnit) {
		this.editedStockKeepingUnit = editedStockKeepingUnit;
	}

	public StockKeepingUnit getAddedStockKeepingUnit() {
		return addedStockKeepingUnit;
	}

	public void setAddedStockKeepingUnit(StockKeepingUnit addedStockKeepingUnit) {
		this.addedStockKeepingUnit = addedStockKeepingUnit;
	}

	public List<SelectItem> getDiscountInPercentsOptions() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		for (int i = 0; i <= 50; i++) {
			items.add(new SelectItem(i, i + "%"));
		}
		return items;
	}

}

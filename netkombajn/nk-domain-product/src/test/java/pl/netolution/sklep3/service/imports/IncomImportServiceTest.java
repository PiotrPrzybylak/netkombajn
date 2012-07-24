package pl.netolution.sklep3.service.imports;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;

import pl.netolution.sklep3.dao.AdminConfigurationDao;
import pl.netolution.sklep3.dao.CategoryDao;
import pl.netolution.sklep3.dao.ManufacturerDao;
import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.AdminConfiguration;
import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.Product.Availability;
import pl.netolution.sklep3.service.EmailService;

public class IncomImportServiceTest extends TestCase {
	private static final String SOME_OLD_DESCRIPTION = "SOME OLD DESCRIPTION";

	private static final String CATALOG_ID_1 = "CATALOG_ID_1";

	private static final String CATALOG_ID_2 = "CATALOG_ID_2";

	private static final Logger log = Logger.getLogger(IncomImportServiceTest.class);

	private static final String CATEGORY_EXTERNAL_ID = "1985";

	private IncomImportService service;

	private Category category;

	private Category oldCategory;

	private List<Product> oldProducts;

	private Product existingProduct;

	private ImportStatus importStatus;

	private Document document;

	private AdminConfiguration adminConfiguration;

	private ProductDao productDao;

	EmailService emailService;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		service = new IncomImportService();
	}

	@Override
	protected void tearDown() throws Exception {
		service = null;
		super.tearDown();
	}

	private Map<String, String> createNames() {

		Map<String, String> names = new HashMap<String, String>();

		names.put("root", "rootName");
		names.put("rootChild1", "rootChild1Name");
		names.put("rootChild2", "rootChild2Name");
		names.put("child11", "child11Name");
		names.put("child12", "child12Name");
		names.put("child13", "child13Name");
		names.put("child21", "child21Name");

		return names;
	}

	private Map<String, List<String>> createImportedStructure() {
		Map<String, List<String>> categoriesTree = new HashMap<String, List<String>>();

		categoriesTree.put("", Arrays.asList(new String[] { "root" }));
		categoriesTree.put("root", Arrays.asList(new String[] { "rootChild1", "rootChild2" }));
		categoriesTree.put("rootChild1", Arrays.asList(new String[] { "child11", "child12", "child13" }));
		categoriesTree.put("rootChild2", Arrays.asList(new String[] { "child21" }));

		return categoriesTree;
	}

	public void testMergeImportedCategories_db_with_only_root() {
		// given
		Map<String, String> names = createNames();

		Map<String, List<String>> categoriesTree = createImportedStructure();

		CategoryDao categoryDao = mock(CategoryDao.class);
		Category root = new Category();
		when(categoryDao.findByExternalId("root")).thenReturn(root);
		service.setCategoryDao(categoryDao);

		// when
		service.mergeImportCategories(categoriesTree, names);

		// then
		assertEquals(2, root.getChildren().size());
		assertEquals(3, root.getChildren().get(0).getChildren().size());
		assertEquals(1, root.getChildren().get(1).getChildren().size());
	}

	public void test_shouldCreateNewProductsFromImportedXml() throws MalformedURLException, DocumentException {

		// given
		createProductImportMocks();

		// when
		service.importProducts(document, importStatus);

		//then

		ArgumentCaptor<Product> argument = new ArgumentCaptor<Product>();
		verify(productDao, times(2)).makePersistent(argument.capture());

		Product newProduct = argument.getAllValues().get(0);
		log.debug(newProduct);
		assertEquals(category, newProduct.getCategory());
		assertEquals(CATALOG_ID_1, newProduct.getCatalogNumber());
		assertEquals("Produkt testowy Nowy", newProduct.getName());
		assertEquals(false, newProduct.isUseExternalPicture());

	}

	public void test_shouldUpdateExistingProductsFromImportedXml() throws MalformedURLException, DocumentException {
		// given
		createProductImportMocks();

		existingProduct.setCategory(oldCategory);
		existingProduct.setName("Stara nazwa");
		existingProduct.setDescription(SOME_OLD_DESCRIPTION);

		// when 
		service.importProducts(document, importStatus);

		// then

		assertEquals(oldCategory, existingProduct.getCategory());
		assertEquals("Stara nazwa", existingProduct.getName());
		assertEquals(SOME_OLD_DESCRIPTION, existingProduct.getDescription());
	}

	public void test_shouldSetVisibleFalseForProductsNotFoundInXmlAnymore() throws DocumentException {

		//given
		createProductImportMocks();
		for (int i = 0; i < 3; i++) {
			Product oldProduct = new Product(1 + i);
			oldProduct.setVisible(true);
			oldProducts.add(oldProduct);
		}

		// when 
		service.importProducts(document, importStatus);

		// then
		for (int i = 0; i < 3; i++) {
			assertFalse(oldProducts.get(i).isVisible());
		}

	}

	public void test_shouldFillImportStatusDuringImport() throws DocumentException {

		// given
		createProductImportMocks();

		// when 
		service.importProducts(document, importStatus);

		// then

		assertEquals(4, importStatus.getProcesseedElements());
		assertEquals(100, importStatus.getProgressInPrecents());
	}

	private Document getProductsImportDocument() throws DocumentException {
		return new SAXReader().read(Thread.currentThread().getContextClassLoader().getResourceAsStream("cennik_maly.xml"));
	}

	public void test_shouldSetDefaultCategoryManualAvailabilityForNewProducts() throws DocumentException {
		// given
		createProductImportMocks();

		category.setDefaultManualAvailability(Availability.SOON);
		// when
		service.importProducts(document, importStatus);

		// then
		ArgumentCaptor<Product> argument = new ArgumentCaptor<Product>();
		verify(productDao, times(2)).makePersistent(argument.capture());
		Product newProduct = argument.getAllValues().get(0);
		assertEquals(Availability.SOON, newProduct.getManualAvailability());

	}

	public void test_shouldCalculatePriceForNewProductsWithCategoryMargin() throws DocumentException {
		// given
		createProductImportMocks();
		adminConfiguration.setProfitMargin(33);
		category.setProfitMargin(200);

		// when
		service.importProducts(document, importStatus);
		// then
		ArgumentCaptor<Product> argument = new ArgumentCaptor<Product>();
		verify(productDao, times(2)).makePersistent(argument.capture());
		Product newProduct = argument.getAllValues().get(0);

		assertEquals(new Integer(200), newProduct.getMarkup());

	}

	public void test_shouldSetDefaultCategoryWeightForNewProducts() throws DocumentException {
		// given
		createProductImportMocks();
		category.setWeight(2.5);

		// when
		service.importProducts(document, importStatus);

		// then
		ArgumentCaptor<Product> argument = new ArgumentCaptor<Product>();
		verify(productDao, times(2)).makePersistent(argument.capture());
		Product newProduct = argument.getAllValues().get(0);
		assertEquals(2.5, newProduct.getWeight());
	}

	public void test_shouldSetManualAvailability_TEMPORARY_UNAVIALABLE_ForExistingProductsWhenQuantityInStockIsZeroAndYesterdayWasNot()
			throws DocumentException {
		// given
		createProductImportMocks();
		existingProduct.setQuantityInStock(11L);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date yesterday = calendar.getTime();
		existingProduct.setLastUpdate(yesterday);

		// when
		service.importProducts(document, importStatus);

		// then
		assertEquals(Availability.TEMPORARY_SHORTAGE, existingProduct.getManualAvailability());

	}

	public void test_shouldSendEmailAfterSuccessfulImport() throws DocumentException {
		// given
		createProductImportMocks();

		// when
		service.importProducts(document, importStatus);

		// then
		verify(emailService).sendImportFinishedEmail();

	}

	private void createProductImportMocks() throws DocumentException {

		document = getProductsImportDocument();

		adminConfiguration = new AdminConfiguration();
		adminConfiguration.setProfitMargin(50);

		AdminConfigurationDao adminConfigurationDao = mock(AdminConfigurationDao.class);
		when(adminConfigurationDao.getMainConfiguration()).thenReturn(adminConfiguration);

		service.setAdminConfigurationDao(adminConfigurationDao);

		CategoryDao categoryDao = mock(CategoryDao.class);
		category = new Category();
		when(categoryDao.findByExternalId(CATEGORY_EXTERNAL_ID)).thenReturn(category);

		service.setCategoryDao(categoryDao);

		productDao = Mockito.mock(ProductDao.class);
		when(productDao.findByCatalogNumber(CATALOG_ID_1)).thenReturn(null);
		existingProduct = new Product(1);
		oldCategory = new Category();
		when(productDao.findByCatalogNumber(CATALOG_ID_2)).thenReturn(existingProduct);
		//productDao.makePersistent(existingProduct);

		oldProducts = new ArrayList<Product>();

		when(productDao.getRetiredProducts(Mockito.eq("INCOM"), Mockito.any(Date.class))).thenReturn(oldProducts);
		service.setProductDao(productDao);

		emailService = mock(EmailService.class);
		service.setEmailService(emailService);

		ManufacturerDao manufacturerDao = mock(ManufacturerDao.class);
		when(manufacturerDao.findByName(Matchers.anyString())).thenReturn(null);
		service.setManufacturerDao(manufacturerDao);

		importStatus = new ImportStatus();
	}
}

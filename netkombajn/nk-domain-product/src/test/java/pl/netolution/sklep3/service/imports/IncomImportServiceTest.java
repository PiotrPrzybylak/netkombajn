package pl.netolution.sklep3.service.imports;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.netkombajn.eshop.product.imports.ImportStatus;
import com.netkombajn.eshop.product.imports.IncomImportService;
import com.netkombajn.eshop.product.imports.IncomImportService.Configuration;

import pl.netolution.sklep3.dao.CategoryDao;
import pl.netolution.sklep3.dao.ManufacturerDao;
import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.Product.Availability;
import pl.netolution.sklep3.service.EmailService;

import java.util.*;

import static org.mockito.Mockito.*;

public class IncomImportServiceTest extends TestCase {
    private static final Logger log = Logger.getLogger(IncomImportServiceTest.class);

	private static final String SOME_OLD_DESCRIPTION = "SOME OLD DESCRIPTION";

	private static final String CATALOG_ID_1 = "CATALOG_ID_1";

	private static final String CATALOG_ID_2 = "CATALOG_ID_2";

	private static final String CATEGORY_EXTERNAL_ID = "1985";

	private IncomImportService service = new IncomImportService();

	private Category category;

	private List<Product> oldProducts;

	private Product existingProduct;

	private ImportStatus importStatus;

	private List<Map<String, String>> productsFromXml;

	private Configuration configuration = mock(Configuration.class);

	private ProductDao productDao = mock(ProductDao.class);

	private EmailService emailService = mock(EmailService.class);

	public void test_shouldCreateNewProductsFromImportedXml() {

		// given
		createProductImportMocks();

		// when
		service.importProducts(productsFromXml, importStatus);

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

	public void test_shouldUpdateExistingProductsFromImportedXml() {
		// given
		createProductImportMocks();

		Map<String, String> productDetails = createWarehouseProduct();	
		
		productsFromXml = Arrays.asList(productDetails);
		
		Category oldCategory = new Category();
		existingProduct.setCategory(oldCategory);
		existingProduct.setName("Stara nazwa");
		existingProduct.setDescription(SOME_OLD_DESCRIPTION);
		existingProduct.setQuantityInStock(0L);

		// when 
		service.importProducts(productsFromXml, importStatus);

		// then

		assertEquals(oldCategory, existingProduct.getCategory());
		assertEquals("Stara nazwa", existingProduct.getName());
		assertEquals(SOME_OLD_DESCRIPTION, existingProduct.getDescription());
		assertEquals(Availability.HIGH, existingProduct.getAvailability());
		//assertEquals((Long)10L, existingProduct.getQuantityInStock());
	}

	public void test_shouldSetVisibleFalseForProductsNotFoundInXmlAnymore() {

		//given
		createProductImportMocks();
		for (int i = 0; i < 3; i++) {
			Product oldProduct = new Product(1 + i);
			oldProduct.setVisible(true);
			oldProducts.add(oldProduct);
		}

		// when 
		service.importProducts(productsFromXml, importStatus);

		// then
		for (int i = 0; i < 3; i++) {
			assertFalse(oldProducts.get(i).isVisible());
		}

	}

	public void test_shouldFillImportStatusDuringImport() {

		// given
		createProductImportMocks();

		// when 
		service.importProducts(productsFromXml, importStatus);

		// then

		assertEquals(4, importStatus.getProcesseedElements());
		assertEquals(100, importStatus.getProgressInPrecents());
	}


	public void test_shouldSetDefaultCategoryManualAvailabilityForNewProducts() {
		// given
		createProductImportMocks();

		category.setDefaultManualAvailability(Availability.SOON);
		// when
		service.importProducts(productsFromXml, importStatus);

		// then
		ArgumentCaptor<Product> argument = new ArgumentCaptor<Product>();
		verify(productDao, times(2)).makePersistent(argument.capture());
		Product newProduct = argument.getAllValues().get(0);
		assertEquals(Availability.SOON, newProduct.getManualAvailability());

	}

	public void test_shouldCalculatePriceForNewProductsWithCategoryMargin() {
		// given
		createProductImportMocks();
		when(configuration.getProfitMargin()).thenReturn(33);
		category.setProfitMargin(200);

		// when
		service.importProducts(productsFromXml, importStatus);
		// then
		ArgumentCaptor<Product> argument = new ArgumentCaptor<Product>();
		verify(productDao, times(2)).makePersistent(argument.capture());
		Product newProduct = argument.getAllValues().get(0);

		assertEquals(new Integer(200), newProduct.getMarkup());

	}

	public void test_shouldSetDefaultCategoryWeightForNewProducts() {
		// given
		createProductImportMocks();
		category.setWeight(2.5);

		// when
		service.importProducts(productsFromXml, importStatus);

		// then
		ArgumentCaptor<Product> argument = new ArgumentCaptor<Product>();
		verify(productDao, times(2)).makePersistent(argument.capture());
		Product newProduct = argument.getAllValues().get(0);
		assertEquals(2.5, newProduct.getWeight());
	}

	public void test_shouldSetManualAvailability_TEMPORARY_UNAVIALABLE_ForExistingProductsWhenQuantityInStockIsZeroAndYesterdayWasNot() {
		// given
		createProductImportMocks();
		existingProduct.setQuantityInStock(11L);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date yesterday = calendar.getTime();
		existingProduct.setLastUpdate(yesterday);

		// when
		service.importProducts(productsFromXml, importStatus);

		// then
		assertEquals(Availability.TEMPORARY_SHORTAGE, existingProduct.getManualAvailability());

	}

	public void test_shouldSendEmailAfterSuccessfulImport() {
		// given
		createProductImportMocks();

		// when
		service.importProducts(productsFromXml, importStatus);

		// then
		verify(emailService).sendImportFinishedEmail();

	}

	private void createProductImportMocks() {

		CategoryDao categoryDao = mock(CategoryDao.class);
		ManufacturerDao manufacturerDao = mock(ManufacturerDao.class);
		service.setConfiguration(configuration);
		service.setCategoryDao(categoryDao);
		service.setProductDao(productDao);
		service.setManufacturerDao(manufacturerDao);
		service.setEmailService(emailService);

		
		List<Map<String, String>> productsFromXml2 = createProductsLikeInIntegrationTest();
		productsFromXml = productsFromXml2;

		when(configuration.getProfitMargin()).thenReturn(50);


		category = new Category();
		when(categoryDao.findByExternalId(CATEGORY_EXTERNAL_ID)).thenReturn(category);

		
		when(productDao.findByCatalogNumber(CATALOG_ID_1)).thenReturn(null);
		existingProduct = new Product(1);
		when(productDao.findByCatalogNumber(CATALOG_ID_2)).thenReturn(existingProduct);

		oldProducts = new ArrayList<Product>();

		when(productDao.getRetiredProducts(Mockito.eq("INCOM"), Mockito.any(Date.class))).thenReturn(oldProducts);


		when(manufacturerDao.findByName(Matchers.anyString())).thenReturn(null);

		importStatus = new ImportStatus();
	}

	private List<Map<String, String>> createProductsLikeInIntegrationTest() {
		Map<String, String> productDetails1 = createWarehouseProduct();	
		productDetails1.put("symbol_produktu", CATALOG_ID_1);
		productDetails1.put("nazwa_produktu", "Produkt testowy Nowy");
		Map<String, String> productDetails2 = createWarehouseProduct();	
		Map<String, String> productDetails3 = createWarehouseProduct();	
		productDetails3.put("cena", "tel");
		Map<String, String> productDetails4 = createWarehouseProduct();	
		productDetails4.remove("cena");
		
		List<Map<String, String>> productsFromXml2 = Arrays.asList(productDetails1, productDetails2, productDetails3, productDetails4);
		return productsFromXml2;
	}

	private Map<String, String> createWarehouseProduct() {
		Map<String, String> productDetails = new HashMap<String, String>();
		productDetails.put("symbol_produktu", "CATALOG_ID_2");
		productDetails.put("stan_magazynowy", "2000");
		productDetails.put("cena", "2000,00");
		productDetails.put("grupa_towarowa", "1985");
		return productDetails;
	}
}

package pl.netolution.sklep3.service;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createStrictMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.netkombjan.customerActions.orderSubmition.EmptyOrderException;

import pl.netolution.sklep3.configuration.Configuration;
import pl.netolution.sklep3.dao.AdminConfigurationDao;
import pl.netolution.sklep3.dao.CategoryDao;
import pl.netolution.sklep3.dao.NewsletterRecipientDao;
import pl.netolution.sklep3.dao.OrderDao;
import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.AdminConfiguration;
import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.domain.NewsletterRecipient;
import pl.netolution.sklep3.domain.Order;
import pl.netolution.sklep3.domain.Price;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.ShipmentOption;
import pl.netolution.sklep3.domain.SkuOrderItem;

@RunWith(MockitoJUnitRunner.class)
public class ShopServiceTest {

	private static final int MAX_NEW_PRODUCTS = 5;

	private ShopService shopService;

	@Mock
	private ProductDao productDao;

	@Mock
	private AdminConfiguration adminConfiguration;

	@Mock
	private AdminConfigurationDao adminConfigurationDao;

	@Before
	public void setUp() throws Exception {
		shopService = new ShopService();
		shopService.setProductDao(productDao);
	}


	@Test
	public void testGetHitProduct() {
		ProductDao productDao = createStrictMock(ProductDao.class);
		List<Product> products = new ArrayList<Product>();
		Product product1 = new Product();
		products.add(product1);
		product1.setId(1L);
		Product product2 = new Product();
		product2.setId(2L);
		products.add(product2);
		Product product3 = new Product();
		product3.setId(3L);
		products.add(product3);

		RandomService randomService = createStrictMock(RandomService.class);
		int productCount = 3;
		expect(randomService.getRandomNumber(productCount)).andReturn(1);
		EasyMock.replay(randomService);
		EasyMock.expect(productDao.getHitProducts()).andReturn(products);
		EasyMock.replay(productDao);
		shopService.setProductDao(productDao);
		shopService.setRandomService(randomService);

		assertEquals(product2, shopService.getHitProduct());

	}

	@Test
	public void testGetHitProduct_zeroProducts() {
		ProductDao productDao = createStrictMock(ProductDao.class);
		List<Product> products = new ArrayList<Product>();
		EasyMock.expect(productDao.getHitProducts()).andReturn(products);
		EasyMock.replay(productDao);
		shopService.setProductDao(productDao);

		assertNull(shopService.getHitProduct());

	}

	@Test
	public void testGetNewProducts() {
		ProductDao productDao = EasyMock.createStrictMock(ProductDao.class);
		List<Product> products = new ArrayList<Product>();
		expect(productDao.getNewProducts(MAX_NEW_PRODUCTS)).andReturn(products);
		replay(productDao);
		shopService.setProductDao(productDao);

		Configuration configuration = EasyMock.createStrictMock(Configuration.class);
		expect(configuration.getMaxNewProducts()).andReturn(MAX_NEW_PRODUCTS);
		replay(configuration);
		shopService.setConfiguration(configuration);
		// tested method call
		List<Product> newProducts = shopService.getNewProducts();

		assertSame(products, newProducts);
		EasyMock.verify(configuration);
		EasyMock.verify(productDao);
	}

	@Test
	public void testHideProduct() {
		Long productId = 3L;
		Product product = new Product();
		product.setVisible(true);
		when(productDao.findById(productId)).thenReturn(product);

		shopService.hideProduct(productId);
		assertFalse(product.isVisible());
	}

	@Test
	public void testUnhideProduct() {
		Long productId = 3L;
		Product product = new Product();
		product.setVisible(false);
		when(productDao.findById(productId)).thenReturn(product);

		shopService.unhideProduct(productId);
		assertTrue(product.isVisible());
	}

	@Test
	public void test_shouldAddNewsubcategory() {
		// given
		ShopService shopService = new ShopService();
		Category parent = new Category();
		parent.setId(123L);
		CategoryDao categoryDao = mock(CategoryDao.class);
		Category freshParent = new Category();
		when(categoryDao.findById(123L)).thenReturn(freshParent);
		shopService.setCategoryDao(categoryDao);

		// when
		Category category = shopService.createSubcategory(parent, "new subcategory");

		// then
		assertSame(category.getParent(), freshParent);
	}

	@Test
	public void test_shouldAddNewNewsletterRecipientForNewEmailAddress() {
		// given
		ShopService shopService = new ShopService();
		NewsletterRecipientDao newsletterRecipientDao = mock(NewsletterRecipientDao.class);
		shopService.setNewsletterRecipientDao(newsletterRecipientDao);

		EmailService emailService = mock(EmailService.class);

		shopService.setEmailService(emailService);
		// when
		shopService.registerNewsletterRecipient("recipient@netolution.pl", "eshop1");

		// then
		ArgumentCaptor<NewsletterRecipient> argument = new ArgumentCaptor<NewsletterRecipient>();
		verify(newsletterRecipientDao).makePersistent(argument.capture());

		NewsletterRecipient newsletterRecipient = argument.getValue();
		assertEquals(null, newsletterRecipient.getId());
		assertEquals("recipient@netolution.pl", newsletterRecipient.getEmail());
		assertEquals("eshop1", newsletterRecipient.getSource());
		assertEquals(36, newsletterRecipient.getToken().length());
		assertEquals(false, newsletterRecipient.isConfirmed());
		assertNull(newsletterRecipient.getConfirmed());
	}

	@Test
	public void test_shouldSendEmailToTheNewEmailAddress() {
		// given
		ShopService shopService = new ShopService();
		Configuration configuration = mock(Configuration.class);
		when(configuration.isDeveloperMode()).thenReturn(false);

		EmailService emailService = mock(EmailService.class);
		emailService.setConfiguration(configuration);
		shopService.setEmailService(emailService);

		shopService.setNewsletterRecipientDao(mock(NewsletterRecipientDao.class));

		// when
		shopService.registerNewsletterRecipient("recipient@netolution.pl", "eshop1");

		// then
		ArgumentCaptor<NewsletterRecipient> argument = new ArgumentCaptor<NewsletterRecipient>();
		verify(emailService).sendNewsletterConfirmationEmail(argument.capture());

		NewsletterRecipient newsletterRecipient = argument.getValue();
		assertNotNull(newsletterRecipient);

	}

	@Test
	public void test_shouldConfirmEmailForCorrectToken() {
		// given
		ShopService shopService = new ShopService();
		EmailService emailService = mock(EmailService.class);
		shopService.setEmailService(emailService);
		NewsletterRecipientDao newsletterRecipientDao = mock(NewsletterRecipientDao.class);
		NewsletterRecipient newsletterRecipient = new NewsletterRecipient();
		when(newsletterRecipientDao.findByEmailAndToken("recipient@netolution.pl", "1-2-3")).thenReturn(newsletterRecipient);
		shopService.setNewsletterRecipientDao(newsletterRecipientDao);

		// when
		shopService.confirmNewsletterRecipient("recipient@netolution.pl", "1-2-3");

		// then

		assertEquals(true, newsletterRecipient.isConfirmed());
		verify(emailService).sendNewsletterWelcomeEmail(newsletterRecipient);
	}

	@Test
	public void test_shouldReturnProductsCurrentlyOnSale() {
		// given
		ShopService shopService = new ShopService();
		ProductDao productDao = mock(ProductDao.class);
		shopService.setProductDao(productDao);
		List<Product> products = new ArrayList<Product>();
		when(productDao.getProductsOnSale()).thenReturn(products);

		// when

		List<Product> result = shopService.getProductsOnSale();

		// then

		assertSame(products, result);
	}

	@Test
	public void test_shouldSendContactMessages() {
		// given
		ShopService shopService = new ShopService();
		EmailService emailService = mock(EmailService.class);
		shopService.setEmailService(emailService);

		// when
		shopService.leaveContactMessage("roman@gowo.pl", "Witajcie. To wiadomość od klienta waszego jest!");

		// then
		verify(emailService).sendContactMessageToShopOwner("roman@gowo.pl", "Witajcie. To wiadomość od klienta waszego jest!");
	}

	@Test
	public void shouldReturnAllHitsWhenMaxHitsNumberConfigParameterSmallerThenActualNumber() {
		// given

		List<Product> products = new ArrayList<Product>();
		products.add(new Product());
		when(productDao.getHitProducts()).thenReturn(products);

		when(adminConfigurationDao.getMainConfiguration()).thenReturn(adminConfiguration);
		when(adminConfiguration.getMaxHitsNumber()).thenReturn(2);

		shopService.setAdminConfigurationDao(adminConfigurationDao);

		// when
		List<Product> result = shopService.getHitProducts();

		// then
		assertEquals(1, result.size());

	}

	@Test
	public void shouldReturnOnlyMaxHitsNumberAsConfigured() {
		// given

		List<Product> products = new ArrayList<Product>();
		products.add(new Product());
		products.add(new Product());
		when(productDao.getHitProducts()).thenReturn(products);

		when(adminConfigurationDao.getMainConfiguration()).thenReturn(adminConfiguration);
		when(adminConfiguration.getMaxHitsNumber()).thenReturn(1);

		shopService.setAdminConfigurationDao(adminConfigurationDao);

		// when
		List<Product> result = shopService.getHitProducts();

		// then
		assertEquals(1, result.size());
	}
}

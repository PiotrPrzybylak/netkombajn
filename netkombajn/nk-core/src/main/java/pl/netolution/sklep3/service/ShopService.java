package pl.netolution.sklep3.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import pl.netolution.sklep3.configuration.Configuration;
import pl.netolution.sklep3.dao.AdminConfigurationDao;
import pl.netolution.sklep3.dao.CategoryDao;
import pl.netolution.sklep3.dao.CustomerDao;
import pl.netolution.sklep3.dao.NewsletterRecipientDao;
import pl.netolution.sklep3.dao.OrderDao;
import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.dao.ShipmentOptionDao;
import pl.netolution.sklep3.domain.AdminConfiguration;
import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.domain.NewsletterRecipient;
import pl.netolution.sklep3.domain.Order;
import pl.netolution.sklep3.domain.OrderItem;
import pl.netolution.sklep3.domain.PaymentForm;
import pl.netolution.sklep3.domain.Price;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.ShipmentOption;
import pl.netolution.sklep3.domain.ShoppingCart;
import pl.netolution.sklep3.domain.payment.Payment.Status;

public class ShopService{

	private ProductDao productDao;

	private CategoryDao categoryDao;

	private Configuration configuration;

	private RandomService randomService;

	private EmailService emailService;

	private NewsletterRecipientDao newsletterRecipientDao;

	private AdminConfigurationDao adminConfigurationDao;

	private ShipmentOptionDao shipmentOptionDao;

	public void prepareOrderForSimpleCheckoutProcess(Order order) {

		order.getPayment().setForm(PaymentForm.CASH_ON_DELIVERY);
		order.getPayment().setStatus(Status.NEW);

		order.getRecipient().setShipmentAddress(null);

	}

	public Product getHitProduct() {
		List<Product> hits = productDao.getHitProducts();
		if (hits.isEmpty()) {
			return null;
		}
		return hits.get(randomService.getRandomNumber(hits.size()));
	}

	public List<Product> getHitProducts() {
		List<Product> hits = productDao.getHitProducts();
		//TODO zastanowić się cyz nie lepiej będzie ifa w DAO robic
		AdminConfiguration configuration = adminConfigurationDao.getMainConfiguration();
		if (!hitsNumberExceedsLimit(hits, configuration)) {
			return hits;
		} else {
			return hits.subList(0, configuration.getMaxHitsNumber());
		}
	}

	private boolean hitsNumberExceedsLimit(List<Product> hits, AdminConfiguration configuration) {
		return configuration.getMaxHitsNumber() != null && hits.size() > configuration.getMaxHitsNumber();
	}

	public List<ShipmentOption> getShipmentOptions(ShoppingCart cart) {
		List<ShipmentOption> shipmentOptions = shipmentOptionDao.getAll();
		List<ShipmentOption> inRangeOptions = getOptionsInPriceRange(cart.getTotalPrice(), shipmentOptions);
		List<ShipmentOption> availableShipmentOptions = getAvailableShipmentOptions(cart, inRangeOptions);

		return availableShipmentOptions;
	}

	private List<ShipmentOption> getAvailableShipmentOptions(ShoppingCart cart, List<ShipmentOption> inRangeOptions) {
		List<ShipmentOption> availableShipmentOptions = new ArrayList<ShipmentOption>();

		if (cart.isAvailableInstantly()) {
			return inRangeOptions;
		}
		for (ShipmentOption shipmentOption : inRangeOptions) {
			if (!shipmentOption.isAllowOnlyInstantProducts()) {
				availableShipmentOptions.add(shipmentOption);
			}
		}

		return availableShipmentOptions;
	}

	private List<ShipmentOption> getOptionsInPriceRange(Price totalShoppingPrice, List<ShipmentOption> shipmentOptions) {
		List<ShipmentOption> inRangeOptions = new ArrayList<ShipmentOption>();

		for (ShipmentOption shipmentOption : shipmentOptions) {
			if (shipmentOption.isInRange(totalShoppingPrice)) {
				inRangeOptions.add(shipmentOption);
			}
		}
		return inRangeOptions;
	}

	public void refreshProducts(ShoppingCart shoppingCart) {

		for (OrderItem orderItem : shoppingCart.getItems()) {
			productDao.refresh(orderItem.getProduct());
		}
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public List<Product> getNewProducts() {
		return productDao.getNewProducts(configuration.getMaxNewProducts());
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setRandomService(RandomService randomService) {
		this.randomService = randomService;
	}

	public void hideProduct(Long productId) {
		productDao.findById(productId).setVisible(false);
	}

	public void unhideProduct(Long productId) {
		productDao.findById(productId).setVisible(true);
	}

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	public void setCategoryDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	//TODO przenieść do serwisu odpowiedzialnego za kategorie
	public Category createSubcategory(Category parent, String name) {
		Category child = new Category();
		child.setName(name);
		if (parent != null) {
			Category freshParent = categoryDao.findById(parent.getId());
			freshParent.addChild(child);
		}

		categoryDao.makePersistent(child);

		return child;
	}

	//TODO przenieść do klasy odpowiedzialnej za rejestrowanie odbiorcow newslettera
	public void registerNewsletterRecipient(String email, String source) {
		NewsletterRecipient newsletterRecipient = new NewsletterRecipient();
		newsletterRecipient.setEmail(email);
		newsletterRecipient.setRegistered(new Date());
		newsletterRecipient.setToken(UUID.randomUUID().toString());
		newsletterRecipient.setSource(source);
		newsletterRecipientDao.makePersistent(newsletterRecipient);
		emailService.sendNewsletterConfirmationEmail(newsletterRecipient);

	}

	//TODO przenieść do klasy odpowiedzialnej za rejestrowanie odbiorcow newslettera
	public void setNewsletterRecipientDao(NewsletterRecipientDao newsletterRecipientDao) {
		this.newsletterRecipientDao = newsletterRecipientDao;
	}

	//TODO przenieść do klasy odpowiedzialnej za rejestrowanie odbiorcow newslettera
	public void confirmNewsletterRecipient(String email, String token) {
		NewsletterRecipient newsletterRecipient = newsletterRecipientDao.findByEmailAndToken(email, token);
		if (newsletterRecipient != null) {
			newsletterRecipient.setConfirmed(new Date());
			emailService.sendNewsletterWelcomeEmail(newsletterRecipient);
		}
	}

	public List<Product> getProductsOnSale() {
		return productDao.getProductsOnSale();
	}

	public void setAdminConfigurationDao(AdminConfigurationDao adminConfigurationDao) {
		this.adminConfigurationDao = adminConfigurationDao;
	}

	public void setShipmentOptionDao(ShipmentOptionDao shipmentOptionDao) {
		this.shipmentOptionDao = shipmentOptionDao;
	}

	public void leaveContactMessage(String email, String text) {
		emailService.sendContactMessageToShopOwner(email, text);
	}
}

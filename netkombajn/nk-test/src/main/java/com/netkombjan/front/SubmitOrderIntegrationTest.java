package com.netkombjan.front;

import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import pl.netolution.sklep3.configuration.Configuration;
import pl.netolution.sklep3.dao.AdminConfigurationDao;
import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.dao.SkuDao;
import pl.netolution.sklep3.dao.TextDao;
import pl.netolution.sklep3.domain.AdminConfiguration;
import pl.netolution.sklep3.domain.Order;
import pl.netolution.sklep3.domain.Price;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.Recipient;
import pl.netolution.sklep3.domain.SkuOrderItem;
import pl.netolution.sklep3.domain.StockKeepingUnit;
import pl.netolution.sklep3.domain.Text;


@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration({ "/applicationContext.xml", "/beans.xml"})
public class SubmitOrderIntegrationTest {
	
	@Autowired
	SubmitOrderService submitOrderService;
	
	@Autowired
	ProductDao productDao;
	
	@Autowired
	SkuDao skuDao;
	
	@Autowired
	AdminConfigurationDao adminConfigurationDao;
	
	@Autowired
	TextDao textDao;
	
	@Autowired
	Configuration configuration;
	
	@Test
	public void shouldSubmitOrder() {
		
		when(configuration.isDeveloperMode()).thenReturn(true);
		
		AdminConfiguration adminConfiguration = new AdminConfiguration();
		adminConfiguration.setEmail("test@netkombajn.com");
		adminConfigurationDao.makePersistent(adminConfiguration);
		
		Text text = new Text();
		text.setName("SIMPLE_ORDER");
		text.setText("trallala");
		textDao.makePersistent(text);
		
		Product product = new Product();
		productDao.makePersistent(product);
		
		StockKeepingUnit sku = new StockKeepingUnit();
		sku.setParentProduct(product);
		sku.setOriginalPrice(new Price(100));
		skuDao.makePersistent(sku);
		
		Order order = new Order();
		Recipient recipient = new Recipient();
		recipient.setEmail("test2@netkombajn.com");
		order.setRecipient(recipient);
		order.addSkuOderItem(new SkuOrderItem(sku, 1));
		submitOrderService.submitOrder(order);
	}
	

}

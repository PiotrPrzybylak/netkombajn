package pl.netolution.sklep3.web.jsf;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.mockito.Mockito.mock;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;

import com.netkombajn.store.domain.shared.price.Price;

import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.web.jsf.utils.MessageHelper;

public class ProductBackingBeanTest extends TestCase {

	public void testChooseProduct() throws Exception {

		ProductBackingBean productBackingBean = new ProductBackingBean();

		JsfRequestResolver jsfRequestResolver = createMock(JsfRequestResolver.class);
		expect(jsfRequestResolver.getParameter("productId")).andReturn("1");
		EasyMock.replay(jsfRequestResolver);
		productBackingBean.setJsfRequestResolver(jsfRequestResolver);

		ProductDao productDao = createMock(ProductDao.class);
		Product product = new Product();
		expect(productDao.findById(1L)).andReturn(product);
		EasyMock.replay(productDao);
		productBackingBean.setProductDao(productDao);

		productBackingBean.chooseProduct();

		assertSame(product, productBackingBean.getProduct());

		// Not testable for now - using FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().ge
		// productBackingBean.chooseProduct();
		EasyMock.verify(productDao);
		EasyMock.verify(jsfRequestResolver);

	}

	public void test_shouldAddFiveToPriceOfCoupleProducts() {
		//given
		ProductBackingBean backingBean = new ProductBackingBean();

		ProductDao productDao = mock(ProductDao.class);
		backingBean.setProductDao(productDao);

		Set<Product> productsGroup = createProductsSetForGroupEditTests();

		backingBean.setProductsForMassEdit(productsGroup);

		backingBean.setMassEditPriceAddition(new Price(3));

		MessageHelper messageHelper = mock(MessageHelper.class);
		backingBean.setMessageHelper(messageHelper);

		//when
		backingBean.editGroupPrice();

		//then
		for (Product product : backingBean.getProductsForMassEdit()) {
			assertEquals(new Price(13), product.getRetailGrossPrice());
		}
	}

	public void test_shouldSubstractFiveFromPriceOfCoupleProducts() {
		//given
		ProductBackingBean backingBean = new ProductBackingBean();

		ProductDao productDao = mock(ProductDao.class);
		backingBean.setProductDao(productDao);

		Set<Product> productsGroup = createProductsSetForGroupEditTests();

		backingBean.setProductsForMassEdit(productsGroup);

		backingBean.setMassEditPriceAddition(new Price(-3));

		MessageHelper messageHelper = mock(MessageHelper.class);
		backingBean.setMessageHelper(messageHelper);

		//when
		backingBean.editGroupPrice();

		//then
		for (Product product : backingBean.getProductsForMassEdit()) {
			assertEquals(new Price(7), product.getRetailGrossPrice());
		}
	}

	public void test_shouldNotChangeProductsPrice() {
		//given
		ProductBackingBean backingBean = new ProductBackingBean();

		ProductDao productDao = mock(ProductDao.class);
		backingBean.setProductDao(productDao);

		Set<Product> productsGroup = createProductsSetForGroupEditTests();

		backingBean.setProductsForMassEdit(productsGroup);

		backingBean.setMassEditPriceAddition(new Price(-7));

		MessageHelper messageHelper = mock(MessageHelper.class);
		backingBean.setMessageHelper(messageHelper);
		//when
		backingBean.editGroupPrice();

		//then
		for (Product product : backingBean.getProductsForMassEdit()) {
			assertEquals(new Price(10), product.getRetailGrossPrice());
		}
	}

	public void test_shouldSetNewWeightForProducts() {
		//given
		ProductBackingBean backingBean = new ProductBackingBean();

		ProductDao productDao = mock(ProductDao.class);
		backingBean.setProductDao(productDao);

		Set<Product> productsGroup = createProductsSetForGroupEditTests();

		backingBean.setProductsForMassEdit(productsGroup);

		Product template = new Product();
		template.setWeight(5.0);
		backingBean.setTemplateMassEditProduct(template);

		MessageHelper messageHelper = mock(MessageHelper.class);
		backingBean.setMessageHelper(messageHelper);
		//when
		backingBean.editGroupWeight();

		//then
		for (Product product : backingBean.getProductsForMassEdit()) {
			assertEquals(5.0, product.getWeight());
		}
	}

	private Set<Product> createProductsSetForGroupEditTests() {
		Set<Product> productsGroup = new HashSet<Product>();
		Product product = new Product();
		product.setRetailGrossPrice(new Price(10));
		product.setWholesaleNetPrice(new Price(4));
		product.setWeight(2.0);

		Product product2 = new Product();
		product2.setRetailGrossPrice(new Price(10));
		product2.setWholesaleNetPrice(new Price(4));
		product2.setWeight(2.0);

		productsGroup.add(product);
		productsGroup.add(product2);
		return productsGroup;
	}
}

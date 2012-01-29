package pl.netolution.sklep3.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.TransactionStatus;

import pl.netolution.sklep3.dao.CompositeProductDao;
import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.CompositeProduct;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.ProductGroup;

public class CompositeProductTest {

	private static final long THIRD_LIST_PRODUCT_ID = 14L;
	private static final long SECOND_LIST_PRODUCT_ID = 13L;
	private static final long FIRST_LIST_PRODUCT_ID = 12L;
	private static final long MAIN_PRODUCT_ID = 11L;
	private static CompositeProductDao compositeProductDao;
	private static ProductDao productDao;

	//TODO DBUNIT
	public static void main(String[] args) {

		ApplicationContext ap = new ClassPathXmlApplicationContext("applicationContext.xml");
		compositeProductDao = (CompositeProductDao) ap.getBean("compositeProductDao");
		productDao = (ProductDao) ap.getBean("productDao");
		//given
		CompositeProduct compositeProduct = createCompositeProduct();

		//when
		HibernateTransactionManager transactionManager = (HibernateTransactionManager) ap.getBean("transactionManager");
		TransactionStatus status = transactionManager.getTransaction(null);

		compositeProductDao.makePersistent(compositeProduct);
		transactionManager.commit(status);
		//then

		HibernateTransactionManager transactionManager2 = (HibernateTransactionManager) ap.getBean("transactionManager");
		TransactionStatus status2 = transactionManager.getTransaction(null);

		CompositeProduct compositeProduct2 = compositeProductDao.findById(compositeProduct.getId());
		System.out.println(compositeProduct2.getName());
		System.out.println(compositeProduct2.getProductGroups().size());

		for (ProductGroup group : compositeProduct2.getProductGroups()) {
			System.out.println(group.getName() + " " + group.getPriority());
			System.out.println(group.getPrimaryProduct().getId());

			for (Product product : group.getProducts()) {
				System.out.println(product.getId() + " " + product.getName());

			}
		}
		transactionManager2.commit(status2);
	}

	private static CompositeProduct createCompositeProduct() {
		CompositeProduct compositeProduct = new CompositeProduct();

		compositeProduct.setDescription("description composite product1");
		compositeProduct.setProfitPercent(10);
		compositeProduct.setName("composite product name");
		addGroupsToComposite(compositeProduct);

		return compositeProduct;
	}

	private static void addGroupsToComposite(CompositeProduct compositeProduct) {
		ProductGroup productGroup = createGroup("grupa1", 2);
		ProductGroup productGroup2 = createGroup("grupa2", 3);
		ProductGroup productGroup3 = createGroup("grupa3", 1);

		compositeProduct.addGroup(productGroup);
		compositeProduct.addGroup(productGroup2);
		compositeProduct.addGroup(productGroup3);
	}

	private static ProductGroup createGroup(String name, int priority) {
		ProductGroup productGroup = new ProductGroup();
		productGroup.setName(name);
		productGroup.setPriority(priority);
		Product product = productDao.findById(MAIN_PRODUCT_ID);

		addProducts(productGroup);

		productGroup.setPrimaryProduct(product);
		return productGroup;
	}

	private static void addProducts(ProductGroup productGroup) {
		Product product1 = productDao.findById(FIRST_LIST_PRODUCT_ID);
		Product product2 = productDao.findById(SECOND_LIST_PRODUCT_ID);
		Product product3 = productDao.findById(THIRD_LIST_PRODUCT_ID);

		productGroup.addProduct(product1);
		productGroup.addProduct(product2);
		productGroup.addProduct(product3);
	}
}

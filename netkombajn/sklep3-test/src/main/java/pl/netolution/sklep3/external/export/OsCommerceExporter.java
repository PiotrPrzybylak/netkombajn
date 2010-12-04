package pl.netolution.sklep3.external.export;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.domain.Manufacturer;
import pl.netolution.sklep3.domain.Price;
import pl.netolution.sklep3.domain.Product;

public class OsCommerceExporter extends JdbcDaoSupport {

	private String pictureBaseUrl;

	private final static RowMapper categoryMapper = new RowMapper() {

		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			Category category = new Category();
			category.setName(rs.getString("categories_name"));
			category.setId(rs.getLong("categories_id"));
			Category parent = new Category();
			parent.setId(rs.getLong("parent_id"));
			category.setParent(parent);
			return category;
		}

	};

	public static void main(String[] args) {
		ApplicationContext ap = new ClassPathXmlApplicationContext("OsCommerceTestContext.xml");
		OsCommerceExporter osCommerceExporter = ap.getBean(OsCommerceExporter.class);
		//		for (Product product : towarDao.getProducts()) {
		//			System.out.println(product.getName());
		//		}

		//		for (Category category : towarDao.getCategories()) {
		//			System.out.println("* " + category.getName());
		//			showSubcategories(towarDao, category, "-");
		//		}

		XmlExporter xmlExporter = new XmlExporter();
		xmlExporter.generateXmlWithCategories(osCommerceExporter.getAllCategories(), "target/kategorie_oscommerce.xml");
		xmlExporter.generateXmlWithProducts(osCommerceExporter.getProducts(), "target/towary_oscommerce.xml");

		System.out.println("KONIEC");
	}

	private List<Product> getProducts() {

		List<Product> products = getJdbcTemplate().query(
				"SELECT * FROM products p, products_description d WHERE p.products_id = d.products_id", new RowMapper() {

			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Product product = new Product();
				product.setId(rs.getLong("products_id"));
				product.setName(rs.getString("products_name"));
				product.setCatalogNumber(rs.getString("products_model"));
				product.setDescription(rs.getString("products_description"));
				product.setRetailGrossPrice(new Price(rs.getBigDecimal("products_price")));
				product.setExternalPictureUrl(pictureBaseUrl + rs.getString("products_image"));
				product.setManufacturer(new Manufacturer("testowy producent"));
				return product;

			}

		});

		for (Product product : products) {
			List<Category> categories = getCategories(product);
			product.setCategory(categories.get(0));

		}

		return products;
	}

	private List<Category> getCategories(Product product) {
		return getJdbcTemplate()
				.query(
						"SELECT * FROM categories c, categories_description d, products_to_categories p WHERE c.categories_id = d.categories_id AND p.categories_id = c.categories_id AND p.products_id = ?",
						new Object[] { product.getId() }, categoryMapper);

	}

	private List<Category> getAllCategories() {
		return getJdbcTemplate().query("SELECT * FROM categories c, categories_description d WHERE c.categories_id = d.categories_id ",
				categoryMapper);

	}

	public void setPictureBaseUrl(String pictureBaseUrl) {
		this.pictureBaseUrl = pictureBaseUrl;
	}

}

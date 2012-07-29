package pl.netolution.sklep3.test.optima;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.netkombajn.store.domain.shared.price.Price;

import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.external.export.XmlExporter;

public class TowarDao extends JdbcDaoSupport {

	private static final String TWR_TWR_ID = "Twr_TwrId";

	private static final Logger log = Logger.getLogger(TowarDao.class);

	private final static RowMapper categoryMapper = new RowMapper() {

		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			Category category = new Category();
			category.setName(rs.getString("TwG_Nazwa"));
			category.setId(rs.getLong("TwG_GIDNumer"));
			Category parent = new Category();
			parent.setId(rs.getLong("TwG_GrONumer"));
			category.setParent(parent);
			return category;
		}

	};

	@SuppressWarnings("unchecked")
	public List<Product> getProducts() {
		List<Product> products = getJdbcTemplate().query("SELECT * FROM CDN.Towary", new RowMapper() {

			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Product product = new Product();
				product.setId(rs.getLong(TWR_TWR_ID));
				product.setName(rs.getString("Twr_Nazwa"));
				Category category = new Category();
				category.setId(rs.getLong("Twr_TwGGIDNumer"));
				product.setCategory(category);
				product.setCatalogNumber(rs.getString("Twr_Kod"));
				product.setDescription(rs.getString("Twr_Opis"));
				return product;
			}

		});

		for (Product product : products) {
			List<Map<String, Object>> prices = getPrices(product);
			for (Map<String, Object> cena : prices) {
				log.debug("cena: " + cena);
			}
			product.setRetailGrossPrice(new Price((BigDecimal) prices.get(0).get("TwC_Wartosc")));
		}

		return products;

	}

	public static void main(String[] args) {
		ApplicationContext ap = new ClassPathXmlApplicationContext("OptimaTestContext.xml");
		TowarDao towarDao = (TowarDao) ap.getBean("towarDao");
		//		for (Product product : towarDao.getProducts()) {
		//			System.out.println(product.getName());
		//		}

		//		for (Category category : towarDao.getCategories()) {
		//			System.out.println("* " + category.getName());
		//			showSubcategories(towarDao, category, "-");
		//		}

		XmlExporter xmlExporter = new XmlExporter();
		xmlExporter.generateXmlWithCategories(towarDao.getAllCategories(), "kategorie_optima.xml");
		xmlExporter.generateXmlWithProducts(towarDao.getProducts(), "towary_optima.xml");
	}

	private static void showSubcategories(TowarDao towarDao, Category category, String prefix) {
		prefix += "-";
		for (Category subcategory : towarDao.getCategories(category)) {
			System.out.println(prefix + subcategory.getName());
			showSubcategories(towarDao, subcategory, prefix);
		}
	}

	public List<Category> getCategories() {
		List<Category> categories = getJdbcTemplate().query("SELECT * FROM CDN.TwrGrupy WHERE TwG_GrONumer = 0 order by TwG_Nazwa ",
				categoryMapper);

		return categories;
	}

	public List<Category> getAllCategories() {
		List<Category> categories = getJdbcTemplate().query("SELECT * FROM CDN.TwrGrupy WHERE TwG_GIDTyp = -16", categoryMapper);

		return categories;
	}

	public List<Category> getCategories(Category category) {
		List<Category> categories = getJdbcTemplate().query(
				"SELECT * FROM CDN.TwrGrupy WHERE TwG_GIDTyp = -16  AND TwG_GrONumer = ? order by TwG_Nazwa ",
				new Object[] { category.getId() }, categoryMapper);

		return categories;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrices(Product product) {
		return getJdbcTemplate().query("SELECT * FROM CDN.TwrCeny where TwC_TwrID = ?", new Object[] { product.getId() }, new RowMapper() {

			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					map.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
				}
				return map;
			}
		});
	}
}

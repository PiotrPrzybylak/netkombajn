package pl.netolution.sklep3.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.netkombajn.eshop.product.imports.CategoriesInfo;
import com.netkombajn.eshop.product.imports.IncomImportCategoriesService;
import com.netkombajn.eshop.product.imports.IncomImportService;


public class GetCategories {

	public Document parse(URL url) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(url);
		return document;
	}

	public static void main(String[] args) throws MalformedURLException, DocumentException {
		String cennik = "file:grupy_incom_20090613.xml";
		Document document = new GetCategories().parse(new URL(cennik));

		CategoriesInfo categoriesInfo = new CategoriesInfo(document);

		System.out.println(categoriesInfo.getCategories());
		List<String> roots = categoriesInfo.getCategories().get("");
		System.out.println("Root categories: " + roots);
		showCategory("", categoriesInfo.getCategories(), "", categoriesInfo.getNames());

		ApplicationContext ap = new ClassPathXmlApplicationContext("applicationContext.xml");
		IncomImportCategoriesService incomImportCategoriesService = ap.getBean(IncomImportCategoriesService.class);
		incomImportCategoriesService.mergeImportCategories(categoriesInfo.getCategories(), categoriesInfo.getNames());

	}

	public static void showCategory(String cat, Map<String, List<String>> cats, String tab, Map<String, String> names) {
		System.out.print(tab + cat + " " + names.get(cat));
		if (cats.get(cat) != null) {
			System.out.println();
			for (String kid : cats.get(cat)) {
				showCategory(kid, cats, tab + "-", names);
			}
		} else {
			System.out.println(" ~");
		}
	}
}

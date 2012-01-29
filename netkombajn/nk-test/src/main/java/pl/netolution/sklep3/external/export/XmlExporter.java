package pl.netolution.sklep3.external.export;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.apache.log4j.Logger;

import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.domain.Product;

public class XmlExporter {

	private static final Logger log = Logger.getLogger(XmlExporter.class);

	public void generateXmlWithProducts(List<Product> products, String name) {
		log.debug("Przetwarzam listę towarów: " + products.size());
		try {
			PrintStream os = new PrintStream(new FileOutputStream(name));
			try {
				os.println("<xml>");

				for (Product product : products) {
					Category category = product.getCategory();
					if (0 == category.getId()) {
						log.debug("Ignoruje towar z rootowej kategorii:" + product.getCatalogNumber());
						continue;
					}
					log.debug("Zapisuje towar:" + product.getCatalogNumber());

					os.println(" <produkt>");
					os.println("<grupa_towarowa>" + category.getId() + "</grupa_towarowa>");
					os.println("<symbol_produktu>" + product.getCatalogNumber() + "</symbol_produktu>");
					os.println("<nazwa_produktu>" + escapeXmlSpecialCharacters(product.getName()) + "</nazwa_produktu>");
					os.println("<opis_produktu>" + escapeXmlSpecialCharacters(product.getDescription()) + "</opis_produktu>");
					os.print("<cena>");
					os.printf("%f", product.getRetailGrossPrice().getValue());
					os.println("</cena>");
					os.println("<stan_magazynowy>0</stan_magazynowy>");

					if (product.getManufacturer() != null) {
						os.println("<nazwa_producenta>" + escapeXmlSpecialCharacters(product.getManufacturer().getName())
								+ "</nazwa_producenta>");
					}

					if (product.getExternalPictureUrl() != null) {
						os.println("<link_do_zdjecia_produktu>" + escapeXmlSpecialCharacters(product.getExternalPictureUrl())
								+ "</link_do_zdjecia_produktu>");
					}

					os.println(" </produkt>");
				}

				os.println("</xml>");
			} finally {
				os.close();
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void generateXmlWithCategories(List<Category> categories, String name) {
		try {
			PrintStream os = new PrintStream(new FileOutputStream(name), false, "UTF8");
			try {
				os.println("<xml>");

				for (Category category : categories) {
					if (category.getId() == 0) {
						continue;
					}
					os.println(" <grupy>");
					os.println("<id>" + category.getId() + "</id>");
					if (category.getParent().getId() != 0) {
						os.println("<idh>" + category.getParent().getId() + "</idh>");
					} else {
						os.println("<idh />");
					}

					os.println("<name>" + category.getName() + "</name>");
					os.println(" </grupy>");
				}

				os.println("</xml>");
			} finally {
				os.close();
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String escapeXmlSpecialCharacters(String stringToEscape) {
		return stringToEscape.replace("<", "&lt;").replace(">", "&gt;").replace("&", "&amp;").replace("\u001e", "").replace("\u0017", "")
				.replace("\u001d", "");

		//return "<![CDATA[" + stringToEscape + "]]>";
	}

}

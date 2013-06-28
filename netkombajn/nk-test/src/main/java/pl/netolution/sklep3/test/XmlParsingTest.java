package pl.netolution.sklep3.test;

import java.net.MalformedURLException;
import java.net.URL;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pl.netolution.sklep3.service.imports.ImportStatus;
import pl.netolution.sklep3.service.imports.IncomImportService;

public class XmlParsingTest {

	public Document parse(URL url) throws DocumentException {
		SAXReader reader = new SAXReader();

		System.out.println(Runtime.getRuntime().freeMemory());
		System.out.println(Runtime.getRuntime().totalMemory());
		System.out.println(Runtime.getRuntime().maxMemory());
		Document document = reader.read(url);
		System.out.println(Runtime.getRuntime().freeMemory());
		System.out.println(Runtime.getRuntime().totalMemory());
		System.out.println(Runtime.getRuntime().maxMemory());
		System.out.println();
		return document;
	}

	public static void main(String[] args) throws MalformedURLException, DocumentException {
		String cennik = "file:src/main/resources/cennik_maly.xml";

		ApplicationContext ap = new ClassPathXmlApplicationContext("applicationContext.xml");
		IncomImportService importService = (IncomImportService) ap.getBean("incomImportService");

		Document document = new XmlParsingTest().parse(new URL(cennik));
		importService.importProducts(document, new ImportStatus());
	}
}

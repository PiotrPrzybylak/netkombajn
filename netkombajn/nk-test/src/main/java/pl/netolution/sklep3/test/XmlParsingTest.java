package pl.netolution.sklep3.test;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pl.netolution.sklep3.service.imports.ImportStatus;
import pl.netolution.sklep3.service.imports.IncomImportService;
import pl.netolution.sklep3.service.imports.IncomProductXmlParser;

import java.net.MalformedURLException;
import java.net.URL;

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
        Document document = new XmlParsingTest().parse(new URL(cennik));

        ApplicationContext ap = new ClassPathXmlApplicationContext("applicationContext.xml");
        IncomImportService importService = (IncomImportService) ap.getBean("incomImportService");

        final IncomProductXmlParser incomProductXmlParser = new IncomProductXmlParser();
        importService.importProducts(incomProductXmlParser.convertXmlToListOfMaps(document), new ImportStatus());
	}
}

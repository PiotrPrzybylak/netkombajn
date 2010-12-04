package pl.netolution.sklep3.test;

import java.io.StringReader;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.ws.client.core.WebServiceTemplate;

public class WebserviceTest {
	private static final String MESSAGE = "<GetCitiesByCountry xmlns=\"http://www.webserviceX.NET\"><CountryName>Poland</CountryName></GetCitiesByCountry>";

	public static void main(String[] args) {
		WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

		StreamSource source = new StreamSource(new StringReader(MESSAGE));
		StreamResult result = new StreamResult(System.out);
		webServiceTemplate.sendSourceAndReceiveToResult("http://www.webservicex.net/geoipservice.asmx", source, result);

	}

}

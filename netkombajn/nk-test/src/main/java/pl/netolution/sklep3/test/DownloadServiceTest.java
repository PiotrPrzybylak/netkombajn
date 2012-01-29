package pl.netolution.sklep3.test;

import java.io.IOException;
import java.net.URL;

import junit.framework.TestCase;
import pl.netolution.sklep3.service.imports.DownloadService;

public class DownloadServiceTest extends TestCase {

	public void test_shouldDownloadPictureByUrl() throws IOException {

		//given
		DownloadService downloadService = new DownloadService();
		//when 
		byte[] data = downloadService.downloadFile(new URL("http://powercomputer.netolution.pl/layout/img/powercomputer_logo.jpg"));

		//then

		assertEquals(82002, data.length);
	}

}

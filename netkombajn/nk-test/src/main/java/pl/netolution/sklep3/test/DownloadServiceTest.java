package pl.netolution.sklep3.test;

import java.io.IOException;
import java.net.URL;

import com.netkombajn.eshop.product.imports.DownloadService;

import junit.framework.TestCase;

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

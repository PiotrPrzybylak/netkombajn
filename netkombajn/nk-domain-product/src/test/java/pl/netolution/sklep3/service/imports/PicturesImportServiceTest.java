package pl.netolution.sklep3.service.imports;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import junit.framework.TestCase;

import org.mockito.Mockito;

import com.netkombajn.eshop.product.imports.DownloadService;
import com.netkombajn.eshop.product.imports.ImportStatus;
import com.netkombajn.eshop.product.imports.PicturesImportService;

import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.service.PictureManager;

public class PicturesImportServiceTest extends TestCase {

	public void test_shouldDownloadPicturesForProductsUsingExternalUrlsForPictures() throws MalformedURLException {
		// given
		DownloadService downloadService = mock(DownloadService.class);
		PictureManager pictureManager = mock(PictureManager.class);

		byte[] pictureData = new byte[] { 1, 2, 3 };

		when(downloadService.downloadFile(new URL("http://powercomputer.netolution.pl/layout/img/powercomputer_logo.jpg"))).thenReturn(
				pictureData);

		PicturesImportService picturesImportService = new PicturesImportService();

		final Product product = new Product();
		product.setExternalPictureUrl("http://powercomputer.netolution.pl/layout/img/powercomputer_logo.jpg");
		ProductDao productDao = Mockito.mock(ProductDao.class);
		when(productDao.getProductsWithExteralPicture()).thenReturn(Arrays.asList(new Product[] { product }));
		picturesImportService.setProductDao(productDao);
		picturesImportService.setDownloadService(downloadService);
		picturesImportService.setPictureManager(pictureManager);

		// when
		picturesImportService.importAllMissingPictures(new ImportStatus());

		// then
		verify(pictureManager).saveProductPicture(same(product), eq(pictureData));

	}

}

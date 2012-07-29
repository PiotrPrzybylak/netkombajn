package pl.netolution.sklep3.configuration;

import java.util.List;
import java.util.Map;

import pl.netolution.sklep3.domain.ImageFormat;

// TODO remove ?
public interface Configuration extends 
pl.netolution.sklep3.service.migration.PictureStructureTransformer.Configuration,
pl.netolution.sklep3.service.EmailService.Configuration,
pl.netolution.sklep3.service.PictureManager.Configuration,
pl.netolution.sklep3.service.ProductService.Configuration,
pl.netolution.sklep3.utils.mail.NetKombajnMailSender.Configuration {

	void init();

	String getPicturesUploadFolder();

	String getLayoutPrefix();

	int getMaxNewProducts();

	String getImportsUploadFolder();

	boolean isDeveloperMode();

	String getAnalyticsCode();

	String getApplicationURL();

	ImageFormat getImageFormatByName(String name);

	List<ImageFormat> getImageFormats();

	String getGeneratedPicturesFolder();

	boolean isTestPaymentEnabled();

	String getCustomStyle();

	String getRevisionInfo();

	String getShopName();

	String getExternalCssFileUrl();

	String getIndexPath();

	Map<String, String> getDynamicConfig();
}

package pl.netolution.sklep3.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import pl.netolution.sklep3.dao.ImageFormatDao;
import pl.netolution.sklep3.domain.ImageFormat;
import pl.netolution.sklep3.domain.enums.ImageFormatName;

public class JdbcConfiguration extends JdbcDaoSupport implements Configuration {

	private boolean testPaymentEnabled;

	private String picturesUploadFolder;

	private String layoutPrefix;

	private int maxNewProducts;

	private String importsUploadFolder;

	private boolean developerMode;

	private String analyticsCode;

	private String applicationURL;

	private ImageFormatDao imageFormatDao;

	private String generatedPicturesFolder;

	private String customStyle;

	private String shopName;

	private String externalCssFileUrl;

	private String indexPath;

	private String checkout;

	private Map<String, String> dynamicConfiguration = new HashMap<String, String>();

	public void loadDynamicConfiguration() {
		SqlRowSet rowSet = getJdbcTemplate().queryForRowSet("SELECT * FROM dynamic_configuration ");
		while (rowSet.next()) {
			dynamicConfiguration.put(rowSet.getString("name"), rowSet.getString("value"));
		}
	}

	public void init() {
		Map<String, Object> configuration = getJdbcTemplate().queryForMap("SELECT * FROM configuration WHERE id = 1");
		picturesUploadFolder = (String) configuration.get("picturesUploadFolder");
		layoutPrefix = (String) configuration.get("layoutPrefix");
		maxNewProducts = ((Long) configuration.get("maxNewProducts")).intValue();
		importsUploadFolder = (String) configuration.get("importsUploadFolder");
		generatedPicturesFolder = (String) configuration.get("generatedPicturesFolder");
		analyticsCode = (String) configuration.get("analyticsCode");
		applicationURL = (String) configuration.get("applicationURL");
		customStyle = (String) configuration.get("customStyle");
		shopName = (String) configuration.get("shopName");
		externalCssFileUrl = (String) configuration.get("externalCssFileUrl");
		indexPath = (String) configuration.get("indexPath");
		checkout = (String) configuration.get("checkout");

		if (configuration.get("testPaymentEnabled") != null) {
			testPaymentEnabled = (Boolean) configuration.get("testPaymentEnabled");
		}

		if (configuration.get("developerMode") != null) {
			developerMode = (Boolean) configuration.get("developerMode");
		}
		loadDynamicConfiguration();
	}

	public List<ImageFormat> getImageFormats() {
		return imageFormatDao.getAll();
	}

	public String getPicturesUploadFolder() {
		return picturesUploadFolder;
	}

	public String getLayoutPrefix() {
		return layoutPrefix;
	}

	public int getMaxNewProducts() {
		return maxNewProducts;
	}

	public String getImportsUploadFolder() {
		return importsUploadFolder;
	}

	public boolean isDeveloperMode() {
		return developerMode;
	}

	public String getAnalyticsCode() {
		return analyticsCode;
	}

	public String getApplicationURL() {
		return applicationURL;
	}

	public ImageFormat getImageFormatByName(String name) {

		return imageFormatDao.getByName(ImageFormatName.valueOf(name));
	}

	public void setImageFormatDao(ImageFormatDao imageFormatDao) {
		this.imageFormatDao = imageFormatDao;
	}

	public String getGeneratedPicturesFolder() {
		return generatedPicturesFolder;
	}

	public boolean isTestPaymentEnabled() {
		return testPaymentEnabled;
	}

	public String getCustomStyle() {
		return customStyle;
	}

	public String getRevisionInfo() {
		InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("revision.txt");

		if (resourceAsStream == null) {
			return "";
		}

		StringBuilder stringBuilder = new StringBuilder();
		int c;
		try {
			while ((c = resourceAsStream.read()) != -1) {
				stringBuilder.append((char) c);
			}
		} catch (IOException e) {
			throw new RuntimeException("Problem with reading revision info", e);
		}

		return stringBuilder.toString();
	}

	public String getShopName() {
		return shopName;
	}

	public String getExternalCssFileUrl() {
		return externalCssFileUrl;
	}

	public String getIndexPath() {
		return indexPath;
	}

	public Map<String, String> getDynamicConfig() {
		return dynamicConfiguration;
	}

	public String getCheckout() {
		return checkout;
	}

}

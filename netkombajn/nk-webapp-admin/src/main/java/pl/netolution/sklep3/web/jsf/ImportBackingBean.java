package pl.netolution.sklep3.web.jsf;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import pl.netolution.sklep3.configuration.Configuration;
import pl.netolution.sklep3.dao.ImportDao;
import pl.netolution.sklep3.domain.Import;
import pl.netolution.sklep3.service.imports.CategoriesInfo;
import pl.netolution.sklep3.service.imports.ImportStatus;
import pl.netolution.sklep3.service.imports.IncomImportService;

public class ImportBackingBean {

	private static final Logger log = Logger.getLogger(ImportBackingBean.class);

	private UploadedFile categoryUploadedFile;

	private UploadedFile productUploadedFile;

	private IncomImportService incomImportService;

	private String categoryMessage;

	private ImportDao importDao;

	private Configuration configuration;

	private GlobalImportBackingBean globalImportBackingBean;

	private Import currentImport;

	public String importProductFile() throws IOException {
		// SAXReader reader = new SAXReader();
		//
		// log.debug("Document uploaded. Starting parse process.");
		// log.debug("free : "+Runtime.getRuntime().freeMemory());
		// log.debug("total : "+Runtime.getRuntime().totalMemory());
		// log.debug("max : "+Runtime.getRuntime().maxMemory());
		// Document document = reader.read(productUploadedFile.getInputStream());
		// log.debug("Document with products has bean read");
		// log.debug("free end : "+Runtime.getRuntime().freeMemory());
		// log.debug("total end: "+Runtime.getRuntime().totalMemory());
		// log.debug("max end: "+Runtime.getRuntime().maxMemory());
		// incomImportService.importProducts(document);
		//
		// productMessage = "Produkty zaimportowano poprawnie.";

		currentImport = new Import();
		currentImport.setFileName(productUploadedFile.getName());
		String user = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
		currentImport.setUser(user);
		currentImport.setStatus("UPLOAD_START");
		// TODO use same date as for lastUpdate
		currentImport.setTimestamp(new Date());
		importDao.makePersistent(currentImport);

		OutputStream os = new FileOutputStream(getCurrentImportFile());
		try {
			os.write(productUploadedFile.getBytes());
		} finally {
			os.close();
		}

		currentImport.setStatus("UPLOAD_FINISHED");
		importDao.makePersistent(currentImport);

		startImportingProducts();

		return "importStatus";
	}

	private File getCurrentImportFile() {
		return new File(configuration.getImportsUploadFolder(), "import_" + currentImport.getId());
	}

	public void importCategoryFile(ActionEvent event) throws Exception {

		SAXReader reader = new SAXReader();

		Document document = reader.read(categoryUploadedFile.getInputStream());
		CategoriesInfo categoriesInfo = new CategoriesInfo(document);

		incomImportService.mergeImportCategories(categoriesInfo.getCategories(), categoriesInfo.getNames());

		categoryMessage = "Kategorie zaimportowano poprawnie.";
	}

	// public void startImportingProducts(ActionEvent actionEvent) {
	// startImportingProducts();
	// }

	private void startImportingProducts() {
		globalImportBackingBean.setImportInProgress(true);
		currentImport.setStatus("IMPORT_START");
		importDao.makePersistent(currentImport);
		new Thread() {

			@Override
			public void run() {
				long startTime = System.currentTimeMillis();
				log.debug("Running Thread: " + this.getName());
				try {
					SAXReader reader = new SAXReader();
					InputStream is = new BufferedInputStream(new FileInputStream(getCurrentImportFile()));
					Document document = reader.read(is);
					// productMessage = "Importing " + document.getRootElement().elements().size() + " products";
					globalImportBackingBean.setImportStatus(new ImportStatus());
					globalImportBackingBean.getImportStatus().setTotalElements(document.getRootElement().elements().size());
					incomImportService.importProducts(document, globalImportBackingBean.getImportStatus());

					currentImport.setStatus("IMPORT_FINISHED");
					currentImport.setDuration(System.currentTimeMillis() - startTime);
					importDao.makePersistent(currentImport);
				} catch (Exception e) {
					log.fatal("error while importing products file", e);
					globalImportBackingBean.getImportStatus().addError("error while importing products file", e);
					throw new RuntimeException(e);
				} finally {
					// TODO check if hasErrors and throw exceptions - not because of mail errors
					globalImportBackingBean.setImportInProgress(false);
				}

			}

		}.start();

	}

	public UploadedFile getCategoryUploadedFile() {
		return categoryUploadedFile;
	}

	public void setCategoryUploadedFile(UploadedFile uploadedFile) {
		this.categoryUploadedFile = uploadedFile;
	}

	public UploadedFile getProductUploadedFile() {
		return productUploadedFile;
	}

	public void setProductUploadedFile(UploadedFile productUploadedFile) {
		this.productUploadedFile = productUploadedFile;
	}

	public String getCategoryMessage() {
		return categoryMessage;
	}

	public IncomImportService getIncomImportService() {
		return incomImportService;
	}

	public void setIncomImportService(IncomImportService incomImportService) {
		this.incomImportService = incomImportService;
	}

	public void setCategoryMessage(String message) {
		this.categoryMessage = message;
	}

	public void setImportDao(ImportDao importDao) {
		this.importDao = importDao;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public boolean isImportInProgress() {
		return globalImportBackingBean.isImportInProgress();
	}

	public void setGlobalImportBackingBean(GlobalImportBackingBean globalImportBackingBean) {
		this.globalImportBackingBean = globalImportBackingBean;
	}

}
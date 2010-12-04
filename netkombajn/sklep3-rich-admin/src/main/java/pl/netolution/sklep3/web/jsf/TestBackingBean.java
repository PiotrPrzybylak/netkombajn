package pl.netolution.sklep3.web.jsf;

import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import pl.netolution.sklep3.service.migration.CategoriesMigrationService;
import pl.netolution.sklep3.service.migration.DefaultSkuGenerator;

public class TestBackingBean extends ProductBackingBase {

	private static final Logger log = Logger.getLogger(TestBackingBean.class);

	boolean testButtonVisible;

	private DefaultSkuGenerator defaultSkuGenerator;

	private CategoriesMigrationService categoriesMigrationService;

	public void testAction() {
		throw new RuntimeException("TEST ACTION");
	}

	public void generateDefaultSkus() {
		defaultSkuGenerator.generateDefaultSkus();
	}

	public void testActionListener(ActionEvent actionEvent) {
		log.debug("Test Action Listener");
		throw new RuntimeException("TEST ACTION  LISTENER");
	}

	public void hideButton() {
		testButtonVisible = false;
	}

	public void showButton() {
		testButtonVisible = true;
	}

	public void testButton() {
		log.debug("Test Button testButtonVisible: " + testButtonVisible);
		System.out.println("a dupa !!");
	}

	public void migrateToLinkedCategories() {
		categoriesMigrationService.addMainCategoriesToLinkedCategories();
	}

	public boolean isTestButtonVisible() {
		return testButtonVisible;
	}

	public void setDefaultSkuGenerator(DefaultSkuGenerator defaultSkuGenerator) {
		this.defaultSkuGenerator = defaultSkuGenerator;
	}

	public void setCategoriesMigrationService(CategoriesMigrationService categoriesMigrationService) {
		this.categoriesMigrationService = categoriesMigrationService;
	}

	public void generateRuntimeException() {
		throw new RuntimeException("Test runtime Exception");
	}

	public void generateCheckedException() throws Exception {
		throw new Exception("Test checked Exception");
	}

	public String getFieldRuntimeException() {
		throw new RuntimeException("Test runtime Exception");
	}

}

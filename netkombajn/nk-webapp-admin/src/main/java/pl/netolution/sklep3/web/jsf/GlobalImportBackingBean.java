package pl.netolution.sklep3.web.jsf;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.model.BoundedRangeModel;

import pl.netolution.sklep3.domain.Import;
import pl.netolution.sklep3.service.imports.ImportStatus;
import pl.netolution.sklep3.service.imports.PicturesImportService;

public class GlobalImportBackingBean {

	private static final Logger log = Logger.getLogger(GlobalImportBackingBean.class);

	private boolean importInProgress;

	private Import currentImport;

	private ImportStatus importStatus;

	private ImportStatus picturesImportStatus;

	private final org.apache.myfaces.trinidad.model.BoundedRangeModel boundedRangeModel = new BoundedRangeModel() {

		@Override
		public long getMaximum() {
			return importStatus.getTotalElements();
		}

		@Override
		public long getValue() {
			return importStatus.getProcesseedElements();
		}

	};

	private PicturesImportService pictureImportService;

	public boolean isImportInProgress() {
		return importInProgress;
	}

	public void setImportInProgress(boolean importInProgress) {
		this.importInProgress = importInProgress;
	}

	public Import getCurrentImport() {
		return currentImport;
	}

	public void setCurrentImport(Import currentImport) {
		this.currentImport = currentImport;
	}

	public ImportStatus getImportStatus() {
		return importStatus;
	}

	public void setImportStatus(ImportStatus importStatus) {
		this.importStatus = importStatus;
	}

	public org.apache.myfaces.trinidad.model.BoundedRangeModel getProgressModel() {
		return boundedRangeModel;
	}

	public String resetImportStatus() {
		importInProgress = false;
		return null;
	}

	public String importPictures() {
		importInProgress = true;
		new Thread() {

			@Override
			public void run() {
				long startTime = System.currentTimeMillis();
				log.debug("Running Thread: " + this.getName());
				try {

					picturesImportStatus = new ImportStatus();
					// picturesImportStatus.setTotalElements(document.getRootElement().elements().size());
					pictureImportService.importAllMissingPictures(picturesImportStatus);

					log.debug("Importing pictures finished. Time :" + (System.currentTimeMillis() - startTime));

				} catch (Exception e) {
					// TODO catch Throwable ??
					log.fatal("error while importing products pictures", e);
					picturesImportStatus.addError("error while importing products file", e);
					throw new RuntimeException(e);

				} finally {
					// TODO check if hasErrors and throw exceptions - not because of mail errors
					setImportInProgress(false);
				}

				// if (!picturesImportStatus.getHasErrors()) {
				// setImportInProgress(false);
				// }

			}

		}.start();
		return null;
	}

	public ImportStatus getPicturesImportStatus() {
		return picturesImportStatus;
	}

	public void setPictureImportService(PicturesImportService pictureImportService) {
		this.pictureImportService = pictureImportService;
	}

}

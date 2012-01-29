package pl.netolution.sklep3.service.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

public class FileManager {

	private static final Logger log = Logger.getLogger(FileManager.class);

	public void saveFile(File file, byte[] data) {
		OutputStream os;

		try {
			os = new BufferedOutputStream(new FileOutputStream(file));
		} catch (FileNotFoundException e1) {
			log.error("Canot open file", e1);
			throw new RuntimeException(e1);
		}

		try {
			os.write(data);
			os.close();

		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				log.error("Canot close file", e);
			}

		}
	}

	private byte[] getDataFromFile(File uploadedFile) throws FileNotFoundException, IOException {
		InputStream inputStream = new FileInputStream(uploadedFile);
		byte[] data = new byte[inputStream.available()];
		inputStream.read(data);
		return data;
	}

	public void readFileToStream(File file, OutputStream os) throws FileNotFoundException, IOException {
		log.debug("Reading file to stream: " + file.getPath());

		InputStream is = new FileInputStream(file);

		try {
			int i;
			while ((i = is.read()) != -1) {
				os.write(i);
			}
		} finally {

			is.close();
		}

	}

	public void moveFile(File uploadedFile, File pictureFile) {
		if (!uploadedFile.renameTo(pictureFile)) {
			throw new RuntimeException("Original picture file rename unsuccessful. From: " + uploadedFile + " to " + pictureFile);
		}
	}

}

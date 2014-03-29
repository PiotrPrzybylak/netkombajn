package com.netkombajn.eshop.product.imports;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.log4j.Logger;

public class DownloadService {

	private static final Logger log = Logger.getLogger(DownloadService.class);

	private static final int BUFFER_SIZE = 1024;

	public byte[] downloadFile(URL url) {
		try {

			InputStream is = url.openConnection().getInputStream();
			byte[] buf = new byte[BUFFER_SIZE];
			int byteRead;
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			while ((byteRead = is.read(buf)) != -1) {
				os.write(buf, 0, byteRead);
			}
			return os.toByteArray();
		} catch (IOException e) {
			log.error("Canot download file", e);
			throw new RuntimeException(e);
		}

	}
}

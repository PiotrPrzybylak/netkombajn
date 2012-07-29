package pl.netolution.sklep3.service.security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5CalculatorImpl implements MD5Calculator {

	public String calculateMD5(String input) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");

			ByteArrayOutputStream os = new ByteArrayOutputStream();

			OutputStreamWriter w = new OutputStreamWriter(os, "UTF-8");
			w.write(input);
			w.flush();

			return toHexString(messageDigest.digest(os.toByteArray()));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Wrong algortihm", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Wrong encoding", e);
		} catch (IOException e) {
			throw new RuntimeException("" + "IO problem", e);
		}

	}

	public String toHexString(byte bytes[]) {
		StringBuffer retString = new StringBuffer();
		for (int i = 0; i < bytes.length; ++i) {
			retString.append(Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1));
		}
		return retString.toString();
	}

}

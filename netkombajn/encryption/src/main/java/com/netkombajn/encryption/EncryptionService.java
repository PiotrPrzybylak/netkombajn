package com.netkombajn.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class EncryptionService {

	public String encode(String encode) {
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(encode.getBytes());
			byte[] result = messageDigest.digest();

			return toText(result);

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

	}

	private static String toText(byte[] result) {
		String string = "";
		for (byte b : result) {
			string += Integer.toString((b & 0xff) + 0x100, 16).substring(1);
		}

		return string;
	}
}

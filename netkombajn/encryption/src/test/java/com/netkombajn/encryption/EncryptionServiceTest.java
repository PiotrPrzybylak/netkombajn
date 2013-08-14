package com.netkombajn.encryption;

import com.netkombajn.encryption.EncryptionService;

import junit.framework.TestCase;

public class EncryptionServiceTest extends TestCase {

	private EncryptionService encryptionService;

	@Override
	protected void setUp() throws Exception {
		encryptionService = new EncryptionService();
	}

	public void test_shouldCodeStringProperly() {
		//given
		String baseString = "123";

		//when
		String resultString = encryptionService.encode(baseString);

		//then
		assertEquals("202cb962ac59075b964b07152d234b70", resultString);
	}

}

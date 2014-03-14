package com.netkombajn.encryption;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EncryptionServiceTest {

	private EncryptionService encryptionService = new EncryptionService();

    @Test
	public void shouldCodeStringProperly() {
		//given
		String baseString = "123";

		//when
		String resultString = encryptionService.encode(baseString);

		//then
		assertEquals("202cb962ac59075b964b07152d234b70", resultString);
	}

}

package com.netkombajn.eshop.ordering.customer;

import com.netkombajn.encryption.EncryptionService;


public class CustomerEncryptionService {

	private EncryptionService encryptionService;
	
	public CustomerEncryptionService(EncryptionService encryptionService) {
		this.encryptionService = encryptionService;
	}

	public void encodeCustomer(Customer customer) {
		customer.setPassword(encryptionService.encode(customer.getPassword()));
	}

}

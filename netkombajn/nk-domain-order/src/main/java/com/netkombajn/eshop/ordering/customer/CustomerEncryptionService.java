package com.netkombajn.eshop.ordering.customer;

import pl.netolution.sklep3.service.EncryptionService;


public class CustomerEncryptionService {

	private EncryptionService encryptionService;
	
	public CustomerEncryptionService(EncryptionService encryptionService) {
		this.encryptionService = encryptionService;
	}

	public void encodeCustomer(Customer customer) {
		customer.setPassword(encryptionService.encode(customer.getPassword()));
	}

}

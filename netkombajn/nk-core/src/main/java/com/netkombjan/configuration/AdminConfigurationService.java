package com.netkombjan.configuration;

import pl.netolution.sklep3.dao.AdminConfigurationDao;
import pl.netolution.sklep3.domain.payment.PosDetails;
import pl.netolution.sklep3.service.EmailService.EmailConfiguration;
import pl.netolution.sklep3.service.OrderEmailService.Configuration;

public class AdminConfigurationService implements Configuration,
		pl.netolution.sklep3.service.payment.GeneralExternalPaymentSystem.Configuration ,
		pl.netolution.sklep3.service.ProductService.HitsConfiguration,
		EmailConfiguration,
		pl.netolution.sklep3.service.imports.IncomImportService.Configuration {

	private AdminConfigurationDao adminConfigurationDao;

	public AdminConfigurationService(AdminConfigurationDao adminConfigurationDao) {
		this.adminConfigurationDao = adminConfigurationDao;
	}

	public String getEmail() {
		return adminConfigurationDao.getMainConfiguration().getEmail();
	}

	public boolean isSendOrderEmail() {
		return adminConfigurationDao.getMainConfiguration().isSendOrderEmail();
	}

	public PosDetails getPosDetails() {
		return adminConfigurationDao.getMainConfiguration().getPosDetails();
	}

	public Integer getMaxHitsNumber() {
		return adminConfigurationDao.getMainConfiguration().getMaxHitsNumber();
	}

	public int getProfitMargin() {
		return adminConfigurationDao.getMainConfiguration().getProfitMargin();
	}

}

package pl.netolution.sklep3.service;

import pl.netolution.sklep3.dao.TextDao;

public class TextMessageService {

	private TextDao textDao;

	public String getRemindedPasswordMessage(String password, String email) {
		String emailText = textDao.findTextByName("emailPrzypomnijHaslo").getText();

		String replacedText = emailText.replaceAll("#email#", email).replace("#password#", password);

		return replacedText;

	}

	public void setTextDao(TextDao textDao) {
		this.textDao = textDao;
	}

}

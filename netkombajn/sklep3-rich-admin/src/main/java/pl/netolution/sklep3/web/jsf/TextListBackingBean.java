package pl.netolution.sklep3.web.jsf;

import java.util.List;

import pl.netolution.sklep3.dao.TextDao;
import pl.netolution.sklep3.domain.Text;

public class TextListBackingBean {

	private TextDao textDao;

	public List<Text> getTexts() {
		return textDao.getAll();
	}

	public void setTextDao(TextDao textDao) {
		this.textDao = textDao;
	}

}

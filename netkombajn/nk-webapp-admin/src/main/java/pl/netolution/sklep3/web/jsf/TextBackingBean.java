package pl.netolution.sklep3.web.jsf;

import pl.netolution.sklep3.dao.TextDao;
import pl.netolution.sklep3.domain.Text;

public class TextBackingBean {

	private Text text;

	private TextDao textDao;

	private JsfRequestResolver jsfRequestResolver;

	public String chooseText() {
		text = getTextFromRequest();
		return "editText";
	}

	public Text getText() {
		return text;
	}

	private Text getTextFromRequest() {
		long textId = getTextIdFromRequest();
		return textDao.findById(textId);
	}

	protected long getTextIdFromRequest() {
		long productId = Long.parseLong(jsfRequestResolver.getParameter("textId"));
		return productId;
	}

	public void setJsfRequestResolver(JsfRequestResolver jsfRequestResolver) {
		this.jsfRequestResolver = jsfRequestResolver;
	}

	public void setTextDao(TextDao textDao) {
		this.textDao = textDao;
	}

	public String saveText() {
		textDao.makePersistent(text);
		text = null;
		return "texts";
	}

}

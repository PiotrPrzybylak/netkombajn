package pl.netolution.sklep3.web.controller.contact;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import pl.netolution.sklep3.dao.TextDao;

public class ContactController extends ParameterizableViewController {

	private static final String CONTACT_MAIN_TEXT_NAME = "contact_main";
	private static final String CONTACT_MAP_ADDRESS_TEXT_NAME = "contact_map_address";
	private TextDao textDao;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = super.handleRequestInternal(request, response);
		modelAndView.addObject(CONTACT_MAIN_TEXT_NAME, textDao.findTextByName(CONTACT_MAIN_TEXT_NAME));
		modelAndView.addObject(CONTACT_MAP_ADDRESS_TEXT_NAME, textDao.findTextByName(CONTACT_MAP_ADDRESS_TEXT_NAME));
		return modelAndView;
	}

	public void setTextDao(TextDao textDao) {
		this.textDao = textDao;
	}

}

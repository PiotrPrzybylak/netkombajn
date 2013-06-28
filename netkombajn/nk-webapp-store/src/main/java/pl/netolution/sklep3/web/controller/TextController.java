package pl.netolution.sklep3.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import pl.netolution.sklep3.dao.TextDao;

public class TextController implements Controller {

	private TextDao textDao;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("text", "text", textDao.findTextByName(request.getParameter("name")));
	}

	public void setTextDao(TextDao textDao) {
		this.textDao = textDao;
	}

}

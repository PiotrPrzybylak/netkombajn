package pl.netolution.sklep3.web.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.springframework.web.servlet.ModelAndView;

import pl.netolution.sklep3.dao.TextDao;
import pl.netolution.sklep3.domain.Text;

public class TextControllerTest extends TestCase {

	public void test_shouldDisplayTextByName() throws Exception {
		// given

		TextController textController = new TextController();
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getParameter("name")).thenReturn("regulamin");
		Text text = new Text();
		TextDao textDao = mock(TextDao.class);
		when(textDao.findTextByName("regulamin")).thenReturn(text);
		textController.setTextDao(textDao);

		// when

		ModelAndView result = textController.handleRequest(request, null);

		// then
		assertSame(text, result.getModel().get("text"));

	}
}

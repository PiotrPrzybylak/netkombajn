package pl.netolution.sklep3.web.jsp;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;

import junit.framework.TestCase;

import org.mockito.Matchers;
import org.mockito.Mockito;

public class QueryParameterFormRegeneratorTest extends TestCase {

	QueryParamsFormRegeneratorTag regenerator;

	@Override
	protected void setUp() throws Exception {
		regenerator = new QueryParamsFormRegeneratorTag();
	}

	public void test_shouldAppendHiddenFieldInOutput() throws JspException, IOException {
		//given
		ServletRequest request = prepareRequest();

		JspWriter writer = mock(JspWriter.class);

		initiateTag(request, writer);

		//when
		regenerator.doTag();

		//then
		verify(writer).append(Matchers.contains("<input type='hidden' name='nazwaParametru' value='wartoscParametru'/>"));
		verify(writer).append(Matchers.contains("<input type='hidden' name='nazwaParametru2' value='wartoscParametru2'/>"));
	}

	public void test_shouldAppendHiddenFieldAfterFiltering() throws JspException, IOException {
		//given
		ServletRequest request = prepareRequest();

		JspWriter writer = mock(JspWriter.class);

		initiateTag(request, writer);

		regenerator.addExcludedParameter("nazwaParametru");

		//when
		regenerator.doTag();

		//then
		verify(writer, Mockito.never()).append(Matchers.contains("<input type='hidden' name='nazwaParametru' value='wartoscParametru'/>"));
		verify(writer).append(Matchers.contains("<input type='hidden' name='nazwaParametru2' value='wartoscParametru2'/>"));
	}

	private void initiateTag(ServletRequest request, JspWriter writer) {
		PageContext context = mock(PageContext.class);
		when(context.getRequest()).thenReturn(request);
		when(context.getOut()).thenReturn(writer);

		JspFragment jspFragment = mock(JspFragment.class);

		regenerator.setJspBody(jspFragment);
		regenerator.setJspContext(context);
	}

	private ServletRequest prepareRequest() {
		Map<String, String[]> parameterMap = new HashMap<String, String[]>();
		parameterMap.put("nazwaParametru", new String[] { "wartoscParametru" });
		parameterMap.put("nazwaParametru2", new String[] { "wartoscParametru2" });

		ServletRequest request = mock(ServletRequest.class);
		when(request.getParameterMap()).thenReturn(parameterMap);
		return request;
	}
}

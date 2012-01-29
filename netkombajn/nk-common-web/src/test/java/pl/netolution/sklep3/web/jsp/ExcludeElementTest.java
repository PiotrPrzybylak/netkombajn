package pl.netolution.sklep3.web.jsp;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import junit.framework.TestCase;

import org.mockito.Mockito;

public class ExcludeElementTest extends TestCase {

	private ExcludedElementTag excludedElementTag;

	@Override
	protected void setUp() throws Exception {
		excludedElementTag = new ExcludedElementTag();
	}

	public void test_shouldAddExcludednameToParent() throws JspException, IOException {
		//given
		QueryParamsFormRegeneratorTag regenerator = mock(QueryParamsFormRegeneratorTag.class);

		excludedElementTag.setParent(regenerator);

		excludedElementTag.setExcludedElementName("nazwaElementu");

		//when
		excludedElementTag.doTag();

		//then
		verify(regenerator, Mockito.atMost(1)).addExcludedParameter("nazwaelementu");

	}
}

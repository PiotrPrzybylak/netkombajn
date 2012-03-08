package pl.netolution.sklep3.web.jsp;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.mockito.Mockito;

public class QueryParameterReplacerTest extends TestCase {

	public void test_shouldAddParameterToEmptyQuery() {
		//given
		QueryParameterReplacer replacer = new QueryParameterReplacer();

		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getQueryString()).thenReturn(null);

		//when
		String replacedQuery = replacer.replaceOrAddParameter(request, "categoryId", "123");

		//then
		assertEquals("categoryId=123", replacedQuery);

	}

	public void test_shouldAddParameterToNotEmptyQuery() {
		//given
		QueryParameterReplacer replacer = new QueryParameterReplacer();

		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getQueryString()).thenReturn("order=name&strona=dupa");

		//when
		String replacedQuery = replacer.replaceOrAddParameter(request, "categoryId", "123");

		//then
		assertEquals("order=name&strona=dupa&categoryId=123", replacedQuery);

	}

	public void test_shouldReplaceParameter() {
		//given
		QueryParameterReplacer replacer = new QueryParameterReplacer();

		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getQueryString()).thenReturn("order=name&strona=dupa&categoryId=987&product=111");

		//when
		String replacedQuery = replacer.replaceOrAddParameter(request, "categoryId", "123");

		//then
		assertEquals("order=name&strona=dupa&categoryId=123&product=111", replacedQuery);

	}

	public void test_shouldAddParameterWithOneElement() {
		//given
		QueryParameterReplacer replacer = new QueryParameterReplacer();

		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getQueryString()).thenReturn("order=name");

		//when
		String replacedQuery = replacer.replaceOrAddParameter(request, "categoryId", "123");

		//then
		assertEquals("order=name&categoryId=123", replacedQuery);

	}
}

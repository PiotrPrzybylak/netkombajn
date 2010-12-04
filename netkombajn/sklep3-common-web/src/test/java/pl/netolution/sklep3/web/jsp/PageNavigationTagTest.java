package pl.netolution.sklep3.web.jsp;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;

import junit.framework.TestCase;

import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;

import pl.netolution.sklep3.web.jsp.paginator.PaginatorItem;

public class PageNavigationTagTest extends TestCase {

	private PageNavigationTag navigationTag;

	private PageContext pageContext;

	private HttpServletRequest request;

	private JspFragment jspBody;

	@Override
	protected void setUp() throws Exception {
		navigationTag = new PageNavigationTag();

		pageContext = mock(PageContext.class);
		navigationTag.setJspContext(pageContext);

		jspBody = mock(JspFragment.class);
		navigationTag.setJspBody(jspBody);

		request = mock(HttpServletRequest.class);
	}

	public void test_shouldAddProperNumberOfPaginatorsWhen100Items() throws JspException, IOException {
		//given
		given(pageContext.getRequest()).willReturn(request);

		navigationTag.setCurrentStart(0);
		navigationTag.setFixedItemsOnPage(5);
		navigationTag.setNumberOfAllItems(100);

		//when
		navigationTag.doTag();

		//then
		ArgumentCaptor<List<PaginatorItem>> paginators = new ArgumentCaptor<List<PaginatorItem>>();
		verify(pageContext, atLeastOnce()).setAttribute(Matchers.anyString(), Matchers.anyString());
		verify(pageContext, times(1)).setAttribute(Matchers.eq("paginators"), paginators.capture());

		assertEquals(10, paginators.getValue().size());
	}

	public void test_shouldAddProperNumberOfPaginatorsWhenLessThanPaginatorsItems() throws JspException, IOException {
		//given
		given(pageContext.getRequest()).willReturn(request);

		navigationTag.setCurrentStart(0);
		navigationTag.setFixedItemsOnPage(5);
		navigationTag.setNumberOfAllItems(25);

		//when
		navigationTag.doTag();

		//then
		ArgumentCaptor<List<PaginatorItem>> paginators = new ArgumentCaptor<List<PaginatorItem>>();
		verify(pageContext, atLeastOnce()).setAttribute(Matchers.anyString(), Matchers.anyString());
		verify(pageContext, times(1)).setAttribute(Matchers.eq("paginators"), paginators.capture());

		assertEquals(5, paginators.getValue().size());
	}

	public void test_shouldProperlySetPaginatorsWhenStartIsZeroAnd100Items() throws JspException, IOException {
		//given
		given(pageContext.getRequest()).willReturn(request);

		navigationTag.setCurrentStart(0);
		navigationTag.setFixedItemsOnPage(5);
		navigationTag.setNumberOfAllItems(100);

		//when
		navigationTag.doTag();

		//then
		ArgumentCaptor<List<PaginatorItem>> paginators = new ArgumentCaptor<List<PaginatorItem>>();
		verify(pageContext, atLeastOnce()).setAttribute(Matchers.anyString(), Matchers.anyString());
		verify(pageContext, times(1)).setAttribute(Matchers.eq("paginators"), paginators.capture());

		int lastIndex = paginators.getValue().size() - 1;

		assertEquals(10, paginators.getValue().size());
		assertEquals(1, paginators.getValue().get(0).getSubpageNumber());
		assertEquals(true, paginators.getValue().get(0).isCurrent());
		assertEquals(10, paginators.getValue().get(lastIndex).getSubpageNumber());
	}

	public void test_shouldProperlySetPaginatorsWhenStartIsFiveAnd100Items() throws JspException, IOException {
		//given
		given(pageContext.getRequest()).willReturn(request);

		navigationTag.setCurrentStart(25);
		navigationTag.setFixedItemsOnPage(5);
		navigationTag.setNumberOfAllItems(100);

		//when
		navigationTag.doTag();

		//then
		ArgumentCaptor<List<PaginatorItem>> paginators = new ArgumentCaptor<List<PaginatorItem>>();
		verify(pageContext, atLeastOnce()).setAttribute(Matchers.anyString(), Matchers.anyString());
		verify(pageContext, times(1)).setAttribute(Matchers.eq("paginators"), paginators.capture());

		int lastIndex = paginators.getValue().size() - 1;

		assertEquals(10, paginators.getValue().size());
		assertEquals(1, paginators.getValue().get(0).getSubpageNumber());
		assertEquals(true, paginators.getValue().get(5).isCurrent());
		assertEquals(10, paginators.getValue().get(lastIndex).getSubpageNumber());
	}

	public void test_shouldProperlySetPaginatorsWhenStartIsSeventeenAnd200Items() throws JspException, IOException {
		//given
		given(pageContext.getRequest()).willReturn(request);

		navigationTag.setCurrentStart(85);
		navigationTag.setFixedItemsOnPage(5);
		navigationTag.setNumberOfAllItems(200);

		//when
		navigationTag.doTag();

		//then
		ArgumentCaptor<List<PaginatorItem>> paginators = new ArgumentCaptor<List<PaginatorItem>>();
		verify(pageContext, atLeastOnce()).setAttribute(Matchers.anyString(), Matchers.anyString());
		verify(pageContext, times(1)).setAttribute(Matchers.eq("paginators"), paginators.capture());

		int lastIndex = paginators.getValue().size() - 1;

		assertEquals(10, paginators.getValue().size());
		assertEquals(13, paginators.getValue().get(0).getSubpageNumber());
		assertEquals(true, paginators.getValue().get(5).isCurrent());
		assertEquals(22, paginators.getValue().get(lastIndex).getSubpageNumber());
	}

	public void test_shouldProperlySetPaginatorsWhenStartIsSeventeenAnd100Items() throws JspException, IOException {
		//given
		given(pageContext.getRequest()).willReturn(request);

		navigationTag.setCurrentStart(85);
		navigationTag.setFixedItemsOnPage(5);
		navigationTag.setNumberOfAllItems(100);

		//when
		navigationTag.doTag();

		//then
		ArgumentCaptor<List<PaginatorItem>> paginators = new ArgumentCaptor<List<PaginatorItem>>();
		verify(pageContext, atLeastOnce()).setAttribute(Matchers.anyString(), Matchers.anyString());
		verify(pageContext, times(1)).setAttribute(Matchers.eq("paginators"), paginators.capture());

		int lastIndex = paginators.getValue().size() - 1;

		assertEquals(10, paginators.getValue().size());
		assertEquals(11, paginators.getValue().get(0).getSubpageNumber());
		assertEquals(true, paginators.getValue().get(7).isCurrent());
		assertEquals(20, paginators.getValue().get(lastIndex).getSubpageNumber());
		assertTrue(paginators.getValue().get(lastIndex).getUrl().contains("95"));
	}

	public void test_shouldReturnPreviousUrl() throws JspException, IOException {
		//given
		given(pageContext.getRequest()).willReturn(request);

		navigationTag.setCurrentStart(85);
		navigationTag.setFixedItemsOnPage(5);
		navigationTag.setNumberOfAllItems(100);

		//when
		navigationTag.doTag();

		//then
		ArgumentCaptor<String> prevLinks = new ArgumentCaptor<String>();
		verify(pageContext, atLeastOnce()).setAttribute(Matchers.anyString(), Matchers.anyString());
		verify(pageContext, times(1)).setAttribute(Matchers.eq("prevLink"), prevLinks.capture());
		assertTrue(prevLinks.getValue().contains("80"));
	}

	public void test_shouldReturnPreviousNullUrlWhenStartZero() throws JspException, IOException {
		//given
		given(pageContext.getRequest()).willReturn(request);

		navigationTag.setCurrentStart(0);
		navigationTag.setFixedItemsOnPage(5);
		navigationTag.setNumberOfAllItems(100);

		//when
		navigationTag.doTag();

		//then
		ArgumentCaptor<String> prevLinks = new ArgumentCaptor<String>();
		verify(pageContext, atLeastOnce()).setAttribute(Matchers.anyString(), Matchers.anyString());
		verify(pageContext, times(1)).setAttribute(Matchers.eq("prevLink"), prevLinks.capture());
		assertNull(prevLinks.getValue());
	}

	public void test_shouldReturnNextUrl() throws JspException, IOException {
		//given
		given(pageContext.getRequest()).willReturn(request);

		navigationTag.setCurrentStart(85);
		navigationTag.setFixedItemsOnPage(5);
		navigationTag.setNumberOfAllItems(100);

		//when
		navigationTag.doTag();

		//then
		ArgumentCaptor<String> nextLinks = new ArgumentCaptor<String>();
		verify(pageContext, atLeastOnce()).setAttribute(Matchers.anyString(), Matchers.anyString());
		verify(pageContext, times(1)).setAttribute(Matchers.eq("nextLink"), nextLinks.capture());
		assertTrue(nextLinks.getValue().contains("90"));
	}

	public void test_shouldReturnNextUrlNullWhenPageIsLast() throws JspException, IOException {
		//given
		given(pageContext.getRequest()).willReturn(request);

		navigationTag.setCurrentStart(95);
		navigationTag.setFixedItemsOnPage(5);
		navigationTag.setNumberOfAllItems(100);

		//when
		navigationTag.doTag();

		//then
		ArgumentCaptor<String> nextLinks = new ArgumentCaptor<String>();
		verify(pageContext, atLeastOnce()).setAttribute(Matchers.anyString(), Matchers.anyString());
		verify(pageContext, times(1)).setAttribute(Matchers.eq("nextLink"), nextLinks.capture());
		assertNull(nextLinks.getValue());
	}

	public void test_shouldWorkingWithEmptyList() throws JspException, IOException {
		//given
		given(pageContext.getRequest()).willReturn(request);

		navigationTag.setCurrentStart(0);
		navigationTag.setFixedItemsOnPage(5);
		navigationTag.setNumberOfAllItems(0);

		//when
		navigationTag.doTag();

		//then
		assertTrue(true);
	}
}

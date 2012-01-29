package pl.netolution.sklep3.web.jsp.paginator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import junit.framework.TestCase;

public class PaginatorListTest extends TestCase {

	private PaginatorList paginatorList;

	@Override
	protected void setUp() throws Exception {
		paginatorList = new PaginatorList(5);
	}

	public void test_shouldBeFirstChosenTrue() {
		//given
		PaginatorItem paginator1 = mock(PaginatorItem.class);
		given(paginator1.isCurrent()).willReturn(true);
		paginatorList.addPaginator(paginator1);

		//when
		boolean result = paginatorList.isFirstChoosen();

		//then
		assertEquals(true, result);
	}

	public void test_shouldBeLastChosenTrue() {
		//given
		PaginatorItem paginator1 = mock(PaginatorItem.class);
		given(paginator1.isCurrent()).willReturn(true);

		PaginatorItem paginator2 = mock(PaginatorItem.class);
		given(paginator2.isCurrent()).willReturn(true);

		paginatorList.addPaginator(paginator1);
		paginatorList.addPaginator(paginator2);

		//when
		boolean result = paginatorList.isLastChoosen();

		//then
		assertEquals(true, result);
	}

}

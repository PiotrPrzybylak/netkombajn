package pl.netolution.sklep3.test;

import junit.framework.TestCase;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringContextTest extends TestCase {

	public void testContext() {
		new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml", "testApplicationContext.xml" });
		// assertNotNull(ap.getBean("dataSource"));
	}
}

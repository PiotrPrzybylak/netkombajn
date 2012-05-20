package pl.netolution.sklep3.lucene;

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.LockObtainFailedException;
import org.mockito.Mockito;

import pl.netolution.sklep3.configuration.Configuration;
import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.domain.Designer;
import pl.netolution.sklep3.domain.Manufacturer;
import pl.netolution.sklep3.domain.Product;

public class SearchEngineTest extends TestCase {
	PhraseIndexer phraseIndexer;

	PhraseSearcher phraseSearcher;

	Configuration config;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		config = Mockito.mock(Configuration.class);
		Mockito.when(config.getIndexPath()).thenReturn("target/lucene");
		phraseIndexer = new PhraseIndexer();
		phraseIndexer.setConfiguration(config);
		phraseSearcher = new PhraseSearcher();
		phraseSearcher.setConfiguration(config);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		IndexWriter indexWriter = new IndexWriter(config.getIndexPath(), new StandardAnalyzer(), MaxFieldLength.LIMITED);
		indexWriter.deleteDocuments(new Term(BaseSearcher.NAME, "99"));
		indexWriter.close();
	}

	public void testSimpleAddAndSearch() throws Exception {

		prepareProducts();
		// find by name
		Long[] results = phraseSearcher.findPhrase("lenovo", null);
		assertNotNull(results);
		assertEquals(1, results.length);
		assertEquals(1, results[0].longValue());

		results = phraseSearcher.findPhrase("logitech", null);
		assertNotNull(results);
		assertEquals(1, results.length);
		assertEquals(2, results[0].longValue());

		// use description ,no name
		results = phraseSearcher.findPhrase("najnowszy", null);
		assertNotNull(results);
		assertEquals(1, results.length);
		assertEquals(1, results[0].longValue());

		results = phraseSearcher.findPhrase("super", null);
		assertNotNull(results);
		assertEquals(1, results.length);
		assertEquals(2, results[0].longValue());
		// use designer
		results = phraseSearcher.findPhrase("ibmD", null);
		assertNotNull(results);
		assertEquals(2, results.length);
		// use manufacturer
		results = phraseSearcher.findPhrase("ibmM", null);
		assertNotNull(results);
		assertEquals(2, results.length);

		// negative use name ,and not existing category
		results = phraseSearcher.findPhrase("logitech", "x");
		assertNotNull(results);
		assertEquals(0, results.length);

		// negative use bad name ,and existing category
		results = phraseSearcher.findPhrase("lenovo", "myszki");
		assertNotNull(results);
		assertEquals(0, results.length);

		//update test
		results = phraseSearcher.findPhrase("99", null);
		assertNotNull(results);
		assertEquals(2, results.length);

	}

	private void prepareProducts() throws CorruptIndexException, LockObtainFailedException, IOException {
		Category category1 = new Category();
		category1.setName("laptopy");

		Designer designer = new Designer();
		designer.setName("ibmD");

		Manufacturer manufacturer = new Manufacturer();
		manufacturer.setName("ibmM");

		Product product1 = new Product(1);
		product1.setCategory(category1);
		product1.setDesigner(designer);
		product1.setManufacturer(manufacturer);
		product1.setName("Lenovo1 laptop 99");
		product1.setDescription("Najnowszy laptop Lenovo");

		phraseIndexer.addPhrase(product1);

		Product product1a = new Product(1);
		product1a.setCategory(category1);
		product1a.setDesigner(designer);
		product1a.setManufacturer(manufacturer);
		product1a.setName("Lenovo laptop 99");
		product1a.setDescription("Najnowszy laptop Lenovo");

		phraseIndexer.addPhrase(product1a);

		Category category2 = new Category();
		category2.setName("myszki");

		Product product2 = new Product(2);
		product2.setCategory(category2);
		product2.setDesigner(designer);
		product2.setManufacturer(manufacturer);
		product2.setName("Myszka logitech 99");
		product2.setDescription("Super myszka");

		phraseIndexer.addPhrase(product2);
	}

}

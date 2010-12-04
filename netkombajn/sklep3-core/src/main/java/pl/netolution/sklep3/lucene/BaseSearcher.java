package pl.netolution.sklep3.lucene;

import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;

import pl.netolution.sklep3.configuration.Configuration;

public class BaseSearcher {

	static final String DESCRIPTION = "description";

	static final String ID = "id";

	static final String CATEGORY = "category";

	static final String NAME = "name";

	static final String DESIGNER = "designer";

	static final String MANUFACTURER = "manufacturer";

	private Configuration configuration;

	protected Searcher createIndexSearcher() throws CorruptIndexException, IOException {
		Searcher searcher = new IndexSearcher(getConfiguration().getIndexPath());
		return searcher;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
}
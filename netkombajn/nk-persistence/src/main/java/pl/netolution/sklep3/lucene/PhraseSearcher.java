package pl.netolution.sklep3.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopDocs;

public class PhraseSearcher extends BaseSearcher {
	private static final int MAX_RESULT = 1000;

	private static final Logger log = Logger.getLogger(PhraseSearcher.class);

	// TODO catch exception
	public Long[] findPhrase(String phraseToSearch, String category) {

		try {
			Searcher searcher = createIndexSearcher();

			MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[] { NAME, DESCRIPTION, DESIGNER, MANUFACTURER },
					new StandardAnalyzer());

			Query query = parser.parse(phraseToSearch);
			TopDocs docResults = searcher.search(query, MAX_RESULT);
			ScoreDoc[] hits = docResults.scoreDocs;
			List<Long> result = new ArrayList<Long>();
			for (ScoreDoc hit : hits) {
				Document doc = searcher.doc(hit.doc);
				// iterate over categories this should be done in
				// filter or better else :)\
				if (categoryFilter(doc, category)) {
					String productId = doc.get(ID);
					result.add(Long.valueOf(productId));
					log.debug("Found porductId = " + productId);

				}

			}
			searcher.close();
			return result.toArray(new Long[result.size()]);
		} catch (CorruptIndexException e) {
			log.error("Application error ", e);
			throw new RuntimeException("Application error ", e);
		} catch (IOException e) {
			log.error("Could not load index for path " + getConfiguration() != null ? getConfiguration().getIndexPath() : " no config", e);
			throw new RuntimeException("Could not load index for path " + getConfiguration() != null ? getConfiguration().getIndexPath()
					: " no config", e);
		} catch (ParseException e) {
			log.error("Could not pase query for phrase " + phraseToSearch + " and category " + category, e);
			throw new RuntimeException("Could not pase query for phrase " + phraseToSearch + " and category " + category, e);

		}
	}

	/**
	 * Filter only those categories in which product exists
	 * 
	 * @param doc
	 * @param category
	 * @return true if product exists in given category or category is null
	 */
	private boolean categoryFilter(Document doc, String category) {
		if (category != null) {
			String categoryIndexed = doc.get(CATEGORY);
			if (!category.equals(categoryIndexed)) {
				return false;
			}
		}
		return true;
	}
}

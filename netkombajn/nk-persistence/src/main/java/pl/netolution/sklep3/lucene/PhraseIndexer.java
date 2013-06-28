package pl.netolution.sklep3.lucene;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.LockObtainFailedException;

import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.domain.Product;

public class PhraseIndexer extends BaseSearcher {

	private static final Logger log = Logger.getLogger(PhraseIndexer.class);

	private ProductDao productDao;

	public void init() throws CorruptIndexException, LockObtainFailedException, IOException {
		List<Product> productsOnSale = productDao.getVisibleProducts();
		for (Product product : productsOnSale) {
			addPhrase(product);
		}
	}

	public void addPhrase(Product product) {

		String category = getParentCategoryName(product.getCategory());
		String manufacturer = product.getManufacturer() != null ? product.getManufacturer().getName() : "";
		String designer = product.getDesigner() != null ? product.getDesigner().getName() : "";
		try {
			IndexWriter indexWriter = createIndexWriter();
			Document doc = new Document();
			Field phraseField = new Field(NAME, product.getName(), Field.Store.YES, Field.Index.ANALYZED);
			Field idFiled = new Field(ID, product.getId().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED);
			Field categoryField = new Field(CATEGORY, category, Field.Store.YES, Field.Index.NO);
			Field descriptionField = new Field(DESCRIPTION, product.getDescription(), Field.Store.YES, Field.Index.ANALYZED);
			Field designerField = new Field(DESIGNER, designer, Field.Store.YES, Field.Index.ANALYZED);
			Field manufacturerField = new Field(MANUFACTURER, manufacturer, Field.Store.YES, Field.Index.ANALYZED);

			doc.add(phraseField);
			doc.add(idFiled);
			doc.add(categoryField);
			doc.add(descriptionField);
			doc.add(designerField);
			doc.add(manufacturerField);

			indexWriter.updateDocument(new Term(ID, product.getId().toString()), doc);
			indexWriter.close();
		} catch (Exception e) {
			log.error("Unable to add phrase into the index productId " + product.getId(), e);
			throw new RuntimeException("Unable to add phrase into the index productId " + product.getId(), e);
		}
	}

	private String getParentCategoryName(Category category) {
		if (category == null) {
			return "";
		}
		if (category.getParent() != null) {
			return getParentCategoryName(category.getParent());
		}
		return category.getName();
	}

	private IndexWriter createIndexWriter() throws CorruptIndexException, LockObtainFailedException, IOException {
		// TODO think about it ,whether it should be singleton with synchronized access or catch
		// LockObtainFaildException ?

		return new IndexWriter(getConfiguration().getIndexPath(), new StandardAnalyzer(), MaxFieldLength.LIMITED);
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public ProductDao getProductDao() {
		return productDao;
	}
}

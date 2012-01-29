package pl.netolution.sklep3.service.imports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

public class CategoriesInfo {

	private Map<String, List<String>> categories = new HashMap<String, List<String>>();
	private Map<String, String> names = new HashMap<String, String>();

	@SuppressWarnings("unchecked")
	public CategoriesInfo(Document document) {
		Element root = document.getRootElement();

		for (Iterator i = root.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();
			String parentId = element.element("idh").getText();
			names.put(element.elementText("id"), element.elementText("name"));
			List<String> childs = categories.get(parentId);
			if (null == childs) {
				categories.put(parentId, new ArrayList<String>());
			}

			categories.get(parentId).add(element.elementText("id"));
		}

	}

	public Map<String, List<String>> getCategories() {
		return categories;
	}

	public Map<String, String> getNames() {
		return names;
	}
}
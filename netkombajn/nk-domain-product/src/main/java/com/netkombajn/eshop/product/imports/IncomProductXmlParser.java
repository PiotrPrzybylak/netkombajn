package com.netkombajn.eshop.product.imports;

import org.dom4j.Document;
import org.dom4j.Element;

import java.util.*;

public class IncomProductXmlParser {

    public List<Map<String, String>> convertXmlToListOfMaps(Document document) {
        final List<Map<String, String>> products = new ArrayList<Map<String, String>>();
        for (@SuppressWarnings("unchecked")
		Iterator<Element> i = document.getRootElement().elementIterator(); i.hasNext(); ) {
            products.add(convertDomElementToHashMap(i.next()));
        }
        return products;
    }

    private HashMap<String, String> convertDomElementToHashMap(Element element) {
        final HashMap<String, String> productDetails = new HashMap<String, String>();
        productDetails.put("cena", element.elementTextTrim("cena"));
        productDetails.put("symbol_produktu", element.elementTextTrim("symbol_produktu"));
        productDetails.put("nazwa_produktu", element.elementTextTrim("nazwa_produktu"));
        productDetails.put("nazwa_producenta", element.elementTextTrim("nazwa_producenta"));
        productDetails.put("grupa_towarowa", element.elementTextTrim("grupa_towarowa"));
        productDetails.put("stan_magazynowy", element.elementTextTrim("stan_magazynowy"));
        productDetails.put("link_do_zdjecia_produktu", element.elementTextTrim("link_do_zdjecia_produktu"));
        productDetails.put("opis_produktu", element.elementTextTrim("opis_produktu"));
        return productDetails;
    }
}
package com.netkombajn.eshop.product.imports;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import pl.netolution.sklep3.dao.CategoryDao;
import pl.netolution.sklep3.domain.Category;

import java.util.List;
import java.util.Map;

public class IncomImportCategoriesServiceImpl implements IncomImportCategoriesService {

    private static final Logger log = Logger.getLogger(IncomImportCategoriesServiceImpl.class);

    private final CategoryDao categoryDao;

    public IncomImportCategoriesServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    /**
     * ********************************************** KATEGORIE *********************
     */
    // TODO zastanowic sie czy skladanie drzewa powinno byc w tym komponencie
    @Transactional
    public void mergeImportCategories(Map<String, List<String>> categoriesTree, Map<String, String> names) {

        for (String externalId : categoriesTree.get("")) {
            Category category = getCategoryByExternalId(externalId, names.get(externalId));
            addChildren(categoriesTree, names, externalId, category);
        }

    }

    private void addChildren(Map<String, List<String>> categoriesTree, Map<String, String> names, String parentExternalId, Category category) {

        if (categoriesTree.get(parentExternalId) == null) {
            return;
        }

        for (String childExternalId : categoriesTree.get(parentExternalId)) {
            Category child = getCategoryByExternalId(childExternalId, names.get(childExternalId));
            category.addChild(child);
            addChildren(categoriesTree, names, childExternalId, child);
        }
    }

    private Category getCategoryByExternalId(String externalId, String name) {
        Category category = categoryDao.findByExternalId(externalId);
        log.debug("Category " + category + " found for extenalId:" + externalId);
        if (null == category) {
            category = new Category();
            category.setName(name);
            category.setExternalId(externalId);
            categoryDao.makePersistent(category);
        }
        return category;
    }
}
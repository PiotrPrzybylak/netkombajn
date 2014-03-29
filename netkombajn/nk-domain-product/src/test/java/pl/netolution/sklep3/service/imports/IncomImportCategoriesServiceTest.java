package pl.netolution.sklep3.service.imports;

import org.junit.Test;

import com.netkombajn.eshop.product.imports.IncomImportCategoriesService;
import com.netkombajn.eshop.product.imports.IncomImportCategoriesServiceImpl;

import pl.netolution.sklep3.dao.CategoryDao;
import pl.netolution.sklep3.domain.Category;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IncomImportCategoriesServiceTest {

    private CategoryDao categoryDao = mock(CategoryDao.class);
    private IncomImportCategoriesService service = new IncomImportCategoriesServiceImpl(categoryDao);

    private Map<String, String> createNames() {

        Map<String, String> names = new HashMap<String, String>();

        names.put("root", "rootName");
        names.put("rootChild1", "rootChild1Name");
        names.put("rootChild2", "rootChild2Name");
        names.put("child11", "child11Name");
        names.put("child12", "child12Name");
        names.put("child13", "child13Name");
        names.put("child21", "child21Name");

        return names;
    }

    private Map<String, List<String>> createImportedStructure() {
        Map<String, List<String>> categoriesTree = new HashMap<String, List<String>>();

        categoriesTree.put("", Arrays.asList(new String[]{"root"}));
        categoriesTree.put("root", Arrays.asList(new String[] { "rootChild1", "rootChild2" }));
        categoriesTree.put("rootChild1", Arrays.asList(new String[] { "child11", "child12", "child13" }));
        categoriesTree.put("rootChild2", Arrays.asList(new String[] { "child21" }));

        return categoriesTree;
    }

    @Test
    public void testMergeImportedCategories_db_with_only_root() {
        // given
        Map<String, String> names = createNames();

        Map<String, List<String>> categoriesTree = createImportedStructure();

        Category root = new Category();
        when(categoryDao.findByExternalId("root")).thenReturn(root);

        // when
        service.mergeImportCategories(categoriesTree, names);

        // then
        assertEquals(2, root.getChildren().size());
        assertEquals(3, root.getChildren().get(0).getChildren().size());
        assertEquals(1, root.getChildren().get(1).getChildren().size());
    }

}

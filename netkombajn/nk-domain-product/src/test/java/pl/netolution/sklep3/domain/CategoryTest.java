package pl.netolution.sklep3.domain;

import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import junit.framework.TestCase;

//TODO BDD i mockito
public class CategoryTest extends TestCase {

    private Category category;

    private Product visibleProduct;

    private Product hiddenProduct;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        category = new Category();
        visibleProduct = createVisibleProduct();
        hiddenProduct = createHiddenProduct();
    }

    @Override
    protected void tearDown() throws Exception {
        category = null;
        super.tearDown();
    }

    public void test_shouldReturnNumberOfOnlyLinkedProducts() throws Exception {
        // given
        Product product = mock(Product.class);
        Product product2 = mock(Product.class);
        Product product3 = mock(Product.class);

        category.addProduct(product);
        category.addProduct(product2);

        Set<Product> linkedProducts = new HashSet<Product>();
        linkedProducts.add(product);
        linkedProducts.add(product2);
        linkedProducts.add(product3);

        setLinkedProductsOnCategory(linkedProducts);

        // when
        int onlyLinkedProductsNumber = category.getLinkedProductsNumber();

        // then
        assertEquals(1, onlyLinkedProductsNumber);

    }

    private void setLinkedProductsOnCategory( Set<Product> linkedProducts ) throws NoSuchFieldException,
            IllegalAccessException {
        Field linkedProductsField = Category.class.getDeclaredField("linkedProducts");
        linkedProductsField.setAccessible(true);
        linkedProductsField.set(category, linkedProducts);
    }

    public void test_shouldAddOnlyOneCategoryToSetWhenIdSame() {
        // given
        Category category1 = new Category();
        Category category2 = new Category();
        category1.setId(1L);
        category2.setId(1L);

        Set<Category> set = new HashSet<Category>();

        // when
        set.add(category1);
        set.add(category2);

        // then
        assertEquals(1, set.size());

    }

    public void test_shouldAddAllCategoriesToSetWhenIdDifferent() {
        // given
        Category category1 = new Category();
        Category category2 = new Category();
        category1.setId(1L);
        category2.setId(2L);

        Set<Category> set = new HashSet<Category>();

        // when
        set.add(category1);
        set.add(category2);

        // then
        assertEquals(2, set.size());

    }

    public void testAddProduct() {

        Product product = new Product();

        category.addProduct(product);

        assertTrue(category.getProducts().contains(product));
        assertEquals(1, category.getProducts().size());
        assertFalse(category.getProducts().contains(null));
    }

    public void testGetVisibleProducts() {

        category.addProduct(visibleProduct);
        category.addProduct(hiddenProduct);

        Category category2 = new Category();
        category2.addProduct(createVisibleProduct());
        category2.addProduct(createVisibleProduct());
        category2.addProduct(createHiddenProduct());

        category.addChild(category2);

        assertEquals(2, category.getProducts().size());
        assertEquals(3, category.getVisibleProducts().size());
        assertTrue(category.getVisibleProducts().contains(visibleProduct));

    }

    public void testGetAllProducts() {
        category.addProduct(visibleProduct);
        category.addProduct(hiddenProduct);

        Category category2 = new Category();
        category2.addProduct(createVisibleProduct());
        category2.addProduct(createVisibleProduct());
        category2.addProduct(createHiddenProduct());

        category.addChild(category2);

        assertEquals(2, category.getProducts().size());
        assertEquals(3, category2.getAllProducts().size());
        assertEquals(5, category.getAllProducts().size());
    }

    private Product createHiddenProduct() {
        Product hiddenProduct = new Product();
        hiddenProduct.setVisible(false);
        hiddenProduct.setCatalogNumber("" + new Random().nextInt());
        return hiddenProduct;
    }

    private Product createVisibleProduct() {
        Product visibleProduct = new Product();
        visibleProduct.setVisible(true);
        visibleProduct.setCatalogNumber("" + new Random().nextInt());
        return visibleProduct;
    }

    public void testGetVisibleProductsWithChildren() {
        category.addProduct(visibleProduct);
        category.addProduct(hiddenProduct);

        Category child1 = new Category();
        category.addChild(child1);

        Category child2 = new Category();
        category.addChild(child2);

        assertEquals(1, category.getVisibleProductsNumber());
        child1.addProduct(createHiddenProduct());
        assertEquals(1, category.getVisibleProductsNumber());
        child1.addProduct(createVisibleProduct());
        assertEquals(2, category.getVisibleProductsNumber());

        Category grandChild = new Category();
        child2.addChild(grandChild);

        grandChild.addProduct(createVisibleProduct());
        grandChild.addProduct(createVisibleProduct());

        assertEquals(4, category.getVisibleProductsNumber());
    }

    public void testGetDescendants() {
        Category child1 = new Category();
        child1.setId(1L);
        category.addChild(child1);

        Category child2 = new Category();
        child2.setId(2L);
        category.addChild(child2);

        assertEquals(2, category.getDescendants().size());

        Category grandChild = new Category();
        child2.addChild(grandChild);

        assertEquals(3, category.getDescendants().size());

    }

    public void testGetRootCategory() {

        assertSame(category, category.getRootCategory());

        Category parent = new Category();
        parent.addChild(category);

        assertSame(parent, category.getRootCategory());

        Category grandParent = new Category();
        grandParent.addChild(parent);

        assertSame(grandParent, category.getRootCategory());
    }

    public void testDeleteCategories() throws Exception {
        // with linked categories and products
        Category linkedCategory = new Category();
        linkedCategory.setId(2l);
        visibleProduct.setCategory(category);
        visibleProduct.addLinkedCategory(linkedCategory);
        category.addProduct(visibleProduct);
        assertFalse(category.isDeletable());

        // with product
        visibleProduct.removeLinkedCategory(linkedCategory);
        assertFalse(category.isDeletable());
        //
        // // with linked categories
        // visibleProduct.addLinkedCategory(linkedCategory);
        // visibleProduct.setCategory(null);
        // assertFalse(category.canBeDeleted());

        // empty category
        visibleProduct.removeLinkedCategory(linkedCategory);
        category.removeProduct(visibleProduct);
        assertTrue(category.isDeletable());
    }
}

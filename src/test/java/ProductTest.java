import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.Arrays;

public class ProductTest {
  private Product testProduct;

  @Before
  public void initialize() {
    testProduct = new Product("Manba Face Packs", 22.99);
    testProduct.save();
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void product_instantiatesCorrectly_true() {
    assertEquals(true, testProduct instanceof Product);
  }

  @Test
  public void getName_productInstantiatesWithName_String() {
    assertEquals("Manba Face Packs", testProduct.getName());
  }

  @Test
  public void getName_productInstantiatesWithPrice_Double() {
    assertEquals(22.99, testProduct.getPrice(),0.0);
  }

  @Test
  public void equals_returnsTrueIfNameAndPriceAreSame_true() {
    Product anotherProduct = new Product("Manba Face Packs", 22.99);
    assertTrue(testProduct.equals(anotherProduct));
  }

  @Test
  public void save_insertsObjectIntoDatabase_Product() {
    assertEquals(true, Product.all().get(0).equals(testProduct));
  }

  @Test
  public void all_returnsAllInstancesOfProduct_true() {
    Product secondProduct = new Product("Godzilla Face Packs", 21);
    secondProduct.save();
    assertEquals(true, Product.all().get(0).equals(testProduct));
    assertEquals(true, Product.all().get(1).equals(secondProduct));
  }

  @Test
  public void save_assignsIdToObject() {
    Product savedProduct = Product.all().get(0);
    assertEquals(testProduct.getId(), savedProduct.getId());
  }

  // @Test
  // public void getItems_retrievesAllItemsFromDatabase_itemsList() {
  //   Item firstItem = new Item("Bubbles", testProduct.getId());
  //   firstItem.save();
  //   Item secondItem = new Item("Spud", testProduct.getId());
  //   secondItem.save();
  //   Item[] items = new Item[] { firstItem, secondItem };
  //   assertTrue(testProduct.getItems().containsAll(Arrays.asList(items)));
  // }

}

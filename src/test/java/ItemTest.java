import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.sql.Timestamp;
import java.util.Date;
import java.text.DateFormat;

public class ItemTest {
  private Item testItem;

  @Before
  public void initialize() {
    testItem = new Item(1, 1);
  }

  @Rule
  public DatabaseRule databasee = new DatabaseRule();

  @Test
  public void item_instantiatesCorrectly_true() {
    assertEquals(true, testItem instanceof Item);
  }

  @Test
  public void item_instantiatesWithProductId_int() {
    assertEquals(1, testItem.getProductId());
  }

  @Test
  public void item_instantiatesWithCustomerId_int() {
    assertEquals(1, testItem.getCustomerId());
  }

  @Test
  public void item_recordsTimeOfCreationInDatabase() {
    testItem.save();
    Timestamp savedItemDateSold = Item.find(testItem.getId()).getDateSold();
    Timestamp rightNow = new Timestamp (new Date().getTime());
    assertEquals(rightNow.getDay(), savedItemDateSold.getDay());
  }

  @Test
  public void save_returnsTrueIfDescriptionsAreTheSame() {
    testItem.save();
    Item savedItem = Item.all().get(0);
    assertEquals(savedItem.getId(), testItem.getId());
  }

  @Test
  public void find_returnsItemWithSameId_secondItem() {
    testItem.save();
    Item secondItem = new Item(2, 2);
    secondItem.save();
    assertEquals(Item.find(secondItem.getId()), secondItem);
  }

  @Test
  public void save_savesProductIdIntoDB_true() {
    Product testProduct = new Product("Godzilla Face Pack", 21);
    testProduct.save();
    Item testItemTwo = new Item(testProduct.getId(), 1);
    testItemTwo.save();
    Item savedItem = Item.find(testItemTwo.getId());
    assertEquals(savedItem.getProductId(), testProduct.getId());
  }

}

import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.sql.Timestamp;
import java.util.Date;
import java.text.DateFormat;
import java.util.Arrays;

public class ApparelTest {
  private Apparel testApparel;
  private Apparel anotherApparel;

  @Before
  public void initialize() {
    testApparel = new Apparel("Furoshiki Shoes", 357.00f, "S");
    anotherApparel = new Apparel("Samurai Armor", 63.00f, "M");
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void apparel_instantiatesCorrectly_true() {
    assertEquals(true, testApparel instanceof Apparel);
  }

  @Test
  public void Apparel_instantiatesWithName_String() {
    assertEquals("Furoshiki Shoes", testApparel.getName());
  }

  @Test
  public void Apparel_instantiatesPrice_float(){
    assertEquals(357.00f, testApparel.getPrice(),0);
  }

  @Test
  public void Apparel_instantiatesWithSize_String(){
    assertEquals("S", testApparel.getSize());
  }


  @Test
  public void equals_returnsTrueIfNamesAreSame_true() {
    Apparel sameApparel = new Apparel("Furoshiki Shoes", 357.00f, "S");
    assertTrue(testApparel.equals(sameApparel));
  }

  @Test
  public void save_successfullyAddsApparelToDatabase_List() {
    testApparel.save();
    assertTrue(Apparel.all().get(0).equals(testApparel));
  }

  @Test
  public void save_assignsIdToApparel() {
    testApparel.save();
    Apparel savedApparel = Apparel.all().get(0);
    assertEquals(savedApparel.getId(), testApparel.getId());
  }

  @Test
  public void all_returnsAllInstancesOfApparel_true() {
    testApparel.save();
    anotherApparel.save();
    assertEquals(true, Apparel.all().get(0).equals(testApparel));
    assertEquals(true, Apparel.all().get(1).equals(anotherApparel));
  }

  @Test
  public void find_returnsApparelWithSameId_anotherApparel() {
    testApparel.save();
    anotherApparel.save();
    assertEquals(Apparel.find(anotherApparel.getId()), anotherApparel);
  }

  @Test
  public void apparel_instantiatesWithMaxInventoryQuantity(){
    assertEquals(testApparel.getInventoryQuantity(), (Apparel.MAX_INVENTORYQUANTITY));
  }

  @Test
  public void apparel_instantiatesWithQuantitySold(){
    assertEquals(testApparel.getQuantitySold(), 0);
  }

  @Test
  public void sell_lowersInvetoryAndIncreasesSales_(){
    testApparel.sell();
    assertEquals(testApparel.getInventoryQuantity(), (Apparel.MAX_INVENTORYQUANTITY - 1));
    assertEquals(testApparel.getQuantitySold(), 1);
  }

  @Test
  public void stockInventory_increaseInventoryQuantity(){
    testApparel.sell();
    testApparel.stockInventory();
    assertEquals(testApparel.getInventoryQuantity(), Apparel.MAX_INVENTORYQUANTITY);
  }

  @Test
  public void apparel_inventoryQuantityCannotGoBeyondMaxValue(){
    for(int i = Apparel.MIN_ALL; i <= (Apparel.MAX_INVENTORYQUANTITY); i++){
      try {
        testApparel.stockInventory();
      } catch (UnsupportedOperationException exception){ }
    }
    assertTrue(testApparel.getInventoryQuantity() <= Apparel.MAX_INVENTORYQUANTITY);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void apparel_throwsExceptionIfInventoryQuantityIsAtMaxValue(){
    for(int i = Apparel.MIN_ALL; i <= (Apparel.MAX_INVENTORYQUANTITY); i++){
      testApparel.stockInventory();
    }
  }

  @Test
  public void save_assignsIdToObject() {
    testApparel.save();
    Apparel savedApparel = Apparel.all().get(0);
    assertEquals(testApparel.getId(), savedApparel.getId());
  }

  @Test
  public void getSalesHistory_retrievesAllSalesHistoryFromDatabase_saleHistoryList() {
    testApparel.save();
    SalesHistory firstSalesHistory = new SalesHistory(testApparel.getId(), 1, 1, 5.00f);
    firstSalesHistory.save();
    SalesHistory secondSalesHistory = new SalesHistory(testApparel.getId(), 2, 2, 10.00f);
    secondSalesHistory.save();
    SalesHistory[] salesHistory = new SalesHistory[] { firstSalesHistory, secondSalesHistory };
    assertTrue(testApparel.getSalesHistory().containsAll(Arrays.asList(salesHistory)));
  }

  @Test
  public void searchByName_retrieveAllProductsByName(){
    testApparel.save();
    anotherApparel.save();
    assertEquals("Furoshiki Shoes", Apparel.searchByName("Furoshiki Shoes").get(0).getName());
  }

}

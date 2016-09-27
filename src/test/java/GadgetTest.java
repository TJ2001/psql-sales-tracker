import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.Arrays;
import java.sql.Timestamp;
import java.util.Date;
import java.text.DateFormat;

public class GadgetTest {
  private Gadget testGadget;
  private Gadget anotherGadget;

  @Before
  public void initialize() {
    testGadget = new Gadget("Robot", 1787.00f, true);
    anotherGadget = new Gadget("Electric Bike", 2382.00f, false);
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void gadget_instantiatesCorrectly_true() {
    assertEquals(true, testGadget instanceof Gadget);
  }

  @Test
  public void Gadget_instantiatesWithName_String() {
    assertEquals("Robot", testGadget.getName());
  }

  @Test
  public void Gadget_instantiatesWithWireless_Boolean(){
    assertEquals(true, testGadget.getWireless());
  }


  @Test
  public void Gadget_instantiatesPrice_float(){
    assertEquals(1787.00f, testGadget.getPrice(), 0);
  }

  @Test
  public void equals_returnsTrueIfNameAndPriceAreSame_true() {
    Gadget sameGadget = new Gadget("Robot", 1787.00f, true);
    assertTrue(testGadget.equals(sameGadget));
  }

  @Test
  public void save_successfullyAddsGadgetToDatabase_List() {
    testGadget.save();
    assertTrue(Gadget.all().get(0).equals(testGadget));
  }

  @Test
  public void save_assignsIdToGadget() {
    testGadget.save();
    Gadget savedGadget = Gadget.all().get(0);
    assertEquals(savedGadget.getId(), testGadget.getId());
  }

  @Test
  public void all_returnsAllInstancesOfGadget_true() {
    testGadget.save();
    anotherGadget.save();
    assertEquals(true, Gadget.all().get(0).equals(testGadget));
    assertEquals(true, Gadget.all().get(1).equals(anotherGadget));
  }

  @Test
  public void find_returnsGadgetWithSameId_anotherGadget() {
    testGadget.save();
    anotherGadget.save();
    assertEquals(Gadget.find(anotherGadget.getId()), anotherGadget);
  }

  @Test
  public void gadget_instantiatesWithMaxInventoryQuantity(){
    assertEquals(testGadget.getInventoryQuantity(), (Gadget.MAX_INVENTORYQUANTITY));
  }

  @Test
  public void gadget_instantiatesWithQuantitySold(){
    assertEquals(testGadget.getQuantitySold(), 0);
  }

  @Test
  public void sell_lowersInvetoryAndIncreasesSales_(){
    testGadget.sell();
    assertEquals(testGadget.getInventoryQuantity(), (Gadget.MAX_INVENTORYQUANTITY - 1));
    assertEquals(testGadget.getQuantitySold(), 1);
  }

  @Test
  public void stockInventory_increaseInventoryQuantity(){
    testGadget.sell();
    testGadget.stockInventory();
    assertEquals(testGadget.getInventoryQuantity(), Gadget.MAX_INVENTORYQUANTITY);
  }

  @Test
  public void gadget_inventoryQuantityCannotGoBeyondMaxValue(){
    for(int i = Gadget.MIN_ALL; i <= (Gadget.MAX_INVENTORYQUANTITY); i++){
      try {
        testGadget.stockInventory();
      } catch (UnsupportedOperationException exception){ }
    }
    assertTrue(testGadget.getInventoryQuantity() <= Gadget.MAX_INVENTORYQUANTITY);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void gadget_throwsExceptionIfInventoryQuantityIsAtMaxValue(){
    for(int i = Gadget.MIN_ALL; i <= (Gadget.MAX_INVENTORYQUANTITY); i++){
      testGadget.stockInventory();
    }
  }

  @Test
  public void save_assignsIdToObject() {
    testGadget.save();
    Gadget savedGadget = Gadget.all().get(0);
    assertEquals(testGadget.getId(), savedGadget.getId());
  }


  @Test
  public void getSalesHistory_retrievesAllSalesHistoryFromDatabase_saleHistoryList() {
    testGadget.save();
    SalesHistory firstSalesHistory = new SalesHistory(testGadget.getId(), 1, 1, 5.00f);
    firstSalesHistory.save();
    SalesHistory secondSalesHistory = new SalesHistory(testGadget.getId(), 2, 2, 10.00f);
    secondSalesHistory.save();
    SalesHistory[] salesHistory = new SalesHistory[] { firstSalesHistory, secondSalesHistory };
    assertTrue(testGadget.getSalesHistory().containsAll(Arrays.asList(salesHistory)));
  }

  @Test
  public void searchByName_retrieveAllProductsByName(){
    testGadget.save();
    anotherGadget.save();
    assertEquals("Robot", Gadget.searchByName("Robot").get(0).getName());
  }

}

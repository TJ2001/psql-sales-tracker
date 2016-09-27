import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.sql.Timestamp;
import java.util.Date;
import java.text.DateFormat;

public class SalesHistoryTest {
  private SalesHistory testSalesHistory;
  private SalesHistory anotherSalesHistory;

  @Before
  public void initialize() {
    testSalesHistory = new SalesHistory(1, 1, 1, 5.00f);
    anotherSalesHistory = new SalesHistory(2, 2, 2, 10.00f);
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void salesHistory_instantiatesCorrectly_true() {
    assertEquals(true, testSalesHistory instanceof SalesHistory);
  }

  @Test
  public void SalesHistory_instantiatesWithProductId_int() {
    assertEquals(1, testSalesHistory.getProductId());
  }

  @Test
  public void SalesHistory_instantiatesWithCustomerId_int(){
    assertEquals(1, testSalesHistory.getCustomerId());
  }

  @Test
  public void SalesHistory_instantiatesWithQuantityBought_int(){
    assertEquals(1, testSalesHistory.getQuantityBought());
  }

  @Test
  public void SalesHistory_instantiatesWithTotalSales_float(){
    assertEquals(5.00f, testSalesHistory.getTotalSales(), 0);
  }

  @Test
  public void equals_returnsTrueIfProductIdCustomerIdQuantityBoughtTotalSalesAreSame_true() {
    SalesHistory sameSalesHistory = new SalesHistory(1, 1, 1, 5.00f);
    assertTrue(testSalesHistory.equals(sameSalesHistory));
  }

  @Test
  public void save_successfullyAddsSalesHistoryToDatabase_List() {
    testSalesHistory.save();
    assertTrue(SalesHistory.all().get(0).equals(testSalesHistory));
  }

  @Test
  public void save_assignsIdToSalesHistory() {
    testSalesHistory.save();
    SalesHistory savedSalesHistory = SalesHistory.all().get(0);
    assertEquals(savedSalesHistory.getId(), testSalesHistory.getId());
  }

  @Test
  public void save_recordsTimeOfSaleInDatabase() {
    testSalesHistory.save();
    Timestamp savedSalesHistory = SalesHistory.find(testSalesHistory.getId()).getSaleDate();
    Timestamp rightNow = new Timestamp(new Date().getTime());
    assertEquals(rightNow.getDay(), savedSalesHistory.getDay());
  }

  @Test
  public void all_returnsAllInstancesOfSalesHistory_true() {
    testSalesHistory.save();
    anotherSalesHistory.save();
    assertEquals(true, SalesHistory.all().get(0).equals(testSalesHistory));
    assertEquals(true, SalesHistory.all().get(1).equals(anotherSalesHistory));
  }

  @Test
  public void find_returnsSalesHistoryWithSameId_anotherSalesHistory() {
    testSalesHistory.save();
    anotherSalesHistory.save();
    assertEquals(SalesHistory.find(anotherSalesHistory.getId()), anotherSalesHistory);
  }

  @Test
  public void getHistoryWithinDays_returnsSalesHistoryWithinDays() {
    testSalesHistory.save();
    anotherSalesHistory.save();
    assertEquals(SalesHistory.all(), SalesHistory.getHistoryWithinDays("30"));
  }

}

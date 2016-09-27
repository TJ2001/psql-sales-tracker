import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.Arrays;

public class CustomerTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void customer_instantiatesCorrectly_true() {
    Customer testCustomer = new Customer("Henry", "henry@henry.com");
    assertEquals(true, testCustomer instanceof Customer);
  }

  @Test
  public void getName_customerInstantiatesWithName_Henry() {
    Customer testCustomer = new Customer("Henry", "henry@henry.com");
    assertEquals("Henry", testCustomer.getName());
  }

  @Test
  public void getName_customerInstantiatesWithEmail_String() {
    Customer testCustomer = new Customer("Henry", "henry@henry.com");
    assertEquals("henry@henry.com", testCustomer.getEmail());
  }

  @Test
  public void equals_returnsTrueIfNameAndEmailAreSame_true() {
    Customer testCustomer = new Customer("Henry", "henry@henry.com");
    Customer anotherCustomer = new Customer("Henry", "henry@henry.com");
    assertTrue(testCustomer.equals(anotherCustomer));
  }

  @Test
  public void save_insertsObjectIntoDatabase_Customer() {
    Customer testCustomer = new Customer("Henry", "henry@henry.com");
    testCustomer.save();
    assertEquals(true, Customer.all().get(0).equals(testCustomer));
  }

  @Test
  public void all_returnsAllInstancesOfCustomer_true() {
    Customer firstCustomer = new Customer("Henry", "henry@henry.com");
    firstCustomer.save();
    Customer secondCustomer = new Customer("Harriet", "harriet@harriet.com");
    secondCustomer.save();
    assertEquals(true, Customer.all().get(0).equals(firstCustomer));
    assertEquals(true, Customer.all().get(1).equals(secondCustomer));
  }

  @Test
  public void save_assignsIdToObject() {
    Customer testCustomer = new Customer("Henry", "henry@henry.com");
    testCustomer.save();
    Customer savedCustomer = Customer.all().get(0);
    assertEquals(testCustomer.getId(), savedCustomer.getId());
  }

  @Test
  public void getSalesHistorys_retrievesAllSalesHistorysFromDatabase_saleHistoryList() {
    Customer firstCustomer = new Customer("Henry", "henry@henry.com");
    firstCustomer.save();
    SalesHistory firstSalesHistory = new SalesHistory(1, firstCustomer.getId(), 1, 5.00f);
    firstSalesHistory.save();
    SalesHistory secondSalesHistory = new SalesHistory(2, firstCustomer.getId(), 2, 10.00f);
    secondSalesHistory.save();
    SalesHistory[] salesHistory = new SalesHistory[] { firstSalesHistory, secondSalesHistory };
    assertTrue(firstCustomer.getSalesHistory().containsAll(Arrays.asList(salesHistory)));
  }
}

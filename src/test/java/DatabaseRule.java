import org.junit.rules.ExternalResource;
import org.sql2o.*;

public class DatabaseRule extends ExternalResource {

  @Override
  protected void before() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/sales_trackers_test", null, null);
  }

  @Override
  protected void after() {
    try(Connection con = DB.sql2o.open()) {
      String deleteProductsQuery = "DELETE FROM products *;";
      String deleteCustomersQuery = "DELETE FROM customers *;";
      String deleteSalesHistoryQuery = "DELETE FROM sales_history *;";
      con.createQuery(deleteProductsQuery).executeUpdate();
      con.createQuery(deleteCustomersQuery).executeUpdate();
      con.createQuery(deleteSalesHistoryQuery).executeUpdate();
    }
  }

}

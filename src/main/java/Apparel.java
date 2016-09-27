import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;
import java.sql.Timestamp;

public class Apparel extends Product {
  private String size;


  public Apparel(String name, float price, String size) {
    this.name = name;
    this.price = price;
    this.inventoryQuantity = MAX_INVENTORYQUANTITY;
    this.quantitySold = 0;
    this.size = size;
  }

  public String getSize(){
    return size;
  }

  public static List<Apparel> all() {
    String sql = "SELECT * FROM products;";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
      .throwOnMappingFailure(false)
      .executeAndFetch(Apparel.class);
    }
  }

  public static Apparel find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM products where id=:id";
      Apparel product = con.createQuery(sql)
        .addParameter("id", id)
        .throwOnMappingFailure(false)
        .executeAndFetchFirst(Apparel.class);
    return product;
    }
  }

  public List<SalesHistory> getSalesHistory() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM sales_history where productId=:id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(SalesHistory.class);
    }
  }

  public static List<Apparel> searchByName(String name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM products WHERE name LIKE '" + name + "'";
      return con.createQuery(sql)
        .throwOnMappingFailure(false)
        .executeAndFetch(Apparel.class);
    }

  }
}

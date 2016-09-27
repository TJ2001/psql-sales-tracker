import java.util.List;
import java.util.Timer;
import org.sql2o.*;
import java.util.Arrays;

public class Gadget extends Product {
  private Boolean wireless;


  public Gadget(String name, float price, Boolean wireless) {
    this.name = name;
    this.price = price;
    this.inventoryQuantity = MAX_INVENTORYQUANTITY;
    this.quantitySold = 0;
    this.wireless = wireless;
  }

  public Boolean getWireless(){
    return wireless;
  }

  public static List<Gadget> all() {
    String sql = "SELECT * FROM products;";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
      .throwOnMappingFailure(false)
      .executeAndFetch(Gadget.class);
    }
  }

  public static Gadget find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM products where id=:id";
      Gadget product = con.createQuery(sql)
        .addParameter("id", id)
        .throwOnMappingFailure(false)
        .executeAndFetchFirst(Gadget.class);
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

  public static List<Gadget> searchByName(String name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM products WHERE name LIKE '" + name + "'";
      return con.createQuery(sql)
        .throwOnMappingFailure(false)
        .executeAndFetch(Gadget.class);
    }

  }
}

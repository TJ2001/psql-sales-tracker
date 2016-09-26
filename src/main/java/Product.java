import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;
import java.sql.Timestamp;

public class Product{
  private String name;
  private int price;
  private int inventoryQuantity;
  private int quantitySold;
  private int id;

  public static final int MAX_INVENTORYQUANTITY_LEVEL = 30;
  private static final int MAX_QUANTITYSOLD_LEVEL = 30;
  private static final int MIN_ALL_LEVELS = 0;

  public Product(String name, int price) {
    this.name = name;
    this.price = price;
    this.inventoryQuantity = MAX_INVENTORYQUANTITY_LEVEL;
    this.quantitySold = 0;
  }

  public String getName() {
    return name;
  }

  public int getPrice() {
    return price;
  }

  public int getInventoryQuantity () {
    return inventoryQuantity;
  }

  public int getQuantitySold () {
    return quantitySold;
  }

  public int getId() {
    return id;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO products (name, price) VALUES (:name, :price)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("price", this.price)
        .executeUpdate()
        .getKey();
    }
  }
}

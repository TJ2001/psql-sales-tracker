import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;
import java.sql.Timestamp;

public class Item {
  private int id;
  private int productId;
  private int customerId;
  private Timestamp dateSold;

  public Item(int productId, int customerId) {
    this.productId = productId;
    this.customerId = customerId;
  }

  public int getProductId() {
    return productId;
  }

  public int getCustomerId() {
    return customerId;
  }

  public Timestamp getDateSold() {
    return dateSold;
  }

  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object otherItem) {
    if (!(otherItem instanceof Item)) {
      return false;
    } else {
      Item newItem = (Item) otherItem;
      return this.getProductId() == newItem.getProductId() &&
        this.getCustomerId() == newItem.getCustomerId();
//if this code is broken, try putting timestamp with ==
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO items (productId, customerId, dateSold) VALUES (:productId, :customerId, now())";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("productId", this.productId)
        .addParameter("customerId", this.customerId)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Item> all() {
    String sql = "SELECT * FROM items";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Item.class);
    }
  }

  public static Item find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM items where id=:id";
      Item item = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Item.class);
      return item;
    }
  }
}

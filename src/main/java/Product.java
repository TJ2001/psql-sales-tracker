import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;
import java.sql.Timestamp;

public class Product{
  private String name;
  private double price;
  private int inventoryQuantity;
  private int quantitySold;
  private int id;

  public static final int MAX_INVENTORYQUANTITY_LEVEL = 30;
  private static final int MAX_QUANTITYSOLD_LEVEL = 30;
  private static final int MIN_ALL_LEVELS = 0;

  public Product(String name, double price) {
    this.name = name;
    this.price = price;
    this.inventoryQuantity = MAX_INVENTORYQUANTITY_LEVEL;
    this.quantitySold = 0;
  }

  public String getName() {
    return name;
  }

  public double getPrice() {
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

  // public void save() {
  //   try(Connection con = DB.sql2o.open()) {
  //     String sql = "INSERT INTO products (name, price) VALUES (:name, :price)";
  //     this.id = (int) con.createQuery(sql, true)
  //       .addParameter("name", this.name)
  //       .addParameter("price", this.price)
  //       .executeUpdate()
  //       .getKey();
  //   }
  // }

  public void sell(){
    inventoryQuantity--;
    quantitySold++;
  }

  public void stockInventory(){
    if (inventoryQuantity >= MAX_INVENTORYQUANTITY_LEVEL){
      throw new UnsupportedOperationException("You cannot buy more inventory!");
      }
    inventoryQuantity++;
  }

  public boolean isInStock() {
    if (inventoryQuantity <= MIN_ALL_LEVELS ) {
      return false;
    }
    return true;
  }

  @Override
  public boolean equals(Object otherProduct) {
    if (!(otherProduct instanceof Product)) {
      return false;
    } else {
      Product newProduct = (Product) otherProduct;
      return this.getName().equals(newProduct.getName()) &&
        this.getPrice() == newProduct.getPrice();
    }
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

  public static List<Product> all() {
    String sql = "SELECT id, name, price FROM products";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Product.class);
    }
  }

  public List<Item> getItem() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM items where productId=:id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Item.class);
    }
  }
}

import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;
import java.sql.Timestamp;

public abstract class Product{
  public String name;
  public Float price;
  public int inventoryQuantity;
  public int quantitySold;
  public int id;

  public static final int MAX_INVENTORYQUANTITY = 30;
  public static final int MAX_QUANTITYSOLD = 30;
  public static final int MIN_ALL = 0;

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

  public void sell(){
    inventoryQuantity--;
    quantitySold++;
  }

  public void stockInventory(){
    if (inventoryQuantity >= MAX_INVENTORYQUANTITY){
      throw new UnsupportedOperationException("You cannot buy more inventory!");
      }
    inventoryQuantity++;
  }

  public boolean isInStock() {
    if (inventoryQuantity <= MIN_ALL ) {
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

}

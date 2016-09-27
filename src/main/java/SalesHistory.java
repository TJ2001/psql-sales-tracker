import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;
import java.sql.Timestamp;

public class SalesHistory {
  private int id;
  private Timestamp saleDate;
  private int productId;
  private int customerId;
  private int quantityBought;
  private float totalSales;

  public SalesHistory(int productId, int customerId, int quantityBought, float totalSales) {
    this.productId = productId;
    this.customerId = customerId;
    this.quantityBought = quantityBought;
    this.totalSales = totalSales;
  }

  public int getProductId() {
    return productId;
  }

  public int getCustomerId() {
    return customerId;
  }

  public int getQuantityBought() {
    return quantityBought;
  }

  public float getTotalSales() {
    return totalSales;
  }

  public Timestamp getSaleDate() {
    return saleDate;
  }

  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object otherSalesHistory){
    if (!(otherSalesHistory instanceof SalesHistory)) {
      return false;
    } else {
      SalesHistory newSalesHistory = (SalesHistory) otherSalesHistory;
      return this.getProductId() == newSalesHistory.getProductId() &&
             this.getCustomerId() == newSalesHistory.getCustomerId() &&
             this.getQuantityBought() == newSalesHistory.getQuantityBought() &&
             this.getTotalSales() == newSalesHistory.getTotalSales();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO sales_history (productid, customerid, quantitybought, totalsales, saledate) VALUES (:productid, :customerid, :quantitybought, :totalsales, now())";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("productid", this.productId)
        .addParameter("customerid", this.customerId)
        .addParameter("quantitybought", this.quantityBought)
        .addParameter("totalsales", this.totalSales)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<SalesHistory> all() {
    String sql = "SELECT * FROM sales_history";
    try(Connection con = DB.sql2o.open()) {
     return con.createQuery(sql).executeAndFetch(SalesHistory.class);
    }
  }

  public static SalesHistory find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM sales_history where id=:id";
      SalesHistory salesHistory = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(SalesHistory.class);
    return salesHistory;
    }
  }

  public static List<SalesHistory> getHistoryWithinDays(String numberOfDays) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM sales_history WHERE saledate > (CURRENT_DATE - INTERVAL '" + numberOfDays + " days')";
      return con.createQuery(sql)
        .executeAndFetch(SalesHistory.class);
    }
  }
}

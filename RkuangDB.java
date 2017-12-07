import java.sql.*;
import java.util.*;

public class RkuangDB {
  final String HOST = "jdbc:mysql://cs174a.engr.ucsb.edu:3306/rkuangDB";
  final String USER = "rkuang";
  final String PWD = "994";

  Connection connection;

  public RkuangDB() {
    getDriver();
    getConnection();
  }

  private void getDriver() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void getConnection() {
    try {
      connection = DriverManager.getConnection(HOST, USER, PWD);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Boolean login(String username, String password) {
    String query = String.format("SELECT * FROM Customers WHERE username='%s' AND password='%s'", username, password);

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      if (rs.next()) {
        StarsRUs.activeUser.name = rs.getString("name");
        StarsRUs.activeUser.taxid = rs.getString("taxid");
        StarsRUs.activeUser.admin = rs.getBoolean("admin");
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }
  public Boolean register(String name, String user, String pass, String addr, String state, String phNum, String email, String taxID, String ssn){
    String queryCustomer = String.format("INSERT INTO Customers VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s',false)", name, user, pass, addr, state, phNum, email, taxID, ssn);
    try(Statement statement = connection.createStatement()){
      statement.executeUpdate(queryCustomer);
      return true;
    }catch(SQLException e){
      e.printStackTrace();
    }
    return false;
  }

  public void createMarketAccount(String taxid, double deposit){
    String queryMarket = String.format("INSERT INTO Market_Accounts VALUES ('%s', %f)", taxid, deposit);
    try(Statement statement = connection.createStatement()){
      statement.executeUpdate(queryMarket);
    }catch(SQLException e){
      e.printStackTrace();
    }
  }

  public void createStockAccount(String taxid) {
    String query = String.format("INSERT INTO Stock_Accounts VALUES ('%s', 0, 0)", taxid);
    try(Statement statement = connection.createStatement()){
      statement.executeUpdate(query);
    }catch(SQLException e){
      e.printStackTrace();
    }
  }

  public Boolean updateBalance(double amount) {
    String query = String.format("SELECT balance FROM Market_Accounts WHERE taxid='%s'", StarsRUs.activeUser.taxid);

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      if (rs.next()) {
        double balance = rs.getDouble("balance");
        double newBalance = balance + amount;
        if (newBalance > 0) {
          query = String.format("UPDATE Market_Accounts SET balance='%f' WHERE taxid='%s'", newBalance, StarsRUs.activeUser.taxid);
          statement.executeUpdate(query);
          System.out.println("Balance is now $" + newBalance);
          return true;
        } else {
          System.out.println("Transaction failed. Market Account balance cannot fall below $0");
          return false;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  private void addToStockBalance(String stockid, double price, double quantity) {
    String query = String.format("SELECT * FROM Stock_Balance WHERE taxid='%s' AND stockid='%s' AND buyingprice=%f", StarsRUs.activeUser.taxid, stockid, price);

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      if (rs.next()) {
        double oldQuantity = rs.getInt("quantity");
        double newQuantity = oldQuantity + quantity;
        query = String.format("UPDATE Stock_Balance SET quantity=%f WHERE taxid='%s' AND stockid='%s' AND buyingprice=%f", newQuantity, StarsRUs.activeUser.taxid, stockid, price);
      } else {
        query = String.format("INSERT INTO Stock_Balance VALUES ('%s', '%s', %f, %f)", StarsRUs.activeUser.taxid, stockid, price, quantity);
      }
      statement.executeUpdate(query);
      System.out.println(String.format("%f shares of %s purchased at $%f each", quantity, stockid, price));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // TODO can be refactored still for sellStocks
  private void updateSharesTraded(double quantity) {
    String query = String.format("SELECT * FROM Stock_Accounts WHERE taxid='%s'", StarsRUs.activeUser.taxid);

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      if (rs.next()) {
        double oldSharesTraded = rs.getDouble("shares_traded");
        double newSharesTraded = oldSharesTraded + quantity;
        query = String.format("UPDATE Stock_Accounts SET shares_traded=%f WHERE taxid='%s'", newSharesTraded, StarsRUs.activeUser.taxid);
      } else {
        query = String.format("INSERT INTO Stock_Accounts VALUES ('%s', 0, %f)", StarsRUs.activeUser.taxid, quantity);
      }
      statement.executeUpdate(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Boolean buyStocks(String stockid, double quantity) {
    String query = String.format("SELECT * FROM Stocks WHERE stockid='%s'", stockid);

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      if (rs.next()) {
        double price = rs.getDouble("currentprice");
        double cost = -1 * (price*quantity + 20);
        if (updateBalance(cost)) {
          addToStockBalance(stockid, price, quantity);

          updateSharesTraded(quantity);
        }
        return true;
      } else {
        System.out.println(String.format("'%s' is not a valid Stock ID", stockid));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    // this should never happen
    return false;
  }

  public void sellStocks(String stockid) {
    showStockBalance();
    String query = String.format("SELECT * FROM Stock_Balance WHERE taxid='%s' AND stockid='%s'", StarsRUs.activeUser.taxid, stockid);

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      while (rs.next()) {
        double buyingprice = rs.getDouble("buyingprice");
        double quantity = rs.getDouble("quantity");

        Scanner in = new Scanner(System.in);
        System.out.print(String.format("How many shares of %s at $%.2f would you like to sell?", stockid, buyingprice));
        in.next();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void showStockBalance() {
    String query = String.format("SELECT * FROM Stock_Balance WHERE taxid='%s'", StarsRUs.activeUser.taxid);

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);

      System.out.println("You own:");
      while (rs.next()) {
        String stockid = rs.getString("stockid");
        double buyingprice = rs.getDouble("buyingprice");
        double quantity = rs.getDouble("quantity");

        System.out.println(String.format("    %.3f shares of %s purchased at $%.2f", quantity, stockid, buyingprice));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Boolean showBalance() {
    String query = String.format("SELECT * FROM Market_Accounts WHERE taxid='%s'", StarsRUs.activeUser.taxid);

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      if (rs.next()) {
        System.out.println("Market Account Balance:  $"+rs.getDouble("balance"));
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    // this should never happen
    return false;
  }

  public Boolean getStockInfo(String stockid) {
    String query = String.format("SELECT * FROM Stocks WHERE stockid='%s'", stockid);

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      if (rs.next()) {
        double price = rs.getDouble("currentprice");
        String name = rs.getString("name");
        String date = rs.getDate("dob").toString();

        System.out.println("StockID\tCurrent Price\tName\tDate of Birth");
        System.out.println(stockid+"\t"+price+"\t"+name+"\t"+date);

        // TODO print movie contracts
      } else {
        System.out.println(String.format("'%s' is not a valid Stock ID", stockid));
      }
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    // this should never happen
    return false;
  }

  public void setMarket(Boolean isOpen){
    String query = String.format("UPDATE Market SET open = %b", isOpen);
    try(Statement statement = connection.createStatement()){
      statement.executeUpdate(query);
    }catch (SQLException e){
      e.printStackTrace();
    }
    return;
  }

  public void setStockPrice(String stockid, double newprice) {
    String query = String.format("UPDATE Stocks SET currentprice=%f WHERE stockid='%s'", newprice, stockid);

    try (Statement statement = connection.createStatement()) {
      statement.executeUpdate(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void getDate() {
    String query = String.format("SELECT * FROM Market");
    try (Statement statement = connection.createStatement()){
      ResultSet rs = statement.executeQuery(query);
      if(rs.next()){
        String today = rs.getString("date");
        System.out.println("Today's date is: " + today);
      }
    } catch (SQLException e){
        e.printStackTrace();
      }
  }

  public void setDate(String date){
    String query = String.format("UPDATE Market SET date = '%s'", date);
    try(Statement statement = connection.createStatement()){
      statement.executeUpdate(query);
    } catch (SQLException e){
      e.printStackTrace();
    }
    return;
  }

  public void closeConnection() {
    try {
      if (connection != null) {
        connection.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}

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
        StarsRUs.activeUser.taxid = rs.getInt("taxid");
        StarsRUs.activeUser.admin = rs.getBoolean("admin");
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }
  public Boolean register(String name, String user, String pass, String addr, String state, String phNum, String email, String taxID, String ssn){
    String queryAccount = String.format("INSERT INTO Accounts(taxid)VALUES('%s')",taxID);
    String queryCustomer = String.format("INSERT INTO Customers(name, username, password, address, state, phone, email, taxid, ssn, admin)VALUES('%s','%s','%s','%s','%s','%s','%s','%s','%s',false)", name, user, pass, addr, state, phNum, email, taxID, ssn);
    try(Statement statement = connection.createStatement()){
      statement.executeUpdate(queryAccount);
      statement.executeUpdate(queryCustomer);
      return true;
    }catch(SQLException e){
      e.printStackTrace();
    }
    return false;
  }
  public void createMarketAccount(String taxid, double deposit){
    String queryMarket = String.format("INSERT INTO Market_Accounts(taxid, balance)VALUES('%s', '%f')", taxid, deposit);
    try(Statement statement = connection.createStatement()){
      statement.executeUpdate(queryMarket);
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
          query = String.format("UPDATE Market_Accounts SET balance='%f' WHERE taxid='%d'", newBalance, StarsRUs.activeUser.taxid);
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

  public Calendar getDate() {
    String query = String.format("SELECT * FROM Dates");
    try (Statement statement = connection.createStatement()){
      ResultSet rs = statement.executeQuery(query);
      Calendar today = Calendar.getInstance();
      if(rs.next()){
        today = rs.getDate(0);
      }
    } catch (SQLException e){
        e.printStackTrace();
      }
    return today;
  }

  public void setDate(String date){
    String query = String.format("UPDATE Dates SET date='%s',", date);
    try(Statement statement = connection.createStatement()){
      ResultSet rs = statement.updateQuery(query);
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

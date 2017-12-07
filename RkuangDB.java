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
    String queryCustomer = String.format("INSERT INTO Customers(name, username, password, address, state, phone, email, taxid, ssn, admin)VALUES('%s','%s','%s','%s','%s','%s','%s','%s','%s',false)", name, user, pass, addr, state, phNum, email, taxID, ssn);
    try(Statement statement = connection.createStatement()){
      statement.executeUpdate(queryCustomer);
      return true;
    }catch(SQLException e){
      e.printStackTrace();
    }
    return false;
  }

  public void createMarketAccount(String taxid, double deposit){
    String queryMarket = String.format("INSERT INTO Market_Accounts(taxid, balance)VALUES('%s', '%f')", taxid, deposit);
    String queryInterest = String.format("INSERT INTO Interest VALUES('%s', '%f', 0)", taxid, deposit);
    try(Statement statement = connection.createStatement()){
      statement.executeUpdate(queryMarket);
      statement.executeUpdate(queryInterest);
    }catch(SQLException e){
      e.printStackTrace();
    }
  }

  public void createStockAccount(String taxid) {
    String query = String.format("INSERT INTO Stock_Accounts VALUES ('%s', 0)", taxid);
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

  public Double getBalance() {
    String query = String.format("SELECT * FROM Market_Accounts WHERE taxid='%s'", StarsRUs.activeUser.taxid);
    Double x = 1.0;
    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      if (rs.next()) {
        System.out.println("Market Account Balance:  $"+rs.getDouble("balance"));
        x = rs.getDouble("balance");
        return x;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    // this should never happen
    return x;
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

  public String getDate() {
    String query = String.format("SELECT * FROM Market");
    String today = "";
    try (Statement statement = connection.createStatement()){
      ResultSet rs = statement.executeQuery(query);
      if(rs.next()){
        today = rs.getString("date");
        System.out.println("Today's date is: " + today);
      }
    } catch (SQLException e){
        e.printStackTrace();
      }
      return today;
  }

  public void setDate(String date){
    this.updateInterest(date);
    String query = String.format("UPDATE Market SET date = '%s'", date);
    try(Statement statement = connection.createStatement()){
      statement.executeUpdate(query);
    } catch (SQLException e){
      e.printStackTrace();
    }
    return;
  }

  public void updateInterest(int future){
    int past = this.dayToInt(this.getDate());
    int days = future - past + 1;
    String query = String.format("SELECT i.taxid,i.currentBal from Market_Accounts m, Interest i WHERE m.taxid = i.taxid AND m.balance = i.currentBal");
    try(Statement statement = connection.createStatement()){
      ResultSet rs = statement.executeQuery(query);
      while (rs.next()){
        query = String.format("UPDATE Interest SET daysHeld = '%d'", rs.getInt("days")+days);
        statement.executeUpdate(query);
      }
      query = String.format("SELECT m.taxid,m.currentBal from Market_Accounts m, Interest i WHERE m.taxid = i.taxid AND m.balance <> i.currentBal");
      rs = statement.executeQuery(query);
      while (rs.next()){
        query = String.format("INSERT INTO Interest (taxid, currentBal, daysHeld) VALUES ('%s','%f', '%d')", rs.getString("taxid"), rs.getDouble("balance"), days);
        statement.executeUpdate(query);
      }
    } catch(SQLException e){
      e.printStackTrace();
    }
  }

  public void calcInterest(){
    return;
  }

  public int dayToInt(String date){
    date.replace("-","");
    date.toCharArray();
    int temp = Integer.parseInt(date[2]+date[3]);
    return temp;
  }

  public Boolean setMarket(Boolean isOpen){
    String query = String.format("SELECT open FROM Market");
    try(Statement statement = connection.createStatement()){
      ResultSet rs = statement.executeQuery(query);
      while(rs.next()){
        if(rs.getBoolean("open") == isOpen){
          return false;
        }
        else{
          query = String.format("UPDATE Market SET open = %b", isOpen);
            statement.executeUpdate(query);
            return true;
          }
        }
      }catch (SQLException e){
      e.printStackTrace();
    }
    return false;
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

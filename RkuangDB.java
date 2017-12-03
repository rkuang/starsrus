import java.sql.*;

public class RkuangDB {
  final String HOST = "jdbc:mysql://cs174a.engr.ucsb.edu:3306/rkuangDB";
  final String USER = "rkuang";
  final String PWD = "994";

  Connection connection;
  Statement statement;

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
    String queryMarket = String.format("INSERT INTO Market_Accounts(taxid, balance)VALUES('%s', 10000)",taxID);
    try(Statement statement = connection.createStatement()){
      statement.executeUpdate(queryAccount);
      statement.executeUpdate(queryCustomer);
      statement.executeUpdate(queryMarket);
      return true;
    }catch(SQLException e){
      e.printStackTrace();
    }
    return false;
  }

  public double getBalance() {
    String query = String.format("SELECT * FROM Market_Accounts WHERE taxid='%s'", StarsRUs.activeUser.taxid);

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      if (rs.next()) {
        return rs.getDouble("balance");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    // this should never happen
    return -42;
  }

  public void closeConnection() {
    try {
      if (statement != null) {
        statement.close();
      }

      if (connection != null) {
        connection.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}

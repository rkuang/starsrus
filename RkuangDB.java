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

  public int getBalance() {
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
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}

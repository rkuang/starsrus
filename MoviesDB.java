import java.sql.*;

public class MoviesDB {
  final String HOST = "jdbc:mysql://cs174a.engr.ucsb.edu:3306/moviesDB";
  final String USER = "rkuang";
  final String PWD = "994";

  Connection connection;
  Statement statement;

  public MoviesDB() {
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

  public void getMovieInfo(String title) {
    String query = "SELECT * FROM Movies WHERE title="+title;

    try (Statement statement = connection.createStatement()) {

      ResultSet rs = statement.executeQuery(query);

      System.out.println("ID\tTitle\tRating\tProduction Year");

      while (rs.next()) {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        double rating = rs.getDouble("rating");
        int year = rs.getInt("production_year");

        System.out.println(id + "\t" + title + "\t" + rating + "\t" + year);

      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void closeConnection() {
    try {
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
}

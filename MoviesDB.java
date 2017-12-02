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

  public void getMovieInfo(String name) {
    String query = String.format("SELECT * FROM Movies WHERE title='%s'", name);

    try (Statement statement = connection.createStatement()) {

      ResultSet rs = statement.executeQuery(query);

      while (rs.next()) {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        double rating = rs.getDouble("rating");
        int year = rs.getInt("production_year");

        String tab = "";
        for (int i = 0; i < title.length()/8+1; i++) {
          tab += "\t";
        }

        System.out.println("ID\tTitle"+tab+"Rating\tProduction Year");
        System.out.println(id + "\t" + title + "\t" + rating + "\t" + year);

      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void getTopMovies(int from, int to) {
    String query = String.format("SELECT * FROM Movies WHERE production_year>=%d AND production_year<=%d", from, to);

    try (Statement statement = connection.createStatement()) {

      ResultSet rs = statement.executeQuery(query);

      while (rs.next()) {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        double rating = rs.getDouble("rating");
        int year = rs.getInt("production_year");

        String tab = "";
        for (int i = 0; i < title.length()/8+1; i++) {
          tab += "\t";
        }

        System.out.println("ID\tTitle"+tab+"Rating\tProduction Year");
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

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

      System.out.println("ID\tTitle\tRating\tProduction Year");
      while (rs.next()) {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        double rating = rs.getDouble("rating");
        int year = rs.getInt("production_year");

        // String tab = "";
        // for (int i = 0; i < title.length()/8+1; i++) {
        //   tab += "\t";
        // }
        //
        // System.out.println("ID\tTitle"+tab+"Rating\tProduction Year");
        System.out.println(id + "\t" + title + "\t" + rating + "\t" + year);

      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void getTopMovies(int from, int to) {
    String query = String.format("SELECT * FROM Movies WHERE rating=5 AND production_year>=%d AND production_year<=%d", from, to);

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

  public void getMovieReviews(String name) {
    String query = String.format("SELECT * FROM Movies M, Reviews R  WHERE M.title='%s' AND M.id = R.movie_id", name);

    try (Statement statement = connection.createStatement()) {

      ResultSet rs = statement.executeQuery(query);

      System.out.println("Author\tReview");
      while (rs.next()) {
        int movie_id = rs.getInt("movie_id");
        String author = rs.getString("author");
        String review = rs.getString("review");

        //System.out.println("MOVIE_ID\t"+"Author\tReview");
        System.out.println(author + "\t" + review);
      }

    }
    catch (SQLException e) {
      e.printStackTrace();
    }
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

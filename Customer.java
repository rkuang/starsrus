import java.util.Scanner;
import java.sql.*;

public class Customer {

  public String name;
  public int taxid;
  public Boolean admin;

  public void login() {
    // set current user
    Scanner in = new Scanner(System.in);
    System.out.print("Username:     ");
    String user = in.next();
    System.out.print("Password:     ");
    String pass = in.next();

    if (StarsRUs.rkuangDB.login(user, pass)) {
      StarsRUs.validInputs.setState("loggedIn");
      System.out.println("Login Successful");
      System.out.println("Hello, "+StarsRUs.activeUser.name);
      return;
    }
    System.out.println("Login Failed");
  }

  public void register() {
    Scanner in = new Scanner(System.in);
    System.out.print("Username:     ");
    String user = in.next();
    System.out.print("Password:     ");
    String pass = in.next();
    System.out.print("State:        ");
    String state = in.next();
    System.out.print("Phone Number: ");
    String phone_number = in.next();
    System.out.print("Email:        ");
    String email = in.next();
    System.out.print("Tax ID:       ");
    String taxID = in.next();
    System.out.print("SSN:          ");
    String ssn = in.next();

    // add customer to db
  }

  public void showBalance() {
    System.out.println("Market Account Balance:  $"+StarsRUs.rkuangDB.getBalance());
  }

  public void getMovieInfo() {
    Scanner in = new Scanner(System.in);
    System.out.print("title:  ");
    String title = in.next();
    StarsRUs.moviesDB.getMovieInfo(title);
  }

  public void getTopMovies() {
    Scanner in = new Scanner(System.in);
    System.out.print("from:  ");
    int from = in.nextInt();
    System.out.print("to:    ");
    int to = in.nextInt();
    StarsRUs.moviesDB.getTopMovies(from, to);
  }

  public void getMovieReviews() {
    Scanner in = new Scanner(System.in);
    System.out.print("title:  ");
    String title = in.next();
    StarsRUs.moviesDB.getMovieReviews(title);
  }

  public void logout() {
    StarsRUs.activeUser = new Customer();
    StarsRUs.validInputs.setState("noUser");
    System.out.println("Logout Successful");
  }
}

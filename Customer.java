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
    String user = in.nextLine();
    System.out.print("Password:     ");
    String pass = in.nextLine();

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
    String user = in.nextLine();
    System.out.print("Password:     ");
    String pass = in.nextLine();
    System.out.print("State:        ");
    String state = in.nextLine();
    System.out.print("Phone Number: ");
    String phone_number = in.nextLine();
    System.out.print("Email:        ");
    String email = in.nextLine();
    System.out.print("Tax ID:       ");
    String taxID = in.nextLine();
    System.out.print("SSN:          ");
    String ssn = in.nextLine();

    // add customer to db
  }

  public void showBalance() {
    System.out.println("Market Account Balance:  $"+StarsRUs.rkuangDB.getBalance());
  }

  public void getMovieInfo() {
    Scanner in = new Scanner(System.in);
    System.out.print("title:  ");
    String title = in.nextLine();
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
    String title = in.nextLine();
    StarsRUs.moviesDB.getMovieReviews(title);
  }

  public void logout() {
    StarsRUs.activeUser = new Customer();
    StarsRUs.validInputs.setState("noUser");
    System.out.println("Logout Successful");
  }
}

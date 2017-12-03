import java.util.Scanner;
import java.sql.*;

public class Customer {

  public int taxid;
  public Boolean admin;

  public static void login() {
    // set current user
    Scanner in = new Scanner(System.in);
    System.out.print("Username:     ");
    String user = in.next();
    System.out.print("Password:     ");
    String pass = in.next();

    if (StarsRUs.rkuangDB.login(user, pass)) {
      StarsRUs.validInputs.setState("loggedIn");
      System.out.println("Login Successful");
    }
    System.out.println("Login Failed");
  }

  public static void register() {
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
}

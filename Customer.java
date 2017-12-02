import java.util.Scanner;

public class Customer {

  public String username;
  private String password;

  public Customer(String user, String pass) {
    username = user;
    password = pass;
  }

  public static void login() {
    // set current user
    Scanner in = new Scanner(System.in);
    System.out.print("username:     ");
    String user = in.next();
    System.out.print("password:     ");
    String pass = in.next();
  }

  public static void register() {
    Scanner in = new Scanner(System.in);
    System.out.print("username:     ");
    String user = in.next();
    System.out.print("password:     ");
    String pass = in.next();
    System.out.print("state:        ");
    String state = in.next();
    System.out.print("phone number: ");
    String phone_number = in.next();
    System.out.print("email:        ");
    String email = in.next();
    System.out.print("Tax ID:       ");
    String taxID = in.next();
    System.out.print("SSN:          ");
    String ssn = in.next();

    // add customer to db
  }
}

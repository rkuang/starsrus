import java.util.Scanner;
import java.sql.ResultSet;

public class Customer {

  public static Customer activeUser;

  private int taxid;
  private Boolean admin;

  public static void login() {
    // set current user
    Scanner in = new Scanner(System.in);
    System.out.print("username:     ");
    String user = in.next();
    System.out.print("password:     ");
    String pass = in.next();

    ResultSet rs = StarsRUs.rkuangDB.login(user, pass);
    activeUser.taxid = rs.getString("taxid");
    activeUser.admin = rs.getString("admin");
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

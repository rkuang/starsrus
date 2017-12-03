import java.util.Scanner;
import java.sql.*;

public class Customer {

  public String name;
  public int taxid;
  public Boolean admin;

  public void login() {
    // set current user
    System.out.println("Please Login Below");
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
    System.out.print("Name:    ");
    String name = in.next();
    System.out.print("Username:     ");
    String user = in.next();
    System.out.print("Password:     ");
    String pass = in.next();
    System.out.print("Address:     ");
    String addr = in.next();
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
    if(StarsRUs.rkuangDB.register(name,user,pass,addr,state,phone_number,email,taxID,ssn)){
      System.out.println("Registration Sucessful!");
      System.out.println("Your Username is:" + user);
      System.out.println("Your Password is:" + pass);
      this.login();
    }
    else{
      System.out.println("Registration unsucessful please try again.");
    }


    // add customer to db
  }

  public void logout() {
    StarsRUs.activeUser = new Customer();
    StarsRUs.validInputs.setState("noUser");
    System.out.println("Logout Successful");
  }
}

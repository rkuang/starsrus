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
    System.out.print("Name:    ");
    String name = in.nextLine();
    System.out.print("Username:     ");
    String user = in.nextLine();
    System.out.print("Password:     ");
    String pass = in.nextLine();
    System.out.print("Address:     ");
    String addr = in.nextLine();
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
    if(StarsRUs.rkuangDB.register(name,user,pass,addr,state,phone_number,email,taxID,ssn)){
      System.out.println("Registration Sucessful!");
      System.out.println("Your Username is:" + user);
      System.out.println("Your Password is:" + pass);
      System.out.println("Upon registering a Market Account is automatically opened for you.");
      System.out.println("How much would you like to deposit? Min($1000)");
      double deposit = in.nextLine();
      while(deposit < 1000){
        System.out.println("You're broke af please deposit at least $1000");
        deposit = in.nextLine();
      }
      StarsRUs.rkuangDB.createMarketAccount(taxID, deposit);
      System.out.println("Sucess! Market Account created with balance of " + deposit);
      this.login();
    }
    else{
      System.out.println("Registration unsucessful please try again.");
    }
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

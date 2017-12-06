import java.util.*;
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
      if(admin){
        System.out.println("Logged in as admin");
      }
      else{
        System.out.println("Login Successful");
        System.out.println("Hello, "+StarsRUs.activeUser.name);
      }
      return;
    }
    System.out.println("Login Failed");
  }

  public void register() {
    Scanner in = new Scanner(System.in);
    System.out.print("Name:         ");
    String name = in.nextLine();
    System.out.print("Username:     ");
    String user = in.nextLine();
    System.out.print("Password:     ");
    String pass = in.nextLine();
    System.out.print("Address:      ");
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
      String deposit = in.nextLine();
      double value = Double.parseDouble(deposit.replaceAll("[^\\d.]", ""));
      while(value < 1000){
        System.out.println("You must make an initial deposit of at least $1000");
        deposit = in.nextLine();
        value = Double.parseDouble(deposit.replaceAll("[^\\d.]", ""));
      }
      StarsRUs.rkuangDB.createMarketAccount(taxID, value);
      System.out.println("Success! Market Account created with balance of " + deposit);
      this.login();
    }
    else{
      System.out.println("Registration unsucessful please try again.");
    }
  }

  public void deposit() {
    Scanner in = new Scanner(System.in);
    double amount = -42;
    while (amount < 0) {
      System.out.print("Amount:  ");
      amount = in.nextDouble();
      if (amount < 0) {
        System.out.println("Amount cannot be negative");
      }
    }
    StarsRUs.rkuangDB.updateBalance(amount);
  }

  public void withdraw() {
    Scanner in = new Scanner(System.in);
    double amount = -42;
    while (amount < 0) {
      System.out.print("Amount:  ");
      amount = in.nextDouble();
      if (amount < 0) {
        System.out.println("Amount cannot be negative");
      }
    }
    StarsRUs.rkuangDB.updateBalance(-1 * amount);
  }

  public void buy() {
    // TODO
  }

  public void sell() {
    // TODO
  }

  public void showBalance() {
    StarsRUs.rkuangDB.showBalance();
  }

  public void getTransactionHistory() {
    // TODO
  }

  public void getStockInfo() {
    Scanner in = new Scanner(System.in);
    System.out.print("Stock ID:  ");
    String stockid = in.nextLine();
    StarsRUs.rkuangDB.getStockInfo(stockid);
  }

  public void getMovieInfo() {
    Scanner in = new Scanner(System.in);
    System.out.print("Title:  ");
    String title = in.nextLine();
    StarsRUs.moviesDB.getMovieInfo(title);
  }

  public void getTopMovies() {
    Scanner in = new Scanner(System.in);
    System.out.print("From:  ");
    int from = in.nextInt();
    System.out.print("To:    ");
    int to = in.nextInt();
    StarsRUs.moviesDB.getTopMovies(from, to);
  }

  public void getMovieReviews() {
    Scanner in = new Scanner(System.in);
    System.out.print("Title:  ");
    String title = in.nextLine();
    StarsRUs.moviesDB.getMovieReviews(title);
  }

  public void addInterest(){
    return;
  }

  public void genMthStatement(){
    return;
  }

  public void listActiveCustomer(){
    return;
  }

  public void generateDter(){
    return;
  }

  public void generateCustomerReport(){
    return;
  }

  public void deleteTransactions(){
    return;
  }

  public void getDate(){
    System.out.println("Today's date is: " + StarsRUs.rkuangDB.getDate());
  }

  public void setDate(){
    System.out.println("What date would you like to set?");
    System.out.println("Please enter in the format YYYY-MM-DD");
    Scanner in = new Scanner(System.in);
    String date = in.nextLine();
    StarsRUs.rkuangDB.setDate(date);
    System.out.println("Date is set to: " + date);
  }

  public void logout() {
    StarsRUs.activeUser = new Customer();
    StarsRUs.validInputs.setState("noUser");
    System.out.println("Logout Successful");
  }
}

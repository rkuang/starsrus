import java.util.*;
import java.sql.*;

public class Customer {

  public String name;
  public String taxid;
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
    // Acquire a specified number of shares of a specified stock at the current price.
    // Each buy transaction costs $20 (commission)
    Scanner in = new Scanner(System.in);
    System.out.print("Stock ID:  ");
    String stockid = in.next();
    System.out.print("Quantiy:   ");
    double quantity = in.nextDouble();
    StarsRUs.rkuangDB.buyStocks(stockid, quantity);
  }

  public void sell() {
    // TODO
    // Sell a specified number of shares of a specified stock at the current price.
    // For tax purposes, the customer has to specify the original buying prices
    // of the stocks to be sold.
    // each sell transaction costs $20 of commission. The money from selling the
    // stock will be deposited into the market account.
    Scanner in = new Scanner(System.in);
    System.out.print("Stock ID:  ");
    String stockid = in.next();

    StarsRUs.rkuangDB.sellStocks(stockid);
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

  public void setMarket(){
    System.out.println("Would you like to open or close the market?");
    Scanner in = new Scanner(System.in);
    if(in.next().equals("open")){
      StarsRUs.rkuangDB.setMarket(true);
      System.out.println("The market is now open");
    }
    else{
      StarsRUs.rkuangDB.setMarket(false);
      System.out.println("The market is now closed");
    }
  }

  public void getDate() {
    StarsRUs.rkuangDB.getDate();
  }

  public void setDate() {
    System.out.println("What date would you like to set (YYYY-MM-DD)?");
    System.out.print("Date:  ");
    Scanner in = new Scanner(System.in);
    String date = in.nextLine();
    StarsRUs.rkuangDB.setDate(date);
    System.out.println("Date is set to: " + date);
  }

  public void setStockPrice() {
    Scanner in = new Scanner(System.in);
    System.out.print("Stock ID:   ");
    String stockid = in.next();
    System.out.print("New Price:  ");
    double newprice = in.nextDouble();
    StarsRUs.rkuangDB.setStockPrice(stockid, newprice);
  }

  public void logout() {
    StarsRUs.activeUser = new Customer();
    StarsRUs.validInputs.setState("noUser");
    System.out.println("Logout Successful");
  }
}

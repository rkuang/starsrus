import java.util.*;
import java.sql.*;

public class Customer {

  public String name;
  public String taxid;
  public Boolean admin;
  public Double currBalance;

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
      }
      System.out.println("Hello, "+StarsRUs.activeUser.name);
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
    if (StarsRUs.rkuangDB.updateBalance(amount)) {
      StarsRUs.rkuangDB.newMarketTransaction("deposit", amount);
    }
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
    if (StarsRUs.rkuangDB.updateBalance(-1 * amount)) {
      StarsRUs.rkuangDB.newMarketTransaction("withdraw", -1 * amount);
    }
  }

  public void buy() {
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
    // Sell a specified number of shares of a specified stock at the current price.
    // For tax purposes, the customer has to specify the original buying prices
    // of the stocks to be sold.
    // each sell transaction costs $20 of commission. The money from selling the
    // stock will be deposited into the market account.

    StarsRUs.rkuangDB.showStockBalance(taxid);
    Scanner in = new Scanner(System.in);
    System.out.print("Stock ID:  ");
    String stockid = in.next();
    StarsRUs.rkuangDB.sellStocks(stockid);
  }

  public void getBalance() {
    System.out.println(String.format("Market Account Balance:  $%.2f", StarsRUs.rkuangDB.getBalance(taxid)));
  }

  public void getTransactionHistory() {
    StarsRUs.rkuangDB.getStockHistory(StarsRUs.activeUser.taxid);
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

  public void accrueInterest(){
    StarsRUs.rkuangDB.accrueInterest();
    System.out.println("Interest has been applied to all market accounts.");
    return;
  }

  public void genMthStatement(){
    System.out.println("Generate monthly report for:");
    System.out.print("Tax ID:  ");
    Scanner in = new Scanner(System.in);
    String thisTaxid = in.nextLine();
    StarsRUs.rkuangDB.genMthStatement(thisTaxid);
    return;
  }

  public void listActiveCustomers(){
    StarsRUs.rkuangDB.listActiveCustomers();
  }

  public void generateDter(){
    StarsRUs.rkuangDB.generateDter();
  }

  public void generateCustomerReport(){
    System.out.println("Generate customer report for:");
    System.out.print("Tax ID:  ");
    Scanner in = new Scanner(System.in);
    String thisTaxid = in.nextLine();
    System.out.println(String.format("Account Information for %s:", StarsRUs.rkuangDB.getName(thisTaxid)));
    System.out.println(String.format("Market Account Balance:  $%.2f", StarsRUs.rkuangDB.getBalance(thisTaxid)));
    StarsRUs.rkuangDB.showStockBalance(thisTaxid);
    return;
  }

  public void deleteTransactions(){
    // TODO more explore
    StarsRUs.rkuangDB.deleteTransactions();
  }

  public void setMarket() {
    System.out.println("Would you like to open or close the market?");
    String marketState = StarsRUs.rkuangDB.getMarketState()?"open":"close";

    Scanner in = new Scanner(System.in);
    String input = in.next();
    if(input.equals(marketState)){
      System.out.println("Market is already "+input);
    } else {
      Boolean bool;
      if (input.equals("open")) {
        bool = true;
      } else {
        bool = false;
      }
      StarsRUs.rkuangDB.setMarket(bool);
    }
  }

  public void getDate(){
    System.out.println("Today's date is: " + StarsRUs.rkuangDB.getDate());
  }

  public void setDate() {
    System.out.println("What date would you like to set (YYYY-MM-DD)?");
    System.out.print("Date:  ");
    Scanner in = new Scanner(System.in);
    String date = in.nextLine();
    if(date.equals(StarsRUs.rkuangDB.getDate())){
      System.out.println("It is already set to " + date);
    }
    else{
      StarsRUs.rkuangDB.setDate(date);
    }
  }

  public void setStockPrice() {
    Scanner in = new Scanner(System.in);
    System.out.print("Stock ID:   ");
    String stockid = in.next();
    System.out.print("New Price:  ");
    double newprice = in.nextDouble();
    StarsRUs.rkuangDB.setStockPrice(stockid, newprice);

    System.out.println(String.format("%s has been set to $%.2f", stockid, newprice));
  }

  public void logout() {
    StarsRUs.activeUser = new Customer();
    StarsRUs.validInputs.setState("noUser");
    System.out.println("Logout Successful");
  }
}

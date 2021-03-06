import java.sql.*;
import java.util.*;

public class RkuangDB {
  final String HOST = "jdbc:mysql://cs174a.engr.ucsb.edu:3306/rkuangDB";
  final String USER = "rkuang";
  final String PWD = "994";

  Connection connection;

  public RkuangDB() {
    getDriver();
    getConnection();
  }

  private void getDriver() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void getConnection() {
    try {
      connection = DriverManager.getConnection(HOST, USER, PWD);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Boolean login(String username, String password) {
    String query = String.format("SELECT * FROM Customers WHERE username='%s' AND password='%s'", username, password);

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      if (rs.next()) {
        StarsRUs.activeUser.name = rs.getString("name");
        StarsRUs.activeUser.taxid = rs.getString("taxid");
        StarsRUs.activeUser.admin = rs.getBoolean("admin");
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }
  public Boolean register(String name, String user, String pass, String addr, String state, String phNum, String email, String taxID, String ssn){
    String queryCustomer = String.format("INSERT INTO Customers VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s',false)", name, user, pass, addr, state, phNum, email, taxID, ssn);
    try(Statement statement = connection.createStatement()){
      statement.executeUpdate(queryCustomer);
      return true;
    }catch(SQLException e){
      e.printStackTrace();
    }
    return false;
  }

  public void createMarketAccount(String taxid, double deposit){
    String queryMarket = String.format("INSERT INTO Market_Accounts(taxid, balance)VALUES('%s', '%.2f')", taxid, deposit);
    try(Statement statement = connection.createStatement()){
      statement.executeUpdate(queryMarket);
    }catch(SQLException e){
      e.printStackTrace();
    }
  }

  public void createStockAccount(String taxid) {
    String query = String.format("INSERT INTO Stock_Accounts VALUES ('%s', 0)", taxid);
    try(Statement statement = connection.createStatement()){
      statement.executeUpdate(query);
    }catch(SQLException e){
      e.printStackTrace();
    }
  }

  public Boolean updateBalance(double amount) {
    String query = String.format("SELECT balance FROM Market_Accounts WHERE taxid='%s'", StarsRUs.activeUser.taxid);

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      if (rs.next()) {
        double balance = rs.getDouble("balance");
        double newBalance = balance + amount;
        if (newBalance >= 0) {
          query = String.format("UPDATE Market_Accounts SET balance='%.2f' WHERE taxid='%s'", newBalance, StarsRUs.activeUser.taxid);
          statement.executeUpdate(query);
          System.out.println(String.format("Balance is now $%.2f", newBalance));
          return true;
        } else {
          System.out.println("Transaction failed. Market Account balance cannot fall below $0");
          return false;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public Boolean updateBalance(String taxid, double amount) {
    String query = String.format("SELECT balance FROM Market_Accounts WHERE taxid='%s'", taxid);

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      if (rs.next()) {
        double balance = rs.getDouble("balance");
        double newBalance = balance + amount;
        if (newBalance >= 0) {
          query = String.format("UPDATE Market_Accounts SET balance='%.2f' WHERE taxid='%s'", newBalance, taxid);
          statement.executeUpdate(query);
          return true;
        } else {
          System.out.println("Transaction failed. Market Account balance cannot fall below $0");
          return false;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  private void updateStockBalance(String stockid, double price, double quantity) {
    String query = String.format("SELECT * FROM Stock_Balance WHERE taxid='%s' AND stockid='%s' AND buyingprice=%.2f", StarsRUs.activeUser.taxid, stockid, price);

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      if (rs.next()) {
        double oldQuantity = rs.getDouble("quantity");
        double newQuantity = oldQuantity + quantity;
        if (newQuantity == 0) {
          query = String.format("DELETE FROM Stock_Balance WHERE taxid='%s' AND stockid='%s' AND buyingprice=%.2f AND quantity=%.3f", StarsRUs.activeUser.taxid, stockid, price, oldQuantity);
        } else {
          query = String.format("UPDATE Stock_Balance SET quantity=%.3f WHERE taxid='%s' AND stockid='%s' AND buyingprice=%.2f", newQuantity, StarsRUs.activeUser.taxid, stockid, price);
        }
      } else {
        query = String.format("INSERT INTO Stock_Balance VALUES ('%s', '%s', %.2f, %.3f)", StarsRUs.activeUser.taxid, stockid, price, quantity);
      }
      statement.executeUpdate(query);
      if (quantity < 0) {
        System.out.println(String.format("%.3f shares of %s sold at $%.2f each", -1*quantity, stockid, price));
      } else {
        System.out.println(String.format("%.3f shares of %s purchased at $%.2f each", quantity, stockid, price));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void updateSharesTraded(double profit, double quantity) {
    String query = String.format("SELECT * FROM Stock_Accounts WHERE taxid='%s'", StarsRUs.activeUser.taxid);

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      if (rs.next()) {
        double oldProfit = rs.getDouble("profit");
        double newProfit = oldProfit + profit;
        double oldSharesTraded = rs.getDouble("shares_traded");
        double newSharesTraded = oldSharesTraded + quantity;
        query = String.format("UPDATE Stock_Accounts SET shares_traded=%.3f, profit=%.2f WHERE taxid='%s'", newSharesTraded, newProfit, StarsRUs.activeUser.taxid);
      } else {
        query = String.format("INSERT INTO Stock_Accounts VALUES ('%s', %.2f, %.3f)", StarsRUs.activeUser.taxid, profit, quantity);
      }
      statement.executeUpdate(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void updateSharesTraded(String taxid, double profit, double quantity) {
    String query = String.format("SELECT * FROM Stock_Accounts WHERE taxid='%s'", taxid);

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      if (rs.next()) {
        double oldProfit = rs.getDouble("profit");
        double newProfit = oldProfit + profit;
        double oldSharesTraded = rs.getDouble("shares_traded");
        double newSharesTraded = oldSharesTraded + quantity;
        query = String.format("UPDATE Stock_Accounts SET shares_traded=%.3f, profit=%.2f WHERE taxid='%s'", newSharesTraded, newProfit, taxid);
      } else {
        query = String.format("INSERT INTO Stock_Accounts VALUES ('%s', %.2f, %.3f)", taxid, profit, quantity);
      }
      statement.executeUpdate(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Boolean buyStocks(String stockid, double quantity) {
    String query = String.format("SELECT * FROM Stocks WHERE stockid='%s'", stockid);

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      if (rs.next()) {
        double price = rs.getDouble("currentprice");
        double cost = -1 * (price*quantity + 20);
        if (updateBalance(cost)) {
          updateStockBalance(stockid, price, quantity);

          updateSharesTraded(0, quantity);
          newStockTransaction("buy", stockid, quantity, cost);
        }
        return true;
      } else {
        System.out.println(String.format("'%s' is not a valid Stock ID", stockid));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    // this should never happen
    return false;
  }

  public void sellStocks(String stockid) {
    String query = String.format("SELECT * FROM Stock_Balance WHERE taxid='%s' AND stockid='%s'", StarsRUs.activeUser.taxid, stockid);
    ArrayList<Double> sellAmount = new ArrayList<Double>();
    ArrayList<Double> buyingPrice = new ArrayList<Double>();
    ArrayList<Double> quantity = new ArrayList<Double>();
    int i = 0;

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      while (rs.next()) {
        buyingPrice.add(rs.getDouble("buyingprice"));
        quantity.add(rs.getDouble("quantity"));

        Scanner in = new Scanner(System.in);
        System.out.println(String.format("How many shares of %s at $%.2f would you like to sell?", stockid, buyingPrice.get(i)));
        System.out.print("Quantity:  ");
        sellAmount.add(in.nextDouble());
        while (sellAmount.get(i) > quantity.get(i)) {
          System.out.println("You do not own that many shares");
          System.out.println(String.format("How many shares of %s at $%.2f would you like to sell?", stockid, buyingPrice.get(i)));
          System.out.print("Quantity:  ");
          sellAmount.set(i, in.nextDouble());
        }
        i++;
      }

      double commission = 20;
      double addToMarket = sum(sellAmount) * getStockPrice(stockid);
      double profit = addToMarket;
      for (int j = 0; j<i; j++) {
        profit -= sellAmount.get(j) * buyingPrice.get(j);
        quantity.set(j, quantity.get(j)-sellAmount.get(j));
      }

      if (updateBalance(addToMarket-commission)) {
        for (int j = 0; j<i; j++) {
          updateStockBalance(stockid, buyingPrice.get(j), -1*sellAmount.get(j));
        }

        updateSharesTraded(profit, sum(sellAmount));
        newStockTransaction("sell", stockid, -1*sum(sellAmount), addToMarket-commission);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private double sum(ArrayList<Double> list) {
    double sum = 0;
    for (double i : list) {
      sum += i;
    }
    return sum;
  }

  private double getStockPrice(String stockid) {
    String query = String.format("SELECT * FROM Stocks WHERE stockid='%s'", stockid);
    double price = 0;

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      if (rs.next()) {
        price = rs.getDouble("currentprice");
        return price;
      } else {
        System.out.println(String.format("'%s' is not a valid Stock ID", stockid));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return price;
  }

  public void showStockBalance(String taxid) {
    String query = String.format("SELECT * FROM Stock_Balance WHERE taxid='%s'", taxid);

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);

      System.out.println("Stock Account Balance:");
      while (rs.next()) {
        String stockid = rs.getString("stockid");
        double buyingprice = rs.getDouble("buyingprice");
        double quantity = rs.getDouble("quantity");

        System.out.println(String.format("    %.3f shares of %s purchased at $%.2f", quantity, stockid, buyingprice));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public double getBalance(String taxid) {
    String query = String.format("SELECT * FROM Market_Accounts WHERE taxid='%s'", taxid);
    double balance = -42;
    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      if (rs.next()) {
        balance = rs.getDouble("balance");
        return balance;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    // this should never happen
    return balance;
  }

  public Boolean getStockInfo(String stockid) {
    String query = String.format("SELECT * FROM Stocks WHERE stockid='%s'", stockid);

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      if (rs.next()) {
        double price = rs.getDouble("currentprice");
        String name = rs.getString("name");
        String date = rs.getDate("dob").toString();

        System.out.println("=====================");
        System.out.println("Stock ID:  "+stockid);
        System.out.println("Actor:     "+name);
        System.out.println("DOB:       "+date);
        System.out.println(String.format("Price:     $%.2f", price));
        System.out.println("=====================");

        System.out.println(String.format("%s has the following movie contracts:", name));
        printMovieContracts(stockid);
      } else {
        System.out.println(String.format("'%s' is not a valid Stock ID", stockid));
      }
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    // this should never happen
    return false;
  }

  private void printMovieContracts(String stockid) {
    String query = String.format("SELECT * FROM Movie_Contracts WHERE stockid='%s'", stockid);

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      while (rs.next()) {
        String title = rs.getString("title");
        String role = rs.getString("role");
        int year = rs.getInt("year");
        double contract = rs.getDouble("contract");

        System.out.println("    Title:    "+title);
        System.out.println("    Role:     "+role);
        System.out.println("    Year:     "+year);
        System.out.println(String.format("    Contract: $%.2f\n", contract));

      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void listActiveCustomers() {
    String query = "SELECT * FROM Customers C, Stock_Accounts SA WHERE C.taxid=SA.taxid AND shares_traded>1000";

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      while (rs.next()) {
        String name = rs.getString("name");
        String address = rs.getString("address");
        String state = rs.getString("state");
        String phone = rs.getString("phone");
        String email = rs.getString("email");
        String taxid = rs.getString("C.taxid");
        String ssn = rs.getString("ssn");
        double shares = rs.getDouble("shares_traded");

        System.out.println("=====================");
        System.out.println("Name:           "+name);
        System.out.println("Address:        "+address);
        System.out.println("State:          "+state);
        System.out.println("Phone:          "+phone);
        System.out.println("Email:          "+email);
        System.out.println("Tax ID:         "+taxid);
        System.out.println("SSN:            "+ssn);
        System.out.println(String.format("Shares Traded:  %.3f", shares));
        System.out.println("=====================");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void setStockPrice(String stockid, double newprice) {
    String query = String.format("UPDATE Stocks SET currentprice=%.2f WHERE stockid='%s'", newprice, stockid);

    try (Statement statement = connection.createStatement()) {
      statement.executeUpdate(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public String getDate() {
    String query = String.format("SELECT * FROM Market");
    String today = "";
    try (Statement statement = connection.createStatement()){
      ResultSet rs = statement.executeQuery(query);
      if(rs.next()){
        today = rs.getString("date");
      }
    } catch (SQLException e){
      e.printStackTrace();
    }
    return today;
  }

  public void setDate(String date){
    int day = dayToInt(date);
    int todayDay = dayToInt(getDate());

    if (day < todayDay) {
      System.out.println("Cannot go back in time.");
      return;
    }

    for (int i = 0; i < (day - todayDay); i++) {
      advanceDate();
    }
  }

  public void applyDate(String date){
    String query = String.format("UPDATE Market SET date = '%s'", date);
    try(Statement statement = connection.createStatement()){
      statement.executeUpdate(query);
    } catch (SQLException e){
      if (date.equals("2013-03-32")) {
        System.out.println("End of March reached");
      } else {
        e.printStackTrace();
      }
    }
    return;
  }

  public void accrueInterest() {
    String query = "SELECT taxid, COUNT(*), SUM(balance) FROM Daily_Balances GROUP BY taxid";

    try (Statement statement = connection.createStatement()){
      ResultSet rs = statement.executeQuery(query);

      while (rs.next()) {
        String taxid = rs.getString("taxid");
        int count = rs.getInt("COUNT(*)");
        double sum = rs.getDouble("SUM(balance)");

        double avgBal = sum / count;
        double BaltoAdd = avgBal * 0.03;

        updateBalance(taxid, BaltoAdd);
        newMarketTransaction(taxid, "interest", BaltoAdd);
        updateSharesTraded(taxid, BaltoAdd, 0);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return;
  }

  public int dayToInt(String date){
    int temp = Integer.parseInt(date.substring(8,10));
    return temp;
  }

  public Boolean getMarketState() {
    String query = String.format("SELECT open FROM Market");
    Boolean open = false;
    try (Statement statement = connection.createStatement()){
      ResultSet rs = statement.executeQuery(query);
      while (rs.next()) {
        open = rs.getBoolean("open");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return open;
  }

  public Boolean setMarket(Boolean input){
    String query = String.format("UPDATE Market SET open = %b", input);

    try (Statement statement = connection.createStatement()){
      statement.executeUpdate(query);
      if (!input) {
        advanceDate();
      }
    }catch (SQLException e){
      e.printStackTrace();
    }
    return false;
  }

  public void insertClosingPrices() {
    String query = "SELECT * FROM Stocks";

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);

      String update;
      while (rs.next()) {
        String stockid = rs.getString("stockid");
        double price = rs.getDouble("currentprice");

        update = String.format("INSERT INTO Closing_Prices VALUES ('%s', '%s', %.2f)", stockid, getDate(), price);
        try (Statement s2 = connection.createStatement()) {
          s2.executeUpdate(update);
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void insertDailyBalance() {
    String query = "SELECT * FROM Market_Accounts";

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);

      String update;
      while (rs.next()) {
        String taxid = rs.getString("taxid");
        double balance = rs.getDouble("balance");

        update = String.format("INSERT INTO Daily_Balances VALUES ('%s', '%s', %.2f)", taxid, getDate(), balance);
        try (Statement s2 = connection.createStatement()) {
          s2.executeUpdate(update);
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void advanceDate() {
    String today = getDate();

    int day = dayToInt(today)+1;
    String tomorrow = "2013-03-"+day;

    insertClosingPrices();
    insertDailyBalance();

    applyDate(tomorrow);
    setMarket(true);
  }

  public void newMarketTransaction(String type, double amount) {
    String query = String.format("INSERT INTO Market_Transactions (taxid, date, type, amount) VALUES ('%s', '%s', '%s', %.2f)", StarsRUs.activeUser.taxid, getDate(), type, amount);

    try (Statement statement = connection.createStatement()) {
      statement.executeUpdate(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void newMarketTransaction(String taxid, String type, double amount) {
    String query = String.format("INSERT INTO Market_Transactions (taxid, date, type, amount) VALUES ('%s', '%s', '%s', %.2f)", taxid, getDate(), type, amount);

    try (Statement statement = connection.createStatement()) {
      statement.executeUpdate(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void newStockTransaction(String type, String stockid, double quantity, double price) {
    String query = String.format("INSERT INTO Stock_Transactions (taxid, date, type, stockid, quantity, price) VALUES ('%s', '%s', '%s', '%s', %.3f, %.2f)", StarsRUs.activeUser.taxid, getDate(), type, stockid, quantity, price);

    try (Statement statement = connection.createStatement()) {
      statement.executeUpdate(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void deleteTransactions() {
    String queryDeleteMarketTransactions = "DELETE FROM Market_Transactions";
    String queryDeleteStockTransactions = "DELETE FROM Stock_Transactions";
    String queryResetStockActivity = "UPDATE Stock_Accounts SET profit=0, shares_traded=0";
    String queryDeleteDailyBal = "DELETE FROM Daily_Balances";
    String queryDeleteClosingPrice = "DELETE FROM Closing_Prices";

    try (Statement statement = connection.createStatement()) {
      statement.executeUpdate(queryDeleteMarketTransactions);
      statement.executeUpdate(queryDeleteStockTransactions);
      statement.executeUpdate(queryResetStockActivity);
      statement.executeUpdate(queryDeleteDailyBal);
      statement.executeUpdate(queryDeleteClosingPrice);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void generateDter() {
    String query = "SELECT * FROM Customers C, Stock_Accounts SA WHERE C.taxid=SA.taxid AND SA.profit>10000";

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      while (rs.next()) {
        String name = rs.getString("name");
        String address = rs.getString("address");
        String state = rs.getString("state");
        String phone = rs.getString("phone");
        String email = rs.getString("email");
        String taxid = rs.getString("C.taxid");
        String ssn = rs.getString("ssn");
        double earnings = rs.getDouble("profit");

        System.out.println("=====================");
        System.out.println("Name:     "+name);
        System.out.println("Address:  "+address);
        System.out.println("State:    "+state);
        System.out.println("Phone:    "+phone);
        System.out.println("Email:    "+email);
        System.out.println("Tax ID:   "+taxid);
        System.out.println("SSN:      "+ssn);
        System.out.println(String.format("Earnings: $%.2f", earnings));
        System.out.println("=====================");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void getStockHistory(String taxid) {
    String query = String.format("SELECT * FROM Stock_Transactions WHERE taxid='%s'", taxid);

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      System.out.println("ID\tDate\t\tType\tStockID\tQuantity\tChange to Balance ($)");
      while (rs.next()) {
        int transID = rs.getInt("id");
        String date = rs.getDate("date").toString();
        String type = rs.getString("type");
        String stockid = rs.getString("stockid");
        double quantity = rs.getDouble("quantity");
        double price = rs.getDouble("price");

        System.out.println(String.format("%d\t%s\t%s\t%s\t%.3f\t\t%.2f", transID, date, type, stockid, quantity, price));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void getMarketHistory(String taxid) {
    String query = String.format("SELECT * FROM Market_Transactions WHERE taxid='%s'", taxid);

    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      System.out.println("ID\tDate\t\tType\tAmount");
      while (rs.next()) {
        int transID = rs.getInt("id");
        String date = rs.getDate("date").toString();
        String type = rs.getString("type");
        Double amount = rs.getDouble("amount");

        System.out.println(String.format("%d\t%s\t%s\t%.2f", transID, date, type, amount));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void genMthStatement(String taxid){
    Double finalMarketBalance = this.getBalance(taxid);
    Double initalMarketBalance = 0.0;
    Double initalStockBalance = 0.0;
    Double profit = 0.0;
    Double market_change = 0.0;
    double commissionCount = 0;
    double commissionPaid = 0;
    String name = "";
    String email = "";
    String query = String.format("SELECT name, email FROM Customers WHERE taxid = '%s'", taxid);
    try (Statement statement = connection.createStatement()){
      ResultSet rs = statement.executeQuery(query);
      while(rs.next()){
        name = rs.getString("name");
        email = rs.getString("email");
      }

      query = String.format("SELECT * FROM Stock_Accounts WHERE taxid = '%s'", taxid);
      rs = statement.executeQuery(query);
      while(rs.next()){
        profit = rs.getDouble("profit");
      }

      query = String.format("SELECT SUM(amount) FROM Market_Transactions WHERE taxid = '%s'", taxid);
      rs = statement.executeQuery(query);
      while(rs.next()){
        market_change = rs.getDouble("SUM(amount)");
      }

      query = String.format("SELECT SUM(price) FROM Stock_Transactions WHERE taxid = '%s'", taxid);
      rs = statement.executeQuery(query);
      while(rs.next()){
        market_change += rs.getDouble("SUM(price)");
      }

      query = String.format("SELECT COUNT(*) FROM Stock_Transactions WHERE taxid='%s'", taxid);
      rs = statement.executeQuery(query);
      if (rs.next()) {
        commissionCount = rs.getInt("COUNT(*)");
      }

    } catch(SQLException e){
      e.printStackTrace();
    }
    commissionPaid = commissionCount*20;
    profit = profit - commissionPaid;
    initalMarketBalance = finalMarketBalance - market_change;
    System.out.println("Name:                  " + name);
    System.out.println("E-mail:                " + email);
    System.out.println("Market Account Transaction History:");
    this.getMarketHistory(taxid);
    System.out.println("Stock Account Transaction History:");
    this.getStockHistory(taxid);
    System.out.println(String.format("Commission Paid:       $%.2f", commissionPaid));
    System.out.println(String.format("Earnings:              $%.2f", profit));
    System.out.println(String.format("Inital Market Balance: $%.2f", initalMarketBalance));
    System.out.println(String.format("Final Market Balance:  $%.2f", finalMarketBalance));
    System.out.println("Initial Stock Balance:");
    initStockBal(taxid);
    System.out.println("Final Stock Balance:");
    finalStockBal(taxid);
  }

  public void initStockBal(String taxid) {
    String query = String.format("SELECT * FROM Stocks S", taxid);

    try (Statement statement = connection.createStatement()){
      ResultSet rs = statement.executeQuery(query);
      Map<String, Double> quantity = new HashMap<String, Double>();
      String stockid = "";

      while(rs.next()){
        stockid = rs.getString("stockid");
        quantity.put(stockid, 0.0);
        String q2 = String.format("SELECT stockid, SUM(quantity) FROM Stock_Balance WHERE taxid='%s' AND stockid='%s' GROUP BY stockid", taxid, stockid);
        try (Statement s2 = connection.createStatement()){
          ResultSet rs2 = s2.executeQuery(q2);
          while(rs2.next()){
            quantity.replace(stockid, rs2.getDouble("SUM(quantity)"));
          }
        } catch(SQLException e){
          e.printStackTrace();
        }

        String q3 = String.format("SELECT stockid, SUM(quantity) FROM Stock_Transactions WHERE taxid='%s' AND stockid='%s' GROUP BY stockid", taxid, stockid);
        try (Statement s3 = connection.createStatement()){
          ResultSet rs3 = s3.executeQuery(q3);
          while(rs3.next()){
            quantity.replace(stockid, quantity.get(stockid)-rs3.getDouble("SUM(quantity)"));
          }
        } catch(SQLException e){
          e.printStackTrace();
        }
        System.out.println(String.format("   %.3f shares of %s", quantity.get(stockid), stockid));
      }
    } catch(SQLException e){
      e.printStackTrace();
    }
  }

  public void finalStockBal(String taxid) {
    String query = String.format("SELECT * FROM Stocks S", taxid);

    try (Statement statement = connection.createStatement()){
      ResultSet rs = statement.executeQuery(query);
      Map<String, Double> quantity = new HashMap<String, Double>();
      String stockid = "";

      while(rs.next()){
        stockid = rs.getString("stockid");
        quantity.put(stockid, 0.0);
        String q2 = String.format("SELECT stockid, SUM(quantity) FROM Stock_Balance WHERE taxid='%s' AND stockid='%s' GROUP BY stockid", taxid, stockid);
        try (Statement s2 = connection.createStatement()){
          ResultSet rs2 = s2.executeQuery(q2);
          while(rs2.next()){
            quantity.replace(stockid, rs2.getDouble("SUM(quantity)"));
          }
        } catch(SQLException e){
          e.printStackTrace();
        }
        System.out.println(String.format("   %.3f shares of %s", quantity.get(stockid), stockid));
      }
    } catch(SQLException e){
      e.printStackTrace();
    }
  }

  public String getName(String taxid) {
    String query = String.format("SELECT name, email FROM Customers WHERE taxid = '%s'", taxid);
    String name = "";
    try (Statement statement = connection.createStatement()){
      ResultSet rs = statement.executeQuery(query);
      while(rs.next()){
        name = rs.getString("name");
      }
    } catch(SQLException e){
      e.printStackTrace();
    }
    return name;
  }

  public void closeConnection() {
    try {
      if (connection != null) {
        connection.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}

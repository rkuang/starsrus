import java.util.*;

public class StarsRUs {

  static Customer activeUser;
  static ValidInputs validInputs;
  static RkuangDB rkuangDB;
  static MoviesDB moviesDB;
  static Admin admin;

  public static void main(String[] args) {
    activeUser = new Customer();
    validInputs = new ValidInputs();
    rkuangDB = new RkuangDB();
    moviesDB = new MoviesDB();
    printBanner();

    String input = "";
    while (!input.equals("exit")) {
      input = getUserInput();
      if (validInputs.contains(input)) {
        switch (input) {
          case "login":
          activeUser.login();
          break;

          case "register":
          activeUser.register();
          break;

          case "deposit":
          activeUser.deposit();
          break;

          case "withdraw":
          activeUser.withdraw();
          break;

          case "buy":
          // buy
          break;

          case "sell":
          // sell
          break;

          case "get balance":
          activeUser.getBalance();
          break;

          case "transaction history":
          // transaction history
          break;

          case "stock info":
          activeUser.getStockInfo();
          break;

          case "movie info":
          activeUser.getMovieInfo();
          break;

          case "top movies":
          activeUser.getTopMovies();
          break;

          case "add interest":
          activeUser.addInterest();
          break;

          case "generate monthly statement":
          activeUser.genMthStatement();
          break;

          case "list active customers":
          activeUser.listActiveCustomer();
          break;

          case "generate dter":
          activeUser.generateDter();
          break;

          case "generate customer report":
          activeUser.generateCustomerReport();
          break;

          case "delete transactions":
          activeUser.deleteTransactions();
          break;

          case "get date":
          activeUser.getDate();
          break;

          case "set date":
          activeUser.setDate();
          break;

          case "movie reviews":
          activeUser.getMovieReviews();
          break;

          case "logout":
          activeUser.logout();
          break;

          case "help":
          validInputs.print();
          break;

          case "set market":
          activeUser.setMarket();
          break;
        }
      } else {
        System.out.println(String.format("'%s' is not a valid input", input));
      }
    }

    rkuangDB.closeConnection();
    moviesDB.closeConnection();
    System.exit(0);
  }

  private static void printBanner() {
    System.out.println("");
    System.out.println("");
    System.out.println("   .d88888b    dP                                  888888ba     dP     dP            ");
    System.out.println("   88.    \"'   88                                  88    `8b    88     88            ");
    System.out.println("   `Y88888b. d8888P .d8888b. 88d888b. .d8888b.    a88aaaa8P'    88     88 .d8888b.   ");
    System.out.println("         `8b   88   88'  `88 88'  `88 Y8ooooo.     88   `8b.    88     88 Y8ooooo.   ");
    System.out.println("   d8'   .8P   88   88.  .88 88             88     88     88    Y8.   .8P       88   ");
    System.out.println("    Y88888P    dP   `88888P8 dP       `88888P'     dP     dP    `Y88888P' `88888P'   ");
    System.out.println("ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
    System.out.println("");
    System.out.println("Ricky Kuang, Alan Tran                                                     CMPSC 174A");
    System.out.println("=====================================================================================");
    System.out.println("Welcome to Stars R Us!");
    System.out.println("Type 'help' for help");
    System.out.println("");
  }

  private static String getUserInput() {
    System.out.print(" > ");
    Scanner in = new Scanner(System.in);
    return(in.nextLine());
  }
}

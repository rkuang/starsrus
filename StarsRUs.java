import java.util.*;

public class StarsRUs {

  public static void main(String[] args) {

    // MoviesDB moviesDB = new MoviesDB();
    // String movie = getUserInput();
    // moviesDB.getMovieInfo(movie);

    ArrayList<String> validInputs = new ArrayList<>();
    validInputs.add("login");
    validInputs.add("register");
    validInputs.add("exit");

    printBanner();

    String input = "";
    System.out.println("\n****\n");

    while (!input.equals("exit")) {
      input = getUserInput();
      if (validInputs.contains(input)) {
        switch (input) {
          case "login":
          Customer.login();
          break;
          case "register":
          Customer.register();
          break;
        }
      } else {
        System.out.println(String.format("'%s' is not a valid input", input));
      }
    }

    //RkuangDB db = new RkuangDB();
    //db.getTestTable();
    //db.closeConnection();
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

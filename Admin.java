import java.util.*;
import java.sql.*;

public class Admin{
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
    System.out.println("Please enter in the format MM-DD-YYYY");
    Scanner in = new Scanner(System.in);
    String date = in.nextLine();
    StarsRUs.rkuangDB.setDate(date);
    System.out.println("Date is set to: " + date);
  }
}

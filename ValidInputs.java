import java.util.*;

public class ValidInputs {

  ArrayList<String> activeState;
  ArrayList<String> noUser = new ArrayList<>(Arrays.asList("help","login","register","exit"));
  ArrayList<String> loggedIn = new ArrayList<>(Arrays.asList("help","deposit","withdraw","buy","sell","show balance","transaction history","stock info","movie info","top movies","movie reviews","logout","exit"));
  ArrayList<String> admin = new ArrayList<>(Arrays.asList("help","get date","set date","add interest","generate monthly statement","list active customers","generate dter","generate customer report","delete transactions","logout","exit"));


  public ValidInputs() {
    activeState = noUser;
  }

  public void setState(String state) {
    switch (state) {
      case "noUser":
      activeState = noUser;
      break;
      case "loggedIn":
      if (StarsRUs.activeUser.admin) {
        activeState = admin;
      } else {
        activeState = loggedIn;
      }
      break;
    }
  }

  public Boolean contains(String input) {
    return activeState.contains(input);
  }

  public void print() {
    System.out.println("The following inputs are valid:");
    for (String input : activeState) {
      System.out.println("     "+input);
    }
  }
}

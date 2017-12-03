import java.util.*;

public class ValidInputs {

  ArrayList<String> activeState;
  ArrayList<String> noUser = new ArrayList<>(Arrays.asList("help","login","register","exit"));
  ArrayList<String> loggedIn = new ArrayList<>(Arrays.asList("help","test","logout","exit"));

  public ValidInputs() {
    activeState = noUser;
  }

  public void changeState(String state) {
    switch (state) {
      case "noUser":
      activeState = noUser;
      break;
      case "loggedIn":
      activeState = loggedIn;
      break;
    }
  }

  public Boolean contains(String input) {
    return activeState.contains(input);
  }

  public void print() {
    System.out.println("\nValid inputs are:");
    for (String input : activeState) {
      System.out.println("\t"+input);
    }
    System.out.println("");
  }
}

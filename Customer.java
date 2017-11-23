import java.util.Scanner;

public class Customer {

    public String username;
    private String password;
    
    public Customer(String user, String pass) {
	username = user;
	password = pass;
    }

    public static void login() {
	// set current user
    }

    public static void register() {
	Scanner in = new Scanner(System.in);
	System.out.print("username: ");
	String user = in.next();
	System.out.print("password: ");
	String pass = in.next();

	// add customer to db
    }
    
}

import java.util.Scanner;

public class DatabaseApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            new Login().displayLoginMenu();
            System.out.println("Do you want to log in again? (y/n)");
            String choice = scanner.nextLine();
            if (!choice.equalsIgnoreCase("y")) {
                break;
            }
        }
        System.out.println("Goodbye!");
    }
}

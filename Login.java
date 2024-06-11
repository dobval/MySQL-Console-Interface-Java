import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Scanner;

public class Login {

    public void displayLoginMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Email: ");
        String email = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();

        DatabaseConnectionRoot DBConnRoot = new DatabaseConnectionRoot();
        if(email.equals(DBConnRoot.getUser()) && password.equals(DBConnRoot.getPassword())){
            Connection conn = DBConnRoot.getConnection();
            new EmployeeMenu().displayMenu(conn);
        }

        else if (checkCredentials(email, password)) {
            DatabaseConnectionUser DBConnUser = new DatabaseConnectionUser();
            Connection conn = DBConnUser.getConnection();
            new UserMenu(email).displayMenu(conn);
        } else {
            System.out.println("Invalid credentials. Try again.");
        }
    }

    public boolean checkCredentials(String email, String password){

        String query = "SELECT password FROM customers WHERE email = ?";

        DatabaseConnectionRoot DBConnRoot = new DatabaseConnectionRoot();

        try (Connection conn = DBConnRoot.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (password.equals(storedPassword)) {
                    System.out.println("Login successful for email: " + email);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } catch (SQLException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
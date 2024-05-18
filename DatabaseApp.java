import java.sql.SQLException;

public class DatabaseApp {
    public static void main(String[] args) {
        Menu menu = new Menu();
        try {
            menu.displayMenu();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

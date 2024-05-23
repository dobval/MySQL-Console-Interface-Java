# MySQL Console Interface (Java)

This Java application provides a command-line interface to interact with a MySQL database. You can execute SQL commands, manage data, and perform various database operations seamlessly.

## Project Structure

- **DatabaseApp**: The main entry point of the application.
- **Login**: End-user/Employee login.
- **DatabaseConnectionRoot**: Establishes the MySQL database connection with root user privileges.
- **DatabaseConnectionUser**: Establishes the MySQL database connection with guest/end-user privileges (even without explicit SELECT-only privileges the import java.sql.PreparedStatement removes the risk of SQL injections).
- **EmployeeMenu**: Handles the employee interaction and displays the admin menu
- **UserMenu**: Handles user interaction and displays the command-line menu.
- **TableOperations**: Contains methods to perform operations on tables.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) installed.
- MySQL database setup.
- MySQL JDBC Driver.

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/console-mysql-manager.git
   cd console-mysql-manager
   ```

2. **Setup MySQL database**:
    - Ensure your MySQL server is running.
    - Download [ArcticAthletes_Simple](arcticathletes_simple) MySQL Database and run the script

3. **Configure database connection**:
    - Update the `DatabaseConnection*` classes (root and guest) with your MySQL database URL, username, and password:
      ```java
      private static final String URL = "jdbc:mysql://localhost:3306/database_name";
      private static final String USER = "username";
      private static final String PASSWORD = "password";
      ```

4. **Compile the application**:
   ```bash
   javac -cp .:mysql-connector-java-8.0.23.jar DatabaseApp.java
   ```

5. **Run the application**:
   ```bash
   java -cp .:mysql-connector-java-8.0.23.jar DatabaseApp
   ```

## Usage

Upon running the application, you will be presented with a Login menu:

```plaintext
Email: 
example@mail.com
Password: 
password
```

After a successful login, depending on the entered credentials there will be either an employee (root) menu or a customer (guest) menu:

```
   1. List Tables
	2. Select a Table
	3. Add Data
	4. Modify Data
	5. Delete Data
	6. Logout
Choose an option:
```
```
Login successful for email: customer@example.com
	1. View all products
	2. Browse by category
	3. View my orders
	4. Logout
Choose an option: 
```

## Contributing

Simply fork the repository and submit a pull request for any enhancements or bug fixes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE).

### Acknowledgements

- [MySQL](https://www.mysql.com/)
- [Java](https://www.oracle.com/java/)

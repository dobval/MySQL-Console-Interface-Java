# MySQL Console Interface (Java)

This Java application provides a command-line interface to interact with a MySQL database. You can execute SQL commands, manage data, and perform various database operations seamlessly.

## Features

- **List all tables**: Display all tables present in the connected MySQL database.
- **Select a table**: View the contents of a specific table.
- **Delete a table**: Remove all data from a specific table.
- **Drop a table**: Completely remove a table from the database.

## Project Structure

- **DatabaseApp**: The main entry point of the application.
- **DatabaseManager**: Manages the MySQL database connection.
- **Menu**: Handles user interaction and displays the command-line menu.
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
    - Create a database and tables as required.

3. **Configure database connection**:
    - Update the `DatabaseManager` class with your MySQL database URL, username, and password:
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

Upon running the application, you will be presented with a menu:

```plaintext
Enter a menu entry:
1) List all tables
2) Select a table
3) Delete a table
4) Drop a table
5) Exit
```

- **Option 1**: Lists all tables in the database.
- **Option 2**: Prompts for a table name and displays its contents.
- **Option 3**: Prompts for a table name and deletes all data from the table.
- **Option 4**: Prompts for a table name and drops the table from the database.
- **Option 5**: Exits the application.

## Contributing

Simply fork the repository and submit a pull request for any enhancements or bug fixes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE).

### Acknowledgements

- [MySQL](https://www.mysql.com/)
- [Java](https://www.oracle.com/java/)
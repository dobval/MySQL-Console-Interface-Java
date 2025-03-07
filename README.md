# MySQL Console Interface (Java)

This Java application provides a command-line interface to interact with a MySQL database using the MVC pattern. You can execute SQL commands, manage data, and perform various database operations seamlessly.

<details open>
  <summary>Customer Login Showcase</summary>
	
  https://github.com/dobval/MySQL-Console-Interface-Java/assets/100198047/5b3d9b02-3b60-4368-a516-d15039ee7d4e

</details>
<details>
  <summary>Employee Login Showcase</summary>
	
  https://github.com/dobval/MySQL-Console-Interface-Java/assets/100198047/151f9b7b-b7b8-4aa1-b3c4-182ed79db5b0

</details>



## Project Structure

- **DatabaseApp**: The main entry point of the application.
- **Login**: End-user/Employee login.
- **DatabaseConnection**: Establishes the MySQL connection and is the base class of:
  - **DatabaseConnectionRoot**: Root privileges.
  - **DatabaseConnectionUser**: Guest/end-user privileges.
- **Menu**: Handles the menu, base class of:
  - **EmployeeMenu**: Handles the employee interaction and displays the admin menu
  - **UserMenu**: Handles user interaction and displays the command-line menu.
- **ConsoleView**: (View) Handles the visualisation.
- **TableOperationsController**: (Controller) Handles the interaction between the View and Model
- **com.DatabaseModel.DatabaseOperations**: (Model) Handles the database operations.
	- Database Structure classes inside com.DatabaseModel

## Getting Started

### Prerequisites

- Java Development Kit (JDK) installed.
- MySQL database setup.
- MySQL JDBC Driver.

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/dobval/MySQL-Console-Interface-Java.git
   cd MySQL-Console-Interface-Java
   ```

2. **Setup MySQL database**:
    - Ensure your MySQL server is running.
    - Download [ArcticAthletes_Simple](arctic_athletes_simple.sql) MySQL Database and run the script

3. **Configure database connection**:
    - Update the [dbconfig.properties](dbconfig.properties) file:
      ```
      db.url=jdbc:mysql://localhost:3306/arctic_athletes_simple
      db.root.user=root
      db.root.password=adminpassword
      db.user.user=guest
      db.user.password=password
      ```

4. **Compile the application**:
   ```bash
   javac -cp .:mysql-connector-java-8.0.23.jar DatabaseApp.java
   ```
   OR download the [Release](https://github.com/dobval/MySQL-Console-Interface-Java/releases) .jar file
   

6. **Run the application**:
   ```bash
   java -cp .:mysql-connector-java-8.0.23.jar DatabaseApp
   ```
   OR if you downloaded the .jar file (make sure the properties file is in the same folder)
   ```bash
   java -jar MySQL-Console-Interface-Java.jar
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

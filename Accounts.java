package banking;

import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection connection;
    private Scanner scanner;

    public Accounts(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public long openAccout(String email) {
        if (!accountExist(email)) {
            String query = "INSERT INTO accounts(account_no, full_name, email, balance, security_pin) VALUES(?,?,?,?,?)";
            scanner.nextLine();
            System.out.println("Enter full name: ");
            String name = scanner.nextLine();
            System.out.println("Enter amount in rupees: ");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Enter your security pin: ");
            String pin = scanner.nextLine();

            try {
                long account_no = generateAccount_number();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1, account_no);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setString(5, pin);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    return account_no;
                }
                else {
                    throw new RuntimeException("Account creation failed!!");
                }
            }catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        throw new RuntimeException("Account already exist");
    }

    public long getAccount_number(String email) {
        String query = "SELECT account_no FROM accounts WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("account_no");
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("account number does not exist");
    }

    private long generateAccount_number() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT account_no from accounts ORDER BY account_no DESC LIMIT 1");
            if (resultSet.next()) {
                long lastAccount_no = resultSet.getLong("accout_no");
                return lastAccount_no + 1;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 10000100;
    }
    public boolean accountExist(String email) {
        String query = "SELECT * FROM accounts WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
            else {
                return false;
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}

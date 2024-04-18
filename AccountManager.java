package banking;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;
    private Scanner scanner;
    public AccountManager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void creditMoney(long account_no) throws SQLException{
        scanner.nextLine();
        System.out.println("Enter Amount you want to credit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter the security pin: ");
        String pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if (account_no != 0) {
                String query = "SELECT * FROM accounts WHERE account_no = ? & security_pin = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1, account_no);
                preparedStatement.setString(2, pin);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_no = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1, amount);
                    preparedStatement1.setLong(2, account_no);
                    int rowsAffected = preparedStatement1.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println(amount + "rupees credited!!!");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    }
                    else {
                        System.out.println("Transaction failed");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }
                else {
                    System.out.println("Invalid security pin...");
                }
                connection.setAutoCommit(true);
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void debitMoney(long account_no) throws SQLException{
        scanner.nextLine();
        System.out.println("Enter amount to debit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter your security pin: ");
        String pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if (account_no != 0) {
                String query = "SELECT * FROM accounts WHERE account_no = ? AND security_pin = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1, account_no);
                preparedStatement.setString(2, pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    double currentBalance = resultSet.getDouble("balance");
                    if (amount <= currentBalance) {
                        String debitQuery = "UPDATE accounts SET balance = balance - ? WHERE account_no = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(debitQuery);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2, account_no);
                        int rowsAffected = preparedStatement1.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println(amount + "Rupees debited successfully..");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }
                        else {
                            System.out.println("Transaction failed...");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else {
                        System.out.println("insufficient account balance...");
                    }
                }
                System.out.println("Invalid security pin...");
            }
            connection.setAutoCommit(true);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void transferMoney(long senderAcc_no) throws SQLException{
        scanner.nextLine();
        System.out.println("Enter receiver account: ");
        long receiver_accNumber = scanner.nextLong();
        scanner.nextLine();
        System.out.println("Enter amount in rupees: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter your security pin: ");
        String pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if (senderAcc_no != 0 && receiver_accNumber != 0) {
                String query = "SELECT * FROM accounts WHERE account_no = ? AND security_pin = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1, senderAcc_no);
                preparedStatement.setString(2, pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if (amount <= current_balance) {
                        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_no = ?";
                        String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_no = ?";

                        PreparedStatement debitPreparedStatement = connection.prepareStatement(debit_query);
                        PreparedStatement creditPreparedStatement = connection.prepareStatement(credit_query);

                        debitPreparedStatement.setDouble(1, amount);
                        debitPreparedStatement.setLong(2, senderAcc_no);
                        creditPreparedStatement.setDouble(1, amount);
                        creditPreparedStatement.setLong(2, receiver_accNumber);

                        int rowsAffectedDebit = debitPreparedStatement.executeUpdate();
                        int rowsAffectedCredit = creditPreparedStatement.executeUpdate();

                        if (rowsAffectedDebit > 0 && rowsAffectedCredit > 0) {
                            System.out.println("Transaction successfull!!!");
                            System.out.println(amount + "rupeed transferred successfully!!!");
                            connection.commit();
                            connection.setAutoCommit(true);
                        }
                        else {
                            System.out.println("Transaction failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else {
                        System.out.println("Insufficient balance...");
                    }
                }
                else {
                    System.out.println("invalid security pin");
                }
            }
            else {
                System.out.println("invalid account number");
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        connection.setAutoCommit(true);
    }

    public void getBalance(long account_no) {
        scanner.nextLine();
        System.out.println("Enter your security pin: ");
        String pin = scanner.nextLine();

        try{
            String query = "SELECT balance FROM accounts WHERE account_no = ? AND security_pin = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double balance = resultSet.getDouble("balance");
                System.out.println("Available balance = " + balance);
            }
            else {
                System.out.println("invalid pin");
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

package banking;

import java.sql.*;
import java.util.Scanner;


public class MainApp {
    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String username = "root";
    private static final String password = "7645033043";
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e) {
            System.out.println("class not found");
        }
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner scanner = new Scanner(System.in);
            User user = new User(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);

            String email;
            long account_no;

            while (true) {
                System.out.println("Welcome To Banking System!!!");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Enter your choice: ");
                int choice1 = scanner.nextInt();

                switch (choice1) {
                    case 1:
                        user.register();
                    case 2:
                        email = user.login();
                        if (email != null) {
                            System.out.println();
                            System.out.println("user logged in!!");
                        }
                        if (!accounts.accountExist(email)) {
                            System.out.println();
                            System.out.println("1. open a new account");
                            System.out.println("2. exit");
                            if (scanner.nextInt() == 1) {
                              account_no = accounts.openAccout(email);
                                System.out.println("Account created successfully");
                                System.out.println("Your account number is: " + account_no);
                            }else {
                                break;
                            }
                        }
                        account_no = accounts.getAccount_number(email);
                        int choice2 = 0;
                        while (choice2 != 5) {
                            System.out.println();
                            System.out.println("1. Debit Money");
                            System.out.println("2. Credit Money");
                            System.out.println("3. Transfer Money");
                            System.out.println("4. Check Balance");
                            System.out.println("5. Log Out");
                            System.out.println("Enter your choice: ");
                            choice2 = scanner.nextInt();
                            switch (choice2) {
                                case 1:
                                    accountManager.debitMoney(account_no);
                                    break;
                                case 2:
                                    accountManager.creditMoney(account_no);
                                    break;
                                case 3:
                                    accountManager.transferMoney(account_no);
                                    break;
                                case 4:
                                    accountManager.getBalance(account_no);
                                    break;
                                case 5:
                                    break;
                                default:
                                    System.out.println("enter valid choice");
                                    break;
                            }
                        }
                    case 3:
                        System.out.println("thankyou for using banking system");
                        System.out.println("exiting system");
                        return;
                    default:
                        System.out.println("please enter a valid choice");
                        break;
                }
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

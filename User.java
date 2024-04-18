package banking;

import javax.lang.model.util.SimpleElementVisitor6;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.Scanner;

public class User {
    private Connection connection;
    private Scanner scanner;

    public User (Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void register(){
        scanner.nextLine();
        System.out.println("user name: ");
        String name = scanner.nextLine();
        System.out.println("Email: ");
        String email = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();

        if (userExist(email)) {
            System.out.println("User already exist!!");
            return;
        }
        String query = "INSERT INTO user(full_name, email, pasword) VALUES(?,?,?)";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("user register successfully!!!!");
            }else {
                System.out.println("Registration failed!!!");
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    };


    public String login() {
        scanner.nextLine();
        System.out.println("Enter email: ");
        String email = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        String query = "SELECT * FROM user WHERE email = ? AND password = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return email;
            }
            else {
                return null;
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public boolean userExist(String email) {
        String query = "SELECT * FROM user WHERE email = ?";
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

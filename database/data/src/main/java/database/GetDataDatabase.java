package database;

import java.util.ArrayList;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.Scanner;

public class GetDataDatabase implements GetData {
    final private String url = "jdbc:oracle:thin:@198.168.52.211:1521/pdbora19c.dawsoncollege.qc.ca";
    private Connection conn;

    @Override
    public ArrayList<ArrayList<String>> readData(String path) throws ClassNotFoundException {
        String[] pathPieces = path.split("\\");
        String tableName = pathPieces[pathPieces.length - 1];

        try {
            String sql = "SELECT * FROM " + tableName;
            ResultSet rs = conn.createStatement().executeQuery(sql);
            ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
            while (rs.next()) {
                ArrayList<String> row = new ArrayList<String>();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }
            return data;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        

        return null;
    }

    public User login(Stage stage, int indexUsername, int indexPassword) {
        Connection conn = null;
        File f = new File(".\\src\\main\\java\\database\\storage\\login.txt");
        VBox vBox = (VBox) stage.getScene().getRoot().getChildrenUnmodifiable().get(0);
        TextField usernameField = ((TextField) vBox.getChildren().get(indexUsername));
        PasswordField passwordField = ((PasswordField) vBox.getChildren().get(indexPassword));

        String username = usernameField.getText();
        String password = passwordField.getText();
        // cannot contain whitespaces or commas use for loop
        // regex for only letters and numbers
        String regex = "^[a-zA-Z0-9]+$";
        if (!username.matches(regex) || !password.matches(regex)) {
            return null;
        }
        try {
            User user = checkExists(username, password, f);
            this.conn = DriverManager.getConnection(url, username, password);
            // System.out.println(user);
            if (user != null) {
                return user;
            }
        } catch (Exception e) {
            System.out.println("Error when logging in");
            System.out.println(e.getMessage());
        }
        return null;
    }

    private User checkExists(String username, String password, File f)
            throws FileNotFoundException, ClassNotFoundException {
        PreparedStatement ps = null;

        try {
            //checks on database if user exists
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getString("username"), rs.getString("password"), rs.getInt("status"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public User manageRegister(Stage stage, int indexUsername, int indexPassword, int indexPassword2, int indexStatus) {
        VBox vBox = (VBox) stage.getScene().getRoot().getChildrenUnmodifiable().get(0);
        TextField usernameField = ((TextField) vBox.getChildren().get(indexUsername));
        PasswordField passwordField = ((PasswordField) vBox.getChildren().get(indexPassword));
        PasswordField passwordField2 = ((PasswordField) vBox.getChildren().get(indexPassword2));
        TextField statusField = ((TextField) vBox.getChildren().get(indexStatus));

        String username = usernameField.getText();
        String password = passwordField.getText();
        String password2 = passwordField2.getText();
        String status = statusField.getText();
        // cannot contain whitespaces or commas use for loop
        // regex for only letters and numbers
        String regex = "^[a-zA-Z0-9]+$";
        if (!username.matches(regex) || !password.matches(regex) || !password2.matches(regex) || !status.matches(regex)) {
            return null;
        }
        if (!password.equals(password2)) {
            return null;
        }
        try {
            int statusInt = Integer.parseInt(status);
            if (statusInt != 1 && statusInt != 2) {
                return null;
            }
            User user = new User(username, password, statusInt);
            if (checkExists(username, password, new File(".\\src\\main\\java\\database\\storage\\login.txt")) == null) {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO login VALUES(?, ?, ?)");
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setInt(3, statusInt);
                ps.executeUpdate();
                return user;
            }
        } catch (Exception e) {
            System.out.println("Error when registering");
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void deleteData(String tableName, String idString) throws ClassNotFoundException {
        try {
            String[] ids = idString.split(",");
            String sql = "DELETE * FROM " + tableName + "WHERE " + ids[0] + " = " + ids[1];
            conn.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getCart(String user_id) throws ClassNotFoundException {
        return 0;
    }

    public void writeData(String s, File f) throws ClassNotFoundException {
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(s);
            ps.setString(1, s);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

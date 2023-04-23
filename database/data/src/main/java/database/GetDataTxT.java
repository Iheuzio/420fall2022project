package database;

import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

import java.io.*;
import java.util.Arrays;

public class GetDataTxT implements GetData {

    /**
     * @description: Reads data from a txt file and then returns it as an ArrayList
     */
    @Override
    public ArrayList<ArrayList<String>> readData(String path) {
        File f = new File(path);
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        Scanner sc = null;

        try {
            sc = new Scanner(f);
            if (!f.exists() || f.isDirectory()) {
                System.out.println("File is a directory or doesn't exist");
                // null so verified later in classes when reading data
                return null;
            }
            // tabs based on csv file to read by between values
            // sc.useDelimiter("\n");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",");
                data.add(new ArrayList<>(Arrays.asList(parts)));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found for read data");
            System.out.println(e.getMessage());
        } finally {
            if (sc != null) {
                sc.close();
            }
        }

        return data;
    }
    /**
     * @description: Logs the user in based on the values retrieved and checks if the user exists
     * @param stage - the gui stage for ui
     * @param indexUsername - username of the user as index in gui
     * @param indexPassword - password of the user through the password field in gui (as index)
     */
    @Override
    public User login(Stage stage, int indexUsername, int indexPassword) {
        File f = new File(".\\src\\main\\java\\database\\storage\\login.txt");
        if (!f.exists() || f.isDirectory()) {
            return null;
        }
        VBox vBox = (VBox) stage.getScene().getRoot().getChildrenUnmodifiable().get(0);
        TextField usernameField = ((TextField) vBox.getChildren().get(indexUsername));
        PasswordField passwordField = ((PasswordField) vBox.getChildren().get(indexPassword));

        String username = usernameField.getText();
        String password = passwordField.getText();
        // cannot contain whitespaces or commas since we are using this later in a file 
        String regex = "^[a-zA-Z0-9]+$";
        if (!username.matches(regex) || !password.matches(regex)) {
            return null;
        }
        try {
            User user = checkExists(username, password, ".\\src\\main\\java\\database\\storage\\login.txt");
            // System.out.println(user);
            if (user != null) {
                return user;
            }
        } catch (Exception e) {
            System.out.println("File not found for login");
            System.out.println(e.getMessage());
        }
        return null;
    }
    /**
     * @description Manages the register based on the values retrieved from the gui, if the user is allowed to register based on constraints here 
     * @param stage - the gui stage for ui
     * @param indexUsername - username of the user as index in gui
     * @param indexPassword - password of the user through the password field in gui (as index)
     * @param indexPassword2 - password of the user through the password field in gui (as index)
     * @param indexStatus - status of the user through the textfield in gui (as index)
     */
    @Override
    public User manageRegister(Stage stage, int indexUsername, int indexPassword1, int indexPassword2,int indexStatus) {
        Scanner sc = null;
        FileWriter fw = null;

        try {
            File f = new File(".\\src\\main\\java\\database\\storage\\login.txt");
            if (!f.exists() || f.isDirectory()) {
                return null;
            }

            VBox vBox = (VBox) stage.getScene().getRoot().getChildrenUnmodifiable().get(0);
            TextField usernameField = (TextField) vBox.getChildren().get(indexUsername);
            PasswordField passwordField = (PasswordField) vBox.getChildren().get(indexPassword1);
            PasswordField passwordField2 = (PasswordField) vBox.getChildren().get(indexPassword2);
            TextField statusField = (TextField) vBox.getChildren().get(indexStatus);

            String username1 = usernameField.getText();
            String password1 = passwordField.getText();
            String password3 = passwordField2.getText();
            String statusCheck = statusField.getText();

            sc = new Scanner(statusCheck);
            int status1 = sc.hasNextInt() ? Integer.parseInt(statusField.getText()) : 1;
            // status here can only equal 1 or 2 else its 1 by default
            status1 = (status1 == 1 || status1 == 2) ? status1 : 1;
            String regex = "^[a-zA-Z0-9]+$";
            if (!username1.matches(regex) || !password1.matches(regex)) {
                return null;
            }
            if (password1.equals(password3)) {
                if (checkExists(username1, password1, f.getAbsolutePath()) != null) {
                    return null;
                }
                // stores in the .txt file we defined above 
                fw = new FileWriter(f, true);
                fw.write('\n' + username1 + "," + password1 + "," + status1);
                fw.close();
            }
            // this is needed to check if the user exists in the login.txt file
            return checkExists(username1, password3, f.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sc != null) {
                sc.close();
            }
            if (fw != null) {
                try {
                    fw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * @description: Checks if the user exists in the login.txt file. Cannot override due to it being private only needed here though
     * @param username - username of the user
     * @param password - password of the user
     * @param f - file path to the login.txt file
     * @return
     * @throws FileNotFoundException
     * @throws ClassNotFoundException
     */
    private static User checkExists(String username, String password, String path) throws FileNotFoundException, ClassNotFoundException {
        File f = new File(path);
        Scanner in = new Scanner(f);
        if (username.equals("") || password.equals("")) {
            in.close();
            return null;
        }
        try {
            while (in.hasNextLine()) {
                String line = in.nextLine();
                String[] parts = line.split(",");
                // 2 array vals since we'll have [["bob"], ["joe"]]
                String username2 = parts[0];
                String password2 = parts[1];
                int status2 = Integer.parseInt(parts[2]);
                // check if users match and passwords different reutrn null if so
                if (username.equals(username2) && !password.equals(password2)) {
                    in.close();
                    return null;
                }
                if (username.equals(username2) && password.equals(password2)) {
                    in.close();
                    return new User(username2, password2, status2);
                }
            }
        } finally {
            // closes the scanners since memory leak stuff if the teacher cares about that
            if (in != null) {
                in.close();
            }
        }
        return null;

    }

    /**
     * 
     * @param id - key to use to return a value from file or whatever id (we could've used this for the ingredients if we had time)
     * @param path -  path to the file
     * @return - returns the value of the key (id) in the file
     * @throws ClassNotFoundException
     */
    public int getDataKey(String id, String path) throws ClassNotFoundException {
        File f = new File(path);
        ArrayList<ArrayList<String>> user_vals = readData(f.getAbsolutePath());
        // if the file is empty then it is invalid so -1 here
        if (user_vals.size() == 0 || user_vals == null) {
            return -1;
        }
        for (ArrayList<String> arr : user_vals) {
            if (arr.get(1).equals(id)) {
                // returns the exact value of the key if found
                return Integer.parseInt(arr.get(0));
            }
        }
        int last_key_val = Integer.parseInt(user_vals.get(user_vals.size() - 1).get(0)) + 1;
        writeData(last_key_val + "," + id, f.getAbsolutePath());
        return last_key_val;
    }

    /**
     * @description - writes data to the file based on the string passed to it
     * @param s - the string to write to the file
     * @param path - the path to the file
     */
    public void writeData(String s, String path) {
        File f = new File(path);
        FileWriter fw = null;
        try {
            // System.out.println("Writing to file: " + f.getAbsolutePath() + " with data: " + s);
            fw = new FileWriter(f, true);
            if (f.length() != 0) {
                fw.write('\n');
            }
            for (String val : s.split(",")) {
                fw.write(val + ",");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @description Takes a file path and deletes the first item matching that key
     * @param path - path to the file
     * @param to_delete - the key to delete
     */
    public void deleteData(String path, String to_delete) {
        File f = new File(path);
        ArrayList<ArrayList<String>> data = readData(f.getAbsolutePath());
        for (ArrayList<String> arr : data) {
            System.out.println(arr.get(0) + " " + to_delete);
            if (arr.get(0).equals(to_delete)) {
                System.out.println(arr.get(0) + " " + arr.get(1) + " " + arr.get(2));
                data.remove(arr);
                break;
            }
        }

        clearData(f.getAbsolutePath());
        String data_str = "";
        for (ArrayList<String> arr : data) {
            for (String s : arr) {
                data_str += s + ",";
            }
            // initialize new line
            data_str += '\n';
        }
        // remove the last comma
        data_str = data_str.substring(0, data_str.length() - 1);
        writeData(data_str, f.getAbsolutePath());
    }       
    /**
     * @description: Creates a new cart file for the user under this alias 
     * @param user_id
     */
    public void createCart(String user_id) {
        File f = new File(".\\src\\main\\java\\database\\storage\\c_" + user_id + ".txt");
        try {
            // if the user deleted it, who knows 
            if (!f.exists()) {
                f.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * @description - clears a file of contents so its basically empty
     * @param path - path to the file
     */
    public void clearData(String path) {
        try {
            FileWriter fw = new FileWriter(new File(path));
            fw.write("");
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

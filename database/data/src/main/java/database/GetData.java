package database;

import java.util.ArrayList;
import javafx.stage.Stage;

import java.io.File;

public interface GetData {
    public ArrayList<ArrayList<String>> readData(String path) throws ClassNotFoundException;
    // public User login();
    public User login(Stage stage, int indexUsername, int indexPassword);
    public User manageRegister(Stage stage, int indexUsername, int indexPassword, int indexPassword2, int indexStatus);
    public void deleteData(String path, String delete_key) throws ClassNotFoundException;
}
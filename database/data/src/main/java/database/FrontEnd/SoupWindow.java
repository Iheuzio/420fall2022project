package database.FrontEnd;

import database.GetData;
import database.GetDataTxT;
import database.User;

import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;

public class SoupWindow {
    private static final String SOUP_PATH = ".\\src\\main\\java\\database\\storage\\soup.txt";
    private Stage stage;
    private User user;
    private GetDataTxT getData = new GetDataTxT();
    // private static final String INGREDIENT_PATH = ".\\src\\main\\java\\database\\storage\\ingredient.txt";
    // private static final String COUPON_PATH = ".\\src\\main\\java\\database\\storage\\coupon.txt";

    public SoupWindow(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
    }

    public void openSoupNav() {
        int sceneWidth = 800;
        int sceneHeight = 600;
        double space = ((double)sceneWidth) / 40;
        double scrollbarWidth = 4;
        //added a tiny offset to the width to make sure the scroll bar shows up correctly
        double buttonWidth = (((double)sceneWidth) / 3) - space - scrollbarWidth;

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(space, space, space, scrollbarWidth*3));
        grid.setVgap(space);
        grid.setHgap(space);
        
        readSoups(grid, buttonWidth);

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        
        Scene scene = new Scene(scroll,sceneWidth,sceneHeight);
        stage.setTitle("Soup Store - Buy Soup, now!");
        stage.setScene(scene);
        stage.show();
    }

    public void readSoups(GridPane grid, double btnSize) {
        ArrayList<ArrayList<String>> arr = null;
        int i = 0;
        int j = 0;
        
        try {
            arr = getData.readData(SOUP_PATH);

            for (i = 0; i < arr.size(); i++) {
                for (j = 0; (i * 3) + j < arr.size() && j < 3; j++) {
                    Button btn = new Button(arr.get((i * 3) + j).get(0).replaceAll("-", " "));
                    btn.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent aEvent) {
                            Button btn = (Button)aEvent.getSource();
                            SoupBuyingWindow soupBuyingWindow = new SoupBuyingWindow(btn.getText(), stage, user, getData);
                            soupBuyingWindow.openSoup();
                        }
                    });

                    btn.setPrefHeight(btnSize);
                    btn.setPrefWidth(btnSize);
                    btn.setFont(Font.font("Impact", FontWeight.BOLD, 16));
                    grid.add(btn, j, i);
                }
            }

            Button backButton = new Button("Back");
            backButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent aEvent) {
                    Store appWindow = new Store();
                    appWindow.setUser(user);
                    appWindow.openMain();
                }
            });
            backButton.setPrefHeight(btnSize);
            backButton.setPrefWidth(btnSize);
            backButton.setFont(Font.font("Impact", FontWeight.BOLD, 16));
            grid.add(backButton, j, (j == 2) ? i + 1 : i);
        } catch(Exception e) {
            System.out.println("Error reading soups. There might not be any soups.");
        }
    }
}
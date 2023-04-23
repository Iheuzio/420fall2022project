package database.FrontEnd;

import database.*;

import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;

public class CouponWindow {
    private static final String COUPON_PATH = ".\\src\\main\\java\\database\\storage\\coupons.txt";
    private static GetData getData = new GetDataTxT();
    // private static final String INGREDIENT_PATH = ".\\src\\main\\java\\database\\storage\\ingredient.txt";
    // private static final String COUPON_PATH = ".\\src\\main\\java\\database\\storage\\coupon.txt";

    public static void openCoupons(Stage stage, Store appWindow) {
        stage.setTitle("Soup Store - Coupons");
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
        
        readCoupons(grid, buttonWidth, appWindow);

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        
        Scene scene = new Scene(scroll,sceneWidth,sceneHeight);
        stage.setTitle("Soup Store - Buy Soup, now!");
        stage.setScene(scene);
        stage.show();
    }

    public static void readCoupons(GridPane grid, double btnSize, Store appWindow) {
        ArrayList<ArrayList<String>> arr = null;
        int i = 0;
        
        try {
            arr = getData.readData(COUPON_PATH);

            for (i = 0; i < arr.size(); i++) {
                Text text = null;
                String s = arr.get(i).get(0);
                if (s.charAt(0) == 'p') {
                    s = "Percentage Discount";
                } else {
                    s = "Dollar Discount";
                }
                
                s = s + ", id:" + arr.get(i).get(0) + ", " + arr.get(i).get(1);

                if (s.charAt(0) == 'p') {
                    s = s + "%";
                } else {
                    s = s + "$";
                }

                text = new Text(s);
                text.setFont(Font.font("Impact", FontWeight.BOLD, 16));
                grid.add(text, 0, i);
            }

            Button backButton = new Button("Back");
            backButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent aEvent) {
                    appWindow.openMain();
                }
            });
            backButton.setPrefHeight(btnSize);
            backButton.setPrefWidth(btnSize/4);
            backButton.setFont(Font.font("Impact", FontWeight.BOLD, 16));
            grid.add(backButton, 0, i);
        } catch(Exception e) {
            System.out.println("Error reading coupons. There might not be any coupons.");
            System.out.println(e.getMessage());
        }
    }
}
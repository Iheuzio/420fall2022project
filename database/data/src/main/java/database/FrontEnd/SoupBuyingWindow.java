package database.FrontEnd;

import database.*;

import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.geometry.*;

import java.util.ArrayList;
import java.util.Scanner;

import java.io.File;

public class SoupBuyingWindow {
    private static final String SOUP_PATH = ".\\src\\main\\java\\database\\storage\\soup.txt";
    private static final String COUPON_PATH = ".\\src\\main\\java\\database\\storage\\coupons.txt";
    private SoupType soup;
    private Stage stage;
    private User currentUser;
    private GetDataTxT getData;

    public SoupBuyingWindow(String soupName, Stage stage, User user, GetDataTxT getData) {
        this.soup = getSoup(soupName.replaceAll(" ", "-"));
        this.stage = stage;
        this.currentUser = user;
        this.getData = getData;
    }

    public void openSoup() {
        Group root = new Group();
        Scene scene = new Scene(root,800,600);
        stage.setTitle("Soup Store - Soup - " + soup.getSoupId().replaceAll("-", " "));
        // stage.setScene(scene);
        // stage.show();

        //Center stuff
        VBox verticalBox = new VBox();
        verticalBox.setSpacing(12);
        verticalBox.setAlignment(Pos.TOP_CENTER);
        verticalBox.layoutXProperty().bind(scene.widthProperty().subtract(verticalBox.widthProperty()).divide(2));

        //Top stuff
        HBox top = new HBox();
        top.setSpacing(12);
        top.setAlignment(Pos.TOP_CENTER);

        //Bottom stuff
        HBox bottom = new HBox();
        bottom.setSpacing(12);
        bottom.setAlignment(Pos.BOTTOM_CENTER);
        Button backBtn = new Button("Back");
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SoupWindow soupWindow = new SoupWindow(stage, currentUser);
                soupWindow.openSoupNav();
            }
        });
        TextField amount = new TextField();
        amount.setPromptText("Amount of ingredients - Defaults to 1");
        amount.setPrefWidth(80);
        TextField coupon = new TextField();
        coupon.setPromptText("Coupon Code");
        coupon.setPrefWidth(80);
        Button addBtn = new Button("Add to Cart");
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String numText = amount.getText();
                String couponText = coupon.getText();
                Scanner sc = null;
                try {
                    sc = new Scanner(numText);
                    int num = sc.hasNextInt() ? Integer.parseInt(amount.getText()) : 1;
                    num = (num > 0) ? num : 1;
                    String spName = soup.getSoupId();
                    double price = getCouponPrice(couponText,soup.getPrice());
                    getData.writeData(spName + "," + price*num + "," + num, ".\\src\\main\\java\\database\\storage\\c_" + currentUser.getName() + ".txt");
                } catch (Exception e) {
                    System.out.println("Error writing to file");
                } finally {
                    if (sc != null) {
                        sc.close();
                    }
                }
            }
        });

        //Add stuff to top
        Text title = new Text(soup.getSoupId().replaceAll("-", " ") + " - Ingredients :");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        top.getChildren().add(title);
        
        //Add stuff to center
        Text soupNameText = new Text(soup.getSoupId().replaceAll("-", " "));
        soupNameText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        verticalBox.getChildren().add(soupNameText);

        //Add stuff to bottom
        bottom.getChildren().add(backBtn);
        bottom.getChildren().add(addBtn);
        bottom.getChildren().add(amount);
        bottom.getChildren().add(coupon);

        //Add stuff to root
        root.getChildren().add(verticalBox);
        verticalBox.getChildren().add(top);
        verticalBox.getChildren().add(bottom);
        applyIngredients(verticalBox, soup);

        stage.setScene(scene);
        stage.show();
    }

    // public void applyIngredients(VBox box, String soupName) {
    //     GetDataCSV data = new GetDataCSV();
    //     ArrayList<ArrayList<String>> soupData = data.readData(SOUP_PATH);
    //     String ingredientList = "";

    //     for (ArrayList<String> soup : soupData) {
    //         if (soup.get(0).equals(soupName)) {
    //             for (int i = 2; i < soup.size(); i++) {
    //                 if (i == 2) {
    //                     ingredientList += toTitleCase(soup.get(i)).replaceAll("-", " ");
    //                 } else {
    //                     ingredientList += ", " + toTitleCase(soup.get(i)).replaceAll("-", " ");
    //                 }
    //             }
    //         }
    //     }

    //     Text ingredients = new Text(ingredientList);
    //     ingredients.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
    //     box.getChildren().add(ingredients);
    // }

    public void applyIngredients(VBox box, SoupType soup) {
        String ingredientList = "";

        Ingredient[] ingredients = soup.getIngredients();

        for (int i = 0; i < ingredients.length; i++) {
            if (i == 0) {
                ingredientList += toTitleCase(ingredients[i].getId()).replaceAll("-", " ");
            } else {
                ingredientList += ", " + toTitleCase(ingredients[i].getId()).replaceAll("-", " ");
            }
        }        


        Text ing = new Text(ingredientList);
        ing.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
        box.getChildren().add(ing);
    }

    // public double getSoupPrice(String soupName) {
    //     GetDataCSV data = new GetDataCSV();
    //     ArrayList<ArrayList<String>> soupData = data.readData(SOUP_PATH);
    //     double price = 0;

    //     for (ArrayList<String> soup : soupData) {
    //         if (soup.get(0).equals(soupName)) {
    //             price = Double.parseDouble(soup.get(1));
    //         }
    //     }

    //     return price;
    // }

    public SoupType getSoup(String s) {
        GetDataTxT data = new GetDataTxT();
        ArrayList<ArrayList<String>> soupData = data.readData(SOUP_PATH);
        SoupType soup = null;

        for (ArrayList<String> soupD : soupData) {
            if (soupD.get(0).equals(s)) {
                Ingredient[] ingredients = new Ingredient[soupD.size()-2];
                for (int i = 2; i < soupD.size(); i++) {
                    ingredients[i-2] = new Ingredient(soupD.get(i));
                }
                soup = new SoupType(soupD.get(0), ingredients, Double.parseDouble(soupD.get(1)));
            }
        }

        return soup;
    }

    public double getCouponPrice(String couponString, double price) {
        GetDataTxT data = new GetDataTxT();
        ArrayList<ArrayList<String>> couponData = data.readData(COUPON_PATH);
        System.out.println(price);

        for (ArrayList<String> coupon : couponData) {
            if (coupon.get(0).equals(couponString)) {
                if (coupon.get(0).charAt(0) == 'p') {
                    price = price * (1.0 - Double.parseDouble(coupon.get(1).substring(0)) / 100);
                } else if (coupon.get(0).charAt(0) == 'd') {
                    price = price - Double.parseDouble(coupon.get(1).substring(0));
                }  
            }
        }
           
        return price;
    }

    public static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuilder sb = new StringBuilder();

        for (String str : arr) {
            sb.append(Character.toUpperCase(str.charAt(0)))
                .append(str.substring(1))
                .append(" ");
        }

        return sb.toString().trim();
    }
}
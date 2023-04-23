package database.FrontEnd;

import database.Cart;
import database.GetDataTxT;
import database.User;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.ListView;

public class CartWindow {

    GetDataTxT cartData = new GetDataTxT();

    public static void open(Stage stage, User user, Store applicationTest) {
        Cart user_cart = user.getCart();
        Group root = new Group();
        Scene scene = new Scene(root, 800, 600);
        VBox center = new VBox();

        center.setSpacing(5);
        center.setAlignment(Pos.TOP_CENTER);
        center.layoutXProperty().bind(scene.widthProperty().subtract(center.widthProperty()).divide(2));
        
        Button back_button = new Button("Back");
        Button delete_button = new Button("Delete");
        Button clear_button = new Button("Clear");
        Button checkout_button = new Button("Checkout");

        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                applicationTest.openMain();

            }
        });

        stage.setTitle("Soup Store - Cart \t > " + user.getName());

        Text header = new Text(user.getName() + "'s Cart");
        header.setTextOrigin(VPos.TOP);
        header.setFont(Font.font("Impact", FontWeight.BOLD, 30));

        

        HBox search_row = new HBox();
        search_row.setSpacing(10);
        search_row.setAlignment(Pos.CENTER);

        // use a list view to display the cart items
        ListView<String> cartList = new ListView<String>();

        // add the cart items to the list view using getItemsInCart()
        if(user_cart.getItemsInCart() != null) {
            String[] cartItems = user_cart.getItemsInCart();
            for (String item : cartItems) {
                cartList.getItems().add(item);
            }
        }
        

        delete_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // for this specific item, delete it from the cart
                // get soup id from the entry in the listview
                try  {
                    String soup_id = cartList.getSelectionModel().getSelectedItem();
                    soup_id = soup_id.split("\t")[0];
                    user_cart.queryExecute("remove " + soup_id + " 1");
                    // refresh the listview
                    cartList.getItems().clear();
                    String[] cartItems = user_cart.getItemsInCart();
                
                    for (String item : cartItems) {
                        cartList.getItems().add(item);
                    }
                } catch (Exception ex) {
                    if(cartList.getSelectionModel().getSelectedItem() != null) {
                        cartList.getItems().clear();
                    }
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("Error: Cart is empty or item is not selected");
                    alert.showAndWait();
                }
            }
        });

        clear_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Confirmation");
                alert.setContentText("Are you sure you want to clear your cart?");
                alert.showAndWait();

                if (alert.getResult().getText().equals("OK")) {
                    user_cart.queryExecute("clear");
                    cartList.getItems().clear();
                } 
                return;
            }
        });

        checkout_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                double user_total = user_cart.checkout();
                if(user_total == 0.00) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("Error: Cart is empty");
                    alert.showAndWait();
                    return;
                }
                // refresh the listview
                cartList.getItems().clear();
                for (String item : user_cart.getItemsInCart()) {
                    cartList.getItems().addAll(item);
                }

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Checkout");
                alert.setHeaderText("Checkout");
                alert.setContentText("Your total is: $" + user_total);
                alert.showAndWait();
                if (alert.getResult().getText().equals("OK")) {
                    user_cart.queryExecute("clear");
                    cartList.getItems().clear();

                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setTitle("Checkout");
                    alert2.setHeaderText("Checkout");
                    alert2.setContentText("Purchase complete!");
                    alert2.showAndWait();

                    applicationTest.openMain();
                } else {
                    return;
                }
            }
        });

       

        // add the buttons to the bottom of the screen
        HBox buttons = new HBox();
        buttons.setSpacing(40);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(back_button, delete_button, clear_button, checkout_button);

        TextField search = new TextField();
        search.setPromptText("Execute");
        search.setPrefWidth(200);

        Button search_button = new Button("Execute");
        search_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if(search.getText().contains("list_products")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Search");
                    alert.setHeaderText("Search");
                    // attaches a list of the products to the alert window 
                    alert.setContentText(user_cart.listProducts());
                    alert.showAndWait();
                }
                user_cart.queryExecute(search.getText());
                // refresh the listview
                cartList.getItems().clear();
                for (String item : user_cart.getItemsInCart()) {
                    cartList.getItems().addAll(item);
                }
            }
        });

        Text placeholder = new Text("Soup-id \t\t\t Price $ \t\t\t Quantity");
        if (user.getStatus() == 2) {
            Text input_fields = new Text("- add [soupid][amount]\t   <Adds soup> \n - remove [soupid]      <Removes soup> \n - clear\t\t      <Clears cart> \n - list_products\t   <Lists cart>");
            input_fields.setTextOrigin(VPos.TOP);
            input_fields.setFont(Font.font("Liberation Serif", FontWeight.LIGHT, 12));
            search_row.getChildren().addAll(search, search_button);
            
            center.getChildren().addAll(header,input_fields,search_row,placeholder,cartList, buttons);
        } else {
            center.getChildren().addAll(header,placeholder,cartList,buttons);
        }

        root.getChildren().add(center);
        stage.setScene(scene);
    }
}

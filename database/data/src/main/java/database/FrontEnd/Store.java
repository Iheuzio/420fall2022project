package database.FrontEnd;

import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.geometry.*;

import database.*;
import database.User;

// Manages the login and
// the main page options only
// Other classes manage the other pages
// that are opened from the main page
public class Store extends Application { 	
    private static Stage stage;
    private User currentUser;
    Group root;
    VBox center;
    Scene scene;
    GetDataTxT getData = new GetDataTxT();

    public static void main(String[] args) {
        launch(args);
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    @Override
    public void start(Stage startStage) throws Exception {
        stage = startStage;
        openLogin();
        startStage.setResizable(false);
        startStage.show();
    }

   /**
    * @description Opens the login window
    */
    public void openLogin() {
        //Login menu
        stage.setTitle("Soup Store - Login");
        root = new Group();
        scene = new Scene(root,800,600);
        center = new VBox();

        //Creates a object to center the menu on the screen so everything is in the center
        // We used the basis from: https://stackoverflow.com/questions/18927641/javafx-centering-a-vbox      
        center.setSpacing(12);
        center.setAlignment(Pos.TOP_CENTER);
        center.layoutXProperty().bind(scene.widthProperty().subtract(center.widthProperty()).divide(2));
        center.layoutYProperty().bind(scene.heightProperty().subtract(center.heightProperty().get() + 240.0 * 2).divide(2));
    
        //Just the welcome message
        Text text = new Text(stage.getTitle());
        text.setTextOrigin(VPos.TOP);
        text.setFont(Font.font("Impact", FontWeight.BOLD, 30));

      
        TextField username = new TextField();
        username.setPromptText("Username");
        username.setPrefWidth(200);

        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.setPrefWidth(200);

        // Button for loging in 
        Button loginBtn = new Button("LOGIN");
        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent aEvent) {
                currentUser = getData.login(stage, 1, 2);
                // if the user is defined, meaning not null, then this will open the main page
                if (currentUser != null) { 
                    // System.out.println(currentUser);
                    openMain();
                } else {
                    // user is null
                    alert(1);
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Login Error");
                    alert.setContentText("Invalid username or password");
                    alert.showAndWait();
                    promptRegister();
                }
            }

            private void alert(int i) {
            }
        });
        loginBtn.setPrefHeight(12.0);
        loginBtn.setPrefWidth(80.0);
        loginBtn.setFont(Font.font("Impact", FontWeight.BOLD, 16));

        //adding children
        for(Node n : new Node[]{text, username, password, loginBtn}) {
            center.getChildren().add(n);
        }
        root.getChildren().add(center);
        stage.setScene(scene);
    }

   /**
    * @description Opens the main page with the menu options as buttons
    */
    public void openMain() {
        //Main menu
        root = new Group();
        scene = new Scene(root,800,600);
        center = new VBox();
        stage.setTitle("Soup Store - Menu");

        HBox row1 = new HBox();
        HBox row2 = new HBox();

        //Centers the prompts
        center.setSpacing(12);
        center.setAlignment(Pos.TOP_CENTER);
        center.layoutXProperty().bind(scene.widthProperty().subtract(center.widthProperty()).divide(2));

        //spacing between the rows
        int space = (int) scene.widthProperty().subtract(center.widthProperty()).divide(30).get();
        
        for(HBox row : new HBox[]{row1, row2}) {
            row.setSpacing(space);
            row.setAlignment(Pos.TOP_CENTER);
            row.layoutXProperty().bind(scene.widthProperty().subtract(row.widthProperty()).divide(2));
        }
        row1.layoutYProperty().bind(scene.heightProperty().subtract(row1.heightProperty().get() + 240.0 * 2).divide(2));
        row2.layoutYProperty().bind(scene.heightProperty().subtract(row2.heightProperty().get() + 240.0 * 2).divide(2).add(120.0));

        
        stage.setTitle("Soup Store - Welcome \t > " + currentUser.getName());
        Text text = new Text("Welcome > " + currentUser.getName());
        text.setTextOrigin(VPos.TOP);
        text.setFont(Font.font("Impact", FontWeight.BOLD, 30));

        //Buttons
        double btnWidth = scene.widthProperty().subtract(center.widthProperty()).divide(2).get() - space;
        Button coupons = new Button("COUPONS");
        Button btnSoup = new Button("SOUPS");
        Button btnCart = new Button("CART");
        Button button_logout = new Button("LOGOUT");
        
        btnSoup.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * @description Opens the soup page
             * @param aEvent
             */
            @Override
            public void handle(ActionEvent aEvent) {
                SoupWindow sp = new SoupWindow(stage, currentUser);
                sp.openSoupNav();
            }
        });
       
        btnCart.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * @description Opens the cart page
             * @param aEvent
             */
            @Override
            public void handle(ActionEvent aEvent) {
                CartWindow.open(stage, currentUser, Store.this);
            }
        });
  
        coupons.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * @description Opens the coupons page
             * @param aEvent
             */
            @Override
            public void handle(ActionEvent aEvent) {
                CouponWindow.openCoupons(stage, Store.this);
            }
        });

        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * @description: Logs out the user and returns to the login page
             * @param aEvent
             */
            @Override
            public void handle(ActionEvent aEvent) {
                openLogin();
            }
        });

        // format all buttons the same way
        for (Button b : new Button[]{btnSoup, btnCart, coupons, button_logout}) {
            // set button gradiant color to grey and white checked: https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Button.html
            b.setStyle("-fx-background-color: linear-gradient(#ffffff, #dcdcdc), linear-gradient(#dcdcdc 0%, #dcdcdc 20%, #ffffff 100%), linear-gradient(#dcdcdc, #dcdcdc);");
            b.setEffect(new DropShadow());
            b.setPrefHeight(80);
            b.setPrefWidth(btnWidth);
            b.setFont(Font.font("Impact", FontWeight.BOLD, 20));
        }
        // append all buttons to the rows, seperated by the space so not overlapse or continuation since no overflow
        for(Node n : new Node[]{btnSoup, btnCart}) {
            row1.getChildren().add(n);
        }
        for(Node n : new Node[]{coupons, button_logout}) {
            row2.getChildren().add(n);
        }

        center.getChildren().add(text);

        for(Node n : new Node[]{row1,row2, center}) {
            root.getChildren().add(n);
        }
        stage.setScene(scene);
    }

    /**
     * @description: Opens the register page and prompts the user to register
     */
    public void promptRegister() {
        root = new Group();
        stage.setTitle("Soup Store - Would you like to register?");
        scene = new Scene(root,800,600);
        center = new VBox();

        //Center stuff
        center.setSpacing(10);
        center.setAlignment(Pos.CENTER);
        center.layoutXProperty().bind(scene.widthProperty().subtract(center.widthProperty()).divide(2));

        // Prompt the user for registration if they don't have an account
        Text text = new Text("Your account isn't detected, please create a new account");
        text.setTextOrigin(VPos.TOP);
        text.setFont(Font.font("Impact", FontWeight.BOLD, 30));

        Button prompt_yes = new Button("Yes");
        prompt_yes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent aEvent) {
                openRegister();
            }
        });
        prompt_yes.setPrefHeight(30);
        prompt_yes.setPrefWidth(200);
        prompt_yes.setFont(Font.font("Impact", FontWeight.BOLD, 16));

        Button prompt_no = new Button("No");
        prompt_no.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent aEvent) {
                openLogin();
            }
        });
        prompt_no.setPrefHeight(30);
        prompt_no.setPrefWidth(200);
        prompt_no.setFont(Font.font("Impact", FontWeight.BOLD, 16));

        for (Node n : new Node[]{text, prompt_yes, prompt_no}) {
            center.getChildren().add(n);
        }
        root.getChildren().add(center);
        stage.setScene(scene);
    }

    /**
     * @description: Opens the register page
     */
    public void openRegister() {
        root = new Group();
        scene = new Scene(root,800,600);
        stage.setTitle("Soup Store - Registering");

        center = new VBox();
        center.setSpacing(40);
        center.setAlignment(Pos.CENTER);
        center.layoutXProperty().bind(scene.widthProperty().subtract(center.widthProperty()).divide(2));

        Text text = new Text("Registering account for new user");
        text.setTextOrigin(VPos.TOP);
        text.setFont(Font.font("Impact", FontWeight.BOLD, 30));
        
        TextField username = new TextField();
        username.setPromptText("Username");
        username.setPrefWidth(200);

        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.setPrefWidth(200);

        PasswordField password_verify = new PasswordField();
        password_verify.setPromptText("Confirm Password");
        password_verify.setPrefWidth(200);

        TextField status = new TextField();
        status.setPromptText("Status - 1 or 2, invalid input will be set to 1");
        status.setPrefWidth(200);

        Button btn = new Button("Register");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * @description: Registers the user and returns to the login page if user wants to login again
             */
            @Override
            public void handle(ActionEvent aEvent) {
                GetDataTxT gd = new GetDataTxT();
                try {
                    currentUser = gd.manageRegister(stage, 1, 2, 3, 4);
                    if (currentUser != null) {
                        // uses an alert object to display a message to the user that they have successfully registered 
                        // checked: https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Alert.html
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Registration Successful");
                        alert.setHeaderText("Welcome to the Soup Store! \n \t >" + currentUser.getName());
                        alert.setContentText("Your account has been created successfully");
                        alert.showAndWait();
                        openMain();
                    } else {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Login Error");
                        alert.setContentText("Invalid username or password or user already exists");
                        alert.showAndWait();
                        openRegister();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        for(Node n : new Node[]{text, username, password, password_verify, status, btn}) {
            center.getChildren().add(n);
        }
        root.getChildren().add(center);
        stage.setScene(scene);
    }
} 

package database;

public class User {
    private String user_id; 
    private String password;
    private int status;
    private Cart cart;

    public User(String user_id, String password, int status) {
        this.user_id = user_id;
        this.password = password;
        this.status = status;
        try {
            this.cart = new Cart(user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // getters for this constructor and the cart will be used later in the methods 
    public String getUserId() {
        return this.user_id;   
    }

    public Cart getCart() {
        return this.cart;
    }

    public String getPassword() {
        return this.password;
    }

    public int getStatus() {
        return this.status;
    }
    // overriden toString method to show that the user is not null and contains actual values ( can be anything but I chose to show this )
    @Override
    public String toString() {
        return "User Id: " + this.user_id + ", Password: " + this.password + ", Status: " + this.status;
    }

    public String getName() {
        return this.user_id;
    }
    
}

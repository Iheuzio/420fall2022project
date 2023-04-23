package database;

import java.util.ArrayList;

public interface CartRetrieve {
    public ArrayList<String> getCart() throws ClassNotFoundException;
    public int getCartId() throws ClassNotFoundException;
    public int getUserId() throws ClassNotFoundException;
    public int getProductId() throws ClassNotFoundException;
    public int getQuantity() throws ClassNotFoundException;
    public double getPrice() throws ClassNotFoundException;
    public double getDiscount() throws ClassNotFoundException;
    public String getCoupon() throws ClassNotFoundException;
}
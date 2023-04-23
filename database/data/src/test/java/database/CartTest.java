package database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CartTest {
    final private double Epsilon = 0.0000001;

    @Test
    public void cartTestWorks() {
        assertTrue(true);
    }

    @Test
    public void userIdTest() {
        String id = "eeee";
        Cart cart = null;

        try {
            cart = new Cart(id);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        boolean result = cart.getUserId().equals(id);
        //for some reason, assertEquals is not working?
        assertTrue(result);
    }
}
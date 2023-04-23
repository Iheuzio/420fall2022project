package database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DiscountCouponTest {
    final private double Epsilon = 0.0000001;

    @Test
    public void discountCouponTestWorks() {
        assertTrue(true);
    }

    @Test
    public void discountCouponIdTest() {
        String id = "eeee";
        DiscountCoupon discountCoupon = null;
        int price = 10;

        discountCoupon = new DiscountCoupon(id, price);
        boolean result = discountCoupon.getCoupon().equals(id);
        //for some reason, assertEquals is not working?
        assertTrue(result);
    }

    @Test
    public void discountCouponPriceTest() {
        String id = "eeee";
        DiscountCoupon discountCoupon = null;
        int price = 10;

        discountCoupon = new DiscountCoupon(id, price);
        assertEquals(price, discountCoupon.getDiscount(), Epsilon);
    }

    @Test
    public void discountGetTypeTest() {
        String id = "eeee";
        DiscountCoupon discountCoupon = null;
        String type = "DiscountCoupon";
        int price = 10;

        discountCoupon = new DiscountCoupon(id, price);
        boolean result = discountCoupon.get_discount_type().equals(type);
        assertTrue(result);
    }


}

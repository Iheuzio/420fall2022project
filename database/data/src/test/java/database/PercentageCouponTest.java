package database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PercentageCouponTest {
    final private double Epsilon = 0.0000001;

    @Test
    public void percentageCouponTestWorks() {
        assertTrue(true);
    }

    @Test
    public void percentageCouponIdTest() {
        String id = "eeee";
        PercentCoupon percentCoupon = null;
        int price = 10;

        percentCoupon = new PercentCoupon(id, price);
        boolean result = percentCoupon.getCoupon().equals(id);
        //for some reason, assertEquals is not working?
        assertTrue(result);
    }

    @Test
    public void percentageCouponPriceTest() {
        String id = "eeee";
        PercentCoupon percentCoupon = null;
        int price = 10;

        percentCoupon = new PercentCoupon(id, price);
        assertEquals(price, percentCoupon.getDiscount(), Epsilon);
    }

    @Test
    public void percentageGetTypeTest() {
        String id = "eeee";
        PercentCoupon percentCoupon = null;
        String type = "PercentCoupon";
        int price = 10;

        percentCoupon = new PercentCoupon(id, price);
        boolean result = percentCoupon.get_discount_type().equals(type);
        assertTrue(result);
    }


}

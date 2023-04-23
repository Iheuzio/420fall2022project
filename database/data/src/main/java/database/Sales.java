package database;

public class Sales{
    private String coupon_id;
    private double discount_price;

    public Sales(String coupon_id, double discount_price) {
        this.coupon_id = coupon_id;
        this.discount_price = discount_price;
    }

    public double getDiscount() {
        return discount_price;
    }

    public String getCoupon() {
        return this.coupon_id;
    }
}
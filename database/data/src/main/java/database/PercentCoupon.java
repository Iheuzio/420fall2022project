package database;

public class PercentCoupon extends Sales{
    
    public PercentCoupon(String coupon_id, double discount_price) {
        super(coupon_id, discount_price);
    }
    public String get_discount_type() {
        return "PercentCoupon";
    }
}

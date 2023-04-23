package database;

public class DiscountCoupon extends Sales{
    
    public DiscountCoupon(String coupon_id, double discount_price) {
        super(coupon_id, discount_price);
    }
    public String get_discount_type() {
        return "DiscountCoupon";
    }
}
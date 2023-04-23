package database;

public interface Soup {
    // returns the ingredients as per the format selected (csv or through database)
    public Ingredient[] getIngredients();
    // returns the price of that specific ingredient
    public double getPrice();
    public String getSoupId();
    public String getStock();
    public String getDiscount();
    public String getSales();
}

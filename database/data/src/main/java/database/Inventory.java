package database;
// Not developed not a priority
public class Inventory {
    //private ingredient[] ingredients;//array of ingredients
    private String soupId;//soup id
    private int stock;//number of soups in stocl

    public Inventory(String soupId, int stock) {
        this.soupId = soupId;
        this.stock = stock;
    }

    public String getSoupId() {
        return soupId;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        //calls setStock in db
    }
}
package database;
// Not important to the overall project could be implemented with Inventory.java
public class Ingredient {
    private String id;
    private int cost;

    public Ingredient(String id) {
        this.id = id;
    }
    
    public Ingredient(String id,int cost) {
        this.id = id;
        this.cost = cost;
    }
    
    public String getId() {
        return this.id;
    }

    public int getCost() {
        return this.cost;
    }
}

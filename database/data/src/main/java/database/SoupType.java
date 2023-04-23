package database;
// some methods are not implemented yet since we did not use those classes 
import java.util.ArrayList;

public class SoupType implements Soup {
    private String soup_id;
    private Ingredient[] ingredients; // didn't use
    private double price;
    private GetDataTxT data = new GetDataTxT();
    private ArrayList<ArrayList<String>> soup_data = new ArrayList<ArrayList<String>>(); // this one is for the file format basically
    private ArrayList<Soup> soups = new ArrayList<Soup>(); // for returning to Carts to be parsed

    public SoupType() {
        initalizeSoups();
    }
    /**
     * @description: This method will be used to initialize the soups from the soups.txt file (Overloaded)
     * @param soup_id - The id of the soup
     * @param ingredients - The ingredients of the soup
     * @param parseDouble - The price of the soup
     */
    public SoupType(String soup_id, Ingredient[] ingredients, double parseDouble) {
        this.soup_id = soup_id;
        this.ingredients = ingredients;
        this.price = parseDouble;
    }
    /**
     * @description: This method will initialize the soups from the database based on the file pased
     */
    private void initalizeSoups() {
        this.soup_data = data.readData(".\\src\\main\\java\\database\\storage\\soup.txt");
        for (ArrayList<String> arr : this.soup_data) {
            // takes the ingredients from the soup and puts them in an array
            String[] ingredients = arr.get(1).split(",");
            Ingredient[] ing = new Ingredient[ingredients.length];
            for (int i = 0; i < ingredients.length; i++) {
                ing[i] = new Ingredient(ingredients[i]);
            }
            this.soups.add(new SoupType(arr.get(0), ing, Double.parseDouble(arr.get(1))));
        }
    }

    @Override
    public String getSoupId() {
        return this.soup_id;
    }
    @Override
    public Ingredient[] getIngredients() {
        return this.ingredients;
    }
    @Override
    public double getPrice() {
        return this.price;
    }

    /**
     * @description - returns the String of that specific soup with the ingredients and all that for validation if we used ingredients class
     * @param soup_id - the id of the soup
     * @return
     */
    public String values(String soup_id) {
       String result = "";
         for (Soup soup : this.soups) {
              if (soup.getSoupId().equals(soup_id)) {
                result = soup.getSoupId() + "," + soup.getIngredients() + "," + soup.getPrice();
              }
         }
        return result;
    }

    /**
     * @description - soup arrays are good for when we need to check x in user cart for the Cart class
     * @return
     */
    public ArrayList<Soup> getSoups() {
        return this.soups;
    }

    /**
     * 
     * @param string - if the soupid is equal to this one then it is valid else it isn't
     * @return - This soup object
     */
	public Soup getSoup(String soup_id) {
        // returns the soup object based on the soup id
        for (Soup soup : this.soups) {
            if (soup.getSoupId().equals(soup_id)) {
                return soup;
            }
        }
        return null;
	}

    // things we did not implement yet
    @Override
    public String getStock() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDiscount() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSales() {
        // TODO Auto-generated method stub
        return null;
    }
    
}

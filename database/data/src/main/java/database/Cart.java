package database;

import java.util.ArrayList;
import java.io.File;

public class Cart {
    private ArrayList<Soup> soups;
    private String user_id;
    private ArrayList<ArrayList<String>> soup_desc;
    private SoupType soup_type;
    // actual items will be stored here
    private File user_cart_file;

    // this is just for determining if the user has a cart
    GetDataTxT data = new GetDataTxT();

    public Cart(String user_id) throws ClassNotFoundException {
        this.user_cart_file = new File(".\\src\\main\\java\\database\\storage\\c_" + user_id + ".txt");
        this.soups = new ArrayList<Soup>();
        this.user_id = user_id;
        if (!this.user_cart_file.exists()) {
            data.createCart(user_id);
        }
    }
    /**
     * @description: The [soups] array will be initialized with the soups from the users cart
     */
    private void findSoups() {
        // if calling when in the search bar for admin status user just to make sure there isnt 2x the same soup
        this.soups.clear();
        this.soup_desc = data.readData(this.user_cart_file.getAbsolutePath());
        this.soup_type = new SoupType();
        for (ArrayList<String> arr : this.soup_desc) {
            this.soups.add(this.soup_type.getSoup(arr.get(0)));
        }
    }

    /**
     * @description: This is a checkout method that will be used to checkout the cart for purchasing, just sums up the total
     * @return
     */
    public double checkout() {
        double total = 0;
        // we need to get the soups first to make sure everything is updated
        findSoups();
        // every soup in the cart will be added to the total
        for (Soup soup : this.soups) {
            // total is the total price of the items in the cart * quantity
            int quantity;
            for (ArrayList<String> arr : this.soup_desc) {
                // it first checks to see if the soup id is the same as the soup id in the cart then it will take that array so like {soup_id, price, quantity} and multiplies it by the price and adds until all are added
                if (arr.get(0).equals(soup.getSoupId())) {
                    quantity = Integer.parseInt(arr.get(2));
                    total += soup.getPrice() * quantity;
                }
            }
            return total;
        }
        // shouldn't reach since we have the prompts for empty cart
        return 0;
    }

    /**
     * @description - For the search bar to execute operations to the Cart (only for admin status users) or (2)
     * @param query - the query that will be executed to the cart, This could be add, remove, clear, or list_products
     */
    public void queryExecute(String query) {
        String[] query_type = query.split(" ");
        // to avoid a user typing upper and lowercase letters we can just make it all lowercase
        switch (query_type[0].toLowerCase()) {
            case "add":
                // takes the second argument after add and then takes the int after that to go through 
                addSoup(query_type[1], Integer.parseInt(query_type[2]));
                break;
            case "remove":
                removeSoup(query_type[1], Integer.parseInt(query_type[2]));
                break;
            case "clear":
                clearCart();
                break;
            case "list_products":
                listProducts();
                break;
            default:
                // invalid query
                return;
        }
    }

    /**
     * @description - Lists all the products in the cart through a string read in the cart window for an alert in case the cart is long (could also be used as a receipt later...)
     * @return
     */
    public String listProducts() {
        findSoups();
        String soup_list = "";
        for (Soup soup : this.soups) {
            soup_list += soup.getSoupId() + "\n";
        }
        return soup_list;
    }
    /**
     * 
     * @return - user's id of this cart
     */
    public String getUserId() {
        return this.user_id;
    }
    /**
     * 
     * @param soup_id - id of the soup to be added
     * @param amount - the amount of that specific soup to be added
     */
    private void addSoup(String soup_id, int amount) {
        this.soup_desc = data.readData(this.user_cart_file.getAbsolutePath());
        for (ArrayList<String> arr : this.soup_desc) {
            if (arr.get(0).equals(soup_id)) {
                int quantity = Integer.parseInt(arr.get(2));
                quantity += amount;
                arr.set(2, Integer.toString(quantity));
                // reformats the arraylist to a string so we can actually reuse the method here
                String string = "";
                for (String s : arr) {
                    string += s + ",";
                }
                // writes the values first and then we remove the first value appearing since we added it on through quantity
                data.writeData(string, this.user_cart_file.getAbsolutePath());
                data.deleteData(user_cart_file.getAbsolutePath(), soup_id);
                return;
            }
        }
        // this only executes if the soup is not in the cart file since the above function will not contain that soup id
        soup_type = new SoupType();
        soups = soup_type.getSoups();
        String result = "";
        // checks to see if the soup is first valid in our soup list txt file then it will add it to the cart
        for (int i = 0; i < soups.size(); i++) {
            if (soups.get(i).getSoupId().equals(soup_id)) {
                result = soups.get(i).getSoupId() + "," + soups.get(i).getPrice() + "," + amount;
                data.writeData(result, this.user_cart_file.getAbsolutePath());
            }
        }
    }

    /**
     * @description - removes the first instance of the soup from the cart, meant to take in an amount and parse the file for that amount
     * @param soup_id - id of the soup to be removed
     * @param amount - amount to delete (not developed further)
     */
    private void removeSoup(String soup_id, int amount) {

        for (int i = 0; i < amount; i++) {
            for (Soup soup : soups) {
                if (soup.getSoupId().equals(soup_id)) {
                    soups.remove(soup);
                }
            }
        }
        // remove the soup from the soup description of the cart and the cart file itself
        soup_desc = data.readData(this.user_cart_file.getAbsolutePath());
        data.deleteData(user_cart_file.getAbsolutePath(), soup_id);
    }

    /**
     * @description - fetches all the soups in the cart as String[] examples: {"soup_id", "soup_price", "soup_quantity"}
     * @return
     */
    public String[] getItemsInCart() {
        ArrayList<ArrayList<String>> data_vals = data.readData(this.user_cart_file.getAbsolutePath());
        if(data_vals.size() == 0) {
            return null;
        }
        String[] items = new String[data_vals.size()];

        for (int i = 0; i < data_vals.size(); i++) {
            // formatting is a bit broken here, tabs will be all over the place
            items[i] = data_vals.get(i).get(0) + "\t\t\t" + data_vals.get(i).get(1) + "\t\t\t" + data_vals.get(i).get(2);
        }
        return items;
    }

    /**
     * @description - clears the cart data file of user
     */
    public void clearCart() {
        data.clearData(this.user_cart_file.getAbsolutePath());
    }
}

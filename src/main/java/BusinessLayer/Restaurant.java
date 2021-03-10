package BusinessLayer;

import DataLayer.BillGenerator;
import DataLayer.Serial;

import java.io.Serializable;
import java.util.*;

public class Restaurant extends Observable implements RestaurantProcessing, Serializable{

    /**
     * Implements the serialization
     */
    private static Serial s = new Serial();
    /**
     * Represents the orders
     */
    private Map<Order, ArrayList<MenuItem>> orders = new HashMap<Order, ArrayList<MenuItem>>();
    /**
     * Represents the menu items
     */
    private Set<MenuItem> menuItemsS = new HashSet<MenuItem>(); //serializable
    /**
     * Represents the menu items
     */
    private static Set<MenuItem> menuItems = s.deserialize().getMenuItemsS(); //not serializable; static to be visible in all the packages
    /**
     * Represents the validator
     */
    private Validator v = new Validator();
    /**
     * Represents the order id
     */
    private int orderId;

    public Set<MenuItem> getMenuItems() {
        return menuItems;
    }
    public Set<MenuItem> getMenuItemsS() {
        return menuItemsS;
    }

    /**
     * Checks that every item in the list is non null
     * @return false if there are null elements, true otherwise
     */
    private boolean invariant() {
        for (MenuItem i: menuItems) {
            if (i == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Serializes the restaurant
     */
    private void serialize() {
        menuItemsS = menuItems; //before serializing, the content of the static variable is moved to the serializable variable
        s.serialize(this);
    }

    public Map<Order, ArrayList<MenuItem>> getOrders() {
        return orders;
    }

    /**
     * Find a base product with a given name
     * @param name the name of the base product
     * @return the product with the given name
     */
    public MenuItem findBaseProduct(String name) {
        name = v.getSimpleString(name); //simplifies the name
        for (MenuItem i: menuItems) { //searches the base product with the given name
            if (i instanceof BaseProduct) {
                if (((BaseProduct) i).getName().equals(name)) {
                    return i; //returns the found product
                }
            }
        }
        return null;
    }

    /**
     * Find a composite product with a given list of items
     * @param items the list of items
     * @return the product with the given list
     */
    public MenuItem findCompositeProductItems(Set<MenuItem> items) {
        for (MenuItem i: menuItems) {
            if (i instanceof CompositeProduct) { //searches the composite product with the set of items given as parameter
                if (((CompositeProduct) i).getItems().equals(items)) {
                    return i;
                }
            }
        }
        return null;
    }

    /**
     * Adds in the menu a base product
     * @param product the base product
     * @pre checks the product to be non null
     * @post checks the size of the menu
     */
    @Override
    public void createNewBaseProduct(BaseProduct product) {
        assert product != null: "Null values are not accepted"; //checks the parameter
        assert invariant();
        int oldSize = menuItems.size(); //for checking that the item was inserted
        menuItems.add(product);
        serialize();
        assert menuItems.size() == oldSize + 1;
    }

    /**
     * Adds in the menu a composite product
     * @param product the composite product
     * @pre checks the product to be non null
     * @post checks the size of the menu
     */
    @Override
    public void createNewCompositeProduct(CompositeProduct product) {
        assert product != null: "Null values are not accepted"; //checks the parameter
        assert invariant();
        int oldSize = menuItems.size(); //for checking that the item was inserted
        menuItems.add(product);
        serialize();
        assert menuItems.size() == oldSize + 1;
    }

    /**
     * Deletes a menu item
     * @param item the menu item to be deleted
     * @pre checks the product to be non null
     */
    @Override
    public void deleteMenuItem(MenuItem item) {
        assert item != null: "Null values are not accepted"; //checks the parameter
        Set<MenuItem> toDelete = new HashSet<MenuItem>(); //will contain all the items which contain the item in the parameter

        for (MenuItem i: menuItems) {
            if (i instanceof BaseProduct) { //if is a base product, simply add it to toDelete set
                if (i.equals(item)) {
                    toDelete.add(i);
                }
            }
            else if (i instanceof CompositeProduct) {
                if (i.equals(item)) { //if it is a composite and it's a perfect match add it
                    toDelete.add(i);
                }
                else {
                    for (MenuItem j: ((CompositeProduct) i).getItems()) { //iterate over its list otherwise
                        if ((j instanceof BaseProduct && item instanceof BaseProduct) || (j instanceof CompositeProduct && item instanceof CompositeProduct)) {
                            if (j.equals(item)) { //if the component is a base or a composite perfectly matching then add it
                                toDelete.add(i);
                                break;
                            }
                        }
                        else if (j instanceof CompositeProduct && item instanceof BaseProduct) {
                            for (MenuItem k: ((CompositeProduct) j).getItems()) { //if not, iterate all over its list
                                if (k.equals(item)) {
                                    toDelete.add(i);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        menuItems.removeAll(toDelete); //remove all the marked items
        serialize();
        assert invariant();
    }

    /**
     * Edits the price of a base product
     * @param product the product to be edited
     * @param newPrice the new price
     * @pre checks the product to be non null
     * @pre checks the price to be non negative
     */
    @Override
    public void editMenuItem(BaseProduct product, float newPrice) {
        assert product != null: "Null values are not accepted"; //checks the parameter product
        assert newPrice > 0: "Non positive values for price are not accepted"; //checks the parameter newPrice
        assert invariant();
        for (MenuItem i: menuItems) { //re-set the price of the base product
            if (i instanceof BaseProduct) {
                if (i.equals(product)) {
                    ((BaseProduct) i).setPrice(newPrice);
                }
            }
        }
        for (MenuItem i: menuItems) { //recompute the prices of all products which contain the edited base
            if (i instanceof CompositeProduct) {
                for (MenuItem j: ((CompositeProduct) i).getItems()) {
                    if (j instanceof BaseProduct) {
                        if (j.equals(product)) {
                            i.computePrice();
                            break;
                        }
                    }
                    else if (j instanceof CompositeProduct) {
                        for (MenuItem k: ((CompositeProduct) j).getItems()) {
                            if (k.equals(product)) {
                                i.computePrice();
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }
        serialize();
        assert invariant();
    }

    /**
     * Creates a new order
     * @param items the list of menu items of the order
     * @param table the number of the table
     * @pre checks the list to be non empty
     * @pre checks the table to be grater than 0
     */
    @Override
    public void createNewOrder(ArrayList<MenuItem> items, int table) {
        assert !items.isEmpty(): "Empty lists are not accepted"; //checks the parameter items
        assert table > 0: "Non positive table numbers are not accepted"; //checks the parameter table
        Order o = new Order(); //create an order
        o.setDate(new Date());
        o.setOrderId(orderId);
        orderId++;
        o.setTable(table);
        orders.put(o, items);
        generateBill(o, items); //generate the .txt file bill when an order is placed
        serialize();
        setChanged();
        notifyObservers(o); //notify the chef
    }

    /**
     * Computes the price for an order
     * @param items the items in the order
     * @return the price
     * @pre checks the list to be non empty
     * @post checks the price to be grater than 0
     */
    @Override
    public float computePriceForOrder(ArrayList<MenuItem> items) {
        assert !items.isEmpty(): "Empty lists are not accepted"; //checks the parameter
        float price = 0; //add all the prices of the items in the order
        for (MenuItem i: items) {
            if (i instanceof BaseProduct) {
                price += ((BaseProduct) i).getPrice();
            }
            else if (i instanceof CompositeProduct) {
                price += ((CompositeProduct) i).getPrice();
            }
        }
        assert price > 0: "Non positive prices are not accepted"; //check the final computed price
        return price;
    }

    /**
     * Generates a .txt bill
     * @param order the order
     * @param items the items in the order
     * @pre checks the order to be non null
     * @pre checks the list to be non empty
     */
    @Override
    public void generateBill(Order order, ArrayList<MenuItem> items) {
        assert order != null: "Null orders are not accepted";
        assert !items.isEmpty(): "Empty lists are not accepted";
        BillGenerator g = new BillGenerator();
        g.generateBillFile(order.getOrderId(), items, computePriceForOrder(items), order.getDate());
    }
}

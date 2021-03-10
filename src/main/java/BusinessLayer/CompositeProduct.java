package BusinessLayer;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CompositeProduct extends MenuItem implements Serializable {

    private String name;
    private float price;
    private Set<MenuItem> items = new HashSet<MenuItem>();

    public CompositeProduct() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Set<MenuItem> getItems() {
        return items;
    }

    public void setItems(Set<MenuItem> items) {
        this.items = items;
    }

    @Override
    public float computePrice() {
        setPrice(0); //initially the price is 0
        for (MenuItem i: items) {
            setPrice(getPrice() + i.computePrice()); //add to the final price, the price of every item in its list
        }
        return getPrice();
    }

    public void addMenuItem(MenuItem item) {
        items.add(item);
    } //add one more item to the list

    public String toString() {
        return getName() + " " + getPrice();
    } //brings the product in a printable form
}


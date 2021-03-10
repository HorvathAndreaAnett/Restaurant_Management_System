package BusinessLayer;

import java.io.Serializable;

public class BaseProduct extends MenuItem implements Serializable {

    private String name;
    private float price;

    public BaseProduct() {
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

    @Override
    public float computePrice() {
        return getPrice();
    } //the method returns the price of the product because it's a simple product

    public String toString() {
        return getName() + " " + getPrice();
    }
}

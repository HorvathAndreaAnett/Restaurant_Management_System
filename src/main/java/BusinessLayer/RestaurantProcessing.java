package BusinessLayer;

import java.util.ArrayList;

public interface RestaurantProcessing {

    //operations for administrator
    public void createNewBaseProduct(BaseProduct product);
    public void createNewCompositeProduct(CompositeProduct product);

    public void deleteMenuItem(MenuItem item);
    public void editMenuItem(BaseProduct product, float newPrice);

    //operations for waiter
    public void createNewOrder(ArrayList<MenuItem> items, int table);
    public float computePriceForOrder(ArrayList<MenuItem> items);
    public void generateBill(Order order, ArrayList<MenuItem> items);

}

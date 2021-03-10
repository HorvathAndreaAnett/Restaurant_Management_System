package BusinessLayer;

import DataLayer.Serial;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Chef implements Observer {
    private static String processedOrder;
    private Serial s = new Serial();
    private Restaurant restaurant = s.deserialize();

    public String getProcessedOrder() {
        return processedOrder;
    }

    @Override
    public void update(Observable o, Object arg) { //method executed when notified
        restaurant = s.deserialize();
        ArrayList<MenuItem> items = restaurant.getOrders().get((Order) arg);
        processedOrder = new String();  //will contain the id of the order and the list of items
        processedOrder += "Order " + ((Order) arg).getOrderId() + ": \n";
        for (MenuItem i: items) {
            if (i instanceof BaseProduct) {
                processedOrder += ((BaseProduct) i).getName() + "\n";
            }
            else if (i instanceof CompositeProduct) {
                processedOrder += ((CompositeProduct) i).getName() + "\n";
            }
        }
    }
}

package DataLayer;

import BusinessLayer.MenuItem;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;

public class BillGenerator {

    private File output;
    private FileWriter writer;

    //create .txt file containing data about an order
    public void generateBillFile(int orderId, ArrayList<MenuItem> items, float price, Date date) {
        String name = "Bill" + orderId;
        this.output = new File(name);
        try {
            output.createNewFile();
            writer = new FileWriter(output);

            writer.write("Order id: " + orderId + "\n \n");
            writer.write("Ordered items: \n");
            for (MenuItem i: items) {
                writer.write(i.toString() + "\n");
            }
            writer.write("Total price: " + price + "\n");
            writer.write(date + "");

            writer.close();
        } catch (Exception e) {
        }
    }
}

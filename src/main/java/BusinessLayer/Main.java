package BusinessLayer;

import DataLayer.Serial;
import PresentationLayer.RestaurantGUI;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        Serial s = new Serial(args[0]);
        //Serial s = new Serial("restaurant.ser");

        //launches the main interface
        JFrame frame = new RestaurantGUI("Restaurant");
        frame.setVisible(true);
    }
}

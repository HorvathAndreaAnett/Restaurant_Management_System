package PresentationLayer;

import BusinessLayer.*;
import DataLayer.Serial;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Map;

public class ViewOrdersGUI extends JFrame {
    private JPanel mainPanel;
    private JTable orderTable;

    private Serial s = new Serial();
    private Restaurant restaurant = s.deserialize();

    //display all the previous orders in a JTable
    public ViewOrdersGUI(String title) {
        super(title);
        this.setContentPane(mainPanel);
        this.pack();

        String[][] data = new String[restaurant.getOrders().size()][5];
        String[] columnNames = {"Order id", "Order", "Price", "Date", "Table"};

        int position = 0; //counts the rows
        for (Map.Entry<Order, ArrayList<MenuItem>> i: restaurant.getOrders().entrySet()) {
            data[position][0] = String.valueOf(i.getKey().getOrderId());
            String orderS = new String();
            for (MenuItem item: i.getValue()) {
                orderS += item + " / ";
            }
            orderS = orderS.substring(0, orderS.length() - 2);
            data[position][1] = orderS;
            data[position][2] = String.valueOf(restaurant.computePriceForOrder(i.getValue()));
            data[position][3] = String.valueOf(i.getKey().getDate());
            data[position][4] = String.valueOf(i.getKey().getTable());
            position++;
        }

        DefaultTableModel tableModel = new DefaultTableModel();

        for (int i = 0; i < columnNames.length; i++) {
            tableModel.addColumn(columnNames[i]);
        }

        for (int i = 0; i < restaurant.getOrders().size(); i++) {
            tableModel.addRow(data[i]);
        }

        orderTable.setModel(tableModel);
    }
}

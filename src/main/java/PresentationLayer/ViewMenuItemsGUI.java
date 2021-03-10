package PresentationLayer;

import BusinessLayer.BaseProduct;
import BusinessLayer.CompositeProduct;
import BusinessLayer.MenuItem;
import BusinessLayer.Restaurant;
import DataLayer.Serial;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ViewMenuItemsGUI extends JFrame {

    private JPanel mainPanel;
    private JTable menuTable;

    private Serial s = new Serial();
    private Restaurant restaurant = s.deserialize();

    //displays all the menu items in a JTable
    public ViewMenuItemsGUI(String title) {
        super(title);
        this.setContentPane(mainPanel);
        this.pack();

        String[][] data = new String[restaurant.getMenuItems().size()][2];
        String[] columnNames = {"Menu item", "Price"};

        int position = 0; //counts the rows
        for (MenuItem i: restaurant.getMenuItems()) {
            if (i instanceof BaseProduct) {
                data[position][0] = ((BaseProduct) i).getName();
                data[position][1] = "" + ((BaseProduct) i).getPrice();
            }
            else if (i instanceof CompositeProduct) {
                data[position][0] = ((CompositeProduct) i).getName();
                data[position][1] = "" + ((CompositeProduct) i).getPrice();
            }
            position++;
        }

        DefaultTableModel tableModel = new DefaultTableModel();

        for (int i = 0; i < columnNames.length; i++) {
            tableModel.addColumn(columnNames[i]);
        }

        for (int i = 0; i < restaurant.getMenuItems().size(); i++) {
            tableModel.addRow(data[i]);
        }

        menuTable.setModel(tableModel);
    }
}

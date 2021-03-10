package PresentationLayer;

import BusinessLayer.Chef;
import BusinessLayer.MenuItem;
import BusinessLayer.Restaurant;
import DataLayer.Serial;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AddOrderGUI extends JFrame {
    private JPanel mainPanel;
    private JPanel jPanel;
    private JLabel addOrderLabel;
    private JLabel menuItemLabel;
    private JComboBox menuComboBox;
    private JPanel JPanel;
    private JLabel orderLabel;
    private JButton addItemButton;
    private JTextArea orderTextArea;
    private JButton finalizeOrderButton;
    private JButton cancelButton;
    private JLabel tableLabel;
    private JComboBox tableComboBox;

    private Serial s = new Serial();
    private Restaurant restaurant = s.deserialize();
    private ArrayList<MenuItem> items = new ArrayList<MenuItem>();

    public AddOrderGUI(String title) {
        super(title);
        this.setContentPane(mainPanel);
        this.pack();

        for (MenuItem item: restaurant.getMenuItems()) { //set the menu items in the combo box
            menuComboBox.addItem(item);
        }

        for (int i = 1; i <= 10; i++) { //set the tables in the combo box
            tableComboBox.addItem(i);
        }


        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                items.add((MenuItem)menuComboBox.getSelectedItem()); //append the selected item to a list of items for that order
                orderTextArea.append(((MenuItem)menuComboBox.getSelectedItem()).toString() + "\n");
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                items = new ArrayList<MenuItem>();
                orderTextArea.setText(null);
            }
        });
        finalizeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Chef chef = new Chef();
                restaurant.addObserver(chef); //observe when the order is created
                if (items.isEmpty()) {
                    JOptionPane.showMessageDialog(rootPane, "Order is empty", "Message", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    restaurant.createNewOrder(items, (int)tableComboBox.getSelectedItem());
                    JOptionPane.showMessageDialog(rootPane, "Order created successfully", "Message", JOptionPane.INFORMATION_MESSAGE);
                    items = new ArrayList<MenuItem>(); //reset the list
                    orderTextArea.setText(null);
                }
            }
        });
    }
}

package PresentationLayer;

import BusinessLayer.*;
import BusinessLayer.MenuItem;
import DataLayer.Serial;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class DeleteMenuItemGUI extends JFrame {
    private JPanel mainPanel;
    private JLabel deleteMenuItemLabel;
    private JLabel menuItemLabel;
    private JTextField menuItemTextField;
    private JButton cancelButton;
    private JButton deleteButton;

    private Validator v = new Validator();
    private Serial s = new Serial();
    private Restaurant restaurant = s.deserialize();

    private MenuItem parseItemBaseProduct(String name) {
        BaseProduct product = (BaseProduct) restaurant.findBaseProduct(v.getSimpleString(name));
        if (product == null) {
            return null;
        }
        else {
            return product;
        }
    }

    private MenuItem parseItemSimpleComposite(String name) {
        String[] tokens = name.split("[+]");
        CompositeProduct product = new CompositeProduct();
        for (String item: tokens) {
            BaseProduct base = (BaseProduct) restaurant.findBaseProduct(v.getSimpleString(item));
            if (base == null) {
                return null;
            }
            else {
                product.addMenuItem(base);
            }
        }
        if (restaurant.findCompositeProductItems(product.getItems()) == null) {
            return null;
        }
        else {
            return restaurant.findCompositeProductItems(product.getItems());
        }
    }

    private MenuItem parseItemCompositeComposite(String name) {
        Set<MenuItem> items = new HashSet<MenuItem>();
        String[] tokens = name.split("[,]");
        for (String item: tokens) {
            if (!item.contains("+")) { //base in composite
                BaseProduct base = (BaseProduct) restaurant.findBaseProduct(v.getSimpleString(item));
                if (base == null) {
                    return null;
                }
                else {
                    items.add(base);
                }
            }
            else { //composite in composite
                String[] tokensB = item.split("[+]");
                CompositeProduct composite = new CompositeProduct();
                for (String itemB: tokensB) {
                    BaseProduct base = (BaseProduct) restaurant.findBaseProduct(v.getSimpleString(itemB));
                    if (base == null) {
                        return null;
                    }
                    else {
                        composite.addMenuItem(base);
                    }
                }
                CompositeProduct existingComposite = (CompositeProduct) restaurant.findCompositeProductItems(composite.getItems());
                if (existingComposite == null) {
                    return null;
                }
                else {
                    System.out.println("In parse" + existingComposite.getName());
                    items.add(existingComposite);
                }
            }
        }
        if (restaurant.findCompositeProductItems(items) == null) {
            return null;
        }
        else {
            return restaurant.findCompositeProductItems(items);
        }
    }

    private MenuItem parseItem(String name) {
        if (!name.contains(",") && !name.contains("+")) {  //base product
            return parseItemBaseProduct(name);
        }
        else if (!name.contains(",") && name.contains("+")) { //simple composite
            return parseItemSimpleComposite(name);
        }
        else {  //composite composite product
            return parseItemCompositeComposite(name);
        }
    }

    public DeleteMenuItemGUI(String title) {
        super(title);
        this.setContentPane(mainPanel);
        this.pack();
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuItemTextField.setText(null);
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (menuItemTextField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(rootPane, "Menu item field is empty", "Message", JOptionPane.WARNING_MESSAGE);
                }
                else if (!v.isCompositeProductNameValid(menuItemTextField.getText())) {
                    JOptionPane.showMessageDialog(rootPane, "Invalid menu item name", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    String name = menuItemTextField.getText();
                    if (parseItem(v.getSimpleString(name)) == null) {
                        JOptionPane.showMessageDialog(rootPane, "Inexistent menu item", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(rootPane, "Menu item deleted successfully", "Message", JOptionPane.INFORMATION_MESSAGE);
                        restaurant.deleteMenuItem(parseItem(v.getSimpleString(name)));
                        menuItemTextField.setText(null);
                    }

                }
            }
        });
    }
}

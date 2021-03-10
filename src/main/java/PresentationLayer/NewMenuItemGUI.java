package PresentationLayer;

import BusinessLayer.BaseProduct;
import BusinessLayer.CompositeProduct;
import BusinessLayer.MenuItem;
import BusinessLayer.Restaurant;
import BusinessLayer.Validator;
import DataLayer.Serial;

import javax.swing.*;
import javax.xml.bind.ValidationEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class NewMenuItemGUI extends JFrame {

    private JPanel mainPanel;
    private JPanel insertBaseProductTitlePanel;
    private JLabel insertBaseProductLabel;
    private JPanel insertBaseProductNamePanel;
    private JLabel baseProductNameLabel;
    private JTextField baseProductNameTextField;
    private JPanel insertBaseProductPricePanel;
    private JLabel baseProductPriceLabel;
    private JTextField baseProductPriceTextField;
    private JButton addBaseProductButton;
    private JButton cancelBaseProductButton;
    private JPanel insertMenuItemTitlePanel;
    private JLabel insertMenuItemLabel;
    private JPanel insertMenuItemPanel;
    private JLabel productNameLabel;
    private JTextField productNameTextField;
    private JButton addProductButton;
    private JButton cancelProductButton;
    private JButton finalizeMenuItemButton;
    private JTextArea menuItemtextArea;
    private JPanel menuItemTitlePanel;
    private JLabel menuItemLabel;

    private Serial s = new Serial();
    private Restaurant restaurant = s.deserialize();
    private Validator v = new Validator();
    private CompositeProduct compProduct = new CompositeProduct();


    private Set<MenuItem> parseCompositeProduct(String product) { //parse an input if type "a+b"
        Set<MenuItem> items = new HashSet<MenuItem>();
        String[] tokens = product.split("[+]"); //splits in bases
        String unavailableBaseProducts = new String("");
        for (String item: tokens) { //checks if each base exists in the menu item
            if (restaurant.findBaseProduct(v.getSimpleString(item)) == null) {
                if (unavailableBaseProducts.equals("")) { //create a string with the unavailable base products
                    unavailableBaseProducts += v.getSimpleString(item);
                }
                else {
                    unavailableBaseProducts += ", " + v.getSimpleString(item);
                }
            }
        }
        if (!unavailableBaseProducts.equals("")) {
            JOptionPane.showMessageDialog(rootPane, "The following base products do not exist: " + unavailableBaseProducts, "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        for (String item: tokens) { //if all the items exist, create a list with those bases
            items.add(restaurant.findBaseProduct(v.getSimpleString(item)));
        }
        return items;
    }

    public NewMenuItemGUI(String title) {
        super(title);
        this.setContentPane(mainPanel);
        this.pack();
        cancelBaseProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                baseProductNameTextField.setText("");
                baseProductPriceTextField.setText("");
            }
        });
        addBaseProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (baseProductNameTextField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(rootPane, "Base product name field is empty", "Message", JOptionPane.WARNING_MESSAGE);
                }
                else if (!v.isBaseProductNameValid(baseProductNameTextField.getText())) {
                    JOptionPane.showMessageDialog(rootPane, "Invalid base product name", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if (baseProductPriceTextField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(rootPane, "Base product price field is empty", "Message", JOptionPane.WARNING_MESSAGE);
                }
                else if (!v.isPriceValid(baseProductPriceTextField.getText())) {
                    JOptionPane.showMessageDialog(rootPane, "Invalid base product price", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if (restaurant.findBaseProduct(baseProductNameTextField.getText()) != null) {
                    JOptionPane.showMessageDialog(rootPane, "This base product already exists", "Message", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    BaseProduct p = new BaseProduct();
                    p.setName(v.getSimpleString(baseProductNameTextField.getText()));
                    p.setPrice(Float.parseFloat(v.getSimpleString(baseProductPriceTextField.getText())));
                    restaurant.createNewBaseProduct(p);
                    JOptionPane.showMessageDialog(rootPane, "Base product added successfully", "Message", JOptionPane.INFORMATION_MESSAGE);
                    baseProductNameTextField.setText("");
                    baseProductPriceTextField.setText("");
                }

            }
        });
        cancelProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productNameTextField.setText("");
                menuItemtextArea.setText("");
                compProduct = new CompositeProduct();
            }
        });
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (productNameTextField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(rootPane, "Product name field is empty", "Message", JOptionPane.WARNING_MESSAGE);
                }
                else if (!v.isCompositeProductNameValid(productNameTextField.getText())) {
                    JOptionPane.showMessageDialog(rootPane, "Invalid product name", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    if (productNameTextField.getText().contains("+")) { //if it is a composite
                        CompositeProduct product = new CompositeProduct();
                        product.setName(v.getSimpleString(productNameTextField.getText()));
                        product.setItems(parseCompositeProduct(v.getSimpleString(productNameTextField.getText())));
                        if (product.getItems() != null) {
                            if (restaurant.findCompositeProductItems(product.getItems()) == null) {
                                if (product.getItems().size() > 1) {
                                    CompositeProduct newProduct = product;
                                    newProduct.setPrice(newProduct.computePrice());
                                    restaurant.createNewCompositeProduct(newProduct); //create the composite if it does not already exist
                                    JOptionPane.showMessageDialog(rootPane, "New composite product created", "Message", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                            else { //adds the composite to the list of the final composite
                                product = (CompositeProduct) restaurant.findCompositeProductItems(product.getItems());
                            }
                            product.setPrice(product.computePrice());
                            compProduct.addMenuItem(product);
                            menuItemtextArea.append(product.getName() + "\n");
                            productNameTextField.setText(null);
                        }
                    }
                    else { //if it is a base
                        BaseProduct product = new BaseProduct();
                        product.setName(v.getSimpleString(productNameTextField.getText()));
                        BaseProduct existingProduct = (BaseProduct) restaurant.findBaseProduct(product.getName());
                        if (existingProduct == null) {
                            JOptionPane.showMessageDialog(rootPane, "The base product " + product.getName() + " does not exist", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        else {
                            compProduct.addMenuItem(existingProduct);
                            menuItemtextArea.append(product.getName() + "\n");
                            productNameTextField.setText(null);
                        }
                    }
                }
            }
        });
        finalizeMenuItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (menuItemtextArea.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(rootPane, "Non existent menu item", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    compProduct.setPrice(compProduct.computePrice());
                    String productName = new String(); //create the name of the product
                    boolean onlyBaseProducts = true;
                    for (MenuItem i: compProduct.getItems()) {
                        if (i instanceof CompositeProduct) {
                            onlyBaseProducts = false;
                            break;
                        }
                    }
                    if (onlyBaseProducts) {
                        for (MenuItem i: compProduct.getItems()) {
                            productName = productName + ((BaseProduct) i).getName() + "+";
                        }
                    }
                    else {
                        for (MenuItem i: compProduct.getItems()) {
                            if (i instanceof BaseProduct) {
                                productName = productName + ((BaseProduct) i).getName() + ",";
                            }
                            else {
                                productName = productName + ((CompositeProduct) i).getName() + ",";
                            }
                        }
                    }

                    productName = productName.substring(0, productName.length() - 1);
                    compProduct.setName(productName);
                    if (menuItemtextArea.getText().split("[\n]").length == 1) {
                        JOptionPane.showMessageDialog(rootPane, "Composite menu item needs more than one item", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else if (restaurant.findCompositeProductItems(compProduct.getItems()) != null) {
                        JOptionPane.showMessageDialog(rootPane, "Already existing menu item", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        restaurant.createNewCompositeProduct(compProduct);
                        JOptionPane.showMessageDialog(rootPane, "Menu item added successfully", "Message", JOptionPane.INFORMATION_MESSAGE);
                    }
                    menuItemtextArea.setText(null);
                    compProduct = new CompositeProduct();
                }
            }
        });
    }
}

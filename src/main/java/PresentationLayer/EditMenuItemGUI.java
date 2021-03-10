package PresentationLayer;

import BusinessLayer.BaseProduct;
import BusinessLayer.MenuItem;
import BusinessLayer.Restaurant;
import BusinessLayer.Validator;
import DataLayer.Serial;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

public class EditMenuItemGUI extends JFrame {
    private JPanel mainPanel;
    private JLabel editMenuItemLabel;
    private JLabel baseProductLabel;
    private JTextField baseProductTextField;
    private JLabel newPriceLabel;
    private JTextField newPriceTextField;
    private JButton editButton;
    private JButton cancelButton;

    private Validator v = new Validator();
    private Serial s = new Serial();
    private Restaurant restaurant = s.deserialize();

    public EditMenuItemGUI(String title) {
        super(title);
        this.setContentPane(mainPanel);
        this.pack();
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                baseProductTextField.setText(null);
                newPriceTextField.setText(null);
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (baseProductTextField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(rootPane, "Base product field is empty", "Message", JOptionPane.WARNING_MESSAGE);
                }
                else if (!v.isBaseProductNameValid(v.getSimpleString(baseProductTextField.getText()))) {
                    JOptionPane.showMessageDialog(rootPane, "Invalid base product name", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if (newPriceTextField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(rootPane, "New price field is empty", "Message", JOptionPane.WARNING_MESSAGE);
                }
                else if (!v.isPriceValid(v.getSimpleString(newPriceTextField.getText()))) {
                    JOptionPane.showMessageDialog(rootPane, "Invalid price", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else { //if everything is ok
                    for (MenuItem i: restaurant.getMenuItems()) { //search for the base with the entered name
                        if (i instanceof BaseProduct) {
                            if (restaurant.findBaseProduct(v.getSimpleString(baseProductTextField.getText())) == i) {
                                restaurant.editMenuItem((BaseProduct) i, Float.parseFloat(newPriceTextField.getText())); //edit the found item
                                JOptionPane.showMessageDialog(rootPane, "Item edited successfully", "Message", JOptionPane.INFORMATION_MESSAGE);
                                baseProductTextField.setText(null);
                                newPriceTextField.setText(null);
                                return;
                            }
                        }
                    }
                    JOptionPane.showMessageDialog(rootPane, "Inexistent menu item", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}

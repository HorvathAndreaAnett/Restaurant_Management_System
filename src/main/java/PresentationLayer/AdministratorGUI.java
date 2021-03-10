package PresentationLayer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdministratorGUI extends JFrame {
    private JPanel mainPanel;
    private JLabel administratorLabel;
    private JButton createButton;
    private JButton deleteButton;
    private JButton editButton;
    private JButton viewButton;

    public AdministratorGUI(String title) {
        super(title);
        this.setContentPane(mainPanel);
        this.pack();
        //one button for each operation which opens a different interface
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new ViewMenuItemsGUI("View Menu Items");
                frame.setVisible(true);
            }
        });
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new NewMenuItemGUI("New Menu Item");
                frame.setVisible(true);
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new DeleteMenuItemGUI("Delete Menu Item");
                frame.setVisible(true);
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new EditMenuItemGUI("Edit Menu Item");
                frame.setVisible(true);
            }
        });
    }

}

package PresentationLayer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RestaurantGUI extends JFrame {
    private JPanel mainPanel;
    private JLabel userLabel;
    private JButton adminButton;
    private JButton waiterButton;
    private JButton chefButton;

    public RestaurantGUI(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        //one button for each user which opens a different interface
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new AdministratorGUI("Administrator");
                frame.setVisible(true);
            }
        });
        waiterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new WaiterGUI("View Menu Items");
                frame.setVisible(true);
            }
        });
        chefButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new ChefGUI("View Menu Items");
                frame.setVisible(true);
            }
        });
    }
}

package PresentationLayer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WaiterGUI extends JFrame {
    private JPanel mainPanel;
    private JLabel waiterLabel;
    private JButton addOrderButton;
    private JButton viewOrdersButton;

    public WaiterGUI(String title) {
        super(title);
        this.setContentPane(mainPanel);
        this.pack();
        //one button for each operation which opens a different interface
        addOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new AddOrderGUI("Add order");
                frame.setVisible(true);
            }
        });
        viewOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new ViewOrdersGUI("View Orders");
                frame.setVisible(true);
            }
        });
    }
}

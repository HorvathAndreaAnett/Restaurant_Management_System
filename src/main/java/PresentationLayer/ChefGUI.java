package PresentationLayer;

import BusinessLayer.*;

import javax.swing.*;

public class ChefGUI extends JFrame {

    private JPanel mainPanel;
    private JLabel chefLabel;
    private JTextArea orderTextArea;

    //display a message with the last order
    public ChefGUI(String title) {
        super(title);
        this.setContentPane(mainPanel);
        this.pack();

        Chef chef = new Chef();
        orderTextArea.setText(chef.getProcessedOrder());
    }
}

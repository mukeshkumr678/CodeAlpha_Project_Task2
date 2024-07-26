import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.sun.imageio.plugins.jpeg.JPEG.vendor;


public class Main {

    Main() {
        JFrame frame = new JFrame();
        JLabel label = new JLabel("Stock Management Main ");
        JPanel panel = new JPanel();
        panel.setLayout(null);

        ImageIcon image = new ImageIcon("D:\\TaskTwo\\pexels-babydov-7788009.jpg");
        Image backgroundImage = image.getImage().getScaledInstance(1000, 600, Image.SCALE_SMOOTH);
        ImageIcon resizedBackgroundImageIcon = new ImageIcon(backgroundImage);
        JLabel backgroundLabel = new JLabel(resizedBackgroundImageIcon);
        backgroundLabel.setBounds(0, 0, 1000, 600);


        // Set bounds for label2 to ensure the image fits within the frame
        //label2.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());

//

        JButton vendor = new JButton("VENDOR");
        JButton product = new JButton("PRODUCT");
        JButton purchase = new JButton("PURCHASE");
        JButton sell = new JButton("SALE");
        JButton logout = new JButton("LOGOUT");
        Font font = new Font("Arial", Font.BOLD, 16);

        label.setBounds(300, 100, 400, 50);
        label.setFont(new Font("Algerian", Font.PLAIN, 30));
        label.setForeground(Color.WHITE);

        vendor.setBounds(150, 200, 180, 50);
        vendor.setFocusable(false);
        vendor.setFont(font);
        vendor.setBackground(Color.LIGHT_GRAY);

        product.setBounds(150, 270, 180, 50);
        product.setFocusable(false);
        product.setFont(font);
        product.setBackground(Color.LIGHT_GRAY);

        purchase.setBounds(150, 340, 180, 50);
        purchase.setFocusable(false);
        purchase.setFont(font);
        purchase.setBackground(Color.LIGHT_GRAY);

        sell.setBounds(150, 400, 180, 50);
        sell.setFocusable(false);
        sell.setFont(font);
        sell.setBackground(Color.LIGHT_GRAY);

        logout.setBounds(150, 470, 180, 50);
        logout.setFocusable(false);
        logout.setFont(font);
        logout.setBackground(Color.LIGHT_GRAY);

        MyEventHandler mh = new MyEventHandler(frame);
        vendor.addActionListener(mh);
        product.addActionListener(mh);
        purchase.addActionListener(mh);
        sell.addActionListener(mh);
        logout.addActionListener(mh);


        panel.add(vendor);
        panel.add(product);
        panel.add(purchase);
        panel.add(sell);
        panel.add(logout);
        backgroundLabel.add(label);


        frame.setSize(image.getIconWidth(), image.getIconHeight()); // Set frame size to image size
        frame.add(backgroundLabel);
        //frame.add(label);// Add label2 before panel to ensure it's behind the buttons
        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(1000, 600);
        frame.setVisible(true);
    }
}


class MyEventHandler implements ActionListener {
    private JFrame frame;

    public MyEventHandler(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dispatch(e);
    }

    private void dispatch(ActionEvent e) {
        JButton sourceButton = (JButton) e.getSource();
        String command = sourceButton.getActionCommand();

        frame.dispose(); // Close the current frame

        switch (command) {
            case "VENDOR":
                new Vendor();
                break;
            case "PRODUCT":
                new Product();
                break;
            case "PURCHASE":
                new Purchase();
                break;
            case "SALE":
                new Sale();
                break;
            case "LOGOUT":
                // Handle logout action
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + command);
        }
    }
}


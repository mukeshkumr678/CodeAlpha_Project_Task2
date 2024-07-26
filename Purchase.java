import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Purchase extends Component implements ActionListener {
    JFrame frame;
    JLabel productbarcode = new JLabel("Product Barcode");
    JLabel productname = new JLabel("Product Name");
    JLabel price = new JLabel("Price");
    JLabel quantity = new JLabel("Quantity");
    JLabel tcost = new JLabel("Total Cost");
    JLabel pay = new JLabel("Payment");
    JLabel balance = new JLabel("Balance");
    JLabel vendor = new JLabel("Vendor");
    JButton add2 = new JButton("ADD");
    JComboBox<String> vendorcombo = new JComboBox<>();

    JTextField tfproductbarcode = new JTextField(20);
    JTextField tfproductname = new JTextField(20);
    JTextField tfprice = new JTextField(20);
    JTextField tfqty = new JTextField(20);
    JTextField tftcost = new JTextField(20);
    JTextField tfbalance = new JTextField(20);
    JTextField tfpay = new JTextField(20);

    JLabel msg = new JLabel();

    JTable table;
    JScrollPane scrollPane;
    DefaultTableModel model;

    public Purchase() {
        frame = new JFrame("Purchase Management");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);

        JLabel vendorLabel = new JLabel("Purchase Management Section");
        vendorLabel.setFont(new Font("Algerian", Font.PLAIN, 30));
        vendorLabel.setForeground(Color.BLACK);
        vendorLabel.setBounds(300, 0, 550, 30);

        JButton back = new JButton("Back");
        back.setBounds(0, 0, 100, 30);
        back.setBackground(Color.RED);
        back.setFocusable(false);
        back.addActionListener(this);

        JButton add = new JButton("Add");
        add.setBounds(200, 300, 80, 30);
        add.setFocusable(false);

        vendor.setBounds(500, 30, 100, 30);
        vendorcombo.setBounds(550, 30, 100, 30);

        productbarcode.setBounds(70, 100, 200, 30);
        productname.setBounds(70, 150, 200, 30);
        price.setBounds(70, 200, 200, 30);
        quantity.setBounds(70, 250, 200, 30);
        tcost.setBounds(70, 350, 200, 30);
        balance.setBounds(70, 400, 200, 30);
        pay.setBounds(70, 450, 200, 30);
        add2.setBounds(200, 500, 80, 30);
        add2.setFocusable(false);

        tfproductbarcode.setBounds(140, 100, 200, 30);
        tfproductname.setBounds(140, 150, 200, 30);
        tfprice.setBounds(140, 200, 200, 30);
        tfqty.setBounds(140, 250, 200, 30);
        tftcost.setBounds(140, 350, 200, 30);
        tfbalance.setBounds(140, 400, 200, 30);
        tfpay.setBounds(140, 450, 200, 30);

        add.addActionListener(this);
        add2.addActionListener(this);
        msg.setBounds(0, 500, 400, 30);

        frame.add(productbarcode);
        frame.add(productname);
        frame.add(price);
        frame.add(quantity);
        frame.add(tcost);
        frame.add(balance);
        frame.add(pay);
        frame.add(vendor);
        frame.add(vendorcombo);

        frame.add(tfproductname);
        frame.add(tfproductbarcode);
        frame.add(tfprice);
        frame.add(tfqty);
        frame.add(tftcost);
        frame.add(tfbalance);
        frame.add(tfpay);

        frame.add(add);
        frame.add(vendorLabel);
        frame.add(back);
        frame.add(add2);

        // Create and add JTable to display data
        String[] columnNames = {"Product Barcode", "Product Name", "Price", "Quantity", "Total"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(400, 100, 550, 300);
        frame.add(scrollPane);

        frame.setVisible(true);

        // Load data into the table when the frame is initialized
        loadTableData();
        connect();
        vendor();

        tfproductbarcode.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                productKeyPressed(evt);
            }
        });
    }

    private void loadTableData() {
        // Initialization moved to constructor and table model is created there
        // No database operations here, just showing existing structure
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Back")) {
            frame.dispose();
            new Main();
        } else if (e.getActionCommand().equals("Add")) {
            String productname = tfproductname.getText();
            String productcode = tfproductbarcode.getText();
            String price = tfprice.getText();
            String quantity = tfqty.getText();

            try {
                if (price.isEmpty() || quantity.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Price or Quantity cannot be empty.");
                    return;
                }

                int priceInt = Integer.parseInt(price);
                int qtyInt = Integer.parseInt(quantity);
                int total = priceInt * qtyInt;

                model.addRow(new Object[]{
                        productcode,
                        productname,
                        price,
                        quantity,
                        total
                });

                tfproductbarcode.requestFocus(); // Cursor will blink on name text field after clicking on add button


                updateTotalCost();

                tfproductname.setText("");
                tfprice.setText("");
                tfproductbarcode.setText("");
                tfqty.setText("");
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                msg.setForeground(Color.RED);
                msg.setText("Error: " + ex.getMessage());
            }
        } else if (e.getActionCommand().equals("ADD")) {
            JButtonActionPerformed(e);
        }
    }

    private void updateTotalCost() {
        int sum = 0;
        for (int i = 0; i < table.getRowCount(); i++) {
            sum += Integer.parseInt(table.getValueAt(i, 4).toString());
        }
        tftcost.setText(String.valueOf(sum));
    }

    private void JButtonActionPerformed(ActionEvent e) {
        try {
            int pay = Integer.parseInt(tfpay.getText());
            int subtotal = Integer.parseInt(tftcost.getText());
            int bal = subtotal - pay;
            tfbalance.setText(String.valueOf(bal));
            add2(); // Process adding purchase after payment is calculated
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            msg.setForeground(Color.RED);
            msg.setText("Error: " + ex.getMessage());
        }
    }

    public void add2() {
        DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        String date = dt.format(now);

        String subtotalStr = tftcost.getText();
        String payStr = tfpay.getText();
        String balStr = tfbalance.getText();
        String vendor = vendorcombo.getSelectedItem().toString();
        int lastid = 0;

        try {
            String url = "jdbc:mysql://localhost:3306/stock";
            Connection connection = DriverManager.getConnection(url, "root", "Mukesh*12");

            // Insert into Purchase2
            String query1 = "INSERT INTO Purchase2 (Date, Vendor, Subtotal, Pay, Balance) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, date);
            pst.setString(2, vendor);
            pst.setString(3, subtotalStr);
            pst.setString(4, payStr);
            pst.setString(5, balStr);
            pst.executeUpdate();

            ResultSet resultSet = pst.getGeneratedKeys();
            if (resultSet.next()) {
                lastid = resultSet.getInt(1);
            }

            // Insert into PurchaseItem
            String query2 = "INSERT INTO PurchaseItem (PurId, ProId, Retprice, Qty, Total) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst2 = connection.prepareStatement(query2);
            for (int i = 0; i < table.getRowCount(); i++) {
                String proid = (String) table.getValueAt(i, 0);
                int price = Integer.parseInt(table.getValueAt(i, 2).toString());
                int qty = Integer.parseInt(table.getValueAt(i, 3).toString());
                int total = Integer.parseInt(table.getValueAt(i, 4).toString());

                pst2.setInt(1, lastid);
                pst2.setString(2, proid);
                pst2.setInt(3, price);
                pst2.setInt(4, qty);
                pst2.setInt(5, total);
                pst2.addBatch();
            }
            pst2.executeBatch();

            // Update Product quantities
            String query3 = "UPDATE Product SET Qty = Qty + ? WHERE Barcode = ?";
            PreparedStatement pst3 = connection.prepareStatement(query3);
            for (int i = 0; i < table.getRowCount(); i++) {
                String proid = (String) table.getValueAt(i, 0);
                int qty = Integer.parseInt(table.getValueAt(i, 3).toString());
                pst3.setInt(1, qty);
                pst3.setString(2, proid);
                pst3.addBatch();
            }
            pst3.executeBatch();

            JOptionPane.showMessageDialog(frame, "Purchase is completed......");

            // Clear table and fields after completion
            model.setRowCount(0);
            tftcost.setText("");
            tfbalance.setText("");
            tfpay.setText("");

            pst.close();
            pst2.close();
            pst3.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            msg.setForeground(Color.RED);
            msg.setText("Error: " + ex.getMessage());
        }
    }

    public void connect() {
        try {
            String url = "jdbc:mysql://localhost:3306/stock";
            Connection connection = DriverManager.getConnection(url, "root", "Mukesh*12");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void vendor() {
        try {
            String url = "jdbc:mysql://localhost:3306/stock";
            Connection connection = DriverManager.getConnection(url, "root", "Mukesh*12");
            String query = "SELECT DISTINCT name FROM VendorInfo";
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet resultSet = pst.executeQuery();
            vendorcombo.removeAllItems();
            while (resultSet.next()) {
                vendorcombo.addItem(resultSet.getString("name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void purchase() {
        String pcode = tfproductbarcode.getText();
        try {
            String url = "jdbc:mysql://localhost:3306/stock";
            Connection connection = DriverManager.getConnection(url, "root", "Mukesh*12");
            String query = "SELECT * FROM Product WHERE barcode = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, pcode);
            ResultSet resultSet = pst.executeQuery();

            if (!resultSet.next()) {
                JOptionPane.showMessageDialog(frame, "Barcode not found");
            } else {
                String pname = resultSet.getString("ProductName");
                String price = resultSet.getString("RetailPrice");
                tfproductname.setText(pname.trim());
                tfprice.setText(price.trim());
                tfqty.requestFocus();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void productKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            purchase();
        }
    }
}

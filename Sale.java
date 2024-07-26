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

class Sale extends Component implements ActionListener {
    JFrame frame;
    JLabel productbarcode = new JLabel("Product Barcode");
    JLabel productname = new JLabel("Product Name");
    JLabel price = new JLabel("Price");
    JLabel quantity = new JLabel("Quantity");
    JLabel tcost = new JLabel("Total Cost");
    JLabel pay = new JLabel("Payment");
    JLabel balance = new JLabel("Balance");
    JButton add = new JButton("Add");
    JButton finalizeSale = new JButton("Finalize Sale");

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

    public Sale() {
        frame = new JFrame("Sales Management");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);

        JLabel titleLabel = new JLabel("Sales Management Section");
        titleLabel.setFont(new Font("Algerian", Font.PLAIN, 30));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBounds(300, 0, 550, 30);

        JButton back = new JButton("Back");
        back.setBounds(0, 0, 100, 30);
        back.setBackground(Color.RED);
        back.setFocusable(false);
        back.addActionListener(this);

        add.setBounds(200, 300, 80, 30);
        add.setFocusable(false);
        finalizeSale.setBounds(200, 500, 150, 30);
        finalizeSale.setFocusable(false);

        productbarcode.setBounds(70, 100, 200, 30);
        productname.setBounds(70, 150, 200, 30);
        price.setBounds(70, 200, 200, 30);
        quantity.setBounds(70, 250, 200, 30);
        tcost.setBounds(70, 350, 200, 30);
        balance.setBounds(70, 400, 200, 30);
        pay.setBounds(70, 450, 200, 30);

        tfproductbarcode.setBounds(140, 100, 200, 30);
        tfproductname.setBounds(140, 150, 200, 30);
        tfprice.setBounds(140, 200, 200, 30);
        tfqty.setBounds(140, 250, 200, 30);
        tftcost.setBounds(140, 350, 200, 30);
        tfbalance.setBounds(140, 400, 200, 30);
        tfpay.setBounds(140, 450, 200, 30);

        add.addActionListener(this);
        finalizeSale.addActionListener(this);
        msg.setBounds(0, 500, 400, 30);

        frame.add(productbarcode);
        frame.add(productname);
        frame.add(price);
        frame.add(quantity);
        frame.add(tcost);
        frame.add(balance);
        frame.add(pay);
        frame.add(add);
        frame.add(finalizeSale);

        frame.add(tfproductname);
        frame.add(tfproductbarcode);
        frame.add(tfprice);
        frame.add(tfqty);
        frame.add(tftcost);
        frame.add(tfbalance);
        frame.add(tfpay);

        frame.add(titleLabel);
        frame.add(back);

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
            addProductToTable();
        } else if (e.getActionCommand().equals("Finalize Sale")) {
            finalizeSale();
        }
    }

    private void addProductToTable() {
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
    }

    private void updateTotalCost() {
        int sum = 0;
        for (int i = 0; i < table.getRowCount(); i++) {
            sum += Integer.parseInt(table.getValueAt(i, 4).toString());
        }
        tftcost.setText(String.valueOf(sum));
    }

    private void finalizeSale() {
        try {
            int payAmount = Integer.parseInt(tfpay.getText());
            int subtotal = Integer.parseInt(tftcost.getText());
            int balance = subtotal - payAmount;
            tfbalance.setText(String.valueOf(balance));
            processSale(); // Process sale after payment is calculated
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            msg.setForeground(Color.RED);
            msg.setText("Error: " + ex.getMessage());
        }
    }

    private void processSale() {
        DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        String date = dt.format(now);

        String subtotalStr = tftcost.getText();
        String payStr = tfpay.getText();
        String balStr = tfbalance.getText();
        int lastid = 0;
        boolean sufficientStock = true;

        try {
            String url = "jdbc:mysql://localhost:3306/stock";
            Connection connection = DriverManager.getConnection(url, "root", "Mukesh*12");

            // Insert into Sale
            String query1 = "INSERT INTO Sale (Date, Subtotal, Pay, Balance) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, date);
            pst.setString(2, subtotalStr);
            pst.setString(3, payStr);
            pst.setString(4, balStr);
            pst.executeUpdate();

            ResultSet resultSet = pst.getGeneratedKeys();
            if (resultSet.next()) {
                lastid = resultSet.getInt(1);
            }

            // Prepare to insert into SaleItem
            String query2 = "INSERT INTO SaleItem (SaleId, ProductCode, Price, Quantity, Total) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst2 = connection.prepareStatement(query2);

            // Check stock availability
            for (int i = 0; i < table.getRowCount(); i++) {
                String productCode = (String) table.getValueAt(i, 0);
                int price = Integer.parseInt(table.getValueAt(i, 2).toString());
                int qty = Integer.parseInt(table.getValueAt(i, 3).toString());
                int total = Integer.parseInt(table.getValueAt(i, 4).toString());

                // Check stock availability
                String stockQuery = "SELECT Qty FROM Product WHERE Barcode = ?";
                PreparedStatement stockStmt = connection.prepareStatement(stockQuery);
                stockStmt.setString(1, productCode);
                ResultSet stockResult = stockStmt.executeQuery();

                if (stockResult.next()) {
                    int availableQty = stockResult.getInt("Qty");

                    if (qty > availableQty) {
                        JOptionPane.showMessageDialog(frame,
                                "Not enough stock for product: " + productCode,
                                "Stock Error",
                                JOptionPane.ERROR_MESSAGE);
                        sufficientStock = false;
                        stockStmt.close();
                        break;
                    }

                    // Prepare to update stock
                    String updateStockQuery = "UPDATE Product SET Qty = Qty - ? WHERE Barcode = ?";
                    PreparedStatement updateStockStmt = connection.prepareStatement(updateStockQuery);
                    updateStockStmt.setInt(1, qty);
                    updateStockStmt.setString(2, productCode);
                    updateStockStmt.executeUpdate();
                    updateStockStmt.close();
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Product not found: " + productCode,
                            "Product Error",
                            JOptionPane.ERROR_MESSAGE);
                    sufficientStock = false;
                    stockStmt.close();
                    break;
                }
                stockStmt.close();

                if (sufficientStock) {
                    pst2.setInt(1, lastid);
                    pst2.setString(2, productCode);
                    pst2.setInt(3, price);
                    pst2.setInt(4, qty);
                    pst2.setInt(5, total);
                    pst2.addBatch();
                }
            }

            if (sufficientStock) {
                pst2.executeBatch();
                JOptionPane.showMessageDialog(frame, "Sale completed successfully!");

                // Clear table and fields after completion
                model.setRowCount(0);
                tftcost.setText("");
                tfbalance.setText("");
                tfpay.setText("");
            }

            pst.close();
            pst2.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            msg.setForeground(Color.RED);
            msg.setText("Error: " + ex.getMessage());
        }
    }


    private void productKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            fetchProductDetails();
        }
    }

    private void fetchProductDetails() {
        String barcode = tfproductbarcode.getText();
        try {
            String url = "jdbc:mysql://localhost:3306/stock";
            Connection connection = DriverManager.getConnection(url, "root", "Mukesh*12");
            String query = "SELECT * FROM Product WHERE Barcode = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, barcode);
            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                String pname = resultSet.getString("ProductName");
                String price = resultSet.getString("RetailPrice");
                tfproductname.setText(pname.trim());
                tfprice.setText(price.trim());
                tfqty.requestFocus();
            } else {
                JOptionPane.showMessageDialog(frame, "Barcode not found");
            }
            pst.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



}

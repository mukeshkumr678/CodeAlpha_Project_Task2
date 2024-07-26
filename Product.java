import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

class Product implements ActionListener {
    JFrame frame;
    JLabel productname = new JLabel("Product Name");
    JLabel description = new JLabel("Description");
    JLabel barcode = new JLabel("Barcode");
    JLabel costprice = new JLabel("Cost Price");
    JLabel retailprice = new JLabel("Retail Price");
    JLabel quantity = new JLabel("Quantity");
    JLabel reordered = new JLabel("Reordered");


    JTextField tfproductname = new JTextField(20);
    JTextField tfdescription = new JTextField(20);
    JTextField tfbarcode = new JTextField(20);
    JTextField tfcostprice = new JTextField(20);
    JTextField tfretailprice = new JTextField(20);
    JTextField tfquantity = new JTextField(20);
    JTextField tfreordered = new JTextField(20);

    JLabel msg = new JLabel();

    JTable table;
    JScrollPane scrollPane;

    public Product() {
        frame = new JFrame("Product Management");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);

        JLabel vendorLabel = new JLabel("Product Management Section");
        vendorLabel.setFont(new Font("Algerian", Font.PLAIN, 30));
        vendorLabel.setForeground(Color.BLACK);
        vendorLabel.setBounds(300, 30, 550, 30);

        JButton back = new JButton("Back");
        back.setBounds(0, 0, 100, 30);
        back.setBackground(Color.RED);
        back.setFocusable(false);
        back.addActionListener(this);

        productname.setBounds(70, 100, 200, 30);
        description.setBounds(70, 150, 200, 30);
        barcode.setBounds(70, 200, 200, 30);
        costprice.setBounds(70, 250, 200, 30);
        retailprice.setBounds(70,300,200,30);
        quantity.setBounds(70, 350, 200, 30);
        reordered.setBounds(70,400,200,30);

        tfproductname.setBounds(140, 100, 200, 30);
        tfdescription.setBounds(140, 150, 200, 30);
        tfbarcode.setBounds(140, 200, 200, 30);
        tfcostprice.setBounds(140, 250, 200, 30);
        tfretailprice.setBounds(140, 300, 200, 30);
        tfquantity.setBounds(140, 350, 200, 30);
        tfreordered.setBounds(140, 400, 200, 30);

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");
        JButton cancel = new JButton("Cancel");

        add.setBounds(0, 450, 80, 30);
        add.addActionListener(this);
        edit.setBounds(100, 450, 80, 30);
        edit.addActionListener(this);
        delete.setBounds(200, 450, 80, 30);
        delete.addActionListener(this);
        cancel.setBounds(300, 450, 80, 30);
        cancel.addActionListener(this);

        msg.setBounds(0, 500, 400, 30);

        frame.add(productname);
        frame.add(description);
        frame.add(barcode);
        frame.add(costprice);
        frame.add(retailprice);
        frame.add(quantity);
        frame.add(reordered);

        frame.add(tfproductname);
        frame.add(tfdescription);
        frame.add(tfbarcode);
        frame.add(tfcostprice);
        frame.add(tfretailprice);
        frame.add(tfquantity);
        frame.add(tfreordered);


        frame.add(add);
        frame.add(edit);
        frame.add(delete);
        frame.add(cancel);

        frame.add(vendorLabel);
        frame.add(back);
        frame.add(msg);

        // Add JTable to display database contents
        table = new JTable();
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(400, 100, 550, 300);
        frame.add(scrollPane);

        frame.setVisible(true);

        // Load data into the table when the frame is initialized
        loadTableData();
    }

    private void loadTableData() {
        try {
            String url = "jdbc:mysql://localhost:3306/stock";
            Connection connection = DriverManager.getConnection(url, "root", "Mukesh*12");
            String query = "SELECT * FROM Product";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Get metadata to dynamically create column names
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            // Fetch data and populate table model
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = resultSet.getObject(i);
                }
                model.addRow(row);
            }
            table.setModel(model);

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            msg.setForeground(Color.RED);
            msg.setText("Error: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Back")) {
            frame.dispose();
            new Main();
        } else if (e.getActionCommand().equals("Add")) {
            String productname = tfproductname.getText();
            String description = tfdescription.getText();
            String barcode = tfbarcode.getText();
            String costprice = tfcostprice.getText();
            String retialprice = tfretailprice.getText();
            String quantity= tfquantity.getText();
            String reordered = tfreordered.getText();

            try {
                String url = "jdbc:mysql://localhost:3306/stock";
                Connection connection = DriverManager.getConnection(url, "root", "Mukesh*12");

                String insertQuery = "INSERT INTO Product (ProductName, Description, Barcode, CostPrice, RetailPrice, Qty,Reordered) VALUES (?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

                preparedStatement.setString(1, productname);
                preparedStatement.setString(2, description);
                preparedStatement.setString(3, barcode);
                preparedStatement.setString(4, costprice);
                preparedStatement.setString(5, retialprice);
                preparedStatement.setString(6, quantity);
                preparedStatement.setString(7, reordered);

                int rowsAffected = preparedStatement.executeUpdate();

                tfproductname.setText("");
                tfdescription.setText("");
                tfbarcode.setText("");
                tfcostprice.setText("");
                tfretailprice.setText("");
                tfquantity.setText("");
                tfreordered.setText("");
                tfretailprice.requestFocus();// cursor will be blinked on name text field after clicking on add button

                if (rowsAffected > 0) {
                    msg.setForeground(Color.GREEN);
                    msg.setText("product added successfully!");
                } else {
                    msg.setForeground(Color.RED);
                    msg.setText("Failed to add product!");
                }

                preparedStatement.close();
                connection.close();

                // Reload table data after insertion
                loadTableData();
            } catch (SQLException ex) {
                ex.printStackTrace();
                msg.setForeground(Color.RED);
                msg.setText("Error: " + ex.getMessage());
            }
        } else if (e.getActionCommand().equals("Edit")) {
            String productname = tfproductname.getText();
            String description = tfdescription.getText();
            String barcode = tfbarcode.getText();
            String costprice = tfcostprice.getText();
            String retialprice = tfretailprice.getText();
            String quantity= tfquantity.getText();
            String reordered = tfreordered.getText();

            // Check if ID field is empty
            if (barcode.isEmpty()) {
                msg.setForeground(Color.RED);
                msg.setText("Please enter a valid ID to edit!");
                return;
            }

            try {
                String url = "jdbc:mysql://localhost:3306/stock";
                Connection connection = DriverManager.getConnection(url, "root", "Mukesh*12");

                String updateQuery ="UPDATE Product SET ProductName =?, Description=?,Barcode=?, CostPrice=?, RetailPrice=?, Qty=?,Reordered=?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

                preparedStatement.setString(1, productname);
                preparedStatement.setString(2, description);
                preparedStatement.setString(3, barcode);
                preparedStatement.setString(4, costprice);
                preparedStatement.setString(5, retialprice);
                preparedStatement.setString(6, quantity);
                preparedStatement.setString(7, reordered);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    msg.setForeground(Color.GREEN);
                    msg.setText("Product updated successfully!");
                } else {
                    msg.setForeground(Color.RED);
                    msg.setText("Failed to update product! ID may not exist.");
                }

                preparedStatement.close();
                connection.close();

                // Reload table data after update
                loadTableData();
            } catch (SQLException ex) {
                ex.printStackTrace();
                msg.setForeground(Color.RED);
                msg.setText("Error: " + ex.getMessage());
            }
        } else if (e.getActionCommand().equals("Delete")) {
            String barcode = tfbarcode.getText();

            // Check if ID field is empty
            if (barcode.isEmpty()) {
                msg.setForeground(Color.RED);
                msg.setText("Please enter a valid barcode to delete!");
                return;
            }

            try {
                String url = "jdbc:mysql://localhost:3306/stock";
                Connection connection = DriverManager.getConnection(url, "root", "Mukesh*12");

                String deleteQuery = "DELETE FROM Product WHERE Barcode = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                preparedStatement.setString(1, barcode);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    msg.setForeground(Color.GREEN);
                    msg.setText("Product deleted successfully!");
                } else {
                    msg.setForeground(Color.RED);
                    msg.setText("Failed to delete product! Barcode may not exist.");
                }

                preparedStatement.close();
                connection.close();

                // Reload table data after deletion
                loadTableData();
            } catch (SQLException ex) {
                ex.printStackTrace();
                msg.setForeground(Color.RED);
                msg.setText("Error: " + ex.getMessage());
            }
        } else if (e.getActionCommand().equals("Cancel")) {
            String productname = tfproductname.getText();
            String description = tfdescription.getText();
            String barcode = tfbarcode.getText();
            String costprice = tfcostprice.getText();
            String retialprice = tfretailprice.getText();
            String quantity= tfquantity.getText();
            String reordered = tfreordered.getText();
            msg.setText("");
        }
    }


}

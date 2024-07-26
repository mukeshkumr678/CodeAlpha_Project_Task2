import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

class Vendor implements ActionListener {
    JFrame frame;
    JLabel name = new JLabel("Name");
    JLabel id = new JLabel("ID");
    JLabel phone = new JLabel("PhoneNo");
    JLabel email = new JLabel("Email");

    JTextField tfname = new JTextField(20);
    JTextField tfid = new JTextField(20);
    JTextField tfphone = new JTextField(20);
    JTextField tfemail = new JTextField(20);
    JLabel msg = new JLabel();

    JTable table;
    JScrollPane scrollPane;

    public Vendor() {
        frame = new JFrame("Vendor Management");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);

        JLabel vendorLabel = new JLabel("Vendor Management Section");
        vendorLabel.setFont(new Font("Algerian", Font.PLAIN, 30));
        vendorLabel.setForeground(Color.BLACK);
        vendorLabel.setBounds(300, 30, 550, 30);

        JButton back = new JButton("Back");
        back.setBounds(0, 0, 100, 30);
        back.setBackground(Color.RED);
        back.setFocusable(false);
        back.addActionListener(this);

        name.setBounds(70, 100, 200, 30);
        id.setBounds(70, 150, 200, 30);
        phone.setBounds(70, 200, 200, 30);
        email.setBounds(70, 250, 200, 30);

        tfname.setBounds(140, 100, 200, 30);
        tfid.setBounds(140, 150, 200, 30);
        tfphone.setBounds(140, 200, 200, 30);
        tfemail.setBounds(140, 250, 200, 30);

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");
        JButton cancel = new JButton("Cancel");

        add.setBounds(0, 400, 80, 30);
        add.addActionListener(this);
        edit.setBounds(100, 400, 80, 30);
        edit.addActionListener(this);
        delete.setBounds(200, 400, 80, 30);
        delete.addActionListener(this);
        cancel.setBounds(300, 400, 80, 30);
        cancel.addActionListener(this);

        msg.setBounds(0, 450, 400, 30);

        frame.add(name);
        frame.add(id);
        frame.add(phone);
        frame.add(email);

        frame.add(tfname);
        frame.add(tfid);
        frame.add(tfphone);
        frame.add(tfemail);

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
            String query = "SELECT * FROM VendorInfo";
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
            String name1 = tfname.getText();
            String id1 = tfid.getText();
            String phone1 = tfphone.getText();
            String email1 = tfemail.getText();

            try {
                String url = "jdbc:mysql://localhost:3306/stock";
                Connection connection = DriverManager.getConnection(url, "root", "Mukesh*12");

                String insertQuery = "INSERT INTO VendorInfo (Name, ID, PhoneNo, Email) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

                preparedStatement.setString(1, name1);
                preparedStatement.setString(2, id1);
                preparedStatement.setString(3, phone1);
                preparedStatement.setString(4, email1);

                int rowsAffected = preparedStatement.executeUpdate();

                tfname.setText("");
                tfid.setText("");
                tfphone.setText("");
                tfemail.setText("");
                tfname.requestFocus();// cursor will be blinked on name text field after clicking on add button

                if (rowsAffected > 0) {
                    msg.setForeground(Color.GREEN);
                    msg.setText("Vendor added successfully!");
                } else {
                    msg.setForeground(Color.RED);
                    msg.setText("Failed to add vendor!");
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
            String name1 = tfname.getText();
            String id1 = tfid.getText();
            String phone1 = tfphone.getText();
            String email1 = tfemail.getText();

            // Check if ID field is empty
            if (id1.isEmpty()) {
                msg.setForeground(Color.RED);
                msg.setText("Please enter a valid ID to edit!");
                return;
            }

            try {
                String url = "jdbc:mysql://localhost:3306/stock";
                Connection connection = DriverManager.getConnection(url, "root", "Mukesh*12");

                String updateQuery = "UPDATE VendorInfo SET Name = ?, PhoneNo = ?, Email = ? WHERE ID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

                preparedStatement.setString(1, name1);
                preparedStatement.setString(2, phone1);
                preparedStatement.setString(3, email1);
                preparedStatement.setString(4, id1);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    msg.setForeground(Color.GREEN);
                    msg.setText("Vendor updated successfully!");
                } else {
                    msg.setForeground(Color.RED);
                    msg.setText("Failed to update vendor! ID may not exist.");
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
            String id1 = tfid.getText();

            // Check if ID field is empty
            if (id1.isEmpty()) {
                msg.setForeground(Color.RED);
                msg.setText("Please enter a valid ID to delete!");
                return;
            }

            try {
                String url = "jdbc:mysql://localhost:3306/stock";
                Connection connection = DriverManager.getConnection(url, "root", "Mukesh*12");

                String deleteQuery = "DELETE FROM VendorInfo WHERE ID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                preparedStatement.setString(1, id1);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    msg.setForeground(Color.GREEN);
                    msg.setText("Vendor deleted successfully!");
                } else {
                    msg.setForeground(Color.RED);
                    msg.setText("Failed to delete vendor! ID may not exist.");
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
            tfname.setText("");
            tfid.setText("");
            tfphone.setText("");
            tfemail.setText("");
            msg.setText("");
        }
    }


}

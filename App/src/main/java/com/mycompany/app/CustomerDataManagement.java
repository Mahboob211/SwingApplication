package com.mycompany.app;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class CustomerDataManagement extends JFrame{
    
    private DatabaseConnection dbConnection;
    private JList<String> customerList;
     private DefaultListModel<String> listModel;
    private JTextField address1Field,address2Field,address3Field,cityField,postalCodeField;
    private JButton addButton, modifyButton,deleteButton;
    private AddModifyDeleteCustomerDataDaoImpl addressOperations;
    
    public CustomerDataManagement(){
      try {
           dbConnection = new DatabaseConnection();
           addressOperations = new AddModifyDeleteCustomerDataDaoImpl();
        } catch (SQLException e) {
            e.printStackTrace();
        }
     setTitle("Customer Data Management Systems");
     setLayout(new BorderLayout()); 
     
     
        listModel = new DefaultListModel<>();
        customerList = new JList<>(listModel);  // Use the model to display customer names
        JScrollPane listScrollPane = new JScrollPane(customerList);
        add(listScrollPane, BorderLayout.WEST);  // Place customer list on the left side

        customerList.addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
        String selectedCustomer = customerList.getSelectedValue();
        if (selectedCustomer != null) {
            loadCustomerDetails(selectedCustomer);
        }
        }
        });


     
        JPanel addressPanel = new JPanel();
        addressPanel.setLayout(new GridLayout(5, 2));

        addressPanel.add(new JLabel("Address Line 1:"));
        address1Field = new JTextField();
        addressPanel.add(address1Field);

        addressPanel.add(new JLabel("Address Line 2:"));
        address2Field = new JTextField();
        addressPanel.add(address2Field);

        addressPanel.add(new JLabel("Address Line 3:"));
        address3Field = new JTextField();
        addressPanel.add(address3Field);

        addressPanel.add(new JLabel("City:"));
        cityField = new JTextField();
        addressPanel.add(cityField);

        addressPanel.add(new JLabel("Postal Code:"));
        postalCodeField = new JTextField();
        addressPanel.add(postalCodeField);

        add(addressPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        modifyButton = new JButton("Modify");
        deleteButton = new JButton("Delete");
        buttonPanel.add(addButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               CustomerDataDto customerDataDto = new CustomerDataDto(
               address1Field.getText(),
               address2Field.getText(),
               address3Field.getText(),
               cityField.getText(),
               postalCodeField.getText(),
               customerList        
               );
               addressOperations.addAddress(customerDataDto,dbConnection);
               clearAddressFields();
            }
        });
        modifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               CustomerDataDto customerDataDto = new CustomerDataDto(
               address1Field.getText(),
               address2Field.getText(),
               address3Field.getText(),
               cityField.getText(),
               postalCodeField.getText(),
               customerList        
               );
                addressOperations.modifyAddress(customerDataDto,dbConnection);
                clearAddressFields();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               CustomerDataDto customerDataDto = new CustomerDataDto(
               address1Field.getText(),
               address2Field.getText(),
               address3Field.getText(),
               cityField.getText(),
               postalCodeField.getText(),
               customerList        
               );
                addressOperations.deleteAddress(customerDataDto,dbConnection);
                clearAddressFields();
            }
        });
        addressOperations.addCustomersToList(dbConnection,listModel);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public JList<String> getCustomerList() {
        return customerList;
    }

    public JTextField getAddress1Field() {
        return address1Field;
    }

    public JTextField getAddress2Field() {
        return address2Field;
    }

    public JTextField getAddress3Field() {
        return address3Field;
    }

    public JTextField getCityField() {
        return cityField;
    }

    public JTextField getPostalCodeField() {
        return postalCodeField;
    }
    
   
    
    public static void main(String[] args) {
       new CustomerDataManagement();
    }
    
    private void clearAddressFields() {
    address1Field.setText("");
    address2Field.setText("");
    address3Field.setText("");
    cityField.setText("");
    postalCodeField.setText("");
}
    
    
    public void loadCustomerDetails(String customerName) {
    String sql = "SELECT * FROM customers WHERE FullName = ?"; // Assuming 'FullName' is the column for the customer's name

    try {
        PreparedStatement stmt = dbConnection.getConnection().prepareStatement(sql);
        stmt.setString(1, customerName);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String address1 = rs.getString("Address1");
            String address2 = rs.getString("Address2");
            String address3 = rs.getString("Address3");
            String city = rs.getString("City");
            String postalCode = rs.getString("PostalCode");
            address1Field.setText(address1);
            address2Field.setText(address2);
            address3Field.setText(address3);
            cityField.setText(city);
            postalCodeField.setText(postalCode);
        } else {
            JOptionPane.showMessageDialog(this, "Customer not found!");
        }

        rs.close();
        stmt.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error loading customer details: " + e.getMessage());
    }
}

     
    }
    
    


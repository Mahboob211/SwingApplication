package com.mycompany.app;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class AddModifyDeleteCustomerDataDaoImpl implements AddModifyDeleteCustomerDataDao {

    private CustomerDataManagement customerDataManagement;

    
    public AddModifyDeleteCustomerDataDaoImpl(){}
    
    public AddModifyDeleteCustomerDataDaoImpl(CustomerDataManagement customerDataManagement){
        this.customerDataManagement = customerDataManagement;
    }
    
    
    @Override
    public void addAddress(CustomerDataDto customerDataDto,DatabaseConnection dbConnection){

    String address1 = customerDataDto.getAddress1();
    String address2 = customerDataDto.getAddress2();
    String address3 = customerDataDto.getAddress3();
    String city = customerDataDto.getCity();
    String postalCode = customerDataDto.getPostalCode();

    
    if (!validatePostalCode(postalCode)) {
        JOptionPane.showMessageDialog(customerDataManagement, "Invalid Postal Code");
        return;
    }
    try {
        String selectedCustomer = customerDataDto.getCustomerList().getSelectedValue();
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(customerDataManagement, "Please select a customer first.");
            return;
        }

        int customerId = getCustomerIdByName(selectedCustomer,  dbConnection);
        int addressCount = getAddressCountByCustomerId(customerId,dbConnection);

        if (addressCount >= 3) {
            JOptionPane.showMessageDialog(customerDataManagement, "Customer already has 3 addresses.");
            return;
        }

        String sql = "INSERT INTO Customers (CustomerID, Address1, Address2, Address3, City, PostalCode) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = dbConnection.getConnection().prepareStatement(sql);
        stmt.setInt(1, customerId);
        stmt.setString(2, address1);
        stmt.setString(3, address2);
        stmt.setString(4, address3);
        stmt.setString(5, city);
        stmt.setString(6, postalCode);
        stmt.executeUpdate();

        JOptionPane.showMessageDialog(customerDataManagement, "Address added successfully!");
       
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(customerDataManagement, "Error adding address: " + e.getMessage());
    }
}

    @Override
    public void modifyAddress(CustomerDataDto customerDataDto, DatabaseConnection dbConnection) {
        String address1 = customerDataDto.getAddress1();
        String address2 = customerDataDto.getAddress2();
        String address3 = customerDataDto.getAddress3();
        String city = customerDataDto.getCity();
        String postalCode = customerDataDto.getPostalCode();

    if (!validatePostalCode(postalCode)) {
        JOptionPane.showMessageDialog(customerDataManagement, "Invalid Postal Code");
        return;
    }

    try {
        String selectedCustomer = customerDataDto.getCustomerList().getSelectedValue();
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(customerDataManagement, "Please select a customer first.");
            return;
        }

        int customerId = getCustomerIdByName(selectedCustomer,dbConnection);
        int addressCount = getAddressCountByCustomerId(customerId,dbConnection);
        if (addressCount == 0) {
            JOptionPane.showMessageDialog(customerDataManagement, "This customer has no addresses to modify.");
            return;
        }

        String selectedAddress = getSelectedAddressForCustomer(customerId,dbConnection);
        if (selectedAddress == null) {
            JOptionPane.showMessageDialog(customerDataManagement, "Please select an address to modify.");
            return;
        }

        String sql = "UPDATE Customers SET Address1 = ?, Address2 = ?, Address3 = ?, City = ?, PostalCode = ? WHERE CustomerID = ? AND Address1 = ?";
        PreparedStatement stmt = dbConnection.getConnection().prepareStatement(sql);
        stmt.setString(1, address1);
        stmt.setString(2, address2);
        stmt.setString(3, address3);
        stmt.setString(4, city);
        stmt.setString(5, postalCode);
        stmt.setInt(6, customerId);
        stmt.setString(7, selectedAddress);
        stmt.executeUpdate();

        JOptionPane.showMessageDialog(customerDataManagement, "Address modified successfully!");
        

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(customerDataManagement, "Error modifying address: " + e.getMessage());
    }
}

    @Override 
    public void deleteAddress(CustomerDataDto customerDataDto, DatabaseConnection dbConnection) {
    try {
        String selectedCustomer = customerDataDto.getCustomerList().getSelectedValue();
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(customerDataManagement, "Please select a customer first.");
            return;
        }

        int customerId = getCustomerIdByName(selectedCustomer, dbConnection);
        int addressCount = getAddressCountByCustomerId(customerId,dbConnection);

        if (addressCount == 0) {
            JOptionPane.showMessageDialog(customerDataManagement, "This customer has no addresses to delete.");
            return;
        }

        String selectedAddress = getSelectedAddressForCustomer(customerId, dbConnection);
        if (selectedAddress == null) {
            JOptionPane.showMessageDialog(customerDataManagement, "Please select an address to delete.");
            return;
        }

        String sql = "DELETE FROM Customers WHERE CustomerID = ? AND Address1 = ?";
        PreparedStatement stmt = dbConnection.getConnection().prepareStatement(sql);
        stmt.setInt(1, customerId);
        stmt.setString(2, selectedAddress);
        stmt.executeUpdate();

        JOptionPane.showMessageDialog(customerDataManagement, "Address deleted successfully!");
         

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(customerDataManagement, "Error deleting address: " + e.getMessage());
    }
}


    @Override
    public boolean validatePostalCode(String postalCode) {
      String regex = "^[0-9]{5}$";
        return postalCode.matches(regex);
    }

    @Override
    public int getCustomerIdByName(String name,DatabaseConnection dbConnection) throws SQLException {
            String sql = "SELECT CustomerID FROM Customers WHERE FullName = ?";
            PreparedStatement stmt = dbConnection.getConnection().prepareStatement(sql);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("CustomerID");
            }
            return -1;
    }

@Override
public int getAddressCountByCustomerId(int customerId, DatabaseConnection dbConnection) throws SQLException {
    String sql = "SELECT COUNT(*) FROM Customers WHERE CustomerID = ?";
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        stmt = dbConnection.getConnection().prepareStatement(sql);
        stmt.setInt(1, customerId); 
        rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);  
        } else {
            return 0; 
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw e;  
    } finally {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


    @Override
    public String getSelectedAddressForCustomer(int customerId,DatabaseConnection dbConnection) throws SQLException {
        String sql = "SELECT Address1 FROM Customers WHERE CustomerID = ?";
        PreparedStatement stmt = dbConnection.getConnection().prepareStatement(sql);
        stmt.setInt(1, customerId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getString("Address1");
        }
        return null; 
}

   
    @Override
    public void addCustomersToList(DatabaseConnection dbConnection,DefaultListModel<String> listModel) {
   

    try {
        String sql = "SELECT FullName FROM customers"; 

        Statement stmt = dbConnection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            String customerName = rs.getString("fullName"); 
            listModel.addElement(customerName); 
        }

        rs.close();
        stmt.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(customerDataManagement, "Error loading customer data: " + e.getMessage());
    }
}   
 



    
   
}

    
 

    


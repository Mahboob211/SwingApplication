package com.mycompany.app;

import java.sql.SQLException;
import javax.swing.DefaultListModel;

public interface AddModifyDeleteCustomerDataDao {
   
   public void addAddress(CustomerDataDto customerDataDto,DatabaseConnection dbConnection);
   public void modifyAddress(CustomerDataDto customerDataDto,DatabaseConnection dbConnection);
   public void deleteAddress(CustomerDataDto customerDataDto,DatabaseConnection dbConnection); 
   public boolean validatePostalCode(String postalCode);
   public int getCustomerIdByName(String name,DatabaseConnection dbConnection) throws SQLException;
   public int getAddressCountByCustomerId(int customerId,DatabaseConnection dbConnection) throws SQLException;
   public String getSelectedAddressForCustomer(int customerId,DatabaseConnection dbConnection) throws SQLException;
   public void addCustomersToList(DatabaseConnection dbConnection,DefaultListModel<String> listModel); 
  
}

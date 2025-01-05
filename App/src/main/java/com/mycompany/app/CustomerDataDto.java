package com.mycompany.app;

import javax.swing.JList;

public class CustomerDataDto {
   
    private String address1;
    private String address2;
    private String address3;
    private String city;
    private String postalCode;
    private JList<String> customerList;

    
    public CustomerDataDto(String address1,String address2,String address3,String city,
            String postalCode,JList<String> customerList){
    
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.city = city;
        this.postalCode = postalCode;
        this.customerList = customerList;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getAddress3() {
        return address3;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public JList<String> getCustomerList() {
        return customerList;
    }
    
    
    
}

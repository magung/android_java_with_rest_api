package com.magung.myapp;

public class Customer {
    String idcustomer, namacustomer, telpcustomer;

    public Customer(String idcustomer, String namacustomer, String telpcustomer) {
        this.idcustomer = idcustomer;
        this.namacustomer = namacustomer;
        this.telpcustomer = telpcustomer;
    }

    public String getIdcustomer() {
        return idcustomer;
    }

    public void setIdcustomer(String idcustomer) {
        this.idcustomer = idcustomer;
    }

    public String getNamacustomer() {
        return namacustomer;
    }

    public void setNamacustomer(String namacustomer) {
        this.namacustomer = namacustomer;
    }

    public String getTelpcustomer() {
        return telpcustomer;
    }

    public void setTelpcustomer(String telpcustomer) {
        this.telpcustomer = telpcustomer;
    }
}

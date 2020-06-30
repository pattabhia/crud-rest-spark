package com.pattabhi.model;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @Column (name="address_email")
    private String email;

    private String city;

    private String pincode;

    @Column(name = "address_desc")
    private String addressText;

    @OneToOne(mappedBy = "address")
    private User user;

    public Address() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddressText() {
        return addressText;
    }

    public void setAddressText(String addressText) {
        this.addressText = addressText;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

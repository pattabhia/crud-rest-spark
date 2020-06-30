package com.pattabhi.model;

public class UserStatus extends User {

    private String status;

    public UserStatus() {
    }

    public UserStatus(User user, String status) {
        super();
        this.status = status;
        this.setDateOfBirth(user.getDateOfBirth());
        this.setEmail(user.getEmail());
        this.setName(user.getName());
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

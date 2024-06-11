package com.DatabaseModel;

class Person {
    private int id;
    private String firstName;
    private String lastName;
    private String phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}

public class Employee extends Person {
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + getId() +
                ", first name='" + getFirstName() + '\'' +
                ", last name='" + getLastName() + '\'' +
                ", phone='" + getPhone() + '\'' +
                '}';
    }
}


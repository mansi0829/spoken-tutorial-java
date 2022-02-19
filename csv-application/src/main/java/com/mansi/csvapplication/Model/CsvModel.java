package com.mansi.csvapplication.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity                 //table mapped with entity
@Table(name = "Data")   //table annotation indicates database table

public class CsvModel {
    @Id                 //id to represent primary key in table
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phonenumber")
    private long phonenumber;

    public CsvModel() {

    }
    public CsvModel(long id, String name, String  email, long phonenumber) {
        //constructor
        this.id = id;
        this.name = name;
        this.email = email;
        this.phonenumber = phonenumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(long phonenumber) {
        this.phonenumber = phonenumber;
    }

    @Override
    public String toString() {
        return "Tutorial [id=" + id + ", name=" + name + ", email=" + email + ", phonenumber=" + phonenumber + "]";
    }

}
